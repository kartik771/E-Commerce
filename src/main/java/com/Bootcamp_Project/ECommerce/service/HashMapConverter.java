package com.Bootcamp_Project.ECommerce.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.AttributeConverter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HashMapConverter implements AttributeConverter<Map<String , String> , String> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(Map<String, String> customerInfo) {

        try {
            return objectMapper.writeValueAsString(customerInfo);
        } catch (final JsonProcessingException e) {
            throw new IllegalArgumentException("JSON writing error");
        }
    }

    @Override
    public Map<String, String> convertToEntityAttribute(String customerInfoJSON) {

        try {
            return objectMapper.readValue(customerInfoJSON, new TypeReference<HashMap<String, String>>() {});
        } catch (final IOException e) {
            throw new IllegalArgumentException("JSON reading error");
        }
    }
}
