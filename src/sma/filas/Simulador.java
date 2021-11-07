package sma.filas;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Simulador {

	public double tempoAtual = 0;
	public double tempoUltimoEvento = 0;
	public int qtdAleatorios = 0;
	public List<Evento> eventosOcorridos = new ArrayList<>();
	public int qtdFilas = 0;
	public Escalonador escalonador;
	public final int INFINITA = 0;
	List<Roteamento> roteamentos = new ArrayList<>();
	List<Fila> filas = null;

	public Simulador(ArrayList<Fila> filas, long seed, int qtdAleatorios, ArrayList<Roteamento> roteamentos) {
		this.roteamentos = roteamentos;
		this.filas = filas;			
		escalonador = new Escalonador(seed);

	}

	public ArrayList<Fila> executa(ArrayList<Fila> filas, int qtdAleatorios) {
		this.qtdFilas = filas.size();
		this.qtdAleatorios = qtdAleatorios;		
		
		for (Fila fila : filas) {
			fila.estado = new ArrayList<>();
			fila.estado.add(0.0); //estado 0 da fila
			// adiciona os roteamentos da fila, se houverem
			fila.roteamentos = roteamentos.stream().filter(roteamento -> roteamento.FilaOrigem == fila.numeroFila)
					.collect(Collectors.toList());
			fila.roteamentos.sort(Comparator.comparing(Roteamento::getProbabilidade));
		}
		
		
		escalonador.agendaEventoInicial(filas.get(0).primeiroClienteTempo, Tipo.CHEGADA, filas.get(0).tempoAtendimentoMinimo, filas.get(0).tempoAtendimentoMaximo, filas.get(0));

		while (escalonador.qtdAleatorios < qtdAleatorios) { // executa enquanto houver nro aleatórios
			Evento evento = escalonador.executaProximoEvento();
			this.eventosOcorridos.add(evento);
			if (evento.tipo.equals(Tipo.CHEGADA)) {
				this.chegada(evento.origem, null, evento.sorteio);
			} else if (evento.tipo.equals(Tipo.PASSAGEM)) {
				this.passagem(evento.origem, evento.destino, evento.sorteio);
			} else { // SAIDA
				this.saida(evento.destino == null ? evento.origem : evento.destino , evento.sorteio);
			}
		}

		return filas;
	}

	private void atualizaTempos(Fila filaOrigem) {
		
		if (filaOrigem.estado.size() <= filaOrigem.clientesNaFila) {
			filaOrigem.estado.add(tempoAtual - tempoUltimoEvento);
		} else {
			filaOrigem.estado.set(filaOrigem.clientesNaFila,
					filaOrigem.estado.get(filaOrigem.clientesNaFila) + (tempoAtual - tempoUltimoEvento));
		}
		
		//atualizar tempo demais filas, exceto a fila ja ajustada
		for (Fila fila : filas) {
			if (fila.numeroFila == filaOrigem.numeroFila) continue;
			
			if (fila.estado.size() <= fila.clientesNaFila) {
				fila.estado.add(tempoAtual - tempoUltimoEvento);
			} else {
				fila.estado.set(fila.clientesNaFila,
						fila.estado.get(fila.clientesNaFila) + (tempoAtual - tempoUltimoEvento));
			}
		}

	}

	public void chegada(Fila filaOrigem, Fila filaDestino, double tempoSorteio) {
		tempoUltimoEvento = tempoAtual;
		tempoAtual += tempoSorteio == 0 ? filaOrigem.primeiroClienteTempo : tempoSorteio;
		atualizaTempos(filaOrigem);
		if (filaOrigem.capacidadeFila == INFINITA || filaOrigem.clientesNaFila < filaOrigem.capacidadeFila) {
			filaOrigem.clientesNaFila++;
			if (filaOrigem.clientesNaFila <= filaOrigem.numeroServidores) { // chegou e se encontra de frente pra um
				if (filaOrigem.roteamentos.isEmpty()) {
					escalonador.agendaEvento(tempoAtual, qtdFilas == 1 ? Tipo.SAIDA : Tipo.PASSAGEM,
							filaOrigem.tempoAtendimentoMinimo, filaOrigem.tempoAtendimentoMaximo, filaOrigem,
							filaDestino);
				} else {
					Fila filaDst = this.filaRoteada(filaOrigem);
					escalonador.agendaEvento(tempoAtual, filaDst != null ? Tipo.PASSAGEM : Tipo.SAIDA, filaOrigem.tempoAtendimentoMinimo,
							filaOrigem.tempoAtendimentoMaximo, filaOrigem, filaDst);
				}
			}
		} else {
			filaOrigem.perdas += 1;
		}
				
		escalonador.agendaEvento(tempoAtual, Tipo.CHEGADA, filaOrigem.tempoChegadaMinimo, filaOrigem.tempoChegadaMaximo,
				filaOrigem, filaRoteada(filaOrigem));
	}

	private void passagem(Fila filaOrigem, Fila filaDestino, double tempoSorteio) {
		tempoUltimoEvento = tempoAtual;
		tempoAtual += tempoSorteio;
		this.atualizaTempos(filaOrigem);
		filaOrigem.clientesNaFila--;

		if (filaOrigem.clientesNaFila >= filaOrigem.numeroServidores) { // passar da fila origem para a destino
			if (filaOrigem.roteamentos.isEmpty()) { //fila sem roteamento
				escalonador.agendaEvento(tempoAtual, qtdFilas == 1 ? Tipo.SAIDA : Tipo.PASSAGEM,
						filaOrigem.tempoAtendimentoMinimo, filaOrigem.tempoAtendimentoMaximo, filaOrigem, filaDestino);
			} else {
				Fila filaDst = this.filaRoteada(filaOrigem);
				escalonador.agendaEvento(tempoAtual, filaDst != null ? Tipo.PASSAGEM : Tipo.SAIDA,
						filaOrigem.tempoAtendimentoMinimo, filaOrigem.tempoAtendimentoMaximo, filaOrigem, filaDst);
			}
		}

		filaDestino.clientesNaFila++;
		if (filaDestino.clientesNaFila <= filaDestino.numeroServidores) {
			escalonador.agendaEvento(tempoAtual, Tipo.SAIDA, filaDestino.tempoAtendimentoMinimo,
					filaDestino.tempoAtendimentoMaximo, filaOrigem, filaDestino);
		}
	}

	public void saida(Fila fila, double tempoSorteio) {
		tempoUltimoEvento = tempoAtual;
		tempoAtual += tempoSorteio;
		atualizaTempos(fila);
		fila.clientesNaFila--;
		if (fila.clientesNaFila >= fila.numeroServidores) {
			if (fila.roteamentos.isEmpty()) {
				escalonador.agendaEvento(tempoAtual, qtdFilas == 1 ? Tipo.SAIDA : Tipo.PASSAGEM,
						fila.tempoAtendimentoMinimo, fila.tempoAtendimentoMaximo, fila,
						null);
			} else {
				Fila filaDst = this.filaRoteada(fila);
				escalonador.agendaEvento(tempoAtual, filaDst != null ? Tipo.PASSAGEM : Tipo.SAIDA, fila.tempoAtendimentoMinimo,
						fila.tempoAtendimentoMaximo, fila, filaDst);
			}
		}
	}

	/**
	 * Trata o roteamento das filas, recebe a fila origem com seus roteamentos
	 * e consome um aleatorio para saber a fila destino ou retorna null se for uma saída.
	 */
	private Fila filaRoteada(Fila filaOrigem) {
		double rnd = escalonador.geraNroAleatorio();
		double acm = 0.0;
		// valida a lista de roteamentos destino dessa fila
		for (Roteamento roteamento : filaOrigem.roteamentos) {
			acm += roteamento.probabilidade;
			if (rnd < acm) {
				//caiu em um roteamento de passagem, agenda e para a execução do método
				Fila filaDestinoRtm = filas.get((int) roteamento.FilaDestino - 1);
				return filaDestinoRtm; 
			}
		}

		//não caiu nenhuma passagem
		return null;
	}

}