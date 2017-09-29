/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engines;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import util.DetectaSistema;
import static util.DetectaSistema.detectarSistema;
import util.PadraoRegex;
import util.PreprocessoStrings;

/**
 *
 * @author root
 */
public class WatsonFragment extends Thread implements DetectaSistema {

    private File saida;
    private File entrada;
    private final Logger logger;
    private final JProgressBar progresso;
    private JButton botao;

    private boolean removeAcent;
    private boolean removeLink;
    private boolean removeRT;
    private boolean separarEmDiretorios;

    private final List<TokenTweet> listToken;
    private Scanner scan;
    private BufferedWriter buff;
    private FileWriterWithEncoding writer;

    private JLabel labelQtde;
    private long qtdArquivosGerados;
    private final PadraoRegex padrao;

    public WatsonFragment(JProgressBar progresso) {
        InputStream in = this.getClass().getResourceAsStream("/log4j/log4j.properties");
        PropertyConfigurator.configure(in);
        this.logger = Logger.getLogger(WatsonFragment.class);
        this.progresso = progresso;
        listToken = new ArrayList<>();
        qtdArquivosGerados = 1;
        padrao = new PadraoRegex();

    }

    public void setLabelQtde(JLabel labelQtde) {
        this.labelQtde = labelQtde;
    }

    public void setBotao(JButton botao) {
        this.botao = botao;
    }

    public void setRemoveLink(boolean removeLink) {
        this.removeLink = removeLink;
    }

    public void setRemoveAcent(boolean removeAcent) {
        this.removeAcent = removeAcent;
    }

    public void setRemoveRT(boolean removeRT) {
        this.removeRT = removeRT;
    }

    public void setEntrada(File entrada) {
        this.entrada = entrada;
    }

    public void setSaida(File saida) {
        this.saida = saida;
    }

    public void setSepararEmDiretorios(boolean separarEmDiretorios) {
        this.separarEmDiretorios = separarEmDiretorios;
    }

    /**
     * Inicio da execução da Thread
     */
    @Override
    public void run() {

        botao.setEnabled(false);
        desmembrar();
        botao.setEnabled(true);
        interrupt();
        JOptionPane.showMessageDialog(null, "Arquivo processado com sucesso!");

    }

    /**
     * Verifica se é para separar os arquivos separando os respectivos
     * diretórios ou se é para separar os arquivos num único diretório
     */
    private void desmembrar() {
        if (separarEmDiretorios) {
            separarHierarquia(entrada, saida);
        } else {
            separarGlobal(entrada);
        }
    }

    /**
     * Separa cada tweet em arquivos individuais, mantendo a hierarquia de
     * diretorios exatamente como os da origem
     *
     * @param source
     * @param target
     */
    private void separarHierarquia(File source, File target) {
        if (source.isDirectory()) {
            String[] childrens = source.list();
            for (String children : childrens) {
                separarHierarquia(new File(source, children), new File(target, children));
            }

        } else {
            separarTokens(source);
            gerarArquivos(source, target);

        }

    }

    /**
     * Gera os tokens de todos os arquivos contidos na origem e preenche a lista
     * de objetos tokentweet para ser usada no método 'separarGlobal'
     *
     * @param srcDir
     */
    private void gerarTokensGlobal(File srcDir) {
        if (srcDir.isDirectory()) {
            gerarTokensGlobal(srcDir);
        } else {
            separarTokens(srcDir);
        }
    }

    /**
     * Separa os tweets contidos na lista de objetos tokentweet, para serem
     * gravados em um único diretório
     *
     * @param srcDir
     */
    private void separarGlobal(File srcDir) {

        gerarTokensGlobal(srcDir);

        for (int i = 0; i < listToken.size(); i++) {
            try {

                String[] arq = listToken.get(i).getNome().split(Pattern.quote("."));
                String nomeArquivo = arq[0].replaceAll("\\(.*?\\)", "");

                String data = null;

                if (detectarSistema() == 0) {
                    data = listToken.get(i).getDataCreated();
                } else if (detectarSistema() == 1) {
                    data = listToken.get(i).getDataCreated().replaceAll(Pattern.quote(":"), ";");
                }

                String texto = processar(listToken.get(i).getText());

                if (texto != null) {
                    File novo = new File(saida.getAbsolutePath() + FileSystems.getDefault().getSeparator() + (i + 1) + "-" + nomeArquivo + " " + data + ".txt");
                    writer = new FileWriterWithEncoding(novo, StandardCharsets.UTF_8);
                    buff = new BufferedWriter(writer);

                    buff.append(texto);
                    buff.flush();
                    buff.close();

                    labelQtde.setText(String.valueOf(qtdArquivosGerados++));
                }
            } catch (IOException ex) {
                logger.error(ex);
                JOptionPane.showMessageDialog(null, "Erro: " + ex.getLocalizedMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }

            progresso.setValue(i + 1);

        }

        listToken.clear();
    }

    /**
     * Realiza a leitura dos delimitadores contidos no arquivo de tweets, Separa
     * a informação em texto e data de criação (Objeto TokenTweet) armazena numa
     * lista de objetos TokenTweet
     *
     * @param origem
     */
    public void separarTokens(File origem) {
        detectarPadrao(origem);
        /*if (origem.getName().endsWith(".txt")) {
            padraoTxt(origem);
        } else if (origem.getName().endsWith(".csv")) {
            padraoCsv(origem);
        }*/
    }

    private void detectarPadrao(File origem) {
        try {
            scan = new Scanner(origem).useDelimiter(System.getProperty("line.separator"));
            String linha = "";
            if (scan.hasNext()) {
                linha = scan.next().trim();
            }
            String token = "\\|\\d{10}$";
            Pattern pattern = Pattern.compile(token);
            Matcher m = pattern.matcher(linha);
            if (m.find()) {
                padraoCsv(origem);
            } else {
                padraoTxt(origem);
            }
            scan.close();

        } catch (FileNotFoundException ex) {
            logger.error(ex);
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getLocalizedMessage());
        }
    }

