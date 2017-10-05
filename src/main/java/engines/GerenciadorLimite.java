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
package engines;

import entidade.Chave;
import java.util.List;
import javax.swing.JLabel;
import org.apache.log4j.Logger;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatusEvent;
import util.AutenticacaoAPI;
import view.SherlockGUI;

/**
 *
 * @author root
 */
public class GerenciadorLimite {

    private final JLabel status;
    private final Logger logger;

    public GerenciadorLimite(JLabel status) {
        this.status = status;
        logger = Logger.getLogger(GerenciadorLimite.class);
    }

    public void checarLimite(RateLimitStatusEvent limit) {
        List<Chave> chaves = SherlockGUI.keys;

        if (chaves.size() > 1) {
            for (int i = 0; i < chaves.size(); i++) {
                if (i < chaves.size() - 1) {
                    AutenticacaoAPI.indiceChave = i+1;
                    AutenticacaoAPI.appAutentication(chaves.get(i + 1));
                    break;
                } else {
                    AutenticacaoAPI.indiceChave = 0;
                    AutenticacaoAPI.appAutentication(chaves.get(0));
                    break;
                }

            }

        } else {
            if (limit.isAccountRateLimitStatus() || limit.isIPRateLimitStatus()) {
                if (limit.getRateLimitStatus().getRemaining() <= 0) {
                    int tempoEspera = limit.getRateLimitStatus().getSecondsUntilReset() + 200;

                    for (int i = tempoEspera; i > 0; i--) {
                        try {
                            status.setText("\nLimite atingido!! Esperando: " + i + " segundos");
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                            logger.error(ex);
                        }
                    }
                }
            }
        }
    }

    public void checarLimiteBusca(QueryResult limit) {
        if (limit != null) {
            if (limit.getRateLimitStatus().getRemaining() <= 0) {
                int tempoEspera = limit.getRateLimitStatus().getSecondsUntilReset() + 200;
                for (int i = tempoEspera; i > 0; i--) {
                    try {
                        status.setText("\nLimite atingido!! Esperando: " + i + " segundos");
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                        logger.error(ex);
                    }
                }
            }
        }
    }
}
