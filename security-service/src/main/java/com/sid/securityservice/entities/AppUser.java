package com.sid.securityservice.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;


@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
// Parce que dans spring security on deja une class qui s'appel USER pour ne pas confendre
public class AppUser
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
   // @JsonIgnore Pour ne pas afficher le PWD dans la reponse HTTP JSON !!!!
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    // Des Qu'on appel un USER on charge en meme temps ses ROLES
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<AppRole> appRoles = new ArrayList<>();
}
