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
	
	public void executaFila(int qtdAleatorios) {
		this.chegada(primeiroClienteTempo); //inicio do algoritmo
		tempo = primeiroClienteTempo;
		for (int i = 0; i < qtdAleatorios; i++) {
			this.chegada(tempo);
		}
	}

	public void chegada(double tempoEvento) {
		tempo += tempoEvento;
		if (clientesNaFila < capacidadeFila) {
			clientesNaFila++;
			if (clientesNaFila <= 1) {
				this.saida(escalonador.sorteio(tempo, Tipo.SAIDA, tempoAtendimentoMinimo, tempoAtendimentoMaximo));
			}
		}
		this.chegada(tempo + escalonador.sorteio(tempo, Tipo.CHEGADA, tempoChegadaMinimo, tempoChegadaMaximo));
	}

	public void saida(double tempoEvento) {
		tempo += tempoEvento;
		clientesNaFila--;
		if (clientesNaFila >= 1) {
			this.saida(escalonador.sorteio(tempo, Tipo.SAIDA, tempoAtendimentoMinimo, tempoAtendimentoMaximo));
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