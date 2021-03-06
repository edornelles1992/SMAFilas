package sma.filas;

import java.util.ArrayList;
import java.util.List;

public class Simulador {

	public double tempoAtual = 0;
	public double tempoUltimoEvento = 0;
	public int qtdAleatorios = 0;
	public List<Evento> eventosOcorridos = new ArrayList<>();
	public int qtdFilas = 0;
	public Escalonador escalonador;

	public Simulador(ArrayList<Fila> filas, long seed, int qtdAleatorios) {
		escalonador = new Escalonador(filas.get(0).primeiroClienteTempo, seed);
	}

	public ArrayList<Fila> executa(ArrayList<Fila> filas, int qtdAleatorios) {
		this.qtdFilas = filas.size();
		this.qtdAleatorios = qtdAleatorios;

		for (Fila fila : filas) {
			fila.clientesNaFila = 0; //zera a fila;
			fila.estado = new double[fila.capacidadeFila + 1]; // +1 para o estado 0 da fila!
		}

		while (escalonador.qtdAleatorios < qtdAleatorios) { // executa enquanto houver nro aleatórios
			Evento evento = escalonador.executaProximoEvento();
			this.eventosOcorridos.add(evento);
			if (evento.tipo.equals(Tipo.CHEGADA)) {
				this.chegada(filas.get(0), evento.sorteio);
			} else if (evento.tipo.equals(Tipo.PASSAGEM)) {
				this.passagem(filas.get(0), filas.get(1), evento.sorteio);
			} else { // SAIDA
				this.saida(filas.get(0), evento.sorteio);
			}
		}

		return filas;
	}

	private void atualizaTempos(Fila filaOrigem, Fila filaDestino, Tipo tipo, double tempoSorteio) {
 			filaOrigem.estado[filaOrigem.clientesNaFila] += tempoAtual - tempoUltimoEvento;
	}

	private void passagem(Fila filaOrigem, Fila filaDestino, double tempoSorteio) {
		tempoUltimoEvento = tempoAtual;
		tempoAtual += tempoSorteio;
		this.atualizaTempos(filaOrigem, filaDestino, Tipo.PASSAGEM, tempoSorteio);
		filaOrigem.clientesNaFila--;
		if (filaOrigem.clientesNaFila >= filaOrigem.numeroServidores) { // passar da fila origem para a destino
			escalonador.agendaEvento(tempoAtual, Tipo.PASSAGEM, filaOrigem.tempoAtendimentoMinimo,
					filaOrigem.tempoAtendimentoMaximo);
		}
		if (filaDestino.clientesNaFila < filaDestino.capacidadeFila) {
			filaDestino.clientesNaFila++;
			if (filaDestino.clientesNaFila <= filaDestino.numeroServidores) {
				escalonador.agendaEvento(tempoAtual, Tipo.SAIDA, filaDestino.tempoAtendimentoMinimo,
						filaDestino.tempoAtendimentoMaximo);
			}
		}
	}

	public void chegada(Fila filaOrigem, double tempoSorteio) {
		tempoUltimoEvento = tempoAtual;
		tempoAtual += tempoSorteio == 0 ? filaOrigem.primeiroClienteTempo : tempoSorteio;
		atualizaTempos(filaOrigem, null, Tipo.CHEGADA, tempoSorteio);
		if (filaOrigem.capacidadeFila == -1 || filaOrigem.clientesNaFila < filaOrigem.capacidadeFila) {
			filaOrigem.clientesNaFila++;
			if (filaOrigem.clientesNaFila <= filaOrigem.numeroServidores) { // chegou e se encontra de frente pra um
				// agenda saida pois é uma fila simples...
				if (qtdFilas <= 1) { //Rede de 1 fila simples...
					escalonador.agendaEvento(tempoAtual, Tipo.SAIDA, filaOrigem.tempoAtendimentoMinimo,
							filaOrigem.tempoAtendimentoMaximo);
				} else { //evento de passagem de fila para modelos em tandem..
					escalonador.agendaEvento(tempoAtual, Tipo.PASSAGEM, filaOrigem.tempoAtendimentoMinimo,
							filaOrigem.tempoAtendimentoMaximo);
				}
			}
		}
		escalonador.agendaEvento(tempoAtual, Tipo.CHEGADA, filaOrigem.tempoChegadaMinimo, filaOrigem.tempoChegadaMaximo);
	}

	public void saida(Fila filaOrigem, double tempoSorteio) {
		tempoUltimoEvento = tempoAtual;
		tempoAtual += tempoSorteio;
		atualizaTempos(filaOrigem, null, Tipo.SAIDA, tempoSorteio);
		filaOrigem.clientesNaFila--;
		if (filaOrigem.clientesNaFila >= filaOrigem.numeroServidores) {
			escalonador.agendaEvento(tempoAtual, Tipo.SAIDA, filaOrigem.tempoAtendimentoMinimo,
					filaOrigem.tempoAtendimentoMaximo);
		}
	}

}