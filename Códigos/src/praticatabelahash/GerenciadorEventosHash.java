package praticatabelahash;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

	public void caminhamentoEmOrdem() {
		caminhamentoEmOrdem(this.raiz);
	}

	private void caminhamentoEmOrdem(No<E> raizArvore) {
		if (raizArvore != null) {
			caminhamentoEmOrdem(raizArvore.getEsquerda());
			System.out.println(raizArvore.getItem());
			caminhamentoEmOrdem(raizArvore.getDireita());
		}
	}
}

class Entrada<K extends Comparable<? super K>, V> implements Comparable<Entrada<K, V>> {
	private K chave;
	private V valor;

	public Entrada(K chave, V valor) {
		this.chave = chave;
		this.valor = valor;
	}

	public K getChave() {
		return chave;
	}

	public V getValor() {
		return valor;
	}

	@Override
	public int compareTo(Entrada<K, V> outra) {
		return this.chave.compareTo(outra.getChave());
	}
}

class Evento implements Comparable<Evento> {
	private String nome;
	private String esporte;

	public Evento(String esporte, String nome) {
		this.esporte = esporte;
		this.nome = nome;
	}

	@Override
	public int compareTo(Evento outro) {
		int esporteComparacao = this.esporte.compareTo(outro.esporte);
		return (esporteComparacao != 0) ? esporteComparacao : this.nome.compareTo(outro.nome);
	}

	@Override
	public String toString() {
		return esporte + " - " + nome;
	}
}

class TabelaHash<K extends Comparable<? super K>, V> {
	private ABB<Entrada<K, V>>[] tabela;
	private int capacidade;

	@SuppressWarnings("unchecked")
	public TabelaHash(int capacidade) {
		this.capacidade = capacidade;
		this.tabela = new ABB[capacidade];
		for (int i = 0; i < capacidade; i++) {
			this.tabela[i] = new ABB<>();
		}
	}

	private int funcaoHash(K chave) {
		return Math.abs(chave.hashCode() % this.capacidade);
	}

	public void inserir(K chave, V item) {
		int posicao = funcaoHash(chave);
		this.tabela[posicao].adicionar(new Entrada<>(chave, item));
	}

	public V pesquisar(K chave) {
		int posicao = funcaoHash(chave);
		for (No<Entrada<K, V>> no = this.tabela[posicao].raiz; no != null; no = no.getDireita()) {
			if (no.getItem().getChave().equals(chave)) {
				return no.getItem().getValor();
			}
		}
		return null;
	}
}

public class GerenciadorEventosHash {
	public static void main(String[] args) {
		try {
			TabelaHash<LocalDate, ABB<Evento>> tabelaEventos = new TabelaHash<>(100);
			BufferedReader br = new BufferedReader(new FileReader("medallists.csv"));
			String linha;
			br.readLine();

			while ((linha = br.readLine()) != null) {
				String[] partes = linha.split(",");
				LocalDate data = LocalDate.parse(partes[1], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				String esporte = partes[6];
				String nomeEvento = partes[7];

				ABB<Evento> eventosNaData = tabelaEventos.pesquisar(data);
				if (eventosNaData == null) {
					eventosNaData = new ABB<>();
					tabelaEventos.inserir(data, eventosNaData);
				}
				eventosNaData.adicionar(new Evento(esporte, nomeEvento));
			}
			br.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String comando;
			while (!(comando = in.readLine()).equals("FIM")) {
				LocalDate data = LocalDate.parse(comando, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
				ABB<Evento> eventosNaData = tabelaEventos.pesquisar(data);
				System.out.println("Eventos do dia " + comando);
				if (eventosNaData != null) {
					eventosNaData.caminhamentoEmOrdem();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
