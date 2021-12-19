package com.sid.securityservice.util;

public class JWTutil
{
    public static final String SECRET = "mysecret1234";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer "; // Avec espace

    // Le delai doit etre cours entre 2 a 5 minutes
    public static final long ACCES_TOKEN_EXPIRE = 1*60*1000; // 2 minutes
    // Par contre le refresh token doit avoir un delai long allant de 20 jours a 2 mois
    // Puisque c'est lui qui va permettre a l'utilisateur d'avoir a chaque fois un nouvel
    // Acces token comme ca il n'aura pas a se connecter chaque 2 a 5 minutes
    public static final long REFRESH_TOKEN_EXPIRE = 15*60*1000; // 15 minutes
}
