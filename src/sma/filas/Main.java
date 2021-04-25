package sma.filas;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

	private static String[] seedsCarregadas;
	private static int qtdSimulacoes;

	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("========= SIMULADOR DE FILA SIMPLES ==========");
		if (args == null || args.length != 1) {
			System.out.println("Informe o parametro que indica a quantidade de simulações desejadas no programa. Ex:"
					+ " java -jar simulador.jar 5");
			System.exit(1);
		}

		qtdSimulacoes = Integer.parseInt(args[0]);
		ArrayList<Fila> filas = lerDadosFila();
		double[][] estadoFinalFilas = new double[qtdSimulacoes][filas.get(0).capacidadeFila]; // TODO AJUSTAR ESTADO
																								// FINAL FILAS
		executaSimulador(filas, estadoFinalFilas);
		// calculaMediaExecucoes(estadoFinalFilas, filas);
		System.out.println("==============FIM===================");
	}

	private static void executaSimulador(ArrayList<Fila> filas, double[][] estadoFinalFilas)
			throws FileNotFoundException {
		displayDadosRecebidos(filas);
//		for (int i = 0; i < qtdSimulacoes; i++) {
//			System.out.println();
//			System.out.println("========= EXECUÇÃO NUMERO: " + (i + 1) + "========");
//			Simulador simulador = new Simulador(); 
//			double[] estadoFinalFinal = simulador.executaFila(100000, Long.parseLong(seedsCarregadas[i]));
//			// salvar os valores dos estados final da fila para calcular a media
//			// posteriormente
////			for (int j = 0; j < filas.capacidadeFila; j++) {
////				estadoFinalFilas[i][j] = estadoFinalFinal[j];
////			}
//		}
		//PARA TESTE PROVISORIO COM 1 SIMULACAO
		Simulador simulador = new Simulador(filas, Integer.parseInt(seedsCarregadas[0]), 100000); //PROVISóRIO 1 SEED PARA TESTE 
//		double[] estadoFinalFinal = simulador.executaFila(100000, Long.parseLong(seedsCarregadas[0]));
	}

//	private static void calculaMediaExecucoes(double[][] estadoFinalFilas, ArrayList<Fila> filas) {
//		System.out.println();
//		System.out.println("====MEDIA DAS EXECUÇÕES========");
//		System.out.println();
//		for (int i = 0; i < estadoFinalFilas.length; i++) {
//			double somadorColuna = 0;
//			for (int j = 0; j < estadoFinalFilas.length; j++) {
//				somadorColuna += estadoFinalFilas[j][i];
//			}
//			double mediaColuna = somadorColuna / (filas.capacidadeFila - 1);
//			System.out.println("Estado " + i + ": " + String.format("%.2f", mediaColuna));
//			System.out.println();
//		}
//	}

	private static void displayDadosRecebidos(ArrayList<Fila> filas) {
		System.out.println("===== DADOS RECEBIDOS ===== ");
		System.out.println("seeds: " + Arrays.toString(seedsCarregadas));
		System.out.println(" ");
		for (int i = 0; i < filas.size(); i++) {
			System.out.println("FILA " + (i + 1));

			if (i == 0) {
				System.out.println("tempoChegadaMinimo: " + filas.get(i).tempoChegadaMinimo);
				System.out.println("tempoChegadaMaximo: " + filas.get(i).tempoChegadaMaximo);
			}
			System.out.println("tempoAtendimentoMinimo: " + filas.get(i).tempoAtendimentoMinimo);
			System.out.println("tempoAtendimentoMaximo: " + filas.get(i).tempoAtendimentoMaximo);
			System.out.println("numeroServidores: " + filas.get(i).numeroServidores);
			System.out.println("capacidadeFila: " + (filas.get(i).capacidadeFila));

			System.out.println("==========================");
		}
	}

	private static ArrayList<Fila> lerDadosFila() throws FileNotFoundException {
		Scanner in = new Scanner(new FileReader("parametros.txt"));
		ArrayList<Fila> filas = new ArrayList<>();

		int nroFilas = 1;
		int cont = 0;
		int[] parametros = new int[6]; // primeira fila recebe 6 parametros
		Fila fila = new Fila();
		while (in.hasNextLine()) {
			String comando = in.nextLine();
			if (comando.contains(",")) { // linha que corresponde a seeds
				seedsCarregadas = comando.split(",");
				continue;
			} else if (comando.contains("F")) { // fim da fila prepara dados para próxima, se houver.
				cont = 0;
				parametros = new int[4]; // demais filas 4 parametros
				filas.add(fila);
				nroFilas++;
				fila = new Fila();
				continue;
			} else {
				parametros[cont] = Integer.parseInt(comando);
				if (nroFilas == 1) { // primeira fila sendo carregada
					fila.tempoChegadaMinimo = parametros[0];
					fila.tempoChegadaMaximo = parametros[1];
					fila.tempoAtendimentoMinimo = parametros[2];
					fila.tempoAtendimentoMaximo = parametros[3];
					fila.numeroServidores = parametros[4];
					fila.capacidadeFila = parametros[5];
					fila.primeiroClienteTempo = 2.5; // Fixo conforme enunciado do trabalho
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
