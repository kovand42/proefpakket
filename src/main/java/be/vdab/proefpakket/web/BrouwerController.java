package be.vdab.proefpakket.web;

import be.vdab.proefpakket.domain.Bestelling;
import be.vdab.proefpakket.domain.Brouwer;
import be.vdab.proefpakket.forms.OndernemingsNrForm;
import be.vdab.proefpakket.services.BestellingService;
import be.vdab.proefpakket.services.BrouwerService;
import be.vdab.proefpakket.services.GemeenteService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.awt.*;
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
    @GetMapping("{optionalBrouwer}/proefpakket")
    ModelAndView proefpakket(@PathVariable Optional<Brouwer> optionalBrouwer) {
        ModelAndView modelAndView = new ModelAndView("proefpakketstap1");
        optionalBrouwer.ifPresent(
                brouwer -> modelAndView.addObject(brouwer).addObject(new Bestelling()));
        return modelAndView;
    }
    @InitBinder("bestelling")
    void initBinder(DataBinder binder) {
        binder.initDirectFieldAccess();
    }

    @PostMapping(value = "{optionalBrouwer}/proefpakket", params = "stap2")
    ModelAndView proefpakketNaarStap2(
            @PathVariable Optional<Brouwer> optionalBrouwer,
            @Validated(Bestelling.Stap1.class) Bestelling bestelling, Errors errors) {
        if (optionalBrouwer.isPresent()) {
            Brouwer brouwer = optionalBrouwer.get();
            if (errors.hasErrors()) {
                return new ModelAndView("proefpakketstap1").addObject(brouwer);
            }
            return new ModelAndView("proefpakketstap2").addObject(brouwer)
                    .addObject("gemeenten", gemeenteService.findAll());
        }
        return new ModelAndView("proefpakketstap2");
    }
    @PostMapping(value = "{optionalBrouwer}/proefpakket", params = "stap1")
    ModelAndView proefpakketNaarStap1(
            @PathVariable Optional<Brouwer> optionalBrouwer, Bestelling bestelling) {
        ModelAndView modelAndView = new ModelAndView("proefpakketstap1");
        optionalBrouwer.ifPresent(brouwer -> modelAndView.addObject(brouwer));
        return modelAndView;
    }
    @PostMapping(value = "{optionalBrouwer}/proefpakket", params = "opslaan")
    ModelAndView proefpakketOpslaan(@PathVariable Optional<Brouwer> optionalBrouwer,
                                    @Validated(Bestelling.Stap2.class) Bestelling bestelling, Errors errors,
                                    SessionStatus session, RedirectAttributes redirect) {
        if (optionalBrouwer.isPresent()) {
            Brouwer brouwer = optionalBrouwer.get();
           // System.out.println(brouwer.getNaam());
            if (errors.hasErrors()) {
                return new ModelAndView("proefpakketstap2").addObject(brouwer)
                        .addObject("gemeenten", gemeenteService.findAll());
            }
            bestelling.setBrouwer(brouwer);
            bestellingService.create(bestelling);
            session.setComplete();
            return new ModelAndView("redirect:/");
        }
        return new ModelAndView("proefpakketstap2");
    }
}
