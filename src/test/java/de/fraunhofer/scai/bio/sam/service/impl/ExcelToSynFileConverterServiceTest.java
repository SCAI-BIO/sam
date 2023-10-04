package de.fraunhofer.scai.bio.sam.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import de.fraunhofer.scai.bio.sam.service.ConceptService;
import de.fraunhofer.scai.bio.sam.service.CurieService;
import de.fraunhofer.scai.bio.sam.service.ServiceRegistry;
import de.fraunhofer.scai.bio.sam.service.TerminologyService;
import de.fraunhofer.scai.bio.sam.service.exceptions.NotFoundException;
import de.fraunhofer.scai.bio.sam.service.impl.delegator.ConceptServiceDelegator;
import de.fraunhofer.scai.bio.sam.service.impl.delegator.TerminologyServiceDelegator;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        ExcelToSynFileConverterService.class,
        ProminerExportService.class,
        TerminologyService.class,
        TerminologyServiceDelegator.class,
        CurieService.class,
        ServiceRegistry.class,
        ConceptService.class,
        ConceptServiceDelegator.class,
        CacheResolver.class})
@ContextConfiguration(classes = {TestConfigOLS.class})
@TestPropertySource(locations="classpath:application.properties")
public class ExcelToSynFileConverterServiceTest {

    @Autowired
    ExcelToSynFileConverterService excelToSynFileConverterService;

    @Test
    public void test_convertResourceToSynFile() throws IOException, NotFoundException {
        File resource = new File("src/test/resources/PD data mapping.xlsx");
        String storeDestination = "terminologies/PD_parkinsons_disease.syn";
        String terminology = "Parkinsons Disease";
        Optional<String> terminologyPrefixShortForm = Optional.of("PD");
        String version = "0.0.1";
        String idSourceColName = "id"; // should not be present and instead be auto-generated
        String prefLabelSourceColName = "Feature";
        String descriptionSourceColName = "Description"; // Sanity Check, should not be present either
        File f = excelToSynFileConverterService.convertResourceToSynFile(resource, storeDestination, terminology,
                terminologyPrefixShortForm, version, idSourceColName, prefLabelSourceColName, descriptionSourceColName);
        Assert.assertTrue(f.exists());
        this.transfer(f, new File(storeDestination));
    }

    private void transfer(File tmpFile, File targetFile) {
        try (
                FileInputStream inStream = new FileInputStream(tmpFile);
                FileOutputStream outStream = new FileOutputStream(targetFile)
        ) {
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
            System.out.println("Temporary file transferred to disk successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}