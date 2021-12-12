package com.sid.customerservice;

import com.sid.customerservice.entities.Customer;
import com.sid.customerservice.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

@SpringBootApplication
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            RepositoryRestConfiguration restConfiguration){
        return args -> {
            restConfiguration.exposeIdsFor(Customer.class);

            customerRepository.save(new Customer(null,"name1","name1@gmail.com"));
            customerRepository.save(new Customer(null,"name2","name2@gmail.com"));
            customerRepository.save(new Customer(null,"name3","name3@gmail.com"));

            customerRepository.findAll().forEach(customer -> System.out.println(customer.toString()));
        };
    }
}
