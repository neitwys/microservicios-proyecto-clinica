package com.mvclinicas.Citas.swagger;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration

public class SwaggerConfig {
    @Bean
    public OpenAPI CitasOpenAPI(){
        return new OpenAPI() 
            .info(new Info()
                        .title("API citas")
                        .description("api rest para citas ")
                        .version("1.0")
                        .contact(new Contact()
                                    .name("clinica")
                                    .email("Soporte@gmail.com"))

                    
                    
                    
                    
                    
                    
                    
                    
                    )  ;
    }

    

}
