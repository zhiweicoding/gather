package com.news.ai.gather.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.news.ai.gather.bean.model.EmailBean;
import com.news.ai.gather.dao.EmailDao;
import com.news.ai.gather.services.EmailService;
import com.news.ai.gather.support.RedisSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

/**
 * @author zhiwei
 */
@Slf4j
@Service(value = "emailService")
public class EmailServiceImpl extends ServiceImpl<EmailDao, EmailBean> implements EmailService {

    @Autowired
    private RedisSupport redisSupport;

    @Value("${email.from}")
    private String from;

    @Value("${email.password}")
    private String password;

    @Value("${email.defaultTo}")
    private String defaultTo;

    @Value("${email.host}")
    private String host;

    @Value("${email.protocol}")
    private String protocol;

    @Value("${email.smtpAuth}")
    private String smtpAuth;

    @Value("${email.debug}")
    private String debug;

    @Value("${email.content}")
    private String contentType;

    private static final String mail_debug = "mail.debug";
    private static final String mail_smtp_auth = "mail.smtp.auth";
    private static final String mail_host = "mail.host";
    private static final String mail_transport_protocol = "mail.transport.protocol";

    /**
     * @param direction 发送人
     * @param subject   邮件标题
     * @param message   邮件内容
     * @return
     */
    private boolean sendEmail(String direction, String subject, String message) {
        Properties props = new Properties();
        props.setProperty(mail_debug, debug);
        props.setProperty(mail_smtp_auth, smtpAuth);
        props.setProperty(mail_host, host);
        props.setProperty(mail_transport_protocol, protocol);
        Session session = Session.getInstance(props);
        Transport transport = null;
        Message msg = new MimeMessage(session);
        try {
            msg.setSubject(subject);
            msg.setContent(message, contentType);
            msg.setFrom(new InternetAddress(from));
            transport = session.getTransport();
            transport.connect(host, from, password);
            transport.sendMessage(msg, new Address[]{new InternetAddress(direction)});

        } catch (MessagingException e) {
            log.error("send email error,{}", e.getMessage(), e);
            return false;
        } finally {
            try {
                if (transport != null) {
                    transport.close();
                }
            } catch (MessagingException e) {
                log.error("send email error,{}", e.getMessage(), e);
            }
        }
        return true;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendOverdue(String to, String title, String content) {
        String html = overdue(title, content);
        sendEmail(to, title, html);
        EmailBean emailBean = new EmailBean();
        emailBean.setEmailName(title);
        emailBean.setEmailContent(html);
        emailBean.setEmailFrom(from);
        emailBean.setEmailTo(to == null ? defaultTo : to);
        emailBean.setLogTime(LocalDateTime.now());
        boolean save = this.save(emailBean);
        if (save) {
            log.info("send overdue email success");
            return true;
        } else {
            log.error("send overdue email failed");
            return false;
        }
    }

    @Override
    public void sendOverdue(List<String> to, String title, String content) {
        for (String each : to) {
            boolean resultEach = sendOverdue(each, title, content);
            log.debug("send overdue email to {} result:{}", each, resultEach);
        }
    }

    /**
     * @param title   Twitter's Cookies and Token almost time out !
     * @param content Please check it ! otherwise twitter fetch can not work.
     * @return html content
     */
    public static String overdue(String title, String content) {
        return "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; background-color: #f4f4f9; margin: 40px 0; padding: 0; }" +
                ".container { width: 80%; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }" +
                ".header { background-color: #4CAF50; color: white; padding: 10px 0; text-align: center; border-radius: 10px 10px 0 0; }" +
                ".content { padding: 20px; }" +
                ".content h1 { color: #d9534f; }" +
                ".content p { font-size: 16px; line-height: 1.5; }" +
                ".footer { text-align: center; padding: 10px 0; font-size: 12px; color: #777777; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div class='header'>" +
                "<h2>System Alert</h2>" +
                "</div>" +
                "<div class='content'>" +
                "<h1>" + title + "</h1>" +
                "<p>" + content + "</p>" +
                "<p>Please check the system and ensure everything is working correctly.</p>" +
                "</div>" +
                "<div class='footer'>" +
                "<p>&copy; 2024 <a href=\"https://www.zhiwei.plus\">zhiwei.plus</a>. All rights reserved.</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

}




