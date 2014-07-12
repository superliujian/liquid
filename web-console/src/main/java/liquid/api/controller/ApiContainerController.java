package liquid.api.controller;

import liquid.container.domain.Container;
import liquid.container.facade.ContainerFacade;
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
 * Created by redbrick9 on 7/12/14.
 */
@Controller
@RequestMapping("/api/container")
public class ApiContainerController {
    private static final Logger logger = LoggerFactory.getLogger(ApiContainerController.class);

    @Autowired
    private ContainerFacade containerFacade;

    @RequestMapping(method = RequestMethod.GET, params = "bicCode")
    @ResponseBody
    public List<Container> findByBicCodeLike(@RequestParam String bicCode) {
        logger.debug("bicCode: {}", bicCode);
        return containerFacade.findByBicCodeLike(bicCode);
    }
}
