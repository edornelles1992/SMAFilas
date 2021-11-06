package sma.filas;

import java.util.ArrayList;
import java.util.List;

public class Escalonador {

	public List<Evento> eventosAgendados = new ArrayList<>();
	public int qtdAleatorios = 0;
	public double seed;
	//M == 2^48
	public double M = 281474976710656L, a = 25214903917L, c = 11L;

	 Escalonador(double tempo, long seed) { // instancia o escalonador com o primeiro evento agendado no tempo recebido
		Evento eventoInicial = new Evento(Tipo.CHEGADA, tempo);
		eventosAgendados.add(eventoInicial);
		this.seed = seed;
	}

	public void agendaEvento(double tempo, Tipo tipoEvento, double minimo, double maximo) {
		double nroSorteado = (maximo - minimo) * geraNroAleatorio() + minimo;
		double tempoTotal = tempo + nroSorteado;
		String tempoTotalFormatted = String.format("%.4f", tempoTotal).replace(",", ".");
		eventosAgendados.add(new Evento(tipoEvento, Double.parseDouble(tempoTotalFormatted), nroSorteado));
		qtdAleatorios++;
	}	

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
	
	private double geraNroAleatorio() {
		seed = (a * seed + c) % M;
		Double valor = seed / M;
		String valorCortado = String.format("%.4f", valor).replace(",", ".");
		return Double.parseDouble(valorCortado);
	}
}
