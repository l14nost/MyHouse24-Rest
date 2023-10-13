package lab.space.my_house_24_rest.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "li4nost",
                        email = "amirbanor@gmail.com"
                ),
                description = "OpenApi documentation for MyHouse24"
        ),
        servers = {
                @Server(
                        description = "MyHouse24",
                        url = "http://localhost:8083/myhouse24-amirb-nikitaf/rest"
                ),

        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JwtAuth",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
