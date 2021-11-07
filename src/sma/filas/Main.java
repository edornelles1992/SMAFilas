package sma.filas;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
/**
 * Classe main
 * responsavel por ler  e formatar os dados da fila recebido por txt
 * e solicitar a execução do simulador com os dados recebidos
 */
public class Main {

	private static String[] seedsCarregadas;
	private static int qtdSimulacoes;
	private static int qtdAleatorios = 100000;
	private static int totalFilas = 0;
	private static double primeiroClienteTempo;
	private static ArrayList<Roteamento> roteamentos = new ArrayList<>();

	/**
	 * Método main, solicitar os dados do arquivo e repassada as filas retornadas
	 * para o simulador executar.
	 */
	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("========= SIMULADOR DE FILAS==========");
		ArrayList<Fila> filas = lerDadosFila();
		executaSimulador(filas);
		System.out.println("==============FIM===================");
	}

	/**
	 * Método que inicia o simulador recebendo as filas.
	 * Informa os dados recebidos no console e invoca o inicio da simulação.
	 * O laço do método é baseado na quantidade de seeds informadas, caso seja somente uma seed,
	 * somente havera uma iteração do lado para simulação.
	 * Ao final a(s) fila(s) processada é gravada e repassada para o método que calcula as medias das
	 * execuções.
	 */
	private static void executaSimulador(ArrayList<Fila> filas) throws FileNotFoundException {
		displayDadosRecebidos(filas);
		ArrayList<Fila> filasProcessadas = new ArrayList<>();
		System.out.println();
		System.out.println("EXECUTANDO SIMULAÇÕES...");
		for (int i = 0; i < qtdSimulacoes; i++) {
			System.out.println();
			System.out.print("========= EXECUÇÃO NUMERO: " + (i + 1) + " ======== SEED: " + seedsCarregadas[i]);
			Simulador simulador = new Simulador(filas, Integer.parseInt(seedsCarregadas[i]), qtdAleatorios, roteamentos);
			simulador.executa(filas, qtdAleatorios);
			// informaResultadoExecucao(filas, i);
			for (Fila f : filas)
				filasProcessadas.add(new Fila(f));

			System.out.print(" ...OK");
		}

		calculaMediaExecucoes(filasProcessadas);
	}

	/**
	 * Pega todas as execuções (com cada seed) e faz uma media dos resultados de
	 * cada fila.
	 */
	private static void calculaMediaExecucoes(ArrayList<Fila> filasProcessadas) {
		System.out.println();
		System.out.println();
		System.out.println("SIMULAÇÕES ENCERRADAS...");
		System.out.println();
		System.out.println("====MEDIA DAS EXECUÇÕES========");
		System.out.println();

		Map<Integer, ArrayList<Fila>> filasAgrupadas = new HashMap<>();

		for (int i = 0; i < totalFilas; i++) {
			filasAgrupadas.put(i + 1, new ArrayList<Fila>());
		}

		for (Fila f : filasProcessadas) {
			filasAgrupadas.get((int) f.numeroFila).add(f);
		}

		double totalTempoMedio = 0.0;
		for (int x = 1; x <= totalFilas; x++) {
			ArrayList<Fila> filas = filasAgrupadas.get(x);
			double tempoMedio = 0.0;

			System.out.println("Fila F" + x);
			ArrayList<ArrayList<Double>> estadosFinais = new ArrayList<>();
			for (int i = 0; i < filas.size(); i++) {
				estadosFinais.add(filas.get(i).estado);
			}

			int qtdEstadosMax = filas.get(0).estado.size();
			
			double[] totalMediaEstados = new double[filas.get(0).capacidadeFila == 0 ? qtdEstadosMax + 1 : filas.get(0).capacidadeFila + 1];

			for (int i = 0; i < qtdEstadosMax; i++) {
				double somador = 0.0;
				ArrayList<Double> estado = new ArrayList<>();

				// soma valores de um estado de todas as filas
				int j;
				for (j = 0; j < estadosFinais.size(); j++) {
					estado = estadosFinais.get(j);
					somador += estado.get(i);
				}
				totalMediaEstados[i] = somador / j;
			}

			// total de tempo medio

			for (int i = 0; i < totalMediaEstados.length; i++) {
				tempoMedio += totalMediaEstados[i];
			}
			totalTempoMedio += tempoMedio;

			// calcula probabilidade de cada estado da fila.
			for (int i = 0; i < totalMediaEstados.length; i++) {
				String probabilidade = String.format("%.2f", (totalMediaEstados[i] * 100) / tempoMedio);
				System.out.println("Estado " + i + ": " + String.format("%.2f", totalMediaEstados[i])
						+ "  |   Probabilidade: " + probabilidade + "%");
			}
			System.out.println();
			// System.out.println("Número de Perdas: " + filas);
		}

		System.out.println();
		System.out.println("Média de tempo das execuções: " + String.format("%.2f", totalTempoMedio));
		System.out.println();
	}

	/**
	 * Display no console para mostrar os dados recebidos via arquivo texto.
	 */
	private static void displayDadosRecebidos(ArrayList<Fila> filas) {
		System.out.println();
		System.out.println("===== DADOS RECEBIDOS ===== ");
		System.out.println("seeds: " + Arrays.toString(seedsCarregadas));
		System.out.println("Primeiro cliente no tempo: " + primeiroClienteTempo);
		System.out.println("NRO ALEATORIOS POR SIMULAÇÃO: " + qtdAleatorios);
		System.out.println();
		System.out.println("============FILAS===============");
		System.out.println();
		for (int i = 0; i < filas.size(); i++) {
			System.out.print("FILA " + (i + 1));

			System.out.println(" (G/G/" + filas.get(i).numeroServidores + (filas.get(i).capacidadeFila == 0 ? ")" : "/" + filas.get(i).capacidadeFila + ")"));
			if (i == 0) {
				System.out.println(
						"CHEGADA: " + filas.get(i).tempoChegadaMinimo + " até " + filas.get(i).tempoChegadaMaximo);
			}
			System.out.println("TEMPO ATENDIMENTO: " + filas.get(i).tempoAtendimentoMinimo + " até "
					+ filas.get(i).tempoAtendimentoMaximo);
			System.out.println("==========================");
		}
	}

	/**
	 * Método responsavel por ler o arquivo parametros.txt e interpretar os dados.
	 * Itera por cada linha do arquivo e grava os dados de cada fila descrita
	 * e posteriores roteamentos, conforme descrito no arquito de instruções.txt
	 */
	private static ArrayList<Fila> lerDadosFila() throws FileNotFoundException {
		Scanner in = new Scanner(new FileReader("parametros.txt"));
		ArrayList<Fila> filas = new ArrayList<>();

		int nroFilas = 1;
		int cont = 0;
		double[] parametros = new double[8]; // primeira fila recebe 6 parametros
		Fila fila = new Fila();
		while (in.hasNextLine()) {
			String comando = in.nextLine();
			if (comando.contains("SEEDS")) { // linha que corresponde a seeds
				seedsCarregadas = comando.split(":")[1].split(",");
				qtdSimulacoes = seedsCarregadas.length;
				continue;
			} else if (comando.contains("FIM_FILA")) { // fim da fila prepara dados para prï¿½xima, se houver.
				cont = 0;
				parametros = new double[5]; // demais filas 4 parametros
				filas.add(fila);
				fila = new Fila();
				nroFilas++;
				continue;
			} else {

				if (comando.contains("FILA"))
					parametros[0] = Double.parseDouble(comando.split(":")[1]);
				else if (comando.contains("PRIMEIRO_CLIENTE_TEMPO"))
					parametros[1] = Double.parseDouble(comando.split(":")[1]);
				else if (comando.contains("INICIO_ROTEAMENTOS")) {
					populaRoteamentos(in);
					break;
				} else
					parametros[cont] = Double.parseDouble(comando);

				if (nroFilas == 1) { // primeira fila sendo carregad
					fila.numeroFila = parametros[0];
					fila.primeiroClienteTempo = parametros[1]; // ***** Fixo conforme enunciado do trabalho
					primeiroClienteTempo = fila.primeiroClienteTempo;
					fila.tempoChegadaMinimo = parametros[2];
					fila.tempoChegadaMaximo = parametros[3];
					fila.tempoAtendimentoMinimo = parametros[4];
					fila.tempoAtendimentoMaximo = parametros[5];
					fila.numeroServidores = (int) parametros[6];
					fila.capacidadeFila = (int) parametros[7];
				} else { // demais filas sem precisar o tempo de chegada
					fila.numeroFila = parametros[0];
					fila.tempoAtendimentoMinimo = parametros[1];
					fila.tempoAtendimentoMaximo = parametros[2];
					fila.numeroServidores = (int) parametros[3];
					fila.capacidadeFila = (int) parametros[4];
				}
				cont++;
			}
		}

		totalFilas = nroFilas - 1;
		return filas;
	}

	/**
	 * Método interno auxiliar ao lerDadosFila, organiza e grava os dados dos roteamentos.
	 */
	private static void populaRoteamentos(Scanner in) {
		while (in.hasNextLine()) {
			String comando = in.nextLine();
			if (comando.contains("FIM_ROTEAMENTO"))
				break;
			else {
				String[] cmd = comando.split(",");
				roteamentos.add(new Roteamento(
						Double.parseDouble(cmd[0]),
						Double.parseDouble(cmd[1]),
						Double.parseDouble(cmd[2])
						));
			}
		}

	}

}
