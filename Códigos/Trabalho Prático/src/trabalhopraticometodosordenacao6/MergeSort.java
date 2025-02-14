package trabalhopraticometodosordenacao6;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.lang.reflect.Array;

class Evento {
	private String esporte;
	private String nome;
	private Medalhista[] medalhistas;
	private int totalMedalhistas;

	public Evento(String esporte, String nome) {
		this.esporte = esporte;
		this.nome = nome;
		this.medalhistas = new Medalhista[50];
		this.totalMedalhistas = 0;
	}

	public void adicionarMedalhista(Medalhista m) {
		medalhistas[totalMedalhistas++] = m;
	}

	public String getEsporte() {
		return esporte;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(esporte).append(" - ").append(nome).append("\n");
		for (int i = 0; i < totalMedalhistas; i++) {
			sb.append(medalhistas[i]).append("\n");
		}
		return sb.toString();
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

interface IOrdenator<T> {
	T[] ordenar();

	void setComparador(Comparator<T> comparador);

	int getComparacoes();

	int getMovimentacoes();

	double getTempoOrdenacao();
}

public class MergeSort<T> implements IOrdenator<T> {
	private T[] array;
	private Comparator<T> comparador;
	private int comparacoes;
	private int movimentacoes;
	private double tempoOrdenacao;

	public MergeSort(T[] array) {
		this.array = array;
		this.comparacoes = 0;
		this.movimentacoes = 0;
		this.tempoOrdenacao = 0;
	}

	@Override
	public T[] ordenar() {
		long inicio = System.currentTimeMillis();
		mergesort(0, array.length - 1);
		tempoOrdenacao = System.currentTimeMillis() - inicio;
		return array;
	}

	private void mergesort(int esq, int dir) {
		if (esq < dir) {
			int meio = (esq + dir) / 2;
			mergesort(esq, meio);
			mergesort(meio + 1, dir);
			merge(esq, meio, dir);
		}
	}

	@SuppressWarnings("unchecked")
	private void merge(int esq, int meio, int dir) {
		int n1 = meio - esq + 1;
		int n2 = dir - meio;

		T[] esquerda = (T[]) Array.newInstance(array.getClass().getComponentType(), n1);
		T[] direita = (T[]) Array.newInstance(array.getClass().getComponentType(), n2);

		for (int i = 0; i < n1; i++)
			esquerda[i] = array[esq + i];
		for (int j = 0; j < n2; j++)
			direita[j] = array[meio + 1 + j];

		int i = 0, j = 0, k = esq;
		while (i < n1 && j < n2) {
			comparacoes++;
			if (comparador.compare(esquerda[i], direita[j]) <= 0) {
				array[k++] = esquerda[i++];
			} else {
				array[k++] = direita[j++];
			}
			movimentacoes++;
		}

		while (i < n1)
			array[k++] = esquerda[i++];
		while (j < n2)
			array[k++] = direita[j++];
	}

	@Override
	public void setComparador(Comparator<T> comparador) {
		this.comparador = comparador;
	}

	@Override
	public int getComparacoes() {
		return comparacoes;
	}

	@Override
	public int getMovimentacoes() {
		return movimentacoes;
	}

	@Override
	public double getTempoOrdenacao() {
		return tempoOrdenacao;
	}

	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new FileReader("medallists.csv"));
			String linha;
			br.readLine();
			HashMap<String, Evento> eventos = new HashMap<>();
			while ((linha = br.readLine()) != null) {
				String[] partes = linha.split(",");
				String esporte = partes[6];
				String eventoNome = partes[7];
				eventos.putIfAbsent(esporte + " - " + eventoNome, new Evento(esporte, eventoNome));
				eventos.get(esporte + " - " + eventoNome).adicionarMedalhista(new Medalhista(partes[0], partes[1],
						LocalDate.parse(partes[2], DateTimeFormatter.ofPattern("yyyy-MM-dd")), partes[3]));
			}
			br.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			int n = Integer.parseInt(in.readLine());
			Evento[] aOrdenar = new Evento[n];
			for (int i = 0; i < n; i++)
				aOrdenar[i] = eventos.get(in.readLine());

			MergeSort<Evento> mergeSort = new MergeSort<>(aOrdenar);
			mergeSort.setComparador(Comparator.comparing(Evento::getEsporte).thenComparing(Evento::getNome));
			mergeSort.ordenar();
			for (Evento e : aOrdenar)
				System.out.println(e + "\n");

			BufferedWriter log = new BufferedWriter(new FileWriter("matricula_mergesort.txt"));
			log.write("1234567\t" + mergeSort.getTempoOrdenacao() + "\t" + mergeSort.getComparacoes() + "\t"
					+ mergeSort.getMovimentacoes());
			log.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
