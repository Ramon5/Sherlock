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

import dao.TweetDAO;
import entidade.Coleta;
import entidade.Tweet;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatusEvent;
import twitter4j.RateLimitStatusListener;
import twitter4j.Status;
import twitter4j.TwitterException;
import util.AutenticacaoAPI;
import util.GerenciadorLimite;
import util.ManipuladorTabela;

public final class TwitterSearch extends Thread implements ManipuladorTabela {

    private String termo;
    private JLabel status;
    private JLabel quantidade;
    private JLabel ultimaData;
    private final JButton botao;
    private final JButton botaoLimpar;
    private JScrollPane scroll;
    private JTable table;

    private Query query;
    private QueryResult result;
    private List<Status> listTweets;
    private final Logger logger;
    private Date dataLimite;

   
    private GerenciadorLimite limite;
    private Date dataRef;
    
    private boolean retweet;
    private Coleta coleta;
    private TweetDAO tDAO;

    /**
     * Construtor
     *
     * @param botao
     * @param botaoLimpar
     */
    public TwitterSearch(JButton botao, JButton botaoLimpar) {
        this.botao = botao;
        this.botaoLimpar = botaoLimpar;
        InputStream in = this.getClass().getResourceAsStream("/log4j/log4j.properties");
        PropertyConfigurator.configure(in);
        this.logger = Logger.getLogger(TwitterSearch.class);
        criarListener();
        tDAO = new TweetDAO();
    }

    public void setColeta(Coleta coleta) {
        this.coleta = coleta;
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
                limite.checarLimite(event);
            }
        });
    }

    /**
     * Método principal da Thread
     */
    @Override
    public void run() {

        botao.setEnabled(false);
        botaoLimpar.setEnabled(false);

        status.setText("Coletando...");

        //filtra retweets de acordo com o parametro passado
        if (retweet) {
            query = new Query(termo).lang("pt").count(100);
        } else {
            query = new Query(termo + " -filter:retweets").lang("pt").count(100);
        }
        //coleta de acordo com o tema informado
        efetuarColeta(query);

        this.interrupt();

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
                    quantidade.setText(String.valueOf(count));
                    max_id = Math.min(tweet.getId(), max_id);
                    dataRef = tweet.getCreatedAt();
                    count++;
                }

                listTweets.clear();

                //evita que sejam capturados tweets que já foram processados
                query.setMaxId(max_id - 1);

                //Condicao de parada; verifica se a data limite foi atingida ou se nenhum termo foi encontrado
            } while (dataRef.after(dataLimite) && finalizado > 0);

        } catch (TwitterException e ) {
            if (e.exceededRateLimitation()) {
                logger.error(e);

            } else if (e.resourceNotFound()) {
                logger.error(e);
            }

        }finally{
            tDAO.closeConnection();
            habilitarComandos(sdf2.format(dataRef));
        }
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
     * Reabilita comandos quando a coleta for finalizada
     */
    private void habilitarComandos(String data) {
        status.setText("Coleta finalizada");
        botao.setEnabled(true);
        botaoLimpar.setEnabled(true);
        ultimaData.setText(data);
        mensagem();

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

}
