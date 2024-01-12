package com.test.retrospective.services;

import com.test.retrospective.model.Retrospective;

import java.util.List;

public record PageData(int totalPages, long totalElements,  int pageNumber, int size, List<Retrospective> retrospectiveList) {
}
