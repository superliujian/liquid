package liquid.container.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import liquid.container.domain.ContainerStatus;
import liquid.container.domain.ExcelFileInfo;
import liquid.container.persistence.domain.ContainerEntity;
import liquid.container.persistence.domain.ContainerEntity_;
import liquid.container.persistence.domain.ContainerSubtypeEntity;
import liquid.container.persistence.repository.ContainerRepository;
import liquid.excel.AbstractExcelService;
import liquid.excel.RowMapper;
import liquid.operation.domain.ServiceProvider;
import liquid.persistence.domain.LocationEntity;
import liquid.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.*;
import java.util.Date;
import java.util.List;

import static org.springframework.data.jpa.domain.Specifications.where;


/**
 * TODO: Comments.
 * User: tao
 * Date: 10/12/13
 * Time: 1:11 AM
 */
@Service
public class ContainerService {
    private static final Logger logger = LoggerFactory.getLogger(ContainerService.class);

    @Autowired
    private Environment env;

    @Autowired
    private ContainerRepository containerRepository;

    @Autowired
    private LocationService locationService;

    @Autowired
    private AbstractExcelService<ContainerEntity> abstractExcelService;

    public List<ContainerEntity> findAll() {
        return containerRepository.findAll();
    }

    public Iterable<ContainerEntity> findAllInStock(int type) {
        return containerRepository.findByStatusAndSubtype(ContainerStatus.IN_STOCK.getValue(), type);
    }

    public Page<ContainerEntity> findAll(Pageable pageable) {
        return containerRepository.findAll(pageable);
    }

    public List<ContainerEntity> findBicCode(String bicCode) {
        return containerRepository.findByBicCode(bicCode);
    }

    public Page<ContainerEntity> findAll(final long containerSubtypeId, final long ownerId, final long yardId, final Pageable pageable) {
        Specifications<ContainerEntity> specifications = where(new Specification<ContainerEntity>() {
            @Override
            public Predicate toPredicate(Root<ContainerEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.equal(root.get(ContainerEntity_.status), ContainerStatus.IN_STOCK.getValue());
            }
        });

        if (containerSubtypeId > 0L) specifications = specifications.and(new Specification<ContainerEntity>() {
            @Override
            public Predicate toPredicate(Root<ContainerEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.equal(root.get(ContainerEntity_.subtype), ContainerSubtypeEntity.newInstance(ContainerSubtypeEntity.class, containerSubtypeId));
            }
        });

        // owner
        if (ownerId > 0L) specifications = specifications.and(new Specification<ContainerEntity>() {
            @Override
            public Predicate toPredicate(Root<ContainerEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.equal(root.get(ContainerEntity_.owner), ServiceProvider.newInstance(ServiceProvider.class, ownerId));
            }
        });

        // yard
        if (yardId > 0L) specifications = specifications.and(new Specification<ContainerEntity>() {
            @Override
            public Predicate toPredicate(Root<ContainerEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.equal(root.get(ContainerEntity_.yard), LocationEntity.newInstance(LocationEntity.class, yardId));
            }
        });

        return containerRepository.findAll(specifications, pageable);
    }

    public ContainerEntity find(long id) {
        ContainerEntity containerEntity = containerRepository.findOne(id);
        return containerEntity;
    }

    public void save(ContainerEntity containerEntity) {
        containerRepository.save(containerEntity);
    }

    public Iterable<ContainerEntity> save(List<ContainerEntity> containers) {
        return containerRepository.save(containers);
    }

