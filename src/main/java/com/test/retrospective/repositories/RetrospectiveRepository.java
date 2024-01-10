package com.test.retrospective.repositories;

import com.test.retrospective.model.Retrospective;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface RetrospectiveRepository extends JpaRepository<Retrospective, String> {
    // Add custom query methods if needed
    Page<Retrospective> findByDate(Date date, Pageable pageable);
}
