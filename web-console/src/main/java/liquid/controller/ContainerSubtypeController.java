package liquid.controller;

import liquid.persistence.domain.ContainerSubtype;
import liquid.service.ContainerSubtypeService;
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

/**
 * Created by redbrick9 on 5/4/14.
 */
@Controller
@RequestMapping("/data_dict/container_subtype")
public class ContainerSubtypeController {
    private static final Logger logger = LoggerFactory.getLogger(ContainerSubtypeController.class);

    @Autowired
    private ContainerSubtypeService containerSubtypeService;

    @ModelAttribute("containerSubtypes")
    public Iterable<ContainerSubtype> findAll() {
        return containerSubtypeService.findEnabled();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String init(Model model) {
        model.addAttribute("containerSubtype", new ContainerSubtype());
        return "data_dict/container_subtype";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute("containerSubtype") ContainerSubtype containerSubtype,
                         BindingResult bindingResult) {
        logger.debug("containerSubtype: {}", containerSubtype);

        if (bindingResult.hasErrors()) {
            return "data_dict/container_subtype";
        } else {
            containerSubtypeService.save(containerSubtype);
            return "redirect:/data_dict/container_subtype";
        }
    }
}
