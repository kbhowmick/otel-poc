package com.paychex.otel.autoinstrumentclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Tracer;

@Configuration
public class OtelClientConfig {
	
	private static final String tracerName = "autoInstrumentClientTracer"; 

//	@Bean
//	public WebClient webClient() {
//		return WebClient.create();
//	}
	
	@Bean
	public Tracer otelTracer() {
		return GlobalOpenTelemetry.getTracer(tracerName);
	}
	
}
