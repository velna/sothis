package org.sothis.dal.mongo;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.types.ObjectId;
import org.sothis.core.util.Cursor;
import org.sothis.core.util.Pager;
import org.sothis.dal.AbstractJpaCompatibleDao;
import org.sothis.dal.query.Chain;
import org.sothis.dal.query.Cnd;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;

/**
 * mongo db Dao的虚基类
 * 
 * @author velna
 * 
 * @param <E>
 */
public abstract class AbstractMongoDao<E extends MongoEntity> extends AbstractJpaCompatibleDao<E, String> implements MongoDao<E> {
	public static final String ID = "_id";

	private final String dbName;
	private final String collectionName;
	private final MongoQueryBuilder queryBuilder;
	private Mongo mongo;

	public AbstractMongoDao() {
		super();
		String tableName = this.getTableName();
		int i = tableName.indexOf('.');
		if (i <= 0 || i == tableName.length() - 1) {
			throw new RuntimeException("invalid table name: " + tableName + " of entity class: " + this.getEntityClass());
		}
		dbName = tableName.substring(0, i);
		this.collectionName = tableName.substring(i + 1);
		queryBuilder = new MongoQueryBuilder(getPropertyMap(), this.isIdGeneratedValue());
	}

	/**
	 * 得到集合
	 * 
	 * @return
	 */
	protected final DBCollection getDbCollection() {
		DBCollection dbCollection = mongo.getDB(dbName).getCollection(collectionName);
		dbCollection.setWriteConcern(WriteConcern.SAFE);
		return dbCollection;
	}

	public Mongo getMongo() {
		return mongo;
	}

	public void setMongo(Mongo mongo) {
		this.mongo = mongo;
	}

	@Override
	public List<E> find(Cnd cnd, Pager pager, Chain chain) {
		DBObject query = queryBuilder.cndToQuery(cnd);
		DBObject fields = queryBuilder.chainToFields(chain);
		DBObject sorts = queryBuilder.orderByToSorts(cnd);

		DBCursor cursor = this.getDbCollection().find(query, fields).sort(sorts);
		if (null != pager) {
			cursor.limit(pager.getPageSize()).skip(pager.getStartRow()).batchSize(pager.getPageSize());
		} else {
			cursor.limit(Integer.MAX_VALUE).skip(0);
		}
		List<DBObject> dbObjects = cursor.toArray();
		List<E> ret = new ArrayList<E>(dbObjects.size());
		for (DBObject object : dbObjects) {
			ret.add(this.dbObjectToEntity(object));
		}
		return ret;
	}

	@Override
	public Cursor<E> cursor(Cnd cnd, Chain chain) {
		DBObject query = queryBuilder.cndToQuery(cnd);
		DBObject fields = queryBuilder.chainToFields(chain);
		DBObject sorts = queryBuilder.orderByToSorts(cnd);

		DBCursor cursor = this.getDbCollection().find(query, fields).sort(sorts);
		return new MongoCursor(cursor);
	}

	@Override
	public int update(Cnd cnd, Chain chain) {
		DBObject query = queryBuilder.cndToQuery(cnd);
		DBObject update = queryBuilder.chainToUpdate(chain);
		WriteResult result = this.getDbCollection().update(query, update, false, true, WriteConcern.SAFE);
		return result.getN();
	}

	@Override
	public int delete(Cnd cnd) {
		DBObject query = queryBuilder.cndToQuery(cnd);
		WriteResult result = this.getDbCollection().remove(query, WriteConcern.SAFE);
		return result.getN();
	}

	@Override
	public E insert(E entity) {
		if (!this.isIdGeneratedValue() && entity.getId() == null) {
			throw new IllegalArgumentException("id can not be null since id column is not a generated value");
		}
		DBObject object = this.entityToDBObject(entity);
		this.getDbCollection().insert(object, WriteConcern.SAFE);
		entity.setId(toEntityId(object.get(ID)));
		return entity;
	}

	@Override
	public List<E> insert(List<E> entityList) {
		List<DBObject> objects = new ArrayList<DBObject>(entityList.size());
		for (E e : entityList) {
			objects.add(this.entityToDBObject(e));
		}
		this.getDbCollection().insert(objects, WriteConcern.SAFE);
		for (int i = 0; i < objects.size(); i++) {
			entityList.get(i).setId(this.toEntityId(objects.get(i).get(ID)));
		}
		return entityList;
	}

