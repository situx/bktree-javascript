/**
	@module BKTree
	Inspired by https://github.com/threedaymonk/bktree
	See http://blog.notdot.net/archives/30-Damn-Cool-Algorithms,-Part-1-BK-Trees.html
	and http://www.dcc.uchile.cl/~gnavarro/ps/spire98.2.ps.gz
*/

/**
	Levenshtein distance function.
*/
var levenshtein = (function()
{
	var split = false;
	try{
		split=!('0')[0];
	} catch (e){
		split=true; // Earlier IE may not support access by string index
	}

	// http://kevin.vanzonneveld.net
	// + original by: Carlos R. L. Rodrigues (http://www.jsfromhell.com)
	// + bugfixed by: Onno Marsman
	// + revised by: Andrea Giammarchi (http://webreflection.blogspot.com)
	// + reimplemented by: Brett Zamir (http://brett-zamir.me)
	// + reimplemented by: Alexander M Beedie
	// * example 1: levenshtein('Kevin van Zonneveld', 'Kevin van Sommeveld');
	// * returns 1: 3
	return function(s1, s2) {

		if (s1 == s2) {
			return 0;
		}

		var s1_len = s1.length;
		var s2_len = s2.length;
		if (s1_len === 0) {
			return s2_len;
		}
		if (s2_len === 0) {
			return s1_len;
		}

		if (split){
			s1 = s1.split('');
			s2 = s2.split('');
		}

		var v0 = new Array(s1_len+1);
		var v1 = new Array(s1_len+1);

		var s1_idx=0, s2_idx=0, cost=0;
		for (s1_idx=0; s1_idx<s1_len+1; s1_idx++) {
			v0[s1_idx] = s1_idx;
		}
		var char_s1='', char_s2='';
		for (s2_idx=1; s2_idx<=s2_len; s2_idx++) {
			v1[0] = s2_idx;
			char_s2 = s2[s2_idx - 1];

			for (s1_idx=0; s1_idx<s1_len;s1_idx++) {
				char_s1 = s1[s1_idx];
				cost = (char_s1 == char_s2) ? 0 : 1;
				var m_min = v0[s1_idx+1] + 1;
				var b = v1[s1_idx] + 1;
				var c = v0[s1_idx] + cost;
				if (b < m_min) {
					m_min = b; }
				if (c < m_min) {
					m_min = c; }
				v1[s1_idx+1] = m_min;
			}
			var v_tmp = v0;
			v0 = v1;
			v1 = v_tmp;
		}
		return v0[s1_len];
	}
})();

/**
	BKTree Node
	@class BKTreeNode
	@constructor
	@param {String} term
*/
var BKTreeNode = function(term) {
	this.term = term;
	this.children = new Object();
};

BKTreeNode.prototype.add2 = function(term,scoree) {
	//var score = levenshtein(term["transliteration"], this.term["transliteration"]);
	alert("Score: "+scoree)
	// If the term is already in the tree 
	if (scoree === 0) return false;

	var child = this.children[scoree];
	if (child)
		return child.add(term);

	this.children[scoree] = new BKTreeNode(term);

	return true;
};

/**
	Add a term in the BK-Tree
	@method add
	@param {String} term
	@return {Boolean} true if the term was correctly added
*/
BKTreeNode.prototype.add = function(term) {
	var score = levenshtein(term["transliteration"], this.term["transliteration"]);

	// If the term is already in the tree 
	if (score === 0) return false;

	var child = this.children[score];
	if (child)
		return child.add(term);

	this.children[score] = new BKTreeNode(term);

	return true;
};




/**
	Search a term in the BK-Tree
	@method search
	@param {String} term
	@param {Number} threshold The search threshold
	@param {Object} collected The hashmap of collected terms
*/
BKTreeNode.prototype.search = function(term, threshold, collected) {
	var distance_at_BKTreeNode = levenshtein(term, this.term["transliteration"]);
	if (distance_at_BKTreeNode <= threshold){
		collected[this.term["transliteration"]] = distance_at_BKTreeNode;
	}

	for (var d = -threshold; d <= threshold; ++d) {
		var child = this.children[distance_at_BKTreeNode + d];
		if (child)
			child.search(term, threshold, collected);
	}
};

/**
	BKTree
	@class BKTree
	@constructor
*/
var BKTree = function() {
	this.root = new BKTreeNode('');
};

/**
	Add a term in the BK-Tree
	@method add
	@param {String} term
	@return {Boolean} true if the term was correctly added
*/
BKTree.prototype.add = function(term) {
	this.root = new BKTreeNode(term);
	this.add = BKTree.prototype._add_with_root;
	return true;
};	

/**
Add a term in the BK-Tree
@method add
@param {String} term
@return {Boolean} true if the term was correctly added
*/
BKTree.prototype.add2 = function(term,score) {
this.root = new BKTreeNode(term);
this.add2 = BKTree.prototype._add_with_root2;
return true;
};	


/**
	Add a term in the BK-Tree after the initialization.
	@method add
	@protected
	@param {String} term
	@return {Boolean} true if the term was correctly added
*/
BKTree.prototype._add_with_root = function(term) {
	return this.root.add(term);
};

/**
Add a term in the BK-Tree after the initialization.
@method add
@protected
@param {String} term
@return {Boolean} true if the term was correctly added
*/
BKTree.prototype._add_with_root2 = function(term,score) {
return this.root.add2(term,score);
};

/**
	Search a term in the BK-Tree
	@method search
	@param {String} term
	@param {Number} threshold The search threshold
	@return {Object} The hashmap of collected terms
*/
BKTree.prototype.search = function(term, threshold) {
	if (typeof threshold === 'undefined')
		threshold = 2;

	var collected = {};
	this.root.search(term, threshold, collected);
	return collected;
};

/**
	Add a list of terms in the BK-Tree.
	@method addList
	@param {Array} terms
*/
BKTree.prototype.addList = function(list) {
	// Copy of the list
	var list = list.slice(0);

	// Randomize the list in order to prevent worst cases
	// of the bk-tree algorithm
	for (var i = list.length - 1; i; --i)
	{
		var j = Math.floor( Math.random() * ( i + 1 ) );
		var tmp = list[i];
		list[i] = list[j];
		list[j] = tmp;
	}

	for (var i = 0; i < list.length; ++i)
		this.add(list[i]);
};


function searchTree(tree,term,threshold){
	if (typeof threshold === 'undefined')
		threshold = 2;

	var collected = {};
	searchTerms(tree["root"],term, threshold, collected);
	return collected;
};

function searchTerms(treenode,term, threshold, collected) {
	var distance_at_BKTreeNode = levenshtein(term, treenode.term["transliteration"]);
	if (distance_at_BKTreeNode <= threshold){
		obj={}
		obj["term"]=treenode.term;
		obj["distance"]=distance_at_BKTreeNode;
		collected[treenode.term["transliteration"]] = obj;
	}

	for (var d = -threshold; d <= threshold; ++d) {
		var child = treenode["children"][distance_at_BKTreeNode + d];
		if (child)
			searchTerms(child,term, threshold, collected);
	}
};