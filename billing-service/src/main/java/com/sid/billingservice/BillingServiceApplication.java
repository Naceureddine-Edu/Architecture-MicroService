package com.sid.billingservice;

import com.sid.billingservice.Model.Customer;
import com.sid.billingservice.Model.Product;
import com.sid.billingservice.entities.Bill;
import com.sid.billingservice.entities.ProductItem;
import com.sid.billingservice.feign.CustomerRestClient;
import com.sid.billingservice.feign.ProductRestClient;
import com.sid.billingservice.repositories.BillRepository;
import com.sid.billingservice.repositories.ProductItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.PagedModel;

import java.util.Date;
import java.util.Random;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(BillRepository billRepository,
                            ProductItemRepository productItemRepository,
                            CustomerRestClient customerRestClient,
                            ProductRestClient productRestClient)
    {
        return args -> {
            // Retourner un customer a partir de la DB du MicroService customer-service
            Customer customer = customerRestClient.getCustomerById(1l);// Puisque c'est un Long

            // Creer une nouvelle facture
            Bill bill1 = billRepository.save(new Bill(
                            null,new Date(),null,customer.getId(),null));

            // Retouner tout les Produits a partir de la DB du MicroService inventory-service
            PagedModel<Product> productPagedModel = productRestClient.pageProducts();

            productPagedModel.forEach(inventoryServiceProduct ->
            {
                ProductItem productItem = new ProductItem();
                productItem.setQuantity(1 + new Random().nextInt(100));
                productItem.setPrice(inventoryServiceProduct.getPrice());
                productItem.setBill(bill1);
                productItem.setProductID(inventoryServiceProduct.getId());
                productItemRepository.save(productItem);
            });
        };
    };
}
