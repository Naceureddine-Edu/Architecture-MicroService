package com.sid.billingservice.entities;


import com.sid.billingservice.Model.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;


@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Bill
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date billingDate;

    @OneToMany(mappedBy = "bill")
    public Collection<ProductItem> productItems;

    private long customerID;

    @Transient
    private Customer customer;
}
