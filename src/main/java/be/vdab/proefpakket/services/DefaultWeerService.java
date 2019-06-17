package be.vdab.proefpakket.services;

import be.vdab.proefpakket.restclients.WeerClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DefaultWeerService implements WeerService {
    private final WeerClient client;

    public DefaultWeerService(WeerClient client) {
        this.client = client;
    }
     @Override
    public BigDecimal getTemperatuur(String plaats){
        return client.getTemperatuur(plaats);
     }
}
