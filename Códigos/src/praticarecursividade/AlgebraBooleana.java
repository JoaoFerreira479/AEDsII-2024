package praticarecursividade;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AlgebraBooleana {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			String linha = scanner.nextLine().trim();

			if (linha.equals("FIM")) {
				break;
			}

			String[] partes = linha.split(" ");
			int n = Integer.parseInt(partes[0]);
			int[] valores = new int[n];
			for (int i = 0; i < n; i++) {
				valores[i] = Integer.parseInt(partes[i + 1]);
			}
			String expressao = linha.substring(linha.indexOf(partes[n + 1]));

			boolean resultado = avaliarExpressao(expressao, valores);
			System.out.println(resultado ? "1" : "0");
		}

		scanner.close();
	}

	private static boolean avaliarExpressao(String expr, int[] valores) {
		expr = expr.trim();

		if (expr.startsWith("not(")) {
			return !avaliarExpressao(expr.substring(4, expr.length() - 1), valores);
		} else if (expr.startsWith("and(")) {
			String[] partes = separarArgumentos(expr.substring(4, expr.length() - 1));
			for (String parte : partes) {
				if (!avaliarExpressao(parte, valores)) {
					return false;
				}
			}
			return true;
		} else if (expr.startsWith("or(")) {
			String[] partes = separarArgumentos(expr.substring(3, expr.length() - 1));
			for (String parte : partes) {
				if (avaliarExpressao(parte, valores)) {
					return true;
				}
			}
			return false;
		} else {
			char var = expr.trim().charAt(0);
			int index = var - 'A';
			return valores[index] == 1;
		}
	}

	private static String[] separarArgumentos(String expr) {
		int nivel = 0;
		StringBuilder buffer = new StringBuilder();
		List<String> argumentos = new ArrayList<>();

		for (char c : expr.toCharArray()) {
			if (c == ',' && nivel == 0) {
				argumentos.add(buffer.toString().trim());
				buffer.setLength(0);
			} else {
				buffer.append(c);
				if (c == '(') {
					nivel++;
				} else if (c == ')') {
					nivel--;
				}
			}
		}

		if (buffer.length() > 0) {
			argumentos.add(buffer.toString().trim());
		}

		return argumentos.toArray(new String[0]);
	}
}