    public void importExcel(String fileName) throws IOException {
        ExcelFileInfo excelFileInfo = find(fileName);
        if (excelFileInfo.getState().equals(ExcelFileInfo.State.IMPORTED) || excelFileInfo.getState().equals(ExcelFileInfo.State.IMPORTING))
            return;

        excelFileInfo.setModifiedDate(new Date());
        excelFileInfo.setState(ExcelFileInfo.State.IMPORTING);
        writeMetadata(excelFileInfo);

        try (FileInputStream inputStream = new FileInputStream(env.getProperty("upload.dir", "/opt/liquid/upload/") + fileName)) {
            List<ContainerEntity> containers = abstractExcelService.extract(inputStream, ContainerEntity.class, new RowMapper<ContainerEntity>() {
                @Override
                public void translate(ContainerEntity entity, String r, Object value) {
                    switch (r.substring(0, 1)) {
                        case "A": // No.
                            break;
                        case "B": // biccode
                            if (null == value) break;
                            String string = value.toString().trim();
                            if (string.length() == 0) break;
                            entity.setBicCode(string);
                            break;
                        case "C": // container subtype
                            if (null == value) break;
                            string = value.toString().trim();
                            // TODO:
                            Long containerSubtypeId = null;
                            switch (string) {
                                case "40`HQ":
                                    containerSubtypeId = 11L;
                                    break;
                                case "40`GP":
                                    containerSubtypeId = 10L;
                                    break;
                                case "40`RH": // TODO: new
                                    containerSubtypeId = 12L;
                                    break;
                                case "20`GP":
                                    containerSubtypeId = 7L;
                                    break;
                                default:
                                    logger.warn("No matched container subtype {}. ", string);
                                    break;
                            }
                            if (null == containerSubtypeId) break;
                            entity.setSubtype(ContainerSubtypeEntity.newInstance(ContainerSubtypeEntity.class, containerSubtypeId));
                            break;
                        case "D": // owner
                            if (null == value) break;
                            string = value.toString().trim();
                            Long serviceProviderId = null;
                            switch (string) {
                                case "长荣箱":
                                    serviceProviderId = 5L;
                                    break;
                                case "新海丰":
                                    serviceProviderId = 49L;
                                    break;
                                case "MSK箱":
                                    serviceProviderId = 34L;
                                    break;
                                case "中远箱":
                                    serviceProviderId = 15L;
                                    break;
                                case "OOCL箱":
                                    serviceProviderId = 28L;
                                    break;
                                case "南青箱":
                                    serviceProviderId = 24L;
                                    break;
                                case "HPL箱":
                                    serviceProviderId = 42L;
                                    break;
                                case "安通箱": // TODO: new
                                    serviceProviderId = 51L;
                                    break;
                                case "华联通箱": // TODO: new
                                    serviceProviderId = 52L;
                                    break;
                                case "穗明达": // TODO: new
                                    serviceProviderId = 53L;
                                    break;
                                case "吉达箱": // TODO: new
                                    serviceProviderId = 54L;
                                    break;
                                case "轮迪箱": // TODO: new
                                    serviceProviderId = 55L;
                                    break;
                                case "中海箱":
                                    serviceProviderId = 25L;
                                    break;
                                case "浦东箱":
                                    serviceProviderId = 56L;
                                    break;
                                default:
                                    logger.warn("No matched service provider {}. ", string);
                                    break;
                            }
                            if (null == serviceProviderId) break;
                            entity.setOwner(ServiceProvider.newInstance(ServiceProvider.class, serviceProviderId));
                            break;
                        case "E": // yard
                            if (null == value) break;
                            string = value.toString().trim();
                            Long locationId = null;
                            switch (string) {
                                case "阳都堆场":
                                    locationId = 18L;
                                    break;
                                case "阳都2号":
                                    locationId = 19L;
                                    break;
                                case "阳都3号":
                                    locationId = 20L;
                                    break;
                                case "阳都4号":
                                    locationId = 21L;
                                    break;
                                case "阳都5号":
                                    locationId = 25L;
                                    break;
                                case "昆明南站": // TODO: new
                                    locationId = 26L;
                                    break;
                                case "南站王家营": // TODO: new
                                    locationId = 27L;
                                    break;
                                default:
                                    logger.warn("No matched yard {}. ", string);
                                    break;
                            }
                            if (null == locationId) break;
                            entity.setYard(LocationEntity.newInstance(LocationEntity.class, locationId));
                            break;
                        case "F": // move in date
                            if (null == value) break;
                            if (value instanceof Date) entity.setMoveInTime((Date) value);
                            break;
                        case "G": // comment
                            if (null == value) break;
                            entity.setComment(value.toString());
                            break;
                        case "H": // is Loaded
                            break;
                        case "I": // Load Date
                            break;
                        case "J": // move out date
                            break;
                        case "K": // goods
                            break;
                        case "L": // destination
                            break;
                        case "M": // customer
                            break;
                        case "N": // consignee
                            break;
                        case "O": // days in yard
                            break;
                        case "P": // fee for move in
                            break;
                        case "Q": // self no
                            break;
                        case "R": // fee for move out
                            break;
                        case "S": // self no
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public Boolean validate(ContainerEntity entity) {
                    if (null == entity) return Boolean.FALSE;
                    if (null == entity.getBicCode()) return Boolean.FALSE;
                    if (null == entity.getSubtype()) return Boolean.FALSE;
                    if (null == entity.getOwner()) return Boolean.FALSE;
                    if (null == entity.getYard()) return Boolean.FALSE;
                    return Boolean.TRUE;
                }
            });
            logger.info("Total number of containers is {}.", containers.size());

            List<ContainerEntity> oldOnes = findAll();
            containers.removeAll(oldOnes);
            containerRepository.save(containers);

            excelFileInfo.setModifiedDate(new Date());
            excelFileInfo.setState(ExcelFileInfo.State.IMPORTED);
            writeMetadata(excelFileInfo);
        }
    }

    public void writeToFile(String fileName, byte[] bytes) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(env.getProperty("upload.dir", "/opt/liquid/upload/") + fileName)) {
            fos.write(bytes);
        }

        ExcelFileInfo excelFileInfo = new ExcelFileInfo();
        excelFileInfo.setName(fileName);
        excelFileInfo.setModifiedDate(new Date());
        excelFileInfo.setState(ExcelFileInfo.State.UPLOADED);
        writeMetadata(excelFileInfo);
    }

