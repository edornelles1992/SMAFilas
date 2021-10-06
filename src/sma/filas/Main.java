package sma.filas;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

	private static String[] seedsCarregadas;
	private static int qtdSimulacoes;
	private static int qtdAleatorios = 100000;
	private static int totalFilas = 0;
	private static double primeiroClienteTempo;

	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("========= SIMULADOR DE FILA SIMPLES ==========");
		ArrayList<Fila> filas = lerDadosFila();
		executaSimulador(filas);
		System.out.println("==============FIM===================");
	}

	private static void executaSimulador(ArrayList<Fila> filas) throws FileNotFoundException {
		displayDadosRecebidos(filas);
		ArrayList<Fila> filasProcessadas = new ArrayList<>();
		System.out.println("EXECUTANDO SIMULAÇÕES...");
		for (int i = 0; i < qtdSimulacoes; i++) {
			System.out.println();
			System.out.print("========= EXECUÇÃO NUMERO: " + (i + 1) + " ======== SEED: " + seedsCarregadas[i]);
			Simulador simulador = new Simulador(filas, Integer.parseInt(seedsCarregadas[i]), qtdAleatorios);
			simulador.executa(filas, qtdAleatorios);
			// informaResultadoExecucao(filas, i);
			for (Fila f : filas)
				filasProcessadas.add(new Fila(f));

			System.out.print(" ...OK");
		}

		calculaMediaExecucoes(filasProcessadas);
	}

	private static void calculaMediaExecucoes(ArrayList<Fila> filasProcessadas) {
		System.out.println();
		System.out.println();
		System.out.println("SIMULAÇÕES ENCERRADAS...");
		System.out.println();
		System.out.println("====MEDIA DAS EXECUÇÕES========");
		System.out.println();

		double totalTempoMedio = 0.0;
		Map<Integer, ArrayList<Fila>> filasAgrupadas = new HashMap<>();

		for (int i = 0; i < totalFilas; i++) {
			filasAgrupadas.put(i + 1, new ArrayList<Fila>());
		}

		for (Fila f : filasProcessadas) {
			filasAgrupadas.get(f.nroFila).add(f);
		}

		for (int x = 1; x <= totalFilas; x++) {
			ArrayList<Fila> filas = filasAgrupadas.get(x);

				System.out.println("Fila F" + x);
				ArrayList<double[]> estadosFinais = new ArrayList<>();
				for (int i = 0; i < filas.size(); i++) {
					estadosFinais.add(filas.get(i).estado);
				}

				int qtdEstadosMax = filas.get(0).estado.length;

				double[] totalMediaEstados = new double[qtdEstadosMax];

				for (int i = 0; i < qtdEstadosMax; i++) {
					double somador = 0.0;
					double[] estado = { 0 };

					// soma valores de um estado de todas as filas
					int j;
					for (j = 0; j < estadosFinais.size(); j++) {
						estado = estadosFinais.get(j);
						somador += estado[i];
					}
					totalMediaEstados[i] = somador / j;
				}

				// total de tempo medio

				for (int i = 0; i < totalMediaEstados.length; i++) {
					totalTempoMedio += totalMediaEstados[i];
				}

				// calcula probabilidade de cada estado da fila.
				for (int i = 0; i < totalMediaEstados.length; i++) {
					String probabilidade = String.format("%.2f", (totalMediaEstados[i] * 100) / totalTempoMedio);
					System.out.println("Estado " + i + ": " + String.format("%.2f", totalMediaEstados[i])
							+ "  |   Probabilidade: " + probabilidade + "%");
				}		
				System.out.println();
		}

		System.out.println();
		System.out.println("Média de tempo das execuções: " + String.format("%.2f", totalTempoMedio));
		System.out.println();
	}

	private static void displayDadosRecebidos(ArrayList<Fila> filas) {
		System.out.println("===== DADOS RECEBIDOS ===== ");
		System.out.println("seeds: " + Arrays.toString(seedsCarregadas));
		System.out.println("Primeiro cliente no tempo: " + primeiroClienteTempo);
		System.out.println("NRO ALEATORIOS POR SIMULAÇÃO: "+qtdAleatorios);
		System.out.println("============FILAS===============");
		for (int i = 0; i < filas.size(); i++) {
			System.out.print("FILA " + (i + 1));

			System.out.println(" (G/G/" + filas.get(i).numeroServidores + "/" + filas.get(i).capacidadeFila + ")");
			if (i == 0) {
				System.out.println(
						"CHEGADA: " + filas.get(i).tempoChegadaMinimo + " até " + filas.get(i).tempoChegadaMaximo);
			}
			System.out.println("TEMPO ATENDIMENTO: " + filas.get(i).tempoAtendimentoMinimo + " até "
					+ filas.get(i).tempoAtendimentoMaximo);
			System.out.println("==========================");
		}
	}

	private static ArrayList<Fila> lerDadosFila() throws FileNotFoundException {
		Scanner in = new Scanner(new FileReader("parametros.txt"));
		ArrayList<Fila> filas = new ArrayList<>();

		int nroFilas = 1;
		int cont = 0;
		int[] parametros = new int[7]; // primeira fila recebe 6 parametros
		Fila fila = new Fila();
		while (in.hasNextLine()) {
			String comando = in.nextLine();
			if (comando.contains("SEEDS")) { // linha que corresponde a seeds
				seedsCarregadas = comando.split(":")[1].split(",");
				qtdSimulacoes = seedsCarregadas.length;
				continue;
			} else if (comando.contains("FIM_FILA")) { // fim da fila prepara dados para prï¿½xima, se houver.
				cont = 0;
				parametros = new int[4]; // demais filas 4 parametros
				filas.add(fila);
				fila = new Fila();
				nroFilas++;
				continue;
			} else {
				parametros[cont] = Integer
						.parseInt(comando.contains("PRIMEIRO_CLIENTE_TEMPO") ? comando.split(":")[1] : comando);
				if (nroFilas == 1) { // primeira fila sendo carregad
					fila.primeiroClienteTempo = parametros[0]; // ***** Fixo conforme enunciado do trabalho
					primeiroClienteTempo = fila.primeiroClienteTempo;
					fila.tempoChegadaMinimo = parametros[1];
					fila.tempoChegadaMaximo = parametros[2];
					fila.tempoAtendimentoMinimo = parametros[3];
					fila.tempoAtendimentoMaximo = parametros[4];
					fila.numeroServidores = parametros[5];
					fila.capacidadeFila = parametros[6];
					fila.nroFila = nroFilas;
				} else { // demais filas sem precisar o tempo de chegada
					fila.tempoAtendimentoMinimo = parametros[0];
					fila.tempoAtendimentoMaximo = parametros[1];
					fila.numeroServidores = parametros[2];
					fila.capacidadeFila = parametros[3];
					fila.nroFila = nroFilas;
				}
				cont++;
			}
		}

		totalFilas = nroFilas - 1;
		return filas;
	}

}
