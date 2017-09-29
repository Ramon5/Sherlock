/*
 * Copyright (c) 2016, Ramon dos Santos Rodrigues
 * Todos os direitos reservados.
 *
 * É permitida a redistribuição e o uso em formulários originais e binários, com ou sem modificação, desde que sejam cumpridas as
 * seguintes condições:
 *
 * 1. Redistribuições do código-fonte devem manter o aviso de direitos autorais acima, esta lista de condições e a seguinte isenção.
 *
 * 2. As redistribuições em formato binário devem reproduzir o aviso de copyright acima, esta lista de condições ea seguinte isenção de 
 * responsabilidade na documentação e / ou outros materiais fornecidos com a distribuição.
 *
 * 3. Nem o nome do detentor dos direitos autorais nem os nomes dos seus contribuidores podem ser utilizados para endossar ou promover 
 * produtos derivados deste software sem autorização prévia específica por escrito.
 *
 * ESTE SOFTWARE É FORNECIDO PELOS DETENTORES DE COPYRIGHT E COLABORADORES "NO ESTADO EM QUE SE ENCONTRAM" E QUAISQUER GARANTIAS 
 * EXPRESSAS OU IMPLÍCITAS, INCLUINDO, MAS NÃO SE LIMITANDO A, GARANTIAS IMPLÍCITAS DE COMERCIABILIDADE E ADEQUAÇÃO A UM PROPÓSITO 
 * ESPECÍFICO. EM NENHUMA CIRCUNSTÂNCIA O PROPRIETÁRIO OU OS CONTRIBUIDORES SERÃO RESPONSÁVEIS POR QUAISQUER DANOS DIRETOS, INDIRETOS, 
 * INCIDENTAIS, ESPECIAIS, EXEMPLARES OU CONSEQÜENCIAIS (INCLUINDO, MAS NÃO SE LIMITANDO À, AQUISIÇÃO DE BENS OU SERVIÇOS SUBSTITUTOS, 
 * PERDA DE USO, DADOS OU LUCROS; OU INTERRUPÇÃO DE NEGÓCIOS), QUALQUER CAUSA E QUALQUER TEORIA DE RESPONSABILIDADE, SEJA POR CONTRATO, 
 * RESPONSABILIDADE ESTRITA OU DANO (INCLUINDO NEGLIGÊNCIA OU QUALQUER OUTRA) DECORRENTE DE QUALQUER FORMA FORA DO USO DESTE SOFTWARE, 
 * MESMO SE AVISADO DA POSSIBILIDADE DE TAIS DANOS.
 *
 */
package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class GeradorCredencial extends CriptografiaDeChaves {

    private File diretorio;
    private FileOutputStream bfwriter;
    private final Logger logger;

    public GeradorCredencial() {
        InputStream in = this.getClass().getResourceAsStream("/log4j/log4j.properties");
        PropertyConfigurator.configure(in);
        this.logger = Logger.getLogger(GeradorCredencial.class);
    }

    public void criarDiretorio() {

        diretorio = new File(System.getProperty("user.home") + "/SherlockTM/Config_Auth/");

        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }
    }

    public void gerarArquivoCrypto(String keys, String caminho) {
        try {
            bfwriter = new FileOutputStream(new File(caminho));
            bfwriter.write(encriptar(keys));
            bfwriter.flush();
            bfwriter.close();

        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}
