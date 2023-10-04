package de.fraunhofer.scai.bio.sam.service.impl.loinc;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.EnumSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.common.collect.Multimap;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import de.fraunhofer.scai.bio.sam.config.LoincProperties;
import de.fraunhofer.scai.bio.sam.domain.DAO.Concept;
import de.fraunhofer.scai.bio.sam.domain.DAO.Mapping;
import de.fraunhofer.scai.bio.sam.domain.DAO.Mapping.MAPPING_TYPE;
import de.fraunhofer.scai.bio.sam.domain.DAO.Terminology;
import de.fraunhofer.scai.bio.sam.service.ConceptSearchService;
import de.fraunhofer.scai.bio.sam.service.ConceptService;
import de.fraunhofer.scai.bio.sam.service.ConceptSuggestService;
import de.fraunhofer.scai.bio.sam.service.exceptions.NotFoundException;
import de.fraunhofer.scai.bio.sam.service.impl.ols.json.JSONDeserializerTerminolgies;
import de.fraunhofer.scai.bio.sam.service.impl.ols.json.JSONDeserializerTerminology;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
@Order(2)
@Service("LoincTerminologyService")
@Profile("LOINC")
public class LoincTerminologyService implements ConceptService, ConceptSearchService,ConceptSuggestService {

	Logger logger = LoggerFactory.getLogger(getClass());

	LoincProperties loincProperties;
	private Loinc loincService;


	private interface Loinc {
		
		@GET("terminologies/concepts/autocomplete")
		Call<Page<Concept>> suggestConcepts(@NotNull @Valid @Query("q") String q, @Query("page") long page, @Query("size") long size);


		@GET("ontologies/{id}/terms")
		Call<Page<Concept>> listConceptsOfTerminology(@Path("id") String terminology, @Query("size") int pageSize,
				@Query("page") int pageNumber);

		@GET("ontologies/{id}/terms/{iri}")
		Call<Concept> getConceptOfTerminology(@Path("id") String id, @Path("iri") String iri);

		// returns the direct solr json response....
		// fieldList what results we want for Concepts if present.
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
		Call<Page<Concept>> selectConcept( @Query("q") String iri, @Query("rows") int pageSize, @Query("start") long offset);
		@GET("select?fieldList=iri,description,label,synonym,ontology_name")
//		@GET("select?fieldList=label,synonym")
		Call<Page<Concept>> selectConceptInOntology(@Query("ontology") String ontology, @Query("q") String iri, @Query("rows") int pageSize, @Query
				("start") long offset);

	}

	public LoincTerminologyService(LoincProperties loincProperties2) {
	    this.loincProperties = loincProperties2;
	    construct();
    }

    @PostConstruct
	public void construct() {

	    if(loincProperties.getUrl() == null || loincProperties.getUrl().isEmpty()) {
	        logger.info(" >> switched off LOINC");
	        return;
	    }

		Type collectionType1 = new TypeToken<Page<Terminology>>(){}.getType();
		Type collectionType2 = new TypeToken<Page<Concept>>(){}.getType();

		Retrofit.Builder halServiceBuilder =  new Retrofit.Builder()
				.baseUrl(loincProperties.getUrl())
				.addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
						.registerTypeAdapter(Terminology.class,new JSONDeserializerTerminology())
						.registerTypeAdapter(collectionType1,new JSONDeserializerTerminolgies())
						.registerTypeAdapter(Concept.class,new JSONDeserializerConcept())
						.registerTypeAdapter(collectionType2,new JSONDeserializerConcepts())
						.create()));
		loincService = halServiceBuilder.build().create(Loinc.class);
		
		logger.info(" >> Main LOINC is running on {}", loincProperties.getUrl());
	}

	@Override
	public Page<Concept> searchConceptInTerminology(String ontology, EnumSet<SEARCH_FIELDS> searchFields, boolean exact,
			String query, Pageable
			request) {
		try {
			String fields="";
			//label, synonym, description, short_form, obo_id, annotations, logical_description, iri
			for (SEARCH_FIELDS field : searchFields){
				switch (field){
				case iri:
					fields+="iri,";
					break;
				case altLabels:
					fields+="synonym,";
					break;
				case prefLabel:
					fields+="label,";
					break;
				case description:
					fields+="description,";
					break;
				}
			}

			// search expect single quoted input, but all others request double quoted requests...
			Page<Concept> a = loincService.getConceptInOntology(exact,fields, query ,ontology.toLowerCase(),request.getPageSize(),
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
			String fields="";
			//label, synonym, description, short_form, obo_id, annotations, logical_description, iri
			for (SEARCH_FIELDS field : searchFields){
				switch (field){
				case iri:
					fields+="iri,";
					break;
				case altLabels:
					fields+="synonym,";
					break;
				case prefLabel:
					fields+="label,";
					break;
				case description:
					fields+="description,";
					break;
				}
			}

			// search expect single quoted input, but all others request double quoted requests...
			Page<Concept> a = loincService.getConcept(exact,fields, query ,request.getPageSize(),request.getOffset()).execute()
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
			Page<Concept> a = loincService.suggestConcepts(q, 
					request.getOffset(),
					request.getPageSize()
					)
					.execute()
					.body();

			return a;
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			logger.info(e.getLocalizedMessage(), e);
		}

		return null;

	}

	@Override
	public Page<Concept> suggestConceptInTerminology(String terminologyId, @NotNull @Valid String q, Pageable request) {
		try {
			// search expect single quoted input, but all others request double quoted requests...
			Page<Concept> a = loincService.selectConceptInOntology(terminologyId.toLowerCase(),  q, 
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

	@Override
	public void saveConceptHierarchyRelations(Multimap<String, String> relationFromTo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveConcepts(String terminologyId, List<Concept> concepts) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T extends Concept> T getConceptOfTerminology(String terminology, String iri) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Concept> Page<T> getDirectParents(Concept concept, Pageable request) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Concept> Page<T> getDirectChildren(Concept concept, Pageable request) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Concept> Page<T> getTransitiveChildren(Concept concept, Pageable request)
			throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Concept> Page<T> getRelatedConcepts(Concept concept, Pageable request) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isParent(Concept concept) throws NotFoundException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Page<Mapping> getMappings(Concept concept, MAPPING_TYPE type, Pageable request) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Concept> getTopConceptOfTerminology(String terminology, Pageable request) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Concept> Page<T> getConceptsOfTerminology(String terminology, Pageable request)
			throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Concept> Page<T> searchConceptExactByIRI(String id, Pageable request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T extends Concept> T getConcept(String iri) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
