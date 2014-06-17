package liquid.service;

import liquid.excel.AbstractExcelService;
import liquid.excel.RowMapper;
import liquid.metadata.ContainerStatus;
import liquid.persistence.domain.*;
import liquid.persistence.repository.ContainerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
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

    public Page<ContainerEntity> findAll(final long containerSubtypeId, final long ownerId, final long yardId, final Pageable pageable) {
        Specifications<ContainerEntity> specifications = where(new Specification<ContainerEntity>() {
            @Override
            public Predicate toPredicate(Root<ContainerEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.equal(root.get(ContainerEntity_.subtype), ContainerSubtypeEntity.newInstance(ContainerSubtypeEntity.class, containerSubtypeId));
            }
        });

        // status
        specifications = specifications.and(new Specification<ContainerEntity>() {
            @Override
            public Predicate toPredicate(Root<ContainerEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.equal(root.get(ContainerEntity_.status), ContainerStatus.IN_STOCK.getValue());
            }
        });

        // owner
        if (ownerId > 0L) specifications = specifications.and(new Specification<ContainerEntity>() {
            @Override
            public Predicate toPredicate(Root<ContainerEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                return builder.equal(root.get(ContainerEntity_.owner), ServiceProviderEntity.newInstance(ServiceProviderEntity.class, ownerId));
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

    public void save(List<ContainerEntity> containers) {
        containerRepository.save(containers);
    }

    public void importExcel(String fileName) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(fileName)) {
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
                                    containerSubtypeId = 12L;
                                    break;
                                case "40`GP":
                                    containerSubtypeId = 11L;
                                    break;
                                case "40`RH": // TODO: new
                                    containerSubtypeId = 13L;
                                    break;
                                case "20`GP":
                                    containerSubtypeId = 8L;
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
                            entity.setOwner(ServiceProviderEntity.newInstance(ServiceProviderEntity.class, serviceProviderId));
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
//            writeToFile(containers);
        }
    }

    private void writeToFile(List<ContainerEntity> containers) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream("/tmp/container.csv")) {
            for (ContainerEntity container : containers) {
                outputStream.write(String.format("%s,%s,%s,%s,%s,%s,%s\n",
                        container.getBicCode(),
                        container.getSubtype(),
                        container.getOwner(),
                        container.getYard(),
                        container.getMoveInTime(),
                        container.getStatus(),
                        container.getComment()).
                        getBytes(Charset.forName("UTF-8")));
            }
        }
    }
}
