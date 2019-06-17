package be.vdab.proefpakket.restclients;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OpenWeatherWeerClientTest {
    @Autowired
    private WeerClient client;
    @Test
    public void deTemperatuurGroterDanMin273Celsius(){
        assertTrue(client.getTemperatuur("Leuven").compareTo(BigDecimal.valueOf(-273)) > 0);
    }

}
