/*
 * Copyright 2017 root.
 * Todos os direitos reservados.
 *
 * É permitida a redistribuição e o uso em formulários originais e binários, com ou
 * sem modificação, desde que sejam cumpridas as seguintes condições:
 *
 *  * Redistribuições do código-fonte devem manter o aviso de direitos autorais acima, 
 *   esta lista de condições e a seguinte isenção.
 *
 *  * As redistribuições em formato binário devem reproduzir o aviso de copyright acima,
 *   esta lista de condições ea seguinte isenção de responsabilidade na documentação
 *   e / ou outros materiais fornecidos com a distribuição.
 *
 *  * Nem o nome do Expression project.organization!organization is undefined on line 17, column 36 in Templates/Licenses/license-bsd_3-pt_br.txt. nem os nomes dos seus contribuidores 
 *   podem ser utilizados para endossar ou promover produtos derivados deste software sem
 *   autorização prévia específica por escrito.
 *
 * ESTE SOFTWARE É FORNECIDO PELOS DETENTORES DE COPYRIGHT E COLABORADORES "NO ESTADO EM QUE SE ENCONTRAM"
 * E QUAISQUER GARANTIAS EXPRESSAS OU IMPLÍCITAS, INCLUINDO, MAS NÃO SE LIMITANDO A,
 * GARANTIAS IMPLÍCITAS DE COMERCIABILIDADE E ADEQUAÇÃO A UM PROPÓSITO ESPECÍFICO.
 * EM NENHUMA CIRCUNSTÂNCIA O PROPRIETÁRIO OU OS CONTRIBUIDORES SERÃO
 * RESPONSÁVEIS POR QUAISQUER DANOS DIRETOS, INDIRETOS, INCIDENTAIS, ESPECIAIS, EXEMPLARES OU
 * CONSEQÜENCIAIS (INCLUINDO, MAS NÃO SE LIMITANDO À, AQUISIÇÃO DE
 * BENS OU SERVIÇOS SUBSTITUTOS, PERDA DE USO, DADOS OU LUCROS; OU INTERRUPÇÃO DE NEGÓCIOS),
 * QUALQUER CAUSA E QUALQUER TEORIA DE RESPONSABILIDADE,  
 * SEJA POR CONTRATO, RESPONSABILIDADE ESTRITA OU DANO (INCLUINDO NEGLIGÊNCIA OU QUALQUER OUTRA)
 * DECORRENTE DE QUALQUER FORMA FORA DO USO DESTE SOFTWARE, MESMO SE AVISADO DA 
 * POSSIBILIDADE DE TAIS DANOS.
 */
package tablemodel;

import entidade.Tweet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author root
 */
public class TableModelTweet extends AbstractTableModel{

    private final String[] colunas = {"ID TWEET", "TWEET","USUÁRIO","ID USUÁRIO","DATA"};
    private final List<Tweet> lista;

    public TableModelTweet() {
        this.lista = new ArrayList<>();
    }    
    
    @Override
    public int getRowCount() {
        return lista.size();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        return colunas[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex){
            case 0:
                return lista.get(rowIndex).getIdTweet();
            case 1:
                return lista.get(rowIndex).getTweet();
            case 2:
                return lista.get(rowIndex).getAutor();
            case 3:
                return lista.get(rowIndex).getIdUsuario();
            case 4:
                return lista.get(rowIndex).getDatecreated();
            
        }
        return null;
    }
   
    
    
    public void setValueAt(Tweet aValue, int rowIndex) {
        Tweet tweet = lista.get(rowIndex); 
        
        tweet.setIdTweet(aValue.getIdTweet());
        tweet.setTweet(aValue.getTweet());
        tweet.setAutor(aValue.getAutor());
        tweet.setIdUsuario(aValue.getIdUsuario());
        tweet.setDatecreated(aValue.getDatecreated());
        
        fireTableCellUpdated(rowIndex, 0);
        fireTableCellUpdated(rowIndex, 1);
        fireTableCellUpdated(rowIndex, 2);
        fireTableCellUpdated(rowIndex, 3);
        fireTableCellUpdated(rowIndex, 4);

    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Tweet tweet = lista.get(rowIndex);

        switch (columnIndex) {
            case 0:
                tweet.setIdTweet(Long.parseLong(aValue.toString()));
            case 1:
                tweet.setTweet(aValue.toString());
            case 2:
                tweet.setAutor(aValue.toString());
            case 3:
                tweet.setIdUsuario(Long.parseLong(aValue.toString()));
            case 4:
                Date data = (Date) aValue;
                tweet.setDatecreated(data);
                
            default:
                System.err.println("Índice da coluna inválido");
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    public Object getSelecionado(int rowIndex){
        return lista.get(rowIndex);
    }

    public void addTweet(Tweet tweet) {
        lista.add(tweet);
        int ultimoIndice = getRowCount() - 1;
        fireTableRowsInserted(ultimoIndice, ultimoIndice);
    }

    public void limparTabela(){
        this.lista.clear();
        fireTableDataChanged();
    }
    
    public void atualizar(){
        fireTableDataChanged();
    }
    
}
