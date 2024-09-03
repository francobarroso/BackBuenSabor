package com.entidades.buenSabor.business.service;

import com.entidades.buenSabor.config.email.EmailDto;
import jakarta.mail.MessagingException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public interface EmailService {
    void enviarEmail(EmailDto emailDto, ByteArrayOutputStream facturaPdf) throws MessagingException, IOException;
}
