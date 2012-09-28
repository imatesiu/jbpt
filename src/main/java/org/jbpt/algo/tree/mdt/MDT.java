package org.jbpt.algo.tree.mdt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jbpt.algo.tree.mdt.ComponentGraph;
import org.jbpt.algo.tree.mdt.MDTNode;
import org.jbpt.algo.tree.mdt.MDTNode.NodeType;
import org.jbpt.graph.DirectedEdge;
import org.jbpt.graph.DirectedGraph;
import org.jbpt.hypergraph.abs.Vertex;

/**
 * This class computes the Modular Decomposition Tree of a directed graph.
 * It implements the algorithm described in the following article:
 * 
 * A. Ehrenfeucht, H.N. Gabow, R.M. McConnell, and S.J. Sullivan
 * An O(n^2) Divide-and Conquer Algorithm for the Prime Tree Decomposition of
 * Two-Structures and Modular Decomposition of Graphs
 * JOURNAL OF ALGORITHMS 16, 283-294 (1994)
 * 
 * @author Luciano Garcia-Banuelos
 */
public class MDT {
	private DirectedGraph graph;
	private MDTNode root;

	public MDT(DirectedGraph graph) {
		this.graph = graph;
		this.root = decompose(graph.getVertices());
	}

	/**
	 * Given vertex v, this method partitions a set of vertices into four
	 * partitions according to their connectivity pattern with v:
	 * 	1:	Bidirectional connected
	 * 	2:	Directed edge having v as target vertex
	 * 	3:	Directed edge having v as source vertex
	 *	4:	Disconnected
	 *
	 * @param vertices	Set of vertices to partition
	 * @param v			Vertex to guide partitioning
	 */
	private List<Set<Vertex>> partitionSubsets(Collection<Vertex> vertices, Vertex v) {		
		List<Set<Vertex>> partitions = new ArrayList<Set<Vertex>>(4);
		for (int i = 0; i<4; i++)
			partitions.add(new HashSet<Vertex>());
		
		for (Vertex w: vertices) {
			DirectedEdge w_v = graph.getDirectedEdge(w, v);
			DirectedEdge v_w = graph.getDirectedEdge(v, w);
			
												// Four cases:
			if (w_v != null && v_w != null)		// (w,v),(v,w) \in V(G)
				partitions.get(0).add(w);
			else if (w_v != null)				// (w,v) \in V(G) /\ (v,w) \nin V(G)
				partitions.get(1).add(w);
			else if (v_w != null)				// (w,v) \nin V(G) /\ (v,w) \in V(G)
				partitions.get(2).add(w);
			else								// (w,v), (v,w) \nin V(G)
				partitions.get(3).add(w);				
		}
		
		return partitions ;
	}
	
	/**
	 * Algorithm 3.1 Compute M(g, v)
	 * 
	 * @param vertices AKA dom(g) in the reference paper, corresponds with the set of vertices of graph g
	 * @param v		   vertex used for partitioning
	 * @return
	 */
	private Collection<Set<Vertex>> partition(Collection<Vertex> vertices, Vertex v) {
		// L - Family of partition classes 
		Set<Set<Vertex>> l = new HashSet<Set<Vertex>>();
		// Z - Unprocessed outsiders
		Map<Set<Vertex>, Set<Vertex>> z = new HashMap<Set<Vertex>, Set<Vertex>>();
		
		// Place holder
		Set<Set<Vertex>> result = new LinkedHashSet<Set<Vertex>>();
		
		// Initially, there is one partition class S = V(g) \ {v} in L
		Set<Vertex> s = new HashSet<Vertex>(vertices);
		s.remove(v);
		l.add(s);
		
		// with Z(S) = {v}
		Set<Vertex> _v_ = new HashSet<Vertex>();
		_v_.add(v);
		z.put(s, _v_);
		
		while(!l.isEmpty()) {
			// Remove S from L
			s = l.iterator().next(); l.remove(s);
			
			// Let w be an arbitrary member of Z(S)
			Vertex w = z.get(s).iterator().next();
			
			// Partition S into maximal subsets that are not distinguished by w
			// -- for each resulting subset W
			for (Set<Vertex> W: partitionSubsets(s, w)) {
				if (W.isEmpty()) continue;
				
				// Let Z(W) = (S \ W) \cup Z(S) \ {w}
				Set<Vertex> tmp = new HashSet<Vertex>(s);
				tmp.removeAll(W);
				tmp.addAll(z.get(s));
				tmp.remove(w);
				
				if (!tmp.isEmpty()) {
					// Make W a member of L
					l.add(W);
					z.put(W, tmp); // Actual assignment to Z
				} else
					result.add(W);
			}
		}
		
		return result;
	}

	/**
	 * Algorithm 6.1 Compute the PRIME TREE FAMILY (aka Modular Decomposition Tree) for
	 * an arbitrary two-structure.
	 * 
	 * @param dom
	 * @return
	 */
	private MDTNode decompose(Collection<Vertex> dom) {
		if (dom.size() == 0) return null; // Nothing to do
		
		// Select one vertex from dom
		Vertex v = dom.iterator().next();
		
		// Create a node in the MDT
		MDTNode t = new MDTNode(dom, v);
		
		// Dom is a singleton, then t is a TRIVIAL
		if (dom.size() == 1) return t;
		
		Collection<Set<Vertex>> m = partition(dom, v);
		
		ComponentGraph gpp = new ComponentGraph(graph, m, v);
		MDTNode u = t;
		
		while (gpp.getVertices().size() > 0) {
			Set<Vertex> tmp = gpp.getPartitionUnion();
			tmp.add(v);
			u.setValue(tmp);
			
			tmp = new HashSet<Vertex>();
			tmp.add(v);
			MDTNode w = new MDTNode(tmp, v);
			u.addChild(w);
			Set<Vertex> sinks = gpp.getSinkNodes();
			Set<Set<Vertex>> F = gpp.getPartitions(sinks);
			gpp.removeVertices(sinks);
			
			if (sinks.size() == 1 && F.size() > 1)
				u.setType(NodeType.PRIMITIVE);
			else {
				if (F.size() < 1) {
					System.out.println("Sinks.size() " + sinks.size());
					System.out.println(F);
					System.exit(-1);
				}
				Vertex x = F.iterator().next().iterator().next();
				
				if ((graph.getDirectedEdge(v, x) != null && graph.getDirectedEdge(x, v) != null) ||
						(!(graph.getDirectedEdge(v, x) != null) && !(graph.getDirectedEdge(x, v) != null))) {
					u.setType(NodeType.COMPLETE);
					u.setColor(graph.getDirectedEdge(v, x) != null ? 1 : 0);
				} else
					u.setType(NodeType.LINEAR);
			}
			
			for (Set<Vertex> partition: F) {	
				MDTNode root = decompose(partition);
				if (((u.getType() == NodeType.COMPLETE && root.getType() == NodeType.COMPLETE) ||
						(u.getType() == NodeType.LINEAR && root.getType() == NodeType.LINEAR)) &&
						u.getColor() == root.getColor())
					u.addChildren(root.getChildren());
				else
					u.addChild(root);
			}
			u = w;
		}
		return t;
	}
	
	public String toString() {
		return root.toString();
	}
	
	public MDTNode getRoot() {
		return root;
	}
}
