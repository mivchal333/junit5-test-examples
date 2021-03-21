/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import exception.DataAccessException;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionFactory {

    private static HikariConfig config;
    private static HikariDataSource ds;

    static {
        // odczytanie pliku konfiguracyjnego z classpath
        config = new HikariConfig("/hikari.properties");
        ds = new HikariDataSource(config);

    }

    public static Connection getConnection() {
        try {
            return ds.getConnection();
        } catch (SQLException ex) {
            throw new DataAccessException(ex);
        }
    }

}
