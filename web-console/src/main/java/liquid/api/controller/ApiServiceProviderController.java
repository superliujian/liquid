package liquid.api.controller;

import liquid.domain.ServiceProvider;
import liquid.facade.ServiceProviderFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by redbrick9 on 1/1/14.
 */
@Controller
@RequestMapping("/api/sp")
public class ApiServiceProviderController {
    private static final Logger logger = LoggerFactory.getLogger(ApiServiceProviderController.class);

    @Autowired
    private ServiceProviderFacade serviceProviderFacade;

    @RequestMapping(method = RequestMethod.GET, params = "subtypeId")
    @ResponseBody
    public List<ServiceProvider> list(@RequestParam Long subtypeId) {
        return serviceProviderFacade.findBySubtypeId(subtypeId);
    }

    @RequestMapping(method = RequestMethod.GET, params = "name")
    @ResponseBody
    public Iterable<ServiceProvider> listByName(@RequestParam String name) {
        logger.debug("name: {}", name);
        return serviceProviderFacade.findByQueryNameLike(name);
    }
}
