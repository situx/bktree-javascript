package com.github.situx.webime.bktree;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class Euklid {
	
	public static int ggt(int a, int b)
	{
	   if (a==b) return(a);
	   else
	   {
		  System.out.println("("+a+","+b+")");
	      if (a>b) return(ggt(a-b,b));
	      else return(ggt(b-a,a));
	   }
	}
	
	
	public static Set<Set<Object>> cartesianProduct(Set<?>... sets) {
	    if (sets.length < 2)
	        throw new IllegalArgumentException(
	                "Can't have a product of fewer than two sets (got " +
	                sets.length + ")");

	    return _cartesianProduct(0, sets);
	}

	private static Set<Set<Object>> _cartesianProduct(int index, Set<?>... sets) {
	    Set<Set<Object>> ret = new HashSet<Set<Object>>();
	    if (index == sets.length) {
	        ret.add(new HashSet<Object>());
	    } else {
	        for (Object obj : sets[index]) {
	            for (Set<Object> set : _cartesianProduct(index+1, sets)) {
	                set.add(obj);
	                ret.add(set);
	            }
	        }
	    }
	    return ret;
	}
	
	public static void cartProduct(){
		Set<String> listL=new TreeSet<String>(Arrays.asList("01", "10", "11"));
		Set<String> listLStar=new TreeSet<String>(Arrays.asList("e","01", "10", "11"));
		Set<String> listLPlus=new TreeSet<String>(Arrays.asList("01", "10", "11"));
		Set<String> listLWo1011=new TreeSet<String>(Arrays.asList("01"));
		Set<String> listM=new TreeSet<String>(Arrays.asList("001", "110"));
		Set<String> listMStar=new TreeSet<String>(Arrays.asList("e","001", "110"));
		Set<List<String>> listM2=Sets.cartesianProduct(listM,listM);
		Set<List<String>> listL2=Sets.cartesianProduct(listL,listL);
		Set<Set<Object>> listL3=cartesianProduct(listL,listL);
		System.out.println("ListL: "+listL);
		System.out.println("Aufgabe 6.1a) ListL2: "+listL2);
		System.out.println("ListM: "+listM);
		System.out.println("ListM2: "+listM2);
		System.out.println("ListM*: "+listMStar);
		System.out.println("ListL3: "+listL3);
		System.out.println("Aufgabe 6.1b) "+Sets.union(listM2, listLWo1011));
		System.out.println("Aufgabe 6.1c) "+Sets.intersection(listMStar, listL3));
		System.out.println("Aufgabe 6.1c) "+Sets.intersection(listM2, listL3));
		System.out.println("Aufgabe 6.4d): "+Sets.difference(listLStar, listLPlus));
		//Set<List<String>> listL3=Sets.cartesianProduct(listL2,listL);
		//System.out.println(Sets.intersection(listMStar, listL3));
	}
	 /*
	  * Hauptprogramm:
	  */
	 public static void main(String[] args) {
	   /*
	    * Kommandozeilenargumente einlesen
	    * Aufruf: "ggt <zahl1> <zahl2>"
	    */
	   int a = Integer.parseInt(args[0]);
	   int b = Integer.parseInt(args[1]);
	 
	   // berechne ggT mit der Funktion "ggt()"
	   int ergebnis = ggt(a, b);
	 
	   // Ausgabe des Ergebnisses:
	   System.out.println("ggT("  + a + 
	       "," + b + ")=" + ergebnis);
	   
	   cartProduct();
	 }
	}
