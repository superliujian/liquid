package liquid.controller;

import liquid.container.domain.ContainerType;
import liquid.container.persistence.domain.ContainerSubtypeEntity;
import liquid.container.service.ContainerSubtypeService;
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
import java.util.Map;

/**
 * Created by redbrick9 on 5/4/14.
 */
@Controller
@RequestMapping("/data_dict/container_subtype")
public class ContainerSubtypeController {
    private static final Logger logger = LoggerFactory.getLogger(ContainerSubtypeController.class);

    @Autowired
    private ContainerSubtypeService containerSubtypeService;

    @ModelAttribute("containerTypeMap")
    public Map<Integer, String> populateContainerTypes() {
        return ContainerType.toMap();
    }

    @ModelAttribute("containerSubtypes")
    public Iterable<ContainerSubtypeEntity> findAll() {
        return containerSubtypeService.findEnabled();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String init(Model model) {
        model.addAttribute("containerSubtype", new ContainerSubtypeEntity());
        return "data_dict/container_subtype";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute("containerSubtype") ContainerSubtypeEntity containerSubtypeEntity,
                         BindingResult bindingResult) {
        logger.debug("containerSubtype: {}", containerSubtypeEntity);

        if (bindingResult.hasErrors()) {
            return "data_dict/container_subtype";
        } else {
            containerSubtypeService.save(containerSubtypeEntity);
            return "redirect:/data_dict/container_subtype";
        }
    }
}
