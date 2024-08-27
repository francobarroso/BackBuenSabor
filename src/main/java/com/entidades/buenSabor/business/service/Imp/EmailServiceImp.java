package com.entidades.buenSabor.business.service.Imp;

import com.entidades.buenSabor.business.service.EmailService;
import com.entidades.buenSabor.email.EmailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class EmailServiceImp implements EmailService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public EmailServiceImp(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void enviarEmail(EmailDto emailDto, ByteArrayOutputStream facturaPdf) throws MessagingException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf8");
        helper.setTo(emailDto.getDestinatario());
        helper.setSubject(emailDto.getAsunto());
        helper.setText(emailDto.getMensaje(), true);

        InputStreamSource attachment = new ByteArrayResource(facturaPdf.toByteArray());
        helper.addAttachment("factura.pdf", attachment);

        javaMailSender.send(message);
    }
}
