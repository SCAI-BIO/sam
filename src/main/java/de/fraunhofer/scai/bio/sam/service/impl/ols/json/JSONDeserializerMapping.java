package de.fraunhofer.scai.bio.sam.service.impl.ols.json;

import com.google.gson.*;

import de.fraunhofer.scai.bio.sam.domain.DAO.Concept;
import de.fraunhofer.scai.bio.sam.domain.DAO.Mapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

/**
 * JSONDeserializerMapping
 * <p>
 * TODO: Add javadoc
 *
 * @author Johannes Darms <johannes.darms@scai.fraunhofer.de>
 **/

public class JSONDeserializerMapping implements JsonDeserializer<Mapping> {
    Logger log = LoggerFactory.getLogger(getClass());
    
    
    @Override
    public Mapping deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();
    
        String fromCurie=obj.getAsJsonObject("fromTerm").getAsJsonPrimitive("curie").getAsString();
        String fromURI=obj.getAsJsonObject("fromTerm").getAsJsonPrimitive("uri").getAsString();
        String toCurie=obj.getAsJsonObject("toTerm").getAsJsonPrimitive("curie").getAsString();
        String toURI=obj.getAsJsonObject("toTerm").getAsJsonPrimitive("uri").getAsString();
        String scope=obj.getAsJsonPrimitive("scope").getAsString();
        Mapping.MAPPING_TYPE typeOfMapping;
        switch (scope){
            case "RELATED":
                typeOfMapping= Mapping.MAPPING_TYPE.RELATED;
                break;
            case "EXACT":
                typeOfMapping= Mapping.MAPPING_TYPE.EXACT;
                break;
            default:
                typeOfMapping= Mapping.MAPPING_TYPE.RELATED;
                log.info("{}",scope);
        }
        Concept a = new Concept();
        a.setIri(fromURI);
        a.setLocalID(fromCurie.substring(fromCurie.indexOf(":")+1));
        a.setDefiningTerminology(fromCurie.substring(0,fromCurie.indexOf(":")));
    
        Concept b = new Concept();
        b.setIri(toURI);
        b.setLocalID(toCurie.substring(toCurie.indexOf(":")+1));
        b.setDefiningTerminology(toCurie.substring(0,toCurie.indexOf(":")));
        return new Mapping(typeOfMapping,a,b);
    }
}
