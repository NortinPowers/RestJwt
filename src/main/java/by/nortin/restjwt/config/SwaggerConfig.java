package by.nortin.restjwt.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SwaggerConfig {

    @Value("${nortin.openapi.dev-url}")
    private String devUrl;

    @Bean
    public OpenAPI myOpenApi() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");
        Contact contact = new Contact();
        contact.setEmail("A.Nortin@gmail.com");
        contact.setName("Nortin");
        contact.setUrl("https://github.com/NortinPowers");
        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");
        Info info = new Info()
                .title("Tutorial Management API")
                .version("1.0")
                .contact(contact)
                .description("This API provides endpoints for managing databases of books and users and uses protection via jwt-token")
                .license(mitLicense);
//        SecurityRequirement securityRequirement = new SecurityRequirement();
//        securityRequirement.addList("Bearer Authentication");
//        Components components = new Components();
//        components.addSecuritySchemes("Bearer Authentication", createAPIKeyScheme());
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI().addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .info(info)
                .servers(List.of(devServer));
//        return new OpenAPI().addSecurityItem(securityRequirement).info(info).servers(List.of(devServer));
    }

//    private SecurityScheme createAPIKeyScheme() {
//        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
//                .bearerFormat("JWT")
//                .scheme("bearer");
//    }
}

/*
https://www.baeldung.com/openapi-jwt-authentication

@OpenAPIDefinition(
  info =@Info(
    title = "User API",
    version = "${api.version}",
    contact = @Contact(
      name = "Baeldung", email = "user-apis@baeldung.com", url = "https://www.baeldung.com"
    ),
    license = @License(
      name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"
    ),
    termsOfService = "${tos.uri}",
    description = "${api.description}"
  ),
  servers = @Server(
    url = "${api.server.url}",
    description = "Production"
  )
)
@SecurityScheme(
  name = "Bearer Authentication",
  type = SecuritySchemeType.HTTP,
  bearerFormat = "JWT",
  scheme = "bearer"
)

 */
