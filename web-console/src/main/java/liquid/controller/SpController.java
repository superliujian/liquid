package liquid.controller;

import liquid.persistence.domain.ServiceProvider;
import liquid.persistence.domain.SpType;
import liquid.persistence.domain.TradeType;
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
 * Time: 5:02 PM
 */
@Controller
@RequestMapping("/sp")
public class SpController {
    private static final Logger logger = LoggerFactory.getLogger(SpController.class);

    @Autowired
    private SpRepository spRepository;

    @ModelAttribute("sps")
    public Iterable<ServiceProvider> populateSps() {
        return spRepository.findAll();
    }

    @ModelAttribute("spTypes")
    public SpType[] populateSpTypes() {
        return SpType.values();
    }

    @RequestMapping(method = RequestMethod.GET)
    public void list(Model model, Principal principal) {
        model.addAttribute("sp", new ServiceProvider());
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute("sp") ServiceProvider sp,
                         BindingResult bindingResult, Model model, Principal principal) {
        logger.debug("sp: {}", sp);

        if (bindingResult.hasErrors()) {
            return "sp";
        } else {
            spRepository.save(sp);
            return "redirect:/sp";
        }
    }
}
