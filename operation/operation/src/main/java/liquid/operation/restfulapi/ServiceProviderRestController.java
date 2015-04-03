package liquid.operation.restfulapi;

import liquid.operation.domain.ServiceProvider;
import liquid.operation.service.ServiceProviderService;
import liquid.transport.web.domain.TransMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.List;

/**
 * Created by redbrick9 on 1/1/14.
 */
@Controller
@RequestMapping("/api/sp")
public class ServiceProviderRestController {
    private static final Logger logger = LoggerFactory.getLogger(ServiceProviderRestController.class);

    @Autowired
    private ServiceProviderService serviceProviderService;

    @RequestMapping(method = RequestMethod.GET, params = "subtypeId")
    @ResponseBody
    public List<ServiceProvider> listBySubtypeId(@RequestParam Long subtypeId) {
        return serviceProviderService.findByServiceSubtypeId(subtypeId);
    }

    @RequestMapping(method = RequestMethod.GET, params = "transportMode")
    @ResponseBody
    public Iterable<ServiceProvider> listByTransportMode(@RequestParam Integer transportMode) {
        Long serviceType = TransMode.toServiceType(transportMode);
        if (null == serviceType) {
            return Collections.emptyList();
        }
        return serviceProviderService.findByType(serviceType);
    }
}
