/*
 * Copyright 2016 root.
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
 *  * Nem o nome do Expression project.organization!organization is undefined on line 17, column 36 in Templates/Licenses/license-bsd_3-pt_br.txt. nem os nomes dos seus contribuidores 
 *   podem ser utilizados para endossar ou promover *produtos derivados deste software sem
 *   autorização prévia específica por escrito.
 *
 * ESTE SOFTWARE É FORNECIDO PELOS DETENTORES DE COPYRIGHT E COLABORADORES "NO ESTADO EM QUE SE ENCONTRAM"
 * E QUAISQUER GARANTIAS *EXPRESSAS OU IMPLÍCITAS, INCLUINDO, MAS NÃO SE LIMITANDO A,
 * GARANTIAS IMPLÍCITAS DE COMERCIABILIDADE E ADEQUAÇÃO A UM PROPÓSITO ESPECÍFICO.
 * EM NENHUMA CIRCUNSTÂNCIA O PROPRIETÁRIO OU OS CONTRIBUIDORES SERÃO
 * RESPONSÁVEIS POR QUAISQUER DANOS DIRETOS, INDIRETOS, INCIDENTAIS, ESPECIAIS, EXEMPLARES OU
 * CONSEQÜENCIAIS (INCLUINDO, MAS NÃO SE LIMITANDO À, AQUISIÇÃO DE
 * BENS OU SERVIÇOS SUBSTITUTOS, *PERDA DE USO, DADOS OU LUCROS; OU INTERRUPÇÃO DE NEGÓCIOS),
 * QUALQUER CAUSA E QUALQUER TEORIA DE RESPONSABILIDADE,  
 * SEJA POR CONTRATO, RESPONSABILIDADE ESTRITA OU DANO (INCLUINDO NEGLIGÊNCIA OU QUALQUER OUTRA)
 * DECORRENTE DE QUALQUER FORMA FORA DO USO DESTE SOFTWARE, MESMO SE AVISADO DA 
 * POSSIBILIDADE DE TAIS DANOS.
 */
package inativos;

import inativos.Georeferencia;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import util.AutenticacaoAPI;
import view.ImagePanel;

/**
 *
 * @author root
 */
public class Georeferenciamento implements Listageo {

    private BufferedImage imagem;
    private final JPanel container;
    private List<Georeferencia> lista;
    private File entrada;
    private boolean arquivo;
    private final Logger logger;
    private final Georeferencia[] vetorGeo;
    private ArquivoGeo obj;
    private Long total;
    private Long quantidade;

    /*    
        Esquema de funcionamento:
    
        1 - processarEntrada() -
        2 - obterOcorrencias() +
            2.1 - amostrar() -
            2.2 - processarRepetições() -        
     */
    public void setEntrada(File entrada) {
        this.entrada = entrada;
    }

    public Georeferenciamento(JPanel container) {
        this.container = container;
        InputStream in = this.getClass().getResourceAsStream("/log4j/log4j.properties");
        PropertyConfigurator.configure(in);
        this.logger = Logger.getLogger(Georeferenciamento.class);
        vetorGeo = new Georeferencia[5];
    }

    public void definirEntrada(boolean externo) {
        this.arquivo = externo;
        if (externo) {
            this.lista = processarEntrada();
        } else {
            this.lista = listGeoloc;
        }
    }

    public void limparPainel() {
        container.removeAll();
    }

