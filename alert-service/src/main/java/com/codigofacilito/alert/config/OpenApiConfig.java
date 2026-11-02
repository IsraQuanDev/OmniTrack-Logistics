package com.codigofacilito.alert.config;

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
 * Configuración OpenAPI / Swagger para el microservicio Alert-Service.
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
                        .title("OmniTrack Logistics - Alert Service API")
                        .version("1.0.0")
                        .description("Microservicio de supervisión de flotas, detección de incidentes en tiempo real " +
                                "(velocidad, geocercas, batería, pánico), streaming reactivo SSE y ciclo de vida de alertas.")
                        .contact(new Contact().name("Equipo de Seguridad y Monitoreo").email("alerts@fleettracking.com")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")));
    }
}
