package com;

import com.sun.org.apache.regexp.internal.RE;
import com.sun.rowset.CachedRowSetImpl;

import javax.imageio.ImageIO;
import javax.sql.RowSet;
import javax.sql.RowSetEvent;
import javax.sql.RowSetListener;
import javax.sql.RowSetMetaData;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import javax.sql.rowset.RowSetWarning;
import javax.sql.rowset.spi.SyncProvider;
import javax.sql.rowset.spi.SyncProviderException;
import javax.swing.plaf.nimbus.State;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;

public class Main {
    static String userName = "root";
    static String password = "sql123";
    static String connectionUrl = "jdbc:mysql://localhost:3306/test?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";


    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        ResultSet resultSet = getData();
        while (resultSet.next()) {
            System.out.println(resultSet.getInt("id"));
            System.out.println(resultSet.getString("name"));
        }
        CachedRowSet cachedRowSet = (CachedRowSet) resultSet;
        cachedRowSet.setUrl(connectionUrl);
        cachedRowSet.setUsername(userName);
        cachedRowSet.setPassword(password);
//        cachedRowSet.setCommand("select * from Books where id = ?");
//        cachedRowSet.setInt(1,1);
//        cachedRowSet.setPageSize(20);
//        cachedRowSet.execute();
//        do{
//            while (cachedRowSet.nextPage()){
//                System.out.println(cachedRowSet.getInt("id"));
//                System.out.println(cachedRowSet.getString("name"));
//            }
//        }while (cachedRowSet.nextPage());
        CachedRowSet cachedRowSet1 = (CachedRowSet) resultSet;
        cachedRowSet1.setTableName("Books");
        cachedRowSet1.absolute(1);
        cachedRowSet1.deleteRow();
        while (cachedRowSet1.next()) {
            System.out.println(cachedRowSet1.getInt("id"));
            System.out.println(cachedRowSet1.getString("name"));
        }
     //   cachedRowSet1.acceptChanges(DriverManager.getConnection(connectionUrl,userName,password));
      //  cachedRowSet1.acceptChanges();
    }


    static ResultSet getData() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        try (Connection conn = DriverManager.getConnection(connectionUrl, userName, password);
             Statement stat = conn.createStatement()) {

            stat.execute("drop table IF EXISTS Books");
            stat.executeUpdate("CREATE TABLE Books(id MEDIUMINT NOT NULL AUTO_INCREMENT,name CHAR(30) NOT NULL,dt DATE, PRIMARY KEY(id))");
            stat.executeUpdate("INSERT INTO Books(name)VALUES ('Inferno')");
            stat.executeUpdate("INSERT INTO Books(name)VALUES ('Solomon Kein')", Statement.RETURN_GENERATED_KEYS);
            stat.executeUpdate("INSERT INTO Books(name)VALUES ('Ivan Govnov')");
            stat.executeUpdate("INSERT INTO Books(name)VALUES ('Moja borba')");
            stat.executeUpdate("INSERT INTO Books(name)VALUES ('Voina i mir')");
            stat.executeUpdate("INSERT INTO Books(name)VALUES ('KZOT')");

            RowSetFactory factory = RowSetProvider.newFactory();
            CachedRowSet cachedRowSet = factory.createCachedRowSet();

            Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Books");
            cachedRowSet.populate(resultSet);
            return cachedRowSet;
        }
    }
}

// CREATE TABLE IF NOT EXISTS Book (id MEDIUMINT NOT NULL AUTO_INCREMENT, name CHAR(30) NOT NULL, PRIMARY KEY (id))























