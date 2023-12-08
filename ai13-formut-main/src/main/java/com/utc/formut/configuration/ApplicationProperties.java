package com.utc.formut.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="application")
public class ApplicationProperties {

    public final SwaggerDoc swagger = new SwaggerDoc();

    public static class SwaggerDoc{

        public String title = "FormUT";
        public String description = "Trainees form application for AI13 project built with SpringBoot";
        public String contactEmail = "ahmed.lounis@hds.utc.fr";

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getContactEmail() {
            return contactEmail;
        }

        public void setContactEmail(String contactEmail) {
            this.contactEmail = contactEmail;
        }
    }
}
