package com.test.retrospective.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.retrospective.model.Feedback;
import com.test.retrospective.model.FeedbackType;
import com.test.retrospective.services.PageData;
import com.test.retrospective.model.Retrospective;
import com.test.retrospective.services.RetrospectiveServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.hibernate.internal.util.collections.CollectionHelper.listOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RetrospectiveController.class)
public class RetrospectiveControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    RetrospectiveServiceImpl retrospectiveService;

    @Test
    void shouldReturnJsonResponse_WhenAcceptTypeIsJson_GetAllRetrospectives() throws Exception {
        PageData retrospectives = new PageData(1,1,0,5,listOf(
                new Retrospective("Ret_122", new Date(), List.of("John", "Malan") )));
        when(retrospectiveService.retrieveAllRetrospectives(any())).thenReturn(retrospectives);

        ResultActions resultActions = mockMvc.perform(get("/retrospectives?currentPage=0&size=5")
                        .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.retrospectiveList").isArray());
    }

    @Test
    void shouldReturnXmlReasponse_WhenAcceptTypeIsXml_GetAllRetrospectives() throws Exception {
        PageData retrospectives = new PageData(1,1,0,5,listOf(
                new Retrospective("Ret_122", new Date(), List.of("John", "Malan") )));
        when(retrospectiveService.retrieveAllRetrospectives(any())).thenReturn(retrospectives);

        ResultActions resultActions = mockMvc.perform(get("/retrospectives?currentPage=0&size=5")
                .accept(MediaType.APPLICATION_XML));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_XML+ ";charset=UTF-8"))
                .andExpect(xpath("/PageData/totalPages").string("1"));
    }

    private Date getDate(String date) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(date);

    }

    @Test
    void shouldReturnJsonResponse_WhenAcceptTypeIsJson_GetAllRetrospectivesByDate() throws Exception {
        String dateString = "2023-03-27";
        PageData retrospectives = new PageData(1,1,0,5,listOf(
                new Retrospective("Ret_122", getDate(dateString), List.of("John", "Malan") )));
        when(retrospectiveService.retrieveAllRetrospectives(any())).thenReturn(retrospectives);

        ResultActions resultActions = mockMvc.perform(get("/retrospectives?date=2023-03-272&currentPage=0&size=5")
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.retrospectiveList").isArray());
    }

    @Test
    void shouldReturnXmlResponse_WhenAcceptTypeIsXml_GetAllRetrospectivesByDate() throws Exception {
        String dateString = "2023-03-27";
        PageData retrospectives = new PageData(1,1,0,5,listOf(
                new Retrospective("Ret_122", getDate(dateString), List.of("John", "Malan") )));
        when(retrospectiveService.retrieveAllRetrospectives(any())).thenReturn(retrospectives);

        ResultActions resultActions = mockMvc.perform(get("/retrospectives?date=2023-03-27&currentPage=0&size=5")
                .accept(MediaType.APPLICATION_XML));

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_XML+ ";charset=UTF-8"))
                .andExpect(xpath("/PageData/totalPages").string("1"));
    }

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
        when(retrospectiveService.findRetrospective("Ret_122")).thenReturn(retrospective);

        Feedback feedback = new Feedback("John", "Good Feedback", FeedbackType.POSITIVE);

        ResultActions resultActions = mockMvc.perform(post("/retrospectives/Ret_122/addFeedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(feedback)));

        resultActions.andExpect(status().isOk())
                .andExpect(content().string("Feedback item added successfully"));
    }

    @Test
    void shouldReturnStatus200_WhenUpdateFeedbackItem() throws Exception {
        Retrospective retrospective = new Retrospective("Ret_122", new Date(), List.of("John", "Malan") );

        Feedback feedback = new Feedback("John", "Good Feedback", FeedbackType.POSITIVE);
        retrospective.setFeedbackItems(listOf(feedback));

        when(retrospectiveService.findRetrospective("Ret_122")).thenReturn(retrospective);

        Feedback modifiedFeedback = new Feedback("John", "Did not reach target", FeedbackType.PRAISE);

        mockMvc.perform(put("/retrospectives/retrospectiveName/updateFeedbackItem")
                        .content(asJsonString(modifiedFeedback))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // Utility method to convert objects to JSON string
    private String asJsonString(Object obj) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

}
