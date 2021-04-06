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
	//controle dos tempos dos estados da fila
	public double[] estadoFila = null;
	public Escalonador escalonador;
	
	//parametros fixos
	public int clientesNaFila = 0;	
	public double primeiroClienteTempo = 3;
		
	public void executaFila(int qtdAleatorios) {
		escalonador = new Escalonador(primeiroClienteTempo);
		this.qtdAleatorios = qtdAleatorios;
		estadoFila = new double[capacidadeFila];
//		estadoFila[0] = primeiroClienteTempo;
//		clientesNaFila++;

		while (true) {
			Evento evento = escalonador.executaProximoEvento();
			this.eventosOcorridos.add(evento);			
			if (evento.tipo.equals(Tipo.CHEGADA)) {
				this.chegada(evento.sorteio); 
			} else {
				this.saida(evento.sorteio); 
			}				
		}
	}

	public void chegada(double tempoSorteio) {
		this.validaFimDosAleatorios();
		tempo += tempoSorteio ==  0 ? primeiroClienteTempo : tempoSorteio;
		if (capacidadeFila == -1 || clientesNaFila < capacidadeFila) {	
			estadoFila[clientesNaFila] = tempoSorteio == 0 ? primeiroClienteTempo : tempoSorteio;
			clientesNaFila++;
			if (clientesNaFila <= numeroServidores) { //chegou e se encontra de frente pra um servidor? agenda saída
				escalonador.agendaEvento(tempo, Tipo.SAIDA, tempoAtendimentoMinimo, tempoAtendimentoMaximo);
			}
		}
		escalonador.agendaEvento(tempo, Tipo.CHEGADA, tempoChegadaMinimo, tempoChegadaMaximo);
	}

	public void saida(double tempoSorteio) {
		this.validaFimDosAleatorios();
		double tempoAnterior = new Double(tempo);	
		tempo += tempoSorteio;
		estadoFila[clientesNaFila - 1] =  (tempoAnterior - tempoSorteio) + estadoFila[clientesNaFila - 1];
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