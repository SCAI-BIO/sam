package de.fraunhofer.scai.bio.sam.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCacheFactoryBean;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import de.fraunhofer.scai.bio.sam.config.OlsProperties;
import de.fraunhofer.scai.bio.sam.service.ConceptService;
import de.fraunhofer.scai.bio.sam.service.DetectOriginatingTerminology;
import de.fraunhofer.scai.bio.sam.service.TerminologyService;
import de.fraunhofer.scai.bio.sam.service.impl.delegator.TerminologyServiceDelegator;
import de.fraunhofer.scai.bio.sam.service.impl.ols.OLSConceptServiceImpl;
import de.fraunhofer.scai.bio.sam.service.impl.ols.OLSTerminologyServiceImpl;


@Configuration
public class TestConfigOLS {
        
    //
    //You basically generate getters and add @Bean annotation everywhere
    @Bean
    public DetectOriginatingTerminology getBeanA() {
        return new DetectOriginatingTerminology(getBean());
    }

    @Bean
    public TerminologyServiceDelegator getBean() {
        return new TerminologyServiceDelegator();
    }

    @Bean
    public TerminologyService getBeanE() {
        return new OLSTerminologyServiceImpl(getBeanD().getUrl().get(0));
    }
    
    @Bean
    public ConceptService getBeanC() {
        return new OLSConceptServiceImpl(getBeanD().getUrl().get(0), getBeanD().getMaxPageSize());
    }

    @Bean
    public SimpleCacheManager cacheManager(){
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<Cache> caches = new ArrayList<Cache>();
        caches.add(cacheBean().getObject());
        cacheManager.setCaches(caches );
        return cacheManager;
    }

    @Bean
    public ConcurrentMapCacheFactoryBean cacheBean(){
        ConcurrentMapCacheFactoryBean cacheFactoryBean = new ConcurrentMapCacheFactoryBean();
        cacheFactoryBean.setName("default");
        return cacheFactoryBean;
    }
    
    @Bean
    public OlsProperties getBeanD() {
        return new OlsProperties();
    }
    
}
