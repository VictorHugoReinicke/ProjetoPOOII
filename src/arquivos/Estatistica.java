package arquivos;

import java.util.*;
import java.util.stream.Collectors;

public class Estatistica {

    /**
     * Calcula a média aritmética de uma lista de valores
     */
    public static double media(List<Double> valores) {
        if (valores == null || valores.isEmpty()) return 0.0;
        
        return valores.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }

    /**
     * Calcula a mediana de uma lista de valores
     */
    public static double mediana(List<Double> valores) {
        if (valores == null || valores.isEmpty()) return 0.0;
        
        List<Double> sorted = new ArrayList<>(valores);
        Collections.sort(sorted);
        
        int tamanho = sorted.size();
        int meio = tamanho / 2;
        
        if (tamanho % 2 == 0) {
            // Para número par de elementos, média dos dois elementos centrais
            return (sorted.get(meio - 1) + sorted.get(meio)) / 2.0;
        } else {
            // Para número ímpar de elementos, elemento central
            return sorted.get(meio);
        }
    }

    /**
     * Calcula a moda de uma lista de valores
     * Retorna o valor que mais se repete
     */
    public static double moda(List<Double> valores) {
        if (valores == null || valores.isEmpty()) return 0.0;
        
        Map<Double, Long> frequencia = valores.stream()
                .collect(Collectors.groupingBy(v -> v, Collectors.counting()));
        
        return frequencia.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(0.0);
    }

    /**
     * Calcula a variância amostral de uma lista de valores
     * Usa n-1 no denominador (variância amostral, mais adequada para amostras)
     */
    public static double variancia(List<Double> valores) {
        if (valores == null || valores.isEmpty()) return 0.0;
        if (valores.size() == 1) return 0.0; // Variância de uma amostra com 1 elemento é 0
        
        double media = media(valores);
        int n = valores.size();
        
        // Soma dos quadrados das diferenças
        double somaQuadrados = valores.stream()
                .mapToDouble(v -> Math.pow(v - media, 2))
                .sum();
        
        // Variância amostral: soma dos quadrados / (n-1)
        return somaQuadrados / (n - 1);
    }

    /**
     * Calcula o desvio padrão amostral (raiz quadrada da variância amostral)
     */
    public static double desvioPadrao(List<Double> valores) {
        if (valores == null || valores.isEmpty()) return 0.0;
        
        return Math.sqrt(variancia(valores));
    }
}