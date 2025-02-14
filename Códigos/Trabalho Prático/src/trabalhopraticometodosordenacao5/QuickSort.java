package trabalhopraticometodosordenacao5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;

class Pais {
	private String nome;
	private int ouro;
	private int prata;
	private int bronze;

	public Pais(String nome) {
		this.nome = nome;
		this.ouro = 0;
		this.prata = 0;
		this.bronze = 0;
	}

	public void adicionarMedalha(String tipo) {
		switch (tipo) {
		case "ouro":
			this.ouro++;
			break;
		case "prata":
			this.prata++;
			break;
		case "bronze":
			this.bronze++;
			break;
		}
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
		return nome + ": " + String.format("%02d", ouro) + " ouros " + String.format("%02d", prata) + " pratas "
				+ String.format("%02d", bronze) + " bronzes Total: " + (ouro + prata + bronze) + " medalhas.";
	}
}

interface IOrdenator<T> {
	T[] ordenar();

	void setComparador(Comparator<T> comparador);

	int getComparacoes();

	int getMovimentacoes();

	double getTempoOrdenacao();
}

public class QuickSort<T> implements IOrdenator<T> {
	private T[] array;
	private Comparator<T> comparador;
	private int comparacoes;
	private int movimentacoes;
	private double tempoOrdenacao;

	public QuickSort(T[] array) {
		this.array = array;
		this.comparacoes = 0;
		this.movimentacoes = 0;
		this.tempoOrdenacao = 0;
	}

	@Override
	public T[] ordenar() {
		long inicio = System.currentTimeMillis();
		quicksort(0, array.length - 1);
		tempoOrdenacao = System.currentTimeMillis() - inicio;
		return array;
	}

	private void quicksort(int esq, int dir) {
		if (esq < dir) {
			int pivo = particionar(esq, dir);
			quicksort(esq, pivo - 1);
			quicksort(pivo + 1, dir);
		}
	}

	private int particionar(int esq, int dir) {
		T pivo = array[dir];
		int i = esq - 1;

		for (int j = esq; j < dir; j++) {
			comparacoes++;
			if (comparador.compare(array[j], pivo) > 0) {
				i++;
				trocar(i, j);
			}
		}
		trocar(i + 1, dir);
		return i + 1;
	}

	private void trocar(int i, int j) {
		T temp = array[i];
		array[i] = array[j];
		array[j] = temp;
		movimentacoes++;
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

			HashMap<String, Pais> paises = new HashMap<>();
			while ((linha = br.readLine()) != null) {
				String[] partes = linha.split(",");
				String nomePais = partes[3];
				String tipoMedalha = partes[4].toLowerCase();

				paises.putIfAbsent(nomePais, new Pais(nomePais));
				paises.get(nomePais).adicionarMedalha(tipoMedalha);
			}
			br.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			int n = Integer.parseInt(in.readLine());
			Pais[] aOrdenar = new Pais[n];
			for (int i = 0; i < n; i++) {
				aOrdenar[i] = paises.get(in.readLine());
			}

			QuickSort<Pais> quickSort = new QuickSort<>(aOrdenar);
			quickSort.setComparador((p1, p2) -> {
				if (p1.getOuro() != p2.getOuro())
					return Integer.compare(p2.getOuro(), p1.getOuro());
				if (p1.getPrata() != p2.getPrata())
					return Integer.compare(p2.getPrata(), p1.getPrata());
				return Integer.compare(p2.getBronze(), p1.getBronze());
			});
			quickSort.ordenar();

			for (Pais p : aOrdenar) {
				System.out.println(p);
			}

			BufferedWriter log = new BufferedWriter(new FileWriter("matricula_quicksort.txt"));
			log.write("1234567\t" + quickSort.getTempoOrdenacao() + "\t" + quickSort.getComparacoes() + "\t"
					+ quickSort.getMovimentacoes());
			log.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
