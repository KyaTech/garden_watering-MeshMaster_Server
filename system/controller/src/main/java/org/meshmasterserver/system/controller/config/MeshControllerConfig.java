package org.meshmasterserver.system.controller.config;


import org.springframework.boot.context.properties.NestedConfigurationProperty;

public class MeshControllerConfig {

	@NestedConfigurationProperty
	private ApiConfig api;

	public ApiConfig getApi() {
		return api;
	}

	public void setApi(ApiConfig api) {
		this.api = api;
	}
}
