package sma.filas;

import java.util.ArrayList;
import java.util.List;

/**
 * Escalonador: responsavel por agendar os eventos solicitado pelo simulador e
 * gerar os números aleatórios
 */
public class Escalonador {

	public List<Evento> eventosAgendados = new ArrayList<>();
	public int qtdAleatorios = 0;
	public double seed;
	//M == 2^48
	public double M = 281474976710656L, a = 25214903917L, c = 11L;

	public Escalonador(double seed) {		
		this.seed = seed;
	}
	
	/**
	 * método para agendar o evento inicial do simulador
	 */
	public void agendaEventoInicial(double tempo, Tipo tipoEvento, double minimo, double maximo, Fila origem) {
		eventosAgendados.add(new Evento(tipoEvento, tempo, 0, origem, null));		
	}	

	/**
	 * método que agenda os eventos recebendo o tempo, tipo do evento, intervalos minimo e maximo de atendimento,
	 * fila origem e fila destino do evento agendado.
	 */
	public void agendaEvento(double tempo, Tipo tipoEvento, double minimo, double maximo, Fila origem, Fila destino) {
		double nroSorteado = (maximo - minimo) * geraNroAleatorio() + minimo;
		double tempoTotal = tempo + nroSorteado;
		String tempoTotalFormatted = String.format("%.4f", tempoTotal).replace(",", ".");
		eventosAgendados.add(new Evento(tipoEvento, Double.parseDouble(tempoTotalFormatted), nroSorteado, origem, destino));		
	}	

	/**
	 * Métódo que busca na lista de eventos agendados qual é o próximo a ser executado com base no tempo.
	 */
	public Evento executaProximoEvento() {
		Evento proximo = eventosAgendados.get(0);

		for (int i = 1; i < eventosAgendados.size(); i++) {
			if (eventosAgendados.get(i).tempo < proximo.tempo) {
				proximo = eventosAgendados.get(i);
			}
		}

		eventosAgendados.remove(proximo); //proximo evento a executar, remove do escalonador
		return proximo;
	}
	
	/**
	 * Gerador de números aleatórios com base na seed recebido, já convertendo para um ponto flutuante
	 * e cortando para quatro casas decimais o resultado.
	 */
	public double geraNroAleatorio() {
		seed = (a * seed + c) % M;
		Double valor = seed / M;
		String valorCortado = String.format("%.4f", valor).replace(",", ".");
		qtdAleatorios++;
		return Double.parseDouble(valorCortado);
	}	

}
