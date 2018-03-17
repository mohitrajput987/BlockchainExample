package com.otb.blockchain.model;

public class TransactionInput {
	private String transactionOutputId;
	private TransactionOutput UTXO;
	
	public TransactionInput(String transactionOutputId) {
		this.transactionOutputId = transactionOutputId;
	}

	public String getTransactionOutputId() {
		return transactionOutputId;
	}

	public TransactionOutput getUTXO() {
		return UTXO;
	}

	public void setUTXO(TransactionOutput uTXO) {
		UTXO = uTXO;
	}
}
