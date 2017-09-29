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
package sistema;

import com.jtattoo.plaf.mcwin.McWinLookAndFeel;
import view.SherlockGUI;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Controlador {

    public static void main(String[] args) {

        try {
            Properties props = new Properties();
            props.put("logoString", "SherlockTM");
            McWinLookAndFeel.setCurrentTheme(props);
            UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
            SherlockGUI gui = new SherlockGUI();
            gui.setVisible(true);
            gui.setLocationRelativeTo(null);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Controlador.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
