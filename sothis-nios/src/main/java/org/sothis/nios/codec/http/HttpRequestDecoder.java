package org.sothis.nios.codec.http;

import java.util.List;

import org.sothis.nios.ChannelContext;
import org.sothis.nios.ReadBuffer;
import org.sothis.nios.codec.DecodeException;
import org.sothis.nios.codec.Decoder;

public class HttpRequestDecoder extends Decoder {

	private final static int MAX_LINE_LENGTH = 4096;

	private LineReader lineReader = new LineReader();
	private HttpRequest request;
	private DecodeStatus status = DecodeStatus.REQUEST_LINE;

	private static enum DecodeStatus {
		REQUEST_LINE, HEADERS
	}

	@Override
	protected boolean decode(ChannelContext ctx, ReadBuffer in, List<Object> out) {
		String line;
		if (status == DecodeStatus.REQUEST_LINE) {
			line = lineReader.readLine(in);
			if (lineReader.size() > MAX_LINE_LENGTH) {
				throw new DecodeException("reqeust line too long or may be not a valid http reqeust");
			}
			if (null == line) {
				return false;
			}
			if (line.length() > MAX_LINE_LENGTH) {
				throw new DecodeException("reqeust line too long");
			}
			String[] split = line.split(" ");
			if (split.length != 3) {
				throw new DecodeException("invalid request line");
			}
			request = new DefaultHttpRequest(HttpMethod.valueOf(split[0]), split[1], HttpVersion.valueOf(split[2]));
			status = DecodeStatus.HEADERS;
		}
		if (status == DecodeStatus.HEADERS) {
			while ((line = lineReader.readLine(in)) != null) {
				if (line.length() > MAX_LINE_LENGTH) {
					throw new DecodeException("header line too long");
				}
				if (line == LineReader.EMPTY_LINE) {
					out.add(request);
					status = DecodeStatus.REQUEST_LINE;
				} else {
					String[] split = line.split(",", 2);
					if (split.length != 2) {
						throw new DecodeException("invalid header: " + line);
					}
					request.headers().add(split[0].trim(), split[1].trim());
				}
			}
			if (lineReader.size() > MAX_LINE_LENGTH) {
				throw new DecodeException("header line too long");
			}
		}
		return false;
	}
}
