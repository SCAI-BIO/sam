package de.fraunhofer.scai.bio.sam.service.impl;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import de.fraunhofer.scai.bio.sam.domain.DAO.Concept;
import de.fraunhofer.scai.bio.sam.domain.DAO.Mapping;
import de.fraunhofer.scai.bio.sam.service.impl.ols.OLSConceptServiceImpl;

import static org.junit.Assert.*;

/**
 * ConceptServiceImplTest_OXO
 * <p>
 * TODO: Add javadoc
 *
 * @author Johannes Darms <johannes.darms@scai.fraunhofer.de>
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {TestConfigOLS.class})
public class ConceptServiceImplTest_OXO {
    
    @Autowired
    OLSConceptServiceImpl A;

    @Test
    @Ignore
    public void testOXO(){
    
        
        Concept concept=new Concept();
        //EFO:0001360
        concept.setLocalID("0001360");
        concept.setDefiningTerminology("EFO");
        
        
        Page<Mapping> B =A.getMappings(concept, Mapping.MAPPING_TYPE.CLOSE, PageRequest.of(0,100));
        assertTrue(!B.getContent().isEmpty());
        assertEquals(1,B.getContent().size());
        
    }

}