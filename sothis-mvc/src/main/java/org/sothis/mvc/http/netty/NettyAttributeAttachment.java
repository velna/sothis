package org.sothis.mvc.http.netty;

import io.netty.handler.codec.http.multipart.Attribute;

public class NettyAttributeAttachment extends NettyAttachment {

	public NettyAttributeAttachment(Attribute attribute) {
		super(attribute);
	}

	@Override
	public String getContentType() {
		return null;
	}

	@Override
	public String getFilename() {
		return null;
	}

}
