package com.codigofacilito.fleet.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.hateoas.server.core.DefaultLinkRelationProvider;

/**
 * Configuración de OpenAPI 3 / Swagger para documentación de la API (Criterio de Evaluación #8).
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public LinkRelationProvider linkRelationProvider() {
        return new DefaultLinkRelationProvider();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "basicAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("OmniTrack Logistics - Fleet Management Service API")
                        .version("1.0.0")
                        .description("Microservicio de administración integral de flotas, vehículos, conductores, envíos y usuarios. " +
                                "Implementa arquitectura reactiva con Spring WebFlux, seguridad Basic Auth y enlaces hipermedia HATEOAS.")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo Logístico")
                                .email("dev@fleettracking.com"))
                        .license(new License().name("Apache 2.0").url("https://spring.io")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")
                                .description("Autenticación HTTP Basic. Credenciales demo: admin / admin123 o user / user123")));
    }
}
