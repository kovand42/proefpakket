package be.vdab.proefpakket.web;

import be.vdab.proefpakket.domain.Bestelling;
import be.vdab.proefpakket.domain.Brouwer;
import be.vdab.proefpakket.forms.OndernemingsNrForm;
import be.vdab.proefpakket.services.BestellingService;
import be.vdab.proefpakket.services.BrouwerService;
import be.vdab.proefpakket.services.GemeenteService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
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
    private static final String PROEFPAKKET_STAP_1_VIEW =
            "brouwers/proefpakketstap1";
    private static final String PROEFPAKKET_STAP_2_VIEW =
            "brouwers/proefpakketstap2";
    private static final String REDIRECT_NA_BESTELLING = "redirect:/";

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
    @GetMapping("{brouwer}/proefpakket")
    ModelAndView proefpakket(@PathVariable Optional<Brouwer> brouwer,
                             RedirectAttributes redirectAttributes){
        if(brouwer.isPresent()){
            return new ModelAndView(PROEFPAKKET_STAP_1_VIEW)
                    .addObject(brouwer.get())
                    .addObject(new Bestelling());
        }
        redirectAttributes.addAttribute("fout", "Brouwer niet gevonden");
        return new ModelAndView(REDIRECT_BIJ_BROUWER_NIET_GEVONDEN);
    }
    @InitBinder("bestelling")
    void initBinder(DataBinder binder){
        binder.initDirectFieldAccess();
    }
    @PostMapping(value = "{brouwer}/proefpakket", params = "stap2")
    ModelAndView proefpakketStap1NaarStap2(@PathVariable Optional<Brouwer> brouwer,
                                           @Validated(Bestelling.Stap1.class) Bestelling bestelling,
                                           BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(brouwer.isPresent()){
            if(bindingResult.hasErrors()){
                return new ModelAndView(PROEFPAKKET_STAP_1_VIEW).addObject(brouwer.get());
            }
            return new ModelAndView(PROEFPAKKET_STAP_2_VIEW)
                    .addObject(brouwer.get())
                    .addObject("gemeenten", gemeenteService.findAll());
        }
        redirectAttributes.addAttribute("fout", "Brouwer niet gevonden");
        return new ModelAndView(REDIRECT_BIJ_BROUWER_NIET_GEVONDEN);
    }
    @PostMapping(value = "{brouwer}/proefpakket", params = "stap1")
    ModelAndView proefpakketStap2NaarStap1(@PathVariable Optional<Brouwer> brouwer,
                                           Bestelling bestelling, RedirectAttributes redirectAttributes){
        if(brouwer.isPresent()){
            return new ModelAndView(PROEFPAKKET_STAP_1_VIEW).addObject(brouwer.get());
        }
        redirectAttributes.addAttribute("fout", "Brouwer niet gevonden");
        return new ModelAndView(REDIRECT_BIJ_BROUWER_NIET_GEVONDEN);
    }
    @PostMapping(value = "{brouwer}/proefpakket", params = "opslaan")
    ModelAndView proefpakketOpslaan(@PathVariable Optional<Brouwer> brouwer,
                                    @Validated(Bestelling.Stap2.class) Bestelling bestelling,
                                    BindingResult bindingResult, SessionStatus sessionStatus,
                                    RedirectAttributes redirectAttributes) {
        if (brouwer.isPresent()) {
            if (bindingResult.hasErrors()) {
                return new ModelAndView(PROEFPAKKET_STAP_2_VIEW)
                        .addObject(brouwer.get())
                        .addObject("gemeenten", gemeenteService.findAll());
            }
            bestellingService.create(bestelling);
            sessionStatus.setComplete();
            return new ModelAndView(REDIRECT_NA_BESTELLING);
        }
        redirectAttributes.addAttribute("fout", "Brouwer niet gevonden");
        return new ModelAndView(REDIRECT_BIJ_BROUWER_NIET_GEVONDEN);
    }
}
