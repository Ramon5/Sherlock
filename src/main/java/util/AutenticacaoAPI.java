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
package util;


import com.google.maps.GeoApiContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.OAuth2Authorization;
import twitter4j.auth.OAuth2Token;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author root
 */
public class AutenticacaoAPI {

    private static Logger logger;
    public static Twitter twitter;
    public static boolean autenticado;
    public static boolean autenticadoMaps;
    public static GeoApiContext context;
    public static String chaveAPI;
    public static OAuthAuthorization oauth;
    public static String keyMap;

    public AutenticacaoAPI() {
        logger = Logger.getLogger(AutenticacaoAPI.class);
    }

    /**
     * Autenticando na conta do Twitter
     *
     */
    public static void userAutentication() {
        String[] key = getKeysCrypt();

        if (key != null) {

            ConfigurationBuilder config = new ConfigurationBuilder();
            config.setDebugEnabled(true);
            config.setPrettyDebugEnabled(true);
            config.setOAuthConsumerKey(key[0]);
            config.setOAuthConsumerSecret(key[1]);
            config.setOAuthAccessToken(key[2]);
            config.setOAuthAccessTokenSecret(key[3]);
            

            OAuthAuthorization auth = new OAuthAuthorization(config.build());

            twitter = new TwitterFactory().getInstance(auth);
            oauth = auth;

            autenticado = true;
        } else {
            autenticado = false;
            JOptionPane.showMessageDialog(null, "Você não possui credenciais para o Twitter!");
        }

    }
    
    public static void appAutentication() {
        String[] key = getKeysCrypt();

        if (key != null) {

            try {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setApplicationOnlyAuthEnabled(true);
                builder.setOAuthConsumerKey(key[0]);
                builder.setOAuthConsumerSecret(key[1]);
                
                OAuth2Token token = new TwitterFactory(builder.build()).getInstance().getOAuth2Token();
                
                ConfigurationBuilder config = new ConfigurationBuilder();
                config.setDebugEnabled(true);
                config.setPrettyDebugEnabled(true);
                config.setApplicationOnlyAuthEnabled(true);
                config.setOAuthConsumerKey(key[0]);
                config.setOAuthConsumerSecret(key[1]);
                config.setOAuth2TokenType(token.getTokenType());
                config.setOAuth2AccessToken(token.getAccessToken());
                
                twitter = new TwitterFactory(config.build()).getInstance();
                
                autenticado = true;
                
            } catch (TwitterException ex) {
                java.util.logging.Logger.getLogger(AutenticacaoAPI.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            autenticado = false;
            JOptionPane.showMessageDialog(null, "Você não possui credenciais para o Twitter!");
        }

    }

    public static void autenticarMaps() {

        String keys = getKeyCryptMaps();

        if (keys != null) {
            chaveAPI = keys;
            context = new GeoApiContext().setApiKey(chaveAPI);

            autenticadoMaps = true;
        } else {
            autenticadoMaps = false;
            JOptionPane.showMessageDialog(null, "Você não possui credenciais para o Google Maps!");
        }
    }

    /**
     * Obtendo as credenciais gravadas no arquivo config.cfg
     *
     * @return
     */
    private static String[] getKeysCrypt() {
        GeradorCredencial crypt = new GeradorCredencial();

        File arquivo;

        try {

            arquivo = new File(System.getProperty("user.home") + "/SherlockTM/Config_Auth/config.cfg");
                        
            if (arquivo.isFile()) {
                InputStream leitor = new FileInputStream(arquivo);
                int tamanho = (int) arquivo.length();
                byte[] bytes = new byte[tamanho];
                leitor.read(bytes, 0, tamanho);

                String chave = crypt.decriptar(bytes, CriptografiaDeChaves.getKey());
                String[] chaves = chave.split("\\|");
                
                return chaves;
            } else {
                return null;
            }

        } catch (FileNotFoundException e) {
            logger.error(e);
            JOptionPane.showMessageDialog(null, "Arquivo de autenticação não encontrado!");
        } catch (IOException e) {
            logger.error(e);
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
        return null;
    }

    private static String getKeyCryptMaps() {

        GeradorCredencial crypt = new GeradorCredencial();

        File arquivo = new File(System.getProperty("user.home") + "/SherlockTM/Config_Auth/GoogleMapsAPI.cfg");

        FileInputStream leitor = null;

        try {
            if (arquivo.isFile()) {
                leitor = new FileInputStream(arquivo);
                int tamanho = (int) arquivo.length();
                byte[] bytes = new byte[tamanho];

                leitor.read(bytes, 0, tamanho);

                String chave = crypt.decriptar(bytes, CriptografiaDeChaves.getKey());

                return chave;
            }else{
                return null;
            }

        } catch (FileNotFoundException e) {
            logger.error(e);
            JOptionPane.showMessageDialog(null, "Arquivo de autenticação inexistente!");
        } catch (IOException e) {
            logger.error(e);
            JOptionPane.showMessageDialog(null, "Erro: " + e.getLocalizedMessage());
        }
        return null;
    }

}
