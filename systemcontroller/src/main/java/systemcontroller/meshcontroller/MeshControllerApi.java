package systemcontroller.meshcontroller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpResponse;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import systemcontroller.meshcontroller.http.Request;

public class MeshControllerApi implements MeshController {

	private static final Logger log = LoggerFactory.getLogger(Request.class);
	private static final String host = "192.168.176.6:8080";
	private static final String api = "/api/v1";


	@Override
	public ValveState requestState(int node, int index) throws InvalidNodeException, InvalidIndexException {
		JsonNode state = makeApiRequest("state", "/nodes/%d/valves/%d", node, index);
		if (state != null) {
			return ValveState.from(state.asText());
		}

		return null;
	}

	@Override
	public double requestSensor(int node) throws InvalidNodeException {
		JsonNode value = makeApiRequest("value", "/nodes/%d/sensors", node);
		if (value != null) {
			return value.asDouble();
		}

		return 0;
	}

	@Override
	public double requestSensor(int node, int index) throws InvalidNodeException, InvalidIndexException {
		JsonNode value = makeApiRequest("value", "/nodes/%d/sensors/%d", node, index);
		if (value != null) {
			return value.asDouble();
		}

		return 0;
	}

	@Override
	public int requestBattery(int node) throws InvalidNodeException {
		JsonNode value = makeApiRequest("battery", "/nodes/%d/battery", node);
		if (value != null) {
			return value.asInt();
		}

		return 0;
	}

	@Override
	public CommandStatus changeValveState(int node, int index, ValveState state) throws InvalidNodeException, InvalidIndexException {
		JsonNode message = makeApiRequest(Request.MethodType.POST,"message", "/nodes/%d/valves/%d/%s", node,index,state.toString());
		if (message != null) {
			return CommandStatus.from(message.asText());
		}

		return null;
	}

	@Override
	public CommandStatus turnOnValve(int node, int index) throws InvalidNodeException, InvalidIndexException {
		return changeValveState(node, index, ValveState.ON);
	}

	@Override
	public CommandStatus turnOffValve(int node, int index) throws InvalidNodeException, InvalidIndexException {
		return changeValveState(node, index, ValveState.OFF);
	}

	static String makeRequestUri(String endpointPath) throws URISyntaxException {
		URI uri = new URIBuilder()
			.setScheme("http")
			.setHost(host)
			.setPath(String.format("%s%s", api, endpointPath))
			.build();
		log.debug(uri.toString());
		return uri.toString();
	}

	JsonNode entityToJson(HttpResponse response) throws IOException {
		String msg = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
		log.debug(msg);

		ObjectMapper mapper = new ObjectMapper();
		return mapper.readTree(msg);
	}

	void throwExceptionIfNecessary(JsonNode root) {
		if (root.has("error")) {
			JsonNode error = root.get("error");
			String type = error.get("type").asText();

			if ("InvalidNode".equals(type)) {
				throw new InvalidNodeException();
			} else if ("InvalidIndex".equals(type.trim())) {
				throw new InvalidIndexException();
			}

		}
	}

	JsonNode makeApiRequest(String jsonIdentifier, String uriTemplate, Object... templateArgs) {
		return makeApiRequest(Request.MethodType.GET, jsonIdentifier, uriTemplate, templateArgs);
	}

	JsonNode makeApiRequest(Request.MethodType method, String jsonIdentifier, String uriTemplate, Object... templateArgs) {

		try {
			String endpointPath = String.format(uriTemplate, templateArgs);
			HttpResponse response = Request.makeRequest(makeRequestUri(endpointPath), method);

			if (response != null && response.getEntity() != null) {
				JsonNode root = entityToJson(response);
				throwExceptionIfNecessary(root);


				if (root.has(jsonIdentifier)) {
					return root.get(jsonIdentifier);
				}

			} else {
				log.debug("Didn't get any message");
			}
		} catch (URISyntaxException e) {
			log.error("Exception while resolving URI", e);
		} catch (IOException e) {
			log.error("Exception while sending or parsing", e);
		}
		return null;
	}

}
