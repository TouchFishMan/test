package com.telek.hemsipc.repository.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import com.telek.hemsipc.repository.ICommonDAO;

/**
 * 数据库交互统一DAO
 * 
 * @author Xugl
 * @param <E>
 * @date 2015-12-23 下午3:28:25
 */
@Component("commonDAO")
public class CommonDAOImpl implements ICommonDAO {
    private final Logger log = Logger.getLogger(getClass());
    private HibernateTemplate hibernateTemplate;

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    @Autowired
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    @Override
    public void delObectAll(String hql) {
        Session session = getHibernateTemplate().getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.createQuery(hql).executeUpdate();
        tx.commit();
        session.close();
    }

    @Override
    @SuppressWarnings("finally")
    public Object saveOrUpdate(Object entity) {
        if (entity == null) {
            log.error("load entity error: null");
        }
        try {
            getHibernateTemplate().saveOrUpdate(entity);
        } catch (DataAccessException e) {
            log.error(e.getMessage(), e);
            entity = null;
        } finally {
            return entity;
        }
    }

    @Override
    public List<?> saveOrUpdateAll(Collection<?> entities) {
        getHibernateTemplate().saveOrUpdateAll(entities);
        List<Object> list = new ArrayList<Object>();
        list.addAll(entities);
        return list;
    }

    @Override
    public void delObject(Class<?> clazz, Serializable id) {
        getHibernateTemplate().delete(getObject(clazz, id));
    }

    @Override
    public void delObjectAll(Collection<?> entities) {
        getHibernateTemplate().deleteAll(entities);
    }

    @Override
    public void delBaseModel(Object baseModel) {
        getHibernateTemplate().delete(baseModel);
    }

    @Override
    public Object getObject(Class<?> clazz, Serializable id) {
        Object o = getHibernateTemplate().get(clazz, id);
        return o == null ? null : o;
    }

//    @Override
//    public List<Object> getObjects(Class<Object> clazz) {
//        return getHibernateTemplate().loadAll(clazz);
//    }

    
    @Override
    public List<?> findByHql(String queryString) {
        return getHibernateTemplate().find(queryString);
    }

    @Override
    public List<?> findBySQL(final String queryString, final Class<?> clazz) {
        return (List<?>) getHibernateTemplate().execute(new HibernateCallback<Object>() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException {
                SQLQuery query = session.createSQLQuery(queryString);
                if (clazz != null) {
                    query.addEntity(clazz);
                }
                return query.list();
            }
        });
    }

    @Override
    public List<?> findBySQL(final String queryString) {
        return (List<?>) getHibernateTemplate().execute(new HibernateCallback<Object>() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException {
                Query query = session.createSQLQuery(queryString);
                return query.list();
            }
        });
    }

    @Override
    public int executeUpdateSQL(final String sql) {
        return ((Integer) this.getHibernateTemplate().execute(new HibernateCallback<Object>() {
            @Override
            @SuppressWarnings("deprecation")
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Connection conn = null;
                PreparedStatement statement = null;
                try {
                    int execResult = 0;
                    conn = session.connection();
                    log.info("【executeBySQL:】" + sql);
                    statement = conn.prepareStatement(sql);
                    execResult = statement.executeUpdate();
                    statement.close();
                    return new Integer(execResult);
                } catch (SQLException se) {
                    log.error("执行原生sql更新出错：" + sql);
                    log.error(se);
                    se.printStackTrace();
                    return new Integer(-1);
                } catch (HibernateException he) {
                    log.error("执行原生sql更新出错：" + sql);
                    log.error(he);
                    he.printStackTrace();
                    return new Integer(-1);
                } finally {
                    if (statement != null) {
                        statement.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                }
            }
        })).intValue();
    }

    @Override
    public Integer executeSqlBatch(final List<String> sqlList) {
        return (Integer) this.getHibernateTemplate().execute(new HibernateCallback<Object>() {
            @SuppressWarnings("deprecation")
            @Override
            public Object doInHibernate(Session session) throws SQLException {
                Connection conn = null;
                Statement statement = null;
                try {
                    conn = session.connection();
                    int execresult = 0;
                    conn.setAutoCommit(false);
                    statement = conn.createStatement();
                    Iterator<String> it = sqlList.iterator();
                    int i = 0;
                    while (it.hasNext()) {
                        i++;
                        String sql = it.next().toString();
                        statement.addBatch(sql);
                        if (i % 500 == 0) {
                            statement.executeBatch();
                            statement.clearBatch();
                        }
                    }
                    int[] result = statement.executeBatch();
                    execresult = sumIntArr(result);
                    statement.clearBatch();
                    conn.commit();
                    return new Integer(execresult);
                } catch (SQLException se) {
                    log.error("执行原生SQL语句出错");
                    se.printStackTrace();
                    throw se;
                } catch (HibernateException he) {
                    log.error("执行原生SQL语句出错");
                    he.printStackTrace();
                    throw he;
                } catch (RuntimeException ex) {
                    throw ex;
                } finally {
                    if (statement != null) {
                        statement.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                }
            }
        });
    }

    @Override
    public boolean save(Object obj) {
        getHibernateTemplate().save(obj);
        return true;
    }
    
    @Override
    public boolean update(Object obj) {
    	getHibernateTemplate().update(obj);
    	return true;
    }

    public static int sumIntArr(int[] intArr) {
        int result = 0;
        for (int i = 0; i < intArr.length; i++) {
            result += intArr[i];
        }
        return result;
    }

    @Override
    public void flush() {
        getHibernateTemplate().flush();
    }

    @Override
    public List<?> findAllByClass(Class<?> clazz) {
        String className = clazz.getName();
        return getHibernateTemplate().find("from " + className.substring(className.lastIndexOf(".") + 1));
    }
}
