package de.fraunhofer.scai.bio.sam.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Value("${swagger.config.hostname}")
    private String hostName;

    @Value("${server.port}")
    private String port;

    @Bean
    public Docket swaggerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("de.fraunhofer.scai.bio.sam"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .version("7.0")
                        .title("SAM, the semantic aggregation machine")
                        .description("API to retrieve and export Terminologies / Ontologies and their contents. Some API Calls expect and/or return a TerminologyID. This ID is configured by the System and depends on the system configuration. A list of all available Terminologies can be obtained via the API Call `\\terminologies`. Even though the ID is defined by the system reuse of IDs is intended. For example the TerminologyID of the Terminology 'Gene Ontology' should be 'GO'. If a Terminology is missing or uses an uncommon TerminologyID please contact the system administrator. Issued API Calls with a not present TerminologyID will lead to an error. Some API Calls expect and/or return a ConceptID. The ID is either encoded as CURIE (https://www.w3.org/TR/2010/NOTE-curie-20101216/) or a canonical IRI (PURL?!). A CURIE encoding is used if the originating Terminology (the one that initial defined this Concept) is also present in this System, otherwise an IRI is used. However requests issued with an IRI are redirected to a CURIE if possible. However as multiple IRI could be defined for a single Concept this redirect may not work. Please contact the Administrator if you know about a not configured IRI Mapping. If a CURIE is returned the non compact URL can be generated. The used prefix corresponds to a valid TerminologyID in this system and thus the API can be used to obtain the longform? to substitute the prefix. The non compact URL should correspond to the canonical IRI used everywhere else. If a IRI is returned the system may not have all/sufficient information about the Concept. Please issue a request to the given IRI to obtain more information. Alternatively ask the Administrator to also add this Terminology to the System. Please note, Terminologies reuses Concepts from other Terminologies. By that means a Terminology can contain Concepts form multiple Terminologies. For e.g. the Terminology 'ECO' defines new Concepts and also reuse Concepts from 'Gene Ontology'. Due that CURIES are used as request Parameters even tough they duplicate information in some cases. In general ConceptIDs are case sensitive. Non existed ConceptID will lead to an error response. Further if a Concept is used in multiple Terminologies present in this System, the Concept is `merged` meaning all defined properties are combined. If conflicts occur and the originating Terminology (the one that initial defined this Concept) is present the properties of originating are used. If not the majority wins, if no majority is present the longest property wins. Additionally some API Calls allows to configure the encoding of ConceptIDs in responses. The specified encoding is only performed for Concept where a CURIE can be constructed. For others a IRI is returned. Most API Calls allow a Pagination of results via the Parameters page and size. The numbering of pages starts at zero (0). The default page size is 10, the maximal page size is 500. In each response the numberOfPages the totalNumber of Elements as well as current pageNumber are returned. All API Calls have a versionTag Parameter that allows to retrieve properties of a specific version of a Terminology. By default the latest version is selected. A List of available versions for a Terminologies can be obtained via the API Call `\\terminologies\\{ID}\\versionTags`. If an invalid versionTag is supplied the latest version is selected instead.  Some API Calls have a language Parameter that allows to filter the response to only contain properties in a specific language. If no property in the given language is present non is returned. The API expect a language to be encoded as defined in https://tools.ietf.org/html/bcp47. Further an extension 'all' is allowed. If that is given properties in all language are returned. The default language is 'en'.")
                        .license("Fraunhofer SCAI")
                        .licenseUrl("https://www.scai.fraunhofer.de")
                        .termsOfServiceUrl("https://www.scaiview.com/en/terms-and-conditions.html")
                        .contact(new Contact("Dr. Marc Jacobs", "", "marc.jacobs@scai.fraunhofer.de"))
                        .build());
    }


}
