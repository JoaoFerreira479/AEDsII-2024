package praticafila;

import java.util.NoSuchElementException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

class Celula<T> {
	private final T item;
	private Celula<T> proximo;

	public Celula() {
		this.item = null;
		this.proximo = null;
	}

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

class Fila<E> {
	private Celula<E> frente;
	private Celula<E> tras;

	public Fila() {
		this.frente = this.tras = new Celula<>();
	}

	public boolean vazia() {
		return frente == tras;
	}

	public void enfileirar(E item) {
		tras.setProximo(new Celula<>(item));
		tras = tras.getProximo();
	}

	public E desenfileirar() {
		if (vazia())
			throw new NoSuchElementException("Fila vazia");
		Celula<E> primeiro = frente.getProximo();
		frente.setProximo(primeiro.getProximo());
		if (tras == primeiro)
			tras = frente;
		return primeiro.getItem();
	}

	public boolean verificarExistencia(E item) {
		Celula<E> atual = frente.getProximo();
		while (atual != null) {
			if (atual.getItem().equals(item))
				return true;
			atual = atual.getProximo();
		}
		return false;
	}

	public Fila<E> dividir() {
		Fila<E> novaFila = new Fila<>();
		Celula<E> atual = frente.getProximo();
		Fila<E> original = new Fila<>();
		boolean impar = true;
		while (atual != null) {
			if (impar)
				original.enfileirar(atual.getItem());
			else
				novaFila.enfileirar(atual.getItem());
			atual = atual.getProximo();
			impar = !impar;
		}
		this.frente = original.frente;
		this.tras = original.tras;
		return novaFila;
	}

	public void imprimir() {
		Celula<E> atual = frente.getProximo();
		while (atual != null) {
			System.out.println(atual.getItem());
			atual = atual.getProximo();
		}
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

	public String getNome() {
		return nome;
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

public class GerenciadorFila {
	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new FileReader("medallists.csv"));
			br.readLine();

			Fila<Medalhista> fila = new Fila<>();
			String linha;
			while ((linha = br.readLine()) != null) {
				String[] partes = linha.split(",");
				String nome = partes[0];
				String genero = partes[1];
				LocalDate nascimento = LocalDate.parse(partes[2], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				String pais = partes[3];
				fila.enfileirar(new Medalhista(nome, genero, nascimento, pais));
			}
			br.close();

			Scanner scanner = new Scanner(System.in);
			while (scanner.hasNextLine()) {
				String comando = scanner.nextLine();
				if (comando.equals("FIM"))
					break;

				String[] partes = comando.split(" ", 2);
				switch (partes[0]) {
				case "ENFILEIRAR":
					fila.enfileirar(new Medalhista(partes[1], "DESCONHECIDO", LocalDate.now(), "DESCONHECIDO"));
					break;
				case "DESENFILEIRAR":
					System.out.println("(DESENFILEIRADO) " + fila.desenfileirar().getNome());
					break;
				case "EXISTE":
					boolean existe = fila.verificarExistencia(
							new Medalhista(partes[1], "DESCONHECIDO", LocalDate.now(), "DESCONHECIDO"));
					System.out.println(partes[1] + " EXISTE NA FILA? " + (existe ? "SIM" : "NAO"));
					break;
				case "DIVIDIR":
					Fila<Medalhista> novaFila = fila.dividir();
					System.out.println("FILA ORIGINAL");
					fila.imprimir();
					System.out.println("FILA NOVA");
					novaFila.imprimir();
					break;
				}
			}
			scanner.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
