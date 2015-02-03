package org.sothis.core.util;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.FactoryBean;

public class ExecutorFactoryBean implements FactoryBean<Executor> {

	private int maxThreads;
	private int minThreads;
	private int queueSize;
	private int maxIdel;
	private String prefix = "exe";

	@Override
	public Executor getObject() throws Exception {
		return new BlockingThreadPoolExecutor(prefix, minThreads, maxThreads, queueSize, maxIdel);
	}

	@Override
	public Class<?> getObjectType() {
		return Executor.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}

	public void setMinThreads(int minThreads) {
		this.minThreads = minThreads;
	}

	public void setQueueSize(int queueSize) {
		this.queueSize = queueSize;
	}

	public void setMaxIdel(int maxIdel) {
		this.maxIdel = maxIdel;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

}
