package de.fraunhofer.scai.bio.sam.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import de.fraunhofer.scai.bio.sam.domain.DAO.Concept;
import de.fraunhofer.scai.bio.sam.domain.task.ExportTask;
import de.fraunhofer.scai.bio.sam.service.SynFileConverterService;
import de.fraunhofer.scai.bio.sam.service.exceptions.NotFoundException;

@Service
public class ExcelToSynFileConverterService implements SynFileConverterService {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    ProminerExportService prominerExportService;

    @Override
    public File convertResourceToSynFile(File resource, String storeDestination, String terminology,
                                         Optional<String> terminologyPrefixShortForm,
                                         String version,
                                         String idSourceColName,
                                         String prefLabelSourceColName,
                                         String descriptionSourceColName)
            throws IOException, NotFoundException {

        FileInputStream file = new FileInputStream(resource);
        Workbook workbook = new XSSFWorkbook(file);

        int idSourceColIdx = -1;
        int prefLabelSourceColIdx = -1;
        int descriptionSourceColIdx = -1;

        List<Concept> concepts = new ArrayList<>();

        // iterate over all sheets
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            try {

                Sheet sheet = workbook.getSheetAt(i);

                // first row should be headers
                Row headers = sheet.getRow(0);
                for (Cell cell : headers) {
                    if (cell.getStringCellValue().equals(idSourceColName)) {
                        idSourceColIdx = cell.getColumnIndex();
                    } else if (cell.getStringCellValue().equals(prefLabelSourceColName)) {
                        prefLabelSourceColIdx = cell.getColumnIndex();
                    } else if (cell.getStringCellValue().equals(descriptionSourceColName)) {
                        descriptionSourceColIdx = cell.getColumnIndex();
                    }
                }

                // now iterate over body of relevant columns
                for (int j = 1; j < sheet.getLastRowNum(); j++) {
                    Concept concept = new Concept();
                    concept.setDefiningTerminology(terminology);
                    Row row = sheet.getRow(j);
                    String prefLabel = "";
                    if (prefLabelSourceColIdx >= 0) {
                        prefLabel = row.getCell(prefLabelSourceColIdx).getStringCellValue();
                        // ignore empty cells
                        if (prefLabel.isEmpty()) {
                            continue;
                        }
                        concept.setPrefLabel(prefLabel);
                    } else {
                        throw new RuntimeException("PrefLabel Column not found in provided Excel File, can't be null.");
                    } if (descriptionSourceColIdx >= 0) {
                        String description = row.getCell(descriptionSourceColIdx).getStringCellValue();
                        concept.setDescription(description);
                    } if (idSourceColIdx >= 0) {
                        String id = row.getCell(idSourceColIdx).getStringCellValue();
                        concept.setLocalID(id);
                    } else {
                        // generate ID based on hashes
                        String prefix = "";
                        if (terminologyPrefixShortForm.isPresent()) {
                            prefix = terminologyPrefixShortForm.get();
                        } else {
                            // take first 5 letters of terminology name
                            prefix = terminology.toUpperCase().substring(0, 4);
                        }
                        String suffix = DigestUtils.md5Hex(prefLabel).toUpperCase();
                        concept.setLocalID(prefix + ":" + suffix);
                    }
                    concepts.add(concept);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // store as syn file
        Page<Concept> conceptPage = new PageImpl<>(concepts);
        File synFile = new File("/terminologies/" + terminology + ".syn");
        ExportTask task = new ExportTask(String.valueOf(System.currentTimeMillis()),
                terminology, version, synFile.getName());
        File f = prominerExportService.writeConceptsOfTerminologyToFile(task, conceptPage, task.getFilename());
        log.info("Finished excel conversion & export of terminology " + terminology);
        return f;

    }
}
