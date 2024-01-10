package com.test.retrospective.repositories;

import com.test.retrospective.model.Feedback;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FeedbackRepository extends CrudRepository<Feedback, Long> {
    List<Feedback> findByRetrospectiveName(String retrospectiveName);
}
