package de.fraunhofer.scai.bio.sam.service.impl.ols;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.OffsetDateTime;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import de.fraunhofer.scai.bio.sam.domain.DAO.Terminology;
import de.fraunhofer.scai.bio.sam.domain.DAO.VersionTag;
import de.fraunhofer.scai.bio.sam.service.TerminologyService;
import de.fraunhofer.scai.bio.sam.service.impl.ols.json.JSONDeserializerTerminolgies;
import de.fraunhofer.scai.bio.sam.service.impl.ols.json.JSONDeserializerTerminology;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
@Order(3)
@Service("OLSTerminologyService")
@Profile("OLS")
@Scope(value = SCOPE_SINGLETON)
public class OLSTerminologyServiceImpl implements TerminologyService {

    Logger logger = LoggerFactory.getLogger(getClass());

    private OLS HALservice;

    private String url;

    private interface OLS {

        @Headers({"Accept: application/json"})
        @GET("ontologies")
        Call<Page<Terminology>> listTerminologies( @Query("size") int pageSize, @Query("page") int pageNumber);

        @Headers({"Accept: application/json"})
        @GET("ontologies/{id}")
        Call<Terminology> getTerminology(@Path("id") String id);

    }

    public OLSTerminologyServiceImpl(String url) {
        construct(url);
    }

    // @PostConstruct
    public void construct(String url) {

        if(url == null || url.isEmpty()) return;
        else this.url = url;

        Type collectionType1 = new TypeToken<Page<Terminology>>(){}.getType();

        Retrofit.Builder halServiceBuilder =  new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .registerTypeAdapter(Terminology.class,new JSONDeserializerTerminology())
                        .registerTypeAdapter(collectionType1,new JSONDeserializerTerminolgies())
                        .create()));
        HALservice = halServiceBuilder.build().create(OLS.class);

        logger.info(" >> Main terminology OLS is running on {}", url);
        logger.info(" >>> {} terminologies registered", getCountOfTerminologies()); 
    }

    @Override
    public String toString() {
        return "OLS @ "+ url;
    }
    
    @Override
    public void save(Terminology t) {

    }

    @Override
    public Terminology getTerminology(String terminologyId) {
        if(terminologyId==null || terminologyId.trim().isEmpty() ) {
            throw new IllegalArgumentException("passed terminologyId is either null, empty!");
        }
        try {
            if(HALservice != null) {
                return HALservice.getTerminology(terminologyId).execute().body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Page<Terminology> getAllTerminologies( Pageable request) {
        try {
            if(HALservice != null) {
                logger.debug(" >>> querying all terminologies from {}", HALservice.toString());
                Page<Terminology> p =
                        HALservice.listTerminologies(request.getPageSize(),request.getPageNumber()).execute().body();
                return p != null && p.hasContent() ? p : Page.empty();
            }
        } catch (IOException e) {
            // TODO: ebi ols fails for baseURL/ontologies -> investigate why
            logger.error(" >>> querying all terminologies from {} failed", this.url);
            return Page.empty();
        }
        return null;
    }

    @Override
    public long getCountOfTerminologies() {
        if(HALservice != null) {
            return this.getAllTerminologies(PageRequest.of(0,100)).getTotalElements();
        }

        return 0l;
    }

    @Override
    public boolean existsByID(String terminologyId) {
        if(terminologyId==null || terminologyId.trim().isEmpty() ){
            throw new IllegalArgumentException("passed terminologyId is either null, empty!");
        }
        Terminology a = getTerminology(terminologyId);
        if(a!=null){
            return true;
        }
        return false;
    }

    @Override
    public VersionTag getLastVersionTag(String terminologyID) {
        return new VersionTag("LATEST", OffsetDateTime.MIN);
    }

}
