package de.fraunhofer.scai.bio.sam.service;


import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import de.fraunhofer.scai.bio.sam.domain.DAO.Terminology;
import de.fraunhofer.scai.bio.sam.service.impl.delegator.TerminologyServiceDelegator;

/**
 * Given an OBO and a Set of prefixDefinition (SAPRQL/RDF like)<prefix,OBO> find the prefix which has the largest
 * overlap with the given OBO. This is considered the defining prefix.
 */
@Component
// that is also the default of Component but to be sure this is always singleton!
@Scope(value = SCOPE_SINGLETON)
public class DetectOriginatingTerminology {

    TerminologyServiceDelegator service;
    
    private Map<String,String> prefixes;

    @Autowired
    public DetectOriginatingTerminology(TerminologyServiceDelegator service) {
        if (service == null) return;
        this.service = service;
        construct();
    }
    
    // make sure this is refetched once every hour during office times.
    //"0 0 7,20 * * *" = 7:00 AM and 8:00 PM every day.
    @Scheduled(cron="0 0 7-20 * * *")
    public void construct() {
        prefixes = new HashMap<>();
        
        Page<Terminology> page=null;
        Pageable request;
        do {
            request = (page !=null) ? page.nextPageable() : PageRequest.of(0,500);
            page = service.getAllTerminologies(request);
            for (Terminology terminology: page.getContent()) {
                if(terminology.getIri() != null) {
                    prefixes.put(terminology.getShortName(),terminology.getIri().toLowerCase());
                }
            }
        } while (page.hasNext());
        prefixes.put("owl","http://www.w3.org/2002/07/owl#");
    }
    
    /**
     * This should be a Trie/SuffixArray but this will work for the time being.
     * @param IRI
     * @return
     */
    public Map.Entry<String,String> getDefiningTerminologyPrefix(String IRI){
        String longestMatch="";
        List<String> alsoLongest = new ArrayList<>();
        for (Map.Entry<String,String> entry : prefixes.entrySet()){
            if(IRI.startsWith(entry.getValue())){
                // got a valid prefix
                if(longestMatch.length()<entry.getValue().length()){
                    // new longest match
                    longestMatch=entry.getValue();
                    alsoLongest = new ArrayList<>();
                }else if(longestMatch.length()==entry.getValue().length()){
                    if(!longestMatch.equals(entry.getValue())){
                        // now we got two prefix matching with same length...
                        alsoLongest.add(entry.getValue());
                        // this is no good....
                    }
                }
            }
        }
        if(alsoLongest.isEmpty()){
            final String longest = longestMatch;
            Set<String> values = prefixes.entrySet()
                    .stream()
                    .filter(entry -> Objects.equals(entry.getValue(), longest))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());
            if(values.size()==1){
                Map.Entry<String,String> entrt = new AbstractMap.SimpleEntry<String, String>( ((String) values
                        .toArray()[0]),longest);
                return entrt;
            }else{
                // not reachable
                return null;
            }
        }else{
            // ambig
            return null;
        }
    }
    
    
    
}
