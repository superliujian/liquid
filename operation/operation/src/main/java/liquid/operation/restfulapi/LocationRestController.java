package liquid.operation.restfulapi;

import liquid.operation.domain.Location;
import liquid.operation.service.InternalLocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by redbrick9 on 7/12/14.
 */
@Controller
@RequestMapping("/api/location")
public class LocationRestController {
    private static final Logger logger = LoggerFactory.getLogger(LocationRestController.class);

    @Autowired
    private InternalLocationService locationService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Iterable<Location> listByName(@RequestParam(required = false) Byte type, @RequestParam String name) {
        logger.debug("name: {}", name);

        Iterable<Location> locations = null == type ?
                locationService.findByNameLike(name) :
                locationService.findByTypeAndNameLike(type, name);
        return locations;
    }
}
