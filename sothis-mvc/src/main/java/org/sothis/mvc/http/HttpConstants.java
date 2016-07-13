package org.sothis.mvc.http;

public final class HttpConstants {

	private HttpConstants() {
	}

	public static final class Schemes {
		public static final String HTTP = "http";
		public static final String HTTPS = "https";
	}

	public static final class Methods {
		public static final String GET = "GET";
		public static final String POST = "POST";
		public static final String PUT = "PUT";
		public static final String DELETE = "DELETE";
		public static final String HEAD = "HEAD";
		public static final String CONNECT = "CONNECT";
		public static final String OPTIONS = "OPTIONS";
		public static final String TRACE = "TRACE";

		private Methods() {
		}
	}

	/**
	 * Server status codes; see RFC 2068.
	 */
	public static final class StatusCodes {
		/**
		 * Status code (100) indicating the client can continue.
		 */
		public static final int SC_CONTINUE = 100;

		/**
		 * Status code (101) indicating the server is switching protocols
		 * according to Upgrade header.
		 */
		public static final int SC_SWITCHING_PROTOCOLS = 101;

		/**
		 * Status code (200) indicating the request succeeded normally.
		 */
		public static final int SC_OK = 200;

		/**
		 * Status code (201) indicating the request succeeded and created a new
		 * resource on the server.
		 */
		public static final int SC_CREATED = 201;

		/**
		 * Status code (202) indicating that a request was accepted for
		 * processing, but was not completed.
		 */
		public static final int SC_ACCEPTED = 202;

		/**
		 * Status code (203) indicating that the meta information presented by
		 * the client did not originate from the server.
		 */
		public static final int SC_NON_AUTHORITATIVE_INFORMATION = 203;

		/**
		 * Status code (204) indicating that the request succeeded but that
		 * there was no new information to return.
		 */
		public static final int SC_NO_CONTENT = 204;

		/**
		 * Status code (205) indicating that the agent <em>SHOULD</em> reset the
		 * document view which caused the request to be sent.
		 */
		public static final int SC_RESET_CONTENT = 205;

		/**
		 * Status code (206) indicating that the server has fulfilled the
		 * partial GET request for the resource.
		 */
		public static final int SC_PARTIAL_CONTENT = 206;

		/**
		 * Status code (300) indicating that the requested resource corresponds
		 * to any one of a set of representations, each with its own specific
		 * location.
		 */
		public static final int SC_MULTIPLE_CHOICES = 300;

		/**
		 * Status code (301) indicating that the resource has permanently moved
		 * to a new location, and that future references should use a new URI
		 * with their requests.
		 */
		public static final int SC_MOVED_PERMANENTLY = 301;

		/**
		 * Status code (302) indicating that the resource has temporarily moved
		 * to another location, but that future references should still use the
		 * original URI to access the resource.
		 *
		 * This definition is being retained for backwards compatibility.
		 * SC_FOUND is now the preferred definition.
		 */
		public static final int SC_MOVED_TEMPORARILY = 302;

		/**
		 * Status code (302) indicating that the resource reside temporarily
		 * under a different URI. Since the redirection might be altered on
		 * occasion, the client should continue to use the Request-URI for
		 * future requests.(HTTP/1.1) To represent the status code (302), it is
		 * recommended to use this variable.
		 */
		public static final int SC_FOUND = 302;

		/**
		 * Status code (303) indicating that the response to the request can be
		 * found under a different URI.
		 */
		public static final int SC_SEE_OTHER = 303;

		/**
		 * Status code (304) indicating that a conditional GET operation found
		 * that the resource was available and not modified.
		 */
		public static final int SC_NOT_MODIFIED = 304;

		/**
		 * Status code (305) indicating that the requested resource
		 * <em>MUST</em> be accessed through the proxy given by the
		 * <code><em>Location</em></code> field.
		 */
		public static final int SC_USE_PROXY = 305;

		/**
		 * Status code (307) indicating that the requested resource resides
		 * temporarily under a different URI. The temporary URI <em>SHOULD</em>
		 * be given by the <code><em>Location</em></code> field in the response.
		 */
		public static final int SC_TEMPORARY_REDIRECT = 307;

