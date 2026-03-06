package arquivos;

import java.io.File;
import java.io.IOException;
import java.awt.Desktop;
import java.util.Scanner;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        File arquivo = new File("relatorio.html");

        System.out.print("Digite o nome do arquivo (ex: dados.json ou dados.txt): ");
        String nomeArquivo = scanner.nextLine();

       
        BuscaDados gerenciadorDados = new BuscaDados(nomeArquivo);
        gerenciadorDados.buscarDados();

        if (gerenciadorDados.getProdutos() == null || gerenciadorDados.getProdutos().isEmpty()) {
            System.out.println("Lista de produtos vazia ou erro ao ler o arquivo. Encerrando.");
            return;
        }

        boolean continuar = true;

        while (continuar) {

            if (arquivo.exists()) {
                System.out.println("Relatório já existe.");
                System.out.println("Última modificação: " + new Date(arquivo.lastModified()));
                System.out.print("Deseja gerar novamente? (s/n): ");

                String resp = scanner.nextLine();

                if (!resp.equalsIgnoreCase("s")) {
                    System.out.println("Abrindo relatório existente...");
                    Desktop.getDesktop().browse(arquivo.toURI());
                    break; 
                }
            }

           
            HtmlView.gerarHtml(gerenciadorDados.getProdutos());

            System.out.println("Relatório gerado com sucesso!");

            
            Desktop.getDesktop().browse(arquivo.toURI());

            System.out.print("Deseja gerar novamente? (s/n): ");
            String resp = scanner.nextLine();

            if (!resp.equalsIgnoreCase("s")) {
                continuar = false;
            }
        }

        scanner.close();
    }
}