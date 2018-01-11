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
package view;

import dao.ColetaDAO;
import dao.TweetDAO;
import graficos.GraficoPizza;
import entidade.Coleta;
import entidade.Tweet;
import graficos.Grafico;
import graficos.GraficoBarras;
import graficos.GraficoLinhas;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import tablemodel.TableModelColeta;
import tablemodel.TableModelTweet;
import util.SQL;

/**
 *
 * @author root
 */
public class ColetasGUI extends javax.swing.JDialog {

    private TableModelColeta modelColeta;
    private TableModelTweet modelTweet;
    private ColetaDAO colDAO;
    private List<Coleta> coletas;
    private TweetDAO tweetDAO;
    private List<Tweet> tweets;

    /**
     * Creates new form ColetasGUI
     */
    public ColetasGUI(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        modelColeta = new TableModelColeta();
        modelTweet = new TableModelTweet();
        initComponents();
        tweetDAO = new TweetDAO();
        colDAO = new ColetaDAO();
        coletas = colDAO.listar();
        colDAO.closeConnection();
        preencherColetas(coletas);
    }

    private void preencherColetas(List<Coleta> list) {
        if (list != null && list.size() > 0) {
            for (Coleta c : list) {
                modelColeta.addColeta(c);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lbTotal = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableColeta = new javax.swing.JTable();
        painelPizza = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        painelBarra = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Qtd. Total de Tweets:");
        jPanel3.add(jLabel1);

        lbTotal.setForeground(new java.awt.Color(0, 0, 204));
        lbTotal.setText("0");
        jPanel3.add(lbTotal);

        jPanel4.setBackground(new java.awt.Color(51, 51, 51));
        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 20));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/coletas.png"))); // NOI18N
        jPanel4.add(jLabel2);

        jLabel4.setText("Coletas:");

        tableColeta.setModel(modelColeta);
        tableColeta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableColetaMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tableColeta);

        painelPizza.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        painelPizza.setLayout(new java.awt.BorderLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jButton1.setText("Exibir Coletas");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1);

        painelBarra.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        painelBarra.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 866, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jSeparator1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(painelBarra, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(painelPizza, javax.swing.GroupLayout.PREFERRED_SIZE, 393, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(painelPizza, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(painelBarra, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tableColetaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableColetaMouseClicked
        calcular();
    }//GEN-LAST:event_tableColetaMouseClicked

    /*private void exibirTweets() {
        modelTweet.limparTabela();
        Coleta coleta = (Coleta) modelColeta.getSelecionado(tableColeta.getSelectedRow());
        if (coleta != null) {
            tweets = tweetDAO.getTweets(coleta);
            if (tweets != null && tweets.size() > 0) {
                for (Tweet t : tweets) {
                    modelTweet.addTweet(t);
                }
            }
            calcular();
            tweets.clear();
        }
    }*/
    private void calcular() {
        Coleta coleta = (Coleta) modelColeta.getSelecionado(tableColeta.getSelectedRow());
        tweets = tweetDAO.getTweets(coleta, SQL.TWEETS);

        if (coleta != null) {
            int qtdTotal = tweets.size();
            int qtdOriginais = tweetDAO.getCount(SQL.COUNT_ORIG, coleta, 0);
            int qtdGeo = tweetDAO.getCount(SQL.GEO, coleta, 0);
            int qtdReply = tweetDAO.getCount(SQL.REPLY, coleta, 0);            
            int qtdRetweets = qtdTotal - qtdOriginais;
            plotar(qtdTotal, qtdOriginais, qtdRetweets, qtdGeo, qtdReply);

        }
    }

    private void plotar(int qtdTotal, int qtdOriginais, int qtdRetweets, int qtdGeo, int qtdReply) {
        lbTotal.setText(String.valueOf(qtdTotal));

        Grafico grafico = new GraficoPizza();

        grafico.addValue("originais", qtdOriginais);
        grafico.addValue("retweets", qtdRetweets);
        if (qtdReply > 0) {
            grafico.addValue("respostas", qtdReply);
        }
        if (qtdGeo > 0) {
            grafico.addValue("georeferenciados", qtdGeo);
        }
        grafico.saveDataset(null);
        JPanel graf = grafico.criarGrafico();
        painelPizza.removeAll();
        painelPizza.add(graf, BorderLayout.CENTER);
        graf.setVisible(true);
        painelPizza.updateUI();

        Grafico bar = new GraficoBarras();

        bar.addValue("originais", qtdOriginais);
        bar.addValue("retweets", qtdRetweets);
        if (qtdReply > 0) {
            bar.addValue("respostas", qtdReply);
        }
        if (qtdGeo > 0) {
            bar.addValue("georeferenciados", qtdGeo);
        }

        bar.saveDataset("tweets");
        JPanel barra = bar.criarGrafico();
        painelBarra.removeAll();
        painelBarra.add(barra, BorderLayout.CENTER);
        barra.setVisible(true);
        painelBarra.updateUI();
    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (tweetDAO != null) {
            tweetDAO.closeConnection();
        }
    }//GEN-LAST:event_formWindowClosing

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        modelTweet.limparTabela();
        Coleta coleta = (Coleta) modelColeta.getSelecionado(tableColeta.getSelectedRow());
        if (coleta != null) {
            tweets = tweetDAO.getTweets(coleta, SQL.TWEETS);
            if (tweets != null && tweets.size() > 0) {
                for (Tweet t : tweets) {
                    modelTweet.addTweet(t);
                }
            }
            TweetsGUI tgui = new TweetsGUI(modelTweet, coleta, tweetDAO);
            tgui.setLocationRelativeTo(this);
            tgui.setVisible(true);
            tweets.clear();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ColetasGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ColetasGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ColetasGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ColetasGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(() -> {
            ColetasGUI dialog = new ColetasGUI(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lbTotal;
    private javax.swing.JPanel painelBarra;
    private javax.swing.JPanel painelPizza;
    private javax.swing.JTable tableColeta;
    // End of variables declaration//GEN-END:variables
}
