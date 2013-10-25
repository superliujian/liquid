package liquid.controller;

import liquid.persistence.domain.SpType;
import liquid.persistence.repository.SpTypeRepository;
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
 * Date: 10/25/13
 * Time: 7:37 PM
 */
@Controller
@RequestMapping("/sp_type")
public class SpTypeController {
    private static final Logger logger = LoggerFactory.getLogger(SpTypeController.class);

    @Autowired
    private SpTypeRepository spTypeRepository;

    @ModelAttribute("spTypes")
    public Iterable<SpType> populateCts() {
        return spTypeRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String init(Model model, Principal principal) {
        model.addAttribute("spType", new SpType());
        return "data_dict/sp_type";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute("spType") SpType spType,
                         BindingResult bindingResult, Model model, Principal principal) {
        logger.debug("spType: {}", spType);

        if (bindingResult.hasErrors()) {
            return "data_dict/sp_type";
        } else {
            spTypeRepository.save(spType);
            return "redirect:/sp_type";
        }
    }
}
