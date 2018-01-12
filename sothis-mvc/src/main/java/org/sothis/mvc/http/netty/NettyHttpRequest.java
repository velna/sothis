package org.sothis.mvc.http.netty;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.sothis.mvc.Attachment;
import org.sothis.mvc.Attachments;
import org.sothis.mvc.Attributes;
import org.sothis.mvc.HashMapAttachments;
import org.sothis.mvc.HashMapAttributes;
import org.sothis.mvc.HashMapParameters;
import org.sothis.mvc.Headers;
import org.sothis.mvc.Parameters;
import org.sothis.mvc.Request;
import org.sothis.mvc.RequestParseExecption;
import org.sothis.mvc.Session;
import org.sothis.mvc.UriParser;
import org.sothis.mvc.http.HttpConstants;

import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostMultipartRequestDecoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.HttpPostStandardRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder;
import io.netty.handler.ssl.SslHandler;

public class NettyHttpRequest implements Request {

	private final FullHttpRequest request;
	private final Channel channel;
	private InputStream inputStream;
	private Headers headers;
	private String scheme;
	private HttpDataFactory httpDataFactory;
	private Attributes attributes;
	private Charset charset;
	private UriParser uriParser;
	private Parameters parameters;
	private Attachments attachments;

	public NettyHttpRequest(FullHttpRequest request, Channel channel) {
		super();
		this.request = request;
		this.channel = channel;
	}

	protected UriParser getUriParser() {
		if (null == uriParser) {
			uriParser = new UriParser(getUri(), getCharset());
		}
		return uriParser;
	}

	@Override
	public Attributes attributes() {
		if (null == attributes) {
			attributes = new HashMapAttributes();
		}
		return attributes;
	}

	@Override
	public Charset getCharset() {
		if (null == charset) {
			charset = DEFAULT_CHARSET;
		}
		return charset;
	}

	@Override
	public void setCharset(Charset charset) throws UnsupportedEncodingException {
		this.charset = charset;
	}

	@Override
	public String getQueryString() {
		return getUriParser().queryString();
	}

	@Override
	public String getUriPath() {
		return getUriParser().path();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		if (null == inputStream) {
			inputStream = new ByteBufInputStream(request.content());
		}
		return inputStream;
	}

	@Override
	public Session getSession() {
		return getSession(true);
	}

	@Override
	public Session getSession(boolean create) {
		if (create) {
			throw new UnsupportedOperationException();
		} else {
			return null;
		}
	}

	@Override
	public String getMethod() {
		return request.getMethod().name();
	}

	@Override
	public String getProtocol() {
		return request.getProtocolVersion().text();
	}

	@Override
	public String getUri() {
		return request.getUri();
	}

	@Override
	public Headers headers() {
		if (null == headers) {
			headers = new NettyHttpHeaders(request.headers());
		}
		return headers;
	}

	@Override
	public String getLocalAddr() {
		return ((InetSocketAddress) channel.localAddress()).getHostString();
	}

	@Override
	public int getLocalPort() {
		return ((InetSocketAddress) channel.localAddress()).getPort();
	}

	@Override
	public String getRemoteAddr() {
		return ((InetSocketAddress) channel.remoteAddress()).getHostString();
	}

	@Override
	public int getRemotePort() {
		return ((InetSocketAddress) channel.remoteAddress()).getPort();
	}

	@Override
	public String getScheme() {
		if (null == scheme) {
			scheme = channel.pipeline().get(SslHandler.class) == null ? HttpConstants.Schemes.HTTP
					: HttpConstants.Schemes.HTTPS;
		}
		return scheme;
	}

	protected HttpDataFactory getHttpDataFactory() {
		if (null == httpDataFactory) {
			httpDataFactory = new DefaultHttpDataFactory();
		}
		return httpDataFactory;
	}

	@Override
	public Parameters parameters() throws RequestParseExecption {
		if (null == parameters) {
			Map<String, Object[]> params = getUriParser().parameters();
			HttpMethod method = request.getMethod();
			if ((!method.equals(HttpMethod.GET) && !method.equals(HttpMethod.HEAD))
					&& (method.equals(HttpMethod.POST) || method.equals(HttpMethod.PUT)
							|| method.equals(HttpMethod.PATCH) || method.equals(HttpMethod.TRACE))) {
				InterfaceHttpPostRequestDecoder decoder = new HttpPostStandardRequestDecoder(this.getHttpDataFactory(),
						request, this.getCharset());
				while (decoder.hasNext()) {
					InterfaceHttpData data = decoder.next();
					switch (data.getHttpDataType()) {
					case Attribute:
						try {
							params.put(data.getName(),
									ArrayUtils.add(params.get(data.getName()), ((Attribute) data).getString()));
						} catch (IOException e) {
							throw new RequestParseExecption(e);
						}
						break;
					default:
						break;
					}
				}
			}
			parameters = new HashMapParameters(params);
		}
		return parameters;
	}

	@Override
	public Attachments attachments() throws RequestParseExecption {
		if (null == attachments) {
			if (HttpPostRequestDecoder.isMultipart(request)) {
				InterfaceHttpPostRequestDecoder decoder = new HttpPostMultipartRequestDecoder(this.getHttpDataFactory(),
						request, this.getCharset());
				Map<String, Collection<Attachment>> attachments = new HashMap<>();
				while (decoder.hasNext()) {
					InterfaceHttpData data = decoder.next();
					Attachment att = null;
					switch (data.getHttpDataType()) {
					case Attribute:
						att = new NettyAttributeAttachment((Attribute) data);
						break;
					case FileUpload:
						att = new NettyFileUploadAttachment((FileUpload) data);
						break;
					default:
						break;
					}
					if (att != null) {
						Collection<Attachment> atts = attachments.get(data.getName());
						if (null == atts) {
							atts = new ArrayList<>();
							attachments.put(data.getName(), atts);
						}
						atts.add(att);
					}
				}
				this.attachments = new HashMapAttachments(attachments);
			} else {
				this.attachments = Attachments.EMPTY;
			}
		}
		return attachments;
	}

}
