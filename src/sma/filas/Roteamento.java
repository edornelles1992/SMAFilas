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

	public double getFilaOrigem() {
		return FilaOrigem;
	}

	public void setFilaOrigem(double filaOrigem) {
		FilaOrigem = filaOrigem;
	}

	public double getFilaDestino() {
		return FilaDestino;
	}

	public void setFilaDestino(double filaDestino) {
		FilaDestino = filaDestino;
	}

	public double getProbabilidade() {
		return probabilidade;
	}

	public void setProbabilidade(double probabilidade) {
		this.probabilidade = probabilidade;
	}

}
