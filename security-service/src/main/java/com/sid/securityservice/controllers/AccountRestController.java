package com.sid.securityservice.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sid.securityservice.entities.AppRole;
import com.sid.securityservice.entities.AppUser;
import com.sid.securityservice.services.AccountServiceImpl;
import com.sid.securityservice.util.JWTutil;
import lombok.Data;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
public class AccountRestController
{
    private final AccountServiceImpl accountService;

    public AccountRestController(AccountServiceImpl accountService) {
        this.accountService = accountService;
    }

    @PostMapping(path = "/users")
    @PostAuthorize("hasAuthority('ADMIN')")
    public AppUser addUser(@RequestBody AppUser appUser)
    {
        return accountService.addNewUser(appUser);
    }

    @PostMapping(path ="/roles")
    @PostAuthorize("hasAuthority('ADMIN')")
    public AppRole addRole(@RequestBody AppRole appRole)
    {
        return accountService.addNewRole(appRole);
    }

    @GetMapping(path ="/users")
    @PostAuthorize("hasAuthority('USER')")
    public List<AppUser> appUserList()
    {
        return accountService.listUsers();
    }

    @GetMapping(path ="/users/{userName}")
    @PostAuthorize("hasAuthority('USER')")
    public AppUser appUser(@PathVariable(name = "userName") String userName)
    {
        return accountService.loadUserByUserName(userName);
    }

    @PostMapping(path ="/addRoleToUser")
    @PostAuthorize("hasAuthority('ADMIN')")
    public void addRoleToUser(RoleToUser roleToUserClass)
    {
        accountService.addRoleToUser(roleToUserClass.getAppUserName(),roleToUserClass.getAppRoleName());
    }

    @GetMapping(path = "/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        // Recuperer le TOKEN a partir du header de la requette
        String authorizationToken = request.getHeader(JWTutil.AUTHORIZATION_HEADER);

        if(authorizationToken != null && authorizationToken.startsWith(JWTutil.TOKEN_PREFIX))
        {
            try
            {
                // Pour enlever Bearer et espace
                String jwtRefrechToken = authorizationToken.substring(JWTutil.TOKEN_PREFIX.length());

                // Le meme secret avec quoi on a signé le token dans la methode
                // successfulAuthentication de AuthenticationFilter
                Algorithm algorithmHMAC = Algorithm.HMAC256(JWTutil.SECRET);// Pour signer le TOKEN
                JWTVerifier jwtVerifier = JWT.require(algorithmHMAC).build();// Permet de verfier le token
                DecodedJWT decodedJWT = jwtVerifier.verify(jwtRefrechToken);
                String username = decodedJWT.getSubject();// getSubject return le username=>Payload
                // Ici ca montre qu'on peut aller vers le service est appelée un traitement si on veut
                AppUser appUser = accountService.loadUserByUserName(username);

                String jwtaccesToken = JWT.create()
                        .withSubject(appUser.getUserName())
                        .withExpiresAt(new Date(System.currentTimeMillis()+JWTutil.ACCES_TOKEN_EXPIRE))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", appUser.getAppRoles().stream().map(grantedAuthority ->
                                grantedAuthority.getRoleName()).collect(Collectors.toList()))
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
            catch(Exception e)
            {
                throw e;
            }
        }
        else
        {
            throw new RuntimeException("Refresh Token Is Required !!!");
        }
    }
}

@Data
class RoleToUser
{
    private String appUserName;
    private String appRoleName;
}
