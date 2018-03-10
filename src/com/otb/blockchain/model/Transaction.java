package com.otb.blockchain.model;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import com.otb.blockchain.utils.StringUtils;

public class Transaction {
	private String transactionId;
	private PublicKey sender;
	private PublicKey receiver;
	private float value;
	private byte[] signature;

	private List<TransactionInput> inputs = new ArrayList<>();
	private List<TransactionOutput> outputs = new ArrayList<>();

	private int sequence;

	public Transaction(PublicKey sender, PublicKey receiver, float value,
			List<TransactionInput> inputs) {
		super();
		this.sender = sender;
		this.receiver = receiver;
		this.value = value;
		this.inputs = inputs;
	}

	// This hash is the transaction ID
	public String generateHash() {
		sequence++; // Increase sequence everytime generating a new hash so that
					// two identical transactions will not have same hash again.
		return StringUtils.applySHA256(StringUtils.getStringFromKey(sender)
				+ StringUtils.getStringFromKey(receiver)
				+ Float.toString(value) + sequence);
	}

	public void generateSignature(PrivateKey privateKey) {
		String transactionData = StringUtils.getStringFromKey(sender)
				+ StringUtils.getStringFromKey(receiver)
				+ Float.toString(value);
		signature = StringUtils.applyECDSASig(privateKey, transactionData);
	}

	public boolean isSignatureValid() {
		String transactionData = StringUtils.getStringFromKey(sender)
				+ StringUtils.getStringFromKey(receiver)
				+ Float.toString(value);
		return StringUtils.verifyECDSASig(sender, transactionData, signature);
	}
}
