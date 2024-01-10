package com.test.retrospective.services;

import com.test.retrospective.exceptions.DuplicateRetrospectiveException;
import com.test.retrospective.exceptions.FeedbackNotFoundException;
import com.test.retrospective.exceptions.RetrospectiveNotFoundException;
import com.test.retrospective.model.Feedback;
import com.test.retrospective.model.Retrospective;
import com.test.retrospective.repositories.FeedbackRepository;
import com.test.retrospective.repositories.RetrospectiveRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RetrospectiveServiceImpl implements RetrospectiveService {
    private final RetrospectiveRepository retrospectiveRepository;
    private final FeedbackRepository feedbackRepository;
    RetrospectiveServiceImpl(RetrospectiveRepository retrospectiveRepository, FeedbackRepository feedbackRepository) {
        this.retrospectiveRepository = retrospectiveRepository;
        this.feedbackRepository = feedbackRepository;
    }
    public Retrospective createRetrospective(Retrospective retrospective) {
        if (findRetrospective(retrospective.getName()) == null) {
            return retrospectiveRepository.save(retrospective);
        }
        else {
            throw new DuplicateRetrospectiveException("Retrospective with name " + retrospective.getName() + " already exists");
        }
    }

    @Override
    public Retrospective findRetrospective(String retrospectiveName) {
        Optional<Retrospective> retrospectiveOptional = retrospectiveRepository.findById(retrospectiveName);
        return retrospectiveOptional.orElse(null);
    }

    public Retrospective addFeedbackItem(String retrospectiveName, Feedback feedback) {
        Retrospective retrospective = findRetrospective(retrospectiveName);
        if (retrospective != null) {
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

            return retrospective;
        } else {
            throw new RetrospectiveNotFoundException("Retrospective with name " + retrospectiveName + " not found");
        }
    }

    @Override
    public void updateFeedbackItem(String retrospectiveName, Feedback modifiedFeedback) {
        Retrospective retrospective = findRetrospective(retrospectiveName);
        if (retrospective != null) {
            List<Feedback> feedbackList = retrospective.getFeedbackItems();
            if (!feedbackList.isEmpty()) {
                // Find the existing Feedback in the list
                Optional<Feedback> existingFeedbackOptional = feedbackList.stream()
                        .filter(feedback -> feedback.getId().equals(modifiedFeedback.getId()))
                        .findFirst();

                if (existingFeedbackOptional.isPresent()) {
                    Feedback existingFeedback = existingFeedbackOptional.get();
                    existingFeedback.setFeedbackType(modifiedFeedback.getFeedbackType());
                    existingFeedback.setBody(modifiedFeedback.getBody());

                    feedbackRepository.save(existingFeedback);
                }
                else {
                    throw new FeedbackNotFoundException("Feedback with ID " + modifiedFeedback.getId() + " not found in Retrospective " + retrospectiveName);
                }
            }

        } else {
            throw new RetrospectiveNotFoundException("Retrospective with name " + retrospectiveName + " not found");
        }
    }

    @Override
    public Page<Retrospective> retrieveAllRetrospectives(PageRequest pageRequest) {
        return retrospectiveRepository.findAll(pageRequest);
    }

    @Override
    public Page<Retrospective> filterRetrospectiveByDate(Date date, PageRequest pageRequest) {
        return retrospectiveRepository.findByDate(date, pageRequest);
    }

//    private String generateUniqueName() {
//        // Implement logic to generate a unique name
//        return "Retrospective_" + System.currentTimeMillis();
//    }

}
