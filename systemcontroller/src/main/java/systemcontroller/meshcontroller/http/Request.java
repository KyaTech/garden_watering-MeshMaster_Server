package systemcontroller.meshcontroller.http;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Request {
	private static Logger log = LoggerFactory.getLogger(Request.class);

	public static HttpResponse makeRequest(String url) throws IOException {

		try  {
			HttpClient client = HttpClientBuilder.create().build();
			HttpResponse response = client.execute(new HttpGet(url));
			return response;
		} catch (IOException e) {
			log.error("Exception while sending request",e);
			throw e;
		}
	}

}
