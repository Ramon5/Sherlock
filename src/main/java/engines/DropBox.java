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
package engines;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.DbxWriteMode;
import java.awt.Desktop;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import util.CriptografiaDeChaves;
import view.DropTokenGUI;
import util.DetectaSistema;
import static util.DetectaSistema.detectarSistema;
import util.GeradorCredencial;
import view.SherlockGUI;

public class DropBox implements DetectaSistema {

    private DbxClient client;
    private Logger logger;
    private DbxWebAuthNoRedirect webAuth;
    private String accessToken;
    private DbxRequestConfig config;
    private String authorizeUrl;
    
    

    public DropBox() {
        InputStream in = this.getClass().getResourceAsStream("/log4j/log4j.properties");
        PropertyConfigurator.configure(in);
        this.logger = Logger.getLogger(DropBox.class);
    }
    

    public void carregarAutenticacao() {

        File arquivo = new File(System.getProperty("user.home") + "/SherlockTM/Config_Auth/authDropBox.cfg");

        if (arquivo.exists()) {
            config = new DbxRequestConfig("SherlockTM", Locale.getDefault().toString());
            client = new DbxClient(config, getKeyCrypt());
            SherlockGUI.lbCloud.setEnabled(true);
            SherlockGUI.lbCloudReal.setEnabled(true);
            SherlockGUI.lbCloud.setIcon(new ImageIcon(DropBox.class.getResource("/imagens/dropbox.png")));
            SherlockGUI.lbCloudReal.setIcon(new ImageIcon(DropBox.class.getResource("/imagens/dropbox.png")));
            try {
                SherlockGUI.lbCloud.setToolTipText("Conectado como: " + client.getAccountInfo().displayName);
                SherlockGUI.lbCloudReal.setToolTipText("Conectado como: " + client.getAccountInfo().displayName);
            } catch (DbxException e) {
                logger.error(e);
                JOptionPane.showMessageDialog(null, "Erro! Verifique o arquivo de log.");
            }
        } else {
            SherlockGUI.lbCloud.setIcon(new ImageIcon(DropBox.class.getResource("/imagens/dropbox.png")));
            SherlockGUI.lbCloudReal.setIcon(new ImageIcon(DropBox.class.getResource("/imagens/dropbox.png")));
            SherlockGUI.lbCloud.setToolTipText("Você não está logado ou não possui credenciais registradas");
            SherlockGUI.lbCloudReal.setToolTipText("Você não está logado ou não possui credenciais registradas");
            SherlockGUI.lbCloud.setEnabled(false);
            SherlockGUI.lbCloudReal.setEnabled(false);
            DropTokenGUI guiDrop = new DropTokenGUI(null, true);
            guiDrop.setVisible(true);
        }
    }

    public void logout() {
        SherlockGUI.lbCloud.setToolTipText("Você não está logado!");
        SherlockGUI.lbCloudReal.setToolTipText("Você não está logado!");
        SherlockGUI.lbCloud.setEnabled(false);
        SherlockGUI.lbCloudReal.setEnabled(false);
    }

    public void upload(String caminho, String novoPath) throws DbxException, IOException {
        File diretorio = new File(caminho);
        FileInputStream input = null;
        File[] arquivos = diretorio.listFiles();
        DbxEntry.Folder folder = client.createFolder("/" + novoPath);
        String arquivo = novoPath + ".csv";
        for (File file : arquivos) {
            input = new FileInputStream(file);
            client.uploadFile(folder.path + "/" + arquivo, DbxWriteMode.add(), file.length(), input);
        }

        JOptionPane.showMessageDialog(null, "Arquivos de coleta armazenados no DropBox!");
        input.close();
    }

    public String getAccessToken(String appKey, String appSec) {
        DbxAuthFinish authfinish;
        DbxAppInfo info = new DbxAppInfo(appKey, appSec);
        config = new DbxRequestConfig("SherlockTM", Locale.getDefault().toString());
        webAuth = new DbxWebAuthNoRedirect(config, info);
        authorizeUrl = webAuth.start();
        irParaSite();
        String pin = JOptionPane.showInputDialog(null, "Insira o código PIN", "PIN para geração do token", JOptionPane.INFORMATION_MESSAGE);
        if(pin.length() > 0){
            try {
                authfinish = webAuth.finish(pin);
                accessToken = authfinish.accessToken;
                client = new DbxClient(config, accessToken);
            } catch (DbxException ex) {
                
            }
        }
        return accessToken;
    }

    protected String getKeyCrypt() {

        GeradorCredencial crypt = new GeradorCredencial();

        File arquivo = new File(System.getProperty("user.home") + "/SherlockTM/Config_Auth/authDropBox.cfg");

        FileInputStream leitor = null;

        try {
            leitor = new FileInputStream(arquivo);
            int tamanho = (int) arquivo.length();
            byte[] bytes = new byte[tamanho];
            leitor.read(bytes, 0, tamanho);

            String chave = crypt.decriptar(bytes, CriptografiaDeChaves.getKey());

            return chave;

        } catch (FileNotFoundException e) {
            logger.error(e);
            JOptionPane.showMessageDialog(null, "Arquivo de autenticação inexistente!");
        } catch (IOException e) {
            logger.error(e.getMessage());
            JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
        } finally {
            try {
                leitor.close();
            } catch (IOException e) {
                logger.error(e);
                JOptionPane.showMessageDialog(null, "Erro: " + e.getMessage());
            }
        }
        return null;
    }

    private void irParaSite(){
        try {
            if (detectarSistema() == 0) {
                String[] args = new String[]{"/usr/bin/firefox", authorizeUrl};
                Runtime.getRuntime().exec(args);
            } else if (detectarSistema() == 1) {
                Desktop.getDesktop().browse(new URI(authorizeUrl));
            }
        } catch (IOException e1) {
            logger.error(e1);
        } catch (URISyntaxException ex) {
            logger.error(ex);
        }
    }
}
