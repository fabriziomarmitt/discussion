package discussion.controllers;


import discussion.exceptions.EntryDuplicatedException;
import discussion.models.Entry;
import discussion.services.EntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ApiController{

    EntryService entryService;

    public ApiController(){
        entryService = EntryService.getInstance();
    }

    @RequestMapping("/entry/getAll")
    public List<Entry> getAllEntries(){
        return entryService.getAll();
    }

    @RequestMapping("/entry/{entryId}")
    public  ResponseEntity<?> getEntry(@PathVariable String entryId){
        Entry entry =  entryService.getEntry(UUID.fromString(entryId));
        if (entry == null){
            return new ResponseEntity<>("Entity not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(entry);
    }

    @RequestMapping(value = "/entry/new", method = RequestMethod.POST)
    public ResponseEntity<?> addNewEntry(Entry entry){
        try {
            entry = entryService.add(entry);
            ServletUriComponentsBuilder uriComponentsBuilder = ServletUriComponentsBuilder
                    .fromCurrentContextPath();
            URI location = uriComponentsBuilder
                    .path("/api/entry/{id}")
                    .buildAndExpand(entry.getID().toString()).toUri();
            return ResponseEntity.created(location).build();
        } catch (EntryDuplicatedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/entry/{id}/response", method = RequestMethod.POST)
    public ResponseEntity<?> addNewResponse(@PathVariable UUID entryId, Entry entry){
        Entry parentEntry = entryService.getEntry(entryId);
        if(parentEntry == null){
            return new ResponseEntity<>("Entity not found", HttpStatus.NOT_FOUND);
        }
        entryService.addResponse(parentEntry, entry);
        ServletUriComponentsBuilder uriComponentsBuilder = ServletUriComponentsBuilder
                .fromCurrentContextPath();
        URI location = uriComponentsBuilder
                .path("/api/entry/{id}")
                .buildAndExpand(entry.getID().toString()).toUri();
        return ResponseEntity.created(location).build();
    }


}
