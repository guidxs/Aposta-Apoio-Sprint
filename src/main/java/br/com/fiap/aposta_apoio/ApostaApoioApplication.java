package br.com.fiap.aposta_apoio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ApostaApoioApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApostaApoioApplication.class, args);
    }
    @Bean
    public RestTemplate restTemplate() { return new RestTemplate(); }
}
