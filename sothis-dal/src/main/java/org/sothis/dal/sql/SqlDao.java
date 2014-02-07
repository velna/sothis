package org.sothis.dal.sql;

import java.io.Serializable;

import org.sothis.dal.Dao;

/**
 * 基本sql的Dao接口
 * 
 * @author velna
 * 
 * @param <E>
 * @param <K>
 */
public interface SqlDao<E extends SqlEntity, K extends Serializable> extends Dao<E, K> {

}
