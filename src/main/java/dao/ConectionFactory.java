 
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ramon
 */


public class ConectionFactory {
    
    private Connection con;
    private final String url = System.getProperty("user.home") + "/SherlockTM/database/sherlockdb.fdb";
    
    public Connection getConnection(){
        try {
            Class.forName("org.firebirdsql.jdbc.FBDriver");
            con = DriverManager.getConnection("jdbc:firebirdsql:localhost/3050:/"+url,"SYSDBA","masterkey");
            
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ConectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }
    
    
    
}
