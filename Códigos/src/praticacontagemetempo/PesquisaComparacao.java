package praticacontagemetempo;

import java.util.Arrays;
import java.util.Random;

public class PesquisaComparacao {

	public static void main(String[] args) {
		long limiteN = 2_000_000_000; // Limite de N
		int n = 7_500_000; // Tamanho inicial do vetor
		Random rand = new Random();

		System.out.println("Pesquisa Sequencial:");
		System.out.println("N\tCenário\tComparações\tTempo (ns)");

		while (n <= limiteN) {
			int[] vetor = gerarVetorOrdenado(n);
			int qualquer = vetor[rand.nextInt(n)]; // Número existente
			int primeiro = vetor[0]; // Primeiro elemento
			int inexistente = -1; // Número que não está no vetor

			executarPesquisa("Sequencial", n, vetor, qualquer, primeiro, inexistente);
			n *= 2;
		}

		n = 7_500_000; // Reiniciar para pesquisa binária
		System.out.println("\nPesquisa Binária:");
		System.out.println("N\tCenário\tComparações\tTempo (ns)");

		while (n <= limiteN) {
			int[] vetor = gerarVetorOrdenado(n);
			int qualquer = vetor[rand.nextInt(n)];
			int primeiro = vetor[0];
			int inexistente = -1;

			executarPesquisa("Binária", n, vetor, qualquer, primeiro, inexistente);
			n *= 2;
		}
	}

	// Método que executa as pesquisas para cada cenário
	public static void executarPesquisa(String tipo, int n, int[] vetor, int qualquer, int primeiro, int inexistente) {
		realizarTeste(tipo, "Qualquer", vetor, qualquer);
		realizarTeste(tipo, "Primeiro", vetor, primeiro);
		realizarTeste(tipo, "Inexistente", vetor, inexistente);
	}

	// Método que realiza os testes com 5 medições e calcula a média das 3 centrais
	public static void realizarTeste(String tipo, String caso, int[] vetor, int chave) {
		long[] tempos = new long[5];
		int comparacoes = 0;

		for (int i = 0; i < 5; i++) {
			long inicio = System.nanoTime();
			if (tipo.equals("Sequencial")) {
				comparacoes = pesquisaSequencial(vetor, chave);
			} else {
				comparacoes = pesquisaBinaria(vetor, chave);
			}
			long fim = System.nanoTime();
			tempos[i] = fim - inicio;
		}

		long tempoMedio = calcularMediaFiltrada(tempos);
		System.out.println(vetor.length + "\t" + caso + "\t" + comparacoes + "\t" + tempoMedio);
	}

	// Pesquisa Sequencial com contagem de comparações
	public static int pesquisaSequencial(int[] vetor, int chave) {
		int comparacoes = 0;
		for (int i = 0; i < vetor.length; i++) {
			comparacoes++;
			if (vetor[i] == chave) {
				return comparacoes;
			}
		}
		return comparacoes;
	}

	// Pesquisa Binária com contagem de comparações
	public static int pesquisaBinaria(int[] vetor, int chave) {
		int inicio = 0, fim = vetor.length - 1, comparacoes = 0;

		while (inicio <= fim) {
			comparacoes++;
			int meio = (inicio + fim) / 2;
			if (vetor[meio] == chave) {
				return comparacoes;
			} else if (vetor[meio] < chave) {
				inicio = meio + 1;
			} else {
				fim = meio - 1;
			}
		}
		return comparacoes;
	}

	// Geração de vetor ordenado para teste
	public static int[] gerarVetorOrdenado(int n) {
		int[] vetor = new int[n];
		for (int i = 0; i < n; i++) {
			vetor[i] = i * 2; // Evitar valores repetidos
		}
		return vetor;
	}

	// Método para calcular a média das 3 medições centrais
	public static long calcularMediaFiltrada(long[] tempos) {
		Arrays.sort(tempos);
		return (tempos[1] + tempos[2] + tempos[3]) / 3;
	}
}
