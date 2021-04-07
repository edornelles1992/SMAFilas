package sma.filas;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {

	private static long seedCarregada;
	
	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("========= SIMULADOR DE FILA SIMPLES ==========");
		Fila fila = lerDadosFila();
		displayDadosRecebidos(fila);
		fila.executaFila(100000, seedCarregada); //TODO: trocar para 100mil quando tiver ok
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
		System.out.println("seed: "+ seedCarregada);
		System.out.println("==========================");
		System.out.println();
	}

	private static Fila lerDadosFila() throws FileNotFoundException {
		Scanner in = new Scanner(new FileReader("parametros.txt"));
		
		int cont = 0;
		int[] comandos = new int[7];
		Fila fila = new Fila();
		while (in.hasNextLine()) {
			String comando = in.nextLine();
			comandos[cont] = Integer.parseInt(comando);	
			cont++;
		}
		
		fila.tempoChegadaMinimo = comandos[0];
		fila.tempoChegadaMaximo = comandos[1];
		fila.tempoAtendimentoMinimo = comandos[2];
		fila.tempoAtendimentoMaximo = comandos[3];
		fila.numeroServidores = comandos[4];
		fila.capacidadeFila = comandos[5];
		seedCarregada = (long) comandos[6];
		
		return fila;
	}

}
