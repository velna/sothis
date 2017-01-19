package org.sothis.core.util.bwlist;

public abstract class BWSource {
	private int id;
	private final Object attachment;
	private final BWLogic logic;

	public BWSource(BWLogic logic, Object attachment) {
		this.attachment = attachment;
		this.logic = logic;
	}

	void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public Object getAttachment() {
		return attachment;
	}

	public BWLogic getLogic() {
		return logic;
	}

	abstract public int refresh(BWboolean force);
}
