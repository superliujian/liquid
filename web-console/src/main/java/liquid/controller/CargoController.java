package liquid.controller;

import liquid.persistence.domain.Cargo;
import liquid.persistence.repository.CargoRepository;
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
 * Date: 9/28/13
 * Time: 3:30 PM
 */
@Controller
@RequestMapping("/cargo")
public class CargoController {
    private static final Logger logger = LoggerFactory.getLogger(Cargo.class);

    @Autowired
    private CargoRepository cargoRepository;

    @ModelAttribute("cargos")
    public Iterable<Cargo> populateCargos() {
        return cargoRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET)
    public void list(Model model, Principal principal) {
        model.addAttribute("cargo", new Cargo());
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute Cargo cargo,
                         BindingResult bindingResult, Principal principal) {
        logger.debug("Cargo: {}", cargo);

        if (bindingResult.hasErrors()) {
            return "cargo";
        } else {
            cargoRepository.save(cargo);
            return "redirect:/cargo";
        }
    }
}
