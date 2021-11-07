package sma.filas;

import java.util.ArrayList;
import java.util.List;

public class Fila implements Cloneable {

	public double numeroFila;
	public double primeiroClienteTempo = -1;
	public double tempoChegadaMinimo = -1;
	public double tempoChegadaMaximo = -1;

	// parametros em todas as filas.
	public double tempoAtendimentoMinimo;
	public double tempoAtendimentoMaximo;
	public int numeroServidores;
	public int capacidadeFila;
	public int clientesNaFila = 0;
	public ArrayList<Double> estado = null;
	public double tempoTotal;
	public int perdas = 0;
	public List<Roteamento> roteamentos = new ArrayList<>();

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
		this.numeroFila = fila.numeroFila;
	}

	@Override
	public String toString() {
		return "Fila [numeroFila=" + numeroFila + ", primeiroClienteTempo=" + primeiroClienteTempo
				+ ", tempoChegadaMinimo=" + tempoChegadaMinimo + ", tempoChegadaMaximo=" + tempoChegadaMaximo
				+ ", tempoAtendimentoMinimo=" + tempoAtendimentoMinimo + ", tempoAtendimentoMaximo="
				+ tempoAtendimentoMaximo + ", numeroServidores=" + numeroServidores + ", capacidadeFila="
				+ capacidadeFila + ", clientesNaFila=" + clientesNaFila + ", estado=" + estado + ", tempoTotal="
				+ tempoTotal + ", perdas=" + perdas + "]";
	}

}