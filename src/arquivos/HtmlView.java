package arquivos;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class HtmlView {

    public static void gerarHtml(List<Produto> produtos) throws IOException {

        StringBuilder html = new StringBuilder();

        html.append("""
        <html>
        <head>
            <meta charset="UTF-8">
            <style>
                body {
                    font-family: Arial, sans-serif;
                    margin: 20px;
                    background-color: #f5f5f5;
                }
                .container {
                    max-width: 1500px;
                    margin: 0 auto;
                    background-color: white;
                    padding: 20px;
                    border-radius: 10px;
                    box-shadow: 0 0 10px rgba(0,0,0,0.1);
                }
                h1 {
                    color: #333;
                    text-align: center;
                    margin-bottom: 30px;
                }
                .chart-container {
                    margin-bottom: 40px;
                    padding: 20px;
                    background-color: white;
                    border-radius: 8px;
                    box-shadow: 0 0 10px rgba(0,0,0,0.05);
                }
                .chart-title {
                    color: #666;
                    margin-bottom: 15px;
                    font-size: 1.2em;
                    border-bottom: 2px solid #eee;
                    padding-bottom: 10px;
                }
                .charts-grid {
                    display: grid;
                    grid-template-columns: 1fr 1fr;
                    gap: 20px;
                }
                .full-width {
                    grid-column: span 2;
                }
            </style>
            <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
            <script type="text/javascript">
              google.charts.load('current', {'packages':['corechart', 'table']});
              google.charts.setOnLoadCallback(drawCharts);

              function formatMoney(value) {
                  if (value === null || value === undefined) return 'R$ 0,00';
                  return 'R$ ' + value.toFixed(2).replace('.', ',');
              }

              function drawCharts() {
        """);

        List<String> ordemDDR = List.of("DDR3", "DDR4", "DDR5");

        // TABELA DE ESTATÍSTICAS POR TIPO DDR
        
        html.append("""
            var dataTable = new google.visualization.DataTable();
            dataTable.addColumn('string', 'Tipo DDR');
            dataTable.addColumn('number', 'Média (R$)');
            dataTable.addColumn('number', 'Mediana (R$)');
            dataTable.addColumn('number', 'Moda (R$)');
            dataTable.addColumn('number', 'Desvio Padrão (R$)');
            dataTable.addColumn('number', 'Variância (R$)');
            dataTable.addColumn('number', 'Quantidade');
        """);

        for (String ddr : ordemDDR) {
            List<Double> precosDDR = produtos.stream()
                    .filter(p -> ddr.equals(p.getTipoDDR()) && p.getPreco() > 0)
                    .map(Produto::getPreco)
                    .collect(Collectors.toList());

            if (!precosDDR.isEmpty()) {
                double media = Estatistica.media(precosDDR);
                double mediana = Estatistica.mediana(new ArrayList<>(precosDDR));
                double moda = Estatistica.moda(precosDDR);
                double desvio = Estatistica.desvioPadrao(precosDDR);
                double variancia = Estatistica.variancia(precosDDR);
                int quantidade = precosDDR.size();

                html.append(String.format(Locale.US,
                    "dataTable.addRow(['%s', %.2f, %.2f, %.2f, %.2f, %.2f, %d]);",
                    ddr, media, mediana, moda, desvio, variancia, quantidade
                ));
            } else {
                html.append(String.format("dataTable.addRow(['%s', 0, 0, 0, 0, 0, 0]);", ddr));
            }
        }

        html.append("""
            var table = new google.visualization.Table(document.getElementById('table_stats'));
            
            var formatter = new google.visualization.NumberFormat({
                pattern: 'R$ ###,##0.00'
            });
            formatter.format(dataTable, 1); // Formatar coluna Média
            formatter.format(dataTable, 2); // Formatar coluna Mediana
            formatter.format(dataTable, 3); // Formatar coluna Moda
            formatter.format(dataTable, 4); // Formatar coluna Desvio Padrão
            formatter.format(dataTable, 5); // Formatar coluna Variância
            
            table.draw(dataTable, {
                showRowNumber: true,
                width: '100%',
                height: '100%',
                alternatingRowStyle: true,
                page: 'enable',
                pageSize: 10
            });
        """);

        // GRÁFICO DE COLUNAS COM MÉDIA POR MARCA (COM TOOLTIP)

        Map<String, List<Double>> precosPorMarca = produtos.stream()
                .collect(Collectors.groupingBy(
                        Produto::getMarca,
                        Collectors.mapping(Produto::getPreco, Collectors.toList())
                ));

        html.append("var dataMarca = google.visualization.arrayToDataTable([");
        html.append("['Marca', 'Média (R$)', {type: 'string', role: 'tooltip', p: {html: true}}, { role: 'annotation' } ]");

        for (Map.Entry<String, List<Double>> entry : precosPorMarca.entrySet()) {
            String marca = entry.getKey();
            List<Double> lista = entry.getValue();

            double media = Estatistica.media(lista);
            double mediana = Estatistica.mediana(new ArrayList<>(lista));
            double moda = Estatistica.moda(lista);
            double variancia = Estatistica.variancia(lista);
            double desvio = Estatistica.desvioPadrao(lista);
            int quantidade = lista.size();

            String tooltip = String.format(Locale.US,
                "<div style=\\\"padding:10px; font-size:12px;\\\">" +
                "<b>%s</b><br/>" +
                "<b>Média: R$ %.2f</b><br/>" +
                "Mediana: R$ %.2f<br/>" +
                "Moda: R$ %.2f<br/>" +
                "Desvio Padrão: R$ %.2f<br/>" +
                "Variância: R$ %.2f<br/>" +
                "Quantidade: %d produtos" +
                "</div>",
                marca, media, mediana, moda, desvio, variancia, quantidade
            );

            html.append(String.format(Locale.US,
                ",['%s', %.2f, '%s', %.2f]",
                marca, media, tooltip, media
            ));
        }

        html.append("]);");

        html.append("""
            var chartMarca = new google.visualization.ColumnChart(document.getElementById('graficoMarca'));
            chartMarca.draw(dataMarca, {
                title: 'Média de Preço por Marca',
                legend: { position: 'none' },
                vAxis: {
                    format: 'R$ ###,##0.00',
                    title: 'Preço (R$)'
                },
                tooltip: { isHtml: true }
            });
        """);

        // GRÁFICO DE PIZZA - QUANTIDADE POR MARCA

        Map<String, Long> quantidadePorMarca = produtos.stream()
                .collect(Collectors.groupingBy(
                        Produto::getMarca,
                        Collectors.counting()
                ));

        html.append("var dataPizza = google.visualization.arrayToDataTable([");
        html.append("['Marca', 'Quantidade']");

        for (Map.Entry<String, Long> entry : quantidadePorMarca.entrySet()) {
            html.append(",['" + entry.getKey() + "', " + entry.getValue() + "]");
        }

        html.append("]);");

        html.append("""
            var chartPizza = new google.visualization.PieChart(document.getElementById('graficoPizza'));
            chartPizza.draw(dataPizza, {
                title: 'Distribuição de Produtos por Marca',
                legend: { position: 'labeled' },
                sliceVisibilityThreshold: 0.05,
                pieSliceText: 'value',
                pieSliceTextStyle: { fontSize: 12 }
            });
        """);

        // PREÇO MÁXIMO

        gerarGraficoExtremos(html, produtos, true,
                "graficoMax", "Preço Máximo por Tipo DDR e Categoria", ordemDDR);

        // PREÇO MÍNIMO

        gerarGraficoExtremos(html, produtos, false,
                "graficoMin", "Preço Mínimo por Tipo DDR e Categoria", ordemDDR);

        html.append("""
              }
            </script>
        </head>
        <body>
            <div class="container">
                <h1>Relatório de Análise de Memórias RAM</h1>
                
                <div class="chart-container">
                    <div class="chart-title">Estatísticas por Tipo de DDR</div>
                    <div id="table_stats" style="width:100%; height:300px;"></div>
                </div>
                
                <div class="charts-grid">
                    <div class="chart-container">
                        <div class="chart-title">Média de Preço por Marca</div>
                        <div id="graficoMarca" style="width:100%; height:400px;"></div>
                    </div>
                    
                    <div class="chart-container">
                        <div class="chart-title">Distribuição por Marca</div>
                        <div id="graficoPizza" style="width:100%; height:400px;"></div>
                    </div>
                </div>
                
                <div class="charts-grid">
                    <div class="chart-container">
                        <div class="chart-title">Preço Máximo por DDR e Categoria</div>
                        <div id="graficoMax" style="width:100%; height:400px;"></div>
                    </div>
                    
                    <div class="chart-container">
                        <div class="chart-title">Preço Mínimo por DDR e Categoria</div>
                        <div id="graficoMin" style="width:100%; height:400px;"></div>
                    </div>
                </div>
            </div>
        </body>
        </html>
        """);

        FileWriter writer = new FileWriter("relatorio.html");
        writer.write(html.toString());
        writer.close();
    }

    // MÉTODO AUXILIAR

    private static void gerarGraficoExtremos(StringBuilder html,
                                             List<Produto> produtos,
                                             boolean max,
                                             String divId,
                                             String titulo,
                                             List<String> ordemDDR) {

        Map<String, Map<String, Double>> mapa = produtos.stream()
                .filter(p -> p.getTipoDDR() != null && p.getCategoria() != null)
                .collect(Collectors.groupingBy(
                        Produto::getTipoDDR,
                        Collectors.groupingBy(
                                Produto::getCategoria,
                                Collectors.collectingAndThen(
                                        Collectors.mapping(Produto::getPreco, Collectors.toList()),
                                        lista -> {
                                            if (lista.isEmpty()) return 0.0;
                                            return max
                                                    ? lista.stream().max(Double::compare).orElse(0.0)
                                                    : lista.stream().min(Double::compare).orElse(0.0);
                                        }
                                )
                        )
                ));

        html.append("var data_" + divId + " = google.visualization.arrayToDataTable([");
        html.append("['Tipo DDR', 'PC', 'Notebook']");

        for (String ddr : ordemDDR) {
            double pc = mapa.getOrDefault(ddr, new HashMap<>())
                            .getOrDefault("PC", 0.0);
            double note = mapa.getOrDefault(ddr, new HashMap<>())
                              .getOrDefault("Notebook", 0.0);

            html.append(String.format(Locale.US,
                ",['%s', %.2f, %.2f]",
                ddr, pc, note
            ));
        }

        html.append("]);");

        html.append(
            "var chart_" + divId + " = new google.visualization.ColumnChart(document.getElementById('" + divId + "'));" +
            "chart_" + divId + ".draw(data_" + divId + ", {" +
            "title: '" + titulo + "'," +
            "legend: { position: 'top' }," +
            "vAxis: { " +
            "   format: 'R$ ###,##0.00'," +
            "   title: 'Preço (R$)'" +
            "}," +
            "hAxis: { title: 'Tipo DDR' }" +
            "});"
        );
    }
}