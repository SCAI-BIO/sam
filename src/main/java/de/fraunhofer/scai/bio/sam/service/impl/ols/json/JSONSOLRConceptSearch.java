package de.fraunhofer.scai.bio.sam.service.impl.ols.json;

import com.google.gson.*;

import de.fraunhofer.scai.bio.sam.domain.DAO.Concept;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JSONSOLRConceptSearch implements JsonDeserializer<Page<Concept>> {
    @Override
    public Page<Concept> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        List<Concept> concepts= new ArrayList<>();
        JsonObject obj = jsonElement.getAsJsonObject();
        // get Concepts from Response
       JsonArray cons = obj.getAsJsonObject("response").getAsJsonArray("docs");
       for (JsonElement e : cons) {
           JsonObject conobj = e.getAsJsonObject();
           Concept c = new Concept();
           c.setIri(conobj.get("iri").getAsString());
    
           JsonElement descrElement=  conobj.get("description");
           if(descrElement!=null){
               JsonElement descr = descrElement.getAsJsonArray().get(0);
               if(!descr.isJsonNull()) {
                   c.setDescription(descr.getAsString());
               }else{
                   c.setDescription("");
               }
           }
           JsonElement synonymElement=  conobj.get("synonym");
           if(synonymElement!=null){
               Gson gson = new Gson();
               String[] synonyms = gson.fromJson(synonymElement, String[].class);
               if(synonyms!=null) {
                   c.setAltLabels(Arrays.asList(synonyms));
               }
           }
          
           
           c.setPrefLabel(conobj.get("label").getAsString());
           concepts.add(c);
       }
    
        // get pagination information from response
        
        // rows
        //https://lucene.apache.org/solr/guide/6_6/common-query-parameters.html#CommonQueryParameters-TherowsParameter
        int  numberOfElementsInThisPage= obj.getAsJsonObject("responseHeader").getAsJsonObject("params").get("rows").getAsInt();
        
        // start is the offset
        //https://lucene.apache.org/solr/guide/6_6/common-query-parameters.html#CommonQueryParameters-ThestartParameter
        //response.start
        int offset = obj.getAsJsonObject("response").get("start").getAsInt();
        
        // numFound totalNumber of Elements
        long numberOfElements= obj.getAsJsonObject("response").get("numFound").getAsInt();
        // response.numFound
    

        // offset = page * size =>  ceil (offset/size) = page
        int numberOfThisPage = (int) Math.ceil(offset/numberOfElementsInThisPage);
        
        
        Page<Concept> retuner = new PageImpl<Concept>(concepts,  PageRequest.of(numberOfThisPage,numberOfElementsInThisPage),numberOfElements);
        return retuner;
    }
}
