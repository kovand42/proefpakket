package be.vdab.proefpakket.mail;

import be.vdab.proefpakket.exceptions.KanMailNietZendenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
class DefaultMailSender implements MailSender{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMailSender.class);
    private final JavaMailSender sender;

    public DefaultMailSender(JavaMailSender sender) {
        this.sender = sender;
    }

    @Override
    public void proefpakket(String emailAdres, String brouwerNaam){
        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(emailAdres);
            mailMessage.setSubject("Proefpakket " + brouwerNaam);
            mailMessage.setText("Bedankt voor uw interesse. U ontvangt uw proefpakket " +
                    brouwerNaam + " binnenkort.");
            sender.send(mailMessage);
        }catch (KanMailNietZendenException ex){
            LOGGER.error("Kan mail proefpakket niet versturen", ex);
            throw new KanMailNietZendenException();
        }
    }
}
