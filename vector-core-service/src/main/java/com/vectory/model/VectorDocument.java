package com.vectory.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VectorDocument {
    private String id;
    private String content;
    private List<Double> vector; // The mathematical representation
    private double score; // Used for search results (similarity score)
}