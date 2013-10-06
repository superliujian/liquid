package liquid.controller;

import liquid.persistence.domain.Charge;
import liquid.persistence.domain.ChargeType;
import liquid.persistence.domain.ChargeWay;
import liquid.persistence.domain.Order;
import liquid.persistence.repository.ChargeRepository;
import liquid.persistence.repository.ChargeTypeRepository;
import liquid.persistence.repository.OrderRepository;
import liquid.service.ChargeService;
import liquid.service.bpm.ActivitiEngineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/2/13
 * Time: 9:17 PM
 */
@Controller
@RequestMapping("/charge")
public class ChargeController {
    private static final Logger logger = LoggerFactory.getLogger(ChargeController.class);

    @Autowired
    private ActivitiEngineService bpmService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ChargeTypeRepository ctRepository;

    @Autowired
    private ChargeRepository chargeRepository;

    @Autowired
    private ChargeService chargeService;

    @ModelAttribute("chargeWays")
    public ChargeWay[] populateChargeWays() {
        return ChargeWay.values();
    }

    @ModelAttribute("cts")
    public Map<Long, String> populateChargeTypes() {
        return chargeService.getChargeTypes();
    }

    @ModelAttribute("charges")
    public Iterable<Charge> populateCharges() {
        return chargeRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET)
    public void init() {
        logger.debug("init");
    }

    @RequestMapping(method = RequestMethod.POST)
    public String record(@Valid @ModelAttribute Charge charge,
                         @RequestHeader(value = "referer") String referer,
                         BindingResult bindingResult, Principal principal) {
        logger.debug("charge: {}", charge);
        logger.debug("referer: {}", referer);

        long orderId = bpmService.getOrderIdByTaskId(charge.getTaskId());
        Order order = orderRepository.findOne(orderId);

        charge.setOrder(order);

        if (ChargeWay.PER_ORDER.getValue() == charge.getWay()) {
            charge.setUnitPrice(0L);
        } else if (ChargeWay.PER_CONTAINER.getValue() == charge.getWay()) {
            charge.setTotalPrice(charge.getUnitPrice() * order.getContainerQty());
        } else {
            logger.warn("{} is out of charge way range.", charge.getWay());
        }

        chargeRepository.save(charge);

        String redirect = "redirect:" + referer;
        return redirect;
    }
}
