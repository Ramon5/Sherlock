/*
 * Copyright 2017 root.
 * All rights reserved.
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
 *  * Nem o nome do Expression project.organization is undefined on line 17, column 36 in Templates/Licenses/license-bsd_3-pt_br.txt. nem os nomes dos seus contribuidores 
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
package control;

import dao.ColetaDAO;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import view.Mensagem;
import engines.TwitterStreamCollect;
import entidade.Chave;
import entidade.Coleta;
import entidade.TweetStream;
import java.util.Calendar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import tablemodel.TableModelSearch;
import tablemodel.TableModelStream;
import view.SherlockGUI;
import static view.SherlockGUI.btnColetaReal;
import static view.SherlockGUI.btnStop;
import static view.SherlockGUI.lbStatusReal;
import static view.SherlockGUI.tableTR;

/**
 *
 * @author root
 */
public class TwitterStreamController {

    
    private TwitterStreamCollect arquivoStream;
    private static List<TwitterStreamCollect> ativos;
    private JScrollPane scroll;
    private JTable tableView;

    public TwitterStreamController(JScrollPane scroll, JTable tableView) {
        this.scroll = scroll;
        this.tableView = tableView;
        
    }
    
    

    public static void instanciarAtivos() {
        ativos = new ArrayList<>();
    }

    public void criarArquivoStream(String termo, Chave chave, Coleta coleta) {
        ColetaDAO cDAO = new ColetaDAO();
        coleta = cDAO.salvar(coleta);
        cDAO.closeConnection();
        TweetStream tweet = new TweetStream();
        tweet.setTermo(termo);  
        
        TwitterStreamCollect stream = new TwitterStreamCollect(termo);
        stream.setContainer(tweet);
        stream.setScroll(scroll, tableView);
        stream.setStatus(SherlockGUI.lbStatusReal);
        stream.setChave(chave);
        stream.setColeta(coleta);
        TableModelSearch tabelaTweets = new TableModelSearch();
        stream.setTabelaTweets(tabelaTweets);
        
        SherlockGUI.modelStream.addTweet(stream);
        SherlockGUI.btnStop.setEnabled(true);
        btnColetaReal.setEnabled(false);
        verificarArquivo(stream);
    }

    public void coletarStreams() {
        if (arquivoStream != null && !arquivoStream.getArquivoTweet().isAtivo()) { 
            Thread tStream = new Thread(arquivoStream);
            tStream.start();
            verificarArquivo(arquivoStream);
        }
    }

    private void verificarArquivo(TwitterStreamCollect coleta) {
        if (coleta != null) {
            if (coleta.getArquivoTweet().isAtivo()) {
                btnStop.setEnabled(true);
                btnColetaReal.setEnabled(false);
            } else {
                btnStop.setEnabled(false);
                btnColetaReal.setEnabled(true);
            }
        }
    }

    public void cliqueTBStream(MouseEvent event) {
        if (event.getClickCount() == 1) {
            arquivoStream = (TwitterStreamCollect) SherlockGUI.modelStream.getSelecionado(tableTR.getSelectedRow());
            tableView.setModel(arquivoStream.getTabelaTweets());
            verificarArquivo(arquivoStream);

        }
    }

    /**
     * Quando o usuário pedir para encerrar uma captura stream
     */
    private void verificarAtivos() {
        for (int i = 0; i < tableTR.getRowCount(); i++) {
            TwitterStreamCollect obj = (TwitterStreamCollect) SherlockGUI.modelStream.getSelecionado(i);
            ativos.add(obj);
        }
        int contador = 0;
        for (TwitterStreamCollect t : ativos) {
            if (!t.getArquivoTweet().isAtivo()) {
                contador++;
            }
        }

        if (contador == ativos.size()) {
            lbStatusReal.setText("Finalizado");
            mensagem();
        }

        ativos.clear();
    }

    /**
     * Informativo do término da coleta
     */
    private static void mensagem() {
        Mensagem msg;
        msg = new Mensagem(null, true);
        msg.setTitulo("Tweet Stream");
        msg.setLocationRelativeTo(null);
        msg.setVisible(true);

    }

    public void pararColeta() {
        if (arquivoStream != null) {
            arquivoStream.finalizar();
            verificarAtivos();
            verificarArquivo(arquivoStream);
        }
    }

}
