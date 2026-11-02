package com.codigofacilito.gps.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.hateoas.server.core.DefaultLinkRelationProvider;

/**
 * Configuración OpenAPI para el microservicio de ingesta GPS.
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
                        .title("OmniTrack Logistics - GPS Ingestion Service API")
                        .version("1.0.0")
                        .description("Microservicio para la ingesta de telemetría y geoposicionamiento en tiempo real. " +
                                "Soporta procesamiento individual y por lotes, streaming reactivo (SSE) y WebSockets.")
                        .contact(new Contact().name("Equipo de Telemetría").email("gps@fleettracking.com")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")));
    }
}
