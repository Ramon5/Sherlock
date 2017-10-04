package engines;

import entidade.Coleta;
import java.io.File;
import java.io.IOException;
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
    public void createInstances(Status tweet) {
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
        if (tweet.isRetweet()) {
            valorDeInstancia[8] = 1;
        } else {
            valorDeInstancia[8] = 0;
        }
        if (tweet.getGeoLocation() != null) {
            valorDeInstancia[9] = tweet.getGeoLocation().getLatitude();
            valorDeInstancia[10] = tweet.getGeoLocation().getLongitude();
        } else {
            valorDeInstancia[9] = 0.0;
            valorDeInstancia[10] = 0.0;
        }
        Instance instancia = new DenseInstance(1.0, valorDeInstancia);
        aux.add(instancia);
    }

    public void buildInstances(Coleta coleta) {
        //inicializando a stream de instancias
        instancias = new Instances(coleta.getTermo(), atributos, aux.size());

        //preenchendo o stream com as instancias de tweets recuperados
        for (Instance i : aux) {
            instancias.add(i);
        }
    }

    /**
     * Grava o arquivo .arff no disco rígido
     */
    public void saveArff(Coleta coleta) {
        try {
            ArffSaver saver = new ArffSaver();
            saver.setInstances(instancias);
            saver.setFile(new File("destino/" + coleta.getTermo() + ".arff"));
            saver.writeBatch();
        } catch (IOException ex) {

        }
    }

}
