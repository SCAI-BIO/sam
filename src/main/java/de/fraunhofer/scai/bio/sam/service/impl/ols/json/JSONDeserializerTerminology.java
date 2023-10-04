package de.fraunhofer.scai.bio.sam.service.impl.ols.json;

import com.google.gson.*;

import de.fraunhofer.scai.bio.sam.domain.DAO.Terminology;

import java.lang.reflect.Type;

public class JSONDeserializerTerminology implements JsonDeserializer<Terminology> {

    @Override
    public Terminology deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final Terminology term = new Terminology();
       
        final JsonObject obj = jsonElement.getAsJsonObject();
        term.setOlsID(obj.get("ontologyId").getAsString());
        final JsonObject conf= obj.getAsJsonObject("config");
        term.setShortName(conf.get("namespace").getAsString());
    
        final JsonElement desc = conf.get("description");
        if(!desc.isJsonNull()){
            term.setDescription(desc.getAsString());
        }else{term.setDescription("");}

        final JsonElement title = conf.get("title");
        if (!title.isJsonNull()) {
            term.setLongName(conf.get("title").getAsString());
        } else {
            term.setLongName("");
        }

        JsonArray uris = conf.getAsJsonArray("baseUris");
        if (uris != null && uris.size() > 0) {
            term.setIri(uris.get(0).getAsString());
        } else {
            term.setIri("");
        }
        return term;
    }
}
