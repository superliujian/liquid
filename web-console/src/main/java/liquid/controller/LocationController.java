package liquid.controller;

import liquid.persistence.domain.LocationEntity;
import liquid.domain.LocationType;
import liquid.persistence.repository.LocationRepository;
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
 * Date: 10/5/13
 * Time: 11:09 AM
 */
@Controller
@RequestMapping("/location")
public class LocationController {
    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

    @Autowired
    private LocationRepository locationRepository;

    @ModelAttribute("locations")
    public Iterable<LocationEntity> populateLocations() {
        return locationRepository.findAll();
    }

    @ModelAttribute("location")
    public LocationEntity populateLocation() {
        return new LocationEntity();
    }

    @ModelAttribute("locationTypes")
    public LocationType[] populateLocationTypes() {
        return LocationType.values();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String init(Model model, Principal principal) {
        return "location";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String add(@Valid @ModelAttribute LocationEntity locationEntity,
                      BindingResult bindingResult, Principal principal) {
        logger.debug("location: {}", locationEntity);

        if (bindingResult.hasErrors()) {
            return "location";
        } else {
            locationRepository.save(locationEntity);
            return "redirect:/location";
        }
    }
}
