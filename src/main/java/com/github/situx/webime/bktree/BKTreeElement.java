package com.github.situx.webime.bktree;

import java.util.UUID;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ModelFactory;
import org.json.JSONObject;

public class BKTreeElement implements Comparable<BKTreeElement> {

	private static String NAMESPACE="https://github.com/situx/ontology/cuneiform";
	
	public Integer recid;
	
	public String concept;
	
	public String meaning;
	
	public String ref;
	
	public String pos;
	
	public String transcription;
	
	public String transliteration;
	
	public String translation;
	
	public String script;


	public BKTreeElement(String transliteration){
		this.transliteration=transliteration;
	}
	
	public BKTreeElement(String word, String postag, String translation, String concept, String transliteration,
			Integer wordid,String transcription,String meaning, String ref) {
		super();
		this.script = word;
		this.pos = postag;
		this.translation = translation;
		this.concept = concept;
		this.transliteration = transliteration;
		this.recid = wordid;
		this.transcription=transcription;
		this.meaning=meaning;
		this.ref=ref;
	}
	
	public JSONObject toJSON(Integer metricvalue){
		JSONObject json=new JSONObject();
		json.put("recid", this.recid);
		json.put("concept", this.concept);
		json.put("meaning", this.meaning);
		json.put("ref",this.ref);
		json.put("pos", this.pos);
		json.put("translation", this.translation);
		json.put("transliteration", this.transliteration);
		json.put("transcription", this.transcription);
		json.put("script", this.script);
		json.put("bkvalue", metricvalue);
		return json;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((concept == null) ? 0 : concept.hashCode());
		result = prime * result + ((pos == null) ? 0 : pos.hashCode());
		result = prime * result + ((translation == null) ? 0 : translation.hashCode());
		result = prime * result + ((transliteration == null) ? 0 : transliteration.hashCode());
		result = prime * result + ((transcription == null) ? 0 : transcription.hashCode());
		result = prime * result + ((script == null) ? 0 : script.hashCode());
		result = prime * result + ((recid == null) ? 0 : recid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BKTreeElement other = (BKTreeElement) obj;
		return other.transliteration.equals(this.transliteration);
	}

	@Override
	public String toString() {
		JSONObject json=new JSONObject();
		json.put("recid", this.recid);
		json.put("script", this.script);
		json.put("pos", this.pos);
		json.put("translation", this.translation);
		json.put("transliteration", this.transliteration);
		json.put("transcription", this.transcription);
		json.put("concept", this.concept);
		return json.toString();
	}
	
	public OntModel toRDF(Integer metricvalue,String metric,String locale){
		System.out.println("MetricValue: "+metricvalue);
		OntModel model=ModelFactory.createOntologyModel();
		DatatypeProperty distance=model.createDatatypeProperty("http://lemon-model.net/lemon#distance");
		ObjectProperty isSenseOf=model.createObjectProperty("http://lemon-model.net/lemon#isSenseOf");
		ObjectProperty hasSense=model.createObjectProperty("http://lemon-model.net/lemon#hasSense");
		ObjectProperty lemonReference=model.createObjectProperty("http://lemon-model.net/lemon#reference");
		OntClass lemonWord=model.createClass("http://lemon-model.net/lemon#Word");
        OntClass lemonForm=model.createClass("http://lemon-model.net/lemon#LexicalForm");
        OntClass metic=model.createClass("http://www.w3.org/ns/dqv#Metric");
        OntClass lemonReferenceClass=model.createClass("http://lemon-model.net/lemon#LemonReference");
        OntClass qualitymeasurement=model.createClass("http://www.w3.org/ns/dqv#QualityMeasurement");
        Individual metricind=metic.createIndividual(NAMESPACE+"#"+metric);
        Individual qualitymeasurementind=qualitymeasurement.createIndividual(NAMESPACE+"/measurement#"+script+"_"+metric);
        qualitymeasurementind.addLabel(script+"_"+metric, "en");
        qualitymeasurementind.addProperty(model.createObjectProperty(NAMESPACE+"#hasQualityMetric"), metricind);
        qualitymeasurementind.addProperty(model.createDatatypeProperty(NAMESPACE+"#hasValue"), model.createTypedLiteral(metricvalue));
        ObjectProperty isFormOf=model.createObjectProperty("http://lemon-model.net/lemon#isFormOf");
        ObjectProperty lemonEntryProp=model.createObjectProperty("http://lemon-model.net/lemon#entry");
        System.out.println("Word: "+script);
        String uuid= UUID.randomUUID().toString();
        Individual curword=lemonWord.createIndividual(NAMESPACE+"/word#"+script.replace(" ","")+"_word");
        OntClass lemonLexicon=model.createClass("http://lemon-model.net/lemon#Lexicon");
        Individual cursense;
        Individual lexicon=lemonLexicon.createIndividual(NAMESPACE+"/"+locale);
        //lexicon.addProperty(license,model.createOntResource("https://www.wikidata.org/wiki/Q10513445"));
        lexicon.addProperty(lemonEntryProp,curword);
        OntClass lemonLexicalSense=model.createClass("http://lemon-model.net/lemon#LexicalSense");
        Individual curform=lemonForm.createIndividual(NAMESPACE+"/word#"+script.replace(" ","")+"_form");
        curform.addProperty(model.createDatatypeProperty("http://lemon-model.net/lemon#writtenRep"),script);
        curform.addProperty(model.createDatatypeProperty("http://lemon-model.net/lemon#representation"),transliteration);
        Literal label = model.createLiteral( transliteration, transliteration );
        curword.addLabel(transliteration,"en");
        curform.addProperty(model.createObjectProperty(NAMESPACE+"#distance"), qualitymeasurementind);
        //curword.addLiteral(model.createAnnotationProperty("http://www.w3.org/2000/01/rdf-schema#label"),label);
        label = model.createLiteral( script );

        curform.addLabel(script,"en");
        curform.addProperty(model.createDatatypeProperty("http://lemon-model.net/lemon#writtenRep"), script);
        if(concept!=null && !concept.isEmpty()){
            System.out.println("ConceptURI: "+concept.toString());

            if(concept.contains("#")){
                cursense=lemonLexicalSense.createIndividual(NAMESPACE+"/word#"+concept.substring(concept.lastIndexOf('#')+1).replace(" ",""));
                cursense.addLabel(concept.substring(concept.lastIndexOf('#')+1).replace(" ","")+"_sense","en");
            }else if(concept.contains("/")) {
                cursense = lemonLexicalSense.createIndividual(NAMESPACE+"/word#" + concept.substring(concept.lastIndexOf('/')+1).replace(" ",""));
                cursense.addLabel(concept.substring(concept.lastIndexOf('/')+1).replace(" ","")+"_sense","en");
            }else{
                String uuuid=UUID.randomUUID().toString();
                cursense = lemonLexicalSense.createIndividual(NAMESPACE+"/word#" + uuuid);
                cursense.addLabel(uuuid+"_sense",null);
            }
            curword.addProperty(hasSense,cursense);
            cursense.addProperty(isSenseOf,curword);
            cursense.addProperty(lemonReference,lemonReferenceClass.createIndividual(concept));
            OntClass pos=model.createClass("http://purl.org/olia/ubyCat.owl#PartOfSpeech");
            System.out.println("Postags: "+this.pos);
            if(!this.pos.isEmpty()) {
                System.out.println("Adding postag: "+this.pos);
                curform.addProperty(model.createObjectProperty("http://purl.org/olia/ubyCat.owl#partOfSpeech"), pos.createIndividual(this.pos));
            }
        }
        return model;
	}

	@Override
	public int compareTo(BKTreeElement o) {
		return this.transliteration.compareTo(o.transliteration);
	}
	
	public boolean isEmpty(){
		return this.transliteration.isEmpty();
	}
	
	public Integer length(){
		return this.transliteration.length();
	}
	
	public char charAt(Integer position){
		return this.transliteration.charAt(position);
	}
	
}
