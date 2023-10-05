/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.fraunhofer.scai.bio.sam;

import de.fraunhofer.scai.bio.sam.domain.DAO.Concept;
import de.fraunhofer.scai.bio.sam.domain.DAO.Terminology;
import de.fraunhofer.scai.bio.sam.service.ConceptService;
import de.fraunhofer.scai.bio.sam.service.TerminologyService;
import de.fraunhofer.scai.bio.sam.service.impl.TestConfigOLS;
import de.fraunhofer.scai.bio.sam.service.impl.delegator.TerminologyServiceDelegator;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {TestConfigOLS.class})
public class ApplicationTest {


    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    TerminologyServiceDelegator tService;

    @Autowired
    TerminologyService service;

    @Autowired
    ConceptService service2;

    @Test
    @Ignore
    public void loadAllTerms() throws IOException {
        Page<Terminology> page = null;
        Pageable request;
        do {
            request = (page != null) ? page.nextPageable() : PageRequest.of(0, 500);
            page = tService.getAllTerminologies(request);
            for (Terminology terminology : page.getContent()) {
                logger.info("Terminology:{}", terminology.getOlsID());
                Page<Concept> pageC = null;
                do {
                    for (Concept concept : pageC.getContent()) {
                        logger.info("\tconcept:{}", concept);
                    }
                } while (pageC.hasNext());
            }
        } while (page.hasNext());

    }

}
