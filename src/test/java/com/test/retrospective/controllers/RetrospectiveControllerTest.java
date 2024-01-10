package com.test.retrospective.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.retrospective.exceptions.RetrospectiveNotFoundException;
import com.test.retrospective.model.Feedback;
import com.test.retrospective.model.FeedbackType;
import com.test.retrospective.model.Retrospective;
import com.test.retrospective.services.RetrospectiveServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RetrospectiveController.class)
public class RetrospectiveControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    RetrospectiveServiceImpl retrospectiveService;

    @Test
    public void shouldReturnStatus201_WhenCorrectRetrospectiveIsSent() throws Exception {

        Retrospective retrospective = new Retrospective("Ret_122", new Date(), List.of("John", "Malan") );

        when(retrospectiveService.createRetrospective(retrospective)).thenReturn(retrospective);
        String requestBody = asJsonString(retrospective);

        ResultActions resultActions = mockMvc.perform(post("/retrospectives/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        resultActions.andExpect(status().isCreated())
                .andExpect(content().string("Retrospective created successfully"));
    }

    @Test
    public void shouldReturnStatus200_WhenFeedbackIsAddedToRetrospective() throws Exception {
        Retrospective retrospective = new Retrospective("Ret_122", new Date(), List.of("John", "Malan") );
        //when(retrospectiveService.createRetrospective(retrospective)).thenReturn(retrospective);
        when(retrospectiveService.findRetrospective("Ret_122")).thenReturn(retrospective);

        Feedback feedback = new Feedback("John", "Good Feedback", FeedbackType.POSITIVE);

        ResultActions resultActions = mockMvc.perform(post("/retrospectives/Ret_122/addFeedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(feedback)));

        resultActions.andExpect(status().isOk())
                .andExpect(content().string("Feedback item added successfully"));
    }

    @Test
    public void shouldReturnRetrospectiveNotFoundException_WhenFeedbackIsAddedTo_NonExistingRetrospective() throws Exception {
        Feedback feedback = new Feedback("John", "Good Feedback", FeedbackType.POSITIVE);
        when(retrospectiveService.findRetrospective("Ret_122")).thenReturn(null);

        String nonexistentRetrospectiveName = "NonexistentRetrospective";
        assertThrows(RetrospectiveNotFoundException.class, () -> {
            mockMvc.perform(post("/retrospectives/{retrospectiveName}/addFeedback", nonexistentRetrospectiveName)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(asJsonString(feedback)));
        });

//        ResultActions resultActions = mockMvc.perform(post("/retrospectives/Ret_122/addFeedback")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(feedback)));
//
//        resultActions.andExpect(status().)
//                .andExpect(content().string("Feedback item added successfully"));


    }

    // Utility method to convert objects to JSON string
    private String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

}
