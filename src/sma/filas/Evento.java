package sma.filas;

public class Evento {
	public Tipo tipo;
	public double tempo;
	public double sorteio;
	public boolean agendado;

	public Evento(Tipo tipo, double tempo, double sorteio) {
		super();
		this.tipo = tipo;
		this.tempo = tempo;
		this.sorteio = sorteio;
	}
	
	public Evento(Tipo tipo, double tempo) {
		super();
		this.tipo = tipo;
		this.tempo = tempo;
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
