package com.github.situx.webime.bktree;

import org.json.JSONObject;

public class BKTreeElement {

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
	
}
