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

import entidade.TweetStream;
import entidade.Tweet;
import com.dropbox.core.DbxException;
import control.TwitterStreamController;
import dao.AuthDAO;
import dao.ChaveDAO;
import dao.TweetDAO;
import entidade.Autenticacao;
import entidade.Chave;
import entidade.Coleta;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Pattern;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import listener.Internet;
import listener.InternetEvent;
import listener.InternetListener;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import tablemodel.TableModelMeta;
import tablemodel.TableModelStream;
import twitter4j.ConnectionLifeCycleListener;
import twitter4j.FilterQuery;
import twitter4j.RateLimitStatusEvent;
import twitter4j.RateLimitStatusListener;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.auth.OAuth2Token;
import twitter4j.auth.OAuthAuthorization;
import twitter4j.conf.ConfigurationBuilder;
import util.AutenticacaoAPI;
import static util.AutenticacaoAPI.autenticado;
import static util.AutenticacaoAPI.twitter;
import util.DetectaSistema;
import util.PreprocessoStrings;
import view.SherlockGUI;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class TwitterStreamCollect extends Internet implements Runnable, DetectaSistema {

    private String query;
    private Logger logger;
    private JScrollPane scroll;
    private JTable table;
    private TwitterStream twitterSt;
    private TableModelStream model;

    private String filename;
    private long contador = 0;
    private Date dataFinal;
    private int linha;
    private TableModelMeta tabelaTweets;
    private JLabel labelSt;

    private TweetStream arquivoColeta;
    private GerenciadorLimite limite;

    private FilterQuery filter;

    private Coleta coleta;
    private TweetDAO tDAO;
    private Chave chave;
    private boolean internet;

    public TwitterStreamCollect(String query) {
        this.query = query;
        InputStream in = this.getClass().getResourceAsStream("/log4j/log4j.properties");
        PropertyConfigurator.configure(in);
        this.logger = Logger.getLogger(TwitterStreamCollect.class);
        limite = new GerenciadorLimite(labelSt);
        filename = query;
        tDAO = new TweetDAO();
        internet = true;
    }

    public TableModelMeta getTabelaTweets() {
        return tabelaTweets;
    }

    public void setTabelaTweets(TableModelMeta tabelaTweets) {
        this.tabelaTweets = tabelaTweets;
    }

    public TweetStream getArquivoTweet() {
        return arquivoColeta;
    }

    public void setArquivoTweet(TweetStream arquivoTweet) {
        this.arquivoColeta = arquivoTweet;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public void setChave(Chave chave) {
        this.chave = chave;
    }

    public void setContainer(TweetStream containerTweet) {
        this.arquivoColeta = containerTweet;
    }

    public void setStatus(JLabel labelSt) {
        this.labelSt = labelSt;
    }

    public void setColeta(Coleta coleta) {
        this.coleta = coleta;
    }

    @Override
    public void run() {
        collectRealTime();
    }

    /**
     * Método responsável por realizar a coleta em tempo real
     *
     * @param chave
     */
    private void collectRealTime() {

        twitterSt = new TwitterStreamFactory().getInstance(AutenticacaoAPI.oauth);
        arquivoColeta.setAtivo(true);
        arquivoColeta.setDataInicio(getData());
        tabelaTweets.atualizar();
        labelSt.setText("Coletando...");

        addInternetListener(new InternetListener() {
            @Override
            public void conectado(InternetEvent evt) {
                internet = evt.getSource().isConectado();
            }
        });

        twitterSt.addRateLimitStatusListener(new RateLimitStatusListener() {

            @Override
            public void onRateLimitStatus(RateLimitStatusEvent event) {
                limite.checarLimite(event);
            }

            @Override
            public void onRateLimitReached(RateLimitStatusEvent event) {
                limite.checarLimite(event);
            }
        });

        StatusListener listener = new StatusListener() {

            @Override
            public void onException(Exception arg0) {
                logger.error(arg0);
            }

            @Override
            public void onTrackLimitationNotice(int arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStatus(Status tweet) {
                if (internet) {
                    labelSt.setText("Coletando...");
                    coletar(tweet);
                    contador++;
                    arquivoColeta.setQuantidade(contador);
                    SherlockGUI.modelStream.refreshData(linha);
                    dataFinal = tweet.getCreatedAt();
                }
            }

            @Override
            public void onStallWarning(StallWarning arg0) {
                labelSt.setText(arg0.getMessage());
            }

            @Override
            public void onScrubGeo(long arg0, long arg1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice arg0) {

            }
        };
        configurarStream(listener);

    }

    public void setScroll(JScrollPane scroll, JTable table) {
        this.scroll = scroll;
        this.table = table;
    }

    private void configurarStream(StatusListener listener) {
        if (AutenticacaoAPI.autenticado) {
            filter = new FilterQuery();
            filter.track(query);
            filter.language("pt");
            twitterSt.addListener(listener);
            twitterSt.filter(filter);

        } else {
            JOptionPane.showMessageDialog(null, "Você não possui credenciais para acessar o Twitter!");
        }
    }

    /**
     * Método que será chamado na Thread para captura dos tweets e
     * geolocalização dos mesmos
     *
     * @param tweet
     */
    public void coletar(Status tweet) {
        tabelaTweets.addTweet(getTweet(tweet));
        scroll.getVerticalScrollBar().setValue(table.getHeight());
    }

    /**
     * Método responsável por obter as informações dos tweets recuperados em
     * tempo real para que sejam mostrados na tabela da tela principal, conforme
     * são capturados
     *
     * @param status
     * @return
     */
    private Tweet getTweet(Status status) {
        Tweet tw = new Tweet();
        tw.setDatecreated(status.getCreatedAt());
        tw.setAutor("@" + status.getUser().getScreenName());
        tw.setLocal(status.getUser().getLocation());
        tw.setIdTweet(status.getId());
        tw.setIdUsuario(status.getUser().getId());
        String texto = status.getText().replaceAll(Pattern.quote("\""), "'");
        texto = texto.replace("\n", "").replace("\r", "");
        texto = texto.replaceAll("\\|", " ");
        tw.setTweet(texto);
        tw.setTo_user_id(status.getInReplyToUserId());
        tw.setFavorite_count(status.getFavoriteCount());
        tw.setLang(status.getLang());
        if (status.isRetweet()) {
            tw.setRetweet(1);
        } else {
            tw.setRetweet(0);
        }

        if (status.getGeoLocation() != null) {
            tw.setLatitude(status.getGeoLocation().getLatitude());
            tw.setLongitude(status.getGeoLocation().getLongitude());
        } else {
            tw.setLatitude(0.0);
            tw.setLongitude(0.0);
        }
        tw.setColeta(coleta);

        tDAO.salvar(tw);

        return tw;
    }

    /**
     * Finalizando a coleta em tempo real
     *
     */
    public void finalizar() {
        SimpleDateFormat dataFormato = new SimpleDateFormat("dd/MM/yyyy");
        try {

            twitterSt.cleanUp();
            arquivoColeta.setAtivo(false);

            if (dataFinal != null) {
                arquivoColeta.setDataFim(dataFormato.format(dataFinal));
            }


        } catch (IllegalStateException ex) {
            logger.error(ex);
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getLocalizedMessage());
        } finally {
            tDAO.closeConnection();
        }

    }

    private String getData() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Calendar hoje = Calendar.getInstance();
        return formato.format(hoje.getTime());
    }

}
