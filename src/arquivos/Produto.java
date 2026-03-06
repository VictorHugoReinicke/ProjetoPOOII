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

	@JsonProperty("categoria")
	private String categoria;

	@JsonProperty("tipoDDR")
	private String tipoDDR;

	public Produto() {
	}

	public Produto(String nome, double preco, String marca, String tipoDDR, String categoria) {
		this.setNome(nome);
	    this.setPreco(preco);
	    this.setMarca(marca);
	    this.setCategoria(categoria);
	    this.setTipoDDR(tipoDDR);
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
		if (marca.length() > 1)
			this.marca = marca;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		if(categoria.length() > 1)
		this.categoria = categoria;
	}

	public String getTipoDDR() {
		return tipoDDR;
	}

	public void setTipoDDR(String tipoDDR) {
		if(tipoDDR.length() > 1)
		this.tipoDDR = tipoDDR;
	}

	@Override
	public String toString() {
		return "Produto: " + nome + " | Preço: R$ " + preco + " | Marca: " + marca;
	}

}
