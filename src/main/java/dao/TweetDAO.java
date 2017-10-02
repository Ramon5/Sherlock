
package dao;

import entidade.Coleta;
import entidade.Tweet;
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


public class TweetDAO {
    
    private Connection conection;
    private ConectionFactory factory;
    private PreparedStatement stmt;
    private ResultSet result;

    public TweetDAO() {
        this.factory = new ConectionFactory();        
    }
    
    public boolean salvar(Tweet tweet){
        this.conection = factory.getConnection();
        try {
            String sql = "insert into TWEET(idtweet,coleta_idcoleta,tweet,to_user_id,screenname,user_id,favorite_count,created_at,lang) values(?,?,?,?,?,?,?,?,?)";
            stmt = conection.prepareStatement(sql);
            stmt.setLong(1, tweet.getIdTweet());
            stmt.setLong(2, tweet.getColeta().getIdColeta());
            stmt.setString(3, tweet.getTweet());
            stmt.setLong(4, tweet.getTo_user_id());
            stmt.setString(5, tweet.getAutor());
            stmt.setLong(6, tweet.getIdUsuario());
            stmt.setLong(7, tweet.getFavorite_count());
            stmt.setDate(8, new Date(tweet.getDatecreated().getTime()));
            stmt.setString(9, tweet.getLang());
                        
            return stmt.execute();
            
        } catch (SQLException ex) {
            Logger.getLogger(TweetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    
    public List<Tweet> getTweets(Coleta coleta){
        this.conection = factory.getConnection();
        List<Tweet> lista = new ArrayList<>();
        try {
            String sql = "select * from tweet as t inner join coleta as c on (t.coleta_idcoleta = c.idcoleta) where c.idcoleta = ?";
            stmt = conection.prepareStatement(sql);
            stmt.setLong(1, coleta.getIdColeta());
            result = stmt.executeQuery();
            
            while(result.next()){
                Tweet t = new Tweet();
                t.setIdTweet(result.getLong("idtweet"));
                t.setTweet(result.getString("tweet"));
                t.setTo_user_id(result.getLong("to_user_id"));
                t.setAutor(result.getString("screename"));
                t.setIdUsuario(result.getLong("user_id"));
                t.setFavorite_count(result.getLong("favorite_count"));
                t.setDatecreated(result.getDate("created_at"));
                t.setLang(result.getString("lang"));
                
                lista.add(t);
                
            }
            return lista;
            
        } catch (SQLException ex) {
            Logger.getLogger(TweetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
    
    public void closeConnection(){
        try {
            conection.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
