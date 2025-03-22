package com.adobe.adobetest.controller;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/romannumeral")
@CrossOrigin(origins = "http://localhost:3000")
public class RomanNumeralController {

    private static final Logger logger = LoggerFactory.getLogger(RomanNumeralController.class);
    private final MeterRegistry meterRegistry;
    private final Tracer tracer;

    public RomanNumeralController(MeterRegistry meterRegistry, OpenTelemetry openTelemetry) {
        this.meterRegistry = meterRegistry;
        this.tracer = openTelemetry.getTracer("roman-numeral-converter");
    }

    @GetMapping
    public Map<String, String> convertToRoman(@RequestParam("query") String input) {
        logger.info("Received request to convert: {}", input);

        // Start metrics timer
        Timer.Sample sample = Timer.start(meterRegistry);

        // Start OpenTelemetry trace span
        Span span = tracer.spanBuilder("convertToRoman").startSpan();
        try (Scope scope = span.makeCurrent()) {

            // Validate input is numeric
            if (!input.matches("\\d+")) {
                logger.warn("Invalid input format: {}", input);
                span.setStatus(StatusCode.ERROR, "Input is not numeric");
                throw new IllegalArgumentException("Invalid input: must be a number between 1 and 3999.");
            }

            int number = Integer.parseInt(input);

            // Validate range
            if (number < 1 || number > 3999) {
                logger.warn("Invalid number received: {}", number);
                span.setStatus(StatusCode.ERROR, "Input out of range");
                throw new IllegalArgumentException("Number must be between 1 and 3999.");
            }

            String romanNumeral = convert(number);
            logger.info("Successfully converted {} -> {}", number, romanNumeral);

            meterRegistry.counter("api.requests.success").increment();
            sample.stop(meterRegistry.timer("api.request.duration"));

            Map<String, String> response = new LinkedHashMap<>();
            response.put("input", input);
            response.put("output", romanNumeral);
            return response;

        } catch (IllegalArgumentException ex) {
            logger.error("Conversion failed due to invalid input: {}", ex.getMessage());
            span.recordException(ex);
            span.setStatus(StatusCode.ERROR);
            throw ex;
        } catch (Exception ex) {
            logger.error("Unexpected error occurred: {}", ex.getMessage(), ex);
            span.recordException(ex);
            span.setStatus(StatusCode.ERROR);
            throw new RuntimeException("Internal server error");
        } finally {
            span.end();
        }
    }

    private String convert(int num) {
        Map<Integer, String> romanMap = new LinkedHashMap<>();
        romanMap.put(1000, "M");
        romanMap.put(900, "CM");
        romanMap.put(500, "D");
        romanMap.put(400, "CD");
        romanMap.put(100, "C");
        romanMap.put(90, "XC");
        romanMap.put(50, "L");
        romanMap.put(40, "XL");
        romanMap.put(10, "X");
        romanMap.put(9, "IX");
        romanMap.put(5, "V");
        romanMap.put(4, "IV");
        romanMap.put(1, "I");

        StringBuilder result = new StringBuilder();
        for (Map.Entry<Integer, String> entry : romanMap.entrySet()) {
            while (num >= entry.getKey()) {
                result.append(entry.getValue());
                num -= entry.getKey();
            }
        }
        return result.toString();
    }
}
