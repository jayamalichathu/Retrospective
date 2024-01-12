package com.test.retrospective.services;

import com.test.retrospective.exceptions.DuplicateRetrospectiveException;
import com.test.retrospective.exceptions.FeedbackNotFoundException;
import com.test.retrospective.exceptions.InvalidRetrospectiveException;
import com.test.retrospective.exceptions.RetrospectiveNotFoundException;
import com.test.retrospective.model.Feedback;
import com.test.retrospective.model.FeedbackType;
import com.test.retrospective.model.Retrospective;
import com.test.retrospective.repositories.RetrospectiveRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hibernate.internal.util.collections.CollectionHelper.listOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
public class RetrospectiveServiceImplTest {
    @Mock
    RetrospectiveRepository repository;

    @InjectMocks
    RetrospectiveServiceImpl retrospectiveService;

    @Test
    public void shouldReturnRetrospectiveNotFoundException_WhenFeedbackIsAddedTo_NonExistingRetrospective() {
        Feedback feedback = new Feedback("John", "Good Feedback", FeedbackType.POSITIVE);
        when(repository.findById("Ret_122")).thenReturn(null);

        String nonexistentRetrospectiveName = "NonexistentRetrospective";
        assertThrows(RetrospectiveNotFoundException.class, () -> {
            retrospectiveService.addFeedbackItem(nonexistentRetrospectiveName, feedback);
        });
    }

    @Test
    public void shouldReturnInvalidRetrospectiveException_WhenCreate_RetrospectiveWithNonEmptyFeedbackList() {
        Retrospective retrospective = new Retrospective("Ret_122", new Date(), List.of("John", "Malan") );

        Feedback feedback = new Feedback("John", "Good Feedback", FeedbackType.POSITIVE);
        retrospective.setFeedbackItems(listOf(feedback));

        assertThrows(InvalidRetrospectiveException.class, () -> {
            retrospectiveService.createRetrospective(retrospective);
        });
    }

    @Test
    public void shouldReturnInvalidRetrospectiveException_WhenCreate_RetrospectiveWithNoDate() {
        Retrospective retrospective = new Retrospective("Ret_122", null, List.of("John", "Malan") );

        assertThrows(InvalidRetrospectiveException.class, () -> {
            retrospectiveService.createRetrospective(retrospective);
        });
    }

    @Test
    public void shouldReturnInvalidRetrospectiveException_WhenCreate_RetrospectiveWithNoParticipants() {
        Retrospective retrospective = new Retrospective("Ret_122", new Date(), null );

        assertThrows(InvalidRetrospectiveException.class, () -> {
            retrospectiveService.createRetrospective(retrospective);
        });
    }

    @Test
    public void shouldReturnInvalidRetrospectiveException_WhenCreate_RetrospectiveWithExistingName() {
        Retrospective retrospective = new Retrospective("Ret_122", new Date(), List.of("John", "Malan") );
        when(repository.findById("Ret_122")).thenReturn(Optional.of(retrospective));

        assertThrows(DuplicateRetrospectiveException.class, () -> {
            retrospectiveService.createRetrospective(retrospective);
        });
    }

    @Test
    public void shouldReturnRetrospective_WhenCreate_Retrospective() {
        Retrospective retrospective = new Retrospective("Ret_122", new Date(), List.of("John", "Malan") );
        when(repository.save(retrospective)).thenReturn(retrospective);

        assertEquals(retrospectiveService.createRetrospective(retrospective).getName(), "Ret_122");
    }

    @Test
    public void shouldReturnFeedbackNotFoundException_WhenUpdate_Feedback_WhenFeedbacklistDoesNotExist() {
        Retrospective retrospective = new Retrospective("Ret_122", new Date(), List.of("John", "Malan") );
        when(repository.findById("Ret_122")).thenReturn(Optional.of(retrospective));

        Feedback feedback = new Feedback("John", "Good Feedback", FeedbackType.POSITIVE);
        FeedbackNotFoundException exception = assertThrows(FeedbackNotFoundException.class, () -> {
            retrospectiveService.updateFeedbackItem("Ret_122", feedback);
        });

        assertEquals("Feedback items are not found in the retrospectiveName Ret_122", exception.getMessage());
    }

    @Test
    public void shouldReturnFeedbackNotFoundException_WhenUpdate_FeedbackWithNotExistingFeedbackName() {
        Retrospective retrospective = new Retrospective("Ret_122", new Date(), List.of("John", "Malan") );
        Feedback feedback = new Feedback("John2", "Good Feedback", FeedbackType.POSITIVE);
        feedback.setId(1L);
        retrospective.setFeedbackItems(listOf(feedback));

        when(repository.findById("Ret_122")).thenReturn(Optional.of(retrospective));

        Feedback modifiedFeedback = new Feedback("John2", "Back Feedback", FeedbackType.NEGATIVE);
        modifiedFeedback.setId(2L);
        FeedbackNotFoundException exception = assertThrows(FeedbackNotFoundException.class, () -> {
            retrospectiveService.updateFeedbackItem("Ret_122", modifiedFeedback);
        });

        assertEquals("Feedback with ID 2 not found in Retrospective Ret_122", exception.getMessage());
    }



}
