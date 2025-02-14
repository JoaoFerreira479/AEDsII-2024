package praticarecursividade;

import java.util.Scanner;

public class Palindromo {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			String entrada = scanner.nextLine().trim();

			if (entrada.equals("FIM")) {
				break;
			}

			if (ehPalindromo(entrada)) {
				System.out.println("SIM");
			} else {
				System.out.println("NAO");
			}
		}

		scanner.close();
	}

	public static boolean ehPalindromo(String s) {
		return ehPalindromoRec(s, 0, s.length() - 1);
	}

	private static boolean ehPalindromoRec(String s, int inicio, int fim) {
		if (inicio >= fim) {
			return true;
		}

		if (s.charAt(inicio) != s.charAt(fim)) {
			return false;
		}

		return ehPalindromoRec(s, inicio + 1, fim - 1);
	}
}
