package liquid.controller;

import liquid.dto.MailDto;
import liquid.service.MailNotificationService;
import liquid.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

/**
 * TODO: Comments.
 * User: tao
 * Date: 12/2/13
 * Time: 8:12 PM
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private MailNotificationService mailNotificationService;

    @Autowired
    private TestService testService;

    @RequestMapping(value = "/{template}", method = RequestMethod.GET, params = "msg")
    public String combobox(@PathVariable String template,
                           @RequestParam(required = false) String msg,
                           Model model, Principal principal) {
        model.addAttribute("msg", msg);
        return "test/" + template;
    }

    @RequestMapping(value = "/mail", method = RequestMethod.POST)
    public String sendMail(MailDto mailDto) {
        mailNotificationService.send(mailDto.getMailTo(), mailDto.getSubject(), mailDto.getContent());
        return "test/mail";
    }

    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public String message(String msgKey) {
        String msg = testService.i18n(msgKey);
        return "redirect:/test/message?msg=" + msg;
    }
}
