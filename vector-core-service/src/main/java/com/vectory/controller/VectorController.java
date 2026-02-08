package com.vectory.controller;

import com.vectory.model.VectorDocument;
import com.vectory.service.VectorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class VectorController {

    private final VectorService vectorService;

    public VectorController(VectorService vectorService) {
        this.vectorService = vectorService;
    }

    @PostMapping("/add")
    public String addDocument(@RequestBody Map<String, String> payload) {
        vectorService.addDocument(payload.get("id"), payload.get("text"));
        return "Document indexed successfully.";
    }

    @GetMapping("/search")
    public List<VectorDocument> search(@RequestParam String query) {
        return vectorService.search(query, 3); // Return top 3 matches
    }
}