package com.jg.sistema.auth.infrastructure.security;

import com.jg.sistema.auth.application.port.in.RefreshTokenUseCase;
import com.jg.sistema.user.application.port.in.RegisterUserUseCase;
import com.jg.sistema.user.application.port.out.UserRepositoryPort;
import com.jg.sistema.user.domain.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class Oauth2SuccessHandler implements AuthenticationSuccessHandler {

    @Value("${redirect-url}")
    private String redirectUrl;
    private final JwtService jwtService;
    private final RegisterUserUseCase registerUserUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oauthToken.getPrincipal();
        if (oAuth2User != null) {

            String provider = oauthToken.getAuthorizedClientRegistrationId();
            String email = oAuth2User.getAttribute("email");
            String name = null;
            String lastName = null;

            if("facebook".equals(provider)){
                name = oAuth2User.getAttribute("first_name");
                lastName =  oAuth2User.getAttribute("last_name");
            } else if("google".equals(provider)){
                name = oAuth2User.getAttribute("given_name");
                lastName =  oAuth2User.getAttribute("last_name");
            }

            registerUserUseCase.registerUserOauth2(email, name, lastName);

            String token = jwtService.generateToken(email);
            String refreshToken = refreshTokenUseCase.createRefreshTokenOauth2(email);
            response.sendRedirect(redirectUrl+"?token="+token+"&refreshToken="+refreshToken);
        }
    }
}
