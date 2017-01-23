package discussion.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;


public class Entry {

    private UUID ID;
    private String title;
    private String text;
    private List<Entry> responses;

    public UUID getID() {
        return ID;
    }

    public Entry setID(UUID ID) {
        this.ID = ID;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Entry setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getText() {
        return text;
    }

    public Entry setText(String text) {
        this.text = text;
        return this;
    }

    public List<Entry> getResponses() {
        if(responses == null){
            return new ArrayList<>();
        }
        return responses;
    }

    public Entry setResponses(List<Entry> responses) {
        this.responses = responses;
        return this;
    }

    @JsonIgnore
    public Stream<Entry> flattened() {
        return Stream.concat(
                Stream.of(this),
                responses.stream().flatMap(Entry::flattened));
    }

}
