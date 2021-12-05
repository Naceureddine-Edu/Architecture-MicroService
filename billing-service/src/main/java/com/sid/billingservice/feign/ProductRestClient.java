package com.sid.billingservice.feign;


import com.sid.billingservice.Model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

    // Feign Permet de communiquer avec d'autre MicroService en utilisant REST
    // Comme dans ctte exemple on est entrain de communiquer avec le MicroService inventory-service

//Name En majuscule imperativement =>le MicorService s'enregistre comme ca dans l'annuaire(Eureka-discovery)
@FeignClient(name = "INVENTORY-SERVICE")
public interface ProductRestClient
{
    // Les PATHS c'est ceux de @RepositoryRestResource dans le MicroService inventory-service

    /*
        @GetMapping(path = "/products")
        PagedModel<Product> pageProducts();
     */

    /* Avec Pagination
    @GetMapping(path = "/products")
    PagedModel<Product> pageProducts(@RequestParam(name = "page") int page,
                                     @RequestParam(name = "size") int size);
    */
    
    @GetMapping(path = "/products")
    PagedModel<Product> pageProducts();


    @GetMapping(path = "/products/{id}")
    Product geProductById(@PathVariable(name = "id") Long id);
}
