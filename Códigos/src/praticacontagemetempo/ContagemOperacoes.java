package praticacontagemetempo;

import java.util.Arrays;

public class ContagemOperacoes {

	public static void main(String[] args) {
		long limiteTempo = 50_000_000_000L; // 50 segundos em nanossegundos
		int n = 15625; // Tamanho inicial

		System.out.println("Resultados do Algoritmo A:");
		System.out.println("N\tOperações\tTempo (ns)");
		while (n <= 2_000_000_000) {
			long[] tempos = new long[5];
			int operacoes = 0;

			// Executar 5 medições
			for (int i = 0; i < 5; i++) {
				long inicio = System.nanoTime();
				operacoes = codigoA(n);
				long fim = System.nanoTime();
				tempos[i] = fim - inicio;
			}

			// Calcular a média das 3 medições centrais
			long tempoMedio = calcularMediaFiltrada(tempos);
			System.out.println(n + "\t" + operacoes + "\t" + tempoMedio);

			if (tempoMedio > limiteTempo) {
				break;
			}
			n *= 2; // Dobrar o tamanho
		}

		System.out.println("\nResultados do Algoritmo B:");
		System.out.println("N\tOperações\tTempo (ns)");

		n = 15625; // Reiniciar tamanho

		while (n <= 2_000_000_000) {
			long[] tempos = new long[5];
			int[] vetor = new int[n];
			Arrays.fill(vetor, 1); // Preencher o vetor para teste
			int operacoes = 0;

			// Executar 5 medições
			for (int i = 0; i < 5; i++) {
				long inicio = System.nanoTime();
				operacoes = codigoB(vetor);
				long fim = System.nanoTime();
				tempos[i] = fim - inicio;
			}

			// Calcular a média das 3 medições centrais
			long tempoMedio = calcularMediaFiltrada(tempos);
			System.out.println(n + "\t" + operacoes + "\t" + tempoMedio);

			if (tempoMedio > limiteTempo) {
				break;
			}
			n *= 2; // Dobrar o tamanho
		}
	}

	// Algoritmo A com contagem de operações (removendo a variável b)
	public static int codigoA(int n) {
		int operacoes = 0;

		for (int i = 0; i <= n; i += 2) {
			operacoes++; // Contar cada iteração
		}

		return operacoes;
	}

	// Algoritmo B com contagem de operações
	public static int codigoB(int[] vet) {
		int operacoes = 0;

		for (int i = 0; i < vet.length; i += 2) {
			for (int j = i; j < (vet.length / 2); j++) {
				vet[i] += vet[j];
				operacoes++; // Contar cada operação de soma
			}
		}

		return operacoes;
	}

	// Calcular a média das 3 medições centrais
	public static long calcularMediaFiltrada(long[] tempos) {
		Arrays.sort(tempos);
		return (tempos[1] + tempos[2] + tempos[3]) / 3;
	}
}
