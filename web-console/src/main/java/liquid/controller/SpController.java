package liquid.controller;

import liquid.persistence.domain.ServiceProvider;
import liquid.persistence.domain.SpType;
import liquid.persistence.repository.SpRepository;
import liquid.persistence.repository.SpTypeRepository;
import liquid.service.SpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;

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
    private SpService spService;

    @ModelAttribute("sps")
    public Iterable<ServiceProvider> populateSps() {
        return spService.findAll();
    }

    @ModelAttribute("spTypes")
    public Map<Long, String> populateSpTypes() {
        return spService.getSpTypes();
    }

    @RequestMapping(method = RequestMethod.GET)
    public void list(Model model, Principal principal) {
        model.addAttribute("sp", new ServiceProvider());
    }

    @RequestMapping(method = RequestMethod.GET, params = "type")
    @ResponseBody
    public Iterable<ServiceProvider> list(@RequestParam long type) {
        return spService.findByType(type);
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute("sp") ServiceProvider sp,
                         BindingResult bindingResult, Model model, Principal principal) {
        logger.debug("sp: {}", sp);

        if (bindingResult.hasErrors()) {
            return "sp";
        } else {
            spService.save(sp);
            return "redirect:/sp";
        }
    }
}
