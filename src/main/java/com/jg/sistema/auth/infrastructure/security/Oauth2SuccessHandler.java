package com.jg.sistema.auth.infrastructure.security;
import com.jg.sistema.auth.application.service.OAuthAccountService;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;
@Component @RequiredArgsConstructor
public class Oauth2SuccessHandler implements AuthenticationSuccessHandler {
    private final OAuthAccountService accounts;
    @Value("${redirect-url}") private String redirect;
    public void onAuthenticationSuccess(HttpServletRequest request,HttpServletResponse response,Authentication auth) throws IOException {
        var token=(OAuth2AuthenticationToken)auth;
        var user=token.getPrincipal(); String provider=token.getAuthorizedClientRegistrationId();
        if("google".equals(provider) && !Boolean.TRUE.equals(user.getAttribute("email_verified"))) {
            response.sendError(401,"Provider email is not verified"); return;
        }
        String subject=String.valueOf(user.getAttributes().getOrDefault("sub",user.getAttributes().get("id")));
        if("null".equals(subject)) { response.sendError(401,"Provider identity missing"); return; }
        String code;
        try {
            code=accounts.issue(provider,subject,user.getAttribute("email"),user.getAttribute("given_name")!=null?user.getAttribute("given_name"):user.getAttribute("first_name"),"google".equals(provider)?user.getAttribute("family_name"):user.getAttribute("last_name"));
        } catch(org.springframework.web.server.ResponseStatusException ex) {
            if(request.getSession(false)!=null) request.getSession(false).invalidate();
            org.springframework.security.core.context.SecurityContextHolder.clearContext();
            response.sendError(ex.getStatusCode().value(),"OAuth2 sign-in rejected");return;
        }
        if(request.getSession(false)!=null) request.getSession(false).invalidate();
        org.springframework.security.core.context.SecurityContextHolder.clearContext();
        response.sendRedirect(UriComponentsBuilder.fromUriString(redirect).queryParam("code",code).build().encode().toUriString());
    }
}
