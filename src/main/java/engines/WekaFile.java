package engines;

import entidade.Coleta;
import entidade.Tweet;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import twitter4j.Status;
import util.PreprocessoStrings;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

/**
 *
 * @author Ramon
 */
public class WekaFile {

    private ArrayList<Attribute> atributos;
    private Instances instancias;
    private List<Instance> aux;
    private SimpleDateFormat dataHora;

    public WekaFile() {
        buildAttributes();
        dataHora = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    }

    /**
     * Cria os atributos que serão usados no arquivo .arff inicia a lista que
     * irá armazenar as instancias de tweets
     */
    private void buildAttributes() {
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
        atributos.add(new Attribute("RETWEET"));
        atributos.add(new Attribute("LATITUDE"));
        atributos.add(new Attribute("LONGITUDE"));
    }

    /**
     * Recupera os tweets e cria as instancias dos mesmos com os respectivos
     * dados que serão populados no arquivo .arff (Weka Cluster)
     *
     * @param tweet
     */
    public void createInstances(Tweet tweet) {
        double[] valorDeInstancia = new double[atributos.size()];
        valorDeInstancia[0] = atributos.get(0).addStringValue(PreprocessoStrings.processar(tweet.getTweet(), true));
        valorDeInstancia[1] = tweet.getTo_user_id();
        valorDeInstancia[2] = atributos.get(2).addStringValue(tweet.getAutor());
        valorDeInstancia[3] = tweet.getIdTweet();
        valorDeInstancia[4] = tweet.getIdUsuario();
        valorDeInstancia[5] = atributos.get(5).addStringValue(tweet.getLang());
        valorDeInstancia[6] = tweet.getFavorite_count();
        //valorDeInstancia[8] = atributos.get(8).parseDate(String.valueOf(dataHora.format(tweet.getCreatedAt())));
        valorDeInstancia[7] = atributos.get(7).addStringValue(String.valueOf(dataHora.format(tweet.getDatecreated())));
        valorDeInstancia[8] = tweet.getRetweet();
        valorDeInstancia[9] = tweet.getLatitude();
        valorDeInstancia[10] = tweet.getLongitude();
       
        Instance instancia = new DenseInstance(1.0, valorDeInstancia);
        aux.add(instancia);
    }

    public void buildInstances(Coleta coleta) {
        //inicializando a stream de instancias
        instancias = new Instances(coleta.getTermo(), atributos, aux.size());

        //preenchendo o stream com as instancias de tweets recuperados
        aux.forEach((i) -> {
            instancias.add(i);
        });
    }

    /**
     * Grava o arquivo .arff no disco rígido
     * @param coleta
     */
    public void saveArff(Coleta coleta, File diretorio) {
        try {
            ArffSaver saver = new ArffSaver();
            saver.setInstances(instancias);
            saver.setFile(new File(diretorio.getAbsolutePath() + FileSystems.getDefault().getSeparator() + coleta.getTermo() + ".arff"));
            saver.writeBatch();
        } catch (IOException ex) {

        }
    }

}
