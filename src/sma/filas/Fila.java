package sma.filas;

public class Fila {

	//paramtros recebidos

	//parametros primeira fila
	public double primeiroClienteTempo = -1; 
	public int tempoChegadaMinimo = -1; 
	public int tempoChegadaMaximo = -1;
	
	//parametros em todas as filas.
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