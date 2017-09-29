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
package control;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import engines.WatsonConverter;
import engines.WatsonFragment;
import view.WatsonGUI;
import static view.WatsonGUI.barraProgressoCond;
import static view.WatsonGUI.botaoCondensar;
import static view.WatsonGUI.botaoDesmembrar;
import static view.WatsonGUI.btnProcessar;
import static view.WatsonGUI.campoEntrada;
import static view.WatsonGUI.chkAcent;
import static view.WatsonGUI.chkAcent2;
import static view.WatsonGUI.chkAcentCond;
import static view.WatsonGUI.chkRT;
import static view.WatsonGUI.chkRemoveLink;
import static view.WatsonGUI.chkRemoveLink2;
import static view.WatsonGUI.chkRemoveLinkCond;
import static view.WatsonGUI.chkRemoveRT2;
import static view.WatsonGUI.chkRemoveRTCond;
import static view.WatsonGUI.progressDesm;
import static view.WatsonGUI.progresso;
import static view.WatsonGUI.qtdArquivos;
import static view.WatsonGUI.rdbMultiplos;
import static view.WatsonGUI.rdbUnico;

/**
 *
 * @author root
 */
public class WatsonController {

    private static File entrada;
    private static File saida;

    public static void converter() {
        WatsonConverter convert = new WatsonConverter(WatsonGUI.progresso);
        //convert = new WatsonConverter(progresso);
        convert.setEntrada(entrada);
        convert.setSaida(saida);
        convert.setRemoveLink(chkRemoveLink.isSelected());
        convert.setRemoveRT(chkRT.isSelected());
        convert.setRemoveAcent(chkAcent.isSelected());
        convert.setBotao(btnProcessar);
        convert.start();
    }

    public static void desmembrar() {
        WatsonFragment desm = new WatsonFragment(progressDesm);
        desm.setEntrada(entrada);
        desm.setSaida(saida);
        desm.setRemoveLink(chkRemoveLink2.isSelected());
        desm.setRemoveRT(chkRemoveRT2.isSelected());
        desm.setRemoveAcent(chkAcent2.isSelected());
        desm.setBotao(botaoDesmembrar);
        desm.setLabelQtde(qtdArquivos);
        desm.setSepararEmDiretorios(verificaParametro());
        desm.start();
    }

    public static void mesclar() {
        WatsonConverter texto = new WatsonConverter(barraProgressoCond);
        texto.setEntrada(entrada);
        texto.setTextoLimpo(true);
        texto.setSaida(saida);
        texto.setRemoveLink(chkRemoveLinkCond.isSelected());
        texto.setRemoveAcent(chkAcentCond.isSelected());
        texto.setRemoveRT(chkRemoveRTCond.isSelected());
        texto.setBotao(botaoCondensar);
        texto.start();
    }


    public static String getEntrada() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter txt = new FileNameExtensionFilter("Arquivos de Texto", "txt");
        FileNameExtensionFilter csv = new FileNameExtensionFilter("Arquivos CSV", "csv");
        fileChooser.addChoosableFileFilter(txt);
        fileChooser.addChoosableFileFilter(csv);
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.showOpenDialog(null);
        entrada = fileChooser.getSelectedFile();
        if (fileChooser.getSelectedFile() != null) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            return "";
        }
    }

    public static String salvarArquivo() {
        JFileChooser salvandoArquivo = new JFileChooser();
        salvandoArquivo.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int select = salvandoArquivo.showOpenDialog(null);
        if (select != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        File dir = salvandoArquivo.getSelectedFile();
        if (dir != null) {
            saida = salvandoArquivo.getSelectedFile();
            return dir.getAbsolutePath();
        } else {
            return "";
        }
    }

    private static boolean verificaParametro() {
        if (rdbMultiplos.isSelected()) {
            return true;
        } else if (rdbUnico.isSelected()) {
            return false;
        }
        return false;
    }

}
