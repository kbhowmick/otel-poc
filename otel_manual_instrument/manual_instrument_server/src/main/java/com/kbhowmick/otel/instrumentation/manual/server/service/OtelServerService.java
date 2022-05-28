package com.kbhowmick.otel.instrumentation.manual.server.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class OtelServerService {

	private static final String SERVER_URL = "http://numbersapi.com/";

	public String get(Integer id) {
		String url = SERVER_URL + id;
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		
		return response.getBody();
//		return webClient.get()
//				.uri("http://numbersapi.com/" + id)
//				.retrieve()
//				.bodyToMono(String.class);
	}

}
