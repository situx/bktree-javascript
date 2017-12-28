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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import com.github.liblevenshtein.collection.dictionary.Dawg;
import com.github.liblevenshtein.collection.dictionary.factory.DawgFactory;


public class BKTreeExporter {

	Integer workedon=0;
	
	Metric<BKTreeElement> metric;
	
	public String locale="";
	
	public MutableIMETree<BKTreeElement> bktree;
	
	public BKTreeExporter(Metric<BKTreeElement> metric){
		this.metric=metric;
		this.bktree=new MutableIMETree<>(metric);
	}
	
	
	public BKTreeExporter(Metric<BKTreeElement> metric,String locale){
		this.metric=metric;
		this.bktree=new MutableIMETree<>(metric);
		this.locale=locale;
	}
	
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
	
	
	public  List<BKTreeElement> makeDictList(JSONObject jsondict){
		JSONArray array=jsondict.getJSONArray("records");
		List<BKTreeElement> levlist=new LinkedList<BKTreeElement>();
		for(int i=0;i<array.length();i++){
			JSONObject record=array.getJSONObject(i);
			levlist.add(new BKTreeElement(record.getString("script"),
					record.getString("pos"),
					record.getString("translation"),
					record.getString("concept"),
					record.getString("transliteration"),
					record.getInt("recid"),
					record.getString("transcription"),record.getString("meaning"),record.getString("ref")));
		}
		return levlist;
	}
	
	public  SortedMap<String,BKTreeElement> makeDictMap(JSONObject jsondict){
		JSONArray array=jsondict.getJSONArray("records");
		SortedMap<String,BKTreeElement> levlist=new TreeMap<String,BKTreeElement>();
		for(int i=0;i<array.length();i++){
			JSONObject record=array.getJSONObject(i);
			levlist.put(record.getString("transliteration"),new BKTreeElement(record.getString("script"),
					record.getString("pos"),
					record.getString("translation"),
					record.getString("concept"),
					record.getString("transliteration"),
					record.getInt("recid"),
					record.getString("transcription"),record.getString("meaning"),record.getString("ref")));
		}
		return levlist;
	}
	
	
	public Dawg makeLevenshteinAutomaton(JSONObject jsondict){
		JSONArray array=jsondict.getJSONArray("records");
		List<BKTreeElement> levlist=new LinkedList<BKTreeElement>();
		for(int i=0;i<array.length();i++){
			JSONObject record=array.getJSONObject(i);
			levlist.add(new BKTreeElement(record.getString("script"),
					record.getString("pos"),
					record.getString("translation"),
					record.getString("concept"),
					record.getString("transliteration"),
					record.getInt("recid"),
					record.getString("transcription"),record.getString("meaning"),record.getString("ref")));
		}
		Collections.sort(levlist);
		System.out.println(levlist);
		Dawg dawg=new DawgFactory().build(new LinkedList<String>());
		for(BKTreeElement elem:levlist){
			System.out.println(elem);
			dawg.add(elem.transliteration);
		}
		return dawg;
	}
	
	public void exportBKTreeToJSON(MutableIMETree<BKTreeElement> bktree,String filepath){
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
			FileWriter writer=new FileWriter(new File(filepath));
			writer.write("bktree="+bktree.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(result.toString());
	}
	
	public OntModel exportBKTreeToRDF(MutableIMETree<BKTreeElement> bktree,String filepath){
		OntModel model=ModelFactory.createOntologyModel();
		Node<BKTreeElement> root=bktree.getRoot();
		System.out.println(bktree.toString());
		model.add(((BKTreeElement)root.getElement()).toRDF(0,this.metric.getClass().getName(),this.locale));
		List<Node<BKTreeElement>> childnodes=new LinkedList<Node<BKTreeElement>>();
		childnodes.add(root);
		for(int i=0;i<10000;i++){
			Node<BKTreeElement> node=root.getChildNode(i);
			childnodes.add(node);
			if(node!=null && node.getElement()!=null)
				model.add(((BKTreeElement)node.getElement()).toRDF(i,this.metric.getClass().getName(),this.locale));
		}
		try {
			model.write(new FileWriter(new File(filepath)),"TTL");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return model;
	}
	
	public static void main(String[] args) throws IOException{
		Metric<BKTreeElement> mymetric=new Metric<BKTreeElement>() {
		    @Override
		    public int distance(BKTreeElement x, BKTreeElement y) {
		    	Levenshtein lev=new Levenshtein();
		    	return (int) lev.distance(x.transliteration, y.transliteration);
		    }
		};
		BKTreeExporter exporter=new BKTreeExporter(mymetric);
		MutableIMETree<BKTreeElement> tree=exporter.makeBKTree(exporter.parseJSON("dict/sumerian.js"));
		exporter.exportBKTreeToJSON(tree,"ime/sumerian_bktree_dict.js");
		exporter.exportBKTreeToRDF(tree,"ime/sumerian_bktree_dict.ttl");
		System.out.println(exporter.workedon);
		BkTreeSearcher<BKTreeElement> searcher=new BkTreeSearcher<BKTreeElement>(tree);
		System.out.println(searcher.search(new BKTreeElement("aszur"), 5));
	}
	
}
