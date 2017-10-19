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
package view;

import control.TwitterSearchController;
import control.TwitterStreamController;
import dao.AuthDAO;
import dao.ChaveDAO;
import entidade.Autenticacao;
import entidade.Chave;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import entidade.Coleta;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import tablemodel.TableModelMeta;
import tablemodel.TableModelStream;
import util.AutenticacaoAPI;
import util.DetectaSistema;

/**
 *
 * @author root
 */
public class SherlockGUI extends javax.swing.JFrame implements DetectaSistema {

    private TwitterStreamController controlStream;
    private TwitterSearchController searchControl;
    public static List<Chave> keys;
    private ChaveDAO cDAO;
    private AuthDAO aDAO;
    private TableModelMeta modelSearch;
    public static TableModelStream modelStream;
    private TableModelMeta modelView;

    /**
     * Creates new form SherlockGUI
     */
    public SherlockGUI() {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        modelSearch = new TableModelMeta();
        modelStream = new TableModelStream();
        modelView = new TableModelMeta();
        initComponents();
        keys = new ArrayList<>();
        cDAO = new ChaveDAO();
        aDAO = new AuthDAO();
        obterChaves();
        TwitterStreamController.instanciarAtivos();
        controlStream = new TwitterStreamController(scrollPainel, tableView);

    }

    private void obterChaves() {
        List<Autenticacao> auth = aDAO.listar();
        if (auth.size() > 0) {
            for (Autenticacao a : auth) {
                List<Chave> chaves = cDAO.listar(a);
                for (Chave c : chaves) {
                    keys.add(c);
                }
            }
            AutenticacaoAPI.indiceChave = 0;
            AutenticacaoAPI.appAutentication(keys.get(0));
        } else {
            JOptionPane.showMessageDialog(this, "Você não possui credenciais!");
        }
        cDAO.closeConnection();
        aDAO.closeConnection();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem4 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        lbLogo = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        campoBusca = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        lbTwitter = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        chckbxColetaRetroativa = new javax.swing.JCheckBox();
        cbDia = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        chkRetweet = new javax.swing.JCheckBox();
        painelColeta = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        scroll = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        campoContainer = new javax.swing.JTextField();
        btnCriaArquivo = new javax.swing.JButton();
        scrollTR = new javax.swing.JScrollPane();
        tableTR = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        btnColetaReal = new javax.swing.JButton();
        btnStop = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        lbStatusReal = new javax.swing.JLabel();
        scrollPainel = new javax.swing.JScrollPane();
        tableView = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        lbTwitter1 = new javax.swing.JLabel();
        painelCloudReal = new javax.swing.JPanel();
        lbCloudReal = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuOpcoes = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        menuSair = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        menuSobre = new javax.swing.JMenuItem();

        jMenuItem4.setText("jMenuItem4");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SherlockTM v1.1");
        setName("frame"); // NOI18N

        jPanel1.setBackground(new java.awt.Color(64, 64, 64));
        java.awt.FlowLayout flowLayout3 = new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 20);
        flowLayout3.setAlignOnBaseline(true);
        jPanel1.setLayout(flowLayout3);

        lbLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Logo-TM.png"))); // NOI18N
        jPanel1.add(lbLogo);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setText("Palavra-Chave:");

