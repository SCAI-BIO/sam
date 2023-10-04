package de.fraunhofer.scai.bio.sam.service.impl.ols.json;

import com.google.gson.*;

import de.fraunhofer.scai.bio.sam.domain.DAO.Terminology;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class JSONDeserializerTerminolgies implements JsonDeserializer<Page<Terminology>> {
    Logger log = LoggerFactory.getLogger(getClass());
    
    @Override
    public Page<Terminology> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        List<Terminology> retuner = new ArrayList<Terminology>();
        try {
            final Gson gson = new GsonBuilder().registerTypeAdapter(Terminology.class, new JSONDeserializerTerminology()).create();
            final JsonObject obj = jsonElement.getAsJsonObject();
            final JsonArray list = obj.getAsJsonObject("_embedded").getAsJsonArray("ontologies");
            for (JsonElement obs : list) {
                retuner.add(gson.fromJson(obs.getAsJsonObject(), Terminology.class));
            }
    
            int numberOfElements = obj.getAsJsonObject("page").get("totalElements").getAsInt();
            int numberOfThisPage = obj.getAsJsonObject("page").get("number").getAsInt();
            int numberOfElementsInThisPage = obj.getAsJsonObject("page").get("size").getAsInt();
            return new PageImpl<Terminology>(retuner, PageRequest.of(numberOfThisPage, numberOfElementsInThisPage),numberOfElements);
        }catch (NullPointerException e){
            log.error("Unable to parse OLS response. Cause: {} Respone Content: {}",e.getMessage(),jsonElement);
        }
        return new PageImpl<Terminology>(retuner, PageRequest.of(1, 10),0);
    }
}
