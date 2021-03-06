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
package graficos;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

/**
 *
 * @author root
 */
public class GraficoBarras extends Grafico {

    private DefaultCategoryDataset dataset;

    public GraficoBarras() {
        this.dataset = new DefaultCategoryDataset();
    }

    @Override
    public void saveDataset(String columnkey) {
        for (Map.Entry<String, Object> map : values.entrySet()) {
            dataset.setValue((double) map.getValue(), map.getKey(), columnkey);
        }
    }

    @Override
    public JPanel criarGrafico() {
        JFreeChart chart = ChartFactory.createBarChart3D("Quantitativo", "Legenda", "Quantidade", dataset, PlotOrientation.VERTICAL, true, true, true);
        
        CategoryPlot plot = chart.getCategoryPlot();
        CategoryItemRenderer renderer = plot.getRenderer();
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);
        
        ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12,
                TextAnchor.TOP_CENTER);
        renderer.setBasePositiveItemLabelPosition(position);

        return new ChartPanel(chart);
    }

}
