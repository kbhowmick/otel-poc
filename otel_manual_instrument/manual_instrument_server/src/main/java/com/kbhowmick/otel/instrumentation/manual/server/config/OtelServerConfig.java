package com.kbhowmick.otel.instrumentation.manual.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Tracer;

@Configuration
public class OtelServerConfig{
	
	private static final String tracerName = "autoInstrumentServerTracer";

//	@Bean
//	public WebClient webClient() {
//		return WebClient.create();
//	}
	
	@Bean
	public Tracer otelTracer() {
		return GlobalOpenTelemetry.getTracer(tracerName);
	}
	
}
