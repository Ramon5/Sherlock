package dao;

import entidade.Autenticacao;
import entidade.Chave;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.GeradorCredencial;

/**
 *
 * @author Ramon
 */
public class ChaveDAO {

    private Connection con;
    private PreparedStatement stmt;
    private ResultSet result;

    public ChaveDAO() {
        this.con = new ConnectionFactory().getConnection();
    }

    public void salvar(Chave chave) {
        try {
            String sql = "insert into Chave(Autorizacao_idAuth,consumer_key,consumer_secret) values(?,?,?)";
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, chave.getAutenticacao().getIdAuth());
            stmt.setBytes(2, new GeradorCredencial().criptografar(chave.getConsumerKey()));
            stmt.setBytes(3, new GeradorCredencial().criptografar(chave.getConsumerSecret()));
            stmt.execute();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public List<Chave> listar(Autenticacao auth) {
        List<Chave> lista = new ArrayList<>();
        try {
            String sql = "select * from Chave as c inner join Autorizacao as a on (c.Autorizacao_idAuth = a.idAuth) where a.idAuth = ?";
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, auth.getIdAuth());
            result = stmt.executeQuery();
            while (result.next()) {
                Chave chave = new Chave();
                chave.setIdChave(result.getLong("idChave"));
                chave.setConsumerKey(new GeradorCredencial().decriptar(result.getBytes("consumer_key"), GeradorCredencial.getKey()));
                chave.setConsumerSecret(new GeradorCredencial().decriptar(result.getBytes("consumer_secret"), GeradorCredencial.getKey()));
                lista.add(chave);
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                if(stmt != null){
                    stmt.close();
                }
                if(result != null){
                    result.close();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        return lista;
    }
    
    public void closeConnection() {
        try {
            con.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
