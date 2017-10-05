package entidade;

/**
 *
 * @author Ramon
 */


public class Chave {
    
    private Long idChave;
    private String consumerKey;
    private String consumerSecret;
    private Autenticacao autenticacao;

    public Long getIdChave() {
        return idChave;
    }

    public void setIdChave(Long idChave) {
        this.idChave = idChave;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }
    
    public Autenticacao getAutenticacao() {
        return autenticacao;
    }

    public void setAutenticacao(Autenticacao autenticacao) {
        this.autenticacao = autenticacao;
    }
    
    
    
}
