package liquid.controller;

import liquid.metadata.ContainerCap;
import liquid.metadata.LocationType;
import liquid.persistence.domain.Container;
import liquid.metadata.ContainerStatus;
import liquid.persistence.domain.Location;
import liquid.persistence.repository.ContainerRepository;
import liquid.service.ContainerService;
import liquid.service.LocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    private LocationService locationService;

    @ModelAttribute("containers")
    public Iterable<Container> populateContainers() {
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
    public List<Location> populateLocations() {
        return locationService.findByType(LocationType.YARD.getType());
    }

    @RequestMapping(method = RequestMethod.GET)
    public String init(Model model, Principal principal) {
        return "container/list";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute Container container,
                      BindingResult bindingResult, Principal principal) {
        logger.debug("container: {}", container);

        if (bindingResult.hasErrors()) {
            return "container/list";
        } else {
            container.setStatus(0);
            containerService.save(container);
            return "redirect:/container";
        }
    }
}
