package com.sid.securityservice.security;

import com.sid.securityservice.entities.AppUser;
import com.sid.securityservice.filters.JwtAuthenticationFilter;
import com.sid.securityservice.filters.JwtAuthorizationFilter;
import com.sid.securityservice.services.AccountServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.Collection;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    private final AccountServiceImpl accountService;

    public SecurityConfig(AccountServiceImpl accountService)
    {
        this.accountService = accountService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(new UserDetailsService()
        {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
            {
                AppUser appUser = accountService.loadUserByUserName(username);
                Collection<GrantedAuthority> authorities = new ArrayList<>();

                appUser.getAppRoles().forEach(appRole -> {
                    authorities.add(new SimpleGrantedAuthority((appRole.getRoleName())));
                });

                return new User(appUser.getUserName(), appUser.getPassword(), authorities);
            }
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        // Quand on utilise les sessions et les cookies=> statefull
        // il ne faut pas desactiver csrf=> champ hidden dans le formulaire qui contient le token
        http.csrf().disable();
        // http.formLogin(); //Le formulaire classic de Spring dans le path LOGIN

        // Activier l'authentification STATELESS=> JSON WEB TOKEN
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Pour la base de donnée H2
        // Pour avoir un nouveau acces token a partir de refreshToken
        // Pour acceder a la page login MEME si c'est par default
        http.headers().frameOptions().disable();
        http.authorizeRequests().antMatchers("/h2-console/**",
                                            "/refreshToken/**","/login/**").permitAll();

        // Tout utilisateur avec le ROLE USER peut acceder a la liste des USERS
        //http.authorizeRequests().antMatchers(HttpMethod.GET,"/users/**").hasAuthority("USER");

        // Y'a que l'utilisateur avec le ROLE ADMIN qui peut AJOUTER UN USER a la BD
        //http.authorizeRequests().antMatchers(HttpMethod.POST,"/users/**").hasAuthority("ADMIN");

        // Toutes les ressources nécessite d'etre AUTHENTIFIER
        // si on veut permettre des routes faut le faire avant cette ligne
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(new JwtAuthenticationFilter(authenticationManagerBean()));
        http.addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();
    }
}