	@Override
	public int count(Cnd cnd) {
		DBObject query = queryBuilder.cndToQuery(cnd);
		return (int) this.getDbCollection().count(query);
	}

	protected E dbObjectToEntity(DBObject dbObject) {
		if (null == dbObject) {
			return null;
		}
		E entity;
		try {
			entity = this.getEntityClass().newInstance();
			Set<String> keySet = dbObject.keySet();
			Map<String, PropertyInfo> fieldMap = this.getFieldMap();
			for (String key : keySet) {
				PropertyInfo propertyInfo = fieldMap.get(key);
				if (null == propertyInfo) {
					throw new RuntimeException("un-mapped field '" + key + "' of entity class " + this.getEntityClass());
				}
				PropertyDescriptor descriptor = propertyInfo.getPropertyDescriptor();
				Method writeMethod = descriptor.getWriteMethod();
				Object value = dbObject.get(key);
				if (value instanceof ObjectId) {
					value = ((ObjectId) value).toString();
				}
				try {
					writeMethod.invoke(entity, value);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException("Error set value to " + value + "[" + value.getClass() + "] of field "
							+ descriptor.getName() + "[" + descriptor.getPropertyType() + "]", e);
				}
			}
			return entity;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	protected DBObject entityToDBObject(E entity) {
		if (null == entity) {
			return null;
		}
		DBObject dbObject = new BasicDBObject();
		Map<String, PropertyInfo> propertyMap = this.getPropertyMap();
		for (Map.Entry<String, PropertyInfo> entry : propertyMap.entrySet()) {
			PropertyInfo propertyInfo = entry.getValue();
			if (propertyInfo.isTransient()) {
				continue;
			}
			Method readMethod = propertyInfo.getPropertyDescriptor().getReadMethod();
			Object value;
			try {
				value = readMethod.invoke(entity, (Object[]) null);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			if (propertyInfo.isID()) {
				value = toMongoId(value);
			}
			if (null != value) {
				dbObject.put(propertyInfo.getColumn().name(), value);
			}
		}
		return dbObject;
	}

	protected String toEntityId(Object id) {
		if (this.isIdGeneratedValue()) {
			ObjectId objectId = (ObjectId) id;
			return objectId.toString();
		} else {
			return (String) id;
		}
	}

	@SuppressWarnings("unchecked")
	protected Object toMongoId(Object id) {
		if (null == id) {
			return null;
		}
		if (id instanceof List) {
			List<Object> idList = (List<Object>) id;
			List<Object> idObjectList = new ArrayList<Object>(idList.size());
			for (Object _id : idList) {
				idObjectList.add(toMongoId(_id));
			}
			return idObjectList;
		}
		if (!(id instanceof String)) {
			throw new IllegalArgumentException("id must be instanceof " + String.class.getName() + ", but was "
					+ id.getClass().getName() + " of class " + this.getEntityClass().getName());
		}
		if (this.isIdGeneratedValue()) {
			return new ObjectId((String) id);
		} else {
			return id;
		}
	}

	private class MongoCursor implements Cursor<E> {

		private final DBCursor cursor;

		public MongoCursor(DBCursor cursor) {
			this.cursor = cursor;
		}

		@Override
		public Iterator<E> iterator() {
			return new EntityMapIterator(cursor.iterator());
		}

		@Override
		public int count() {
			return cursor.count();
		}

		@Override
		public Cursor<E> batchSize(int batchSize) {
			cursor.batchSize(batchSize);
			return this;
		}

		@Override
		public Cursor<E> limit(int limit) {
			cursor.limit(limit);
			return this;
		}

		@Override
		public Cursor<E> skip(int skip) {
			cursor.skip(skip);
			return this;
		}

	}

	private class EntityMapIterator implements Iterator<E> {
		private final Iterator<DBObject> i;

		public EntityMapIterator(Iterator<DBObject> i) {
			this.i = i;
		}

		@Override
		public boolean hasNext() {
			return i.hasNext();
		}

		@Override
		public E next() {
			DBObject next = i.next();
			return dbObjectToEntity(next);
		}

		@Override
		public void remove() {
			i.remove();
		}

	}

}
