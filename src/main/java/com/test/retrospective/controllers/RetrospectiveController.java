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

/**
 * Controller class for handling retrospective-related endpoints.
 */
@RestController
@RequestMapping("/retrospectives")
public class RetrospectiveController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RetrospectiveController.class);
    private final RetrospectiveServiceImpl retrospectiveService;

    /**
     * Constructor for RetrospectiveController.
     * @param retrospectiveService Retrospective service for handling business logic.
     */
    public RetrospectiveController(RetrospectiveServiceImpl retrospectiveService) {
        this.retrospectiveService = retrospectiveService;
    }

    /**
     * Endpoint to retrieve all retrospectives with pagination.
     * @param currentPage Current page number for pagination.
     * @param size Number of items per page.
     * @return PageData containing retrospectives.
     */
    @GetMapping
    public PageData getAllRetrospectives(@RequestParam(defaultValue = "0") int currentPage,
                                         @RequestParam(defaultValue = "10") int size) {
        LOGGER.info("Get all retrospectives");
        PageRequest pageRequest = PageRequest.of(currentPage, size);
        return retrospectiveService.retrieveAllRetrospectives(pageRequest);
    }


    /**
     * Endpoint to search retrospectives by date with pagination.
     * @param date Date for filtering retrospectives.
     * @param currentPage Current page number for pagination.
     * @param size Number of items per page.
     * @return PageData containing filtered retrospectives.
     */
    @GetMapping("/searchByDate")
    public PageData searchRetrospectivesByDate(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                                @RequestParam(defaultValue = "0") int currentPage,
                                                @RequestParam(defaultValue = "10") int size) {
        LOGGER.info("Search retrospectives according to the date");
        PageRequest pageRequest = PageRequest.of(currentPage, size);
        return retrospectiveService.filterRetrospectiveByDate(date, pageRequest);
    }

    /**
     * Endpoint to create a new retrospective.
     * @param retrospective Retrospective object to be created.
     * @return ResponseEntity indicating the success of the operation.
     */
    @PostMapping("/create")
    public ResponseEntity<String> createRetrospective(@RequestBody Retrospective retrospective) {
        LOGGER.info("Create a retrospective");
        retrospectiveService.createRetrospective(retrospective);
        return new ResponseEntity<>("Retrospective created successfully", HttpStatus.CREATED);
    }

    /**
     * Endpoint to add feedback to a specific retrospective.
     * @param retrospectiveName Name of the retrospective.
     * @param feedback Feedback object to be added.
     * @return ResponseEntity indicating the success of the operation.
     */
    @PostMapping("/{retrospectiveName}/addFeedback")
    public ResponseEntity<String> addFeedbackItemToRetrospective(@PathVariable String retrospectiveName
            , @RequestBody Feedback feedback) {
        LOGGER.info("Add a feedback to the given retrospective");
        retrospectiveService.addFeedbackItem(retrospectiveName, feedback);
        return new ResponseEntity<>("Feedback item added successfully", HttpStatus.OK);
    }

    /**
     * Endpoint to update feedback of a specific retrospective.
     * @param retrospectiveName Name of the retrospective.
     * @param feedback Feedback object to be updated.
     * @return ResponseEntity indicating the success of the operation.
     */
    @PutMapping("/{retrospectiveName}/updateFeedbackItem")
    public ResponseEntity<String> updateFeedbackItem(@PathVariable String retrospectiveName, @RequestBody Feedback feedback) {
        LOGGER.info("Update a feedback of the given retrospective");
        retrospectiveService.updateFeedbackItem(retrospectiveName, feedback);
        return new ResponseEntity<>("Feedback item updated successfully", HttpStatus.OK);
    }

}
