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
package inativos;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.util.Calendar;
import javax.swing.JOptionPane;
import entidade.TweetStream;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author root
 */
public class GerenciadorDiretorios {

    private final String nomeArquivo;
    private File diretorioTweet;
    //private File diretorioGeo;
    //private File diretorioNodes;
    //private File arquivoSearch;
    private File arquivo;
    private TweetStream container;
    private TweetStream containerNode;
    private boolean tempoReal = false;

    private final Logger logger;

    public GerenciadorDiretorios(String nomeArquivo, boolean tempoReal) {
        this.nomeArquivo = nomeArquivo;
        this.tempoReal = tempoReal;
        InputStream in = this.getClass().getResourceAsStream("/log4j/log4j.properties");
        PropertyConfigurator.configure(in);
        logger = Logger.getLogger(GerenciadorDiretorios.class);
    }

    private void criarDiretorios() {

        String pathTweet;
        //String pathGeo;
        //String pathNodes;
        Calendar mes = Calendar.getInstance();
        try {

            pathTweet = System.getProperty("user.home") + "/SherlockTM/Dataset/" + getDirRaiz(mes) + FileSystems.getDefault().getSeparator() + nomeArquivo;

            //pathGeo = pathTweet + "/GeoLocal";
            diretorioTweet = new File(pathTweet);
            //diretorioGeo = new File(pathGeo);
            //diretorioNodes = new File(pathNodes);

            if (!diretorioTweet.exists()) {
                diretorioTweet.mkdirs();
                //diretorioGeo.mkdirs();
            }

            /*if(!diretorioNodes.exists()){
                diretorioNodes.mkdirs();
            }*/
        } catch (NullPointerException e) {
            logger.error(e);
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }

    }

    public void criaArquivoColeta() {

        criarDiretorios();

        String fileTweet;
        //String node;
        if (tempoReal) {
            fileTweet = FileSystems.getDefault().getSeparator() + nomeArquivo + ".csv";
        }else{
            fileTweet = FileSystems.getDefault().getSeparator() + nomeArquivo + "-retroativo.csv";
        }
        //node = FileSystems.getDefault().getSeparator() + nomeArquivo + ".csv";

        //criando um arquivo para armazenamento dos streams
        container = new TweetStream(diretorioTweet.getAbsolutePath() + fileTweet);
        container.setArquivo(nomeArquivo);

        //criando um arquivo de nó 
        //containerNode = new TweetTR(diretorioNodes.getAbsolutePath() + node);
        //containerNode.setArquivo(nomeArquivo);
        //criando um arquivo para armazenamento de tweets retroativos
        arquivo = new File(diretorioTweet.getAbsolutePath() + fileTweet);
        //arquivoNode = new File(diretorioNodes.getAbsolutePath() + node);

        try {

            if (!container.exists()) {
                container.createNewFile();
            }

        } catch (IOException e) {
            logger.error(e);
            JOptionPane.showMessageDialog(null, "Não foi possível criar o diretório!");
        }

    }

    /**
     * Método responsável por gerar o nome do diretorio conforme o mês e o ano
     * corrente
     *
     * @param mes
     * @return
     */
    public String getDirRaiz(Calendar mes) {
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

    public TweetStream getContainer() {
        return this.container;
    }

    public File getArquivo() {
        return arquivo;
    }

    /*public File getArquivoNode() {
        return arquivoNode;
    }

    public TweetTR getContainerNode() {
        return containerNode;
    }

    /*public File getDiretorioGeo() {
        return diretorioGeo;
    }*/
}
