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

    //http://localhost:8080/swagger-ui.html

    @Value("${sasimee.openapi.dev-url}")
    private String devUrl;
    @Value("${sasimee.openapi.prod-url}")
    private String prodUrl;


    @Bean
    public OpenAPI openAPI() {
        final Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.description("개발 환경 서버 URL");

        final Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.description("운영 환경 서버 URL");

        final Info info = new Info()
                .title("Sasimee API")
                .version("v1.0.0")
                .description("사심이 API");

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer, prodServer));
    }

}
