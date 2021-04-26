package sma.filas;

import java.util.ArrayList;
import java.util.List;

public class Simulador {

	public double tempo = 0;
	public int qtdAleatorios = 0;
	public List<Evento> eventosOcorridos = new ArrayList<>();
	// controle dos tempos dos estados da fila

	public Escalonador escalonador;

	public Simulador(ArrayList<Fila> filas, long seed, int qtdAleatorios) {
		escalonador = new Escalonador(filas.get(0).primeiroClienteTempo, seed);
		this.executa(filas, qtdAleatorios);
	}

	public ArrayList<Fila> executa(ArrayList<Fila> filas, int qtdAleatorios) {
		this.qtdAleatorios = qtdAleatorios;

		for (Fila fila : filas) {
			fila.estado = new double[fila.capacidadeFila + 1]; // +1 para o estado 0 da fila!
		}

		while (escalonador.qtdAleatorios < qtdAleatorios) { // executa enquanto houver nro aleatórios
			Evento evento = escalonador.executaProximoEvento();
			this.eventosOcorridos.add(evento);
			if (evento.tipo.equals(Tipo.CHEGADA)) {
				this.chegada(filas.get(0), filas.get(1),evento.sorteio); 
			} else if (evento.tipo.equals(Tipo.PASSAGEM)) {
				this.passagem(filas.get(0), filas.get(1), evento.sorteio);
			} else { //SAIDA
				this.saida(filas.get(1), filas.get(1), evento.sorteio); 
			}
		}
		informaResultadoExecucao(filas);
		return filas;
	}
	
	private void contabilizaTempos(Fila filaOrigem, Fila filaDestino, Tipo tipo, double tempoSorteio) {
		double tempoAnterior = new Double(tempo);
		if (tipo == Tipo.CHEGADA) {			
			filaOrigem.estado[filaOrigem.clientesNaFila] = tempoSorteio == 0 ? filaOrigem.primeiroClienteTempo
					: (tempoAnterior - tempoSorteio) + filaOrigem.estado[filaOrigem.clientesNaFila];
		} else if (tipo == Tipo.PASSAGEM) {
			System.out.println("CONTABILIZANDO PASSAGEM.....");
		} else { //SAIDA
			filaDestino.estado[filaDestino.clientesNaFila - 1] = (tempoAnterior - tempoSorteio) + filaDestino.estado[filaDestino.clientesNaFila - 1];
		}
	}

	private void passagem(Fila filaOrigem, Fila filaDestino, double tempoSorteio) {
		tempo +=  tempoSorteio;
		this.contabilizaTempos(filaOrigem, filaDestino, Tipo.PASSAGEM, tempoSorteio);
		filaOrigem.clientesNaFila--;
		if (filaOrigem.clientesNaFila >= filaOrigem.numeroServidores) { //passar da fila origem para a destino
			escalonador.agendaEvento(tempo, Tipo.PASSAGEM, filaOrigem.tempoAtendimentoMinimo,
					filaOrigem.tempoAtendimentoMaximo);
		}
		if (filaDestino.clientesNaFila < filaDestino.capacidadeFila) {
			filaDestino.clientesNaFila++;
			if (filaDestino.clientesNaFila <= filaDestino.numeroServidores) {
				escalonador.agendaEvento(tempo, Tipo.SAIDA, filaDestino.tempoAtendimentoMinimo,
						filaDestino.tempoAtendimentoMaximo);
			}
		}
	}

	public void chegada(Fila filaOrigem, Fila filaDestino, double tempoSorteio) {
		tempo += tempoSorteio == 0 ? filaOrigem.primeiroClienteTempo : tempoSorteio;
		contabilizaTempos(filaOrigem, filaDestino, Tipo.CHEGADA , tempoSorteio);
		if (filaOrigem.capacidadeFila == -1 || filaOrigem.clientesNaFila < filaOrigem.capacidadeFila) {
			filaOrigem.clientesNaFila++;
			if (filaOrigem.clientesNaFila <= filaOrigem.numeroServidores) { // chegou e se encontra de frente pra um servidor?
																// agenda PASSAGEM
				escalonador.agendaEvento(tempo, Tipo.PASSAGEM, filaOrigem.tempoAtendimentoMinimo,
						filaOrigem.tempoAtendimentoMaximo);
			}
		}
		escalonador.agendaEvento(tempo, Tipo.CHEGADA, filaOrigem.tempoChegadaMinimo, filaOrigem.tempoChegadaMaximo);
	}

	public void saida(Fila filaOrigem, Fila filaDestino, double tempoSorteio) {
		tempo += tempoSorteio;
		contabilizaTempos(filaOrigem, filaDestino, Tipo.CHEGADA , tempoSorteio);
		filaDestino.clientesNaFila--;
		if (filaDestino.clientesNaFila >= filaDestino.numeroServidores) {
			escalonador.agendaEvento(tempo, Tipo.SAIDA, filaDestino.tempoAtendimentoMinimo, filaDestino.tempoAtendimentoMaximo);
		}
	}

	public void informaResultadoExecucao(ArrayList<Fila> filas) {
		System.out.println("=============RESULTADOS===============");
		System.out.println("Gerou " + qtdAleatorios + " Aleatórios!! FIM!!!");
		
		double tempoTotalSimulacao = 0;
		for (Fila f : filas) {
			double tempototal = 0;
			for (int i = 0; i < f.estado.length; i++) {
				tempototal += f.estado[i];				
			}
			f.tempoTotal = tempototal;
			tempoTotalSimulacao+= tempototal;
		}

		for (int x = 0; x < filas.size(); x++) {
			System.out.println();
			System.out.println("Fila " + (x + 1));
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

}
