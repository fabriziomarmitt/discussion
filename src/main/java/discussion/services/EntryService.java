package discussion.services;


import discussion.exceptions.EntryDuplicatedException;
import discussion.models.Entry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class EntryService {
    private List<Entry> entries = new ArrayList<>();

    private static final EntryService INSTANCE = new EntryService();

    private EntryService() {}

    public static EntryService getInstance() {
        Entry entry = new Entry(){{
            setText("New Entry test");
            setTitle("New Entry test");
        }};
        try {
            INSTANCE.add(entry);
        } catch (EntryDuplicatedException e) {
            e.printStackTrace();
        }
        return INSTANCE;
    }

    private Entry generateUUID(Entry entry){
        entry.setID(UUID.randomUUID());
        return entry;
    }

    private boolean entryExists(Entry entry){
        return entries.stream().anyMatch(e -> e.getID() == entry.getID());
    }

    private List<Entry> getAllFromEntry(Entry entry){
        List<Entry> listOfNodes = new ArrayList<>();
        if (entry != null) {
            listOfNodes.add(entry);
            List<Entry> children = entry.getResponses();
            for(int x = 0; x < children.size(); x++){
                Entry n = children.get(x);
                for (Entry child : children) {
                    listOfNodes.add(child);
                    if(child.getResponses() != null){
                        if(child.getResponses().size() > 0){
                            listOfNodes.addAll(getAllFromEntry(child));
                        }
                    }
                }
            }
        }
        return listOfNodes;
    }

    public List<Entry> getAll(){
        List<Entry> listOfNodes = new ArrayList<>();
        for(int x = 0; x < entries.size(); x++){
            listOfNodes.addAll(getAllFromEntry(entries.get(x)));
        }
        return listOfNodes;
    }

    public Entry getEntry(UUID uuid){
        if(entries.size() == 0){
            return null;
        }
        return getAll().stream().filter(e -> e.getID().equals(uuid)).findFirst().orElse(null);
    }

    public Entry add(Entry entry) throws EntryDuplicatedException {
        if (entryExists(entry)){
            throw new EntryDuplicatedException(String.format("Entry with ID %s already exists", entry.getID()));
        }
        entry = generateUUID(entry);
        entries.add(entry);
        return entry;
    }

    public Entry addResponse(Entry parentEntry, Entry responseEntry) {
       List<Entry> parentEntryResponseList = parentEntry.getResponses();
       if(parentEntryResponseList == null){
           parentEntryResponseList = new ArrayList<>();
       }
       parentEntryResponseList.add(generateUUID(responseEntry));
       return responseEntry;
    }
}
