package org.sothis.core.cache;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 缓存统计结果类
 * 
 * @author velna
 * 
 */
public class CacheStat {

	private final static long PERIOD = 600000;

	private final String name;

	private final long startTime = System.currentTimeMillis();
	private final AtomicLong hits = new AtomicLong(0);
	private final AtomicLong puts = new AtomicLong(0);
	private final AtomicLong misses = new AtomicLong(0);
	private final AtomicLong requests = new AtomicLong(0);

	private long lStartTime = System.currentTimeMillis();
	private final AtomicLong lHits = new AtomicLong(0);
	private final AtomicLong lSets = new AtomicLong(0);
	private final AtomicLong lMisses = new AtomicLong(0);
	private final AtomicLong lRequests = new AtomicLong(0);

	/**
	 * 创建一个名称为{@code name}的缓存统计
	 * 
	 * @param name
	 */
	public CacheStat(String name) {
		this.name = name;
	}

	/**
	 * 记录一次get操作
	 * 
	 * @param hits
	 *            缓存的命中数
	 * @param misses
	 *            缓存的未命中数
	 */
	public void requestGet(int hits, int misses) {
		checkPeriod();
		if (hits > 0) {
			this.hits.addAndGet(hits);
			this.lHits.addAndGet(hits);
		}
		if (misses > 0) {
			this.misses.addAndGet(misses);
			this.lMisses.addAndGet(misses);
		}
		this.requests.incrementAndGet();
		this.lRequests.incrementAndGet();
	}

	/**
	 * 记录一次put操作
	 */
	public void requestPut() {
		checkPeriod();
		this.requests.incrementAndGet();
		this.lRequests.incrementAndGet();
		this.puts.incrementAndGet();
		this.lSets.incrementAndGet();
	}

	private void checkPeriod() {
		long now = System.currentTimeMillis();
		long time = now - this.lStartTime;
		if (time >= PERIOD) {
			lStartTime = now;
			this.lHits.set(0);
			this.lMisses.set(0);
			this.lSets.set(0);
			this.lRequests.set(0);
		}
	}

	/**
	 * 得到本缓存生成的时间。实际为本实例的初始化时间
	 * 
	 * @return
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * 得到所有的请求次数。包括get和put。
	 * 
	 * @return
	 */
	public long getRequests() {
		return this.requests.get();
	}

	/**
	 * 得到命中的次数
	 * 
	 * @return
	 */
	public long getHits() {
		return hits.get();
	}

	/**
	 * 得到未命中的次数
	 * 
	 * @return
	 */
	public long getMisses() {
		return misses.get();
	}

	/**
	 * 得到put的次数
	 * 
	 * @return
	 */
	public long getPuts() {
		return puts.get();
	}

	/**
	 * 得到最近5分钟的统计开始时间
	 * 
	 * @return
	 */
	public long getLStartTime() {
		return lStartTime;
	}

	/**
	 * 得到最近5分钟的所有请求数
	 * 
	 * @return
	 */
	public long getLRequests() {
		return this.lRequests.get();
	}

	/**
	 * 得到最近5分钟的命中次数
	 * 
	 * @return
	 */
	public long getLHits() {
		return lHits.get();
	}

	/**
	 * 得到最近5分钟的未命中次数
	 * 
	 * @return
	 */
	public long getLMisses() {
		return lMisses.get();
	}

	/**
	 * 得到最近5分钟的put次数
	 * 
	 * @return
	 */
	public long getLPuts() {
		return lSets.get();
	}

	/**
	 * 得到每秒命中次数
	 * 
	 * @return
	 */
	public double getHitRate() {
		return this.hits.get() * 1000.0 / (System.currentTimeMillis() - this.startTime);
	}

	/**
	 * 得到每秒未命中次数
	 * 
	 * @return
	 */
	public double getMissRate() {
		return this.misses.get() * 1000.0 / (System.currentTimeMillis() - this.startTime);
	}

	/**
	 * 得到每秒put次数
	 * 
	 * @return
	 */
	public double getPutRate() {
		return this.puts.get() * 1000.0 / (System.currentTimeMillis() - this.startTime);
	}

	/**
	 * 得到每秒请求次数
	 * 
	 * @return
	 */
	public double getRequestRate() {
		return this.requests.get() * 1000.0 / (System.currentTimeMillis() - this.startTime);
	}

	/**
	 * 得到最近5分钟每秒命中次数
	 * 
	 * @return
	 */
	public double getLHitRate() {
		return this.lHits.get() * 1000.0 / (System.currentTimeMillis() - this.lStartTime);
	}

	/**
	 * 得到最近5分钟每秒未命中次数
	 * 
	 * @return
	 */
	public double getLMissRate() {
		return this.lMisses.get() * 1000.0 / (System.currentTimeMillis() - this.lStartTime);
	}

	/**
	 * 得到最近5分钟每秒put次数
	 * 
	 * @return
	 */
	public double getLPutRate() {
		return this.lSets.get() * 1000.0 / (System.currentTimeMillis() - this.lStartTime);
	}

	/**
	 * 得到最近5分钟每秒请求数
	 * 
	 * @return
	 */
	public double getLRequestRate() {
		return this.lRequests.get() * 1000.0 / (System.currentTimeMillis() - this.lStartTime);
	}

	/**
	 * 缓存统计的名称
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

}
