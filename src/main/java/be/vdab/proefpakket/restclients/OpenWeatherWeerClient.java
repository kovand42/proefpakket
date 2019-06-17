package be.vdab.proefpakket.restclients;

import be.vdab.proefpakket.exceptions.KanTemperatuurNietLezenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@Component
public class OpenWeatherWeerClient implements WeerClient{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final String openWeatherMapURL;
    private final RestTemplate restTemplate;

    public OpenWeatherWeerClient(@Value("${openWeatherMapURL}")String openWeatherMapURL,
                                 RestTemplateBuilder restTemplateBuilder) {
        this.openWeatherMapURL = openWeatherMapURL;
        this.restTemplate = restTemplateBuilder.build();
    }
    @Override
    public BigDecimal getTemperatuur(String plaats){
        try{
            return restTemplate.getForObject(openWeatherMapURL, Weer.class, plaats)
                    .getMain().getTemp();
        }catch(RestClientException ex){
            logger.error("kan temperatuur niet lezen", ex);
            throw new KanTemperatuurNietLezenException();
        }
    }
}
