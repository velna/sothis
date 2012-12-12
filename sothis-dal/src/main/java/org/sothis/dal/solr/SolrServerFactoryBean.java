package org.sothis.dal.solr;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.FactoryBean;

public class SolrServerFactoryBean implements FactoryBean<SolrServer> {

	private String solrBaseURL;

	public void init() {
		
	}

	@Override
	public SolrServer getObject() throws Exception {
		HttpSolrServer solrServer = new HttpSolrServer(solrBaseURL);
		return solrServer;
	}

	@Override
	public Class<?> getObjectType() {
		return SolrServer.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	public void setSolrBaseURL(String solrBaseURL) {
		this.solrBaseURL = solrBaseURL;
	}

}
