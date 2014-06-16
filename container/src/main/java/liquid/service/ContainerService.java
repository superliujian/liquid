package liquid.service;

import liquid.excel.AbstractExcelService;
import liquid.excel.CellTranslator;
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
import java.io.IOException;
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

    private AbstractExcelService<ContainerEntity> abstractExcelService;

    public void setAbstractExcelService(AbstractExcelService<ContainerEntity> abstractExcelService) {
        this.abstractExcelService = abstractExcelService;
    }

    public Iterable<ContainerEntity> findAll() {
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
            List<ContainerEntity> containers = abstractExcelService.extract(inputStream, ContainerEntity.class, new CellTranslator<ContainerEntity>() {
                @Override
                public boolean translate(ContainerEntity entity, String r, Object value) {
                    switch (r.substring(0, 1)) {
                        case "A": // No.
                            return true;
                        case "B": // biccode
                            if (null == value) return false;
                            String string = value.toString().trim();
                            if (string.length() == 0) return false;
                            else {
                                entity.setBicCode(string);
                            }
                            return true;
                        case "C": // container subtype
                            if (null == value) return false;
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
                            if (null == containerSubtypeId) return false;
                            entity.setSubtype(ContainerSubtypeEntity.newInstance(ContainerSubtypeEntity.class, containerSubtypeId));
                            return true;
                        case "D": // owner
                            if (null == value) return false;
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
                            if (null == serviceProviderId) return false;
                            entity.setOwner(ServiceProviderEntity.newInstance(ServiceProviderEntity.class, serviceProviderId));
                            return true;
                        case "E": // yard
                            if (null == value) return false;
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
                            if (null == locationId) return false;
                            entity.setYard(LocationEntity.newInstance(LocationEntity.class, locationId));
                            return true;
                        case "F": // move in date
                            if (null == value) return true;
                            if (value instanceof Date) entity.setMoveInTime((Date) value);
                            return true;
                        case "G": // comment
                            if (null == value) return true;
                            entity.setComment(value.toString());
                            return true;
                        case "H": // is Loaded
                            return true;
                        case "I": // Load Date
                            return true;
                        case "J": // move out date
                            return true;
                        case "K": // goods
                            return true;
                        case "L": // destination
                            return true;
                        case "M": // customer
                            return true;
                        case "N": // consignee
                            return true;
                        case "O": // days in yard
                            return true;
                        case "P": // fee for move in
                            return true;
                        case "Q": // self no
                            return true;
                        case "R": // fee for move out
                            return true;
                        case "S": // self no
                            return true;
                        default:
                            return true;
                    }
                }
            });
            System.out.println(containers.size());
        }
    }
}
