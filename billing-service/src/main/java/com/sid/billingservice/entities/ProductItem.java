package com.sid.billingservice.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sid.billingservice.Model.Product;
import lombok.*;
import javax.persistence.*;


@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class ProductItem
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double quantity;
    private double price;
    private long productID;

    // Pour ne pas tomber dans la boucle infini en appeleant /fulBill/id dans le controller
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    public Bill bill;

    //@JsonIgnore
    @Transient
    private Product product;

    @Transient
    private String productName;

}