    public Georeferencia[] getToplist() {
        return vetorGeo;
    }
    
    
    /**
     * Processamento do tipo de entrada (Processo corrente ou arquivo gerado),
     * quando houver geolocalização disponível
     *
     * @return
     */
    private List<Georeferencia> processarEntrada() {
        if (arquivo) {
            try {
                this.lista = new ArrayList<>();
                if (entrada != null) {

                    InputStream in = new FileInputStream(entrada);
                    ObjectInputStream stream = new ObjectInputStream(in);
                    obj = (ArquivoGeo) stream.readObject();
                    this.total = obj.getTotal();
                    this.quantidade = obj.getQuantidade();

                    for (String linha : obj.getCoordenadas()) {
                        Georeferencia geo = new Georeferencia();
                        String localizacao = linha.replaceAll("\\|", ",");
                        geo.setLocal(downloadEnderecos(localizacao));
                        geo.setCoordenadas(linha);
                        lista.add(geo);
                    }

                    in.close();
                    stream.close();                  

                    return lista;

                } else {
                    JOptionPane.showMessageDialog(null, "Arquivo não selecionado!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    return null;
                }
            } catch (FileNotFoundException ex) {
                logger.error(ex);
                JOptionPane.showMessageDialog(null, "Erro: " + ex.getLocalizedMessage());
            } catch (IOException | ClassNotFoundException ex) {
                logger.error(ex);
                JOptionPane.showMessageDialog(null, "Erro: " + ex.getLocalizedMessage());
            }
        } else {
            return listGeoloc;
        }
        return null;
    }

    /**
     * Calcula a frequencia de um local na amostra e armazena na variável do
     * objeto a ser analisado pelo metodo 'ocorrencia()'
     *
     * @return
     */
    private boolean contarFrequencia() {        
        if (lista != null && lista.size() > 0) {
            int count = 1;
            for (int i = 0; i < lista.size(); i++) {
                Georeferencia g1 = lista.get(i);
                for (int j = 0; j < lista.size(); j++) {                    
                    if (j != i) {
                        Georeferencia g2 = lista.get(j);
                        if (g1.getCoordenadas().hashCode() == g2.getCoordenadas().hashCode()) {
                            count++;
                            lista.get(i).setOcorrencia(count);
                        }else{
                            lista.get(i).setOcorrencia(count);
                        }
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verifica quais locais possuem a mesma incidencia, e elimina-os da lista
     * de amostras
     */
    private void eliminarRepeticoes() {
        for (int i = 0; i < lista.size(); i++) {
            String localA = lista.get(i).getLocal();
            for (int j = 0; j < lista.size(); j++) {
                if (j != i) {
                    String localB = lista.get(j).getLocal();
                    if (localA.hashCode() == localB.hashCode()) {
                        lista.remove(j);
                    }
                }
            }
        }
    }

    /**
     * Obter o lugar de maior incidencia de um determinado acontecimento
     *
     */
    public void obterOcorrencias() {
        if (contarFrequencia()) {
            eliminarRepeticoes();
            Collections.sort(lista, new OrdenacaoIncidencias());
            Collections.reverse(lista);
            for (int i = 0; i < vetorGeo.length; i++) {
                vetorGeo[i] = lista.get(i);
            }
            downloadMaps();

        }

    }

    public void exibirMapa(Georeferencia local) {
        if (local != null) {
            updatePanel();
            container.add(new ImagePanel(new ImageIcon(local.getMap())), BorderLayout.CENTER);
            container.updateUI();
        } else {
            JOptionPane.showMessageDialog(null, "Não há locais a serem exibidos!");
        }
    }

    private void downloadMaps() {
        if (AutenticacaoAPI.autenticadoMaps) {
            int width = container.getWidth();
            int heigth = container.getHeight() - 10;
            for (Georeferencia local : vetorGeo) {
                try {

                    String coordenadas = local.getCoordenadas().replaceAll("\\|", ",");

                    String url = "https://maps.googleapis.com/maps/api/staticmap?" + parameters(coordenadas, width, heigth, AutenticacaoAPI.chaveAPI);

                    imagem = ImageIO.read(new URL(url));

                    local.setMap(imagem);

                } catch (MalformedURLException ex) {
                    logger.error(ex);
                } catch (IOException ex) {
                    logger.error(ex);
                }
            }
        }
    }

    private void updatePanel() {
        container.removeAll();
        container.updateUI();
    }

    private String downloadEnderecos(String coordenadas) {
        String endereco = "";
        try {

            GeocodingResult[] result = GeocodingApi.geocode(AutenticacaoAPI.context, coordenadas).await();
            endereco = result[0].formattedAddress;

            return endereco;

        } catch (Exception ex) {
            logger.error(ex);
        }
        return endereco;
    }

    /**
     * Parametros a serem passados para o GoogleAPI
     *
     * @param incidencia
     * @param width
     * @param height
     * @param key
     * @return
     */
    private String parameters(String incidencia, int width, int height, String key) {
        StringBuilder params = new StringBuilder();
        params.append("center=").append(incidencia);
        params.append("&markers=color:red%7Clabel:S%7C").append(incidencia);
        params.append("&sensor=false");
        params.append("&zoom=12");
        params.append("&size=").append(width).append("x").append(height);
        params.append("&key=").append(key);
        params.append("&maptype=roadmap");
        params.append("&path=weight:3%7Ccolor:blue%7Cenc:");
        params.append("&format=jpg");
        params.append("&visual_refresh=true");

        return params.toString();
    }


    public double getPercentual(){
        double percent = quantidade / total;
        return percent;
    }

    
    
}
