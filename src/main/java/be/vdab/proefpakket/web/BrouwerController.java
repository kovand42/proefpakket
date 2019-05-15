package be.vdab.proefpakket.web;

import be.vdab.proefpakket.domain.Brouwer;
import be.vdab.proefpakket.forms.OndernemingsNrForm;
import be.vdab.proefpakket.services.BestellingService;
import be.vdab.proefpakket.services.BrouwerService;
import be.vdab.proefpakket.services.GemeenteService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("brouwers")
@SessionAttributes("bestelling")
public class BrouwerController {
    private static final String VIEW = "brouwers/brouwer";
    private static final String REDIRECT_BIJ_BROUWER_NIET_GEVONDEN = "redirect:/";
    private final BrouwerService brouwerService;
    private final GemeenteService gemeenteService;
    private final BestellingService bestellingService;
    private static final String ONDERNEMINGS_NR_VIEW="ondernemingsnr";
    private static final String REDIRECT_NA_ONDERNEMINGSNR =
            "redirect:/brouwers/{id}";

    public BrouwerController(BrouwerService brouwerService, GemeenteService gemeenteService, BestellingService bestellingService) {
        this.brouwerService = brouwerService;
        this.gemeenteService = gemeenteService;
        this.bestellingService = bestellingService;
    }

    @GetMapping("{optionalBrouwer}")
    ModelAndView read(@PathVariable Optional<Brouwer> optionalBrouwer){
        ModelAndView modelAndView = new ModelAndView("brouwer");
        optionalBrouwer.ifPresent(brouwer -> modelAndView.addObject(brouwer));
        return modelAndView;
    }

    @GetMapping("{brouwer}/ondernemingsnr")
    ModelAndView ondernemingsNr(@PathVariable Optional<Brouwer> brouwer,
                                RedirectAttributes redirectAttributes) {
        if (brouwer.isPresent()) {
            OndernemingsNrForm form = new OndernemingsNrForm();
            form.setOndernemingsNr(brouwer.get().getOndernemingsNr());
            return new ModelAndView(ONDERNEMINGS_NR_VIEW)
                    .addObject(brouwer.get())
                    .addObject(form);
        }
        redirectAttributes.addAttribute("fout", "Brouwer niet gevonden");
        return new ModelAndView(REDIRECT_BIJ_BROUWER_NIET_GEVONDEN);
    }
    @PostMapping("{brouwer}/ondernemingsnr")
    ModelAndView ondernemingsNr(@PathVariable Optional<Brouwer> brouwer,
                                @Valid OndernemingsNrForm form, BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (brouwer.isPresent()) {
            if (bindingResult.hasErrors()) {
                return new ModelAndView(ONDERNEMINGS_NR_VIEW).addObject(brouwer.get());
            }
            brouwer.get().setOndernemingsNr(form.getOndernemingsNr());
            brouwerService.update(brouwer.get());
            redirectAttributes.addAttribute("id", brouwer.get().getId());
            return new ModelAndView(REDIRECT_NA_ONDERNEMINGSNR);
        }
        redirectAttributes.addAttribute("fout", "Brouwer niet gevonden");
        return new ModelAndView(REDIRECT_BIJ_BROUWER_NIET_GEVONDEN);
    }
}
