package praticapegadinha;

import java.util.Scanner;

public class PegadinhaEvergreen {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			String linha1 = scanner.nextLine().trim();
			if (linha1.equals("FIM")) {
				break;
			}
			String linha2 = scanner.nextLine().trim();

			System.out.println(intercalarStrings(linha1, linha2));
		}

		scanner.close();
	}

	public static String intercalarStrings(String s1, String s2) {
		StringBuilder resultado = new StringBuilder();
		int i = 0, j = 0;
		int tam1 = s1.length(), tam2 = s2.length();

		while (i < tam1 || j < tam2) {
			for (int k = 0; k < 2 && i < tam1; k++, i++) {
				resultado.append(s1.charAt(i));
			}
			for (int k = 0; k < 2 && j < tam2; k++, j++) {
				resultado.append(s2.charAt(j));
			}
		}

		return resultado.toString();
	}
}
