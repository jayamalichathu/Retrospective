//package com.test.retrospective.services;
//
//import com.test.retrospective.model.Retrospective;
//import org.springframework.stereotype.Repository;
//
//import java.util.HashMap;
//import java.util.Map;
//@Repository
//public class InMemoryRetrospectiveRepository implements RetrospectiveRepository {
//    private final Map<String, Retrospective> retrospectiveMap;
//
//    InMemoryRetrospectiveRepository() {
//        retrospectiveMap = new HashMap<>();
//    }
//
//    @Override
//    public Retrospective save(Retrospective retrospective) {
//        retrospectiveMap.put(retrospective.getName(), retrospective);
//        return retrospective;
//    }
//
//    @Override
//    public Retrospective findById(String name) {
//        return retrospectiveMap.get(name);
//    }
//
//}