    private void padraoTxt(File origem) {
        try {
            Pattern pattern = Pattern.compile(Pattern.quote("|"));
            scan = new Scanner(new FileReader(origem)).useDelimiter(pattern);

            while (scan.hasNext()) {
                String linha = scan.next().trim();
                if (!linha.isEmpty()) {

                    TokenTweet token = new TokenTweet();
                    token.setNome(origem.getName());
                    token.setText(padrao.obterTweetTxt(linha));
                    token.setDataCreated(padrao.obterDataTxt(linha));

                    listToken.add(token);
                }

            }
            if (listToken.size() > 0) {
                progresso.setMaximum(listToken.size());
            }

        } catch (FileNotFoundException ex) {
            logger.error(ex);
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getLocalizedMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (ArrayIndexOutOfBoundsException ex) {
            logger.error(ex);
            JOptionPane.showMessageDialog(null, "Erro: Um ou mais arquivos não estão no padrão correto!");
        }
    }

    private void padraoCsv(File entrada) {
        try {

            scan = new Scanner(new FileReader(entrada)).useDelimiter(System.getProperty("line.separator"));

            while (scan.hasNext()) {
                String linha = scan.next().trim();
                if (!linha.isEmpty() && !linha.startsWith("TEXT|")) {

                    TokenTweet token = new TokenTweet();
                    token.setNome(entrada.getName());
                    token.setText(padrao.obterTweetCsv(linha));
                    token.setDataCreated(padrao.obterDataCsv(linha));

                    listToken.add(token);
                }

            }
            if (listToken.size() > 0) {
                progresso.setMaximum(listToken.size());
            }

        } catch (FileNotFoundException ex) {
            logger.error(ex);
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getLocalizedMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (ArrayIndexOutOfBoundsException ex) {
            logger.error(ex);
            JOptionPane.showMessageDialog(null, "Erro: Um ou mais arquivos não estão no padrão correto!");
        }
    }

    /**
     * Lê a lista de objetos TokenTweet e grava os arquivos no diretorio de
     * destino
     *
     * @param origem
     * @param destino
     */
    public void gerarArquivos(File origem, File destino) {
        String[] arq = origem.getName().split(Pattern.quote("."));
        String nomeArquivo = arq[0].replaceAll("\\(.*?\\)", "");
        int indice = 1;

        for (int i = 0; i < listToken.size(); i++) {
            try {

                String data = null;

                if (detectarSistema() == 0) {
                    data = listToken.get(i).getDataCreated();
                } else if (detectarSistema() == 1) {
                    data = listToken.get(i).getDataCreated().replaceAll(Pattern.quote(":"), ";");
                }

                String texto = processar(listToken.get(i).getText());
                File dOut = new File(destino.getParent());

                if (!dOut.exists()) {
                    dOut.mkdirs();
                }

                if (texto != null) {
                    File novo = new File(dOut + FileSystems.getDefault().getSeparator() + indice + "-" + nomeArquivo + " " + data + ".txt");
                    writer = new FileWriterWithEncoding(novo, StandardCharsets.UTF_8);
                    buff = new BufferedWriter(writer);

                    buff.append(texto);
                    buff.flush();
                    buff.close();

                    labelQtde.setText(String.valueOf(qtdArquivosGerados++));
                    indice++;
                }
            } catch (IOException ex) {
                logger.error(ex);
                //JOptionPane.showMessageDialog(null, "Erro: " + ex.getLocalizedMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }

            progresso.setValue(i + 1);

        }
        listToken.clear();
    }

    /**
     * Realiza a limpeza dos tweets
     *
     * @param linha
     * @return
     */
    private String processar(String linha) {

        if (removeRT && removeAcent && removeLink) {
            if (linha.toUpperCase().startsWith("RT ") || linha.toUpperCase().startsWith("\"RT ")) {
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
            if (linha.toUpperCase().startsWith("RT ")) {
                return null;
            } else {
                return linha;
            }
        }

        return linha;
    }

}
