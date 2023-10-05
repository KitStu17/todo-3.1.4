package com.example.todo314.service;

import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailService {

    private final JavaMailSender javaMailSender;
    private final RedisService redisService;

    // 인증번호 생성
    private final String certificateNumber = createKey();

    @Value("${spring.mail.username}")
    private String id;

    public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {
        log.info("보내는 대상 : " + to);
        log.info("인증 번호 : " + certificateNumber);
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to.trim()));
        // message.addRecipients(Message.RecipientType.TO, new InternetAddress(to)); //
        // 전송 대상
        message.setSubject("Todo-List 회원가입 인증 코드 : ");

        // 메일 내용의 subtype을 html로 지정하여 html 문법 사용
        String msg = "";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 회원가입 화면에서 입력해주세요.</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += certificateNumber;
        msg += "</td></tr></tbody></table></div>";

        message.setText(msg, "utf-8", "html");
        message.setFrom(new InternetAddress(id));

        return message;

    }

    // 인증코드 만들기
    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) { // 인증코드 6자리
            key.append((rnd.nextInt(10)));
        }
        return key.toString();
    }

    // 메일 발송
    // MimeMessage = 전송할 내용
    // sendSimpleMessage = {to : 인증번호를 받을 메일 주소}
    public String sendSimpleMessage(String to) throws Exception {
        MimeMessage message = createMessage(to);
        try {
            redisService.setDataExpire(certificateNumber, to, 60 * 10L); // 유효시간 10분
            javaMailSender.send(message); // 메일 발송
        } catch (MailException e) {
            // e.printStackTrace();
            throw new IllegalStateException(e.getMessage());
        }
        return certificateNumber; // 인증 코드를 서버로 리턴
    }

    public String verifyCode(String code) throws NotFoundException {
        String memberEmail = redisService.getData(code);
        if (memberEmail == null) {
            throw new NotFoundException();
        }
        redisService.deleteData(code);

        return certificateNumber;
    }
}
