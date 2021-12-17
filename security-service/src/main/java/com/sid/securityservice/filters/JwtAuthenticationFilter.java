package com.sid.securityservice.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter
{
    AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException
    {
        System.out.println("attemptAuthenticationMethode");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username);
        System.out.println(password);
        UsernamePasswordAuthenticationToken authenticationToken = new
                        UsernamePasswordAuthenticationToken(username,password);
        return authenticationManager.authenticate(authenticationToken);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                   FilterChain chain, Authentication authResult) throws IOException, ServletException
    {
        System.out.println("successfulAuthentication");
        // getPrincipal PERMET DE RETOURNER L'UTILISATEUR CONNECTE
        User user = (User) authResult.getPrincipal();//Le cast parce que la methode retourne Object
        // Pour signer le TOKEN
        Algorithm algorithmHMAC = Algorithm.HMAC256("mySecret1234");

        String jwtaccesToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+5*60*1000))// 5min
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(grantedAuthority ->
                        grantedAuthority.getAuthority()).collect(Collectors.toList()))
                .sign(algorithmHMAC);

        String jwtRefrechToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+15*60*1000))// 15min
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithmHMAC);

        // Pour pouvoir recuperer jwtaccesToken et jwtRefrechToken dans le corps de la reponse
        Map<String,String> idToken = new HashMap<>();
        idToken.put("access-token",jwtaccesToken);
        idToken.put("refrech-token",jwtRefrechToken);

        // response.setHeader("Authorization",jwtaccesToken); envoyer le token dans le header
        // Envoye dans le corps de la reponse sous format JSON=>ObjectMapper
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), idToken);
    }
}
