package liquid.controller;

import liquid.persistence.domain.TransRailway;
import liquid.persistence.repository.TransRailwayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * TODO: Comments.
 * User: tao
 * Date: 10/1/13
 * Time: 4:46 PM
 */
@Controller
@RequestMapping("/railway")
public class RailwayController {

    @Autowired
    private TransRailwayRepository railwayRepository;

    @RequestMapping(value = "/{railwayId}/delete", method = RequestMethod.GET)
    public String delete(@PathVariable long railwayId) {
        TransRailway railway = railwayRepository.findOne(railwayId);
        railwayRepository.delete(railwayId);
        return "redirect:/task/" + railway.getTaskId();
    }
}
