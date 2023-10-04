/**
 *
 */
package de.fraunhofer.scai.bio.sam.service;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import de.fraunhofer.scai.bio.sam.domain.DAO.Concept;

import org.springframework.util.ResourceUtils;

/**
 * @author Marc Jacobs
 * <p>
 * this service provides CURIEs for IRIs
 */
@Service
@Scope(value = SCOPE_SINGLETON)
public class CurieService {

    Logger log = LoggerFactory.getLogger(getClass());

    Map<String, String> prefixes;
    Map<String, String> terminologyIDs;

    public Map<String, String> getTerminologyIDs() {
        return terminologyIDs;
    }

    DefaultPrefixManager pm;

    public DefaultPrefixManager getPrefixManager() {
        return pm;
    }

    public Map<String, String> getPrefixes() {
        return prefixes;
    }

    public void addPrefix(String prefix, String iri) {
        this.prefixes.put(prefix, iri);
    }

    /**
     * The constructor initializes a <code>TreeMap</code> with prefixes to be used as CURIE
     */
    public CurieService() {
        prefixes = new TreeMap<String, String>();
        terminologyIDs = new TreeMap<String, String>();
        pm = new DefaultPrefixManager();

        try {
            log.info(" >> starting Curie Service...");
            Path prefixesPath = ResourceUtils.getFile("/usr/share/sam/configurations/prefixes.tsv").toPath();
            if (new File(prefixesPath.toString()).isFile()) {
                log.info(" >>> successfully loaded prefix file from {}", prefixesPath);
            } else {
                log.error(" >>> Configured prefix file not found in path {}", prefixesPath.toString());
                log.info(" >>> Trying alternative location from local resources");
                prefixesPath = ResourceUtils.getFile("classpath:prefixes.tsv").toPath();
            }
            log.info(" >>> parsing prefixes from {} ...", prefixesPath);
            InputStream in = Files.newInputStream(prefixesPath);

            try {
                CSVParser parser = CSVParser.parse(in, Charset.forName("UTF-8"), CSVFormat.TDF);
                parser.forEach(record -> {
                    if (record.isConsistent() && record.size() == 2) {
                        if (!record.get(0).isEmpty() && !record.get(1).isEmpty()) {
                            prefixes.put(record.get(0), record.get(1));
                            pm.setPrefix(record.get(0), record.get(1));
                        } else {
                            log.error("invalid record: {}", record.toString());
                        }
                    } else {
                        log.error("invalid record: {}", record.toString());
                    }
                });
            } catch (Exception e) {
                log.error(" >>> wrong prefixes.tsv file");
                log.debug(e.getLocalizedMessage(), e);
            }

            in.close();

            log.info(" >>> got {} prefixes.", prefixes.size());
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            log.debug(e.getLocalizedMessage(), e);
        }
    }

    /**
     * set  correct IRI and CURIE (localId)
     *
     * @param concept       a <code>Concept</code>.
     * @param conceptId     its corresponding id (<code>String</>)
     * @param terminologyId the terminology where it has been integrated (<code>String</code>)
     */
    public void setCurieAndId(Concept concept, String conceptId, String terminologyId) {

        log.debug(" >>> set curie&id {} {}", conceptId, terminologyId);
        if (concept == null) return;

        if (concept.getIri() != null) {
            log.trace(concept.toString());
            log.trace("iri: " + concept.getIri());
            concept.setLocalID(getCurie(concept.getIri()));
            log.trace("local id " + concept.getLocalID());
            log.trace(concept.toString());

            if (terminologyId == null
                    && concept.getLocalID() != null && concept.getLocalID().contains(":")) {
                terminologyId = concept.getLocalID().split(":")[0];
            }

            if (terminologyId != null) {
                concept.setDefiningTerminology(terminologyId);
            }

        } else if (conceptId.startsWith("http://") || conceptId.startsWith("https://")) {
            // this is an IRI
            concept.setLocalID(getCurie(conceptId));
            concept.setDefiningTerminology(terminologyId);

        } else {
            log.debug("CURIE");
            // this must be a CURIE
            if (conceptId.contains(":")) {
                String[] parts = conceptId.split(":");
                if (parts.length > 2) {
                    log.error("invalid CURIE {}", conceptId);
                } else {
                    concept.setIri(getPrefix(parts[0]) + parts[1]);
                    concept.setDefiningTerminology(terminologyId);

                }
            }
        }

        log.debug(" >>> {}", concept);
    }

    public String getPrefix(String terminologyName) {

        if (prefixes.get(terminologyName) != null) {
            return prefixes.get(terminologyName);

        } else if (prefixes.get(terminologyName.toLowerCase()) != null) {
            return prefixes.get(terminologyName.toLowerCase());

        } else if (prefixes.get(terminologyName.toUpperCase()) != null) {
            return prefixes.get(terminologyName.toUpperCase());

        } else {
            log.warn(" >> Couldn't find prefix for {}", terminologyName);
            return null;
        }
    }

    public String getCurie(String iri) {
        String curie = null;
        for (Map.Entry<String, String> entry : prefixes.entrySet()) {
            if (iri.toLowerCase().startsWith(entry.getValue().toLowerCase())) {
                if (curie == null || curie.length() > iri.length() - entry.getValue().length()) {
                    curie = entry.getKey() + ":" + iri.substring(entry.getValue().length());
                }
            }
        }
        // prefix not found
        if (curie == null) {
            log.warn(" >>> Could not get curie for iri {}", iri);
        }
        return curie;

    }

}
