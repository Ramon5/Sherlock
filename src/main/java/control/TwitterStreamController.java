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
import util.Mensagem;
import engines.TwitterStreamCollect;
import entidade.Coleta;
import inativos.GerenciadorDiretorios;
import static util.ManipuladorTabela.manipuladorTR;
import view.SherlockGUI;
import static view.SherlockGUI.btnColetaReal;
import static view.SherlockGUI.btnStop;
import static view.SherlockGUI.lbStatusReal;
import static view.SherlockGUI.tableTR;
import static view.SherlockGUI.tableView;

/**
 *
 * @author root
 */
public class TwitterStreamController {

    private TwitterStreamCollect coleta;
    private static List<TwitterStreamCollect> ativos;

    public static void instanciarAtivos() {
        ativos = new ArrayList<>();
    }

    public static void criarArquivoStream(String termo) {
        GerenciadorDiretorios diretorio = new GerenciadorDiretorios(termo, true);
        diretorio.criaArquivoColeta();
        TwitterStreamCollect stream = new TwitterStreamCollect(termo);
        stream.setContainer(diretorio.getContainer());
        stream.setScroll(SherlockGUI.scrollPainel, SherlockGUI.tableView);
        stream.setStatus(SherlockGUI.lbStatusReal);
        manipuladorTR.addTweet(stream);
        SherlockGUI.campoContainer.setText(null);
        SherlockGUI.btnStop.setEnabled(true);
        btnColetaReal.setEnabled(false);
        verificarArquivo(stream);
    }

    public void coletarStreams(Coleta col) {
        if (coleta != null && !coleta.getContainerTweet().isAtivo()) {
            ColetaDAO cDAO = new ColetaDAO();
            col = cDAO.salvar(col);
            cDAO.closeConnection();
            coleta.setColeta(col);
            coleta.collectRealTime();
            verificarArquivo(coleta);
        }
    }

    private static void verificarArquivo(TwitterStreamCollect coleta) {
        if (coleta != null) {
            if (coleta.getContainerTweet().isAtivo()) {
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
            coleta = (TwitterStreamCollect) manipuladorTR.getSelecionado(tableTR.getSelectedRow());
            tableView.setModel(coleta.getTabelaTweets());
            verificarArquivo(coleta);

        }
    }

    private static void verificarAtivos() {
        for (int i = 0; i < tableTR.getRowCount(); i++) {
            TwitterStreamCollect obj = (TwitterStreamCollect) manipuladorTR.getSelecionado(i);
            ativos.add(obj);
        }
        int contador = 0;
        for (TwitterStreamCollect t : ativos) {
            if (!t.getContainerTweet().isAtivo()) {
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
        String text = System.getProperty("user.home") + "/SherlockTM/Dataset/";
        msg = new Mensagem(null, true, text);
        msg.setTitulo("Tweet Stream");
        msg.setLocationRelativeTo(null);
        msg.setVisible(true);

    }

    public void pararColeta() {
        if (coleta != null) {
            coleta.finalizar();
            verificarAtivos();
            verificarArquivo(coleta);
        }
    }

}
