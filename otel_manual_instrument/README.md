# Project implementing manual instrumentation of OpenTelememetry 

This project integrates the following

* [OpenTelememetry](https://opentelemetry.io/)- manual instrumentation of OpenTelememetry Java
* [Jaeger](https://www.jaegertracing.io/)
* [Spring Boot Project](https://spring.io/projects/spring-boot)

# Running

* Execute the following in bash/cmd window 1

````bash
docker run --rm -it --name jaeger-ui -p 16686:16686 -p 14250:14250 jaegertracing/all-in-one:1.9
````

* Execute the following in bash/cmd window 2
````bash
cd <install dir>\otel_manual_instrument\manual_instrument-server
mvn clean install
mvn spring-boot:run
````

* Execute the following in bash/cmd window 3
````bash
cd <install dir>\otel_manual_instrument\manual_instrument-client
mvn clean install
mvn spring-boot:run
````
# Tracing

Access the endpoint http://localhost:8081/otel/client/{integer} a few times with different numbers

Get the trace information Using Jaeger at http://localhost:16686/search
