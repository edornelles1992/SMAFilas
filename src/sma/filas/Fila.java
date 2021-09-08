package sma.filas;

public class Fila implements Cloneable {

	// paramtros recebidos

	public Fila() {
		super();
	}

	public Fila(Fila fila) {
		super();
		this.primeiroClienteTempo = fila.primeiroClienteTempo;
		this.tempoChegadaMinimo = fila.tempoChegadaMinimo;
		this.tempoChegadaMaximo = fila.tempoChegadaMaximo;
		this.tempoAtendimentoMinimo = fila.tempoAtendimentoMinimo;
		this.tempoAtendimentoMaximo = fila.tempoAtendimentoMaximo;
		this.numeroServidores = fila.numeroServidores;
		this.capacidadeFila = fila.capacidadeFila;
		this.clientesNaFila = fila.clientesNaFila;
		this.estado = fila.estado;
		this.tempoTotal = fila.tempoTotal;
	}

	// parametros primeira fila
	public double primeiroClienteTempo = -1;
	public int tempoChegadaMinimo = -1;
	public int tempoChegadaMaximo = -1;

	// parametros em todas as filas.
	public int tempoAtendimentoMinimo;
	public int tempoAtendimentoMaximo;
	public int numeroServidores;
	public int capacidadeFila;
	public int clientesNaFila = 0;
	public double[] estado = null;
	public double tempoTotal;

	@Override
	public String toString() {
		return "Fila [tempoChegadaMinimo=" + tempoChegadaMinimo + ", tempoChegadaMaximo=" + tempoChegadaMaximo
				+ ", tempoAtendimentoMinimo=" + tempoAtendimentoMinimo + ", tempoAtendimentoMaximo="
				+ tempoAtendimentoMaximo + ", numeroServidores=" + numeroServidores + ", capacidadeFila="
				+ capacidadeFila + ", clientesNaFila=" + clientesNaFila + "]";
	}
}