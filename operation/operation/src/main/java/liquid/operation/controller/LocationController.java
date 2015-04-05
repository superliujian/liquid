package liquid.operation.controller;

import liquid.domain.LocationForm;
import liquid.model.Alert;
import liquid.model.AlertType;
import liquid.operation.domain.Location;
import liquid.operation.domain.LocationType;
import liquid.operation.service.InternalLocationService;
import liquid.operation.service.LocationTypeService;
import liquid.pinyin4j.PinyinHelper;
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
import java.util.ArrayList;
import java.util.List;

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
    private InternalLocationService locationService;

    @Autowired
    private LocationTypeService locationTypeService;

    @ModelAttribute("locationTypes")
    public Iterable<LocationType> populateLocationTypes() {
        return locationTypeService.findAll();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(@RequestParam(required = false, defaultValue = "0") int number,
                       @RequestParam(required = false, defaultValue = "-1") Byte type,
                       Model model) {
        PageRequest pageRequest = new PageRequest(number, size, new Sort(Sort.Direction.DESC, "id"));

        Page<Location> page;
        if (type == -1) {
            page = locationService.findAll(pageRequest);
        } else {
            page = locationService.findAll(type, pageRequest);
        }
        model.addAttribute("page", page);
        model.addAttribute("type", type);
        model.addAttribute("location", new Location());
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
                List<Location> entities = new ArrayList<>();
                for (Byte type : location.getTypes()) {
                    Location entity = new Location();
                    entity.setName(location.getName());
                    entity.setType(new LocationType(type));
                    String queryName = PinyinHelper.converterToFirstSpell(location.getName()) + ";" + location.getName();
                    entity.setQueryName(queryName);
                    entities.add(entity);
                }
                locationService.save(entities);
                return "redirect:/location";
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
                model.addAttribute("alert", new Alert("duplicated.key"));
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
    public String edit(@Valid @ModelAttribute("location") Location location,
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
                model.addAttribute("alert", new Alert(AlertType.DANGER, "duplicated.key"));
                return "location/edit";
            }
        }
    }
}
