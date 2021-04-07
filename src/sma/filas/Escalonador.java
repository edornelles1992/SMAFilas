package sma.filas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Escalonador {

	public List<Evento> eventosAgendados = new ArrayList<>();
	public Random random = new Random();
	public int qtdAleatorios = 0;
	public long seed;
	
	 Escalonador(double tempo, long seed) { // instancia o escalonador com o primeiro evento agendado no tempo recebido
		 random = new Random(seed);
		Evento eventoInicial = new Evento(Tipo.CHEGADA, tempo);
		eventosAgendados.add(eventoInicial);
	}

	public void agendaEvento(double tempo, Tipo tipoEvento, int minimo, int maximo) {
		double nroSorteado = (maximo - minimo) * random.nextDouble() + minimo;
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
}
