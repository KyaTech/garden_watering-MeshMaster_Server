package systemcontroller.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;


public class Config {
	// static variables
	private static final Logger log = LoggerFactory.getLogger(Config.class);
	private static final String defaultConfigFile = "src/main/resources/config.yaml";
	private static String staticConfigFile = "";
	private static Config config;

	// instance variables
	private String configFile = "";
	private JsonNode root;

	private Config(String configFile) {
		this.configFile = configFile;
		try {
			ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
			root = mapper.readTree(new File(this.configFile));
		} catch (IOException e) {
			log.error("Could not read config file", e);
			throw new RuntimeException(e);
		}
	}

	private static void loadConfig() {
		if (config == null || config.hasChanged()) {
			if (staticConfigFile.isEmpty()) {
				staticConfigFile = defaultConfigFile;
			}
			config = new Config(staticConfigFile);
		}
	}

	private boolean hasChanged() {
		return !this.configFile.equals(staticConfigFile);
	}

	public static String getApiHost() {
		loadConfig();
		return config.root.get("meshcontroller").get("api").get("host").asText();
	}

	public static int getApiPort() {
		loadConfig();
		return config.root.get("meshcontroller").get("api").get("port").asInt();
	}

	public static String getApiPrefix() {
		loadConfig();
		return config.root.get("meshcontroller").get("api").get("prefix").asText();
	}

	// For tests only
	static void changeConfigFile(String pathToConfigFile) {
		if (Files.exists(Paths.get(pathToConfigFile))) {
			staticConfigFile = Paths.get(pathToConfigFile).toString();
		} else {
			log.error(String.format("The given configuration file does not exists: \"%s\"", pathToConfigFile));
		}
	}

}
