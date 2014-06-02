package liquid.controller;

import liquid.domain.Container;
import liquid.facade.ContainerFacade;
import liquid.metadata.ContainerCap;
import liquid.metadata.ContainerStatus;
import liquid.metadata.ContainerType;
import liquid.metadata.LocationType;
import liquid.persistence.domain.ContainerEntity;
import liquid.persistence.domain.ContainerSubtypeEntity;
import liquid.persistence.domain.LocationEntity;
import liquid.persistence.domain.ServiceProviderEntity;
import liquid.service.ContainerService;
import liquid.service.ContainerSubtypeService;
import liquid.service.LocationService;
import liquid.service.ServiceItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/3/13
 * Time: 3:59 PM
 */
@Controller
@RequestMapping("/container")
public class ContainerController {
    private static final Logger logger = LoggerFactory.getLogger(ContainerController.class);

    @Autowired
    private ContainerService containerService;

    @Autowired
    private ContainerFacade containerFacade;

    @Autowired
    private LocationService locationService;

    @Autowired
    private ContainerSubtypeService containerSubtypeService;

    @Autowired
    private ServiceItemService serviceItemService;

    @ModelAttribute("containers")
    public Iterable<ContainerEntity> populateContainers() {
        return containerService.findAll();
    }

    @ModelAttribute("container")
    public Container populateContainer() {
        return new Container();
    }

    @ModelAttribute("statusArray")
    public ContainerStatus[] populateStatus() {
        return ContainerStatus.values();
    }

    @ModelAttribute("containerTypeMap")
    public Map<Integer, String> populateContainerTypeMap() {
        return ContainerCap.toMap();
    }

    @ModelAttribute("containerTypes")
    public ContainerCap[] populateContainerTypes() {
        return ContainerCap.values();
    }

    @ModelAttribute("locations")
    public List<LocationEntity> populateLocations() {
        return locationService.findByType(LocationType.YARD.getType());
    }

    @ModelAttribute("containerSubtypes")
    public Iterable<ContainerSubtypeEntity> populateContainerSubtypes() {
        return containerSubtypeService.findByContainerType(ContainerType.SELF);
    }

    @ModelAttribute("owners")
    public Iterable<ServiceProviderEntity> populateOwners() {
        return serviceItemService.findContainerOwners();
    }

    @RequestMapping(method = RequestMethod.GET, params = "number")
    public String init(@RequestParam int number, Model model) {
        int size = 20;
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
        Page<ContainerEntity> page = containerService.findAll(pageRequest);
        model.addAttribute("page", page);
        return "container/list";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute Container container,
                      BindingResult bindingResult, Principal principal) {
        logger.debug("container: {}", container);

        if (bindingResult.hasErrors()) {
            return "container/list";
        } else {
            containerFacade.enter(container);
            return "redirect:/container";
        }
    }
}
