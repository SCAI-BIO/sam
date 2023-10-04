package de.fraunhofer.scai.bio.sam.config;

import java.util.List;

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
public class JpmProperties {

    @NotNull
    @Value("${jpm.enable}")
    private boolean enabled = false;
    
    @Value("${jpm.syn-file-folder}")
    private String synFileFolder;

    @Value("#{'${jpm.terminologies}'.split(',')}")
    private List<String> terminologies;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getSynFileFolder() {
        return synFileFolder;
    }

    public List<String> getTerminologies() {
        return terminologies;
    }
}
