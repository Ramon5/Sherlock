 
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Ramon
 */


public class ConnectionFactory {
    
    private Connection con;
    private final String URL = System.getProperty("user.home") + "/SherlockTM/database/SHERLOCKDB.FDB";
        
    
    
    public Connection getConnection(){
        try {            
            con = DriverManager.getConnection("jdbc:firebirdsql:localhost/3050:"+URL,"SYSDBA","masterkey");            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return con;
    }
    
    
    
}
