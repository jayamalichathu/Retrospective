package com.test.retrospective.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Retrospective {
    @Id
    private String name; // Unique identifier
    private String summary;
    private Date date;

    @ElementCollection
    private List<String> participants; // Collection of names
    @OneToMany
    private List<Feedback> feedbackItems; // Collection of feedback items

    public Retrospective(String name, Date date, List<String> participants) {
        this.name = name;
        this.date = date;
        this.participants = participants;
    }

}
