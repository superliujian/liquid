package liquid.operation.controller;

import liquid.model.Alert;
import liquid.model.AlertType;
import liquid.operation.domain.Goods;
import liquid.operation.service.InternalGoodsService;
import liquid.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/28/13
 * Time: 3:30 PM
 */
@Controller
@RequestMapping("/goods")
public class GoodsController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private InternalGoodsService goodsService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(required = false, defaultValue = "0") int number,
                       Model model) {
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));
        Page<Goods> page = goodsService.findAll(pageRequest);

        model.addAttribute("page", page);
        model.addAttribute("goods", new Goods());
        model.addAttribute("contextPath", "/goods?");
        return "goods/list";
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String initNew(Model model) {
        model.addAttribute("goods", new Goods());
        return "goods/form";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String initEdit(@PathVariable Long id, Model model) {
        logger.debug("id: {}", id);

        model.addAttribute("goods", goodsService.find(id));
        return "goods/form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute("goods") Goods goods,
                         BindingResult bindingResult, Model model) {
        logger.debug("Goods: {}", goods);

        if (bindingResult.hasErrors()) {
            return "goods/form";
        } else {
            try {
                goodsService.save(goods);
                return "redirect:/goods";
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
                model.addAttribute("alert", new Alert(AlertType.DANGER, "duplicated.key"));
                return "goods/form";
            }
        }
    }
}
