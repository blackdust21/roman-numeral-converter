package com.adobe.adobetest.controller;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanBuilder;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RomanNumeralControllerTest {

    private RomanNumeralController controller;

    @BeforeEach
    void setUp() {
        // Simple in-memory metrics registry
        SimpleMeterRegistry meterRegistry = new SimpleMeterRegistry();

        // Mock OpenTelemetry + trace flow
        OpenTelemetry openTelemetry = mock(OpenTelemetry.class);
        Tracer tracer = mock(Tracer.class);
        SpanBuilder spanBuilder = mock(SpanBuilder.class);
        Span span = mock(Span.class);
        Scope scope = mock(Scope.class);

        when(openTelemetry.getTracer(anyString())).thenReturn(tracer);
        when(tracer.spanBuilder(anyString())).thenReturn(spanBuilder);
        when(spanBuilder.startSpan()).thenReturn(span);
        when(span.makeCurrent()).thenReturn(scope);

        controller = new RomanNumeralController(meterRegistry, openTelemetry);
    }

    @Test
    void testConvertToRoman_ValidNumbers() {
        assertEquals("I", controller.convertToRoman("1").get("output"));
        assertEquals("IV", controller.convertToRoman("4").get("output"));
        assertEquals("X", controller.convertToRoman("10").get("output"));
        assertEquals("XL", controller.convertToRoman("40").get("output"));
        assertEquals("C", controller.convertToRoman("100").get("output"));
        assertEquals("MMMCMXCIX", controller.convertToRoman("3999").get("output"));
    }

    @Test
    void testConvertToRoman_InvalidNumbers() {
        Exception e1 = assertThrows(IllegalArgumentException.class, () -> controller.convertToRoman("0"));
        assertEquals("Number must be between 1 and 3999.", e1.getMessage());

        Exception e2 = assertThrows(IllegalArgumentException.class, () -> controller.convertToRoman("4000"));
        assertEquals("Number must be between 1 and 3999.", e2.getMessage());

        Exception e3 = assertThrows(IllegalArgumentException.class, () -> controller.convertToRoman("-5"));
        assertEquals("Invalid input: must be a number between 1 and 3999.", e3.getMessage());

        Exception e4 = assertThrows(IllegalArgumentException.class, () -> controller.convertToRoman("abc"));
        assertEquals("Invalid input: must be a number between 1 and 3999.", e4.getMessage());
    }
}
