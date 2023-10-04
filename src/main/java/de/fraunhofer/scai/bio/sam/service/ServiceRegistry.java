package de.fraunhofer.scai.bio.sam.service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service 
@Scope(value = SCOPE_SINGLETON)
public class ServiceRegistry {

    List<TerminologyService> terminologyServiceList;
    List<ConceptService> conceptServiceList;

    public ServiceRegistry() {
        terminologyServiceList = new ArrayList<>();   
        conceptServiceList = new ArrayList<>();   
    }

    public void registerConceptService(ConceptService conceptService) {
        getConceptServiceList().add(conceptService);
    }

    public List<ConceptService> getConceptServiceList() {
        return conceptServiceList;
    }
    
    public void registerTerminologyService(TerminologyService terminologyService) {
        getTerminologyServiceList().add(terminologyService);
    }

    public List<TerminologyService> getTerminologyServiceList() {
        return terminologyServiceList;
    }
    
}
