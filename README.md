Graph Traversal Algorithms
========================
[![Build Status](https://travis-ci.org/maxxboehme/GraphTraversalAlgorithms.svg?branch=master)](https://travis-ci.org/maxxboehme/GraphTraversalAlgorithms)

A GUI that shows how Dijkstra's Algorithm and the A* Algorithm look visually.

**Reason for Creating:** Came to creation by me wanting to have a way to help explainn to students how the algorithms work while I was an Undergraduate Teaching Assistant

Algorithms
----------
* Implements Dijkstra's Algorithm
* Implements A* Algorithm with multiple heuristics

**A* Heuristics**
* **Distance:** returns an estimate of the distance form the vertex to the goal vertex
* **Half Distance:** returns an estimate of the distance form the vertex to the goal vertex divided by 2
* **Zip returns:** 0 for all vertices
* **Randombs:** returns an estimate of the distance from the vertex to the goal vertex muliplied by a random fraction
* **RandomLies:** returns a random number as the estimate


![Animated demonstration](https://github.com/maxxboehme/GraphTraversalAlgorithms/raw/master/doc/images/GraphTraversalAlgorithmsExample.gif)
