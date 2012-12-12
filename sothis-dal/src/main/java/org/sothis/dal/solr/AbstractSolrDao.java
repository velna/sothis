package org.sothis.dal.solr;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.sothis.core.util.Pager;
import org.sothis.dal.AbstractJpaCompatibleDao;
import org.sothis.dal.QueryException;
import org.sothis.dal.query.Chain;
import org.sothis.dal.query.Cnd;

public abstract class AbstractSolrDao<E extends SolrEntity, K extends Serializable> extends
		AbstractJpaCompatibleDao<E, K> implements SolrDao<E, K> {

	private final SolrQueryBuilder queryBuilder;
	private SolrServer solrServer;

	public AbstractSolrDao() {
		super();
		queryBuilder = new SolrQueryBuilder(this.getPropertyMap());
	}

	private List<E> find(Cnd cnd, Pager pager, Chain chain, boolean count) {
		try {
			SolrQuery query = this.queryBuilder.buildQuery(cnd, chain);
			if (null != pager) {
				query.setStart(pager.getStartRow());
				query.setRows(pager.getPageSize());
			}

			QueryResponse response = solrServer.query(query);
			SolrDocumentList docList = response.getResults();
			if (count && null != pager) {
				pager.setTotalRows((int) docList.getNumFound());
			}
			if (docList.isEmpty()) {
				return Collections.emptyList();
			}
			List<E> list = new ArrayList<E>(docList.size());
			for (SolrDocument document : docList) {
				list.add(this.documentToEntity(document));
			}
			return list;
		} catch (SolrServerException e) {
			throw new QueryException(e);
		}
	}

	@Override
	public List<E> find(Cnd cnd, Pager pager, Chain chain) {
		return find(cnd, pager, chain, false);
	}

	@Override
	public int update(Cnd cnd, Chain chain) {
		throw new UnsupportedOperationException("update by cnd is not supported.");
	}

	@Override
	public E update(E entity) {
		SolrInputDocument document = this.entityToDocument(entity);
		try {
			this.solrServer.add(document);
			this.solrServer.commit();
		} catch (Exception e) {
			throw new QueryException(e);
		}
		return entity;
	}

	@Override
	public int delete(Cnd cnd) {
		SolrQuery query = this.queryBuilder.buildQuery(cnd, null);
		try {
			this.solrServer.deleteByQuery(query.toString());
			this.solrServer.commit();
		} catch (Exception e) {
			throw new QueryException(e);
		}
		return 0;
	}

	@Override
	public E insert(E entity) {
		return update(entity);
	}

	@Override
	public int count(Cnd cnd) {
		Pager pager = new Pager(0);
		find(cnd, pager, null, true);
		return pager.getTotalRows();
	}

	@Override
	public List<E> findAndCount(Cnd cnd, Pager pager, Chain chain) {
		return find(cnd, pager, chain, true);
	}

	private E documentToEntity(SolrDocument document) {
		try {
			E entity = this.getEntityClass().newInstance();
			for (Map.Entry<String, Object> entry : document.entrySet()) {
				PropertyInfo propertyInfo = this.getPropertyInfoByField(entry.getKey());
				if (null == propertyInfo) {
					throw new QueryException("un-mapped field '" + entry.getKey() + "' of entity class "
							+ this.getEntityClass());
				}
				PropertyDescriptor descriptor = propertyInfo.getPropertyDescriptor();
				Method writeMethod = descriptor.getWriteMethod();
				writeMethod.invoke(entity, entry.getValue());
			}
			return entity;
		} catch (Exception e) {
			throw new QueryException(e);
		}
	}

	private SolrInputDocument entityToDocument(E entity) {
		try {
			SolrInputDocument document = new SolrInputDocument();
			Map<String, PropertyInfo> propertyMap = this.getPropertyMap();
			for (Map.Entry<String, PropertyInfo> entry : propertyMap.entrySet()) {
				PropertyInfo pi = entry.getValue();
				Object value = pi.getPropertyDescriptor().getReadMethod().invoke(entity, (Object[]) null);
				if (null != value) {
					SolrInputField field = new SolrInputField(pi.getColumn().name());
					field.setValue(value, 1);
				}
			}
			return document;
		} catch (Exception e) {
			throw new QueryException(e);
		}
	}

	public void setSolrServer(SolrServer solrServer) {
		this.solrServer = solrServer;
	}

	public SolrServer getSolrServer() {
		return solrServer;
	}

}
