package de.fraunhofer.scai.bio.sam.service.impl.loinc;

import java.lang.reflect.Type;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import de.fraunhofer.scai.bio.sam.domain.DAO.Concept;

public class JSONDeserializerConcept implements JsonDeserializer<Concept> {
    @Override
    public Concept deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Concept concept = new Concept();
        concept.setDefiningTerminology("LOINC");
        concept.setParent(false);
        
        JsonObject obj = jsonElement.getAsJsonObject();
        
        concept.setLocalID("loinc:"+jsonElementToString(obj.get("conceptID")));
        concept.setDescription(jsonElementToString(obj.get("description")));                        
        concept.setPrefLabel(jsonElementToString(obj.get("prefLabel")));
        concept.addLabel(jsonElementToString(obj.get("altLabel")));
        
        return concept;
    }
    
    private String jsonElementToString(JsonElement element) {
    	StringBuilder sb = new StringBuilder();
    	
        if(!element.isJsonNull()) { 
            if(element instanceof JsonArray){
            	for(JsonElement arrayElement : element.getAsJsonArray()) {
            		sb.append(jsonElementToString(arrayElement));
            		sb.append(", ");
            	}
            	sb.delete(sb.length()-2, sb.length()-1);
            }else if(element instanceof JsonObject) {
            	for (Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) { 
            		sb.append(entry.getKey());
            		sb.append(": ");
            		sb.append(jsonElementToString(entry.getValue()));
            		sb.append("\n");
            	}
            } else {
            	sb.append(element.getAsString());
            }
        }

    	return sb.toString();
    }
}

