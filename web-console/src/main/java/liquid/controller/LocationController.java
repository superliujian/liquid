package liquid.controller;

import liquid.domain.LocationType;
import liquid.persistence.domain.LocationEntity;
import liquid.service.LocationService;
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
import java.security.Principal;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/5/13
 * Time: 11:09 AM
 */
@Controller
@RequestMapping("/location")
public class LocationController {
    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

    @Autowired
    private LocationService locationService;

    @ModelAttribute("locationTypes")
    public LocationType[] populateLocationTypes() {
        return LocationType.values();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(required = false, defaultValue = "0") int number,
                       @RequestParam(required = false, defaultValue = "-1") int type,
                       Model model) {
        int size = 10;
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));

        Page<LocationEntity> page;
        if (type == -1) {
            page = locationService.findAll(pageRequest);
        } else {
            page = locationService.findAll(type, pageRequest);
        }
        model.addAttribute("page", page);
        model.addAttribute("type", type);
        model.addAttribute("location", new LocationEntity());
        return "location/list";
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String initNew(Model model) {
        model.addAttribute("location", new LocationEntity());
        return "location/form";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String initEdit(@PathVariable Long id, Model model) {
        logger.debug("id: {}", id);

        model.addAttribute("location", locationService.find(id));
        return "location/form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute LocationEntity locationEntity,
                      BindingResult bindingResult, Principal principal) {
        logger.debug("location: {}", locationEntity);

        if (bindingResult.hasErrors()) {
            return "location";
        } else {
            locationService.save(locationEntity);
            return "redirect:/location";
        }
    }
}
