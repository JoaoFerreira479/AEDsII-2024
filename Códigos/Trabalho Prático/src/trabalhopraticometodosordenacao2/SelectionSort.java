package trabalhopraticometodosordenacao2;

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

	public LocalDate getNascimento() {
		return nascimento;
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

public class SelectionSort<T> implements IOrdenator<T> {
	private T[] array;
	private Comparator<T> comparador;
	private int comparacoes;
	private int movimentacoes;
	private double tempoOrdenacao;

	public SelectionSort(T[] array) {
		this.array = array;
		this.comparacoes = 0;
		this.movimentacoes = 0;
		this.tempoOrdenacao = 0;
	}

	@Override
	public T[] ordenar() {
		long inicio = System.currentTimeMillis();
		int n = array.length;

		for (int i = 0; i < n - 1; i++) {
			int minIndex = i;
			for (int j = i + 1; j < n; j++) {
				comparacoes++;
				if (comparador.compare(array[j], array[minIndex]) < 0) {
					minIndex = j;
				}
			}
			if (minIndex != i) {
				T temp = array[i];
				array[i] = array[minIndex];
				array[minIndex] = temp;
				movimentacoes++;
			}
		}

		tempoOrdenacao = System.currentTimeMillis() - inicio;
		return array;
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
			br.readLine(); // Ignora cabeçalho

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

			SelectionSort<Medalhista> selectionSort = new SelectionSort<>(aOrdenar);
			selectionSort.setComparador(new Comparator<Medalhista>() {
				@Override
				public int compare(Medalhista m1, Medalhista m2) {
					if (!m1.getNascimento().equals(m2.getNascimento())) {
						return m1.getNascimento().compareTo(m2.getNascimento());
					}
					return m1.getNome().compareToIgnoreCase(m2.getNome());
				}
			});
			selectionSort.ordenar();

			for (Medalhista m : aOrdenar) {
				System.out.println(m);
			}

			BufferedWriter log = new BufferedWriter(new FileWriter("matricula_selecao.txt"));
			log.write("1234567\t" + selectionSort.getTempoOrdenacao() + "\t" + selectionSort.getComparacoes() + "\t"
					+ selectionSort.getMovimentacoes());
			log.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