		/**
		 * Status code (400) indicating the request sent by the client was
		 * syntactically incorrect.
		 */
		public static final int SC_BAD_REQUEST = 400;

		/**
		 * Status code (401) indicating that the request requires HTTP
		 * authentication.
		 */
		public static final int SC_UNAUTHORIZED = 401;

		/**
		 * Status code (402) reserved for future use.
		 */
		public static final int SC_PAYMENT_REQUIRED = 402;

		/**
		 * Status code (403) indicating the server understood the request but
		 * refused to fulfill it.
		 */
		public static final int SC_FORBIDDEN = 403;

		/**
		 * Status code (404) indicating that the requested resource is not
		 * available.
		 */
		public static final int SC_NOT_FOUND = 404;

		/**
		 * Status code (405) indicating that the method specified in the
		 * <code><em>Request-Line</em></code> is not allowed for the resource
		 * identified by the <code><em>Request-URI</em></code>.
		 */
		public static final int SC_METHOD_NOT_ALLOWED = 405;

		/**
		 * Status code (406) indicating that the resource identified by the
		 * request is only capable of generating response entities which have
		 * content characteristics not acceptable according to the accept
		 * headers sent in the request.
		 */
		public static final int SC_NOT_ACCEPTABLE = 406;

		/**
		 * Status code (407) indicating that the client <em>MUST</em> first
		 * authenticate itself with the proxy.
		 */
		public static final int SC_PROXY_AUTHENTICATION_REQUIRED = 407;

		/**
		 * Status code (408) indicating that the client did not produce a
		 * request within the time that the server was prepared to wait.
		 */
		public static final int SC_REQUEST_TIMEOUT = 408;

		/**
		 * Status code (409) indicating that the request could not be completed
		 * due to a conflict with the current state of the resource.
		 */
		public static final int SC_CONFLICT = 409;

		/**
		 * Status code (410) indicating that the resource is no longer available
		 * at the server and no forwarding address is known. This condition
		 * <em>SHOULD</em> be considered permanent.
		 */
		public static final int SC_GONE = 410;

		/**
		 * Status code (411) indicating that the request cannot be handled
		 * without a defined <code><em>Content-Length</em></code>.
		 */
		public static final int SC_LENGTH_REQUIRED = 411;

		/**
		 * Status code (412) indicating that the precondition given in one or
		 * more of the request-header fields evaluated to false when it was
		 * tested on the server.
		 */
		public static final int SC_PRECONDITION_FAILED = 412;

		/**
		 * Status code (413) indicating that the server is refusing to process
		 * the request because the request entity is larger than the server is
		 * willing or able to process.
		 */
		public static final int SC_REQUEST_ENTITY_TOO_LARGE = 413;

		/**
		 * Status code (414) indicating that the server is refusing to service
		 * the request because the <code><em>Request-URI</em></code> is longer
		 * than the server is willing to interpret.
		 */
		public static final int SC_REQUEST_URI_TOO_LONG = 414;

		/**
		 * Status code (415) indicating that the server is refusing to service
		 * the request because the entity of the request is in a format not
		 * supported by the requested resource for the requested method.
		 */
		public static final int SC_UNSUPPORTED_MEDIA_TYPE = 415;

		/**
		 * Status code (416) indicating that the server cannot serve the
		 * requested byte range.
		 */
		public static final int SC_REQUESTED_RANGE_NOT_SATISFIABLE = 416;

		/**
		 * Status code (417) indicating that the server could not meet the
		 * expectation given in the Expect request header.
		 */
		public static final int SC_EXPECTATION_FAILED = 417;

		/**
		 * Status code (500) indicating an error inside the HTTP server which
		 * prevented it from fulfilling the request.
		 */
		public static final int SC_INTERNAL_SERVER_ERROR = 500;

		/**
		 * Status code (501) indicating the HTTP server does not support the
		 * functionality needed to fulfill the request.
		 */
		public static final int SC_NOT_IMPLEMENTED = 501;

		/**
		 * Status code (502) indicating that the HTTP server received an invalid
		 * response from a server it consulted when acting as a proxy or
		 * gateway.
		 */
		public static final int SC_BAD_GATEWAY = 502;

