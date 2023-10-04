package de.fraunhofer.scai.bio.sam.service.impl.ols.json;

import com.google.gson.*;

import de.fraunhofer.scai.bio.sam.domain.DAO.Concept;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class JSONDeserializerConcept implements JsonDeserializer<Concept> {
    @Override
    public Concept deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Concept concept = new Concept();
        JsonObject obj = jsonElement.getAsJsonObject();
        concept.setIri(obj.get("iri").getAsString());
        // description may be empty
        String descr = "";
        if (obj.get("description").getAsJsonArray().size() > 0) {
            descr = jsonElementToString(obj.get("description"));
        }
        String annotation = jsonElementToString(obj.get("annotation"));
        if(descr.isEmpty()) {
        	concept.setDescription(annotation);
        } else if(annotation.isEmpty()) {
        	concept.setDescription(descr);        	
        } else {
        	concept.setDescription(descr + "\n" + annotation);
        }
        
        concept.setPrefLabel(obj.get("label").getAsString());
        Gson gson = new Gson();
        String[] synonyms = gson.fromJson(obj.get("synonyms"), String[].class);
        if(synonyms!=null) {
            concept.setAltLabels(Arrays.asList(synonyms));
        }
        
        concept.setParent(Boolean.parseBoolean(obj.get("has_children").getAsString()));
        
        @SuppressWarnings("unchecked")
		Map<String, List<String>> annotations = gson.fromJson(obj.get("annotation"), Map.class);
        concept.setAnnotations(annotations);
        
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

