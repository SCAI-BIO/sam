package de.fraunhofer.scai.bio.sam.service.impl;

import de.fraunhofer.scai.bio.sam.service.DetectOriginatingTerminology;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {TestConfigOLS.class})
public class DetectOriginatingTerminologyTest {
    
    @Autowired
    DetectOriginatingTerminology detectOriginatingTerminology;

    @Test
    @Ignore
    public void getDefiningTerminologyPrefix() throws Exception {
        String iri = "http://purl.obolibrary.org/obo/GO_0043170";
        
        Map.Entry<String, String> prefix = detectOriginatingTerminology.getDefiningTerminologyPrefix(iri);
        assertEquals("go",prefix.getKey());
        
    }
    
}