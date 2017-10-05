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
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import engines.TwitterSearch;
import entidade.Coleta;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import tablemodel.TableModelSearch;
import util.AutenticacaoAPI;
import util.DetectaSistema;
import static util.DetectaSistema.detectarSistema;
import view.SherlockGUI;

/**
 *
 * @author root
 */
public class TwitterSearchController implements DetectaSistema {

    private JScrollPane scroll;
    private JTable table;
    private TableModelSearch model;

    public TwitterSearchController(JScrollPane scroll, JTable table, TableModelSearch model) {
        this.scroll = scroll;
        this.table = table;
        this.model = model;
    }
    
    
    
    public void buscaRetroativa(String termo, int dia, boolean retroativo, boolean retweet) {
        if (AutenticacaoAPI.autenticado) {
            TwitterSearch twitter = new TwitterSearch(SherlockGUI.btnBuscar, SherlockGUI.btnLimpar, model);
            twitter.setTermo(termo);
            twitter.setLabels(SherlockGUI.lbStatus, SherlockGUI.lbQuantidade, SherlockGUI.lbData);
            //twitter.setCloud(DropBoxController.getDropBox());
            twitter.setScroll(scroll, table);
            twitter.setRetweet(retweet);

            Coleta coleta = new Coleta();
            coleta.setTermo(termo);
            coleta.setData(Calendar.getInstance().getTime());
            ColetaDAO cDAO = new ColetaDAO();
            coleta = cDAO.salvar(coleta);
            cDAO.closeConnection();

            twitter.setColeta(coleta);

            if (retroativo) {
                twitter.setLimite(getDataLimite(dia));
            } else {
                twitter.setLimite(getDataAtual());
            }
            twitter.start();
        }else{
            JOptionPane.showMessageDialog(null, "Você precisa de credencias para efetuar as coletas!");
        }

    }

    public static void irParaSite() {
        if (detectarSistema() == 0) {
            String[] link = new String[]{"/usr/bin/firefox", "http://apps.twitter.com/"};
            try {
                Runtime.getRuntime().exec(link);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
            }
        } else if (detectarSistema() == 1) {
            try {
                Desktop.getDesktop().browse(new URI("http://apps.twitter.com/"));
            } catch (URISyntaxException ex) {
                JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
            }
        }
    }

    /**
     * Definindo a partir de qual dia a coleta será abrangida (Coleta
     * Retroativa)
     *
     * @return
     */
    private Date getDataLimite(int dia) {
        Calendar dataLimite = Calendar.getInstance();
        dataLimite.add(Calendar.DAY_OF_MONTH, -(dia));
        return dataLimite.getTime();
    }

    private Date getDataAtual() {
        Calendar hoje = Calendar.getInstance();
        return hoje.getTime();
    }
}
