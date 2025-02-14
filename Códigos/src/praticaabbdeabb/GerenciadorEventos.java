package praticaabbdeabb;

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

	public E pesquisar(E procurado) {
		return pesquisar(this.raiz, procurado);
	}

	private E pesquisar(No<E> raizArvore, E procurado) {
		if (raizArvore == null)
			throw new NoSuchElementException("Evento nao encontrado");

		int comparacao = procurado.compareTo(raizArvore.getItem());
		if (comparacao == 0)
			return raizArvore.getItem();
		else if (comparacao < 0)
			return pesquisar(raizArvore.getEsquerda(), procurado);
		else
			return pesquisar(raizArvore.getDireita(), procurado);
	}
}

class Evento implements Comparable<Evento> {
	private String nome;
	private LocalDate data;

	public Evento(String nome, LocalDate data) {
		this.nome = nome;
		this.data = data;
	}

	public String getNome() {
		return nome;
	}

	public LocalDate getData() {
		return data;
	}

	@Override
	public int compareTo(Evento outro) {
		return this.data.compareTo(outro.data);
	}

	@Override
	public String toString() {
		return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " - " + nome;
	}
}

class ABBEventos extends ABB<Evento> {
	public void imprimirEventosPorData(LocalDate data) {
		System.out.println("Eventos do dia " + data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		imprimirEventosPorData(this.raiz, data);
	}

	private void imprimirEventosPorData(No<Evento> no, LocalDate data) {
		if (no != null) {
			imprimirEventosPorData(no.getEsquerda(), data);
			if (no.getItem().getData().equals(data)) {
				System.out.println(no.getItem().getNome());
			}
			imprimirEventosPorData(no.getDireita(), data);
		}
	}
}

public class GerenciadorEventos {
	public static void main(String[] args) {
		try {
			ABBEventos abb = new ABBEventos();
			BufferedReader br = new BufferedReader(new FileReader("medallists.csv"));
			String linha;
			br.readLine();

			while ((linha = br.readLine()) != null) {
				String[] partes = linha.split(",");
				String nome = partes[0];
				LocalDate data = LocalDate.parse(partes[1], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				abb.adicionar(new Evento(nome, data));
			}
			br.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String comando;
			while (!(comando = in.readLine()).equals("FIM")) {
				if (comando.startsWith("PESQUISAR")) {
					String evento = comando.substring(10);
					try {
						Evento encontrado = abb.pesquisar(new Evento(evento, LocalDate.now()));
						System.out.println("Evento encontrado: " + encontrado);
					} catch (Exception e) {
						System.out.println("Evento nao encontrado");
					}
				} else if (comando.startsWith("IMPRIMIR")) {
					LocalDate data = LocalDate.parse(comando.substring(9), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
					abb.imprimirEventosPorData(data);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
