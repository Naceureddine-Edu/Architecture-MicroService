package com.sid.gatewayservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.route.*;
import org.springframework.cloud.gateway.route.builder.*;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }

    // 1ere facon c'est avec le fichier application.yml

 //2eme facon de faire pour configurer la gateway d'une facon static(stipule qu'on connait tout les routes)
    /*
    @Bean
    RouteLocator routeLocator(RouteLocatorBuilder builder)
    {
        return builder.routes()
                .route((r)-> {
                    return r.path("/customers/**").uri("http://localhost:8081/");
                })
                .route((r)-> {
                    return r.path("/products/**").uri("http://localhost:8082/");
                })
                .build();
    }
     */

    //3eme facon de faire pour configurer la gateway d'une facon static
    // (stipule qu'on deja creer eureka server et qu'on connait le nom des microservices)
    @Bean
    RouteLocator routeLocator(RouteLocatorBuilder builder)
    {
        return builder.routes()
                .route((r)-> {
                    return r.path("/customers/**").uri("lb://CUSTOMER-SERVICE");
                })
                .route((r)-> {
                    return r.path("/products/**").uri("lb://INVENTORY-SERVICE");
                })
                .build();
    }

    //4eme facon de faire mais cette fois d'une facon dynamique c'est a dire qu'on a pas a connaitre
    // le nom des micorservices ou la machine ou ils se trouvent
    @Bean
    DiscoveryClientRouteDefinitionLocator dynamicRoutesConfiguration
                                          (ReactiveDiscoveryClient rdc,DiscoveryLocatorProperties dlp)
    {
        return new DiscoveryClientRouteDefinitionLocator(rdc,dlp);
    }
}
