package liquid.controller;

import liquid.accounting.persistence.domain.ChargeEntity;
import liquid.dto.EarningDto;
import liquid.metadata.ChargeWay;
import liquid.order.domain.Order;
import liquid.order.domain.VerificationSheetForm;
import liquid.order.facade.OrderFacade;
import liquid.order.persistence.domain.OrderEntity;
import liquid.order.service.OrderService;
import liquid.persistence.domain.ServiceSubtypeEntity;
import liquid.security.SecurityContext;
import liquid.service.ChargeService;
import liquid.service.ServiceSubtypeService;
import liquid.service.TaskService;
import liquid.task.domain.Task;
import liquid.transport.persistence.domain.LegEntity;
import liquid.transport.persistence.domain.PathEntity;
import liquid.transport.persistence.domain.RouteEntity;
import liquid.transport.persistence.domain.ShipmentEntity;
import liquid.transport.service.RouteService;
import liquid.transport.service.ShipmentService;
import liquid.transport.web.domain.Shipment;
import liquid.transport.web.domain.TransMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/26/13
 * Time: 11:25 PM
 */
@Controller
@RequestMapping("/task/{taskId}")
public class TaskController extends BaseTaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService taskService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private ChargeService chargeService;

    @Autowired
    private ServiceSubtypeService serviceSubtypeService;

    @RequestMapping(method = RequestMethod.GET)
    public String initForm(@PathVariable String taskId, Model model) {
        logger.debug("taskId: {}", taskId);
        Task task = taskService.getTask(taskId);
        logger.debug("task: {}", task);
        model.addAttribute("task", task);

        switch (task.getDefinitionKey()) {
            case "CDCI":
                String verificationSheetSn = orderService.find(task.getOrderId()).getVerificationSheetSn();
                VerificationSheetForm verificationSheetForm = new VerificationSheetForm();
                verificationSheetForm.setDefinitionKey(task.getDefinitionKey());
                verificationSheetForm.setSn(verificationSheetSn);
                verificationSheetForm.setOrderId(task.getOrderId());
                model.addAttribute("task", task);
                model.addAttribute("verificationSheetForm", verificationSheetForm);
                return "order/verification_sheet_sn";
        }
        
        return "redirect:" + taskService.computeTaskMainPath(taskId);
    }

    @RequestMapping(method = RequestMethod.POST, params = "definitionKey=CDCI")
    public String fillIn(@PathVariable String taskId, @Valid @ModelAttribute VerificationSheetForm verificationSheetForm,
                         BindingResult bindingResult, Model model) {
        logger.debug("taskId: {}", taskId);
        Task task = taskService.getTask(taskId);
        logger.debug("task: {}", task);

        if (bindingResult.hasErrors()) {
            model.addAttribute("verificationSheetForm", verificationSheetForm);
            model.addAttribute("task", task);
            return "order/verification_sheet_sn";
        }

        Order order = orderFacade.find(verificationSheetForm.getOrderId());
        order.setVerificationSheetSn(verificationSheetForm.getSn());
        orderFacade.save(order);

        model.addAttribute("verificationSheetForm", verificationSheetForm);
        model.addAttribute("task", task);
        model.addAttribute("alert", messageSource.getMessage("save.success", new String[]{}, Locale.CHINA));
        return "order/verification_sheet_sn";
    }

    /**
     * TODO: Move to another controller.
     *
     * @param taskId
     * @param model
     * @return
     */
    @RequestMapping(value = "/common", method = RequestMethod.GET)
    public String toCommon(@PathVariable String taskId, Model model) {
        logger.debug("taskId: {}", taskId);
        Task task = taskService.getTask(taskId);
        model.addAttribute("task", task);
        return "task/common";
    }

    @RequestMapping(value = "/check_amount", method = RequestMethod.GET)
    public String checkAmount(@PathVariable String taskId, Model model) {
        long orderId = taskService.getOrderIdByTaskId(taskId);
        Task task = taskService.getTask(taskId);
        model.addAttribute("task", task);

        if ("ROLE_COMMERCE".equals(SecurityContext.getInstance().getRole())) {
            Iterable<ChargeEntity> charges = chargeService.findByOrderId(orderId);
            model.addAttribute("charges", charges);
        } else {
            Iterable<ChargeEntity> charges = chargeService.findByOrderIdAndCreateRole(orderId,
                    SecurityContext.getInstance().getRole());
            model.addAttribute("charges", charges);
        }

        model.addAttribute("chargeWays", ChargeWay.values());
        Iterable<ServiceSubtypeEntity> serviceSubtypes = serviceSubtypeService.findEnabled();
        model.addAttribute("serviceSubtypes", serviceSubtypes);
        return "charge/list";
    }

    @RequestMapping(value = "/settlement", method = RequestMethod.GET)
    public String settle(@PathVariable String taskId, Model model) {
        Task task = taskService.getTask(taskId);
        model.addAttribute("task", task);

        OrderEntity order = taskService.findOrderByTaskId(taskId);
        Iterable<ChargeEntity> charges = chargeService.findByOrderId(order.getId());
        model.addAttribute("charges", charges);

        model.addAttribute("chargeWays", ChargeWay.values());
        Iterable<ServiceSubtypeEntity> serviceSubtypes = serviceSubtypeService.findEnabled();
        model.addAttribute("serviceSubtypes", serviceSubtypes);

        EarningDto earning = chargeService.calculateEarning(order, charges);
        model.addAttribute("earning", earning);
        return "charge/settlement";
    }

    @RequestMapping(value = "/planning", method = RequestMethod.GET)
    public String init(@PathVariable String taskId, Model model) {
        logger.debug("taskId: {}", taskId);

        Long orderId = taskService.getOrderIdByTaskId(taskId);
        OrderEntity order = orderService.find(orderId);

        Iterable<ShipmentEntity> shipmentSet = shipmentService.findByOrderId(orderId);

        int containerUsage = 0;
        for (ShipmentEntity shipment : shipmentSet) {
            containerUsage += shipment.getContainerQty();
        }

        Shipment shipment = new Shipment();
        // set remaining container quantity as the default value for the next shipment planning.
        shipment.setContainerQuantity(order.getContainerQty() - containerUsage);

        // shipment planning bar
        model.addAttribute("shipment", shipment);
        model.addAttribute("containerTotality", order.getContainerQty());

        List<RouteEntity> routes = routeService.find(order.getSrcLocId(), order.getDstLocId());
        routes.add(RouteEntity.newInstance(RouteEntity.class, 0L));
        model.addAttribute("routes", routes);

        // shipment table
        model.addAttribute("shipmentSet", shipmentSet);
        model.addAttribute("transModes", TransMode.toMap());

        // charge table
        model.addAttribute("chargeWays", ChargeWay.values());
        Iterable<ChargeEntity> charges = chargeService.findByTaskId(taskId);
        model.addAttribute("charges", charges);
        model.addAttribute("total", chargeService.total(charges));
        return "planning/main";
    }

    @RequestMapping(value = "/planning/shipment", method = RequestMethod.POST)
    public String addShipment(@PathVariable String taskId, @Valid @ModelAttribute(value = "shipment") Shipment shipment,
                              BindingResult result, Model model) {
        logger.debug("taskId: {}", taskId);
        logger.debug("shipment: {}", shipment);

        Long orderId = taskService.getOrderIdByTaskId(taskId);
        OrderEntity order = orderService.find(orderId);

        int containerUsage = 0;
        Iterable<ShipmentEntity> shipmentSet = shipmentService.findByOrderId(orderId);
        for (ShipmentEntity r : shipmentSet) {
            containerUsage += r.getContainerQty();
        }

        if (result.hasErrors()) {
            model.addAttribute("containerTotality", order.getContainerQty());

            model.addAttribute("shipmentSet", shipmentSet);
            model.addAttribute("transModes", TransMode.toMap());

            // charge table
            model.addAttribute("chargeWays", ChargeWay.values());
            Iterable<ChargeEntity> charges = chargeService.findByTaskId(taskId);
            model.addAttribute("charges", charges);
            model.addAttribute("total", chargeService.total(charges));
            return "planning/main";
        } else if (shipment.getContainerQuantity() > (order.getContainerQty() - containerUsage)) {
            setFieldError(result, "shipment", "containerQty", shipment.getContainerQuantity(), (order.getContainerQty() - containerUsage));

            model.addAttribute("containerTotality", order.getContainerQty());

            model.addAttribute("shipmentSet", shipmentSet);
            model.addAttribute("transModes", TransMode.toMap());

            // charge table
            model.addAttribute("chargeWays", ChargeWay.values());
            Iterable<ChargeEntity> charges = chargeService.findByTaskId(taskId);
            model.addAttribute("charges", charges);
            model.addAttribute("total", chargeService.total(charges));
            return "planning/main";
        } else {
            ShipmentEntity shipmentEntityntity = new ShipmentEntity();
            shipmentEntityntity.setOrder(order);
            shipmentEntityntity.setContainerQty(shipment.getContainerQuantity());
            shipmentEntityntity.setTaskId(taskId);

            if (null != shipment.getRouteId() && shipment.getRouteId() > 0L) {
                RouteEntity route = routeService.findOne(shipment.getRouteId());
                List<PathEntity> paths = route.getPaths();
                List<LegEntity> legs = new ArrayList<>(paths.size());
                for (PathEntity path : paths) {
                    LegEntity leg = new LegEntity();
                    leg.setShipment(shipmentEntityntity);
                    leg.setTransMode(path.getTransportMode());
                    leg.setSrcLoc(path.getFrom());
                    leg.setDstLoc(path.getTo());
                    legs.add(leg);
                }
                shipmentEntityntity.setLegs(legs);
            }

            shipmentService.save(shipmentEntityntity);
            return "redirect:/task/" + taskId + "/planning";
        }
    }
}
