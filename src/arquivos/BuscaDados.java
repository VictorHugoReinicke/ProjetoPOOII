package arquivos;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BuscaDados {

	private String caminhoArquivo;
	private List<Produto> produtos;
	private final ObjectMapper mapper = new ObjectMapper();

	public BuscaDados() {
	}

	public BuscaDados(String caminhoArquivo) {
		setCaminhoArquivo(caminhoArquivo);
		this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public String getCaminhoArquivo() {
		return this.caminhoArquivo;
	}

	public void setCaminhoArquivo(String caminhoArquivo) {
		if (caminhoArquivo.length() > 3) {
			this.caminhoArquivo = caminhoArquivo;
		}
	}

	public List<Produto> getProdutos() {
		return this.produtos;
	}

	private void parseJson(File arquivo) throws IOException {
		this.produtos = mapper.readValue(arquivo, new TypeReference<List<Produto>>() {
		});
	}

	public void carregarDados() throws IOException {
		File arquivo = new File(getCaminhoArquivo());
		if (!arquivo.exists()) {
			throw new IOException("O arquivo não foi achado no" + caminhoArquivo);
		}

		if (caminhoArquivo.toLowerCase().endsWith(".json")) {
			parseJson(arquivo);
		} else if (caminhoArquivo.toLowerCase().endsWith(".txt")) {
			parseTxt(arquivo);
		} else {
			throw new IOException("Arquivo não é JSON e nem TXT");
		}

	}

	private void parseTxt(File arquivo) throws IOException {
		if (this.produtos == null) {
			this.produtos = new ArrayList<>();
		}
		this.produtos.clear();
		try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
			String linha;
			while ((linha = br.readLine()) != null) {
				if (linha.trim().isEmpty())
					continue;

				String[] dados = linha.split(";");

				Produto p = new Produto();
				p.setNome(dados[0]);
				p.setPreco(Double.parseDouble(dados[1]));
				p.setMarca(dados[2]);

				this.produtos.add(p);
			}
		}
	}

	public void buscarDados() {
		try {
			carregarDados();
			System.out.println("Dados carregados");
		} catch (IOException e) {
			System.err.println("Erro ao buscar dados: " + e.getMessage());
		}
	}

}
