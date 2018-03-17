package com.otb.blockchain.model;

import java.security.PublicKey;

import com.otb.blockchain.utils.StringUtils;

public class TransactionOutput {
	private String id;
	private PublicKey receiver;
	private float value;
	private String transactionId; //id of the transaction for which this output was created

	public TransactionOutput(PublicKey receiver, float value,
			String transactionId) {
		this.receiver = receiver;
		this.value = value;
		this.transactionId = transactionId;
		this.id = StringUtils.applySHA256(StringUtils
				.getStringFromKey(receiver)
				+ Float.toString(value)
				+ transactionId);
	}

	public String getId() {
		return id;
	}

	public PublicKey getReceiver() {
		return receiver;
	}

	public float getValue() {
		return value;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public boolean isMine(PublicKey publicKey) {
		return publicKey == receiver;
	}
}
