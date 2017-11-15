package com.github.situx.webime.bktree;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import edu.gatech.gtri.bktree.BkTree;
import edu.gatech.gtri.bktree.Metric;

import static java.lang.String.format;
/**
 * A mutable {@linkplain BkTree BK-tree}.
 *
 * <p>Mutating operations are <em>not</em> thread-safe.
 *
 * <p>Whereas the {@linkplain #add(Object) mutating methods} are iterative and
 * can thus handle very large trees, the {@link #equals(Object)},
 * {@link #hashCode()} and {@link #toString()} methods on this class and its
 * {@link BkTree.Node} implementation are each recursive and as such may not
 * complete normally when called on very deep trees.
 *
 * @param <E> type of elements in this tree
 */

public final class MutableIMETree<E> implements BkTree<E> {

	     private final Metric<? super E> metric;
	     @Nullable MutableNode<E> root;

	     public MutableIMETree(Metric<? super E> metric) {
	         if (metric == null) throw new NullPointerException();
	         this.metric = metric;
	     }

	     /**
	      * Adds the given element to this tree, if it's not already present.
	      *
	      * @param element element
	      */
	     public void add(E element) {
	         if (element == null) throw new NullPointerException();

	         if (root == null) {
	             root = new MutableNode<>(element);
	         } else {
	             MutableNode<E> node = root;
	             while (!node.getElement().equals(element)) {
	                 int distance = distance(node.getElement(), element);

	                 MutableNode<E> parent = node;
	                 node = parent.children.get(distance);
	                 if (node == null) {
	                     node = new MutableNode<>(element);
	                     parent.children.put(distance, node);
	                     break;
	                 }
	             }
	         }
	     }

	     private int distance(E x, E y) {
	         int distance = metric.distance(x, y);
	         if (distance < 0) {
	             throw new IllegalMetricException(
	                 format("negative distance (%d) defined between elements `%s` and `%s`", distance, x, y));
	         }
	         return distance;
	     }

	     /**
	      * Adds all of the given elements to this tree.
	      *
	      * @param elements elements
	      */
	     public void addAll(Iterable<? extends E> elements) {
	         if (elements == null) throw new NullPointerException();
	         for (E element : elements) {
	             add(element);
	         }
	     }

	     /**
	      * Adds all of the given elements to this tree.
	      *
	      * @param elements elements
	      */
	     @SafeVarargs
	     public final void addAll(E... elements) {
	         if (elements == null) throw new NullPointerException();
	         addAll(Arrays.asList(elements));
	     }

	     @Override
	     public Metric<? super E> getMetric() {
	         return metric;
	     }

	     @Override
	     public @Nullable Node<E> getRoot() {
	         return root;
	     }

	     @Override
	     public boolean equals(Object o) {
	         if (this == o) return true;
	         if (o == null || getClass() != o.getClass()) return false;

	         MutableIMETree<E> that = (MutableIMETree<E>) o;

	         if (!metric.equals(that.metric)) return false;
	         if (root != null ? !root.equals(that.root) : that.root != null) return false;

	         return true;
	     }

	     @Override
	     public int hashCode() {
	         int result = metric.hashCode();
	         result = 31 * result + (root != null ? root.hashCode() : 0);
	         return result;
	     }

	     @Override
	     public String toString() {
	    	 Gson gson=new GsonBuilder().setPrettyPrinting().create();
	    	 return gson.toJson(this);
	    	 /*JSONObject object=new JSONObject();
	    	 object.put("metric", this.metric.toString());
	    	 JSONArray records=new JSONArray();
	    	 object.put("records", records);
	    	 JSONObject elem=new JSONObject(root.getElement());
	    	 elem.put("bkvalue", 0);
	    	 records.put(elem);
	         return object.toString();*/
	     }
	     

	     static final class MutableNode<E> implements Node<E> {
	         final E term;
	         final Map<Integer, MutableNode<E>> children = new HashMap<>();

	         MutableNode(E element) {
	             if (element == null) throw new NullPointerException();
	             this.term = element;
	         }

	         @Override
	         public E getElement() {
	             return term;
	         }

	         @Override
	         public @Nullable Node<E> getChildNode(int distance) {
	             return children.get(distance);
	         }

	         @Override
	         public boolean equals(Object o) {
	             if (this == o) return true;
	             if (o == null || getClass() != o.getClass()) return false;

	             MutableNode<E> that = (MutableNode<E>) o;

	             if (!children.equals(that.children)) return false;
	             if (!term.equals(that.term)) return false;

	             return true;
	         }

	         @Override
	         public int hashCode() {
	             int result = term.hashCode();
	             result = 31 * result + children.hashCode();
	             return result;
	         }

	         @Override
	         public String toString() {
		    	 /*JSONObject object=new JSONObject();
		    	 object.put("metric", this.metric.toString());
		    	 JSONArray records=new JSONArray();
		    	 object.put("records", records);
		    	 JSONObject elem=new JSONObject(this.getElement());
		    	 elem.put("bkvalue", this.);
		    	 records.put(elem);
		         return object.toString();*/
	        	 JSONObject obj=new JSONObject();
	        	 obj.put("element", new JSONObject(term));
	        	 Gson gson=new GsonBuilder().setPrettyPrinting().create();
	        	 String jsonString=gson.toJson(this);
	        	 return jsonString;
	        	 /*sb.append(element);
	        	 sb.append(jsonString);
	        	 for(this.childrenByDistance.keySet())
	             StringBuilder sb = new StringBuilder("\"MutableNode\":{");
	             sb.append("\"element\":{").append(element);
	             sb.append(", \"childrenByDistance\":{").append(childrenByDistance);
	             sb.append('}');
	             return sb.toString();*/
	         }
	         
	         /*public JSONObject toJSON(Integer distance){
		    	 JSONObject object=new JSONObject();
		    	 object.put("metric", this.metric.toString());
		    	 JSONArray records=new JSONArray();
		    	 object.put("records", records);
		    	 JSONObject elem=new JSONObject(root.getElement());
		    	 elem.put("bkvalue", 0);
		    	 records.put(elem);
		         return object.toString();
	        	 return records.put(this.getElement().toJSON(0));
	         }*/
	     }

}
	 
