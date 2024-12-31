package com.ncp.moeego.member.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl {
    private final JavaMailSender javaMailSender;
    private static final String senderEmail= "bit.moeego@gmail.com";
    private static int number;
    // 랜덤으로 숫자 생성
    public static void createNumber() {
        number = (int)(Math.random() * (90000)) + 100000; //(int) Math.random() * (최댓값-최소값+1) + 최소값
    }
    public MimeMessage CreateMail(String mail) {
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + number + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");
            log.info("Email 인증 번호 : {}", number);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }
    public int sendMail(String mail) {
        MimeMessage message = CreateMail(mail);
        javaMailSender.send(message);
        return number;
    }
    
    // 고수 신청 승인 이메일 생성
    public MimeMessage AccessMail(String mail) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("달인 신청 승인 되었습니다");
            String body = "<h3>축하합니다!</h3>" +
            			  "<h4>달인 신청이 승인 되었습니다.</h4>" +
                          "<h4>로그아웃 후 다시 로그인 해주세요.</h4>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }

    // 고수 신청 승인 메일 전송
    public void accessMail(String email) {
        MimeMessage message = AccessMail(email);
        javaMailSender.send(message); // 이메일 전송
    }
    
 // 고수 신청 취소 이메일 생성
    public MimeMessage CancelMail(String mail) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("달인 신청 취소 되었습니다");
            String body = "<h3>죄송합니다.</h3>" +
            		      "<h4>달인 신청이 취소 되었습니다.</h4>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }

    // 고수 신청 취소 메일 전송
    public void cancelMail(String email) {
        MimeMessage message = CancelMail(email);
        javaMailSender.send(message); // 이메일 전송
    }
    
    
    // 고수 박탈 시 메일 전송
    private MimeMessage RevokeMail(String mail, String reason) {
    	MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("달인 권한이 박탈 되었습니다");
            String body = "<h4> 이유는 : "  + reason + "</h4>" + 
                    	  "<p>달인 권한이 박탈 되었습니다.</p>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }
	
    public void revokeMail(String email, String reason) {
		MimeMessage message = RevokeMail(email, reason);
		javaMailSender.send(message);
		
	}
}
