package io.bega.servicebase.environment;

public class DevelopmentEnvironment extends Environment {

	public DevelopmentEnvironment() {
		super("Development");
	}

	@Override
	public String getMixpanelToken() {
		return "900a78566cba31a0dad6b66bb90b36c7";
	}

	@Override
	public String getApiHost() {
		return "http://192.168.1.50:3000/";
	}
}
