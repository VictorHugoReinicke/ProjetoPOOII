package testeCharts;

public class Produto {

    private int id;
    private String titulo;
    private double preco;
    private String marca;
    private Integer frequencia; // MHz - nullable
    private String categoria;   // "PC" ou "Notebook" - nullable
    private String tipoDDR;     // DDR3, DDR4, DDR5 - nullable

    public Produto(int id, String titulo, double preco, String marca,
                   Integer frequencia, String categoria, String tipoDDR) {
        this.id = id;
        this.titulo = titulo;
        this.preco = preco;
        this.marca = marca;
        this.frequencia = frequencia;
        this.categoria = categoria;
        this.tipoDDR = tipoDDR;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public double getPreco() { return preco; }
    public String getMarca() { return marca; }
    public Integer getFrequencia() { return frequencia; }
    public String getCategoria() { return categoria; }
    public String getTipoDDR() { return tipoDDR; }
}