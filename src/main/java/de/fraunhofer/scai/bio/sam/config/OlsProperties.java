package de.fraunhofer.scai.bio.sam.config;


import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * Bean to expose the configuration properties for OLS to the application.
 */
@Configuration
@ConfigurationProperties
public class OlsProperties {
    
    @NotNull
    @Value("${ols.enable}")
    private boolean enabled = false;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Value("#{'${ols.url}'.split(',')}")
    private List<String> url;

    @NotEmpty
    @Value("${ols.maxsize}")
    private int maxPageSize;

    public List<String> getUrl() {
        return url;
    }

    public int getMaxPageSize() {
        return maxPageSize;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
}
