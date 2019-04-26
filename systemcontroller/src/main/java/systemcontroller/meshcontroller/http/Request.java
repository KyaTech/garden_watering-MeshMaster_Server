package systemcontroller.meshcontroller.http;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Request {
	private static final Logger log = LoggerFactory.getLogger(Request.class);

	public static HttpResponse makeRequest(String url,MethodType method) throws IOException {

		try  {
			HttpClient client = HttpClientBuilder.create().build();

			HttpUriRequest request;
			if (method == MethodType.POST) {
				request = new HttpPost(url);
			} else {
				request = new HttpGet(url);
			}
			request.setHeader("Accept", "application/json");

			return client.execute(request);
		} catch (IOException e) {
			log.error("Exception while sending request",e);
			throw e;
		}
	}

	public static HttpResponse makeRequest(String url) throws IOException {
		return makeRequest(url, MethodType.GET);
	}

	public enum MethodType {
		GET,
		POST
	}

}
