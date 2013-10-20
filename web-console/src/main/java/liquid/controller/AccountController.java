package liquid.controller;

import liquid.persistence.domain.Account;
import liquid.metadata.GroupType;
import liquid.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.security.Principal;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/6/13
 * Time: 11:50 AM
 */
@Controller
@RequestMapping
public class AccountController {
    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    @ModelAttribute("groupTypes")
    public GroupType[] populateGroups() {
        return GroupType.values();
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String initRegister(Model model, Principal principal) {
        model.addAttribute("account", new Account());
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@Valid Account account,
                           BindingResult bindingResult, Model model, Principal principal) {
        logger.debug("account: {}", account);
        if (bindingResult.hasErrors()) {
            return "register";
        } else {
            if (account.getPassword().equals(account.getPassword2())) {
                accountService.register(account);
                return "redirect:/signin";
            } else {
                ObjectError objectError = new ObjectError("password", "passwords are not same.");
                bindingResult.addError(objectError);
                return "register";
            }
        }
    }
}
