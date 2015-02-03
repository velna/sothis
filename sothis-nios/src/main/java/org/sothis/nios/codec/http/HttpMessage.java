package org.sothis.nios.codec.http;

public interface HttpMessage {

	HttpVersion version();

	HttpHeaders headers();
}
