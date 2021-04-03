package sma.filas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Escalonador {

	public List<Evento> eventos = new ArrayList<>();
	public Random random = new Random();

	public double sorteio(double tempo, Tipo tipoEvento, int minimo, int maximo) {
		double nroSorteado = (maximo - minimo) * random.nextDouble() + minimo;
		double tempoTotal = tempo + nroSorteado;
		eventos.add(new Evento(tipoEvento, tempoTotal, nroSorteado));
		return tempoTotal;
	}

	public Evento proximoEvento() {
		Evento proximo = null;
		for (Evento evento : eventos) {
			if (proximo == null)
				proximo = evento;
			else {
				if (evento.tempo < proximo.tempo)
					proximo = evento;
			}
		}
		return proximo;
	}
}
