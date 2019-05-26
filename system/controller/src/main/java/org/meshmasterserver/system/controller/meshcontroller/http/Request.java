package org.meshmasterserver.system.controller.meshcontroller.http;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class Request {
	private static final Logger log = LoggerFactory.getLogger(Request.class);

	private HttpClient client;

	public HttpResponse makeRequest(String url, MethodType methodType) throws IOException {

		HttpUriRequest request;
		if (methodType == MethodType.POST) {
			request = new HttpPost(url);
		} else {
			request = new HttpGet(url);
		}
		request.setHeader("Accept", "application/json");

		return client.execute(request);
	}

	@Autowired
	public void setClient(HttpClient client) {
		this.client = client;
	}

	@Bean
	public HttpClient createHttpClient() {
		return HttpClientBuilder.create().build();
	}

	public enum MethodType {
		GET,
		POST
	}

}
