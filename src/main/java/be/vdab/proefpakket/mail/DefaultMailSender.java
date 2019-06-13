package be.vdab.proefpakket.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.management.openmbean.KeyAlreadyExistsException;

@Component
public class DefaultMailSender implements MailSender {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public final JavaMailSender sender;

    public DefaultMailSender(JavaMailSender sender) {
        this.sender = sender;
    }

    @Override
    public void proefpakket(String emailAdres, String brouwerNaam){
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailAdres);
            message.setSubject("Nieuwe proefpakket " + brouwerNaam);
            message.setText("Bedankt voor uw interesse. U ontvangt uw proefpakket " +
                    brouwerNaam + " binnenkort.");
            sender.send(message);
        }catch(MailException ex){
            logger.error("Kan mail nieuwe proefpakket niet versturen", ex);
            throw new KeyAlreadyExistsException();
        }
    }
}
