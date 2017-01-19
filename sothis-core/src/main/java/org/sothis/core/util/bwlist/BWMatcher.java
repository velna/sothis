package org.sothis.core.util.bwlist;

public abstract class BWMatcher {

	private final String groupName;

	public BWMatcher(String groupName) {
		super();
		this.groupName = groupName;
	}

	public String getGroupName() {
		return groupName;
	}

	abstract public boolean hasItems();

	abstract public int addItem(String item, Object attachment);

	abstract public void match(String hdr, BWResult result);

	abstract public void compile();

	abstract public int size();
}
