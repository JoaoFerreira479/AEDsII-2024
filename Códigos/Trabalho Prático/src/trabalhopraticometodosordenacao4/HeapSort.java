package trabalhopraticometodosordenacao4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

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

	public String getPais() {
		return pais;
	}

	@Override
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return nome + ", " + genero + ". Nascimento: " + nascimento.format(formatter) + ". Pais: " + pais + "\n";
	}
}

interface IOrdenator<T> {
	T[] ordenar();

	void setComparador(Comparator<T> comparador);

	int getComparacoes();

	int getMovimentacoes();

	double getTempoOrdenacao();
}

public class HeapSort<T> implements IOrdenator<T> {
	private T[] array;
	private Comparator<T> comparador;
	private int comparacoes;
	private int movimentacoes;
	private double tempoOrdenacao;

	public HeapSort(T[] array) {
		this.array = array;
		this.comparacoes = 0;
		this.movimentacoes = 0;
		this.tempoOrdenacao = 0;
	}

	@Override
	public T[] ordenar() {
		long inicio = System.currentTimeMillis();
		int n = array.length;

		for (int i = n / 2 - 1; i >= 0; i--) {
			heapify(n, i);
		}

		for (int i = n - 1; i > 0; i--) {
			T temp = array[0];
			array[0] = array[i];
			array[i] = temp;
			movimentacoes++;
			heapify(i, 0);
		}

		tempoOrdenacao = System.currentTimeMillis() - inicio;
		return array;
	}

	private void heapify(int n, int i) {
		int maior = i;
		int esquerda = 2 * i + 1;
		int direita = 2 * i + 2;

		if (esquerda < n) {
			comparacoes++;
			if (comparador.compare(array[esquerda], array[maior]) > 0) {
				maior = esquerda;
			}
		}

		if (direita < n) {
			comparacoes++;
			if (comparador.compare(array[direita], array[maior]) > 0) {
				maior = direita;
			}
		}

		if (maior != i) {
			T temp = array[i];
			array[i] = array[maior];
			array[maior] = temp;
			movimentacoes++;
			heapify(n, maior);
		}
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

			Medalhista[] medalhistas = new Medalhista[1000];
			int count = 0;

			while ((linha = br.readLine()) != null) {
				String[] partes = linha.split(",");
				String nome = partes[0];
				String genero = partes[1];
				LocalDate nascimento = LocalDate.parse(partes[2], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				String pais = partes[3];

				Medalhista medalhista = new Medalhista(nome, genero, nascimento, pais);
				medalhistas[count++] = medalhista;
			}
			br.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			int n = Integer.parseInt(in.readLine());
			Medalhista[] aOrdenar = new Medalhista[n];
			for (int i = 0; i < n; i++) {
				String nomeBusca = in.readLine();
				for (int j = 0; j < count; j++) {
					if (medalhistas[j].getNome().equalsIgnoreCase(nomeBusca)) {
						aOrdenar[i] = medalhistas[j];
						break;
					}
				}
			}

			HeapSort<Medalhista> heapSort = new HeapSort<>(aOrdenar);
			heapSort.setComparador(new Comparator<Medalhista>() {
				@Override
				public int compare(Medalhista m1, Medalhista m2) {
					int paisCompare = m1.getPais().compareToIgnoreCase(m2.getPais());
					if (paisCompare != 0) {
						return paisCompare;
					}
					return m1.getNome().compareToIgnoreCase(m2.getNome());
				}
			});
			heapSort.ordenar();

			for (Medalhista m : aOrdenar) {
				System.out.println(m);
			}

			BufferedWriter log = new BufferedWriter(new FileWriter("matricula_heapsort.txt"));
			log.write("1234567\t" + heapSort.getTempoOrdenacao() + "\t" + heapSort.getComparacoes() + "\t"
					+ heapSort.getMovimentacoes());
			log.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
