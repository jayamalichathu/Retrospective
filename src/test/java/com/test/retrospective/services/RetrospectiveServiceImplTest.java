package com.test.retrospective.services;

import com.test.retrospective.exceptions.RetrospectiveNotFoundException;
import com.test.retrospective.model.Feedback;
import com.test.retrospective.model.FeedbackType;
import com.test.retrospective.repositories.RetrospectiveRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
public class RetrospectiveServiceImplTest {
    @Mock
    RetrospectiveRepository repository;

    @InjectMocks
    RetrospectiveServiceImpl retrospectiveService;

    @Test
    public void shouldReturnRetrospectiveNotFoundException_WhenFeedbackIsAddedTo_NonExistingRetrospective() throws Exception {
        Feedback feedback = new Feedback("John", "Good Feedback", FeedbackType.POSITIVE);
        when(repository.findById("Ret_122")).thenReturn(null);

        String nonexistentRetrospectiveName = "NonexistentRetrospective";
        assertThrows(RetrospectiveNotFoundException.class, () -> {
            retrospectiveService.addFeedbackItem(nonexistentRetrospectiveName, feedback);
        });
    }

}
