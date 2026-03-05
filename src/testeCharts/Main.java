package testeCharts;

import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {

        List<Produto> produtos = new ArrayList<>();
        Random random = new Random();

        String[] marcas = {"Kingston","Corsair","Crucial","HyperX","Samsung","ADATA"};
        String[] ddrs = {"DDR3","DDR4","DDR5"};
        String[] categorias = {"PC","Notebook"};

        for(int i = 1; i <= 100; i++){

            String marca = marcas[random.nextInt(marcas.length)];
            String ddr = ddrs[random.nextInt(ddrs.length)];
            String categoria = categorias[random.nextInt(categorias.length)];

            int frequencia = switch (ddr) {
                case "DDR3" -> 1333 + random.nextInt(400);
                case "DDR4" -> 2133 + random.nextInt(1000);
                case "DDR5" -> 4800 + random.nextInt(2000);
                default -> 0;
            };

            double preco = 100 + random.nextDouble() * 900;

            produtos.add(new Produto(
                    i,
                    "Memoria " + ddr + " " + frequencia + "MHz",
                    preco,
                    marca,
                    frequencia,
                    categoria,
                    ddr
            ));
        }

        HtmlView.gerarHtml(produtos);

        System.out.println("Relatório gerado com sucesso!");
    }
}