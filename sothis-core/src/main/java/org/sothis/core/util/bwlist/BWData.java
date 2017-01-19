package org.sothis.core.util.bwlist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BWData {
	private int nextSourceId;
	private int nextExprId;
	private final BWList bwList;
	private final List<BWSource> sourceList = new ArrayList<>();
	private final List<BWGroup> groupList = new ArrayList<>();
	private final List<BWSourceData> sourceDataList = new ArrayList<>();
	private final Map<String, Integer> exprIdMap = new HashMap<>();

	public BWData(BWList bwList) {
		this.bwList = bwList;
		this.nextExprId = 1;
		this.nextSourceId = 1;
	}

	BWResult newResult() {
		return new BWResult(this.nextExprId, this.bwList.getVersion());
	}
	
	public void clear(){
		
	}
}
