WebIME implementation using a preexported BKTree
=================

[![Build Status](https://travis-ci.com/situx/webime-bktree.svg?branch=master)](https://travis-ci.com/situx/webime-bktree)

This repository includes code by yellowiscool/bktree-javascript. It has been modified to accept a preexported BKTree in JSON in order not to precalculate the BKTree on the fly on the homepage.

The code to export the BKTree has been included as well.
Currently the Levensthein distance is used as a distance measure, but it could be easily replaced with another metric using the referred package of java-string-similarity in pom.xml.
It would also be possible to write your own metric as a subclass of edu.gatech.gtri.bk-tree.Metric and include it into the export. 

Thank you to https://github.com/gtri/bk-tree for providing a bk-tree implementation in Java which I took the liberty of extending it with a JSON export.

On the website test.html it is possible to conduct an input search using the test dictionary on some cuneiform words.

