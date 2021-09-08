package sma.filas;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

	private static String[] seedsCarregadas;
	private static int qtdSimulacoes;
	private static int qtdAleatorios = 100000;

	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("========= SIMULADOR DE FILA SIMPLES ==========");
		ArrayList<Fila> filas = lerDadosFila();
		executaSimulador(filas);
		System.out.println("==============FIM===================");
	}

	private static void executaSimulador(ArrayList<Fila> filas) throws FileNotFoundException {
		displayDadosRecebidos(filas);
		ArrayList<Fila> filasProcessadas = new ArrayList<>();
		for (int i = 0; i < qtdSimulacoes; i++) {
			System.out.println();
			System.out.print("========= EXECUÇÃO NUMERO: " + (i + 1) + " ======== SEED: " + seedsCarregadas[i]);
			Simulador simulador = new Simulador(filas, Integer.parseInt(seedsCarregadas[i]), qtdAleatorios);
			simulador.executa(filas, qtdAleatorios);
			// informaResultadoExecucao(filas, i);
			for (Fila f: filas)
				filasProcessadas.add(new Fila(f));
			
			System.out.print(" ...OK");
		}

			calculaMediaExecucoes(filasProcessadas);
	}

	private static void calculaMediaExecucoes(ArrayList<Fila> filas) {
		System.out.println();
		System.out.println();
		System.out.println("====MEDIA DAS EXECUÇÕES========");
		System.out.println();
		System.out.println();	
		
		ArrayList<double[]> estadosFinais = new ArrayList<>(); 
		for (int i = 0; i < filas.size(); i++) {			
			estadosFinais.add(filas.get(i).estado);			
		}
		
		int qtdEstadosMax = 0;		
		for (Fila fila : filas) {
			if (fila.estado.length > qtdEstadosMax)
				qtdEstadosMax = fila.estado.length;
		}
		
		double[] totalMediaEstados = new double[qtdEstadosMax];

		for (int i = 0; i < qtdEstadosMax; i++) {	
			double somador = 0.0;
			double[] estado = {0};
			
			//soma valores de um estado de todas as filas
			int j;
			for (j = 0; j < estadosFinais.size(); j++) {
				estado = estadosFinais.get(j);
				somador += estado[i];
			}			
			totalMediaEstados[i] = somador/j;
		}
		
		//total de tempo medio
		double totalTempoMedio = 0.0;
		for (int i = 0; i < totalMediaEstados.length; i++) {
			totalTempoMedio += totalMediaEstados[i];
		}
		
		//calcula probabilidade de cada estado da fila.
		for (int i = 0; i < totalMediaEstados.length; i++) {
			String probabilidade = String.format("%.2f", (totalMediaEstados[i] * 100) / totalTempoMedio);
			System.out.println("Estado " + i + ": " + String.format("%.2f", totalMediaEstados[i]) + "  |   Probabilidade: " + probabilidade + "%");
		}
		
		System.out.println();
		System.out.println("Média de tempo das execuções: " + String.format("%.2f", totalTempoMedio));
		System.out.println();
	}

	//Método para teste de uma execução isolada
	private static void informaResultadoExecucao(ArrayList<Fila> filas, int nroSimulacao) {
		System.out.println("=============RESULTADOS===============");
		System.out.println("Gerou " + 100000 + " Aleatórios!! FIM!!!");

		double tempoTotalSimulacao = 0;
		for (Fila f : filas) {
			double tempototal = 0;
			for (int i = 0; i < f.estado.length; i++) {
				tempototal += f.estado[i];
			}
			f.tempoTotal = tempototal;
			tempoTotalSimulacao += tempototal;
		}

		for (int x = 0; x < filas.size(); x++) {
			System.out.println();
			System.out.println("Fila " + (x + 1) + " Simulação " + (nroSimulacao + 1));
			System.out.println("Estado | Tempo | Probabilidade");
			for (int i = 0; i < filas.get(x).estado.length; i++) {
				String estado = String.format("%.2f", filas.get(x).estado[i]);
				String probabilidade = String.format("%.2f", (filas.get(x).estado[i] * 100) / filas.get(x).tempoTotal);
				System.out.println(i + "        " + estado + "       " + probabilidade + "%");
			}
		}

		String tempototaltxt = String.format("%.2f", tempoTotalSimulacao);
		System.out.println();
		System.out.println("tempo total: " + tempototaltxt);
		System.out.println("=================================");
	}

	private static void displayDadosRecebidos(ArrayList<Fila> filas) {
		System.out.println("===== DADOS RECEBIDOS ===== ");
		System.out.println("seeds: " + Arrays.toString(seedsCarregadas));
		System.out.println(" ");
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
			} else if (comando.contains("FIM_FILA")) { // fim da fila prepara dados para próxima, se houver.
				cont = 0;
				parametros = new int[4]; // demais filas 4 parametros
				filas.add(fila);
				nroFilas++;
				fila = new Fila();
				continue;
			} else {
				parametros[cont] = Integer
						.parseInt(comando.contains("PRIMEIRO_CLIENTE_TEMPO") ? comando.split(":")[1] : comando);
				if (nroFilas == 1) { // primeira fila sendo carregad
					fila.primeiroClienteTempo = parametros[0]; // ***** Fixo conforme enunciado do trabalho
					fila.tempoChegadaMinimo = parametros[1];
					fila.tempoChegadaMaximo = parametros[2];
					fila.tempoAtendimentoMinimo = parametros[3];
					fila.tempoAtendimentoMaximo = parametros[4];
					fila.numeroServidores = parametros[5];
					fila.capacidadeFila = parametros[6];
				} else { // demais filas sem precisar o tempo de chegada
					fila.tempoAtendimentoMinimo = parametros[0];
					fila.tempoAtendimentoMaximo = parametros[1];
					fila.numeroServidores = parametros[2];
					fila.capacidadeFila = parametros[3];
				}
				cont++;
			}
		}

		return filas;
	}

}
