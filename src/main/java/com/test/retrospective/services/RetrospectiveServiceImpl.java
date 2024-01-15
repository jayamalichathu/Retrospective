package com.test.retrospective.services;

import com.test.retrospective.exceptions.*;
import com.test.retrospective.model.Feedback;
import com.test.retrospective.model.Retrospective;
import com.test.retrospective.repositories.FeedbackRepository;
import com.test.retrospective.repositories.RetrospectiveRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Implementation of the RetrospectiveService interface.
 */
@Service
public class RetrospectiveServiceImpl implements RetrospectiveService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetrospectiveServiceImpl.class);
    private final RetrospectiveRepository retrospectiveRepository;
    private final FeedbackRepository feedbackRepository;

    RetrospectiveServiceImpl(RetrospectiveRepository retrospectiveRepository, FeedbackRepository feedbackRepository) {
        this.retrospectiveRepository = retrospectiveRepository;
        this.feedbackRepository = feedbackRepository;
    }

    public Retrospective createRetrospective(Retrospective retrospective) {
        if (retrospective.getFeedbackItems() != null && !retrospective.getFeedbackItems().isEmpty()) {
            LOGGER.warn("Newly created retrospectives should not have feedback items.");
            throw new InvalidRetrospectiveException("Newly created retrospectives should not have feedback items.");
        }
        else if (retrospective.getDate() == null || retrospective.getParticipants() == null
                || retrospective.getParticipants().isEmpty()) {
            LOGGER.error("Date or participants are required.");
            throw new InvalidRetrospectiveException("Date or participants are required.");
        }
        else if (findRetrospective(retrospective.getName()) == null) {
            LOGGER.info("Creating a new retrospective with name: {}", retrospective.getName());
            return retrospectiveRepository.save(retrospective);
        }
        else {
            String errorMessage = "Retrospective with name " + retrospective.getName() + " already exists";
            LOGGER.error(errorMessage);
            throw new DuplicateRetrospectiveException("Retrospective with name " + retrospective.getName() + " already exists");
        }
    }

    @Override
    public Retrospective findRetrospective(String retrospectiveName) {
        try {
            LOGGER.info("Trying to find retrospective with name: {}", retrospectiveName);
            Optional<Retrospective> retrospectiveOptional = retrospectiveRepository.findById(retrospectiveName);
            return retrospectiveOptional.orElse(null);
        } catch (Exception exception) {
            LOGGER.error("An error occurred while finding a retrospective", exception);
            throw new DatasourceException("An error occurred while finding a retrospective", exception);
        }
    }

    public Retrospective addFeedbackItem(String retrospectiveName, Feedback feedback) {
        LOGGER.info("Adding feedback item to retrospective with name: {}", retrospectiveName);
        Retrospective retrospective = findRetrospective(retrospectiveName);
        if (retrospective != null) {
            LOGGER.info("Retrospective found. Adding feedback: {}", feedback);
            feedback.setRetrospective(retrospective);
            List<Feedback> feedbackList = retrospective.getFeedbackItems();
            if (feedbackList == null) {
                feedbackList = List.of(feedback);
            } else {
                feedbackList.add(feedback);
            }

            retrospective.setFeedbackItems(feedbackList);

            feedbackRepository.save(feedback);
            retrospectiveRepository.save(retrospective);

            LOGGER.info("Feedback added successfully to retrospective: {}", retrospective);
            return retrospective;
        } else {
            LOGGER.warn("Retrospective with name {} not found", retrospectiveName);
            throw new RetrospectiveNotFoundException("Retrospective with name " + retrospectiveName + " not found");
        }
    }

    @Override
    public void updateFeedbackItem(String retrospectiveName, Feedback modifiedFeedback) {
        LOGGER.info("Updating feedback item in retrospective with name: {}", retrospectiveName);

        Retrospective retrospective = findRetrospective(retrospectiveName);
        if (retrospective != null) {
            List<Feedback> feedbackList = retrospective.getFeedbackItems();
            if (feedbackList != null && !feedbackList.isEmpty()) {
                // Find the existing Feedback in the list
                Optional<Feedback> existingFeedbackOptional = feedbackList.stream()
                        .filter(feedback -> feedback.getId().equals(modifiedFeedback.getId()))
                        .findFirst();

                if (existingFeedbackOptional.isPresent()) {
                    Feedback existingFeedback = existingFeedbackOptional.get();
                    existingFeedback.setFeedbackType(modifiedFeedback.getFeedbackType());
                    existingFeedback.setBody(modifiedFeedback.getBody());

                    feedbackRepository.save(existingFeedback);
                    LOGGER.info("Feedback item updated successfully: {}", existingFeedback);
                }
                else {
                    LOGGER.warn("Feedback with ID {} not found in Retrospective {}", modifiedFeedback.getId(), retrospectiveName);
                    throw new FeedbackNotFoundException("Feedback with ID " + modifiedFeedback.getId() + " not found in Retrospective " + retrospectiveName);
                }
            }
            else {
                LOGGER.warn("No feedback items found in Retrospective {}", retrospectiveName);
                throw new FeedbackNotFoundException("Feedback items are not found in the retrospectiveName " + retrospectiveName);
            }
        }
        else {
            LOGGER.warn("Retrospective with name {} not found", retrospectiveName);
            throw new RetrospectiveNotFoundException("Retrospective with name " + retrospectiveName + " not found");
        }
    }

    /**
     * Converts a Page of Retrospective entities into a PageData object.
     *
     * @param retrospectivesPage Page of Retrospective entities.
     * @return PageData containing information about the page.
     */
    private PageData makePageData( Page<Retrospective> retrospectivesPage) {
        return new PageData(
                retrospectivesPage.getTotalPages(),
                retrospectivesPage.getTotalElements(),
                retrospectivesPage.getNumber(),
                retrospectivesPage.getSize(),
                retrospectivesPage.getContent() );
    }

    @Override
    public PageData retrieveAllRetrospectives(PageRequest pageRequest) {
        try {
            LOGGER.info("Retrieving all retrospectives with pagination: {}", pageRequest);

            PageData retrospectivesPage = makePageData(retrospectiveRepository.findAll(pageRequest));
            LOGGER.info("Retrieved {} retrospectives for page {} with {} items per page",
                    retrospectivesPage.totalElements(),
                    retrospectivesPage.pageNumber(),
                    retrospectivesPage.size());
            return retrospectivesPage;
        } catch (Exception exception) {
            LOGGER.error("An error occurred while retrieving retrospectives", exception);
            throw new DatasourceException("An error occurred while retrieving retrospectives", exception); // Re-throw the exception after logging
        }
    }

    @Override
    public PageData filterRetrospectiveByDate(Date date, PageRequest pageRequest) {
        try {
            LOGGER.info("Filtering retrospectives by date: {}", date);
            PageData pageData = makePageData(retrospectiveRepository.findByDate(date, pageRequest));
            LOGGER.info("Retrieved {} retrospectives for date: {}", pageData.totalElements(), date);
            return pageData;
        } catch (Exception exception) {
            LOGGER.error("An error occurred while filtering retrospectives by date", exception);
            throw new DatasourceException("An error occurred while filtering retrospectives by date", exception); // Re-throw the exception after logging
        }
    }

}
