package be.vdab.proefpakket.web;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.awt.*;

@Controller
@RequestMapping(name = "gemeenten", produces = MediaType.TEXT_HTML_VALUE)
public class GemeenteController {
}
