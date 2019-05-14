package be.vdab.proefpakket.web;

import be.vdab.proefpakket.services.BrouwerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class IndexController {
    private final char[] alfabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private final BrouwerService brouwerService;

    public IndexController(BrouwerService brouwerService) {
        this.brouwerService = brouwerService;
    }

    @GetMapping
    ModelAndView index(){
        return new ModelAndView("index", "alfabet", alfabet);
    }
    @GetMapping("alfabet/{letter}")
    ModelAndView brouwers(@PathVariable String letter){
        return new ModelAndView("index", "alfabet", alfabet)
                .addObject("brouwers",
                        brouwerService.findByBeginNaam(String.valueOf(letter)));
    }
}
