package com.vectory.service;

import com.vectory.model.VectorDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class VectorService {

    private final RestTemplate restTemplate;
    // In-memory storage (simulating a DB)
    private final Map<String, VectorDocument> vectorStore = new ConcurrentHashMap<>();

    @Value("${embedding.service.url}")
    private String embeddingUrl;

    public VectorService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // 1. ADD DOCUMENT: Send text to Python -> Get Vector -> Store it
    public void addDocument(String id, String content) {
        List<Double> vector = getEmbeddingFromPython(content);
        VectorDocument doc = new VectorDocument(id, content, vector, 0.0);
        vectorStore.put(id, doc);
        System.out.println("✅ Stored document: " + id);
    }

    // 2. SEARCH: Get Query Vector -> Compare with all docs -> Return top K
    public List<VectorDocument> search(String query, int k) {
        List<Double> queryVector = getEmbeddingFromPython(query);
        
        return vectorStore.values().stream()
                .peek(doc -> doc.setScore(cosineSimilarity(queryVector, doc.getVector())))
                .sorted((d1, d2) -> Double.compare(d2.getScore(), d1.getScore())) // Sort descending
                .limit(k)
                .collect(Collectors.toList());
    }

    // Helper: Call the Python Microservice
    private List<Double> getEmbeddingFromPython(String text) {
        // Request body matches Python's expected input
        Map<String, String> request = Map.of("text", text);
        
        // Response body matches Python's output
        Map response = restTemplate.postForObject(embeddingUrl, request, Map.class);
        return (List<Double>) response.get("vector");
    }

    // Helper: The Math (Cosine Similarity)
    private double cosineSimilarity(List<Double> v1, List<Double> v2) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < v1.size(); i++) {
            dotProduct += v1.get(i) * v2.get(i);
            normA += Math.pow(v1.get(i), 2);
            normB += Math.pow(v2.get(i), 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}