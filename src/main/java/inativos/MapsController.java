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
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import inativos.Georeferencia;
import inativos.Georeferenciamento;
import util.PadraoRegex;
import static view.MapsGUI.btnAbrir;
import static view.MapsGUI.campoDir;
import static view.MapsGUI.painelMapa;
import static view.MapsGUI.primeiro;
import static view.MapsGUI.quarto;
import static view.MapsGUI.quinto;
import static view.MapsGUI.rdArquivoExt;
import static view.MapsGUI.rdProcessoAtual;
import static view.MapsGUI.segundo;
import static view.MapsGUI.terceiro;

/**
 *
 * @author root
 */
public class MapsController {
    
    private static Georeferenciamento dados;
    private static Georeferencia locais[];
    
    public static void inicializar(){
        dados = new Georeferenciamento(painelMapa);
        dados.obterOcorrencias();
        locais = dados.getToplist();
    }
    
    public static void mapaAutomatico(){
        dados.exibirMapa(locais[0]);
    }
    
    public static void limparInformacoes(){
        dados.limparPainel();
        painelMapa.removeAll();
        primeiro.setText("...");
        segundo.setText("...");
        terceiro.setText("...");
        quarto.setText("...");
        quinto.setText("...");
        dados = null;
    }
    
    public static void verificarSeletores(){
         if (rdProcessoAtual.isSelected()) {
            campoDir.setEnabled(false);
            btnAbrir.setEnabled(false);
        } else {
            campoDir.setEnabled(true);
            btnAbrir.setEnabled(true);
        }
    }
    
    public static void visualizarMapa(){
        if (dados == null) {
            dados = new Georeferenciamento(painelMapa);
        }
        dados.definirEntrada(rdArquivoExt.isSelected());
        dados.obterOcorrencias();
        locais = dados.getToplist();
        if (locais.length > 0) {
            dados.exibirMapa(locais[0]);
            preencherInformacoes();
        }else{
            JOptionPane.showMessageDialog(null, "Não há locais a serem exibidos!");
        }
    }
    
    public static void getEntrada(){
        if (dados == null) {
            dados = new Georeferenciamento(painelMapa);
        }
        File arq = selecionaEntrada();
        if (arq != null) {
            dados.setEntrada(arq);
        }
    }
    
    public static void exibirMapa(int i){
        if (locais.length > 0) {
            dados.exibirMapa(locais[i]);
        }
    }
    
    private static void preencherInformacoes() {
        PadraoRegex padrao = new PadraoRegex();
        if (locais != null && locais.length > 0) {
            int tamanho = locais.length;
            if (tamanho == 1) {
                primeiro.setText(padrao.obterCidade(locais[0].getLocal()));
            } else if (tamanho <= 2) {
                primeiro.setText(padrao.obterCidade(locais[0].getLocal()));
                segundo.setText(padrao.obterCidade(locais[1].getLocal()));
            } else if (tamanho <= 3) {
                primeiro.setText(padrao.obterCidade(locais[0].getLocal()));
                segundo.setText(padrao.obterCidade(locais[1].getLocal()));
                terceiro.setText(padrao.obterCidade(locais[2].getLocal()));
            } else if (tamanho <= 4) {
                primeiro.setText(padrao.obterCidade(locais[0].getLocal()));
                segundo.setText(padrao.obterCidade(locais[1].getLocal()));
                terceiro.setText(padrao.obterCidade(locais[2].getLocal()));
                quarto.setText(padrao.obterCidade(locais[3].getLocal()));
            } else if (tamanho >= 5) {
                primeiro.setText(padrao.obterCidade(locais[0].getLocal()));
                segundo.setText(padrao.obterCidade(locais[1].getLocal()));                
                terceiro.setText(padrao.obterCidade(locais[2].getLocal()));                
                quarto.setText(padrao.obterCidade(locais[3].getLocal()));
                quinto.setText(padrao.obterCidade(locais[4].getLocal()));
            }
        }
    }
    
    private static File selecionaEntrada() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Arquivos de Geolocalização", "geo");
        fileChooser.addChoosableFileFilter(filtro);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.showOpenDialog(null);
        File selecionado = fileChooser.getSelectedFile();
        if (selecionado != null) {
            campoDir.setText(selecionado.getAbsolutePath());
        }
        return fileChooser.getSelectedFile();

    }
    
}
