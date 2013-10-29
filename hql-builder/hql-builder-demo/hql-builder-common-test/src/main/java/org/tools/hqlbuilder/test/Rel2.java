package org.tools.hqlbuilder.test;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import org.hibernate.annotations.AccessType;

@Entity
@AccessType("field")
public class Rel2 {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "Rel1Id")
	private Rel1 rel1;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String rel2 = "rel2";
	@Version
	private Integer version;

	@OneToMany(mappedBy = "rel2")
	private Set<Rel2Trans> trans = new HashSet<Rel2Trans>();

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

	public Set<Rel2Trans> getTrans() {
		return trans;
	}

	public void setTrans(Set<Rel2Trans> trans) {
		this.trans = trans;
	}

	public Rel1 getRel1() {
		return rel1;
	}

	public void setRel1(Rel1 rel1) {
		this.rel1 = rel1;
	}

	public String getRel2() {
		return rel2;
	}

	public void setRel2(String rel2) {
		this.rel2 = rel2;
	}

	@Override
	public String toString() {
		return rel2;
	}
}
