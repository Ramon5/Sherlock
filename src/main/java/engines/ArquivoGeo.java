
package engines;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tux
 */


public class ArquivoGeo implements Serializable{
    
    public static final long serialVersionUID = 2806421523585360625L;
    private String termo;
    private Long quantidade;
    private Long total;
    private final List<String> coordenadas;

    public ArquivoGeo() {
        coordenadas = new ArrayList<>();
    }

    public String getTermo() {
        return termo;
    }

    public void setTermo(String termo) {
        this.termo = termo;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }

    public List<String> getCoordenadas() {
        return coordenadas;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }   

    public void addCoordenadas(String coordenadas) {
        this.coordenadas.add(coordenadas);
    }
    
    
}
