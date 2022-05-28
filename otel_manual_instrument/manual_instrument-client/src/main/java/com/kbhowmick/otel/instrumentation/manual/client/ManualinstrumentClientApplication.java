package com.kbhowmick.otel.instrumentation.manual.client;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.jaeger.JaegerGrpcSpanExporter;
import io.opentelemetry.exporters.logging.LoggingSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.semconv.resource.attributes.ResourceAttributes;

@SpringBootApplication
public class ManualinstrumentClientApplication {
	
	static {
		SpanProcessor logBatchProcessor = BatchSpanProcessor.builder(new LoggingSpanExporter()).build();
		
		ManagedChannel jaegerChannel = ManagedChannelBuilder.forAddress("localhost", 14250)
				.usePlaintext()
				.build();
		JaegerGrpcSpanExporter jaegerExporter = JaegerGrpcSpanExporter.builder()
				.setChannel(jaegerChannel)
				.setTimeout(30, TimeUnit.SECONDS)
				.build();
		SpanProcessor jaegerProcessor = BatchSpanProcessor.builder(jaegerExporter).build();
		
		Resource resource =
		        Resource.create(Attributes.of(ResourceAttributes.SERVICE_NAME, "otel-poc-client"));
	
		SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
				.addSpanProcessor(logBatchProcessor)
				.addSpanProcessor(jaegerProcessor)
				.setResource(Resource.getDefault().merge(resource))
				.build();
		
		@SuppressWarnings("unused")
		OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
				.setTracerProvider(sdkTracerProvider)
				.setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
				.buildAndRegisterGlobal();
		
		Runtime.getRuntime().addShutdownHook(new Thread(sdkTracerProvider::close));
	}

	public static void main(String[] args) {
		SpringApplication.run(ManualinstrumentClientApplication.class, args);
	}

}
