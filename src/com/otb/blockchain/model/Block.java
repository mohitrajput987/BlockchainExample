package com.otb.blockchain.model;

import java.util.Date;

import com.otb.blockchain.utils.StringUtils;

public class Block {
	private String hash;
	private String previousHash;
	private String data;
	private long timestamp;
	private int nonce;

	public Block(String data, String previousHash) {
		this.data = data;
		this.previousHash = previousHash;
		this.timestamp = new Date().getTime();
		this.hash = generateHash();
	}

	public String getHash() {
		return hash;
	}

	public String getPreviousHash() {
		return previousHash;
	}

	public String getData() {
		return data;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String generateHash() {
		String calculatedHash = StringUtils.applySHA256(previousHash + data
				+ Long.toString(timestamp) + Integer.toString(nonce));
		return calculatedHash;
	}

	public void mineBlock(int difficulty) {
		String target = new String(new char[difficulty]).replace('\0', '0');
		while (!getHash().substring(0, difficulty).equals(target)) {
			nonce++;
			hash = generateHash();
		}
	}

	public String toString() {
		return "Block [hash=" + hash + "\npreviousHash=" + previousHash
				+ "\ndata=" + data + "\ntimestamp=" + timestamp
				+ "]\n----------------------------\n";
	}

}
