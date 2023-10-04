package de.fraunhofer.scai.bio.sam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.fraunhofer.scai.bio.sam.domain.DAO.Terminology;
import de.fraunhofer.scai.bio.sam.service.impl.ols.json.JSONDeserializerTerminology;

import org.junit.Test;

import static org.junit.Assert.*;

public class JSONDeserializerTerminologyTest {

    @Test
    public void deserialize() throws Exception {
        String json = "{\n" +
                "                \"ontologyId\" : \"go\",\n" +
                "                \"loaded\" : \"2018-04-06T02:03:06.272+0000\",\n" +
                "                \"updated\" : \"2018-04-06T02:03:06.272+0000\",\n" +
                "                \"status\" : \"LOADED\",\n" +
                "                \"message\" : \"\",\n" +
                "                \"version\" : null,\n" +
                "                \"numberOfTerms\" : 49450,\n" +
                "                \"numberOfProperties\" : 66,\n" +
                "                \"numberOfIndividuals\" : 0,\n" +
                "                \"config\" : {\n" +
                "            \"id\" : \"http://purl.obolibrary.org/obo/go.owl\",\n" +
                "                    \"versionIri\" : \"http://purl.obolibrary.org/obo/go/releases/2018-04-05/go.owl\",\n" +
                "                    \"title\" : \"Gene Ontology\",\n" +
                "                    \"namespace\" : \"go\",\n" +
                "                    \"preferredPrefix\" : \"GO\",\n" +
                "                    \"description\" : \"An ontology for describing the function of genes and gene products\",\n" +
                "                    \"homepage\" : null,\n" +
                "                    \"version\" : \"2018-04-05\",\n" +
                "                    \"mailingList\" : null,\n" +
                "                    \"creators\" : [ ],\n" +
                "            \"annotations\" : {\n" +
                "                \"license\" : [ \"\" ],\n" +
                "                \"default-namespace\" : [ \"gene_ontology\" ],\n" +
                "                \"has_obo_format_version\" : [ \"1.2\" ],\n" +
                "                \"comment\" : [ \"Includes Ontology(OntologyID(OntologyIRI(<http://purl.obolibrary.org/obo/go/never_in_taxon.owl>))) [Axioms: 18 Logical Axioms: 0]\", \"cvs version: $Revision: 38972$\", \"Includes Ontology(OntologyID(Anonymous-35)) [Axioms: 228 Logical Axioms: 226]\" ]\n" +
                "            },\n" +
                "            \"fileLocation\" : \"http://purl.obolibrary.org/obo/go.owl\",\n" +
                "                    \"reasonerType\" : \"EL\",\n" +
                "                    \"oboSlims\" : true,\n" +
                "                    \"labelProperty\" : \"http://www.w3.org/2000/01/rdf-schema#label\",\n" +
                "                    \"definitionProperties\" : [ \"http://purl.obolibrary.org/obo/IAO_0000115\" ],\n" +
                "            \"synonymProperties\" : [ \"http://www.geneontology.org/formats/oboInOwl#hasExactSynonym\" ],\n" +
                "            \"hierarchicalProperties\" : [ \"http://purl.obolibrary.org/obo/BFO_0000050\" ],\n" +
                "            \"baseUris\" : [ \"http://purl.obolibrary.org/obo/GO_\" ],\n" +
                "            \"hiddenProperties\" : [ ],\n" +
                "            \"internalMetadataProperties\" : [ ],\n" +
                "            \"skos\" : false\n" +
                "        },\n" +
                "        \"_links\" : {\n" +
                "            \"self\" : {\n" +
                "                \"href\" : \"https://www.ebi.ac.uk/ols/api/ontologies/go\"\n" +
                "            },\n" +
                "            \"terms\" : {\n" +
                "                \"href\" : \"https://www.ebi.ac.uk/ols/api/ontologies/go/terms\"\n" +
                "            },\n" +
                "            \"properties\" : {\n" +
                "                \"href\" : \"https://www.ebi.ac.uk/ols/api/ontologies/go/properties\"\n" +
                "            },\n" +
                "            \"individuals\" : {\n" +
                "                \"href\" : \"https://www.ebi.ac.uk/ols/api/ontologies/go/individuals\"\n" +
                "            }\n" +
                "        }\n" +
                "        \n" +
                "        \n" +
                "    }";
    
        Gson gson = new GsonBuilder().registerTypeAdapter(Terminology.class,new JSONDeserializerTerminology()).create();
        Terminology terminology = gson.fromJson(json, Terminology.class);
        assertEquals("go",terminology.getOlsID());
        assertEquals("Gene Ontology",terminology.getLongName());
        assertEquals("go",terminology.getShortName());
        assertEquals("http://purl.obolibrary.org/obo/GO_",terminology.getIri());
        assertEquals("An ontology for describing the function of genes and gene products",terminology.getDescription());
    }
}