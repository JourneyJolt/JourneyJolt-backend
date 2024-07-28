package dev.cyan.travel.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {
    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost/api/v2");
        server.setDescription("Server development URL");

        Contact myContact = new Contact();
        myContact.setName("Nazarii Ryhus");
        myContact.setEmail("superkriper45@gmail.com");

        Info information = new Info()
                .title("Travel Agency API")
                .version("2.0")
                .description("This API exposes endpoints to manage bookings.")
                .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));
    }
}
