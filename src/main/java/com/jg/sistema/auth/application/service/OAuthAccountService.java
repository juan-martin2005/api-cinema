package com.jg.sistema.auth.application.service;
import java.time.*;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.*;
import com.jg.sistema.user.application.port.out.*;
import com.jg.sistema.user.application.port.in.RegisterUserUseCase;
import com.jg.sistema.auth.application.command.TokenPair;
import com.jg.sistema.auth.application.port.out.TokenGeneratorPort;
import com.jg.sistema.auth.application.port.in.RefreshTokenUseCase;
@Service @RequiredArgsConstructor
public class OAuthAccountService {
    private final JdbcTemplate jdbc;
    private final UserRepositoryPort users;
    private final RegisterUserUseCase registration;
    private final TokenGeneratorPort tokens;
    private final RefreshTokenUseCase refresh;
    private final UserContextHolderPort current;
    private final Clock clock;
    @Transactional
    public String issue(String provider, String subject, String email, String name, String lastName) {
        if (subject==null || email==null || email.isBlank()) throw new ResponseStatusException(BAD_REQUEST,"Provider must supply a verified email");
        email=email.strip().toLowerCase(Locale.ROOT);
        var ids=jdbc.queryForList("SELECT user_id FROM oauth_identities WHERE provider=? AND subject=?",UUID.class,provider,subject);
        UUID userId=null;
        if (!ids.isEmpty()) userId=ids.getFirst();
        else if (users.findByEmail(email).isEmpty()) {
            registration.registerUserOauth2(email,name,lastName);
            userId=users.findByEmail(email).orElseThrow().id();
            jdbc.update("INSERT INTO oauth_identities(provider,subject,user_id) VALUES (?,?,?)",provider,subject,userId);
        }
        if (userId!=null) enabled(userId);
        String code=UUID.randomUUID().toString()+UUID.randomUUID();
        jdbc.update("INSERT INTO oauth_codes(code_hash,user_id,provider,subject,email,expires_at) VALUES (?,?,?,?,?,?)",AuthRateLimiter.digest(code),userId,provider,subject,email,LocalDateTime.now(clock).plusMinutes(2));
        return code;
    }
    @Transactional
    public TokenPair exchange(String code, boolean link) {
        var rows=jdbc.queryForList("SELECT * FROM oauth_codes WHERE code_hash=? AND expires_at>? FOR UPDATE",AuthRateLimiter.digest(code),LocalDateTime.now(clock));
        if(rows.isEmpty()) throw new ResponseStatusException(UNAUTHORIZED,"Invalid or expired code");
        var row=rows.getFirst(); UUID id=(UUID)row.get("user_id");
        if(id==null) {
            UUID caller=current.getCurrentUserId();
            if(!link || caller==null || !users.findById(caller).orElseThrow().email().equals(row.get("email")))
                throw new ResponseStatusException(CONFLICT,"Sign in to your existing account and link this provider");
            enabled(caller); id=caller;
            jdbc.update("INSERT INTO oauth_identities(provider,subject,user_id) VALUES (?,?,?)",row.get("provider"),row.get("subject"),id);
        }
        var user=enabled(id);
        jdbc.update("DELETE FROM oauth_codes WHERE code_hash=?",AuthRateLimiter.digest(code));
        return new TokenPair(tokens.generateToken(user.email()),refresh.createRefreshToken(id));
    }
    private com.jg.sistema.user.domain.model.User enabled(UUID id) {
        var user=users.findById(id).orElseThrow();
        if(!Boolean.TRUE.equals(user.isActive()) || !Boolean.TRUE.equals(user.isVerified())) throw new ResponseStatusException(UNAUTHORIZED,"Account disabled");
        return user;
    }
}
