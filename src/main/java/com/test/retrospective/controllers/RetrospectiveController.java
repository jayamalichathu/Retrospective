package com.test.retrospective.controllers;

import com.test.retrospective.model.Feedback;
import com.test.retrospective.services.PageData;
import com.test.retrospective.model.Retrospective;
import com.test.retrospective.services.RetrospectiveServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/retrospectives")
public class RetrospectiveController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RetrospectiveController.class);
    private final RetrospectiveServiceImpl retrospectiveService;

    public RetrospectiveController(RetrospectiveServiceImpl retrospectiveService) {
        this.retrospectiveService = retrospectiveService;
    }

    @GetMapping
    public PageData getAllRetrospectives(@RequestParam(defaultValue = "0") int currentPage,
                                         @RequestParam(defaultValue = "10") int size) {
        LOGGER.info("Get all retrospectives");
        PageRequest pageRequest = PageRequest.of(currentPage, size);
        return retrospectiveService.retrieveAllRetrospectives(pageRequest);
    }

    @GetMapping("/searchByDate")
    public PageData searchRetrospectivesByDate(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                                @RequestParam(defaultValue = "0") int currentPage,
                                                @RequestParam(defaultValue = "10") int size) {
        LOGGER.info("Search retrospectives according to the date");
        PageRequest pageRequest = PageRequest.of(currentPage, size);
        return retrospectiveService.filterRetrospectiveByDate(date, pageRequest);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createRetrospective(@RequestBody Retrospective retrospective) {
        LOGGER.info("Create a retrospective");
        retrospectiveService.createRetrospective(retrospective);
        return new ResponseEntity<>("Retrospective created successfully", HttpStatus.CREATED);
    }

    @PostMapping("/{retrospectiveName}/addFeedback")
    public ResponseEntity<String> addFeedbackItemToRetrospective(@PathVariable String retrospectiveName
            , @RequestBody Feedback feedback) {
        LOGGER.info("Add a feedback to the given retrospective");
        retrospectiveService.addFeedbackItem(retrospectiveName, feedback);
        return new ResponseEntity<>("Feedback item added successfully", HttpStatus.OK);
    }

    @PutMapping("/{retrospectiveName}/updateFeedbackItem")
    public ResponseEntity<String> updateFeedbackItem(@PathVariable String retrospectiveName, @RequestBody Feedback feedback) {
        LOGGER.info("Update a feedback of the given retrospective");
        retrospectiveService.updateFeedbackItem(retrospectiveName, feedback);
        return new ResponseEntity<>("Feedback item updated successfully", HttpStatus.OK);
    }

}
