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

import entidade.TweetTR;
import entidade.Tweet;
import com.dropbox.core.DbxException;
import dao.TweetDAO;
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
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import tablemodel.TableModel;
import twitter4j.FilterQuery;
import twitter4j.RateLimitStatusEvent;
import twitter4j.RateLimitStatusListener;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import util.AutenticacaoAPI;
import util.DetectaSistema;
import util.GerenciadorLimite;
import util.ManipuladorTabela;
import util.PreprocessoStrings;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class TwitterStreamCollect implements DetectaSistema, ManipuladorTabela {

    private FileWriterWithEncoding fileWriter;
    private Calendar data;
    private BufferedWriter bufferTweet;
    private String query;
    private Logger logger;
    private JScrollPane scroll;
    private JTable table;
    private TwitterStream twitterSt;

    private String filename;
    private long contador = 0;
    private Date dataFinal;
    private Object cloud;
    private int linha;
    private TableModel tabelaTweets;
    private JLabel labelSt;

    private TweetTR containerTweet;
    private GerenciadorLimite limite;

    private String novoNomeColeta;
    private SimpleDateFormat dataHora;
    private FilterQuery filter;

    private ArrayList<Attribute> atributos;
    private Instances instancias;
    private List<Instance> aux;
    
    private Coleta coleta;    
    private TweetDAO tDAO;

    public TwitterStreamCollect(String query) {
        this.query = query;
        data = Calendar.getInstance();
        InputStream in = this.getClass().getResourceAsStream("/log4j/log4j.properties");
        PropertyConfigurator.configure(in);
        this.logger = Logger.getLogger(TwitterStreamCollect.class);
        dataHora = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        tabelaTweets = new TableModel();
        limite = new GerenciadorLimite(labelSt);
        filename = query;
        construirAtributos();
        tDAO = new TweetDAO();
    }

    /**
     * Cria os atributos que serão usados no arquivo .arff inicia a lista que
     * irá armazenar as instancias de tweets
     */
    private void construirAtributos() {
        atributos = new ArrayList<>();
        aux = new ArrayList<>();
        atributos.add(new Attribute("TEXT", (ArrayList<String>) null));
        atributos.add(new Attribute("TO_USER_ID"));
        atributos.add(new Attribute("USER", (ArrayList<String>) null));
        atributos.add(new Attribute("ID"));
        atributos.add(new Attribute("FROM_USER_ID"));
        atributos.add(new Attribute("LANG", (ArrayList<String>) null));
        atributos.add(new Attribute("FAVORITE_COUNT"));
        atributos.add(new Attribute("CREATED_AT", (ArrayList<String>) null));
        //formato data
        //atributos.add(new Attribute("CREATED_AT", "dd-MM-yyyy HH:mm:ss"));
    }

    public TableModel getTabelaTweets() {
        return tabelaTweets;
    }

    public void setCloud(Object cloud) {
        this.cloud = cloud;
    }

    public TweetTR getContainerTweet() {
        return containerTweet;
    }

    public void setContainerTweet(TweetTR containerTweet) {
        this.containerTweet = containerTweet;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public void setContainer(TweetTR containerTweet) {
        this.containerTweet = containerTweet;
    }

    public void setStatus(JLabel labelSt) {
        this.labelSt = labelSt;
    }

    public void setColeta(Coleta coleta){
        this.coleta = coleta;
    }
    /**
     * Método responsável por realizar a coleta em tempo real
     */
    public void collectRealTime() {

        twitterSt = new TwitterStreamFactory().getInstance(AutenticacaoAPI.oauth);
        iniciarContainers();
        containerTweet.setAtivo(true);
        containerTweet.setDataInicio(getData());
        manipuladorTR.atualizar();
        labelSt.setText("Coletando...");

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
                coletar(tweet);
                contador++;
                containerTweet.setQuantidade(contador);
                manipuladorTR.refreshData(linha);
                dataFinal = tweet.getCreatedAt();
            }

            @Override
            public void onStallWarning(StallWarning arg0) {

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
            //twitterSt.retweet();
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
        try {

            tabelaTweets.addTweet(getTweet(tweet));
            scroll.getVerticalScrollBar().setValue(table.getHeight());
            gravarTweet(tweet);
            criarInstancias(tweet);

        } catch (IOException ex) {
            logger.error(ex);
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getLocalizedMessage());
        }
    }

    /**
     * Recupera os tweets e cria as instancias dos mesmos com os respectivos
     * dados que serão populados no arquivo .arff (Weka Cluster)
     *
     * @param tweet
     */
    private void criarInstancias(Status tweet) {
        double[] valorDeInstancia = new double[atributos.size()];
        valorDeInstancia[0] = atributos.get(0).addStringValue(PreprocessoStrings.processar(tweet.getText(), true));
        valorDeInstancia[1] = tweet.getInReplyToUserId();
        valorDeInstancia[2] = atributos.get(2).addStringValue(tweet.getUser().getScreenName());
        valorDeInstancia[3] = tweet.getId();
        valorDeInstancia[4] = tweet.getUser().getId();
        valorDeInstancia[5] = atributos.get(5).addStringValue(tweet.getLang());
        valorDeInstancia[6] = tweet.getFavoriteCount();
        //valorDeInstancia[8] = atributos.get(8).parseDate(String.valueOf(dataHora.format(tweet.getCreatedAt())));
        valorDeInstancia[7] = atributos.get(7).addStringValue(String.valueOf(dataHora.format(tweet.getCreatedAt())));
        Instance instancia = new DenseInstance(1.0, valorDeInstancia);
        aux.add(instancia);
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
        tw.setTweet(status.getText());
        tw.setTo_user_id(status.getInReplyToUserId());
        tw.setFavorite_count((long)status.getFavoriteCount());
        tw.setLang(status.getLang());
        tw.setColeta(coleta);
        
        tDAO.salvar(tw);

        return tw;
    }

    /**
     * Método responsável por gravar os tweets recuperados, no arquivo de saída
     *
     * @param tweet
     * @throws IOException
     */
    private void gravarTweet(Status tweet) throws IOException {
        String texto = tweet.getText().replaceAll(Pattern.quote("\""), "'");
        texto = texto.replace("\n", "").replace("\r", "");        
        texto = texto.replaceAll("\\|", " ");
        bufferTweet.append(texto).append("|")
                .append(String.valueOf(tweet.getInReplyToUserId())).append("|")
                .append(tweet.getUser().getScreenName()).append("|")
                .append(String.valueOf(tweet.getId())).append("|")
                .append(String.valueOf(tweet.getUser().getId())).append("|")
                .append(String.valueOf(tweet.getLang())).append("|")
                .append(String.valueOf(tweet.getFavoriteCount())).append("|")
                .append(String.valueOf(tweet.getCreatedAt()));
        bufferTweet.newLine();
        bufferTweet.flush();
    }

    /**
     * Método responsável por criar o arquivo que armazenará os tweets coletados
     */
    private void iniciarContainers() {

        try {
            fileWriter = new FileWriterWithEncoding(containerTweet, StandardCharsets.UTF_8, true);
            bufferTweet = new BufferedWriter(fileWriter);
            bufferTweet.append("TEXT|TO_USER_ID|SCREEN_NAME|ID|USER_ID|LANG|FAVORITE_COUNT|CREATED_AT");
            bufferTweet.newLine();
            bufferTweet.flush();

        } catch (IOException ex) {
            logger.error(ex);
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getLocalizedMessage());
        }

    }

    /**
     * Finalizando a coleta em tempo real
     *
     */
    public void finalizar() {
        SimpleDateFormat dataFormato = new SimpleDateFormat("dd/MM/yyyy");
        try {

            twitterSt.cleanUp();
            containerTweet.setAtivo(false);
            encerrarStreams();

            if (dataFinal != null) {
                containerTweet.setDataFim(dataFormato.format(dataFinal));
                novoNomeColeta = renomearColeta(dataFinal);
            }
            tDAO.closeConnection();

        } catch (IllegalStateException ex) {
            logger.error(ex);
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getLocalizedMessage());
        }

        if (cloud != null) {
            salvarNaNuvem();
        }

        //inicializando a stream de instancias
        /*instancias = new Instances(query, atributos, aux.size());

        //preenchendo o stream com as instancias de tweets recuperados
        for (Instance i : aux) {
            instancias.add(i);
        }

        salvarArff();*/

    }

    /**
     * Encerrar as streams de gravação
     */
    public void encerrarStreams() {
        try {
            fileWriter.close();
            bufferTweet.close();

        } catch (IOException ex) {
            logger.error(ex);
        }
    }

    /**
     * Renomeando o arquivo de coleta após o término
     *
     * @param dataRef
     */
    private String renomearColeta(Date dataRef) {
        try {
            SimpleDateFormat formatoColeta = new SimpleDateFormat("dd-MM-yyyy");
            File coleta = new File(containerTweet.getAbsolutePath());
            StringBuilder str = new StringBuilder();

            str.append(containerTweet.getParent()).append("/")
                    .append(containerTweet.getArquivo()).append(" ").append("(")
                    .append(formatoColeta.format(dataRef)).append(" a ").append(formatoColeta.format(data.getTime())).append(")");

            String novoNome = str.toString();
            File coletaRenomeada = new File(novoNome + ".csv");

            coleta.renameTo(coletaRenomeada);
            return coletaRenomeada.getName();

        } catch (Exception e) {
            logger.error(e);
            JOptionPane.showMessageDialog(null, "Erro: " + e.getLocalizedMessage());
        }
        return null;
    }

    /**
     * Salva a coleta corrente no Dropbox
     */
    private void salvarNaNuvem() {
        Calendar mes = Calendar.getInstance();
        if (cloud instanceof DropBox) {
            DropBox drop = (DropBox) cloud;
            try {
                drop.upload(System.getProperty("user.home") + "/SherlockTM/Dataset/" + getDirRaiz(mes) + "/" + filename, novoNomeColeta + ".txt");

            } catch (DbxException | IOException e) {
                logger.error(e);
                JOptionPane.showMessageDialog(null, "Erro: " + e.getLocalizedMessage());
            }
        }
    }

    /**
     * Grava o arquivo .arff no disco rígido
     */
    private void salvarArff() {
        try {
            ArffSaver saver = new ArffSaver();
            saver.setInstances(instancias);
            saver.setFile(new File(containerTweet.getParent() + "/" + query + ".arff"));
            saver.writeBatch();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(TwitterSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getDirRaiz(Calendar mes) {
        switch (mes.get(Calendar.MONTH)) {
            case 0:
                return "Janeiro-" + mes.get(Calendar.YEAR);
            case 1:
                return "Fevereiro-" + mes.get(Calendar.YEAR);
            case 2:
                return "Marco-" + mes.get(Calendar.YEAR);
            case 3:
                return "Abril-" + mes.get(Calendar.YEAR);
            case 4:
                return "Maio-" + mes.get(Calendar.YEAR);
            case 5:
                return "Junho-" + mes.get(Calendar.YEAR);
            case 6:
                return "Julho-" + mes.get(Calendar.YEAR);
            case 7:
                return "Agosto-" + mes.get(Calendar.YEAR);
            case 8:
                return "Setembro-" + mes.get(Calendar.YEAR);
            case 9:
                return "Outubro-" + mes.get(Calendar.YEAR);
            case 10:
                return "Novembro-" + mes.get(Calendar.YEAR);
            case 11:
                return "Dezembro-" + mes.get(Calendar.YEAR);
        }
        return null;
    }

    private String getData() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Calendar hoje = Calendar.getInstance();
        return formato.format(hoje.getTime());
    }

}
