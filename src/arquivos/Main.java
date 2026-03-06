package arquivos;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        
        
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o nome do arquivo (ex: dados.json ou dados.txt): ");
        String nomeArquivo = scanner.nextLine();

        
        BuscaDados gerenciadorDados = new BuscaDados(nomeArquivo);
        
        gerenciadorDados.buscarDados();
        
        if (gerenciadorDados.getProdutos() != null && !gerenciadorDados.getProdutos().isEmpty()) {
            for (Produto p : gerenciadorDados.getProdutos()) {
                System.out.println("Encontrado: " + p);
            }
        } else {
            System.out.println("Lista de produtos vazia ou houve erro ao ler o arquivo.");
        }
        
      
        scanner.close();
    }
}