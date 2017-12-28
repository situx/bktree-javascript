package com.github.situx.webime.bktree;

import java.io.Serializable;

import com.github.liblevenshtein.collection.dictionary.DawgNode;

/**
	 * Creates a linked list that can be used to traverse an
	 * {@link com.github.liblevenshtein.collection.dictionary.Dawg} structure.
	 * @author Dylon Edwards
	 * @since 2.1.0
	 */
	public final class Prefix implements Serializable {
	  private static final long serialVersionUID = 1L;
	  /**
	   * Current node in the trie.
	   */
	  private final DawgNode node;
	  /**
	   * Sub-term prefix leading up to this one.  This is used to build the
	   * {@link #value()} of the term once a final node is reached.
	   */
	  private final Prefix prevNode;
	  /**
	   * Label along the edge from the predecessor to {@link #node} up to itself.
	   */
	  private final char label;

	  /**
	   * Builds a new {@link Prefix} with the initial node of a DAWG structure.  All
	   * {@link Prefix}es should branch from this one.
	   * @param rootNode Root of the dictionary automaton.
	   */
	  public Prefix(final DawgNode rootNode) {
	    this(rootNode, null, '\000');
	  }

	  /**
	   * Value of the string built by traversing the DAWG from its root node to this
	   * one, and accumulating the character values of the nodes along the way.
	   * @return Value of the string built by traversing the DAWG from its root node
	   * to this one, and accumulating the character values of the nodes along the
	   * way.
	   */
	  public String value() {
	    return buffer().toString();
	  }

	  /**
	   * Buffers the labels along the edges of the previous {@link Prefix}es,
	   * beginning from the root node.
	   * @return A buffer of the labels along the edges of the previous
	   * {@link Prefix}es.
	   */
	  private StringBuilder buffer() {
	    final StringBuilder buffer;
	    if (null != prevNode) {
	      buffer = prevNode.buffer();
	      buffer.append(label);
	    } else {
	      buffer = new StringBuilder();
	    }
	    return buffer;
	  }

	  /**
	   * Current node in the trie.
	   */
	  @java.lang.SuppressWarnings("all")
	  @edu.umd.cs.findbugs.annotations.SuppressFBWarnings(justification = "generated code")
	  @javax.annotation.Generated("lombok")
	  public DawgNode node() {
	    return this.node;
	  }

	  /**
	   * Sub-term prefix leading up to this one.  This is used to build the
	   * {@link #value()} of the term once a final node is reached.
	   */
	  @java.lang.SuppressWarnings("all")
	  @edu.umd.cs.findbugs.annotations.SuppressFBWarnings(justification = "generated code")
	  @javax.annotation.Generated("lombok")
	  public Prefix prevNode() {
	    return this.prevNode;
	  }

	  /**
	   * Label along the edge from the predecessor to {@link #node} up to itself.
	   */
	  @java.lang.SuppressWarnings("all")
	  @edu.umd.cs.findbugs.annotations.SuppressFBWarnings(justification = "generated code")
	  @javax.annotation.Generated("lombok")
	  public char label() {
	    return this.label;
	  }

	  @java.lang.Override
	  @java.lang.SuppressWarnings("all")
	  @edu.umd.cs.findbugs.annotations.SuppressFBWarnings(justification = "generated code")
	  @javax.annotation.Generated("lombok")
	  public boolean equals(final java.lang.Object o) {
	    if (o == this) return true;
	    if (!(o instanceof Prefix)) return false;
	    final Prefix other = (Prefix) o;
	    final java.lang.Object this$node = this.node();
	    final java.lang.Object other$node = other.node();
	    if (this$node == null ? other$node != null : !this$node.equals(other$node)) return false;
	    final java.lang.Object this$prevNode = this.prevNode();
	    final java.lang.Object other$prevNode = other.prevNode();
	    if (this$prevNode == null ? other$prevNode != null : !this$prevNode.equals(other$prevNode)) return false;
	    if (this.label() != other.label()) return false;
	    return true;
	  }

	  @java.lang.Override
	  @java.lang.SuppressWarnings("all")
	  @edu.umd.cs.findbugs.annotations.SuppressFBWarnings(justification = "generated code")
	  @javax.annotation.Generated("lombok")
	  public int hashCode() {
	    final int PRIME = 59;
	    int result = 1;
	    final java.lang.Object $node = this.node();
	    result = result * PRIME + ($node == null ? 43 : $node.hashCode());
	    final java.lang.Object $prevNode = this.prevNode();
	    result = result * PRIME + ($prevNode == null ? 43 : $prevNode.hashCode());
	    result = result * PRIME + this.label();
	    return result;
	  }

	  @java.lang.Override
	  @java.lang.SuppressWarnings("all")
	  @edu.umd.cs.findbugs.annotations.SuppressFBWarnings(justification = "generated code")
	  @javax.annotation.Generated("lombok")
	  public java.lang.String toString() {
	    return "Prefix(node=" + this.node() + ", prevNode=" + this.prevNode() + ", label=" + this.label() + ")";
	  }

	  @java.beans.ConstructorProperties({"node", "prevNode", "label"})
	  @java.lang.SuppressWarnings("all")
	  @edu.umd.cs.findbugs.annotations.SuppressFBWarnings(justification = "generated code")
	  @javax.annotation.Generated("lombok")
	  public Prefix(final DawgNode node, final Prefix prevNode, final char label) {
	    this.node = node;
	    this.prevNode = prevNode;
	    this.label = label;
	  }
	}


