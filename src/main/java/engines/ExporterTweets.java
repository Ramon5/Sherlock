package engines;

import dao.TweetDAO;
import entidade.Coleta;
import entidade.Tweet;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.JTextField;
import tablemodel.TableModelColeta;
import util.PreprocessoStrings;
import view.CorpusGUI;

/**
 *
 * @author Ramon
 */
public class ExporterTweets {

    private File diretorio;
    private JProgressBar progress;
    private TableModelColeta modelColeta;
    private JTable tabela;
    private TweetDAO tweetDAO;
    private List<Tweet> tweets;
    private JProgressBar progress2;

    public ExporterTweets(JProgressBar progress, JTable tabela, TableModelColeta modelColeta) {
        tweetDAO = new TweetDAO();
        this.progress = progress;
        this.tabela = tabela;
        this.modelColeta = modelColeta;
    }

    public void setProgress2(JProgressBar progress2) {
        this.progress2 = progress2;
    }   
    

    public void setDiretorio(JTextField campo) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int value = chooser.showSaveDialog(null);
        if (value == JFileChooser.APPROVE_OPTION) {
            diretorio = chooser.getSelectedFile();
            campo.setText(diretorio.getAbsolutePath());
        } else {
            campo.setText(null);
        }
    }

    public void exportar(boolean url, boolean acentos, String opcao) {
        new Thread() {
            @Override
            public void run() {
                FileWriter fwriter = null;
                BufferedWriter writer = null;
                try {
                    if (diretorio != null) {
                        Coleta coleta = (Coleta) modelColeta.getSelecionado(tabela.getSelectedRow());
                        tweets = tweetDAO.getTweets(coleta, opcao);
                        String nomeArquivo = diretorio.getAbsolutePath()
                                + FileSystems.getDefault().getSeparator() + coleta.getTermo() + "-" + coleta.getData() + ".csv";
                        File arquivo = new File(nomeArquivo);
                        fwriter = new FileWriter(arquivo);
                        writer = new BufferedWriter(fwriter);
                        progress.setMaximum(tweets.size());
                        int i = 0;
                        for (Tweet t : tweets) {
                            StringBuilder str = new StringBuilder();
                            String texto = t.getTweet();
                            if (acentos) {
                                texto = PreprocessoStrings.processar(texto, url);
                            }
                            str.append(texto).append("|").append(t.getIdTweet()).append("|").append(t.getAutor()).append("|")
                                    .append(t.getIdUsuario()).append("|").append(t.getFavorite_count()).append("|").append(t.getLang())
                                    .append("|").append(t.getTo_user_id()).append("|")
                                    .append(t.getLatitude()).append("|").append(t.getLongitude()).append("|").append(t.getDatecreated());
                            writer.append(str.toString());
                            writer.newLine();
                            writer.flush();
                            i++;
                            progress.setValue(i);
                        }
                        tweets.clear();
                        fwriter.close();
                        writer.close();
                        JOptionPane.showMessageDialog(null, "Arquivo gerado com sucesso!");
                    }
                } catch (IOException ex) {
                    Logger.getLogger(CorpusGUI.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        if (fwriter != null) {
                            fwriter.close();
                        }
                        if (writer != null) {
                            writer.close();
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(CorpusGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
    }

    public void gerar(boolean tipo, boolean url, boolean acentos, String opcao) {
        if (tipo) {
            arquivoMestre(url, acentos, opcao);
        } else {
            subArquivos(url, acentos, opcao);
        }
    }

    private void arquivoMestre(boolean url, boolean acentos, String opcao) {
        new Thread() {
            @Override
            public void run() {
                Coleta coleta = (Coleta) modelColeta.getSelecionado(tabela.getSelectedRow());
                if (coleta != null) {
                    FileWriter fwriter = null;
                    BufferedWriter writer = null;
                    try {
                        String dirName = diretorio.getAbsolutePath() + FileSystems.getDefault().getSeparator() + coleta.getTermo();
                        File dir = new File(dirName);
                        if (!dir.exists()) {
                            dir.mkdir();
                        }
                        tweets = tweetDAO.getTweets(coleta, opcao);
                        String arquivo = dir.getAbsolutePath() + FileSystems.getDefault().getSeparator() + coleta.getTermo() + "-" + coleta.getData() + ".txt";
                        File file = new File(arquivo);
                        fwriter = new FileWriter(file);
                        writer = new BufferedWriter(fwriter);
                        progress2.setMaximum(tweets.size());
                        int i = 0;
                        for (Tweet t : tweets) {
                            String texto = t.getTweet();
                            if (acentos) {
                                texto = PreprocessoStrings.processar(texto, url);
                            }
                            writer.append(texto);
                            writer.newLine();
                            writer.flush();
                            i++;
                            progress2.setValue(i);
                        }
                        writer.close();
                        fwriter.close();
                        JOptionPane.showMessageDialog(null, "Arquivo gerado com sucesso!");
                        
                    } catch (IOException ex) {
                        Logger.getLogger(ExporterTweets.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        try {
                            if (fwriter != null) {
                                fwriter.close();
                            }
                            if (writer != null) {
                                writer.close();
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(ExporterTweets.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }
            }
        }.start();
    }

    private void subArquivos(boolean url, boolean acentos, String opcao) {
        new Thread() {
            @Override
            public void run() {
                Coleta coleta = (Coleta) modelColeta.getSelecionado(tabela.getSelectedRow());
                if (coleta != null) {
                    String dirName = diretorio.getAbsolutePath() + FileSystems.getDefault().getSeparator() + coleta.getTermo();
                    File dir = new File(dirName);
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                    tweets = tweetDAO.getTweets(coleta, opcao);
                    progress2.setMaximum(tweets.size());
                    int i = 0;
                    for (Tweet t : tweets) {
                        FileWriter fwriter = null;
                        BufferedWriter writer = null;
                        try {
                            String file = dir.getAbsolutePath() + FileSystems.getDefault().getSeparator() + (i+1) + "-" + coleta.getTermo() + "-" + t.getDatecreated() + ".txt";
                            fwriter = new FileWriter(new File(file));
                            writer = new BufferedWriter(fwriter);
                            String texto = t.getTweet();
                            if (acentos) {
                                texto = PreprocessoStrings.processar(texto, url);
                            }
                            writer.write(texto);
                            writer.flush();                            
                            i++;
                            progress2.setValue(i);
                            writer.close();
                            fwriter.close();
                        } catch (IOException ex) {
                            Logger.getLogger(ExporterTweets.class.getName()).log(Level.SEVERE, null, ex);
                        } finally {
                            try {
                                if (fwriter != null) {
                                    fwriter.close();
                                }
                                if (writer != null) {
                                    writer.close();
                                }
                            } catch (IOException ex) {
                                Logger.getLogger(ExporterTweets.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    JOptionPane.showMessageDialog(null, "Arquivos gerados com sucesso!");
                }
            }
        }.start();
    }

    public void close() {
        if (tweetDAO != null) {
            tweetDAO.closeConnection();
        }
    }
}
