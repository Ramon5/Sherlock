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

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import static util.DetectaSistema.detectarSistema;
import util.GeradorCredencial;
import static view.TwitterAccessGUI.campoConsumerKey;
import static view.TwitterAccessGUI.campoConsumerSec;

/**
 *
 * @author root
 */
public class AuthController {

    private static GeradorCredencial auth;
    private static Logger logger;
    //private static RequestToken requestToken;
    //private static AccessToken accessToken;

    public static void authGoogle(String chave) {
        auth = new GeradorCredencial();
        auth.criarDiretorio();
        auth.gerarArquivoCrypto(chave, System.getProperty("user.home") + "/SherlockTM/Config_Auth/GoogleMapsAPI.cfg");
        JOptionPane.showMessageDialog(null, "Arquivo de autenticacao gerado com sucesso!");

    }

    public static void autenticacaoTwitter(String consumer, String consumerSecret) {
        gerarCredenciais(consumer,consumerSecret);
        /*try {
            Twitter twitter = TwitterFactory.getSingleton();
            twitter.setOAuthConsumer(consumer, consumerSecret);
            requestToken = twitter.getOAuthRequestToken();
            accessToken = null;

            if (null == accessToken) {
                irParaSite(requestToken.getAuthorizationURL());
                String pin = JOptionPane.showInputDialog(null, "Insira o código PIN", "PIN para geração do token", JOptionPane.INFORMATION_MESSAGE);
                if (pin != null) {
                    try {
                        if (pin.length() > 0) {
                            accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                        } else {
                            accessToken = twitter.getOAuthAccessToken();
                        }
                    } catch (TwitterException te) {
                        if (401 == te.getStatusCode()) {
                            JOptionPane.showMessageDialog(null, "Erro: " + te.getLocalizedMessage());
                        } else {
                            logger.error(te);
                        }
                    }
                }
                gerarCredenciais();                
            }

        } catch (TwitterException ex) {
            logger.error(ex);
        }*/

    }

    private static void gerarCredenciais(String consumer, String consumerSecret) {
        auth = new GeradorCredencial();
        auth.criarDiretorio();
        //if (accessToken != null) {
            auth.gerarArquivoCrypto(getKeyForCrypt(consumer, consumerSecret), System.getProperty("user.home") + "/SherlockTM/Config_Auth/config.cfg");
            JOptionPane.showMessageDialog(null, "Arquivo de autenticacao gerado com sucesso!");
        //}else{
        //    JOptionPane.showMessageDialog(null, "Não foi possível gerar o arquivo de autenticação!", "Erro", JOptionPane.ERROR_MESSAGE);
        //}
    }

    /*private static void irParaSite(String link) {
        try {
            if (detectarSistema() == 0) {
                String[] args = new String[]{"/usr/bin/firefox", link};
                Runtime.getRuntime().exec(args);
            } else if (detectarSistema() == 1) {
                Desktop.getDesktop().browse(new URI(link));
            }
        } catch (IOException e1) {
            logger.error(e1);
        } catch (URISyntaxException ex) {
            logger.error(ex);
        }
    }*/

    private static String getKeyForCrypt(String consumer, String consumerSecret) {
        StringBuilder str = new StringBuilder();
        if (consumer != null && consumerSecret != null) {
            str.append(campoConsumerKey.getText()).append("|");
            str.append(campoConsumerSec.getText()).append("|");
            //str.append(accessToken.getToken()).append("|");
            //str.append(accessToken.getTokenSecret());
            return str.toString();
        } 
        return null;
    }

}
