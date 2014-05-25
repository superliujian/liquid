package liquid.api.controller;

import liquid.persistence.domain.ServiceProviderEntity;
import liquid.service.ServiceProviderService;
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
    private ServiceProviderService serviceProviderService;

    @RequestMapping(method = RequestMethod.GET, params = "type")
    @ResponseBody
    public List<ServiceProviderEntity> list(@RequestParam long type) {
        return serviceProviderService.findByChargeType(type);
    }
}
