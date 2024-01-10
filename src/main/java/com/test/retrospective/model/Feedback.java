package com.test.retrospective.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String feedbackProviderName;
    private String body;
    @Enumerated
    private FeedbackType feedbackType;
    @ManyToOne
    @JoinColumn(name = "name")
    private Retrospective retrospective;

    public Feedback(String feedbackProviderName, String body, FeedbackType feedbackType) {
        this.feedbackProviderName = feedbackProviderName;
        this.body = body;
        this.feedbackType = feedbackType;
    }

}

