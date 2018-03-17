package com.otb.blockchain;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.GsonBuilder;
import com.otb.blockchain.model.Block;
import com.otb.blockchain.model.Transaction;
import com.otb.blockchain.model.TransactionInput;
import com.otb.blockchain.model.TransactionOutput;
import com.otb.blockchain.model.Wallet;
import com.otb.blockchain.utils.StringUtils;

public class BlockChainMain {
	public static List<Block> listBlocks = new ArrayList<>();
	public static HashMap<String, TransactionOutput> UTXOs = new HashMap<>();
	public static float minimumTransaction = 1;
	private final static int DIFFICULTY = 5;

	public static void main(String[] args) {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		// test_BlockCreationAndMining();
		test_WalletAndSignature();
	}

	private static void test_BlockCreationAndMining() {
		listBlocks.add(new Block("Mohit sent Sparsh $2", "0"));
		listBlocks.get(0).mineBlock(DIFFICULTY);
		listBlocks.add(new Block("Sparsh sent Vikas $5", listBlocks.get(
				listBlocks.size() - 1).getHash()));
		listBlocks.get(1).mineBlock(DIFFICULTY);
		listBlocks.add(new Block("Garvit sent Ravi $3", listBlocks.get(
				listBlocks.size() - 1).getHash()));
		listBlocks.get(2).mineBlock(DIFFICULTY);
		String blocksJson = new GsonBuilder().setPrettyPrinting().create()
				.toJson(listBlocks);
		System.out.println(blocksJson);
		System.out.println("isChainValid=" + isChainValid());
	}

	private static void test_WalletAndSignature() {
		Wallet walletOne = new Wallet();
		Wallet walletTwo = new Wallet();
		System.out.println("WalletOne\nPrivateKey: "
				+ StringUtils.getStringFromKey(walletOne.getPrivateKey())
				+ "\nPublicKey: "
				+ StringUtils.getStringFromKey(walletOne.getPublicKey()));
		
		List<TransactionInput> inputs = new ArrayList<>();
		Transaction transaction = new Transaction(walletOne.getPublicKey(), walletTwo.getPublicKey(), 5.0f, inputs);
		transaction.generateSignature(walletOne.getPrivateKey());
		System.out.println("Is signature valid="+transaction.isSignatureValid());
	}

	public static boolean isChainValid() {
		Block currentBlock, previousBlock;
		String hashTarget = new String(new char[DIFFICULTY]).replace('\0', '0');
		if (listBlocks.size() > 1) {
			for (int i = 1; i < listBlocks.size(); i++) {
				currentBlock = listBlocks.get(i);
				previousBlock = listBlocks.get(i - 1);
				if (!currentBlock.getHash().equals(currentBlock.generateHash())) {
					System.err.println("Current hashes are not equal");
					return false;
				}
				if (!currentBlock.getPreviousHash().equals(
						previousBlock.getHash())) {
					System.err.println("Previous hash is not equal");
					return false;
				}
				if (!currentBlock.getHash().subSequence(0, DIFFICULTY)
						.equals(hashTarget)) {
					System.err.println("This block has not been minded");
					return false;
				}
			}
		}
		return true;
	}
}
