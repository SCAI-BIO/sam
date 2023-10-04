package de.fraunhofer.scai.bio.sam.service.impl.ols;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.collect.Multimap;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import de.fraunhofer.scai.bio.sam.domain.DAO.Concept;
import de.fraunhofer.scai.bio.sam.domain.DAO.Mapping;
import de.fraunhofer.scai.bio.sam.domain.DAO.Terminology;
import de.fraunhofer.scai.bio.sam.service.ConceptSearchService;
import de.fraunhofer.scai.bio.sam.service.ConceptService;
import de.fraunhofer.scai.bio.sam.service.ConceptSuggestService;
import de.fraunhofer.scai.bio.sam.service.DetectOriginatingTerminology;
import de.fraunhofer.scai.bio.sam.service.exceptions.NotFoundException;
import de.fraunhofer.scai.bio.sam.service.impl.ols.json.JSONDeserializerConcept;
import de.fraunhofer.scai.bio.sam.service.impl.ols.json.JSONDeserializerConcepts;
import de.fraunhofer.scai.bio.sam.service.impl.ols.json.JSONDeserializerTerminolgies;
import de.fraunhofer.scai.bio.sam.service.impl.ols.json.JSONDeserializerTerminology;
import de.fraunhofer.scai.bio.sam.service.impl.ols.json.JSONMAPPINGOXO;
import de.fraunhofer.scai.bio.sam.service.impl.ols.json.JSONSOLRConceptSearch;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

@Order(3)
@Service("OLSConceptService")
@Profile("OLS")
@Scope(value = SCOPE_SINGLETON)
public class OLSConceptServiceImpl implements ConceptService, ConceptSearchService, ConceptSuggestService {

    Logger logger = LoggerFactory.getLogger(getClass());
    int maxPageSize = 500;

    @Autowired
    DetectOriginatingTerminology detectOriginatingTerminology;
    private String url;

    private interface OXO {
        @Headers({"Accept: application/json"})
        @GET("spot/oxo/api/mappings")
        Call<Page<Mapping>> listConceptsOfTerminology(@Query("fromId") String id, @Query("size") int pageSize,
                                                      @Query("page") int pageNumber);
    }

    private interface OLS {

        @Headers({"Accept: application/json"})
        @GET("ontologies/{id}/terms/roots")
        Call<Page<Concept>> listTopConceptsOfTerminology(@Path("id") String terminology, @Query("size") int pageSize,
                                                         @Query("page") int pageNumber);

        @Headers({"Accept: application/json"})
        @GET("ontologies/{id}/terms")
        Call<Page<Concept>> listConceptsOfTerminology(@Path("id") String terminology, @Query("size") int pageSize,
                                                      @Query("page") int pageNumber);

        @Headers({"Accept: application/json"})
        @GET("ontologies/{id}/terms/{iri}")
        Call<Concept> getConceptOfTerminology(@Path("id") String id, @Path("iri") String iri);

        @Headers({"Accept: application/json"})
        @GET("ontologies/{id}/individuals/{iri}")
        Call<Concept> getIndividualOfTerminology(@Path("id") String id, @Path("iri") String iri);

        @Headers({"Accept: application/json"})
        @GET("ontologies/{id}/terms/{iri}/children")
        Call<Page<Concept>> getChildrenOfConcept(@Path("id") String id, @Path("iri") String iri, @Query("size") int pageSize,
                                                 @Query("page") int pageNumber);

        @Headers({"Accept: application/json"})
        @GET("ontologies/{id}/terms/{iri}/hierarchicalDescendants")
        Call<Page<Concept>> getTransChildrenOfConcept(@Path("id") String id, @Path("iri") String iri, @Query("size") int pageSize,
                                                      @Query("page") int pageNumber);

        @Headers({"Accept: application/json"})
        @GET("ontologies/{id}/terms/{iri}/parents")
        Call<Page<Concept>> getParentsOfConcept(@Path("id") String id, @Path("iri") String iri, @Query("size") int pageSize,
                                                @Query("page") int pageNumber);

        // returns the direct solr json response....
        // fieldList what results we want for Concepts if present.
        @Headers({"Accept: application/json"})
        @GET("search?fieldList=iri,description,label,synonym,ontology_name")
        //		@GET("search?fieldList=label,synonym")
        Call<Page<Concept>> getConcept(
                @Query("exact") boolean exact,
                //what values to consider
                @Query("queryFields") String query,
                // they term to search for
                @Query("q") String iri,
                // used for paging
                @Query("rows") int pageSize,
                @Query("start") long offset);

