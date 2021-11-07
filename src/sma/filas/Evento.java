package sma.filas;

public class Evento {
	public Tipo tipo;
	public double tempo;
	public double sorteio;
	public Fila origem;
	public Fila destino;

	public Evento(Tipo tipo, double tempo, double sorteio) {
		super();
		this.tipo = tipo;
		this.tempo = tempo;
		this.sorteio = sorteio;
	}

	public Evento(Tipo tipo, double tempo, Fila origem, Fila destino) {
		super();
		this.tipo = tipo;
		this.tempo = tempo;
		this.origem = origem;
		this.destino = destino;
	}

	public Evento(Tipo tipo, double tempo, double sorteio, Fila origem, Fila destino) {
		super();
		this.tipo = tipo;
		this.tempo = tempo;
		this.sorteio = sorteio;
		this.origem = origem;
		this.destino = destino;
	}

	public Fila getOrigem() {
		return origem;
	}

	public void setOrigem(Fila origem) {
		this.origem = origem;
	}

	public Fila getDestino() {
		return destino;
	}

	public void setDestino(Fila destino) {
		this.destino = destino;
	}

	public Evento() {
		super();
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public double getTempo() {
		return tempo;
	}

	public void setTempo(double tempo) {
		this.tempo = tempo;
	}

	public double getSorteio() {
		return sorteio;
	}

	public void setSorteio(double sorteio) {
		this.sorteio = sorteio;
	}

	@Override
	public String toString() {
		return "Evento [tipo=" + tipo + ", tempo=" + tempo + ", sorteio=" + sorteio + "]";
	}

}
