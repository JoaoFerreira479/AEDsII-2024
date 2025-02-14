package praticaabb2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

class No<T extends Comparable<T>> {
	private T item;
	private No<T> direita;
	private No<T> esquerda;

	public No(T item) {
		this.item = item;
		this.direita = null;
		this.esquerda = null;
	}

	public T getItem() {
		return item;
	}

	public void setItem(T item) {
		this.item = item;
	}

	public No<T> getDireita() {
		return direita;
	}

	public No<T> getEsquerda() {
		return esquerda;
	}

	public void setDireita(No<T> direita) {
		this.direita = direita;
	}

	public void setEsquerda(No<T> esquerda) {
		this.esquerda = esquerda;
	}
}

class ABB<E extends Comparable<E>> {
	protected No<E> raiz;

	public ABB() {
		this.raiz = null;
	}

	public boolean vazia() {
		return this.raiz == null;
	}

	public void adicionar(E item) {
		this.raiz = adicionar(this.raiz, item);
	}

	private No<E> adicionar(No<E> raizArvore, E item) {
		if (raizArvore == null)
			return new No<>(item);

		int comparacao = item.compareTo(raizArvore.getItem());
		if (comparacao < 0)
			raizArvore.setEsquerda(adicionar(raizArvore.getEsquerda(), item));
		else if (comparacao > 0)
			raizArvore.setDireita(adicionar(raizArvore.getDireita(), item));

		return raizArvore;
	}

	public void recortar(E item) {
		this.raiz = remover(this.raiz, item);
	}

	private No<E> remover(No<E> raizArvore, E itemRemover) {
		if (raizArvore == null)
			throw new NoSuchElementException("Item nao encontrado na arvore!");

		int comparacao = itemRemover.compareTo(raizArvore.getItem());

		if (comparacao < 0)
			raizArvore.setEsquerda(remover(raizArvore.getEsquerda(), itemRemover));
		else if (comparacao > 0)
			raizArvore.setDireita(remover(raizArvore.getDireita(), itemRemover));
		else {
			if (raizArvore.getEsquerda() == null)
				return raizArvore.getDireita();
			if (raizArvore.getDireita() == null)
				return raizArvore.getEsquerda();

			No<E> sucessor = encontrarMenor(raizArvore.getDireita());
			raizArvore.setItem(sucessor.getItem());
			raizArvore.setDireita(remover(raizArvore.getDireita(), sucessor.getItem()));
		}
		return raizArvore;
	}

	private No<E> encontrarMenor(No<E> raiz) {
		while (raiz.getEsquerda() != null)
			raiz = raiz.getEsquerda();
		return raiz;
	}
}

class Medalhista implements Comparable<Medalhista> {
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
	public int compareTo(Medalhista outro) {
		return this.nome.compareToIgnoreCase(outro.nome);
	}

	@Override
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return nome + ", " + genero + ". Nascimento: " + nascimento.format(formatter) + ". Pais: " + pais;
	}
}

public class ABBRecortar<E extends Comparable<E>> extends ABB<E> {
	public static void main(String[] args) {
		try {
			ABBRecortar<Medalhista> abb = new ABBRecortar<>();
			BufferedReader br = new BufferedReader(new FileReader("medallists.csv"));
			String linha;
			br.readLine();

			while ((linha = br.readLine()) != null) {
				String[] partes = linha.split(",");
				String nome = partes[0];
				String genero = partes[1];
				LocalDate nascimento = LocalDate.parse(partes[2], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				String pais = partes[3];
				abb.adicionar(new Medalhista(nome, genero, nascimento, pais));
			}
			br.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String atleta;
			while (!(atleta = in.readLine()).equals("FIM")) {
				try {
					abb.recortar(new Medalhista(atleta, "", LocalDate.now(), ""));
					System.out.println("Medalhista removido: " + atleta);
				} catch (Exception e) {
					System.out.println("Medalhista nao encontrado");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
