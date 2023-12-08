package com.utc.formut.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    private ApplicationProperties applicationProperties;

    public SwaggerConfiguration(ApplicationProperties applicationProperties){
        this.applicationProperties = applicationProperties;
    }

    @Bean
    public GroupedOpenApi registerV1(){
        return GroupedOpenApi.builder().group("v1").pathsToMatch("/**")
                .addOpenApiCustomiser(openApi -> openApi.setInfo(getApiInfo("all"))).build();
    }

    @Bean
    public OpenAPI openAPI(){
        return new OpenAPI();
    }


    public Info getApiInfo(String version){

        return new Info().title(applicationProperties.swagger.getTitle())
                .description(applicationProperties.swagger.getDescription()).version(version)
                .contact(new Contact().email(applicationProperties.swagger.getContactEmail()));
    }
}
