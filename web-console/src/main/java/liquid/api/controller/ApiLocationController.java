package liquid.api.controller;

import liquid.facade.LocationFacade;
import liquid.web.domain.Location;
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
public class ApiLocationController {
    private static final Logger logger = LoggerFactory.getLogger(ApiLocationController.class);

    @Autowired
    private LocationFacade locationFacade;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Iterable<Location> listByName(@RequestParam(required = false) Integer type, @RequestParam String name) {
        logger.debug("name: {}", name);
        if (null == type)
            return locationFacade.findByNameLike(name);
        return locationFacade.findByTypeAndNameLike(type, name);
    }
}
