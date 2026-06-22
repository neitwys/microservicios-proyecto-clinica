package com.clinica.examenes.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration

public class swaggerConfig {
    @Bean
    public OpenAPI RecetaOpenAPI(){
        return new OpenAPI() 
            .info(new Info()
                        .title("API EXAMENES")
                        .description("api rest para examenes ")
                        .version("1.0")
                        .contact(new Contact()
                                    .name("clinica")
                                    .email("Soporte@gmail.com"))

                    
                    
                    
                    
                    
                    
                    
                    
                    )  ;
    }

    

}
