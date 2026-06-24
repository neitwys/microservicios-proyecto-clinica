package com.clinica.autenticacion.swagger;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration

public class SwaggerConfig {
    @Bean
    public OpenAPI AutenticacionOpenAPI(){
        return new OpenAPI() 
            .info(new Info()
                        .title("API autenticacion")
                        .description("api rest para autenticacion ")
                        .version("1.0")
                        .contact(new Contact()
                                    .name("clinica")
                                    .email("Soporte@gmail.com"))

                    
                    
                    
                    
                    
                    
                    
                    
                    )  ;
    }

    

}
