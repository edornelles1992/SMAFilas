package sma.filas;

public class Fila {

	//paramtros recebidos
	public int tempoChegadaMinimo;
	public int tempoChegadaMaximo;
	public int tempoAtendimentoMinimo;
	public int tempoAtendimentoMaximo;
	public int numeroServidores;
	public int capacidadeFila;
	public double tempo = 0;
	
	//parametros fixos
	public int clientesNaFila = 0;	
	public double primeiroClienteTempo = 3;
	
	public Escalonador escalonador = new Escalonador();

	public Fila() {

	}

	@Override
	public String toString() {
		return "Fila [tempoChegadaMinimo=" + tempoChegadaMinimo + ", tempoChegadaMaximo=" + tempoChegadaMaximo
				+ ", tempoAtendimentoMinimo=" + tempoAtendimentoMinimo + ", tempoAtendimentoMaximo="
				+ tempoAtendimentoMaximo + ", numeroServidores=" + numeroServidores + ", capacidadeFila="
				+ capacidadeFila + ", clientesNaFila=" + clientesNaFila + "]";
	}

	public void chegada(double tempoEvento) {
		tempo += tempoEvento;
		if (clientesNaFila < capacidadeFila) {
			clientesNaFila++;
			if (clientesNaFila <= 1) {
				//agenda SAIDA(T+rnd(tempoAtendimentoMinimo..tempoAtendimentoMaximo)
			}
		}
		//agenda CHEGADA(T+rnd(tempoChegadaMinimo..tempoChegadaMaximo))
	}

	public void saida(double tempoEvento) {
		tempo += tempoEvento;
		clientesNaFila--;
		if (clientesNaFila >= 1) {
			//agenda SAIDA(T+rnd(tempoAtendimentoMinimo..tempoAtendimentoMaximo)
		}
	}
}