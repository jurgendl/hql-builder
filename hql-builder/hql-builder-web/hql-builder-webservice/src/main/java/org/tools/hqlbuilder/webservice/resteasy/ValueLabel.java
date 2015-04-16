package org.tools.hqlbuilder.webservice.resteasy;

import java.io.Serializable;

public class ValueLabel implements Serializable {
	private static final long serialVersionUID = 3914970740898818440L;

	private String value;

	private String label;

	public ValueLabel() {
		super();
	}

	public ValueLabel(String value) {
		this.value = value;
		this.label = value;
	}

	public ValueLabel(String value, String label) {
		this.value = value;
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}

	public String getValue() {
		return this.value;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setValue(String value) {
		this.value = value;
	}
}