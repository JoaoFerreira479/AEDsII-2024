package praticaleituraarquivotextoolimpiadas;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class Medalhista {
	private static final int MAX_MEDALHAS = 8;
	private String nome;
	private String genero;
	private LocalDate nascimento;
	private String pais;
	private Medalha[] medalhas;
	private int medalCount;

	public Medalhista(String nome, String genero, LocalDate nascimento, String pais) {
		this.nome = nome;
		this.genero = genero;
		this.nascimento = nascimento;
		this.pais = pais;
		this.medalhas = new Medalha[MAX_MEDALHAS];
		this.medalCount = 0;
	}

	public void incluirMedalha(Medalha medalha) {
		if (medalCount < MAX_MEDALHAS) {
			medalhas[medalCount++] = medalha;
		}
	}

	public String relatorioDeMedalhas(TipoMedalha tipo) {
		StringBuilder relatorio = new StringBuilder();
		boolean possuiMedalha = false;

		for (int i = 0; i < medalCount; i++) {
			if (medalhas[i].getTipo() == tipo) {
				relatorio.append(medalhas[i].toString()).append("\n");
				possuiMedalha = true;
			}
		}

		if (!possuiMedalha) {
			relatorio.append("Nao possui medalha de ").append(tipo).append("\n");
		}
		return relatorio.toString();
	}

	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return nome + ", " + genero + ". Nascimento: " + nascimento.format(formatter) + ". Pais: " + pais;
	}

	public String getNome() {
		return nome;
	}
}

enum TipoMedalha {
	OURO, PRATA, BRONZE
}

class Medalha {
	private TipoMedalha tipo;
	private LocalDate data;
	private String disciplina;
	private String evento;

	public Medalha(TipoMedalha tipo, LocalDate data, String disciplina, String evento) {
		this.tipo = tipo;
		this.data = data;
		this.disciplina = disciplina;
		this.evento = evento;
	}

	public TipoMedalha getTipo() {
		return tipo;
	}

	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return tipo + " - " + disciplina + " - " + evento + " - " + data.format(formatter);
	}
}

public class Aplicacao {
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
				TipoMedalha tipo = TipoMedalha.valueOf(partes[4].toUpperCase());
				LocalDate data = LocalDate.parse(partes[5], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				String disciplina = partes[6];
				String evento = partes[7];

				Medalhista medalhista = encontrarOuCriar(medalhistas, count, nome, genero, nascimento, pais);
				medalhista.incluirMedalha(new Medalha(tipo, data, disciplina, evento));
				count++;
			}
			br.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			while (!(linha = in.readLine()).equals("FIM")) {
				String[] partes = linha.split(",");
				String nome = partes[0];
				TipoMedalha tipo = TipoMedalha.valueOf(partes[1].toUpperCase());

				Medalhista medalhista = buscarMedalhista(medalhistas, count, nome);
				if (medalhista != null) {
					System.out.println(medalhista);
					System.out.println(medalhista.relatorioDeMedalhas(tipo));
					System.out.println();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Medalhista encontrarOuCriar(Medalhista[] medalhistas, int count, String nome, String genero,
			LocalDate nascimento, String pais) {
		for (int i = 0; i < count; i++) {
			if (medalhistas[i].getNome().equals(nome)) {
				return medalhistas[i];
			}
		}
		Medalhista novo = new Medalhista(nome, genero, nascimento, pais);
		medalhistas[count] = novo;
		return novo;
	}

	private static Medalhista buscarMedalhista(Medalhista[] medalhistas, int count, String nome) {
		for (int i = 0; i < count; i++) {
			if (medalhistas[i].getNome().equals(nome)) {
				return medalhistas[i];
			}
		}
		return null;
	}
}
