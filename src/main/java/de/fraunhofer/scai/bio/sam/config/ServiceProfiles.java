package de.fraunhofer.scai.bio.sam.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.fraunhofer.scai.bio.sam.service.CurieService;
import de.fraunhofer.scai.bio.sam.service.ServiceRegistry;
import de.fraunhofer.scai.bio.sam.service.impl.InternalTerminologyService;
import de.fraunhofer.scai.bio.sam.service.impl.ProminerTerminologyService;
import de.fraunhofer.scai.bio.sam.service.impl.ols.OLSConceptServiceImpl;
import de.fraunhofer.scai.bio.sam.service.impl.ols.OLSTerminologyServiceImpl;

/**
 * checks the configuration and starts (concept and terminology) services that are properly configured
 * 
 * @author Marc Jacobs
 *
 */
@Configuration
public class ServiceProfiles {
    
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    CurieService curieService;
    
    @Autowired
    OlsProperties olsProperties;
    
    @Autowired
    LoincProperties loincProperties;

    @Autowired
    ItsProperties itsProperties;
    
    @Autowired
    JpmProperties jpmProperties;
    
    @Autowired 
    ServiceRegistry serviceRegistry;
    
    @Bean("olsProfile")
    public boolean setOLSProfile() {
        if(!olsProperties.isEnabled() || olsProperties.getUrl() == null || olsProperties.getUrl().isEmpty()) {
            logger.info(" >> switched off OLS");
            return false;
            
        } else {

            logger.info(" >> switched on OLS on {}", olsProperties.getUrl());

            AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
            ctx.getEnvironment().setActiveProfiles("OLS");
            ctx.refresh();
            ctx.close();
            
            for(String url : olsProperties.getUrl()) {
                OLSTerminologyServiceImpl tols = new OLSTerminologyServiceImpl(url);            
                serviceRegistry.registerTerminologyService(tols);
            }

            for(String url : olsProperties.getUrl()) {
                OLSConceptServiceImpl cols = new OLSConceptServiceImpl(url, olsProperties.getMaxPageSize());            
                serviceRegistry.registerConceptService(cols);
            }

            return true;
        }
    }
    
    @Bean("loincProfile")
    public boolean setLoincProfile() {
        if(!loincProperties.isEnabled() || loincProperties.getUrl() == null || loincProperties.getUrl().isEmpty()) {
            logger.info(" >> switched off Loinc");
            return false;
            
        } else {

            logger.info(" >> switched on Loinc on {}", loincProperties.getUrl());

            AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
            ctx.getEnvironment().setActiveProfiles("LOINC");
            ctx.refresh();
            ctx.close();
            
            return true;
        }
    }
   
    @Bean("itsProfile")
    public boolean setInternalTerminologyProfile() {
        if(!itsProperties.isEnabled()) {
            logger.info(" >> switched off internal terminology service");
            return false;
            
        } else {

            logger.info(" >> switched on internal terminology service with {}, {}", itsProperties.getConcepts(), itsProperties.getTerminologies());

            AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
            ctx.getEnvironment().setActiveProfiles("ITS");
            ctx.refresh();
            ctx.close();
            
            InternalTerminologyService its = new InternalTerminologyService(itsProperties);           
            serviceRegistry.registerConceptService(its);
            serviceRegistry.registerTerminologyService(its);
            
            return true;
        }
    }
   
    @Bean("jpmProfile")
    public boolean setProminerTerminologyProfile() {
        if(!jpmProperties.isEnabled()) {
            logger.info(" >> switched off Prominer terminology service");
            return false;
            
        } else {

            logger.info(" >> switched on Prominer terminology service with {}, {}", jpmProperties.getSynFileFolder(), jpmProperties.getTerminologies());

            AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
            ctx.getEnvironment().setActiveProfiles("JPM");
            ctx.refresh();
            ctx.close();
            
            ProminerTerminologyService jpms = new ProminerTerminologyService(jpmProperties, curieService);           
            serviceRegistry.registerConceptService(jpms);
            serviceRegistry.registerTerminologyService(jpms);
            
            return true;
        }
    }
}
