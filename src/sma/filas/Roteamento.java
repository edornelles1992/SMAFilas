package sma.filas;

public class Roteamento {
	public double FilaOrigem;
	public double FilaDestino;
	public double probabilidade;

	public Roteamento(double filaOrigem, double filaDestino, double probabilidade) {
		super();
		this.FilaOrigem = filaOrigem;
		this.FilaDestino = filaDestino;
		this.probabilidade = probabilidade;
	}

	@Override
	public String toString() {
		return "Roteamento [FilaOrigem=" + FilaOrigem + ", FilaDestino=" + FilaDestino + ", probabilidade="
				+ probabilidade + "]";
	}

}
