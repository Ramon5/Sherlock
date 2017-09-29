/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package engines;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import util.PadraoRegex;
import util.PreprocessoStrings;

/**
 *
 * @author root
 */
public class WatsonConverter extends Thread {

    private JButton botao;

    private FileWriterWithEncoding writer;
    private BufferedWriter buff;
    private Scanner leitor;

    private final JProgressBar progress;

    private File entrada;
    private File saida;

    private boolean removeAcent;
    private boolean removeLink;
    private boolean removeRT;
    private boolean textoPuro;

    private File container;
    private final PadraoRegex padrao;

    private static int indice;

    private final Logger logger;

    public WatsonConverter(JProgressBar progress) {
        this.progress = progress;
        InputStream in = this.getClass().getResourceAsStream("/log4j/log4j.properties");
        PropertyConfigurator.configure(in);
        this.logger = Logger.getLogger(WatsonConverter.class);
        padrao = new PadraoRegex();
    }

    public void setEntrada(File entrada) {
        this.entrada = entrada;
    }

    public File getSaida() {
        return saida;
    }

    public void setSaida(File saida) {
        this.saida = saida;
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

    public void setTextoLimpo(boolean textoPuro) {
        this.textoPuro = textoPuro;
    }

    private void converterArquivos(File source, File destino) throws IOException {
        if (source.isDirectory()) {
            if (!destino.exists()) {
                destino.mkdir();
            }
            String[] childrens = source.list();
            for (String children : childrens) {
                converterArquivos(new File(source, children), new File(destino, children));
            }
        } else {
            if (source.getName().endsWith(".csv")) {
                int i = 0;
                String[] arquivoNovaExtensao = source.getName().split(Pattern.quote("."));
                File novo = null;
                if(entrada.isDirectory()){
                    novo = new File(destino.getParent() + "/" + arquivoNovaExtensao[0] + ".txt");
                }else{
                    novo = new File(destino + "/" + arquivoNovaExtensao[0] + ".txt");
                }
                writer = new FileWriterWithEncoding(novo, StandardCharsets.UTF_8, true);
                buff = new BufferedWriter(writer);
                leitor = new Scanner(new FileReader(source)).useDelimiter(System.getProperty("line.separator"));

                while (leitor.hasNext()) {
                    String linha = leitor.next();

                    String texto = recycle(linha);

                    if (texto != null) {
                        texto = padrao.parseTxtFormat(texto);
                        buff.append(texto);
                        buff.newLine();
                        buff.flush();
                    }
                    if (entrada.isFile()) {
                        i++;
                        progress.setValue(i);
                    }
                }

                leitor.close();
            }
            if (entrada.isDirectory()) {
                indice++;
                progress.setValue(indice);
            }
        }

    }

    private void mesclarColeta(File entrada) {
        if (entrada.canRead()) {
            File[] arquivos = entrada.listFiles();
            progress.setMaximum(arquivos.length);
            for (File file : arquivos) {
                progress.setValue(indice);
                if (file.isDirectory()) {
                    mesclarColeta(file);
                } else {
                    if (file.getName().endsWith(".txt")) {
                        try {
                            writer = new FileWriterWithEncoding(container, StandardCharsets.UTF_8, true);
                            buff = new BufferedWriter(writer);
                            leitor = new Scanner(new FileReader(file)).useDelimiter(System.getProperty("line.separator"));
                            while (leitor.hasNext()) {
                                String token = leitor.next().trim();
                                String texto = recycle(token);
                                if (texto != null) {
                                    texto = padrao.removerDelimitadores(texto);
                                    buff.append(texto);
                                    buff.newLine();
                                    buff.flush();
                                }
                                indice++;
                            }

                            closeStreams();

                        } catch (IOException ex) {
                            logger.error(ex);
                            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
                        } catch (NoSuchElementException ex) {
                            logger.error(ex);
                        }
                    }
                }
            }
        }
    }

    private String recycle(String linha) {

        if (removeRT && removeAcent && removeLink) {
            if (linha.toUpperCase().startsWith("RT ") || linha.toUpperCase().startsWith("\"RT ")) {
                return null;
            } else {
                return PreprocessoStrings.processar(linha, removeLink);
            }
        }

        if (removeRT && removeAcent) {
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

        if (removeRT) {
            if (linha.toUpperCase().startsWith("RT ")) {
                return null;
            }
        }

        if (removeAcent) {
            return PreprocessoStrings.processar(linha, removeLink);
        }

        if (removeLink) {
            return PreprocessoStrings.removeUrl(linha);

        }

        return linha;
    }

    private void criarContainer() {
        container = new File(saida.getAbsolutePath() + FileSystems.getDefault().getSeparator() + entrada.getName() + ".txt");
    }

    private void closeStreams() {
        try {
            writer.close();
            buff.close();
            leitor.close();
        } catch (IOException ex) {
            logger.error(ex);
            JOptionPane.showMessageDialog(null, "Erro: " + ex.getLocalizedMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void run() {

        if (textoPuro) {
            indice = 1;
            botao.setEnabled(false);
            criarContainer();
            mesclarColeta(saida);

            JOptionPane.showMessageDialog(null, "Arquivo gerado com sucesso!");
        } else {
            try {
                botao.setEnabled(false);
                int total = 0;
                if (entrada.isDirectory()) {
                    File[] arquivos = entrada.listFiles();
                    total = arquivos.length;
                } else {
                    total = 100;
                }
                progress.setMaximum(total);
                converterArquivos(entrada, saida);
                JOptionPane.showMessageDialog(null, "Arquivos processados com sucesso!");
            } catch (IOException ex) {
                ex.printStackTrace();
                logger.error(ex);
            }
        }
        botao.setEnabled(true);
        interrupt();

    }

}
