package com.github.situx.webime.bktree;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import com.github.liblevenshtein.collection.dictionary.Dawg;
import com.github.liblevenshtein.serialization.PlainTextSerializer;
import com.github.liblevenshtein.serialization.Serializer;
import com.github.liblevenshtein.transducer.Algorithm;
import com.github.liblevenshtein.transducer.Candidate;
import com.github.liblevenshtein.transducer.ITransducer;
import com.github.liblevenshtein.transducer.factory.TransducerBuilder;
import com.google.gson.Gson;

import edu.gatech.gtri.bktree.BkTreeSearcher;
import edu.gatech.gtri.bktree.Metric;
import eu.danieldk.dictomaton.DictionaryBuilder;
import eu.danieldk.dictomaton.levenshtein.LevenshteinAutomaton;
import info.debatty.java.stringsimilarity.Levenshtein;
import eu.danieldk.dictomaton.PerfectHashDictionary;

import eu.danieldk.dictomaton.collections.ImmutableStringObjectMap;

public class LevenshteinAutomatonn {

	
	public static void main(String[] args) throws Exception{
		Metric<BKTreeElement> mymetric=new Metric<BKTreeElement>() {
		    @Override
		    public int distance(BKTreeElement x, BKTreeElement y) {
		    	Levenshtein lev=new Levenshtein();
		    	return (int) lev.distance(x.transliteration, y.transliteration);
		    }
		};
		BKTreeExporter exporter=new BKTreeExporter(mymetric);
		final Dawg dictionary =exporter.makeLevenshteinAutomaton(exporter.parseJSON("dict/dict.js"));
		SortedMap<String,BKTreeElement> levlist=exporter.makeDictMap(exporter.parseJSON("dict/dict.js"));
		DictionaryBuilder builder=new DictionaryBuilder();
		PerfectHashDictionary dictt=builder.addAll(levlist.keySet()).buildPerfectHash();
		LevenshteinAutomaton la = new LevenshteinAutomaton("asz", 2);
		Set<String> test=la.intersectionLanguage(dictt);
		System.out.print("[");
		for(String res:test){
			System.out.println(levlist.get(res)+", ");
		}
		System.out.println("]");
		System.out.println(test);
		//ImmutableStringObjectMap<BKTreeElement> map=new ImmutableStringObjectMap.OrderedBuilder<BKTreeElement>().putAll(levlist).build();



	Candidate candi;
	
	final ITransducer<Candidate> transducer = new TransducerBuilder()
	  .dictionary(dictionary)
	  .algorithm(Algorithm.TRANSPOSITION)
	  .defaultMaxDistance(4)
	  .includeDistance(true)
	  .build();

	for (final String queryTerm : new String[] {"asz", "abc"}) {
	  System.out.println(
	    "+-------------------------------------------------------------------------------");
	  System.out.printf("| Spelling Candidates for Query Term: \"%s\"%n", queryTerm);
	  System.out.println(
	    "+-------------------------------------------------------------------------------");
	  for (final Candidate candidate : transducer.transduce(queryTerm)) {
	    System.out.printf("| d(\"%s\", \"%s\") = [%d]%n",
	      queryTerm,
	      candidate.term(),
	      candidate.distance());
	    System.out.println(levlist.get(queryTerm));
	  }
	//}
	Gson gson=new Gson();
	System.out.println(gson.toJson(transducer));
	}}
}