		/**
		 * Status code (503) indicating that the HTTP server is temporarily
		 * overloaded, and unable to handle the request.
		 */
		public static final int SC_SERVICE_UNAVAILABLE = 503;

		/**
		 * Status code (504) indicating that the server did not receive a timely
		 * response from the upstream server while acting as a gateway or proxy.
		 */
		public static final int SC_GATEWAY_TIMEOUT = 504;

		/**
		 * Status code (505) indicating that the server does not support or
		 * refuses to support the HTTP protocol version that was used in the
		 * request message.
		 */
		public static final int SC_HTTP_VERSION_NOT_SUPPORTED = 505;

		private StatusCodes() {
		}
	}

	/**
	 * Standard HTTP header names.
	 */
	public static final class HeaderNames {
		/**
		 * {@code "Accept"}
		 */
		public static final String ACCEPT = "Accept";
		/**
		 * {@code "Accept-Charset"}
		 */
		public static final String ACCEPT_CHARSET = "Accept-Charset";
		/**
		 * {@code "Accept-Encoding"}
		 */
		public static final String ACCEPT_ENCODING = "Accept-Encoding";
		/**
		 * {@code "Accept-Language"}
		 */
		public static final String ACCEPT_LANGUAGE = "Accept-Language";
		/**
		 * {@code "Accept-Ranges"}
		 */
		public static final String ACCEPT_RANGES = "Accept-Ranges";
		/**
		 * {@code "Accept-Patch"}
		 */
		public static final String ACCEPT_PATCH = "Accept-Patch";
		/**
		 * {@code "Access-Control-Allow-Credentials"}
		 */
		public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
		/**
		 * {@code "Access-Control-Allow-Headers"}
		 */
		public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
		/**
		 * {@code "Access-Control-Allow-Methods"}
		 */
		public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
		/**
		 * {@code "Access-Control-Allow-Origin"}
		 */
		public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
		/**
		 * {@code "Access-Control-Expose-Headers"}
		 */
		public static final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
		/**
		 * {@code "Access-Control-Max-Age"}
		 */
		public static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
		/**
		 * {@code "Access-Control-Request-Headers"}
		 */
		public static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";
		/**
		 * {@code "Access-Control-Request-Method"}
		 */
		public static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";
		/**
		 * {@code "Age"}
		 */
		public static final String AGE = "Age";
		/**
		 * {@code "Allow"}
		 */
		public static final String ALLOW = "Allow";
		/**
		 * {@code "Authorization"}
		 */
		public static final String AUTHORIZATION = "Authorization";
		/**
		 * {@code "Cache-Control"}
		 */
		public static final String CACHE_CONTROL = "Cache-Control";
		/**
		 * {@code "Connection"}
		 */
		public static final String CONNECTION = "Connection";
		/**
		 * {@code "Content-Base"}
		 */
		public static final String CONTENT_BASE = "Content-Base";
		/**
		 * {@code "Content-Encoding"}
		 */
		public static final String CONTENT_ENCODING = "Content-Encoding";
		/**
		 * {@code "Content-Language"}
		 */
		public static final String CONTENT_LANGUAGE = "Content-Language";
		/**
		 * {@code "Content-Length"}
		 */
		public static final String CONTENT_LENGTH = "Content-Length";
		/**
		 * {@code "Content-Location"}
		 */
		public static final String CONTENT_LOCATION = "Content-Location";
		/**
		 * {@code "Content-Transfer-Encoding"}
		 */
		public static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
		/**
		 * {@code "Content-MD5"}
		 */
		public static final String CONTENT_MD5 = "Content-MD5";
		/**
		 * {@code "Content-Range"}
		 */
		public static final String CONTENT_RANGE = "Content-Range";
		/**
		 * {@code "Content-Type"}
		 */
		public static final String CONTENT_TYPE = "Content-Type";
		/**
		 * {@code "Cookie"}
		 */
		public static final String COOKIE = "Cookie";
		/**
		 * {@code "Date"}
		 */
		public static final String DATE = "Date";
		/**
		 * {@code "ETag"}
		 */
		public static final String ETAG = "ETag";
		/**
		 * {@code "Expect"}
		 */
		public static final String EXPECT = "Expect";
		/**
		 * {@code "Expires"}
		 */
		public static final String EXPIRES = "Expires";
		/**
		 * {@code "From"}
		 */
		public static final String FROM = "From";
		/**
		 * {@code "Host"}
		 */
		public static final String HOST = "Host";
		/**
		 * {@code "If-Match"}
		 */
		public static final String IF_MATCH = "If-Match";
		/**
		 * {@code "If-Modified-Since"}
		 */
		public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
		/**
		 * {@code "If-None-Match"}
		 */
		public static final String IF_NONE_MATCH = "If-None-Match";
		/**
		 * {@code "If-Range"}
		 */
		public static final String IF_RANGE = "If-Range";
		/**
		 * {@code "If-Unmodified-Since"}
		 */
		public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
		/**
		 * {@code "Last-Modified"}
		 */
		public static final String LAST_MODIFIED = "Last-Modified";
		/**
		 * {@code "Location"}
		 */
		public static final String LOCATION = "Location";
		/**
		 * {@code "Max-Forwards"}
		 */
		public static final String MAX_FORWARDS = "Max-Forwards";
		/**
		 * {@code "Origin"}
		 */
		public static final String ORIGIN = "Origin";
		/**
		 * {@code "Pragma"}
		 */
		public static final String PRAGMA = "Pragma";
		/**
		 * {@code "Proxy-Authenticate"}
		 */
		public static final String PROXY_AUTHENTICATE = "Proxy-Authenticate";
		/**
		 * {@code "Proxy-Authorization"}
		 */
		public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
		/**
		 * {@code "Range"}
		 */
		public static final String RANGE = "Range";
		/**
		 * {@code "Referer"}
		 */
		public static final String REFERER = "Referer";
		/**
		 * {@code "Retry-After"}
		 */
		public static final String RETRY_AFTER = "Retry-After";
		/**
		 * {@code "Sec-WebSocket-Key1"}
		 */
		public static final String SEC_WEBSOCKET_KEY1 = "Sec-WebSocket-Key1";
		/**
		 * {@code "Sec-WebSocket-Key2"}
		 */
		public static final String SEC_WEBSOCKET_KEY2 = "Sec-WebSocket-Key2";
		/**
		 * {@code "Sec-WebSocket-Location"}
		 */
		public static final String SEC_WEBSOCKET_LOCATION = "Sec-WebSocket-Location";
		/**
		 * {@code "Sec-WebSocket-Origin"}
		 */
		public static final String SEC_WEBSOCKET_ORIGIN = "Sec-WebSocket-Origin";
		/**
		 * {@code "Sec-WebSocket-Protocol"}
		 */
		public static final String SEC_WEBSOCKET_PROTOCOL = "Sec-WebSocket-Protocol";
		/**
		 * {@code "Sec-WebSocket-Version"}
		 */
		public static final String SEC_WEBSOCKET_VERSION = "Sec-WebSocket-Version";
		/**
		 * {@code "Sec-WebSocket-Key"}
		 */
		public static final String SEC_WEBSOCKET_KEY = "Sec-WebSocket-Key";
		/**
		 * {@code "Sec-WebSocket-Accept"}
		 */
		public static final String SEC_WEBSOCKET_ACCEPT = "Sec-WebSocket-Accept";
		/**
		 * {@code "Server"}
		 */
		public static final String SERVER = "Server";
		/**
		 * {@code "Set-Cookie"}
		 */
		public static final String SET_COOKIE = "Set-Cookie";
		/**
		 * {@code "Set-Cookie2"}
		 */
		public static final String SET_COOKIE2 = "Set-Cookie2";
		/**
		 * {@code "TE"}
		 */
		public static final String TE = "TE";
		/**
		 * {@code "Trailer"}
		 */
		public static final String TRAILER = "Trailer";
		/**
		 * {@code "Transfer-Encoding"}
		 */
		public static final String TRANSFER_ENCODING = "Transfer-Encoding";
		/**
		 * {@code "Upgrade"}
		 */
		public static final String UPGRADE = "Upgrade";
		/**
		 * {@code "User-Agent"}
		 */
		public static final String USER_AGENT = "User-Agent";
		/**
		 * {@code "Vary"}
		 */
		public static final String VARY = "Vary";
		/**
		 * {@code "Via"}
		 */
		public static final String VIA = "Via";
		/**
		 * {@code "Warning"}
		 */
		public static final String WARNING = "Warning";
		/**
		 * {@code "WebSocket-Location"}
		 */
		public static final String WEBSOCKET_LOCATION = "WebSocket-Location";
		/**
		 * {@code "WebSocket-Origin"}
		 */
		public static final String WEBSOCKET_ORIGIN = "WebSocket-Origin";
		/**
		 * {@code "WebSocket-Protocol"}
		 */
		public static final String WEBSOCKET_PROTOCOL = "WebSocket-Protocol";
		/**
		 * {@code "WWW-Authenticate"}
		 */
		public static final String WWW_AUTHENTICATE = "WWW-Authenticate";

