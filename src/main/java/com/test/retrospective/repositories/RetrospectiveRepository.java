package com.test.retrospective.repositories;

import com.test.retrospective.model.Retrospective;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Repository interface for managing Retrospective entities. Extends JpaRepository for JPA-based CRUD operations.
 */
@Repository
public interface RetrospectiveRepository extends JpaRepository<Retrospective, String> {

    /**
     * Custom query method to find retrospectives by date with pagination.
     *
     * @param date     Date to filter retrospectives.
     * @param pageable Pageable object for pagination.
     * @return Page of retrospectives matching the specified date.
     */
    Page<Retrospective> findByDate(Date date, Pageable pageable);
}
