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
    private PreparedStatement stmt;
    private ResultSet result;

    public ColetaDAO() {
        this.con = new ConnectionFactory().getConnection();
    }

    public Coleta salvar(Coleta coleta) {
        try {
            String sql = "insert into Coleta(termo,datacoleta) values(?,?)";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, coleta.getTermo());
            stmt.setDate(2, new Date(coleta.getData().getTime()));
            stmt.execute();
            Coleta col = recuperar();
            stmt.close();

            return col;

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    public boolean excluir(Coleta coleta) {
        try {
            String sql = "delete from Coleta where idColeta = ?";
            stmt = con.prepareStatement(sql);
            stmt.setLong(1, coleta.getIdColeta());

            boolean delete = stmt.execute();
            stmt.close();

            return delete;

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    private Coleta recuperar() {
        Coleta coleta = null;
        try {
            String sql = "select * from Coleta";
            stmt = con.prepareStatement(sql);
            result = stmt.executeQuery();
            coleta = new Coleta();

            while (result.next()) {
                if (result.isLast()) {
                    coleta.setIdColeta(result.getLong("idcoleta"));
                    coleta.setTermo(result.getString("termo"));
                    coleta.setData(result.getDate("datacoleta"));
                }
            }
            stmt.close();
            result.close();

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
        return coleta;
    }

    public List<Coleta> listar() {
        List<Coleta> lista = new ArrayList<>();
        try {
            String sql = "select * from Coleta";
            stmt = con.prepareStatement(sql);
            result = stmt.executeQuery();

            while (result.next()) {
                Coleta coleta = new Coleta();
                coleta.setIdColeta(result.getLong("IDCOLETA"));
                coleta.setTermo(result.getString("TERMO"));
                coleta.setData(result.getDate("DATACOLETA"));
                lista.add(coleta);
            }

            stmt.close();
            result.close();

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
