package com.otb.blockchain;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.GsonBuilder;
import com.otb.blockchain.model.Block;

public class BlockChainMain {
	private static List<Block> listBlocks = new ArrayList();
	private final static int DIFFICULTY = 2;

	public static void main(String[] args) {

		listBlocks.add(new Block("Laddu sent Golu $2", "0"));
		listBlocks.get(0).mineBlock(DIFFICULTY);
		listBlocks.add(new Block("Golu sent Laddu $5", listBlocks.get(
				listBlocks.size() - 1).getHash()));
		listBlocks.get(1).mineBlock(DIFFICULTY);
		listBlocks.add(new Block("Aman sent Golu $3", listBlocks.get(
				listBlocks.size() - 1).getHash()));
		listBlocks.get(2).mineBlock(DIFFICULTY);
		String blocksJson = new GsonBuilder().setPrettyPrinting().create()
				.toJson(listBlocks);
		System.out.println(blocksJson);
		System.out.println("isChainValid="+isChainValid());
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
				if(!currentBlock.getHash().subSequence(0, DIFFICULTY).equals(hashTarget)){
					System.err.println("This block has not been minded");
					return false;
				}
			}
		}
		return true;
	}
}
