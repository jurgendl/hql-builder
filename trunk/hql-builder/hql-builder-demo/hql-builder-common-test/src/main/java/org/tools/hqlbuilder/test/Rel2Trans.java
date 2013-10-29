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
public class Rel2Trans {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Rel2Id")
	private Rel2 rel2;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String rel2trans = "rel2trans";
	@Version
	private Integer version;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LangId")
	@NotNull
	private Lang lang;

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

	public Rel2 getRel2() {
		return rel2;
	}

	public void setRel2(Rel2 rel2) {
		this.rel2 = rel2;
	}

	public String getRel2trans() {
		return rel2trans;
	}

	public void setRel2trans(String rel2trans) {
		this.rel2trans = rel2trans;
	}

	@Override
	public String toString() {
		return rel2trans;
	}
}