    public void writeMetadata(ExcelFileInfo excelFileInfo) throws IOException {
        ExcelFileInfo[] excelFileInfos = readMetadata();
        boolean isExist = false;
        for (ExcelFileInfo fileInfo : excelFileInfos) {
            if (fileInfo.getName().equals(excelFileInfo.getName())) {
                fileInfo.setModifiedDate(excelFileInfo.getModifiedDate());
                fileInfo.setState(excelFileInfo.getState());
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            ExcelFileInfo[] newOnes = new ExcelFileInfo[excelFileInfos.length + 1];
            System.arraycopy(excelFileInfos, 0, newOnes, 0, excelFileInfos.length);
            newOnes[excelFileInfos.length] = excelFileInfo;
            writeMetadata(newOnes);
        } else {
            writeMetadata(excelFileInfos);
        }
    }

    public void writeMetadata(ExcelFileInfo[] excelFileInfos) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        try (OutputStream out = new FileOutputStream(env.getProperty("upload.dir", "/opt/liquid/upload/") + "metadata.json")) {
            mapper.writeValue(out, excelFileInfos);
        }
    }

    public ExcelFileInfo[] readMetadata() throws IOException {
        File file = new File(env.getProperty("upload.dir", "/opt/liquid/upload/") + "metadata.json");
        if (!file.exists()) return new ExcelFileInfo[0];

        try (InputStream in = new FileInputStream(env.getProperty("upload.dir", "/opt/liquid/upload/") + "metadata.json")) {
            ObjectMapper mapper = new ObjectMapper();
            byte[] bytes = new byte[in.available()];
            in.read(bytes);
            return mapper.readValue(bytes, ExcelFileInfo[].class);
        }
    }

    public ExcelFileInfo find(String fileName) throws IOException {
        File file = new File(env.getProperty("upload.dir", "/opt/liquid/upload/") + "metadata.json");
        if (!file.exists()) return null;

        try (InputStream in = new FileInputStream(env.getProperty("upload.dir", "/opt/liquid/upload/") + "metadata.json")) {
            ObjectMapper mapper = new ObjectMapper();
            byte[] bytes = new byte[in.available()];
            in.read(bytes);
            ExcelFileInfo[] excelFileInfos = mapper.readValue(bytes, ExcelFileInfo[].class);
            for (ExcelFileInfo excelFileInfo : excelFileInfos) {
                if (fileName.equals(excelFileInfo.getName())) {
                    excelFileInfo.setModifiedDate(excelFileInfo.getModifiedDate());
                    excelFileInfo.setState(excelFileInfo.getState());
                    return excelFileInfo;
                }
            }
        }
        return null;
    }

    @Transactional("transactionManager")
    public Iterable<ContainerEntity> findByBicCodeLike(String bicCode) {
        Iterable<ContainerEntity> containers = containerRepository.findByBicCodeLike("%" + bicCode + "%");
        return containers;
    }

    public Page<ContainerEntity> findByBicCodeLike(String bicCode, Pageable pageable) {
        Page<ContainerEntity> containers = containerRepository.findByBicCodeLike("%" + bicCode + "%", pageable);
        return containers;
    }

}
