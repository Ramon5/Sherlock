/*
* Copyright (c) 2016, Ramon dos Santos Rodrigues
* Todos os direitos reservados.
*
* É permitida a redistribuição e o uso em formulários originais e binários, com ou sem modificação, desde que sejam cumpridas as
* seguintes condições:
*
* 1. Redistribuições do código-fonte devem manter o aviso de direitos autorais acima, esta lista de condições e a seguinte isenção.
*
* 2. As redistribuições em formato binário devem reproduzir o aviso de copyright acima, esta lista de condições ea seguinte isenção de 
* responsabilidade na documentação e / ou outros materiais fornecidos com a distribuição.
*
* 3. Nem o nome do detentor dos direitos autorais nem os nomes dos seus contribuidores podem ser utilizados para endossar ou promover 
* produtos derivados deste software sem autorização prévia específica por escrito.
*
* ESTE SOFTWARE É FORNECIDO PELOS DETENTORES DE COPYRIGHT E COLABORADORES "NO ESTADO EM QUE SE ENCONTRAM" E QUAISQUER GARANTIAS 
* EXPRESSAS OU IMPLÍCITAS, INCLUINDO, MAS NÃO SE LIMITANDO A, GARANTIAS IMPLÍCITAS DE COMERCIABILIDADE E ADEQUAÇÃO A UM PROPÓSITO 
* ESPECÍFICO. EM NENHUMA CIRCUNSTÂNCIA O PROPRIETÁRIO OU OS CONTRIBUIDORES SERÃO RESPONSÁVEIS POR QUAISQUER DANOS DIRETOS, INDIRETOS, 
* INCIDENTAIS, ESPECIAIS, EXEMPLARES OU CONSEQÜENCIAIS (INCLUINDO, MAS NÃO SE LIMITANDO À, AQUISIÇÃO DE BENS OU SERVIÇOS SUBSTITUTOS, 
* PERDA DE USO, DADOS OU LUCROS; OU INTERRUPÇÃO DE NEGÓCIOS), QUALQUER CAUSA E QUALQUER TEORIA DE RESPONSABILIDADE, SEJA POR CONTRATO, 
* RESPONSABILIDADE ESTRITA OU DANO (INCLUINDO NEGLIGÊNCIA OU QUALQUER OUTRA) DECORRENTE DE QUALQUER FORMA FORA DO USO DESTE SOFTWARE, 
* MESMO SE AVISADO DA POSSIBILIDADE DE TAIS DANOS.
*
*/
package tablemodel;

import engines.TwitterStreamCollect;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import engines.TweetTR;

public class TableModelTR extends AbstractTableModel {

    /**
     *
     */
    private static final long serialVersionUID = 7842967758725084979L;
    private final String[] colunas = {"Arquivo", "Quantidade", "Data Início", "Data Fim"};
    private final List<TwitterStreamCollect> lista;

    public TableModelTR() {
        this.lista = new ArrayList<>();
    }

    @Override
    public int getColumnCount() {
        return colunas.length;
    }

    @Override
    public int getRowCount() {
        return lista.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return colunas[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    public void setValueAt(TweetTR aValue, int rowIndex) {
        TwitterStreamCollect tw = lista.get(rowIndex);

        tw.getContainerTweet().setArquivo(aValue.getArquivo());
        tw.getContainerTweet().setQuantidade(aValue.getQuantidade());
        tw.getContainerTweet().setDataInicio(aValue.getDataInicio());
        tw.getContainerTweet().setDataFim(aValue.getDataFim());
        

        fireTableCellUpdated(rowIndex, 0);
        fireTableCellUpdated(rowIndex, 1);
        fireTableCellUpdated(rowIndex, 2);
        fireTableCellUpdated(rowIndex, 3);

    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        TwitterStreamCollect tw = lista.get(rowIndex);

        switch (columnIndex) {
            case 0:
                tw.getContainerTweet().setArquivo(aValue.toString());
            case 1:
                tw.getContainerTweet().setQuantidade(Long.parseLong(aValue.toString()));
            case 2:
                tw.getContainerTweet().setDataInicio(aValue.toString());
            case 3:
                tw.getContainerTweet().setDataInicio(aValue.toString());            
            default:
                System.err.println("Índice da coluna inválido");
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public Object getValueAt(int linha, int coluna) {
        switch (coluna) {
            case 0:
                return lista.get(linha).getContainerTweet().getArquivo();
            case 1:
                return lista.get(linha).getContainerTweet().getQuantidade();
            case 2:
                return lista.get(linha).getContainerTweet().getDataInicio();
            case 3:
                return lista.get(linha).getContainerTweet().getDataFim();
            
        }
        return null;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public void addTweet(TwitterStreamCollect tw) {
        lista.add(tw);
        int ultimoIndice = getRowCount() - 1;
        fireTableRowsInserted(ultimoIndice, ultimoIndice);
    }
    
    public Object getSelecionado(int index){
        return lista.get(index);
    }
    
    public void refreshData(int linha){
        fireTableCellUpdated(linha, 1);
    }
    
    public void atualizar(){
        fireTableDataChanged();
    }

    public void limparTabela(){
        this.lista.clear();
        fireTableDataChanged();
    }
    
}
