package zelora.data;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    static public Connection connection;
    static public QueryRunner runner;

    public static void doConnection(){
        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/zelora", "root", "");
            runner = new QueryRunner();
        } catch (SQLException ex){
            System.out.println(ex);
        }
    }//end doConnection

    public static void closeConnection(){
        try{
            DbUtils.close(connection);

        }catch (SQLException ex){
            System.out.println(ex);
        }
    }

}
