
package entidade;

import java.util.Date;

/**
 *
 * @author Ramon
 */


public class Coleta {
    
    private Long idColeta;
    private String termo;
    private Date data;

    public Long getIdColeta() {
        return idColeta;
    }

    public void setIdColeta(Long idColeta) {
        this.idColeta = idColeta;
    }

    public String getTermo() {
        return termo;
    }

    public void setTermo(String termo) {
        this.termo = termo;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
    
    @Override
    public String toString(){
        return termo + " - " + data;
    }
    
}
