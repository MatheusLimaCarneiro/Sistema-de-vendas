package estudo.jpa.spring.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Vendas API")
                        .description("Api de projetos de Vendas")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Matheus Lima Carneiro")
                                .url("https://github.com/MatheusLimaCarneiro")
                                .email("matheuslimacarneirors@gmail.com")));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .packagesToScan("estudo.jpa.spring.rest.controller")
                .build();
    }

}
