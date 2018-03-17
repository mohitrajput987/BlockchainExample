package com.otb.blockchain.model;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.otb.blockchain.BlockChainMain;

public class Wallet {
	private PrivateKey privateKey;
	private PublicKey publicKey;
	private HashMap<String, TransactionOutput> UTXOs = new HashMap<>();

	public Wallet() {
		generateKeyPair();
	}

	private void generateKeyPair() {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
					"ECDSA", "BC");
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
			keyPairGenerator.initialize(ecSpec, secureRandom);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			privateKey = keyPair.getPrivate();
			publicKey = keyPair.getPublic();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public float getBalance() {
		float total = 0;
		for (Map.Entry<String, TransactionOutput> entry : BlockChainMain.UTXOs
				.entrySet()) {
			TransactionOutput transactionOutput = entry.getValue();
			if (transactionOutput.isMine(publicKey)) {
				UTXOs.put(transactionOutput.getId(), transactionOutput);
				total += transactionOutput.getValue();
			}
		}
		return total;
	}

	public Transaction sendFunds(PublicKey receiver, float value) {
		if (getBalance() < value) {
			System.err.println("Not enough balance to proceed this transaction");
			return null;
		}

		List<TransactionInput> inputs = new ArrayList<>();
		float total = 0;
		for (Map.Entry<String, TransactionOutput> entry : BlockChainMain.UTXOs
				.entrySet()) {
			TransactionOutput transactionOutput = entry.getValue();
			total += transactionOutput.getValue();
			inputs.add(new TransactionInput(transactionOutput.getId()));
			if (total > value) {
				break;
			}
		}

		Transaction transaction = new Transaction(publicKey, receiver, value,
				inputs);
		transaction.generateSignature(privateKey);

		for (TransactionInput transactionInput : inputs) {
			UTXOs.remove(transactionInput.getTransactionOutputId());
		}
		
		return transaction;
	}
}
