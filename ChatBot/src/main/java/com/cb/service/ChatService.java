package com.cb.service;

import java.util.List;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



@Service
public class ChatService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${gemini.api.key}")
    private String API_KEY;

    
    public String getBotResponse(String userInput) {
    	String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the request body format required by Gemini
        Map<String, Object> messagePart = Map.of("text", userInput);
        Map<String, Object> content = Map.of("parts", List.of(messagePart));
        Map<String, Object> requestBody = Map.of("contents", List.of(content));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            // Get text from response
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.getBody().get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                Map<String, Object> first = (Map<String, Object>) candidates.get(0).get("content");
                List<Map<String, Object>> parts = (List<Map<String, Object>>) first.get("parts");
                return parts.get(0).get("text").toString();
            } else {
                return "No response from Gemini.";
            }
        } catch (Exception e) {
            return "Error talking to Gemini: " + e.getMessage();
        }
    }
}
