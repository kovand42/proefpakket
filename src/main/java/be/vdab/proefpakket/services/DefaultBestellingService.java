package be.vdab.proefpakket.services;

import be.vdab.proefpakket.domain.Bestelling;
import be.vdab.proefpakket.mail.MailSender;
import be.vdab.proefpakket.messaging.ProefpakketMessage;
import be.vdab.proefpakket.repositories.BestellingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
public class DefaultBestellingService implements BestellingService{
    private final BestellingRepository bestellingRepository;
    /*private final MailSender mailSender;*/
    private final JmsTemplate jmsTemplate;
    private final String proefpakkatQueue;

    public DefaultBestellingService(BestellingRepository bestellingRepository,
                                    /*MailSender mailSender,*/ JmsTemplate jmsTemplate,
                                    @Value("{proefpakketQueue}") String proefpakkatQueue) {
        this.bestellingRepository = bestellingRepository;
        /*this.mailSender = mailSender;*/
        this.jmsTemplate = jmsTemplate;
        this.proefpakkatQueue = proefpakkatQueue;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
    public void create(Bestelling bestelling){
/*        mailSender.proefpakket(bestelling.getEmailAdres(),
                bestelling.getBrouwer().getNaam());*/
        bestellingRepository.save(bestelling);
        ProefpakketMessage message = new ProefpakketMessage(
                bestelling.getEmailAdres(), bestelling.getBrouwer().getNaam());
        jmsTemplate.convertAndSend(proefpakkatQueue, message);
    }

}
