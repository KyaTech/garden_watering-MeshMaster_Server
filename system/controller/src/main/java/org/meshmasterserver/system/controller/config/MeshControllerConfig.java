package org.meshmasterserver.system.controller.config;


import org.springframework.boot.context.properties.NestedConfigurationProperty;

public class MeshControllerConfig {

	@NestedConfigurationProperty
	private ApiConfig api;
	private int retries;

	public ApiConfig getApi() {
		return api;
	}

	public void setApi(ApiConfig api) {
		this.api = api;
	}


	public int getRetries() {
		return retries;
	}

	public void setRetries(int retries) {
		this.retries = retries;
	}
}