        campoBusca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                campoBuscaKeyPressed(evt);
            }
        });

        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/pick.png"))); // NOI18N
        btnBuscar.setText("Coletar");
        btnBuscar.setToolTipText("Clique aqui para iniciar a coleta");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        lbTwitter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Twitter-icon.png"))); // NOI18N
        lbTwitter.setToolTipText("Ir para apps.twitter.com");
        lbTwitter.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbTwitterMouseClicked(evt);
            }
        });
        jPanel3.add(lbTwitter);

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 10);
        flowLayout1.setAlignOnBaseline(true);
        jPanel5.setLayout(flowLayout1);

        chckbxColetaRetroativa.setSelected(true);
        chckbxColetaRetroativa.setText("Coleta Retroativa");
        chckbxColetaRetroativa.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chckbxColetaRetroativaItemStateChanged(evt);
            }
        });
        jPanel5.add(chckbxColetaRetroativa);

        cbDia.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "7", "6", "5", "4", "3", "2" }));
        jPanel5.add(cbDia);

        jLabel2.setText("Dias");
        jPanel5.add(jLabel2);

        btnLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/clear.png"))); // NOI18N
        btnLimpar.setText("Limpar");
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });
        jPanel5.add(btnLimpar);

        chkRetweet.setSelected(true);
        chkRetweet.setText("retweet");
        jPanel5.add(chkRetweet);

        painelColeta.setLayout(new java.awt.GridLayout(3, 2));

        jLabel3.setText("Status:");
        painelColeta.add(jLabel3);

        lbStatus.setForeground(new java.awt.Color(0, 0, 255));
        lbStatus.setText("...");
        painelColeta.add(lbStatus);

        jLabel4.setText("Tweets:");
        painelColeta.add(jLabel4);

        lbQuantidade.setForeground(new java.awt.Color(0, 0, 204));
        lbQuantidade.setText("0");
        painelColeta.add(lbQuantidade);

        jLabel5.setText("Última data disponível:");
        painelColeta.add(jLabel5);

        lbData.setForeground(new java.awt.Color(0, 51, 255));
        lbData.setText("...");
        painelColeta.add(lbData);

        table.setModel(modelSearch);
        scroll.setViewportView(table);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(painelColeta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(campoBusca, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnBuscar))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 603, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 864, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(109, 109, 109))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(campoBusca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBuscar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(painelColeta, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(217, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                    .addGap(297, 297, 297)
                    .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        jTabbedPane1.addTab("Twitter Search", jPanel2);

        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel6.setText("Palavra-chave:");

        btnCriaArquivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/new-file-icon.png"))); // NOI18N
        btnCriaArquivo.setText("Criar Container");
        btnCriaArquivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCriaArquivoActionPerformed(evt);
            }
        });

        tableTR.setModel(modelStream);
        tableTR.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableTRMouseClicked(evt);
            }
        });
        scrollTR.setViewportView(tableTR);

        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btnColetaReal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/pick.png"))); // NOI18N
        btnColetaReal.setText("Coletar");
        btnColetaReal.setEnabled(false);
        btnColetaReal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnColetaRealActionPerformed(evt);
            }
        });
        jPanel9.add(btnColetaReal);

        btnStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/hand-stop.png"))); // NOI18N
        btnStop.setText("Finalizar");
        btnStop.setEnabled(false);
        btnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopActionPerformed(evt);
            }
        });
        jPanel9.add(btnStop);

        jLabel7.setText("Status:");

        lbStatusReal.setText("...");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollTR)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2)
                                .addComponent(campoContainer, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnCriaArquivo))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lbStatusReal, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 41, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(campoContainer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCriaArquivo))
                .addGap(18, 18, 18)
                .addComponent(scrollTR, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(lbStatusReal))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tableView.setModel(modelView);
        scrollPainel.setViewportView(tableView);

        lbTwitter1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Twitter-icon.png"))); // NOI18N
        jPanel4.add(lbTwitter1);

        painelCloudReal.add(lbCloudReal);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPainel)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(painelCloudReal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(painelCloudReal, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(16, 16, 16)
                .addComponent(scrollPainel, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Twitter Streaming", jPanel7);

        menuOpcoes.setText("Configurações");

        jMenu4.setText("Autenticação");

        jMenuItem1.setText("Twitter");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem1);

        menuOpcoes.add(jMenu4);

        menuSair.setText("Sair");
        menuSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSairActionPerformed(evt);
            }
        });
        menuOpcoes.add(menuSair);

        jMenuBar1.add(menuOpcoes);

        jMenu1.setText("Ferramentas");

        jMenuItem3.setText("Watson Toolkit");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem6.setText("Weka Converter");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenuItem2.setText("Database Collects");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem7.setText("Corpus Factory");
        jMenu1.add(jMenuItem7);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Ajuda");

        jMenuItem5.setText("Tutorial");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        menuSobre.setText("Sobre");
        menuSobre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSobreActionPerformed(evt);
            }
        });
        jMenu3.add(menuSobre);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        jPanel1.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        TwitterAccessGUI guiAuth = new TwitterAccessGUI(this, rootPaneCheckingEnabled);
        guiAuth.setLocationRelativeTo(this);
        guiAuth.setVisible(true);

    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void menuSobreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSobreActionPerformed
        SobreGUI guiAbout = new SobreGUI(this, rootPaneCheckingEnabled);
        guiAbout.setLocationRelativeTo(this);
        guiAbout.setVisible(true);

    }//GEN-LAST:event_menuSobreActionPerformed

    private void menuSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSairActionPerformed
        System.exit(0);
    }//GEN-LAST:event_menuSairActionPerformed


    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        TutorialGUI ajudaGui = new TutorialGUI(this, rootPaneCheckingEnabled);
        ajudaGui.setLocationRelativeTo(this);
        ajudaGui.setVisible(true);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        WatsonGUI utilitario = new WatsonGUI();
        utilitario.setLocationRelativeTo(this);
        utilitario.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed


    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        ConvertWekaGUI weka = new ConvertWekaGUI(this, rootPaneCheckingEnabled);
        weka.setLocationRelativeTo(this);
        weka.setVisible(true);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopActionPerformed
        controlStream.pararColeta();
    }//GEN-LAST:event_btnStopActionPerformed

    private void btnColetaRealActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnColetaRealActionPerformed
        controlStream.coletarStreams();
    }//GEN-LAST:event_btnColetaRealActionPerformed

    private void tableTRMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableTRMouseClicked
        controlStream.cliqueTBStream(evt);
    }//GEN-LAST:event_tableTRMouseClicked

    private void btnCriaArquivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCriaArquivoActionPerformed
        if (campoContainer.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe um termo de busca!");
        } else {
            Coleta coleta = new Coleta();
            coleta.setTermo(campoContainer.getText());
            coleta.setData(Calendar.getInstance().getTime());
            controlStream.criarArquivoStream(campoContainer.getText(), keys.get(0), coleta);
            campoContainer.setText(null);
        }
    }//GEN-LAST:event_btnCriaArquivoActionPerformed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        limpar();
    }//GEN-LAST:event_btnLimparActionPerformed

    private void chckbxColetaRetroativaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chckbxColetaRetroativaItemStateChanged

    }//GEN-LAST:event_chckbxColetaRetroativaItemStateChanged

    private void lbTwitterMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbTwitterMouseClicked
        TwitterSearchController.irParaSite();
    }//GEN-LAST:event_lbTwitterMouseClicked

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        redefinirLabels();
        searchControl = new TwitterSearchController(scroll, table, modelSearch);
        if (campoBusca.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Digite algum termo de busca!", "Aviso",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            int dia = Integer.parseInt(cbDia.getSelectedItem().toString());
            searchControl.buscaRetroativa(campoBusca.getText(), dia, chckbxColetaRetroativa.isSelected(), chkRetweet.isSelected());
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void campoBuscaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoBuscaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnBuscarActionPerformed(null);
        }
    }//GEN-LAST:event_campoBuscaKeyPressed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        ColetasGUI coletaGUI = new ColetasGUI(this, rootPaneCheckingEnabled);
        coletaGUI.setLocationRelativeTo(this);
        coletaGUI.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void limpar() {
        lbData.setText("...");
        lbStatus.setText("...");
        lbQuantidade.setText("0");
        campoBusca.setText(null);
        //MANIPULADOR.limparTabela();
    }

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
            java.util.logging.Logger.getLogger(SherlockGUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SherlockGUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SherlockGUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SherlockGUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new SherlockGUI().setVisible(true);
        });
    }

    private void redefinirLabels() {
        lbStatus.setText("...");
        lbQuantidade.setText(String.valueOf(0));
        lbData.setText("...");
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static final javax.swing.JButton btnBuscar = new javax.swing.JButton();
    public static javax.swing.JButton btnColetaReal;
    private javax.swing.JButton btnCriaArquivo;
    public static final javax.swing.JButton btnLimpar = new javax.swing.JButton();
    public static javax.swing.JButton btnStop;
    private javax.swing.JTextField campoBusca;
    public static javax.swing.JTextField campoContainer;
    private javax.swing.JComboBox cbDia;
    private javax.swing.JCheckBox chckbxColetaRetroativa;
    private javax.swing.JCheckBox chkRetweet;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTabbedPane jTabbedPane1;
    public static javax.swing.JLabel lbCloudReal;
    public static final javax.swing.JLabel lbData = new javax.swing.JLabel();
    private javax.swing.JLabel lbLogo;
    public static final javax.swing.JLabel lbQuantidade = new javax.swing.JLabel();
    public static final javax.swing.JLabel lbStatus = new javax.swing.JLabel();
    public static javax.swing.JLabel lbStatusReal;
    private javax.swing.JLabel lbTwitter;
    private javax.swing.JLabel lbTwitter1;
    private javax.swing.JMenu menuOpcoes;
    private javax.swing.JMenuItem menuSair;
    private javax.swing.JMenuItem menuSobre;
    private javax.swing.JPanel painelCloudReal;
    private javax.swing.JPanel painelColeta;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JScrollPane scrollPainel;
    public static javax.swing.JScrollPane scrollTR;
    private javax.swing.JTable table;
    public static javax.swing.JTable tableTR;
    private javax.swing.JTable tableView;
    // End of variables declaration//GEN-END:variables
}
