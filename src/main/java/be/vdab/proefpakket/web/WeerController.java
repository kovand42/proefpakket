package be.vdab.proefpakket.web;

import be.vdab.proefpakket.exceptions.KanTemperatuurNietLezenException;
import be.vdab.proefpakket.services.WeerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("weer")
class WeerController {
    private static final String VIEW = "temperatuur";
    private final WeerService weerService;

    public WeerController(WeerService weerService) {
        this.weerService = weerService;
    }

    @GetMapping("{plaats}/temperatuur")
    ModelAndView temperatuur(@PathVariable String plaats) {
        ModelAndView modelAndView = new ModelAndView(VIEW);
        try{
            modelAndView.addObject("temperatuur",
                    weerService.getTemperatuur(plaats));
        }catch (KanTemperatuurNietLezenException ex){
            modelAndView.addObject("fout", "kan temperatuur niet lezen");
        }
        return modelAndView;
    }
}
