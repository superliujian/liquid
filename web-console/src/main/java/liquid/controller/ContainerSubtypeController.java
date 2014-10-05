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
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping(method = RequestMethod.GET)
    public String init(Model model) {
        model.addAttribute("containerSubtypes", containerSubtypeService.findEnabled());
        return "data_dict/container_subtype";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String initForm(Model model) {
        model.addAttribute("containerSubtype", new ContainerSubtypeEntity());
        return "data_dict/container_subtype_form";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String initEdit(@PathVariable Long id, Model model) {
        ContainerSubtypeEntity containerSubtype = containerSubtypeService.find(id);
        model.addAttribute("containerSubtype", containerSubtype);
        return "data_dict/container_subtype_form";
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
