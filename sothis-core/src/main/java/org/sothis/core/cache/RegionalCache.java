package org.sothis.core.cache;

/**
 * 分区方式的缓存实现
 * 
 * @author velna
 * 
 */
public class RegionalCache extends BasicCache {
	private final String region;

	/**
	 * 创建一个分区名为{@code region}的缓存
	 * 
	 * @param region
	 * @param storage
	 */
	public RegionalCache(String region, Storage storage) {
		super(region, storage);
		this.region = region;
	}

	@Override
	protected String buildCacheKey(Object... keys) {
		StringBuilder ret = new StringBuilder();
		ret.append(region).append('_');
		for (int i = 0; i < keys.length; i++) {
			ret.append(keys[i]);
			if (i < keys.length - 1) {
				ret.append('_');
			}
		}
		return ret.toString();
	}

	/**
	 * 分区名
	 * 
	 * @return
	 */
	public String getRegion() {
		return region;
	}

}
