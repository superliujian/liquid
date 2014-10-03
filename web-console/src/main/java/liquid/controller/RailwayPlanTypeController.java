package liquid.controller;

import liquid.persistence.domain.RailPlanTypeEntity;
import liquid.service.RailwayPlanTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by redbrick9 on 8/23/14.
 */
@Controller
@RequestMapping("/railway_plan_type")
public class RailwayPlanTypeController {
    private static final Logger logger = LoggerFactory.getLogger(RailwayPlanTypeController.class);

    @Autowired
    private RailwayPlanTypeService railwayPlanTypeService;

    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        Iterable<RailPlanTypeEntity> railwayPlanTypes = railwayPlanTypeService.findAll();

        model.addAttribute("railwayPlanTypes", railwayPlanTypes);
        return "railway_plan_type/list";
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String initNew(Model model) {
        model.addAttribute("railwayPlanType", new RailPlanTypeEntity());
        return "railway_plan_type/form";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String initEdit(@PathVariable Long id, Model model) {
        logger.debug("id: {}", id);

        model.addAttribute("railwayPlanType", railwayPlanTypeService.find(id));
        return "railway_plan_type/form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String create(@ModelAttribute("railwayPlanType") RailPlanTypeEntity railwayPlanType) {
        logger.debug("RailwayPlanTypeEntity: {}", railwayPlanType);

        railwayPlanTypeService.save(railwayPlanType);
        return "redirect:/railway_plan_type";
    }
}
