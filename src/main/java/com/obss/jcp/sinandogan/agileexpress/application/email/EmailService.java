package com.obss.jcp.sinandogan.agileexpress.application.email;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
}
