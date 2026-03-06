package arquivos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Produto {

	@JsonProperty("product_title")
	private String nome;

	@JsonProperty("product_price")
	private double preco;
	
	@JsonProperty("product_brand")
	private String marca;

	public Produto() {
	}
	
	public Produto(String nome, double preco) {
		
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		if (nome.length() > 3)
			this.nome = nome;
	}

	public double getPreco() {
		return preco;
	}

	public void setPreco(double preco) {
		if (preco > 0)
			this.preco = preco;
	}
	
	public String getMarca() {
		return marca;
	}
	
	public void setMarca(String marca) {
		if(marca.length() > 1)
			this.marca = marca;
	}
	
	@Override
    public String toString() {
        return "Produto: " + nome + " | Preço: R$ " + preco +" | Marca: " + marca;
    }

}
