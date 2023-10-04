package de.fraunhofer.scai.bio.sam.service.impl.loinc;

import com.google.gson.*;

import de.fraunhofer.scai.bio.sam.domain.DAO.Concept;

import org.springframework.data.domain.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JSONDeserializerConcepts implements JsonDeserializer<Page<Concept>> {
    @Override
    public Page<Concept> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        List<Concept> concepts= new ArrayList<>();
        
        final Gson gson = new GsonBuilder().registerTypeAdapter(Concept.class, new JSONDeserializerConcept()).create();
        final JsonObject obj = jsonElement.getAsJsonObject();
    
        int numberOfElements = obj.get("totalElements").getAsInt();
        int numberOfThisPage= obj.get("number").getAsInt();
        int numberOfElementsInThisPage= obj.get("numberOfElements").getAsInt();
    
        JsonArray obs = obj.getAsJsonArray("content");
        if(obs!=null){
            for ( JsonElement obs1 :obs) {
                concepts.add(gson.fromJson(obs1.getAsJsonObject(),Concept.class));
            }
        }
    
        Page<Concept> retuner = new PageImpl<Concept>(concepts,PageRequest.of(numberOfThisPage,numberOfElementsInThisPage),numberOfElements);
        return retuner;
       
    }
}
