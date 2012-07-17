package org.sothis.web.mvc.views.xml.converter;

import java.util.List;

public class XmlNode {
	private String node;
	private String value;
	private List<Attribute> attributes;

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String toString(XmlNode node) {
		return null;
	}

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		StringBuilder ret = new StringBuilder();
		ret.append("<").append(node);
//		if (CollectionUtils.isNotEmpty(attributes)) {
//			for (Attribute attr : attributes) {
//				ret.append(" ").append(attr.name).append("=\"").append(attr.value).append("\"");
//			}
//		}
		ret.append(">");
		ret.append(value);
		ret.append("</").append(node).append(">");
		return ret.toString();
	}

}
