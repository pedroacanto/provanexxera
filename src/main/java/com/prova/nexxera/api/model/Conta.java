package com.prova.nexxera.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;



@Entity
@Table(name="conta")
public class Conta {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="data_lancamento")
	private LocalDate dataLancamento;	
		
	@NotNull(message = "Valor é obrigatório.")
	private BigDecimal valor;
	
	@Column(name="saldo_anterior")
	private BigDecimal saldoAnterior;
	
	@ManyToOne
	@NotNull (message = "Valor é obrigatório.")
	private Filial filial;
	
	@Column(name="conta_paga")
	private boolean contaPaga;

	@Transient
	private Long filialId;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(LocalDate dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public boolean isContaPaga() {
		return contaPaga;
	}

	public void setContaPaga(boolean contaPaga) {
		this.contaPaga = contaPaga;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public BigDecimal getSaldoAnterior() {
		return saldoAnterior;
	}

	public void setSaldoAnterior(BigDecimal saldoAnterior) {
		this.saldoAnterior = saldoAnterior;
	}

	public Long getFilialId() {
		return filialId;
	}

	public void setFilialId(Long filialId) {
		this.filialId = filialId;
	}
	
}
