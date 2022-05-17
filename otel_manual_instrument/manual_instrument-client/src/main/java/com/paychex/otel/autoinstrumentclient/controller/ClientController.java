package com.paychex.otel.autoinstrumentclient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paychex.otel.autoinstrumentclient.service.OtelClientService;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;

@RestController
@RequestMapping("/otel/client")
public class ClientController {

	private static int requestCount = 1;

	private OtelClientService otelClientService;
	
	@Autowired
	private Tracer tracer;

	public ClientController(OtelClientService otelClientService) {
		this.otelClientService = otelClientService;
	}

	@GetMapping("/{id}")
	public String get(@PathVariable("id") Integer id) {
		Span span = tracer.spanBuilder("get").startSpan();

		try(Scope scope = span.makeCurrent()) {
			span.addEvent("Client Controller Entered");
			span.setAttribute("clientcontroller.request.count", requestCount++);
			return otelClientService.get(id);
		} catch (Exception e) {
			span.setAttribute("error", true);
			return "ERROR: No value for Id";
		} finally {
			span.addEvent("Exit Controller");
			span.end();
		}
	}

}
