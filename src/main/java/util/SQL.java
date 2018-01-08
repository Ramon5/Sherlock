/*
 * Copyright 2017 root.
 * Todos os direitos reservados.
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

/**
 *
 * @author root
 */
public class SQL {
    
    public static final String COUNT_ORIG = "select count(t.idtweet) as quantidade from TWEET as t inner join COLETA as c on (t.coleta_idcoleta = c.idcoleta) where c.idcoleta = ? and t.retweet = 0;";
    public static final String COUNT_RET = "select count(t.idtweet) as quantidade from TWEET as t inner join COLETA as c on (t.coleta_idcoleta = c.idcoleta) where c.idcoleta = ? and t.retweet = 1;";
    public static final String REPLY = "select count(t.idtweet) as quantidade from TWEET as t inner join COLETA as c on (t.coleta_idcoleta = c.idcoleta) where c.idcoleta = ? and t.to_user_id <> -1;";
    public static final String GEO = "select count(t.idtweet) as quantidade from TWEET as t inner join COLETA as c on (t.coleta_idcoleta = c.idcoleta) where c.idcoleta = ? and t.latitude <> 0 and t.longitude <> 0;";
    public static final String TWEETS = "select * from tweet as t inner join coleta as c on (t.coleta_idcoleta = c.idcoleta) where c.idcoleta = ?";    
    public static final String ORIGINAIS = "select * from tweet as t inner join coleta as c on (t.coleta_idcoleta = c.idcoleta) where c.idcoleta = ? and t.retweet = 0";
    public static final String RETWEET = "select * from tweet as t inner join coleta as c on (t.coleta_idcoleta = c.idcoleta) where c.idcoleta = ? and t.retweet = 1";
    
    //extrair mês
    //select extract(month from t.created_at) as mes from TWEET as t inner join COLETA as c on (t.coleta_idcoleta = c.idcoleta) where c.idcoleta = 1;
    //select extract(month from t.created_at) as mes, t.tweet, t.created_at from TWEET as t inner join COLETA as c on (t.coleta_idcoleta = c.idcoleta);
}
