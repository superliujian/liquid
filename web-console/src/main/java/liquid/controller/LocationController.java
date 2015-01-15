package liquid.controller;

import liquid.domain.LocationForm;
import liquid.domain.LocationType;
import liquid.facade.LocationFacade;
import liquid.persistence.domain.LocationEntity;
import liquid.service.LocationService;
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
import java.util.Locale;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/5/13
 * Time: 11:09 AM
 */
@Controller
@RequestMapping("/location")
public class LocationController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationFacade locationFacade;

    @ModelAttribute("locationTypes")
    public LocationType[] populateLocationTypes() {
        return LocationType.values();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(required = false, defaultValue = "0") int number,
                       @RequestParam(required = false, defaultValue = "-1") int type,
                       Model model) {
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
        model.addAttribute("contextPath", "/location?");
        return "location/list";
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String initNew(Model model) {
        model.addAttribute("location", new LocationForm());
        return "location/form";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute LocationForm location,
                      BindingResult bindingResult, Model model) {
        logger.debug("location: {}", location);

        if (bindingResult.hasErrors()) {
            return "location/form";
        } else {
            try {
                locationFacade.save(location);
                return "redirect:/location";
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
                model.addAttribute("alert", messageSource.getMessage("duplicated.key", new String[]{}, Locale.CHINA));
                return "location/form";
            }
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String initEdit(@PathVariable Long id, Model model) {
        logger.debug("id: {}", id);

        model.addAttribute("location", locationService.find(id));
        return "location/edit";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public String edit(@Valid @ModelAttribute("location") LocationEntity location,
                       BindingResult bindingResult, Model model) {
        logger.debug("location: {}", location);

        if (bindingResult.hasErrors()) {
            return "location/edit";
        } else {
            try {
                locationService.save(location);
                return "redirect:/location";
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
                model.addAttribute("alert", messageSource.getMessage("duplicated.key", new String[]{}, Locale.CHINA));
                return "location/edit";
            }
        }
    }
}
