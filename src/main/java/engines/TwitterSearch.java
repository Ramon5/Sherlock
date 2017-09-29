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

import com.dropbox.core.DbxException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatusEvent;
import twitter4j.RateLimitStatusListener;
import twitter4j.Status;
import twitter4j.TwitterException;
import util.AutenticacaoAPI;
import util.GerenciadorDiretorios;
import util.GerenciadorLimite;
import util.ManipuladorTabela;
import util.PreprocessoStrings;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

public final class TwitterSearch extends Thread implements ManipuladorTabela {

    private String termo;
    private final Calendar data;
    private JLabel status;
    private JLabel quantidade;
    private JLabel ultimaData;
    private final JButton botao;
    private final JButton botaoLimpar;
    private JScrollPane scroll;
    private JTable table;

    private BufferedWriter bufferTweet;

    private Query query;
    private QueryResult result;
    private List<Status> listTweets;
    private final Logger logger;

    private Date dataLimite;
    private GerenciadorDiretorios manageDir;
    private GerenciadorLimite limite;

    private Object cloud;

    private FileWriterWithEncoding fileWriter;

    private boolean retweet;

    private String novoNomeColeta;
    private SimpleDateFormat dataHora;
    private Date dataRef;
    private boolean limitado;

    private ArrayList<Attribute> atributos;
    private Instances instancias;
    private List<Instance> aux;

