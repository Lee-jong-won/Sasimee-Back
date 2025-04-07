package com.example.Sasimee_Back.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    //http://[server_ip]:8080/swagger-ui.html

    @Bean
    public OpenAPI openAPI() {

        final Info info = new Info()
                .title("Sasimee API")
                .version("v1.0.0")
                .description("사심이 API");

        return new OpenAPI()
                .info(info);
    }

}