        // returns the direct solr json response....
        // fieldList what results we want for Concepts if present.
        @Headers({"Accept: application/json"})
        @GET("search?fieldList=iri,description,label,synonym,ontology_name")
        //		@GET("search?fieldList=label,synonym")
        Call<Page<Concept>> getConceptInOntology(
                @Query("exact") boolean exact,
                //what values to consider
                @Query("queryFields") String query,
                // they term to search for
                @Query("q") String iri,
                @Query("ontology") String ontology,
                // used for paging
                @Query("rows") int pageSize,
                @Query("start") long offset);

        // for autocompletion
        // returns the direct solr json response....
        @GET("select?fieldList=iri,description,label,synonym,ontology_name")
        //		@GET("select?fieldList=label,synonym")
        Call<Page<Concept>> selectConcept(@Query("q") String iri, @Query("rows") int pageSize, @Query("start") long offset);

        @GET("select?fieldList=iri,description,label,synonym,ontology_name")
            //		@GET("select?fieldList=label,synonym")
        Call<Page<Concept>> selectConceptInOntology(@Query("ontology") String ontology, @Query("q") String iri, @Query("rows") int pageSize, @Query
                ("start") long offset);

    }

    @Override
    public String toString() {
        return "OLS @ " + url;
    }

    public OLSConceptServiceImpl(String url, int maxPageSize) {
        this.maxPageSize = maxPageSize;
        construct(url);
    }

    private OLS SOLRservice;
    private OLS OLSService = null;
    private OXO oxo;

