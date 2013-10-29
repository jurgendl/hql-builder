package org.tools.hqlbuilder.test;

import javax.persistence.Embeddable;
import javax.validation.constraints.Min;

@Embeddable
public class EmbedPojo {
	@Min(100)
	private Integer from100;

	public Integer getFrom100() {
		return from100;
	}

	public void setFrom100(Integer from100) {
		this.from100 = from100;
	}

	@Override
	public String toString() {
		return "EmbedPojo [from100=" + getFrom100() + "]";
	}

}
