package org.tools.hqlbuilder.test;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.AccessType;

@Entity
@AccessType("field")
public class Rel1Trans {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Rel1Id")
	private Rel1 rel1;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String rel1trans = "rel1trans";
	@Version
	private Integer version;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LangId")
	@NotNull
	private Lang lang;

	public Rel1 getRel1() {
		return rel1;
	}

	public void setRel1(Rel1 rel1) {
		this.rel1 = rel1;
	}

	public Lang getLang() {
		return lang;
	}

	public void setLang(Lang lang) {
		this.lang = lang;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getRel1trans() {
		return rel1trans;
	}

	public void setRel1trans(String rel1trans) {
		this.rel1trans = rel1trans;
	}

	@Override
	public String toString() {
		return rel1trans;
	}
}
