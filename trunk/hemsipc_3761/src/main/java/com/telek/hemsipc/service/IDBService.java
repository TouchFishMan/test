package com.telek.hemsipc.service;

import java.sql.SQLException;
import java.util.List;

/**
 * @Auther: wll
 * @Date: 2018/9/25 11:16
 * @Description:
 */
public interface IDBService {
    void executeSqls(List<String> sqls) throws SQLException;

    void dbbackup() throws SQLException;

    String getDbType();
}
