package liquid.service;

import liquid.persistence.domain.ContainerSubtype;
import liquid.persistence.repository.ContainerSubtypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by redbrick9 on 5/4/14.
 */
@Service
public class ContainerSubtypeService {

    @Autowired
    private ContainerSubtypeRepository containerSubtypeRepository;

    public ContainerSubtype save(ContainerSubtype containerSubtype) {
        return containerSubtypeRepository.save(containerSubtype);
    }

    public ContainerSubtype find(long id) {
        return containerSubtypeRepository.findOne(id);
    }

    public Iterable<ContainerSubtype> findAll() {
        return containerSubtypeRepository.findAll();
    }

    public Iterable<ContainerSubtype> findEnabled() {
        return containerSubtypeRepository.findByState(0);
    }

    public Iterable<ContainerSubtype> findByContainerType(int containerType) {
        return containerSubtypeRepository.findByContainerType(containerType);
    }
}
