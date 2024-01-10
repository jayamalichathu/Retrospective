package com.test.retrospective.controllers;

import com.test.retrospective.exceptions.InvalidRetrospectiveException;
import com.test.retrospective.model.Feedback;
import com.test.retrospective.model.Retrospective;
import com.test.retrospective.services.RetrospectiveServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/retrospectives")
public class RetrospectiveController {
    private final RetrospectiveServiceImpl retrospectiveService;

    public RetrospectiveController(RetrospectiveServiceImpl retrospectiveService) {
        this.retrospectiveService = retrospectiveService;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Page<Retrospective> getAllRetrospectives(
                                               @RequestParam(defaultValue = "0") int currentPage,
                                               @RequestParam(defaultValue = "10") int size) {
        PageRequest pageRequest = PageRequest.of(currentPage, size);
        Page<Retrospective> pageResponse = retrospectiveService.retrieveAllRetrospectives(pageRequest);
        MediaType responseContentType = MediaType.APPLICATION_JSON;
//        if (contentType.equals(MediaType.APPLICATION_ATOM_XML_VALUE)){
//            responseContentType = MediaType.APPLICATION_ATOM_XML;
//        }
        return pageResponse;
    }

    @GetMapping("/searchByDate")
    public ResponseEntity<Page<Retrospective>> searchRetrospectivesByDate(
            @RequestHeader(value="Content-Type") String contentType,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam(defaultValue = "0") int currentPage,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(currentPage, size);
        Page<Retrospective> pageResponse = retrospectiveService.filterRetrospectiveByDate(date, pageRequest);

        MediaType responseContentType = MediaType.APPLICATION_JSON;
        if (contentType.equals( "application/xml") ){
            responseContentType = MediaType.APPLICATION_ATOM_XML;
        }
        return ResponseEntity.ok()
                .contentType(responseContentType)
                .body(pageResponse);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createRetrospective(@RequestBody Retrospective retrospective) {
        if (retrospective.getFeedbackItems() != null && !retrospective.getFeedbackItems().isEmpty()) {
            throw new InvalidRetrospectiveException("Newly created retrospectives should not have feedback items.");
        }
        if (retrospective.getDate() == null || retrospective.getParticipants() == null
                || retrospective.getParticipants().isEmpty()) {
            throw new InvalidRetrospectiveException("Date or participants are required.");
        }
        else {
            retrospectiveService.createRetrospective(retrospective);
        }

        return new ResponseEntity<>("Retrospective created successfully", HttpStatus.CREATED);
    }

    @PostMapping("/{retrospectiveName}/addFeedback")
    public ResponseEntity<String> addFeedbackItemToRetrospective(@PathVariable String retrospectiveName
            , @RequestBody Feedback feedback) {
        retrospectiveService.addFeedbackItem(retrospectiveName, feedback);
        return new ResponseEntity<>("Feedback item added successfully", HttpStatus.OK);
    }

    @PutMapping("/{retrospectiveName}/updateFeedbackItem")
    public ResponseEntity<String> updateFeedbackItem(@PathVariable String retrospectiveName, @RequestBody Feedback feedback) {
        retrospectiveService.updateFeedbackItem(retrospectiveName, feedback);
        return new ResponseEntity<>("Feedback item updated successfully", HttpStatus.OK);
    }

}