		private HeaderNames() {
		}
	}

	/**
	 * Standard HTTP header values.
	 */
	public static final class HeaderValues {
		/**
		 * {@code "application/x-www-form-urlencoded"}
		 */
		public static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
		/**
		 * {@code "base64"}
		 */
		public static final String BASE64 = "base64";
		/**
		 * {@code "binary"}
		 */
		public static final String BINARY = "binary";
		/**
		 * {@code "boundary"}
		 */
		public static final String BOUNDARY = "boundary";
		/**
		 * {@code "bytes"}
		 */
		public static final String BYTES = "bytes";
		/**
		 * {@code "charset"}
		 */
		public static final String CHARSET = "charset";
		/**
		 * {@code "chunked"}
		 */
		public static final String CHUNKED = "chunked";
		/**
		 * {@code "close"}
		 */
		public static final String CLOSE = "close";
		/**
		 * {@code "compress"}
		 */
		public static final String COMPRESS = "compress";
		/**
		 * {@code "100-continue"}
		 */
		public static final String CONTINUE = "100-continue";
		/**
		 * {@code "deflate"}
		 */
		public static final String DEFLATE = "deflate";
		/**
		 * {@code "gzip"}
		 */
		public static final String GZIP = "gzip";
		/**
		 * {@code "identity"}
		 */
		public static final String IDENTITY = "identity";
		/**
		 * {@code "keep-alive"}
		 */
		public static final String KEEP_ALIVE = "keep-alive";
		/**
		 * {@code "max-age"}
		 */
		public static final String MAX_AGE = "max-age";
		/**
		 * {@code "max-stale"}
		 */
		public static final String MAX_STALE = "max-stale";
		/**
		 * {@code "min-fresh"}
		 */
		public static final String MIN_FRESH = "min-fresh";
		/**
		 * {@code "multipart/form-data"}
		 */
		public static final String MULTIPART_FORM_DATA = "multipart/form-data";
		/**
		 * {@code "must-revalidate"}
		 */
		public static final String MUST_REVALIDATE = "must-revalidate";
		/**
		 * {@code "no-cache"}
		 */
		public static final String NO_CACHE = "no-cache";
		/**
		 * {@code "no-store"}
		 */
		public static final String NO_STORE = "no-store";
		/**
		 * {@code "no-transform"}
		 */
		public static final String NO_TRANSFORM = "no-transform";
		/**
		 * {@code "none"}
		 */
		public static final String NONE = "none";
		/**
		 * {@code "only-if-cached"}
		 */
		public static final String ONLY_IF_CACHED = "only-if-cached";
		/**
		 * {@code "private"}
		 */
		public static final String PRIVATE = "private";
		/**
		 * {@code "proxy-revalidate"}
		 */
		public static final String PROXY_REVALIDATE = "proxy-revalidate";
		/**
		 * {@code "public"}
		 */
		public static final String PUBLIC = "public";
		/**
		 * {@code "quoted-printable"}
		 */
		public static final String QUOTED_PRINTABLE = "quoted-printable";
		/**
		 * {@code "s-maxage"}
		 */
		public static final String S_MAXAGE = "s-maxage";
		/**
		 * {@code "trailers"}
		 */
		public static final String TRAILERS = "trailers";
		/**
		 * {@code "Upgrade"}
		 */
		public static final String UPGRADE = "Upgrade";
		/**
		 * {@code "WebSocket"}
		 */
		public static final String WEBSOCKET = "WebSocket";

		private HeaderValues() {
		}
	}

}
