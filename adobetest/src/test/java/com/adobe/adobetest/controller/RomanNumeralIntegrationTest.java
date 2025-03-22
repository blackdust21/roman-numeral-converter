package com.adobe.adobetest.controller;

import com.adobe.adobetest.AdobetestApplication;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = AdobetestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RomanNumeralIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(RomanNumeralIntegrationTest.class);

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    void testApiSuccess() {
        String url = "http://localhost:" + port + "/romannumeral?query=25";
        logger.info("Testing URL: {}", url);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        logger.info("Response Code: {}", response.getStatusCodeValue());
        logger.info("Response Body: {}", response.getBody());

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("\"output\":\"XXV\""));
    }

    @Test
    void testApiFailure() {
        String url = "http://localhost:" + port + "/romannumeral?query=5000";
        logger.info("Testing invalid input at URL: {}", url);

        try {
            restTemplate.getForEntity(url, String.class);
            fail("Expected HttpClientErrorException.BadRequest but no exception was thrown.");
        } catch (org.springframework.web.client.HttpClientErrorException.BadRequest ex) {
            logger.warn("Received expected 400 Bad Request response");
            assertEquals(400, ex.getStatusCode().value());
            assertTrue(ex.getResponseBodyAsString().contains("Number must be between 1 and 3999."));
        }
    }
}
