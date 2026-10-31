package com.jg.sistema.auth.infrastructure.persistence;
import java.time.*;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
@Component @RequiredArgsConstructor @Slf4j
public class MailOutbox {
    private final JdbcTemplate jdbc;
    private final JavaMailSender mail;
    private final PlatformTransactionManager manager;
    private final Clock clock;
    @Value("${spring.mail.username}") private String sender;
    @Value("${app.jobs-enabled:true}") private boolean enabled;
    public void enqueue(String recipient,String subject,String body) {
        jdbc.update("INSERT INTO mail_outbox(id,recipient,subject,body,next_attempt) VALUES (?,?,?,?,?)",UUID.randomUUID(),recipient,subject,body,LocalDateTime.now(clock));
    }
    @Scheduled(fixedDelayString="${app.mail-delay:10000}")
    public void deliver() {
        if(!enabled) return;
        for(int i=0;i<20;i++) {
            boolean found=Boolean.TRUE.equals(new TransactionTemplate(manager).execute(status -> {
                var rows=jdbc.queryForList("SELECT * FROM mail_outbox WHERE sent=FALSE AND attempts<5 AND next_attempt<=? ORDER BY next_attempt LIMIT 1 FOR UPDATE SKIP LOCKED",LocalDateTime.now(clock));
                if(rows.isEmpty()) return false;
                var row=rows.getFirst();
                try {
                    var message=new SimpleMailMessage();message.setFrom(sender);message.setTo((String)row.get("recipient"));
                    message.setSubject((String)row.get("subject"));message.setText((String)row.get("body"));mail.send(message);
                    jdbc.update("UPDATE mail_outbox SET sent=TRUE,body='' WHERE id=?",row.get("id"));
                } catch(Exception ex) {
                    jdbc.update("UPDATE mail_outbox SET attempts=attempts+1,next_attempt=? WHERE id=?",LocalDateTime.now(clock).plusSeconds(20),row.get("id"));
                    log.warn("Mail delivery queued for retry: {}",row.get("id"));
                }
                return true;
            }));
            if(!found) break;
        }
    }
}
