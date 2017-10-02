
package dao;

import entidade.Coleta;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ramon
 */


public class ColetaDAO {
    
    private Connection con;
    private ConectionFactory factory;
    private PreparedStatement stmt;
    private ResultSet result;

    public ColetaDAO() {
        this.factory = new ConectionFactory();
        this.con = factory.getConnection();
    }
    
    public boolean salvar(Coleta coleta){
        try {
            String sql = "insert into Coleta(termo,datacoleta) values(?,?)";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, coleta.getTermo());
            stmt.setDate(2, (Date) coleta.getData());
            
            return stmt.execute();
            
        } catch (SQLException ex) {
            Logger.getLogger(ColetaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean excluir(Coleta coleta){
        try {
            String sql = "delete from Coleta where idColeta = ?";
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, coleta.getIdColeta());
            
            return stmt.execute();
            
        } catch (SQLException ex) {
            Logger.getLogger(ColetaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public List<Coleta> getColeta(){
        List<Coleta> lista = new ArrayList<>();
        try {            
            String sql = "select * from Coleta";
            stmt = con.prepareStatement(sql);
            result = stmt.executeQuery();
            
            while(result.next()){
                Coleta coleta = new Coleta();
                coleta.setIdColeta(result.getLong("IDCOLETA"));
                coleta.setTermo(result.getString("TERMO"));
                coleta.setData(result.getDate("DATACOLETA"));
                lista.add(coleta);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ColetaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
    
    public void closeConnection(){
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
