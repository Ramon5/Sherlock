package dao;

import entidade.Autenticacao;
import java.sql.Connection;
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
public class AuthDAO {

    private Connection con;
    private PreparedStatement stmt;
    private ResultSet result;

    public AuthDAO() {
        this.con = new ConnectionFactory().getConnection();
    }

    public Autenticacao salvar(Autenticacao auth) {
        try {
            String sql = "insert into Autorizacao(chave) values(?)";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, auth.getChave());
            stmt.execute();
            Autenticacao aut = recuperar();
            
            return aut;

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

    private Autenticacao recuperar() {
        try {
            String sql = "select * from Autorizacao";
            Autenticacao aut = new Autenticacao();
            stmt = con.prepareStatement(sql);
            result = stmt.executeQuery();
            while (result.next()) {
                if (result.isLast()) {
                    aut.setIdAuth(result.getLong("idAuth"));
                    aut.setChave(result.getString("chave"));
                }
            }
            return aut;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (result != null) {
                    result.close();
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void excluir(Autenticacao auth) {
        try {
            String sql = "delete from Autorizacao where idAuth = ?";
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, auth.getIdAuth());
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

    public List<Autenticacao> listar() {
        List<Autenticacao> lista = new ArrayList<>();
        try {
            String sql = "select * from Autorizacao";
            stmt = con.prepareStatement(sql);
            result = stmt.executeQuery();
            while (result.next()) {
                Autenticacao a = new Autenticacao();
                a.setIdAuth(result.getLong("idAuth"));
                a.setChave(result.getString("chave"));
                lista.add(a);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AuthDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (result != null) {
                    result.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
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
