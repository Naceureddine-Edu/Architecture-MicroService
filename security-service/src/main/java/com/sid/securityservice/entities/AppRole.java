package com.sid.securityservice.entities;


import lombok.*;
import javax.persistence.*;


@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
// Parce que dans spring security on deja une class qui s'appel ROLE pour ne pas confendre
public class AppRole
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roleName;
}
