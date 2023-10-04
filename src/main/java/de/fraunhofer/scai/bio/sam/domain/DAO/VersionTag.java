package de.fraunhofer.scai.bio.sam.domain.DAO;

import java.time.OffsetDateTime;

public class VersionTag {

    private String tagName;
    private OffsetDateTime timestamp;
    private String description;

    public VersionTag(String tagName, OffsetDateTime timestamp){
        this.tagName = tagName;
        this.timestamp = timestamp;
        this.description = "desc";
    }

    /*public boolean isBefore(VersionTag tag){
        return this.timestamp.isBefore(tag.timestamp);
    }*/
    public String getTagName() {
        return tagName;
    }

    public OffsetDateTime getTimestamp(){
        return timestamp;
    }

    public String getDescription() {
        return description;
    }
}
