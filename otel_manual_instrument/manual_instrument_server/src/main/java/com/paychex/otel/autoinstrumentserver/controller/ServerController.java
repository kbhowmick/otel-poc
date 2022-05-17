package com.paychex.otel.autoinstrumentserver.controller;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paychex.otel.autoinstrumentserver.service.OtelServerService;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.semconv.trace.attributes.SemanticAttributes;

@RestController
@RequestMapping("/otel/server")
public class ServerController {

	private static final TextMapGetter<HttpServletRequest> tmGetter = new TextMapGetter<HttpServletRequest>() {

		@Override
		public Iterable<String> keys(HttpServletRequest carrier) {
			return Collections.list(carrier.getHeaderNames());
		}

		@Override
		public String get(HttpServletRequest carrier, String key) {
			if (carrier.getHeaders(key).hasMoreElements()) {
				return carrier.getHeader(key);
			}
			return null;
		}
	};
	
	private OtelServerService otelserverService;
	
	@Autowired
	private Tracer tracer;	

	public ServerController(OtelServerService otelserverService) {
		this.otelserverService = otelserverService;
	}

	@GetMapping("/{id}")
	public String get(@PathVariable("id") Integer id, HttpServletRequest request) {
		TextMapPropagator propagator = GlobalOpenTelemetry.get().getPropagators().getTextMapPropagator();
		Context extractedContext = propagator.extract(Context.current(), request, tmGetter);
		
		try(Scope scope = extractedContext.makeCurrent()) {
		    Span serverSpan = tracer.spanBuilder("GET /id")
		            .setSpanKind(SpanKind.SERVER)
		            .startSpan();
		    try {
		        // Add the attributes defined in the Semantic Conventions
		        serverSpan.setAttribute(SemanticAttributes.HTTP_METHOD, "GET");
		        serverSpan.setAttribute(SemanticAttributes.HTTP_SCHEME, "http");
		        serverSpan.setAttribute(SemanticAttributes.HTTP_HOST, "localhost:8080");
		        serverSpan.setAttribute(SemanticAttributes.HTTP_TARGET, "/id");
		        // Serve the request
		        String svcRetVal = otelserverService.get(id);
		        serverSpan.setAttribute("external svc return val", svcRetVal);
				return svcRetVal;	
		      } finally {
		        serverSpan.end();
		      }
		} 
	}
}
