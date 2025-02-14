package praticarecursividade;

import java.util.Scanner;

public class ContaOcorrencias {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			String linhaVetor = scanner.nextLine().trim();
			if (linhaVetor.equals("FIM")) {
				break;
			}

			String[] elementos = linhaVetor.split(";");
			int[] vetor = new int[elementos.length];
			for (int i = 0; i < elementos.length; i++) {
				vetor[i] = Integer.parseInt(elementos[i]);
			}

			int x = Integer.parseInt(scanner.nextLine().trim());

			System.out.println(contaOcorrencias(vetor, x));
		}

		scanner.close();
	}

	public static int contaOcorrencias(int[] vetor, int x) {
		return contaOcorrenciasRec(vetor, x, vetor.length - 1);
	}

	private static int contaOcorrenciasRec(int[] vetor, int x, int index) {
		if (index < 0) {
			return 0;
		}
		return (vetor[index] == x ? 1 : 0) + contaOcorrenciasRec(vetor, x, index - 1);
	}
}
