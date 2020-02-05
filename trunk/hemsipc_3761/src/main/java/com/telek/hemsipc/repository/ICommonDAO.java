package com.telek.hemsipc.repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 基础数据库操作接口
 * @Class Name：IDAO    
 * @Class Description：    
 * @Creater：telek    
 * @Create Time：2019年4月23日上午10:36:00    
 * @Modifier：telek    
 * @Modification Time：2019年4月23日上午10:36:00    
 * @Remarks：
 */
public interface ICommonDAO {
    /**
     * 保存对象 - 同时处理更新和插入
     * 
     * @param entity 对象
     * @return Object 返回对象
     */
    public Object saveOrUpdate(final Object entity);

    /**
     * 保存对象
     */
    public boolean save(final Object obj);

    /**
     * 更新对象
     */
    public boolean update(final Object obj);

    /**
     * 批量保存对象 - 同时处理更新和插入
     * 
     * @param entities 对象集合
     * @return List
     */
    public List<?> saveOrUpdateAll(final Collection<?> entities);

    /**
     * 删除对象 - 基于实例类及id
     * 
     * @param 数据库映射对象实例类
     * @param id 对象id
     */
    public void delObject(Class<?> clazz, Serializable id);

    /**
     * 删除对象集合
     * 
     * @param entities
     */
    public void delObjectAll(final Collection<?> entities);

    public void delObectAll(final String hql);

    public void delBaseModel(Object baseModel);

    /**
     * 通用方法用于获得特定类型的对象。
     * 
     * @param clazz 数据库映射对象实例类
     * @param id 对象id
     * @return Object
     */
    public Object getObject(Class<?> clazz, Serializable id);

    /**
     * 查找对象集合
     * 
     * @param queryString 查询语句
     * @return 对象集合
     */
    public List<?> findByHql(String queryString);

    public List<?> findAllByClass(Class<?> clazz);

    /**
     * 执行原生sql语句
     * 
     * @param queryString sql语句
     * @param Scalar
     * @param clazz
     * @return
     */
    public List<?> findBySQL(String queryString, Class<?> clazz);

    /**
     * 执行原生sql语句
     * 
     * @param queryString sql语句
     * @param Scalar
     * @param clazz
     * @return
     */
    public List<?> findBySQL(String queryString);

    /**
     * 批量执行sql语句
     * 
     * @param sqlList
     * @return Integer
     */
    public Integer executeSqlBatch(final List<String> sqlList);

    public int executeUpdateSQL(final String sql);

    public void flush();
}
