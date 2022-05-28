package com.kbhowmick.otel.instrumentation.manual.client.util;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapSetter;
import io.opentelemetry.semconv.trace.attributes.SemanticAttributes;

@Component
public class HttpUtils {	

//	private static final TextMapSetter<HttpURLConnection> tmSetter = 
//			new TextMapSetter<HttpURLConnection>() {
//
//				@Override
//				public void set(HttpURLConnection carrier, String key, String value) {
//					// TODO Auto-generated method stub
//					carrier.setRequestProperty(key, value);
//				}
//	};
//	
//	@Autowired
//	private Tracer tracer;
//	
//	@Autowired
//	private WebClient webClient;
//
//	public Mono<String> callEndpoint(String url) throws Exception {
//		URL outgoingURL = new URL(url);
//		System.out.println(outgoingURL.getPath());
//		Span outGoingSpan = tracer.spanBuilder(outgoingURL.getPath())
//				.setSpanKind(SpanKind.CLIENT)
//				.startSpan();
//		
//		try (Scope scope = outGoingSpan.makeCurrent()) {
//			outGoingSpan.setAttribute(SemanticAttributes.HTTP_METHOD, "GET");
//			outGoingSpan.setAttribute(SemanticAttributes.HTTP_URL, outgoingURL.toString());
//			HttpURLConnection transportLayer = (HttpURLConnection) outgoingURL.openConnection();
//			GlobalOpenTelemetry.get().getPropagators().getTextMapPropagator().inject(Context.current(), transportLayer, tmSetter);
//			return webClient.get()
//					.uri(url)
//					.retrieve()
//					.bodyToMono(String.class);
//
//		} finally {
//			outGoingSpan.end();
//		}
//	}
}
