package org.sothis.web.mvc.views.xml;

import java.util.List;

import org.sothis.web.mvc.views.xml.converter.XmlAttribute;

public class Student {
	@XmlAttribute(value = "·¿¼ÛÍø", isNode = true)
	private String name;
	private Integer age;
	private double score;
	private Infomation info;
	private List<Infomation> infomations;

	public Infomation getInfo() {
		return info;
	}

	public void setInfo(Infomation info) {
		this.info = info;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public List<Infomation> getInfomations() {
		return infomations;
	}

	public void setInfomations(List<Infomation> infomations) {
		this.infomations = infomations;
	}

}