    /**
     * Construtor
     *
     * @param botao
     * @param botaoLimpar
     */
    public TwitterSearch(JButton botao, JButton botaoLimpar) {
        this.botao = botao;
        this.botaoLimpar = botaoLimpar;
        data = Calendar.getInstance();
        InputStream in = this.getClass().getResourceAsStream("/log4j/log4j.properties");
        PropertyConfigurator.configure(in);
        this.logger = Logger.getLogger(TwitterSearch.class);
        dataHora = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        limitado = false;
        criarListener();
        construirAtributos();
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

    public void setRetweet(boolean ativo) {
        this.retweet = ativo;

    }

    public void setTermo(String termo) {
        this.termo = termo;
    }

    public void setLimite(Date dataLimite) {
        this.dataLimite = dataLimite;
    }

    public void setCloud(Object cloud) {
        this.cloud = cloud;
    }

    public void setScroll(JScrollPane scroll, JTable table) {
        this.scroll = scroll;
        this.table = table;
    }

    /**
     * Labels que serão atualizados conforme o progresso da coleta
     *
     * @param status
     * @param quantidade
     * @param ultimaData
     */
    public void setLabels(JLabel status, JLabel quantidade, JLabel ultimaData) {
        this.status = status;
        this.quantidade = quantidade;
        this.ultimaData = ultimaData;
        limite = new GerenciadorLimite(status);
    }

    /**
     * Listener para checar quando o limite de buscas for atingido
     */
    private void criarListener() {
        AutenticacaoAPI.twitter.addRateLimitStatusListener(new RateLimitStatusListener() {
            @Override
            public void onRateLimitStatus(RateLimitStatusEvent rlse) {
                limite.checarLimite(rlse);
            }

            @Override
            public void onRateLimitReached(RateLimitStatusEvent event) {
                limitado = event.isAccountRateLimitStatus() || event.isIPRateLimitStatus();
            }
        });
    }

    /**
     * Método principal da Thread
     */
    @Override
    public void run() {
        //Se a aplicação está autorizada a coletar então executa abaixo
        if (AutenticacaoAPI.autenticado) {
            botao.setEnabled(false);
            botaoLimpar.setEnabled(false);

            status.setText("Coletando...");

            manageDir = new GerenciadorDiretorios(termo, false);
            manageDir.criaArquivoColeta();
            iniciarGravadores();

            //filtra retweets de acordo com o parametro passado
            if (retweet) {
                query = new Query(termo).lang("pt").count(100);
            } else {
                query = new Query(termo + " -filter:retweets").lang("pt").count(100);
            }
            //coleta de acordo com o tema informado
            efetuarColeta(query);

            if (cloud != null) {
                salvarNaNuvem();
            }

            this.interrupt();

        } else {
            JOptionPane.showMessageDialog(null, "Não foi possível logar! Verifique o arquivo de log");
        }

    }

    /**
     * Realiza a busca na base de dados do Twitter
     *
     * @param query
     */
    private void efetuarColeta(Query query) {

        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");

        //Data limite para busca
        String dataInicio = getData();

        query.since(dataInicio);

        //Data atual para comparação com a data de condição de parada
        dataRef = new Date();

        try {
            long count = 0;

            int finalizado = 1;

            long max_id = Long.MAX_VALUE;

            do {

                result = AutenticacaoAPI.twitter.search(query);

                status.setText("Coletando...");

                listTweets = result.getTweets();

                finalizado = listTweets.size();

                for (Status tweet : listTweets) {

                    manipulador.addTweet(getTweet(tweet));
                    scroll.getVerticalScrollBar().setValue(table.getHeight());
                    gravarTweet(tweet);
                    quantidade.setText(String.valueOf(count));
                    max_id = Math.min(tweet.getId(), max_id);
                    dataRef = tweet.getCreatedAt();
                    
                }
                
                count+=listTweets.size();
                listTweets.clear();
                
                //evita que sejam capturados tweets que já foram processados
                query.setMaxId(max_id - 1);

                //Condicao de parada; verifica se a data limite foi atingida ou se nenhum termo foi encontrado
            } while (dataRef.after(dataLimite) && finalizado > 0);

            //inicializando a stream de instancias
            instancias = new Instances(termo, atributos, aux.size());

            //preenchendo o stream com as instancias de tweets recuperados
            for (Instance i : aux) {
                instancias.add(i);
            }
            //hanilita os botões na tela principal
            habilitarComandos(sdf2.format(dataRef));

        } catch (TwitterException e) {
            if (e.exceededRateLimitation()) {
                logger.error(e);

            } else if (e.resourceNotFound()) {
                logger.error(e);
                habilitarComandos(sdf2.format(dataRef));
            }

        } catch (IOException e) {
            logger.error(e);
            JOptionPane.showMessageDialog(null, "Não foi possível gravar o arquivo de coleta, veja o log!");
        } catch (NullPointerException ex) {
            logger.error(ex);
            JOptionPane.showMessageDialog(null, "Recurso esgotado para este termo!");
            habilitarComandos(sdf2.format(dataRef));
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
        //valorDeInstancia[7] = atributos.get(7).parseDate(String.valueOf(dataHora.format(tweet.getCreatedAt())));
        valorDeInstancia[7] = atributos.get(7).addStringValue(String.valueOf(dataHora.format(tweet.getCreatedAt())));
        Instance instancia = new DenseInstance(1.0, valorDeInstancia);
        aux.add(instancia);

    }

    /**
     * Salva os tweets no arquivo csv
     *
     * @param tweet
     * @throws IOException
     */
    private void gravarTweet(Status tweet) throws IOException {
        String texto = tweet.getText().replaceAll("\"", "'");
        texto = texto.replaceAll("\\|", " ");
        texto = texto.replace("\n", "").replace("\r", "");
        texto = PreprocessoStrings.processar(texto, true);
        /*bufferTweet.append(texto).append("|").append(String.valueOf(tweet.getId())).append("|").
                append(tweet.getUser().getScreenName()).append("|").append(String.valueOf(dataHora.format(tweet.getCreatedAt())));*/
        bufferTweet.append(texto).append("|")
                .append(String.valueOf(tweet.getInReplyToUserId())).append("|")
                .append(tweet.getUser().getScreenName()).append("|")
                .append(String.valueOf(tweet.getId())).append("|")
                .append(String.valueOf(tweet.getUser().getId())).append("|")
                .append(String.valueOf(tweet.getFavoriteCount())).append("|")
                .append(String.valueOf(tweet.getCreatedAt()));

        bufferTweet.newLine();
        bufferTweet.flush();
        criarInstancias(tweet);

    }

    /**
     * Retorna a data limite no padrão da API
     *
     * @return
     */
    private String getData() {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        return formato.format(dataLimite);
    }

    /**
     * Obtém as informações dos tweets para serem exibidas na tabela da tela
     * principal
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

        return tw;
    }

    /**
     * Reabilita comandos quando a coleta for finalizada
     */
    private void habilitarComandos(String data) {
        status.setText("Coleta finalizada");
        closeStreams();
        novoNomeColeta = renomearColeta(dataRef);
        botao.setEnabled(true);
        botaoLimpar.setEnabled(true);
        ultimaData.setText(data);
        salvarArff();
        mensagem();

    }

    /**
     * Grava o arquivo .arff no disco rígido
     */
    private void salvarArff() {
        try {
            ArffSaver saver = new ArffSaver();
            saver.setInstances(instancias);
            saver.setFile(new File(manageDir.getArquivo().getParent() + "/" + termo + ".arff"));
            saver.writeBatch();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(TwitterSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Informativo do término da coleta
     */
    private void mensagem() {
        Mensagem msg;
        String text = System.getProperty("user.home") + "/SherlockTM/Dataset/";
        msg = new Mensagem(null, true, text);
        msg.setTitulo(termo);
        msg.setLocationRelativeTo(null);
        msg.setVisible(true);

    }

    /**
     * Inicializa as os streams responsáveis por gravar os tweets no arquivo
     */
    private void iniciarGravadores() {
        try {
            fileWriter = new FileWriterWithEncoding(manageDir.getArquivo(), StandardCharsets.UTF_8, true);
            bufferTweet = new BufferedWriter(fileWriter);
            bufferTweet.append("TEXT|TO_USER_ID|FROM_USER|ID|LANG|FAVORITE_COUNT|CREATED_AT");
            bufferTweet.newLine();
            bufferTweet.flush();

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
            File coleta = new File(manageDir.getArquivo().getAbsolutePath());
            StringBuilder str = new StringBuilder();

            str.append(manageDir.getArquivo().getParent()).append(FileSystems.getDefault().getSeparator())
                    .append(termo).append("(")
                    .append(formatoColeta.format(dataRef)).append(" a ").append(formatoColeta.format(data.getTime())).append(")");

            String novoNome = str.toString();
            File coletaRenomeada = new File(novoNome + "-retroativo.csv");

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
                drop.upload(System.getProperty("user.home") + "/SherlockTM/Dataset/" + manageDir.getDirRaiz(mes) + "/" + termo, novoNomeColeta + ".csv");

            } catch (DbxException | IOException e) {
                logger.error(e);
                JOptionPane.showMessageDialog(null, "Erro: " + e.getLocalizedMessage());
            }
        }
    }

    /**
     * Encerra os streams de gravação
     */
    private void closeStreams() {
        try {
            fileWriter.close();
            bufferTweet.close();
        } catch (IOException ex) {
            logger.error(ex);
        }
    }

}
