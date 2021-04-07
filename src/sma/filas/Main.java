package sma.filas;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

	private static String[] seedsCarregadas;
	private static int qtdSimulacoes;
	
	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("========= SIMULADOR DE FILA SIMPLES ==========");
		Fila fila = lerDadosFila();
		double[][] estadoFinalFilas = new double[qtdSimulacoes][fila.capacidadeFila];
		for (int i = 0; i < qtdSimulacoes; i++) {
			Fila filaParaExecutar = lerDadosFila();
			if (i == 0) displayDadosRecebidos(fila);
			System.out.println();
			System.out.println("========= EXECUÇÃO NUMERO: "+ (i + 1) + "========");
			double[] estadoFinalFinal = filaParaExecutar.executaFila(100000, Long.parseLong(seedsCarregadas[i]));
			//salvar os valores dos estados final da fila para calcular a media posteriormente
			for (int j = 0; j < fila.capacidadeFila; j++) {
				estadoFinalFilas[i][j] = estadoFinalFinal[j];
			}
		}

		calculaMedia(estadoFinalFilas, fila);
		System.out.println("==============FIM===================");		
	}
	
	private static void calculaMedia(double[][] estadoFinalFilas, Fila fila) {
		System.out.println();
		System.out.println("====MEDIA DAS EXECUÇÕES========");
		System.out.println();
		for (int i = 0; i < estadoFinalFilas.length; i++) {
			double somadorColuna = 0;
			for (int j = 0; j < estadoFinalFilas.length; j++) {
				somadorColuna += estadoFinalFilas[j][i];
			}
			double mediaColuna = somadorColuna / (fila.capacidadeFila - 1);
			System.out.println("Estado " + i + ": " + String.format("%.2f", mediaColuna));
			System.out.println();
		}
	}

	private static void displayDadosRecebidos(Fila fila) {
		System.out.println();
		System.out.println("===== DADOS RECEBIDOS ===== ");
		System.out.println("tempoChegadaMinimo: " + fila.tempoChegadaMinimo);
		System.out.println("tempoChegadaMaximo: " + fila.tempoChegadaMaximo);
		System.out.println("tempoAtendimentoMinimo: " + fila.tempoAtendimentoMinimo);
		System.out.println("tempoAtendimentoMaximo: " + fila.tempoAtendimentoMaximo);
		System.out.println("numeroServidores: " + fila.numeroServidores);
		System.out.println("capacidadeFila: " + fila.capacidadeFila);
		System.out.println("seed: "+ Arrays.toString(seedsCarregadas));
		System.out.println("==========================");
		System.out.println();
	}

	private static Fila lerDadosFila() throws FileNotFoundException {
		Scanner in = new Scanner(new FileReader("parametros.txt"));
		
		int cont = 0;
		int[] comandos = new int[8];
		Fila fila = new Fila();
		while (in.hasNextLine()) {
			String comando = in.nextLine();
			if (cont == 6) { //lista de seeds
				seedsCarregadas = comando.split(",");
			} else {
			 comandos[cont] = Integer.parseInt(comando);				
			}
			cont++;
		}
		
		fila.tempoChegadaMinimo = comandos[0];
		fila.tempoChegadaMaximo = comandos[1];
		fila.tempoAtendimentoMinimo = comandos[2];
		fila.tempoAtendimentoMaximo = comandos[3];
		fila.numeroServidores = comandos[4];
		fila.capacidadeFila = comandos[5] + 1;
		qtdSimulacoes = (int) comandos[7];
		
		return fila;
	}

}
