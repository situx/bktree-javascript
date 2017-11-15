package com.github.situx.webime.bktree;

import edu.gatech.gtri.bktree.BkTree.Node;
import edu.gatech.gtri.bktree.BkTreeSearcher;
import info.debatty.java.stringsimilarity.Levenshtein;
import edu.gatech.gtri.bktree.Metric;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class BKTreeExporter {

	Integer workedon=0;
	
	public MutableIMETree<BKTreeElement> bktree=new MutableIMETree<BKTreeElement>(new Metric<BKTreeElement>() {
	    @Override
	    public int distance(BKTreeElement x, BKTreeElement y) {
	    	workedon++;
	    	Levenshtein lev=new Levenshtein();
	    	return (int) lev.distance(x.transliteration, y.transliteration);
	    }
	});
	
	public JSONObject parseJSON(String filepath) throws IOException{
		String content;
		content = new String(Files.readAllBytes(Paths.get(filepath)));
		if(!content.startsWith("{")){
			content=content.substring(content.indexOf('=')+1);
		}
		System.out.println(content);
		return new JSONObject(content);
	}
	
	public MutableIMETree<BKTreeElement> makeBKTree(JSONObject jsondict){
		JSONArray array=jsondict.getJSONArray("records");
		for(int i=0;i<array.length();i++){
			JSONObject record=array.getJSONObject(i);
			bktree.add(new BKTreeElement(record.getString("script"),
					record.getString("pos"),
					record.getString("translation"),
					record.getString("concept"),
					record.getString("transliteration"),
					record.getInt("recid"),
					record.getString("transcription"),record.getString("meaning"),record.getString("ref")));
		}
		return bktree;
	}
	
	public void exportBKTreeToJSON(MutableIMETree<BKTreeElement> bktree){
		Node<BKTreeElement> root=bktree.getRoot();
		System.out.println(bktree.toString());
		JSONObject result=new JSONObject();
		JSONArray records=new JSONArray();
		result.append("records", records);
		records.put(((BKTreeElement)root.getElement()).toJSON(0));
		List<Node<BKTreeElement>> childnodes=new LinkedList<Node<BKTreeElement>>();
		childnodes.add(root);
		for(int i=0;i<10000;i++){
			Node<BKTreeElement> node=root.getChildNode(i);
			childnodes.add(node);
			if(node!=null && node.getElement()!=null)
				records.put(((BKTreeElement)node.getElement()).toJSON(i));
		}
		try {
			FileWriter writer=new FileWriter(new File("output.js"));
			writer.write("bktree="+bktree.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(result.toString());
	}
	
	public static void main(String[] args) throws IOException{
		BKTreeExporter exporter=new BKTreeExporter();
		MutableIMETree<BKTreeElement> tree=exporter.makeBKTree(exporter.parseJSON("dict.js"));
		exporter.exportBKTreeToJSON(tree);
		System.out.println(exporter.workedon);
		BkTreeSearcher<BKTreeElement> searcher=new BkTreeSearcher<BKTreeElement>(tree);
		System.out.println(searcher.search(new BKTreeElement("aszur"), 5));
	}
	
}
