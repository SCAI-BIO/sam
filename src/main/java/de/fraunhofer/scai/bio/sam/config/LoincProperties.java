package de.fraunhofer.scai.bio.sam.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


/**
 * Bean to expose the configuration properties for Loinc to the application.
 */
@Configuration
@ConfigurationProperties
public class LoincProperties {
    
    @NotNull
    @Value("${loinc.enable}")
    private boolean enabled = false;
    
    @NotEmpty
    @Value("${loinc.url}")
    private String url;
    
    public boolean isEnabled() {
        return enabled;
    }

    public String getUrl() {
        return url;
    }
}
