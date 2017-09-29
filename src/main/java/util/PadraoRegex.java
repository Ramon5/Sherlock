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

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author Rodrigues
 */
public class PadraoRegex {

    private final String padraoDataCsv = "\\|\\w{3}\\s(.*?)\\d{4}\\|{0,1}";
    private final String padraoDataTxt = "\\d{2}-\\d{2}-\\d{4}\\s\\d{2}:\\d{2}:\\d{2}\\|$";
    private final String padraoTokenCsv = "\\w(.*?)$";
    private final String padraoTweet = "\\|(.*?)$";
    private final String padraoTokenTxt = "\\|\\w(.*?)\\d\\|$";
    private final String padraoAno = "\\d{4}$";
    private final String padraoDiaMes = "[A-z]{3}\\s\\d{2}";
    private final String padraoHora = "\\d{2}:\\d{2}:\\d{2}";
    private final Logger logger;

    public PadraoRegex() {
        InputStream in = this.getClass().getResourceAsStream("/log4j/log4j.properties");
        PropertyConfigurator.configure(in);
        logger = Logger.getLogger(PadraoRegex.class);
    }    
   

    public String getPadraoDataCsv() {
        return padraoDataCsv;
    }

    public String getPadraoDataTxt() {
        return padraoDataTxt;
    }

    public String getPadraoTokenCsv() {
        return padraoTokenCsv;
    }

    public String getPadraoTweetCsv() {
        return padraoTweet;
    }

    public String getPadraoTokenTxt() {
        return padraoTokenTxt;
    }

    public String getPadraoAno() {
        return padraoAno;
    }

    public String getPadraoDiaMes() {
        return padraoDiaMes;
    }

    public String getPadraoHora() {
        return padraoHora;
    }
    
    public String obterCidade(String texto){
        Pattern p = Pattern.compile("\\,\\s\\D(.*?)\\s-\\s(\\w[A-Z])");
        Matcher m = p.matcher(texto);
        String cidade = "";
        if(m.find()){
            cidade = m.group().replaceAll(",", "").trim();
        }
        
        return cidade;
    }

    public String obterTweetTxt(String texto) {
        Pattern p = Pattern.compile(padraoTokenTxt);
        Matcher m = p.matcher(texto);
        String text = "";
        if (m.find()) {
            text = m.group();
        }
        text = text.replaceAll(padraoDataTxt + "|^\\|", "");

        return text;
    }

    public String obterDataTxt(String texto) {
        Pattern p = Pattern.compile(padraoDataTxt);
        Matcher m = p.matcher(texto);
        String data = "";
        if (m.find()) {
            data = m.group();
        }
        data = data.replaceAll("\\|$", "");
        return data;
    }
    
    public String removerDelimitadores(String texto){
        texto = texto.replaceAll("^\\||\\|$", "");
        return texto;
    }

    public String parseTxtFormat(String texto) {
        StringBuilder strbuilder = new StringBuilder();
        Pattern pattern = Pattern.compile(padraoDataCsv);
        Matcher m = pattern.matcher(texto);
        String data = "";
        if (m.find()) {
            data = m.group();
        }
        String tweet = texto.replaceAll(padraoTweet, "");

        strbuilder.append("|").append(tweet).append(" ").append(data).append("|");

        return strbuilder.toString();
    }

    public String obterTweetCsv(String texto) {
        Pattern p = Pattern.compile(padraoTweet);
        Matcher m = p.matcher(texto);
        String exclusao = "";
        if (m.find()) {
            exclusao = m.group();
        }
        texto = texto.replace(exclusao, "");
        return texto;
    }

    public String obterDataCsv(String texto) {
        try {
            StringBuilder str = new StringBuilder();
            Pattern padraoData = Pattern.compile(padraoDataCsv);
            Matcher mD = padraoData.matcher(texto);
            String data = "";
            if (mD.find()) {
                data = mD.group().replaceAll("^\\||\\|$", "");
            }
            Pattern padraoDM = Pattern.compile(padraoDiaMes);
            Matcher mDM = padraoDM.matcher(data);
            String diaMes = "";
            if (mDM.find()) {
                diaMes = mDM.group();
            }
            Pattern padraoA = Pattern.compile(padraoAno);
            Matcher mAno = padraoA.matcher(data);
            String ano = "";
            if (mAno.find()) {
                ano = mAno.group();
            }
            diaMes = diaMes.replace(" ", "|");
            String[] parte = diaMes.split("\\|");
            Pattern padraoH = Pattern.compile(padraoHora);
            Matcher mH = padraoH.matcher(data);
            String horario = "";
            if (mH.find()) {
                horario = mH.group();
            }
            String mes = obterMesNumerico(parte[0]);
            str.append(parte[1]).append("-").append(mes).append("-").append(ano).append(" ").append(horario);

            return str.toString();

        } catch (NullPointerException ex) {
            logger.error(ex);
        }
        return "";
    }

    private String obterMesNumerico(String abrv) {
        switch (abrv) {
            case "Jan":
                return "01";
            case "Fev":
                return "02";
            case "Mar":
                return "03";
            case "Abr":
                return "04";
            case "Mai":
                return "05";
            case "Jun":
                return "06";
            case "Jul":
                return "07";
            case "Aug":
                return "08";
            case "Sep":
                return "09";
            case "Oct":
                return "10";
            case "Nov":
                return "11";
            case "Dec":
                return "12";
        }

        return null;
    }

}
