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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.regex.Pattern;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import org.apache.log4j.Logger;
import entidade.TokenTweet;
import org.apache.log4j.PropertyConfigurator;
import util.PreprocessoStrings;

/**
 *
 * @author root
 */
public class GeradorArquivos {

    private Scanner scan;
    private final List<TokenTweet> listToken;
    private FileWriter writer;
    private BufferedWriter buff;
    private final Logger logger;
    private boolean removeRT, removeLink, removeAcent;
    private long qtdArquivosGerados;
    private JLabel labelQtd;
    private JProgressBar progress;
    private int indice;

    public GeradorArquivos(boolean removeRT, boolean removeLink, boolean removeAcent) {
        this.removeRT = removeRT;
        this.removeLink = removeLink;
        this.removeAcent = removeAcent;
        InputStream in = this.getClass().getResourceAsStream("/log4j/log4j.properties");
        PropertyConfigurator.configure(in);
        logger = Logger.getLogger(GeradorArquivos.class);
        listToken = new ArrayList<>();
        indice = 1;
        qtdArquivosGerados = 1;
    }

    public void setLabelQtde(JLabel labelQtd) {
        this.labelQtd = labelQtd;
    }

    public void setProgressBar(JProgressBar progress) {
        this.progress = progress;
    }

    public void separarTokens(File origem) {
        if (origem.getName().endsWith(".txt")) {
            try {
                Pattern pattern = Pattern.compile(Pattern.quote("|.|"));
                scan = new Scanner(new FileReader(origem)).useDelimiter(pattern);

                while (scan.hasNext()) {
                    String linha = scan.next().trim();
                    if (!linha.isEmpty()) {
                        String[] blocos = linha.split(Pattern.quote("|-|"));
                        TokenTweet token = new TokenTweet();
                        token.setText(blocos[0]);
                        token.setDataCreated(blocos[1]);
                        listToken.add(token);
                    }

                }
                progress.setMaximum(listToken.size());

            } catch (FileNotFoundException ex) {
                logger.error(ex);
                JOptionPane.showMessageDialog(null, "Erro: " + ex.getLocalizedMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                logger.error(ex);
                JOptionPane.showMessageDialog(null, "Erro: " + ex.getLocalizedMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Extensão de arquivo não suportada! Converta os arquivos primeiro!");
        }
    }

    public void gerarArquivos(File origem, String destino, int parametro) {
        String[] arq = origem.getName().split(Pattern.quote("."));
        String nomeArquivo = arq[0].replaceAll("\\(.*?\\)", "");
        for (int i = 0; i < listToken.size(); i++) {
            try {
                File novo = null;
                String data = listToken.get(i).getDataCreated();
                
                switch(parametro){
                    case 0:
                        novo = new File(getDiretorio(destino) + File.pathSeparator + (i + 1) + "-" + nomeArquivo + " " + data + ".txt");
                        break;
                    case 1:
                        novo = new File(getDiretorio(destino) + File.pathSeparator + (i + 1) + "-" + nomeArquivo + " " + data + ".txt");
                        break;
                    case 2:
                        novo = new File(destino + File.pathSeparator + indice + "-" + nomeArquivo + " " + data + ".txt");
                        break;
                        
                }
                labelQtd.setText(String.valueOf(qtdArquivosGerados++));
                writer = new FileWriter(novo);
                buff = new BufferedWriter(writer);
                String texto = processar(listToken.get(i).getText());
                if (texto != null) {
                    buff.append(texto);
                    buff.flush();
                    buff.close();
                }
            } catch (IOException ex) {
                logger.error(ex);
                JOptionPane.showMessageDialog(null, "Erro: " + ex.getLocalizedMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
            indice++;
            progress.setValue(i+1);

        }
        listToken.clear();
    }

    /**
     * Método responsável por obter os diretorios de origem para a geração dos
     * diretórios de destino
     *
     * @param fonte
     * @return
     */
    private String getDiretorio(String fonte) {
        String[] partes = fonte.split(Pattern.quote(File.pathSeparator));
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < partes.length - 1; i++) {
            str.append(partes[i]).append(File.pathSeparator);
        }

        return str.toString();
    }

    /**
     * Realiza a limpeza dos tweets
     *
     * @param linha
     * @return
     */
    private String processar(String linha) {

        if (removeRT && removeAcent && removeLink) {
            if (linha.toUpperCase().startsWith("RT ")) {
                return null;
            } else {
                return PreprocessoStrings.processar(linha, removeLink);
            }
        }

        if (removeAcent && removeRT) {
            if (linha.toUpperCase().startsWith("RT ")) {
                return null;
            } else {
                return PreprocessoStrings.processar(linha, removeLink);
            }
        }

        if (removeRT && removeLink) {
            if (linha.toUpperCase().startsWith("RT ")) {
                return null;
            } else {
                return PreprocessoStrings.removeUrl(linha);
            }
        }

        if (removeAcent && removeLink) {
            return PreprocessoStrings.processar(linha, removeLink);
        }

        if (removeAcent) {
            return PreprocessoStrings.processar(linha, removeLink);
        }

        if (removeLink) {
            return PreprocessoStrings.removeUrl(linha);
        }

        if (removeRT) {
            if (linha.toUpperCase().startsWith("RT")) {
                return null;
            } else {
                return linha;
            }
        }

        return linha;
    }
    
    public void closeStreams(){
        try {
            writer.close();
            scan.close();
        } catch (IOException ex) {
            logger.error(ex);
        }
    }

}
