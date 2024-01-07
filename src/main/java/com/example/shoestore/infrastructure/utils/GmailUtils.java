package com.example.shoestore.infrastructure.utils;

import com.example.shoestore.core.common.dto.response.FileResponseDTO;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GmailUtils {

    @Value("${spring.mail.username}")
    public String fromEmail;

    private final JavaMailSender javaMailSender;

    @SneakyThrows
    public void sendEmail(String subject, String text, FileResponseDTO fileResponseDTO, List<String> recipientEmails) {

        MimeMessage message = javaMailSender.createMimeMessage();

        // Gửi email đến
        InternetAddress[] recipientAddresses = new InternetAddress[recipientEmails.size()];
        for (int i = 0; i < recipientEmails.size(); i++) {
            recipientAddresses[i] = new InternetAddress(recipientEmails.get(i));
        }

        try {
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, recipientAddresses);
            // Tiêu đề email
            message.setSubject(subject);

            // Nội dung email
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(text);

            // Tệp đính kèm
            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            DataSource source = new ByteArrayDataSource(fileResponseDTO.getByteArrayResource().getInputStream(), "application/octet-stream");
            attachmentBodyPart.setDataHandler(new DataHandler(source));
            attachmentBodyPart.setFileName(fileResponseDTO.getFileName());

            // Tạo multipart để kết hợp phần thân và phần đính kèm
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentBodyPart);
            message.setContent(multipart);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        javaMailSender.send(message);
    }

}
