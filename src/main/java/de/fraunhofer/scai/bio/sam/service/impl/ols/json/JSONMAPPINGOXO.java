package de.fraunhofer.scai.bio.sam.service.impl.ols.json;

import com.google.gson.*;

import de.fraunhofer.scai.bio.sam.domain.DAO.Mapping;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * JSONMAPPINGOXO
 * <p>
 * TODO: Add javadoc
 *
 * @author Johannes Darms <johannes.darms@scai.fraunhofer.de>
 **/
public class JSONMAPPINGOXO implements JsonDeserializer<Page<Mapping>> {
    
    @Override
    public Page<Mapping> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        List<Mapping> mappings =new ArrayList<Mapping>();
        final Gson gson = new GsonBuilder().registerTypeAdapter(Mapping.class, new JSONDeserializerMapping()).create();
    
        final JsonObject obj = jsonElement.getAsJsonObject();
        int totalNumberOfElements = obj.getAsJsonObject("page").get("totalElements").getAsInt();
        int numberOfThisPage= obj.getAsJsonObject("page").get("number").getAsInt();
        int numberOfElementsInThisPage= obj.getAsJsonObject("page").get("size").getAsInt();
       
        JsonObject em = obj.getAsJsonObject("_embedded");
        if(em!=null) {
            JsonArray list = em.getAsJsonArray("mappings");
            for (JsonElement obs : list) {
                mappings.add(gson.fromJson(obs.getAsJsonObject(), Mapping.class));
            }
            if (numberOfElementsInThisPage == 0) {
                numberOfElementsInThisPage = mappings.size();
            }
        }
        if(numberOfElementsInThisPage==0){
            numberOfElementsInThisPage=10;
        }
        Page<Mapping> retuner = new PageImpl<Mapping>(mappings,PageRequest.of(numberOfThisPage,numberOfElementsInThisPage),totalNumberOfElements);
        return retuner;
    }
}
