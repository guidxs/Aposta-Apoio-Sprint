package br.com.fiap.aposta_apoio.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração mínima do OpenAPI/Swagger.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Aposta Apoio")
                        .version("1.0.0")
                        .description("API REST para apoio a pessoas com vício em apostas"));
    }
}

