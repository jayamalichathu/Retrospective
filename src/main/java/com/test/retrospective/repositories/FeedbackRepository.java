package com.test.retrospective.repositories;

import com.test.retrospective.model.Feedback;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository interface for managing Feedback entities. Extends CrudRepository for basic CRUD operations.
 */
public interface FeedbackRepository extends CrudRepository<Feedback, Long> {
}
