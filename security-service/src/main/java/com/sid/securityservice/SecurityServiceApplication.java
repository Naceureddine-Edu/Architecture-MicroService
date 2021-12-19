package com.sid.securityservice;

import com.sid.securityservice.entities.AppRole;
import com.sid.securityservice.entities.AppUser;
import com.sid.securityservice.services.AccountServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
// Pour utiliser les annotations au lieu de la class SecurityConfig=>.antMatchers().hasAuthority();
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)//Dans les controllers et services
public class SecurityServiceApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(SecurityServiceApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner start(AccountServiceImpl accountService)
    {
        return args -> {
            accountService.addNewRole(new AppRole(null, "USER"));
            accountService.addNewRole(new AppRole(null, "ADMIN"));
            accountService.addNewRole(new AppRole(null, "CUSTOMER_MANAGER"));
            accountService.addNewRole(new AppRole(null, "PRODUCT_MANAGER"));
            accountService.addNewRole(new AppRole(null, "BILLS_MANAGER"));

            accountService.addNewUser(new AppUser(
                    null,"user1","1234",new ArrayList<>()));
            accountService.addNewUser(new AppUser(
                    null,"admin","1234",new ArrayList<>()));
            accountService.addNewUser(new AppUser(
                    null,"user2","1234",new ArrayList<>()));
            accountService.addNewUser(new AppUser(
                    null,"user3","1234",new ArrayList<>()));
            accountService.addNewUser(new AppUser(
                    null,"user4","1234",new ArrayList<>()));


            accountService.addRoleToUser("user1","USER");
            accountService.addRoleToUser("admin","ADMIN");
            accountService.addRoleToUser("admin","USER");
            accountService.addRoleToUser("user2","USER");
            accountService.addRoleToUser("user2","CUSTOMER_MANAGER");
            accountService.addRoleToUser("user3","USER");
            accountService.addRoleToUser("user3","PRODUCT_MANAGER");
            accountService.addRoleToUser("user4","USER");
            accountService.addRoleToUser("user4","BILLS_MANAGER");
            /*
            accountService.listUsers().forEach(appUser -> {
                System.out.println(appUser.toString());
            });
             */
        };
    }
}
