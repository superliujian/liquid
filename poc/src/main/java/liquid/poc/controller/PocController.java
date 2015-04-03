package liquid.poc.controller;

import liquid.model.MailDto;
import liquid.poc.service.PocService;
import liquid.service.MailNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * TODO: Comments.
 * User: tao
 * Date: 12/2/13
 * Time: 8:12 PM
 */
@Controller
@RequestMapping("/poc")
public class PocController {

    @Autowired
    private MailNotificationService mailNotificationService;

    @Autowired
    private PocService testService;

    @RequestMapping(value = "/{template}", method = RequestMethod.GET)
    public String combobox(@PathVariable String template,
                           @RequestParam(required = false) String msg,
                           Model model) {
        model.addAttribute("msg", msg);
        return "poc/" + template;
    }

    @RequestMapping(value = "/mail", method = RequestMethod.POST)
    public String sendMail(MailDto mailDto) {
        mailNotificationService.send(mailDto.getMailTo(), mailDto.getSubject(), mailDto.getContent());
        return "poc/mail";
    }

    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public String message(String msgKey) {
        String msg = testService.i18n(msgKey);
        return "redirect:/poc/message?msg=" + msg;
    }
}
