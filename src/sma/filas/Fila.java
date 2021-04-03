package sma.filas;

import java.util.ArrayList;
import java.util.List;

public class Fila {

	//paramtros recebidos
	public int tempoChegadaMinimo;
	public int tempoChegadaMaximo;
	public int tempoAtendimentoMinimo;
	public int tempoAtendimentoMaximo;
	public int numeroServidores;
	public int capacidadeFila;
	public double tempo = 0;
	public int qtdAleatorios = 0;
	public List<Evento> eventosOcorridos = new ArrayList<>();
	public Escalonador escalonador;
	
	//parametros fixos
	public int clientesNaFila = 0;	
	public double primeiroClienteTempo = 3;
		
	public void executaFila(int qtdAleatorios) {
		escalonador = new Escalonador(primeiroClienteTempo);
		this.qtdAleatorios = qtdAleatorios;


		while (true) {
			Evento evento = escalonador.executaProximoEvento();
			this.eventosOcorridos.add(evento);	
			if (evento.tipo.equals(Tipo.CHEGADA)) {
				this.chegada(tempo + evento.tempo); 
			} else {
				this.saida(tempo + evento.tempo); 
			}			
		}
	}

	public void chegada(double tempoEvento) {
		this.validaFimDosAleatorios();
		tempo += tempoEvento;
		if (clientesNaFila < capacidadeFila) {
			clientesNaFila++;
			if (clientesNaFila <= numeroServidores) { //chegou e se encontra de frente pra um servidor? agenda saída
				escalonador.agendaEvento(tempo, Tipo.SAIDA, tempoAtendimentoMinimo, tempoAtendimentoMaximo);
			}
		}
	//	this.chegada(tempo + escalonador.agendaEvento(tempo, Tipo.CHEGADA, tempoChegadaMinimo, tempoChegadaMaximo));
		escalonador.agendaEvento(tempo, Tipo.CHEGADA, tempoChegadaMinimo, tempoChegadaMaximo);
	}

	public void saida(double tempoEvento) {
		this.validaFimDosAleatorios();
		tempo += tempoEvento;
		clientesNaFila--;
		if (clientesNaFila >= 1) {//TODO: 1 ou numeroServidores??
			escalonador.agendaEvento(tempo, Tipo.SAIDA, tempoAtendimentoMinimo, tempoAtendimentoMaximo);
		}
	}
	
	public void validaFimDosAleatorios() {
		if (qtdAleatorios == escalonador.qtdAleatorios) {
			System.out.println("Gerou "+ qtdAleatorios + " Aleatórios!! FIM!!!");
			System.exit(0);
		}
	}
	

	@Override
	public String toString() {
		return "Fila [tempoChegadaMinimo=" + tempoChegadaMinimo + ", tempoChegadaMaximo=" + tempoChegadaMaximo
				+ ", tempoAtendimentoMinimo=" + tempoAtendimentoMinimo + ", tempoAtendimentoMaximo="
				+ tempoAtendimentoMaximo + ", numeroServidores=" + numeroServidores + ", capacidadeFila="
				+ capacidadeFila + ", clientesNaFila=" + clientesNaFila + "]";
	}
}