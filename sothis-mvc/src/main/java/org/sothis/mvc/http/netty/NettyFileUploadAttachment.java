package org.sothis.mvc.http.netty;

import java.nio.charset.Charset;

import io.netty.handler.codec.http.multipart.FileUpload;

public class NettyFileUploadAttachment extends NettyAttachment {

	public NettyFileUploadAttachment(FileUpload fileUpload) {
		super(fileUpload);
	}

	@Override
	public String getContentType() {
		return ((FileUpload) this.httpData).getContentType();
	}

	@Override
	public String getFilename() {
		return ((FileUpload) this.httpData).getFilename();
	}

}
