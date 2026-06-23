package ConnectDatabase;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;

public class DBConnect {

    public static Connection getConnection() {
        var server = "localhost"; 
        var instanceName = "SQLEXPRESS"; 
        var user = "sa";
        var password = "KazutoCuong"; 
        var db = "SelfRestaurant";
        var port = 1433;

        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setUser(user);
        ds.setPassword(password);
        ds.setDatabaseName(db);
        ds.setServerName(server);
        ds.setInstanceName(instanceName);
        ds.setPortNumber(1433);
        
        ds.setTrustServerCertificate(true);
        ds.setEncrypt(true); 

        try {
            return ds.getConnection();
        } catch (SQLServerException ex) {
            System.err.println("KẾT NỐI THẤT BẠI! Chi tiết lỗi:");
            ex.printStackTrace();
            return null;
        }
    }
}