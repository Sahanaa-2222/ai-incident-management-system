package com.finshield.incident_management.service;

import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class AIScoringService {
    
    private Map<String, Integer> keywordScores = new HashMap<>();
    
    public AIScoringService() {
        keywordScores.put("server crash", 50);
        keywordScores.put("database", 40);
        keywordScores.put("down", 45);
        keywordScores.put("slow", 20);
        keywordScores.put("login", 30);
        keywordScores.put("security", 45);
        keywordScores.put("data loss", 60);
        keywordScores.put("payment", 55);
    }
    
    public int calculateScore(String title, String description) {
        String text = (title + " " + description).toLowerCase();
        int totalScore = 0;
        
        for (Map.Entry<String, Integer> entry : keywordScores.entrySet()) {
            if (text.contains(entry.getKey())) {
                totalScore += entry.getValue();
            }
        }
        
        return Math.min(totalScore, 100);
    }
    
    public String getSeverityFromScore(int score) {
        if (score >= 70) return "CRITICAL";
        if (score >= 40) return "HIGH";
        if (score >= 20) return "MEDIUM";
        return "LOW";
    }
}