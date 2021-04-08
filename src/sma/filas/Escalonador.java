package sma.filas;

import java.util.ArrayList;
import java.util.List;

public class Escalonador {

	public List<Evento> eventosAgendados = new ArrayList<>();
	public int qtdAleatorios = 0;
	public double seed;
	public double mod = 2^48, a = 25214903917.0, c = 11;
	
	 Escalonador(double tempo, long seed) { // instancia o escalonador com o primeiro evento agendado no tempo recebido
		Evento eventoInicial = new Evento(Tipo.CHEGADA, tempo);
		eventosAgendados.add(eventoInicial);
		this.seed = seed;
	}

	public void agendaEvento(double tempo, Tipo tipoEvento, int minimo, int maximo) {
		seed = geraNroAleatorio();
		double nroSorteado = (maximo - minimo) * seed + minimo;
		double tempoTotal = tempo + nroSorteado;
		eventosAgendados.add(new Evento(tipoEvento, tempoTotal, nroSorteado));
		qtdAleatorios++;
	}

	public Evento executaProximoEvento() {
		Evento proximo = eventosAgendados.get(0);

		for (int i = 1; i < eventosAgendados.size(); i++) {
			if (eventosAgendados.get(i).tempo < proximo.tempo) {
				proximo = eventosAgendados.get(i);
			}
		}

		eventosAgendados.remove(proximo); //ja foi passado para execução, remove do escalonador
		return proximo;
	}
	
	private double geraNroAleatorio() {
		return (a * seed + a) % mod ;
	}
}
