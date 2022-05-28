package com.kbhowmick.otel.instrumentation.manual.client.service;

import java.io.IOException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapSetter;
import io.opentelemetry.semconv.trace.attributes.SemanticAttributes;

@Service
public class OtelClientService {
	
	private static final String SERVER_URL = "http://localhost:8082/otel/server/";
	
	private static final TextMapSetter<ClientHttpRequest> tmSetter = 
			new TextMapSetter<ClientHttpRequest>() {

				@Override
				public void set(ClientHttpRequest carrier, String key, String value) {
					carrier.getHeaders().add(key, value);
				}
	};
	
	@Autowired
	private Tracer tracer;	

	public String get(Integer id) throws Exception {
		String url = SERVER_URL + id;
		URL outgoingURL = new URL(url);
		Span outGoingSpan = tracer.spanBuilder(outgoingURL.getPath())
				.setSpanKind(SpanKind.CLIENT)
				.startSpan();
		
		try (Scope scope = outGoingSpan.makeCurrent()) {
			outGoingSpan.setAttribute(SemanticAttributes.HTTP_METHOD, "GET");
			outGoingSpan.setAttribute(SemanticAttributes.HTTP_URL, outgoingURL.toString());
			
			RestTemplate restTemplate = new RestTemplate();
			RequestCallback callback = new HeaderInjectionCallback();
			ResponseExtractor<ResponseEntity<String>> extractor = restTemplate.responseEntityExtractor(String.class);
			ResponseEntity<String> response = restTemplate.execute(url, HttpMethod.GET, callback, extractor);
			
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			outGoingSpan.end();
		}
	}
	
	class HeaderInjectionCallback implements RequestCallback {

		@Override
		public void doWithRequest(ClientHttpRequest request) throws IOException {
			GlobalOpenTelemetry.get().getPropagators().getTextMapPropagator().inject(Context.current(), request, tmSetter);
		}
		
	}
		
}
