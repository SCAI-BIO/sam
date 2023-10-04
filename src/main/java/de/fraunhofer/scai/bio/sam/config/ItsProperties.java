package de.fraunhofer.scai.bio.sam.config;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Bean to expose the configuration properties for internal terminology service to the application.
 *
 * @author Marc Jacobs
 *
 */
@Configuration
@ConfigurationProperties
public class ItsProperties {

    @NotNull
    @Value("${its.enable}")
    private boolean enabled = false;
    
    @Value("${its.concepts}")
    private String concepts;

    @Value("${its.terminologies}")
    private String terminologies;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getConcepts() {
        return concepts;
    }

    public void setConcepts(String concepts) {
        this.concepts = concepts;
    }

    public String getTerminologies() {
        return terminologies;
    }

    public void setTerminologies(String terminologies) {
        this.terminologies = terminologies;
    }
}
