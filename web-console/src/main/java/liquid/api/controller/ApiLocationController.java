package liquid.api.controller;

import liquid.persistence.domain.LocationEntity;
import liquid.service.LocationService;
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
    private LocationService locationService;

    @RequestMapping(method = RequestMethod.GET, params = "name")
    @ResponseBody
    public Iterable<LocationEntity> listByName(@RequestParam String name) {
        logger.debug("name: {}", name);
        return locationService.findByTypeAndNameLike(0, name);
    }
}
