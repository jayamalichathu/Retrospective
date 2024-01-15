package com.test.retrospective.services;

import com.test.retrospective.model.Feedback;
import com.test.retrospective.model.Retrospective;
import org.springframework.data.domain.PageRequest;

import java.util.Date;

/**
 * Service interface for managing retrospectives.
 */
interface RetrospectiveService {

    /**
     * Creates a new retrospective.
     *
     * @param retrospective The retrospective to be created.
     * @return The created retrospective.
     */
    Retrospective createRetrospective(Retrospective retrospective);

    /**
     * Finds a retrospective by name.
     *
     * @param retrospectiveName The name of the retrospective to find.
     * @return The found retrospective.
     */
    Retrospective findRetrospective(String retrospectiveName);

    /**
     * Adds a feedback item to a retrospective.
     *
     * @param retrospectiveName The name of the retrospective.
     * @param feedback          The feedback item to be added.
     * @return The updated retrospective.
     */
    Retrospective addFeedbackItem(String retrospectiveName, Feedback feedback);

    /**
     * Updates a feedback item of a retrospective.
     *
     * @param retrospectiveName The name of the retrospective.
     * @param feedback          The updated feedback item.
     */
    void updateFeedbackItem(String retrospectiveName, Feedback feedback);

    /**
     * Retrieves all retrospectives with pagination.
     *
     * @param pageRequest PageRequest object for pagination.
     * @return PageData containing retrospectives.
     */
    PageData retrieveAllRetrospectives(PageRequest pageRequest);

    /**
     * Filters retrospectives by date with pagination.
     *
     * @param date        Date for filtering retrospectives.
     * @param pageRequest PageRequest object for pagination.
     * @return PageData containing filtered retrospectives.
     */
    PageData filterRetrospectiveByDate(Date date, PageRequest pageRequest);
}
