package com.sid.billingservice.controllers;


import com.sid.billingservice.Model.Customer;
import com.sid.billingservice.Model.Product;
import com.sid.billingservice.entities.Bill;
import com.sid.billingservice.feign.CustomerRestClient;
import com.sid.billingservice.feign.ProductRestClient;
import com.sid.billingservice.repositories.BillRepository;
import com.sid.billingservice.repositories.ProductItemRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class BillingRestController
{
    BillRepository billRepository;
    ProductItemRepository productItemRepository;
    CustomerRestClient customerRestClient;
    ProductRestClient productRestClient;

    public BillingRestController(BillRepository billRepository,
                                 ProductItemRepository productItemRepository,
                                 CustomerRestClient customerRestClient,
                                 ProductRestClient productRestClient)
    {
        this.billRepository = billRepository;
        this.productItemRepository = productItemRepository;
        this.customerRestClient = customerRestClient;
        this.productRestClient = productRestClient;
    }


    @GetMapping(path = "/fullBill/{id}")
    public Bill getBill(@PathVariable(name = "id") Long id)
    {
        Bill bill = billRepository.findById(id).get();
        // Va chercher le CUSTOMER dans le Microservice customer-service qui correspand a cette facture
        Customer customer = customerRestClient.getCustomerById(bill.getCustomerID());// customer-service
        bill.setCustomer(customer);

        bill.getProductItems().forEach(productItem -> {
            Product product = productRestClient.geProductById(productItem.getProductID());
            productItem.setProductName(product.getName());
            productItem.setProduct(product);
        });

        return bill;
    }
}
