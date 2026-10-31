package com.jg.sistema.auth.application.service;
import java.time.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.HexFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;
@Service @RequiredArgsConstructor
public class AuthRateLimiter {
    private final JdbcTemplate jdbc;
    private final PlatformTransactionManager manager;
    private final Clock clock;
    @org.springframework.beans.factory.annotation.Value("${app.jobs-enabled:true}") private boolean jobsEnabled;
    @org.springframework.scheduling.annotation.Scheduled(fixedDelay=300000)
    public void cleanup() {
        if(!jobsEnabled) return;
        jdbc.update("DELETE FROM auth_limits WHERE expires_at<?",LocalDateTime.now(clock).minusDays(1));
        jdbc.update("DELETE FROM oauth_codes WHERE expires_at<?",LocalDateTime.now(clock));
    }
    public void check(String key, int limit, Duration window) {
        var tx = new TransactionTemplate(manager);
        tx.setPropagationBehavior(org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        int attempts = tx.execute(status -> {
            String bucket = digest(key.toLowerCase(java.util.Locale.ROOT).strip());
            var now = LocalDateTime.now(clock);
            jdbc.update("INSERT INTO auth_limits(bucket,attempts,expires_at) VALUES (?,0,?) ON CONFLICT DO NOTHING",bucket,now.plus(window));
            jdbc.queryForList("SELECT bucket FROM auth_limits WHERE bucket=? FOR UPDATE",bucket);
            jdbc.update("UPDATE auth_limits SET attempts=0, expires_at=? WHERE bucket=? AND expires_at<=?",now.plus(window),bucket,now);
            jdbc.update("UPDATE auth_limits SET attempts=attempts+1 WHERE bucket=?",bucket);
            return jdbc.queryForObject("SELECT attempts FROM auth_limits WHERE bucket=?",Integer.class,bucket);
        });
        if (attempts > limit) throw new ResponseStatusException(TOO_MANY_REQUESTS,"Too many attempts; try again later");
    }
    public static String digest(String value) {
        try { return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8))); }
        catch (NoSuchAlgorithmException e) { throw new IllegalStateException(e); }
    }
}
