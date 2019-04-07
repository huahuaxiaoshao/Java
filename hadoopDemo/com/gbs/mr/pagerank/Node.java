package com.gbs.mr.pagerank;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

public class Node {

	private double pageRank;
	private String[] adjacentNodeNames;
	private static final char fieldSeqarator = '\t';
	
	public double getPageRank() {
		return pageRank;
	}
	
	public Node setPageRank(double pageRank) {
		this.pageRank = pageRank;
		return this;
	}
	
	public String[] getAdjacentNodeNames() {
		return adjacentNodeNames;
	}
	
	public Node setAdjacentNodeNames(String[] adjacentNodeNames) {
		this.adjacentNodeNames = adjacentNodeNames;
		return this;
	}
	
	public boolean containsAdjacentNodeNames() {
		return adjacentNodeNames != null && adjacentNodeNames.length > 0;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(pageRank);
		if(adjacentNodeNames != null) {
			sb.append(fieldSeqarator).append(StringUtils.join(adjacentNodeNames, fieldSeqarator));
		}
		return sb.toString();
	} 
	
	//value = 1.0 B D
	public static Node fromMR(String value) throws IOException {
		String[] parts = StringUtils.splitPreserveAllTokens(value,fieldSeqarator);
		if(parts.length < 1) {
			 throw new IOException(
			          "Expected 1 or more parts but received " + parts.length);
		}
		Node node = new Node().setPageRank(Double.valueOf(parts[0]));
		if(parts.length > 1) {
			node.setAdjacentNodeNames(Arrays.copyOfRange(parts, 1, parts.length));
		}
		return node;
	}
	
	
	
}
