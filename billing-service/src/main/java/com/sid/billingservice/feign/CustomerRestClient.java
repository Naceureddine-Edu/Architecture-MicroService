package com.sid.billingservice.feign;


import com.sid.billingservice.Model.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collection;

     // Feign Permet de communiquer avec d'autre MicroService en utilisant REST
     // Comme dans ctte exemple on est entrain de communiquer avec le MicroService customer-service

//Name En majuscule imperativement =>le MicorService s'enregistre comme ca dans l'annuaire(Eureka-discovery)
@FeignClient(name = "CUSTOMER-SERVICE")
public interface CustomerRestClient
{
    // Les PATHS c'est ceux de @RepositoryRestResource dans le MicroService customer-service

    @GetMapping(path = "/customers/{id}")
    Customer getCustomerById(@PathVariable(name = "id") Long id);

    @GetMapping(path = "/customers")
    Collection<Customer> getCustomers();
}
