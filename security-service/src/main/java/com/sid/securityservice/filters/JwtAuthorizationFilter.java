package com.sid.securityservice.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;


public class JwtAuthorizationFilter extends OncePerRequestFilter
{

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain) 
                                    throws ServletException, IOException 
    {
        // Recuperer le TOKEN a partir du header
        String authorizationToken = request.getHeader("Authorization");
        if(authorizationToken!=null && authorizationToken.startsWith("Bearer "))
        {
            try
            {
                String jwt = authorizationToken.substring(7); // pour enlever Bearer et espace
                // Le meme secret avec quoi on a signé le token dans la methode
                // successfulAuthentication de AuthenticationFilter
                Algorithm algorithm = Algorithm.HMAC256("mySecret1234");// Pour signer le TOKEN
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();// Permet de verfier le token
                DecodedJWT decodedJWT = jwtVerifier.verify(jwt);// contient tout les infos du token
                String username = decodedJWT.getSubject();// getSubject return le username=>Payload
                String[] roles =decodedJWT.getClaim("roles").asArray(String.class);
                Collection<GrantedAuthority> authorities = new ArrayList<>();

                for(String role: roles)
                {
                    authorities.add(new SimpleGrantedAuthority(role));
                }

                // Parametres: username,password,authorities=>roles
                UsernamePasswordAuthenticationToken authenticationToken = new
                 UsernamePasswordAuthenticationToken(username,null,authorities);

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request,response);// Passe ou filtre suivant NEXT
            }
            catch(Exception e)
            {
                response.setHeader("error-message",e.getMessage());
                response.sendError(HttpServletResponse.SC_FORBIDDEN);//403
            }
        }
        else
        {
            // Ici on dit tu peux passer mais c'est spring filter qui va voir est ce que la ressource demander
            // demande une authentification ou role ou pas
            filterChain.doFilter(request,response);// Passe ou filtre suivant NEXT
        }
    }
}
