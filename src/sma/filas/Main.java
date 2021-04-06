package sma.filas;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("========= SIMULADOR DE FILA SIMPLES ==========");
		Fila fila = lerDadosFila();
		fila.executaFila(100000);
	}

	private static Fila lerDadosFila() throws FileNotFoundException {
		Scanner in = new Scanner(new FileReader("parametros.txt"));
		
		int cont = 0;
		int[] comandos = new int[6];
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

		return fila;
	}

}
