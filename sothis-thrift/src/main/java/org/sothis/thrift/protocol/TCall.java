package org.sothis.thrift.protocol;

public class TCall<REQ extends TEntity, RESP extends TEntity> {

	protected final TMessage message;
	protected RESP response;
	protected REQ request;

	public TCall(TMessage message) {
		this.message = message;
	}

	public RESP getResponse() {
		return response;
	}

	public void setResponse(RESP response) {
		this.response = response;
	}

	public REQ getRequest() {
		return request;
	}

	public void setRequest(REQ request) {
		this.request = request;
	}

	public TMessage getMessage() {
		return message;
	}

}
