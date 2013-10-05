package liquid.controller;

import liquid.persistence.domain.ChargeType;
import liquid.persistence.domain.ServiceProvider;
import liquid.persistence.domain.SpType;
import liquid.persistence.repository.ChargeTypeRepository;
import liquid.persistence.repository.SpRepository;
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
 * Date: 10/2/13
 * Time: 10:20 PM
 */
@Controller
@RequestMapping("/ct")
public class ChargeTypeController {
    private static final Logger logger = LoggerFactory.getLogger(ChargeTypeController.class);

    @Autowired
    private ChargeTypeRepository ctRepository;

    @ModelAttribute("cts")
    public Iterable<ChargeType> populateCts() {
        return ctRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET)
    public void init(Model model, Principal principal) {
        model.addAttribute("ct", new ChargeType());
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute("ct") ChargeType ct,
                         BindingResult bindingResult, Model model, Principal principal) {
        logger.debug("ct: {}", ct);

        if (bindingResult.hasErrors()) {
            return "ct";
        } else {
            ctRepository.save(ct);
            return "redirect:/ct";
        }
    }
}