    //@PostConstruct
    public void construct(String url) {

        if (url == null || url.isEmpty()) {
            logger.info(" >> switched off OLS");
            return;
        }

        this.url = url;

        Type collectionType1 = new TypeToken<Page<Terminology>>() {
        }.getType();

        Type collectionType2 = new TypeToken<Page<Concept>>() {
        }.getType();

        Retrofit.Builder halServiceBuilder = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .registerTypeAdapter(Terminology.class, new JSONDeserializerTerminology())
                        .registerTypeAdapter(collectionType1, new JSONDeserializerTerminolgies())
                        .registerTypeAdapter(Concept.class, new JSONDeserializerConcept())
                        .registerTypeAdapter(collectionType2, new JSONDeserializerConcepts())
                        .create()));
        OLSService = halServiceBuilder.build().create(OLS.class);

        logger.info(" >> Concept OLS is running on {}", url);

        // we need another Deserializer for the SOLR Responses as thy do not follow the HAL schema.
        Retrofit.Builder solrServiceBuilder = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .registerTypeAdapter(collectionType2, new JSONSOLRConceptSearch())
                        .create()));
        SOLRservice = solrServiceBuilder.build().create(OLS.class);

        Type collectionType3 = new TypeToken<Page<Mapping>>() {
        }.getType();
        Retrofit.Builder oxoBuilder = new Retrofit.Builder()
                .baseUrl("https://www.ebi.ac.uk/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .registerTypeAdapter(collectionType3, new JSONMAPPINGOXO())
                        .create()));
        oxo = oxoBuilder.build().create(OXO.class);
    }


    @Override
    public void saveConceptHierarchyRelations(Multimap<String, String> relationFromTo) {

    }

    @Override
    public void saveConcepts(String terminologyId, List<Concept> concepts) {

    }

    /**
     * In OLS Concepts are defined within a Terminology. Sadly some Concepts are used in multiple Terminologies.
     * Depending on what Terminology is requested the Concepts even though they have the same OBO can differ. This need
     * to be mitigated.
     *
     * @param terminology
     * @param iri
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public Concept getConceptOfTerminology(String terminology, String iri) throws NotFoundException {
        try {
            if (OLSService != null) {
                Concept concept = OLSService.getConceptOfTerminology(terminology, URLEncoder.encode(iri, "UTF-8"))
                        .execute().body();
                if (concept == null) {
                    throw new NotFoundException();
                }
                return concept;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Concept getIndividualOfTerminology(String terminology, String iri) throws NotFoundException {
        try {
            if (OLSService != null) {
                Call<Concept> request = OLSService.getIndividualOfTerminology(terminology, URLEncoder.encode(iri, "UTF-8"));
                Concept concept = request.execute().body();
                if (concept == null) {
                    throw new NotFoundException();
                }
                return concept;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Page<Concept> getDirectParents(Concept concept, Pageable of) {
        try {
            if (OLSService != null) {
                if (concept.getDefiningTerminology() != null) {
                    Page<Concept> a = OLSService.getParentsOfConcept(
                            concept.getDefiningTerminology(),
                            URLEncoder.encode(concept.getIri(), "UTF-8"),
                            of.getPageSize(), of.getPageNumber()
                    ).execute().body();
                    return a;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isParent(Concept concept) throws NotFoundException {
        try {
            if (OLSService != null) {
                if (concept.getDefiningTerminology() != null) {
                    Page<Concept> a = OLSService.getChildrenOfConcept(
                            concept.getDefiningTerminology(),
                            URLEncoder.encode(concept.getIri(), "UTF-8"),
                            1, 0
                    ).execute().body();
                    return a.getNumberOfElements() > 0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public Page<Concept> getDirectChildren(Concept concept, Pageable of) {
        try {
            if (OLSService != null) {
                if (concept.getDefiningTerminology() != null) {
                    Page<Concept> a = OLSService.getChildrenOfConcept(
                            concept.getDefiningTerminology(),
                            URLEncoder.encode(concept.getIri(), "UTF-8"),
                            of.getPageSize(), of.getPageNumber()

                    ).execute().body();
                    return a;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Page<Concept> getTransitiveChildren(Concept concept, Pageable of) {
        try {
            if (OLSService != null) {
                if (concept.getDefiningTerminology() != null) {
                    Page<Concept> a = OLSService.getTransChildrenOfConcept(
                            concept.getDefiningTerminology(),
                            URLEncoder.encode(concept.getIri(), "UTF-8"),
                            of.getPageSize(), of.getPageNumber()

                    ).execute().body();
                    return a;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    //
    // case sensitive
    @Override
    public Page<Concept> getRelatedConcepts(Concept concept, Pageable request) {
        //        } // oxo does not supply correct pagination results. Thus we request all and return only a subset...
        Page<Mapping> a = null;
        try {
            a = oxo.listConceptsOfTerminology(
                    concept.getDefiningTerminology().toUpperCase() + ":" + concept.getLocalID(), 10000,
                    0).execute().body();
            HashSet<Concept> concepts = new HashSet<>();
            a.getContent().stream().forEach(s -> {
                concepts.add(s.getA());
                concepts.add(s.getB());
            });
            concepts.remove(concept);
            List<Concept> returner = new ArrayList<>();
            concepts.stream().forEach(concept1 -> {
                try {
                    returner.add(getConcept(concept1.getIri()));
                } catch (NotFoundException e) {
                    // this is not defined in our system!
                    // Thus we can only work with the IRI or drop this..
                    logger.info("Concept={}:{} not found in OLS!", concept1.getDefiningTerminology().toUpperCase(),
                            concept1.getLocalID
                                    ().toUpperCase());
                }
            });
            return new PageImpl<Concept>(returner);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        // hack that OXO s***
        return new PageImpl<Concept>(new ArrayList<>());

    }


    @Override
    public Page<Mapping> getMappings(Concept concept, Mapping.MAPPING_TYPE type, Pageable request) {
        try {
            // oxo does not supply correct pagination results. Thus we request all and return only a subset...
            Page<Mapping> a = oxo.listConceptsOfTerminology(
                    concept.getDefiningTerminology().toUpperCase() + ":" + concept.getLocalID(), 10000,
                    0).execute().body();
            // hack that OXO s***
            HashSet<Mapping> mappings = new HashSet<>();
            a.getContent().stream().forEach(s -> {
                try {
                    Concept A = getConcept(s.getA().getIri());
                    try {
                        Concept B = getConcept(s.getB().getIri());
                        mappings.add(new Mapping(s.getType(), A, B));
                    } catch (NotFoundException e) {
                        logger.info("Concept={}:{} not found in OLS!", s.getB().getDefiningTerminology().toUpperCase(),
                                s.getB().getLocalID().toUpperCase());
                    }

                } catch (NotFoundException e) {
                    logger.info("Concept={}:{} not found in OLS!", s.getA().getDefiningTerminology().toUpperCase(),
                            s.getA().getLocalID().toUpperCase());
                }


            });
            // we need to filter duplicates....


            return new PageImpl<Mapping>(mappings.stream().collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new PageImpl<Mapping>(new ArrayList<>());
    }


    @Override
    public Page<Concept> getTopConceptOfTerminology(String terminology, Pageable request) throws NotFoundException {
        logger.debug(" >>> get top of {} from {}", terminology, OLSService.toString());

        try {
            if (OLSService != null) {
                return OLSService.listTopConceptsOfTerminology(terminology, request.getPageSize(), request.getPageNumber()).execute().body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Page<Concept> searchConceptInTerminology(String ontology, EnumSet<SEARCH_FIELDS> searchFields, boolean exact,
                                                    String query, Pageable
                                                            request) {
        try {
            String fields = "";
            //label, synonym, description, short_form, obo_id, annotations, logical_description, iri
            for (SEARCH_FIELDS field : searchFields) {
                switch (field) {
                    case iri:
                        fields += "iri,";
                        break;
                    case altLabels:
                        fields += "synonym,";
                        break;
                    case prefLabel:
                        fields += "label,";
                        break;
                    case description:
                        fields += "description,";
                        break;
                }
            }

            // search expect single quoted input, but all others request double quoted requests...
            Page<Concept> a = SOLRservice.getConceptInOntology(exact, fields, query, ontology.toLowerCase(), request.getPageSize(),
                            request
                                    .getOffset()).execute()
                    .body();


            return a;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    public Page<Concept> searchConcept(EnumSet<SEARCH_FIELDS> searchFields, boolean exact, String query, Pageable request) {
        try {
            String fields = "";
            //label, synonym, description, short_form, obo_id, annotations, logical_description, iri
            for (SEARCH_FIELDS field : searchFields) {
                switch (field) {
                    case iri:
                        fields += "iri,";
                        break;
                    case altLabels:
                        fields += "synonym,";
                        break;
                    case prefLabel:
                        fields += "label,";
                        break;
                    case description:
                        fields += "description,";
                        break;
                }
            }

            // search expect single quoted input, but all others request double quoted requests...
            Page<Concept> a = SOLRservice.getConcept(exact, fields, query, request.getPageSize(), request.getOffset()).execute()
                    .body();


            return a;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public Page<Concept> searchConceptExactByIRI(String id, Pageable request) {
        try {
            // search expect single quoted input, but all others request double quoted requests...
            Page<Concept> a = SOLRservice.getConcept(true, "iri", id, request.getPageSize(), request.getOffset())
                    .execute()
                    .body();

            return a;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public Page<Concept> suggestConcepts(@NotNull @Valid String q, Pageable request) {
        try {
            // search expect single quoted input, but all others request double quoted requests...
            Page<Concept> a = SOLRservice.selectConcept(q,
                            request.getPageSize(),
                            request.getOffset())
                    .execute()
                    .body();

            return a;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    @Override
    public Page<Concept> suggestConceptInTerminology(String terminologyId, @NotNull @Valid String q, Pageable request) {
        try {
            // search expect single quoted input, but all others request double quoted requests...
            Page<Concept> a = SOLRservice.selectConceptInOntology(terminologyId.toLowerCase(), q,
                            request.getPageSize(),
                            request.getOffset())
                    .execute()
                    .body();

            return a;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Concept getConcept(String iri) throws NotFoundException {
        try {
            // search expect single quoted input, but all others request double quoted requests...
            Page<Concept> a = SOLRservice.getConcept(true, "iri", iri, maxPageSize, 0).execute().body();
            if (a.getTotalElements() == 1)
                return a.getContent().get(0);
            else if (a.getTotalElements() == 0) {
                throw new NotFoundException();
            } else {
                logger.warn(" >>> OBO NOT UNIQUE!");
                return a.getContent().get(0);
                //argh OBO is not unique
                // this means it is used by copy/import in multiple Terminologies.
                // we need to detect the Terminology from which this Concept originated.
                // @TODO
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Page<Concept> getConceptsOfTerminology(String terminology, Pageable request) {
        try {
            if (OLSService != null) {
                return OLSService.listConceptsOfTerminology(terminology, request.getPageSize(), request.getPageNumber()).execute().body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //	private Concept setDefiningTerminology(Concept concept) {
    //		if(concept==null){
    //			return null;
    //		}
    //		Map.Entry<String, String> definingPrefix = detectOriginatingTerminology.getDefiningTerminologyPrefix(concept.getIri().toLowerCase());
    //
    //		if (definingPrefix != null) {
    //			String iri = concept.getIri();
    //			String lowerCaseId = concept.getIri().toLowerCase().replaceFirst(definingPrefix.getValue(), "");
    //			String id = iri.substring(iri.length()-lowerCaseId.length());
    //			concept.setLocalID(id);
    //			concept.setDefiningTerminology(definingPrefix.getKey());
    //
    //			// rescue cross links from OBO eg http://purl.obolibrary.org/obo/NCIT_C49678
    //		} else if(concept.getIri().startsWith("http://purl.obolibrary.org/obo/")) {
    //			String iri = concept.getIri().replaceFirst("http://purl.obolibrary.org/obo/", "");
    //			if(iri.split("_").length==2) {
    //				concept.setLocalID(iri.split("_")[1]);
    //				concept.setDefiningTerminology(iri.split("_")[0]);
    //			} else {
    //				logger.warn("OBO IS internal, but weird: {}",concept.getIri());        		
    //			}
    //		}else{
    //			logger.warn("OBO IS external: {}",concept.getIri());
    //
    //		}
    //		return concept;
    //	}

    //	private Page<Concept> setDefiningTerminology(Page<Concept> concepts) {
    //
    //		for (Concept c : concepts.getContent()) {
    //			c = setDefiningTerminology(c);
    //		}
    //		return concepts;
    //	}


}
