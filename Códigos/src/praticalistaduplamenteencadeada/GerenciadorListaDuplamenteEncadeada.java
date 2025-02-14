package praticalistaduplamenteencadeada;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

class Celula<T> {
	private final T item;
	private Celula<T> anterior;
	private Celula<T> proximo;

	public Celula(T item) {
		this.item = item;
		this.anterior = null;
		this.proximo = null;
	}

	public T getItem() {
		return item;
	}

	public Celula<T> getAnterior() {
		return anterior;
	}

	public void setAnterior(Celula<T> anterior) {
		this.anterior = anterior;
	}

	public Celula<T> getProximo() {
		return proximo;
	}

	public void setProximo(Celula<T> proximo) {
		this.proximo = proximo;
	}
}

class Medalhista {
	private String nome;
	private String genero;
	private LocalDate nascimento;
	private String pais;

	public Medalhista(String nome, String genero, LocalDate nascimento, String pais) {
		this.nome = nome;
		this.genero = genero;
		this.nascimento = nascimento;
		this.pais = pais;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Medalhista that = (Medalhista) obj;
		return nome.equals(that.nome);
	}

	@Override
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return nome + ", " + genero + ". Nascimento: " + nascimento.format(formatter) + ". Pais: " + pais;
	}
}

class ListaDuplamenteEncadeada<E> {
	private Celula<E> primeiro;
	private Celula<E> ultimo;
	private int tamanho;

	public ListaDuplamenteEncadeada() {
		this.primeiro = this.ultimo = new Celula<>(null);
		this.tamanho = 0;
	}

	public boolean vazia() {
		return (this.primeiro == this.ultimo);
	}

	public void inserirFinal(E item) {
		Celula<E> novaCelula = new Celula<>(item);
		if (vazia()) {
			primeiro = ultimo = novaCelula;
		} else {
			ultimo.setProximo(novaCelula);
			novaCelula.setAnterior(ultimo);
			ultimo = novaCelula;
		}
		tamanho++;
	}

	public boolean contemSequencia(ArrayList<E> sequencia) {
		Celula<E> atual = primeiro;
		while (atual != null) {
			Celula<E> temp = atual;
			boolean encontrado = true;
			for (E item : sequencia) {
				if (temp == null || !temp.getItem().equals(item)) {
					encontrado = false;
					break;
				}
				temp = temp.getProximo();
			}
			if (encontrado)
				return true;
			atual = atual.getProximo();
		}
		return false;
	}

	public void mesclar(ArrayList<E> novosItens) {
		for (E item : novosItens) {
			inserirFinal(item);
		}
	}

	public void imprimir() {
		Celula<E> atual = primeiro;
		while (atual != null) {
			System.out.println(atual.getItem());
			atual = atual.getProximo();
		}
	}
}

public class GerenciadorListaDuplamenteEncadeada {
	public static void main(String[] args) {
		try {
			ListaDuplamenteEncadeada<Medalhista> lista = new ListaDuplamenteEncadeada<>();
			BufferedReader br = new BufferedReader(new FileReader("medallists.csv"));
			String linha;
			br.readLine();

			while ((linha = br.readLine()) != null) {
				String[] partes = linha.split(",");
				String nome = partes[0];
				String genero = partes[1];
				LocalDate nascimento = LocalDate.parse(partes[2], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				String pais = partes[3];
				lista.inserirFinal(new Medalhista(nome, genero, nascimento, pais));
			}
			br.close();

			lista.imprimir();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
