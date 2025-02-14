package praticalistaencadeada;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;

class Celula<T> {
	private final T item;
	private Celula<T> proximo;

	public Celula(T item) {
		this.item = item;
		this.proximo = null;
	}

	public T getItem() {
		return item;
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
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return nome + ", " + genero + ". Nascimento: " + nascimento.format(formatter) + ". Pais: " + pais;
	}
}

class ListaEncadeada<E> {
	private Celula<E> primeiro;
	private Celula<E> ultimo;

	public ListaEncadeada() {
		this.primeiro = this.ultimo = null;
	}

	public boolean vazia() {
		return primeiro == null;
	}

	public void inserirInicio(E item) {
		Celula<E> nova = new Celula<>(item);
		if (vazia()) {
			primeiro = ultimo = nova;
		} else {
			nova.setProximo(primeiro);
			primeiro = nova;
		}
	}

	public void inserirFinal(E item) {
		Celula<E> nova = new Celula<>(item);
		if (vazia()) {
			primeiro = ultimo = nova;
		} else {
			ultimo.setProximo(nova);
			ultimo = nova;
		}
	}

	public E removerInicio() {
		if (vazia())
			throw new IllegalStateException("Lista vazia");
		E removido = primeiro.getItem();
		primeiro = primeiro.getProximo();
		if (primeiro == null)
			ultimo = null;
		return removido;
	}

	public void imprimirSemRepeticao() {
		HashSet<E> set = new HashSet<>();
		Celula<E> atual = primeiro;
		System.out.println("LISTA DE MEDALHISTAS SEM REPETICAO");
		while (atual != null) {
			if (set.add(atual.getItem())) {
				System.out.println(atual.getItem());
			}
			atual = atual.getProximo();
		}
	}

	public void imprimirInvertido() {
		System.out.println("LISTA INVERTIDA DE MEDALHISTAS");
		imprimirInvertidoRecursivo(primeiro);
	}

	private void imprimirInvertidoRecursivo(Celula<E> celula) {
		if (celula == null)
			return;
		imprimirInvertidoRecursivo(celula.getProximo());
		System.out.println(celula.getItem());
	}
}

public class GerenciadorListaEncadeada {
	public static void main(String[] args) {
		try {
			ListaEncadeada<Medalhista> lista = new ListaEncadeada<>();
			BufferedReader br = new BufferedReader(new FileReader("medallists.csv"));
			String linha;
			br.readLine();

			while ((linha = br.readLine()) != null) {
				String[] partes = linha.split(",");
				String nome = partes[0];
				String genero = partes[1];
				LocalDate nascimento = LocalDate.parse(partes[2], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				String pais = partes[3];
				Medalhista medalhista = new Medalhista(nome, genero, nascimento, pais);
				lista.inserirFinal(medalhista);
			}
			br.close();

			lista.imprimirSemRepeticao();
			lista.imprimirInvertido();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
