package com.test.retrospective.services;

import com.test.retrospective.model.Feedback;
import com.test.retrospective.model.Retrospective;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;

interface RetrospectiveService {
    Retrospective createRetrospective(Retrospective retrospective);

    Retrospective findRetrospective(String retrospectiveName);
    Retrospective addFeedbackItem(String retrospectiveName, Feedback feedback);

    void updateFeedbackItem(String retrospectiveName, Feedback feedback);
    Page<Retrospective> retrieveAllRetrospectives(PageRequest pageRequest);

    Page<Retrospective> filterRetrospectiveByDate(Date date, PageRequest pageRequest);
}
