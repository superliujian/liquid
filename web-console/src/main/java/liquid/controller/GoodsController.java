package liquid.controller;

import liquid.persistence.domain.Goods;
import liquid.persistence.repository.GoodsRepository;
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
@RequestMapping("/goods")
public class GoodsController {
    private static final Logger logger = LoggerFactory.getLogger(Goods.class);

    @Autowired
    private GoodsRepository goodsRepository;

    @ModelAttribute("cargos")
    public Iterable<Goods> populateCargos() {
        return goodsRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET)
    public void list(Model model, Principal principal) {
        model.addAttribute("goods", new Goods());
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute Goods goods,
                         BindingResult bindingResult, Principal principal) {
        logger.debug("Goods: {}", goods);

        if (bindingResult.hasErrors()) {
            return "goods";
        } else {
            goodsRepository.save(goods);
            return "redirect:/goods";
        }
    }
}
