package org.meshmasterserver.system.controller.meshcontroller.http;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.ConnectException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
public class RequestTest {

	private Request request;

	@MockBean
	private HttpClient client;

	@Before
	public void setUp() throws Exception {
		request = new Request();
		request.setClient(client);
	}

	@Test
	public void test_hostConnectionFailed() {

		try {
			when(client.execute(any(HttpUriRequest.class))).thenThrow(new ConnectException());
			request.makeRequest("google.de", Request.MethodType.GET);

			fail();
		} catch (ConnectException e) {
			e.printStackTrace();
			assertTrue(true);
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}
}