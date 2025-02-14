package trabalhopraticometodosordenacao1;

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
	private int ouro;
	private int prata;
	private int bronze;

	public Medalhista(String nome, String genero, LocalDate nascimento, String pais, int ouro, int prata, int bronze) {
		this.nome = nome;
		this.genero = genero;
		this.nascimento = nascimento;
		this.pais = pais;
		this.ouro = ouro;
		this.prata = prata;
		this.bronze = bronze;
	}

	public String getNome() {
		return nome;
	}

	public int getOuro() {
		return ouro;
	}

	public int getPrata() {
		return prata;
	}

	public int getBronze() {
		return bronze;
	}

	@Override
	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return nome + ", " + genero + ". Nascimento: " + nascimento.format(formatter) + ". Pais: " + pais
				+ "\nQuantidade de medalhas de ouro: " + ouro
				+ (prata > 0 ? "\nQuantidade de medalhas de prata: " + prata : "")
				+ (bronze > 0 ? "\nQuantidade de medalhas de bronze: " + bronze : "") + "\n";
	}
}

interface IOrdenator<T> {
	T[] ordenar();

	void setComparador(Comparator<T> comparador);

	int getComparacoes();

	int getMovimentacoes();

	double getTempoOrdenacao();
}

public class BubbleSort<T> implements IOrdenator<T> {
	private T[] array;
	private Comparator<T> comparador;
	private int comparacoes;
	private int movimentacoes;
	private double tempoOrdenacao;

	public BubbleSort(T[] array) {
		this.array = array;
		this.comparacoes = 0;
		this.movimentacoes = 0;
		this.tempoOrdenacao = 0;
	}

	@Override
	public T[] ordenar() {
		long inicio = System.currentTimeMillis();
		int n = array.length;
		boolean trocado;

		do {
			trocado = false;
			for (int i = 0; i < n - 1; i++) {
				comparacoes++;
				if (comparador.compare(array[i], array[i + 1]) > 0) {
					T temp = array[i];
					array[i] = array[i + 1];
					array[i + 1] = temp;
					movimentacoes++;
					trocado = true;
				}
			}
			n--;
		} while (trocado);

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
			br.readLine();

			Medalhista[] medalhistas = new Medalhista[1000];
			int count = 0;

			while ((linha = br.readLine()) != null) {
				String[] partes = linha.split(",");
				String nome = partes[0];
				String genero = partes[1];
				LocalDate nascimento = LocalDate.parse(partes[2], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				String pais = partes[3];
				int ouro = Integer.parseInt(partes[4]);
				int prata = Integer.parseInt(partes[5]);
				int bronze = Integer.parseInt(partes[6]);

				Medalhista medalhista = new Medalhista(nome, genero, nascimento, pais, ouro, prata, bronze);
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

			BubbleSort<Medalhista> bubbleSort = new BubbleSort<>(aOrdenar);
			bubbleSort.setComparador(new Comparator<Medalhista>() {
				@Override
				public int compare(Medalhista m1, Medalhista m2) {
					if (m1.getOuro() != m2.getOuro()) {
						return Integer.compare(m2.getOuro(), m1.getOuro());
					}
					if (m1.getPrata() != m2.getPrata()) {
						return Integer.compare(m2.getPrata(), m1.getPrata());
					}
					if (m1.getBronze() != m2.getBronze()) {
						return Integer.compare(m2.getBronze(), m1.getBronze());
					}
					return m1.getNome().compareToIgnoreCase(m2.getNome());
				}
			});
			bubbleSort.ordenar();

			for (Medalhista m : aOrdenar) {
				System.out.println(m);
			}

			BufferedWriter log = new BufferedWriter(new FileWriter("matricula_bubblesort.txt"));
			log.write("1234567\t" + bubbleSort.getTempoOrdenacao() + "\t" + bubbleSort.getComparacoes() + "\t"
					+ bubbleSort.getMovimentacoes());
			log.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
