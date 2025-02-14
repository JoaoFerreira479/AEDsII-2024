package praticaordenacaoselecao;

import java.util.Scanner;

public class OrdenacaoPorSelecao {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			String linha = scanner.nextLine().trim();

			if (linha.equals("FIM")) {
				break;
			}

			String[] elementos = linha.split(";");
			int[] vetor = new int[elementos.length];
			for (int i = 0; i < elementos.length; i++) {
				vetor[i] = Integer.parseInt(elementos[i]);
			}

			int comparacoes = ordenarPorSelecao(vetor);

			for (int i = 0; i < vetor.length; i++) {
				System.out.print(vetor[i]);
				if (i < vetor.length - 1) {
					System.out.print(" ");
				}
			}
			System.out.println();

			System.out.println("Comparacoes realizadas: " + comparacoes);
		}

		scanner.close();
	}

	public static int ordenarPorSelecao(int[] vetor) {
		int comparacoes = 0;
		int n = vetor.length;

		for (int i = 0; i < n - 1; i++) {
			int indiceMenor = i;

			for (int j = i + 1; j < n; j++) {
				comparacoes++;
				if (vetor[j] < vetor[indiceMenor]) {
					indiceMenor = j;
				}
			}

			int temp = vetor[i];
			vetor[i] = vetor[indiceMenor];
			vetor[indiceMenor] = temp;
		}

		return comparacoes;
	}
}
