package liquid.controller;

import liquid.persistence.domain.Container;
import liquid.persistence.domain.ContainerStatus;
import liquid.persistence.domain.TradeType;
import liquid.persistence.repository.ContainerRepository;
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
    private ContainerRepository containerRepository;

    @ModelAttribute("containers")
    public Iterable<Container> populateContainers() {
        return containerRepository.findAll();
    }

    @ModelAttribute("container")
    public Container populateContainer() {
        return new Container();
    }

    @ModelAttribute("statusArray")
    public ContainerStatus[] populateStatus() {
        return ContainerStatus.values();
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
            containerRepository.save(container);
            return "redirect:/container";
        }
    }
}
