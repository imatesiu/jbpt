package org.jbpt.test.graph;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.jbpt.algo.tree.tctree.TCTree;
import org.jbpt.algo.tree.tctree.TCTreeNode;
import org.jbpt.algo.tree.tctree.TCType;
import org.jbpt.graph.DirectedEdge;
import org.jbpt.graph.MultiDirectedGraph;
import org.jbpt.hypergraph.abs.Vertex;
import org.jbpt.pm.ControlFlow;
import org.jbpt.pm.FlowNode;
import org.jbpt.utils.IOUtils;

public class TCTreeTest extends TestCase {
	
	/**
	 * Test of a graph from the WS-FM'10 paper:
	 * Artem Polyvyanyy, Jussi Vanhatalo, Hagen V�lzer: Simplified Computation and Generalization of the Refined Process Structure Tree. WS-FM 2010: 25-41
	 */
	/*public void testWSFM() {
		MultiDirectedGraph g = new MultiDirectedGraph();
		
		Vertex s = new Vertex("s");
		Vertex t = new Vertex("t");
		Vertex u = new Vertex("u");
		Vertex v = new Vertex("v");
		Vertex w = new Vertex("w");
		Vertex x = new Vertex("x");
		Vertex y = new Vertex("y");
		Vertex z = new Vertex("z");
		
		g.addEdge(s,u);
		g.addEdge(u,v);
		g.addEdge(u,w);
		g.addEdge(v,w);
		g.addEdge(v,x);
		g.addEdge(w,x);
		g.addEdge(x,y);
		g.addEdge(y,z);
		g.addEdge(y,z);
		g.addEdge(y,z);
		g.addEdge(z,t);
		DirectedEdge backEdge = g.addEdge(t,s);
		
		IOUtils.toFile("graph.dot", g.toDOT());
		
		long start = System.nanoTime();
		TCTree<DirectedEdge,Vertex> tctree = new TCTree<DirectedEdge,Vertex>(g,backEdge);
		long end = System.nanoTime();
		System.out.println("WSFM\t"+((double) end-start) / 1000000000);
		
		Set<DirectedEdge> edges = new HashSet<DirectedEdge>();
		for (TCTreeNode<DirectedEdge,Vertex> node : tctree.getVertices()) {
			if (node.getType()==TCType.POLYGON) {
				assertEquals(6, node.getSkeleton().getVertices().size());
				assertEquals(4, node.getSkeleton().getOriginalEdges().size());
				assertEquals(2, node.getSkeleton().getVirtualEdges().size());
			}
				
			if (node.getType()==TCType.BOND) {
				assertEquals(2, node.getSkeleton().getVertices().size());
				assertEquals(3, node.getSkeleton().getOriginalEdges().size());
				assertEquals(1, node.getSkeleton().getVirtualEdges().size());
			}
			
			if (node.getType()==TCType.RIGID) {
				assertEquals(4, node.getSkeleton().getVertices().size());
				assertEquals(5, node.getSkeleton().getOriginalEdges().size());
				assertEquals(1, node.getSkeleton().getVirtualEdges().size());
			}
			
			assertEquals(true,g.getEdges().containsAll(node.getSkeleton().getOriginalEdges()));
			edges.addAll((node.getSkeleton().getOriginalEdges()));
			
			IOUtils.toFile(node.getName() + ".dot",node.getSkeleton().toDOT());
		}
		
		assertEquals(true,edges.containsAll(g.getEdges()));
		assertEquals(true,g.getEdges().containsAll(edges));
		assertEquals(3,tctree.getTriconnectedComponents().size());
		assertEquals(1,tctree.getTriconnectedComponents(TCType.BOND).size());
		assertEquals(1,tctree.getTriconnectedComponents(TCType.RIGID).size());
		assertEquals(1,tctree.getTriconnectedComponents(TCType.POLYGON).size());
		
		IOUtils.toFile("tree.dot", tctree.toDOT());
	}
	
	public void testNULL() {
		MultiDirectedGraph g = null;
		long start = System.nanoTime();
		TCTree<DirectedEdge,Vertex> tctree = new TCTree<DirectedEdge,Vertex>(g);
		long end = System.nanoTime();
		System.out.println("NULL\t"+((double) end-start) / 1000000000);
		
		assertEquals(0,tctree.getTriconnectedComponents().size());
	}
	
	public void testSingleVertex() {
		MultiDirectedGraph g = new MultiDirectedGraph();
		g.addVertex(new Vertex("A"));
		long start = System.nanoTime();
		TCTree<DirectedEdge,Vertex> tctree = new TCTree<DirectedEdge,Vertex>(g);
		long end = System.nanoTime();
		System.out.println("1V\t"+((double) end-start) / 1000000000);
		
		assertEquals(0,tctree.getTriconnectedComponents().size());
	}
	
	public void testSingleEdge() {
		MultiDirectedGraph g = new MultiDirectedGraph();
		g.addEdge(new Vertex("A"),new Vertex("B"));
		long start = System.nanoTime();
		TCTree<DirectedEdge,Vertex> tctree = new TCTree<DirectedEdge,Vertex>(g);
		long end = System.nanoTime();
		System.out.println("1E\t"+((double) end-start) / 1000000000);
		
		assertEquals(0,tctree.getTriconnectedComponents().size());
	}
	
	public void testSingleBond() {
		MultiGraph g = new MultiGraph();
		Vertex a = new Vertex("A");
		Vertex b = new Vertex("B");
		g.addEdge(a,b);
		g.addEdge(a,b);
		g.addEdge(a,b);
		g.addEdge(a,b);
		g.addEdge(a,b);
		long start = System.nanoTime();
		TCTree<Edge,Vertex> tctree = new TCTree<Edge,Vertex>(g);
		long end = System.nanoTime();
		System.out.println("1BOND\t"+((double) end-start) / 1000000000);
		
		assertEquals(1,tctree.getTriconnectedComponents().size());
		assertEquals(1,tctree.getTriconnectedComponents(TCType.BOND).size());
		for (TCTreeNode<Edge,Vertex> node : tctree.getVertices()) {
			IOUtils.toFile(node.getName() + ".dot",node.getSkeleton().toDOT());
		}
	}
	
	public void testSingleBondAndSingleVertex() {
		MultiGraph g = new MultiGraph();
		Vertex a = new Vertex("A");
		Vertex b = new Vertex("B");
		g.addEdge(a,b);
		g.addEdge(a,b);
		g.addEdge(a,b);
		g.addEdge(a,b);
		g.addEdge(a,b);
		g.addVertex(new Vertex("C"));
		long start = System.nanoTime();
		TCTree<Edge,Vertex> tctree = new TCTree<Edge,Vertex>(g);
		long end = System.nanoTime();
		System.out.println("1B1V\t"+((double) end-start) / 1000000000);
		
		assertEquals(1,tctree.getTriconnectedComponents().size());
		assertEquals(1,tctree.getTriconnectedComponents(TCType.BOND).size());
		for (TCTreeNode<Edge,Vertex> node : tctree.getVertices()) {
			IOUtils.toFile(node.getName() + ".dot",node.getSkeleton().toDOT());
		}
	}
*/
		
	public void testSimpleGraph() {
		//		  --- t3 --- t4 ---
		//		  |				  |
		// t1 -- s2 ------------ j5 -- t9
		//	.	  |				  |		.
		//	.	  |_ s6 ---- j7 __|		.
		// 	.		  |_ t8 _|			.
		//	............................. 
		
		MultiDirectedGraph g = new MultiDirectedGraph();
		
		Vertex t1 = new Vertex("1");
		Vertex t3 = new Vertex("3");
		Vertex t4 = new Vertex("4");
		Vertex t8 = new Vertex("8");
		Vertex t9 = new Vertex("9");
		
		Vertex s2 = new Vertex("2");
		Vertex s6 = new Vertex("6");
		Vertex j7 = new Vertex("7");
		Vertex j5 = new Vertex("5");
		
		g.addEdge(t1, s2);
		g.addEdge(s2, t3);
		g.addEdge(s2, s6);
		g.addEdge(s2, j5);
		g.addEdge(t3, t4);
		g.addEdge(t4, j5);
		g.addEdge(s6, j7);
		g.addEdge(s6, t8);
		g.addEdge(t8, j7);
		g.addEdge(j7, j5);
		g.addEdge(j5, t9);
		DirectedEdge backEdge = g.addEdge(t9, t1);
		
		IOUtils.toFile("graph.dot", g.toDOT());
		TCTree<DirectedEdge,Vertex> tctree = new TCTree<DirectedEdge,Vertex>(g,backEdge);
		IOUtils.toFile("graph2.dot", g.toDOT());
		IOUtils.toFile("tree.dot", tctree.toDOT());
		
		Set<DirectedEdge> edges = new HashSet<DirectedEdge>();
		for (TCTreeNode<DirectedEdge,Vertex> node : tctree.getVertices()) {
			assertEquals(true,g.getEdges().containsAll(node.getSkeleton().getOriginalEdges()));
			edges.addAll((node.getSkeleton().getOriginalEdges()));
			
			IOUtils.toFile(node.getName() + ".dot",node.getSkeleton().toDOT());
		}
		
		assertEquals(true,edges.containsAll(g.getEdges()));
		assertEquals(true,g.getEdges().containsAll(edges));
		System.out.println(tctree.getTCTreeNodes(TCType.BOND).size());
		System.out.println(tctree.getTCTreeNodes(TCType.POLYGON).size());
		System.out.println(tctree.getTCTreeNodes(TCType.RIGID).size());
		
		/*assertEquals(6,tctree.getTriconnectedComponents().size());
		assertEquals(2,tctree.getTriconnectedComponents(TCType.BOND).size());
		assertEquals(0,tctree.getTriconnectedComponents(TCType.RIGID).size());
		assertEquals(4,tctree.getTriconnectedComponents(TCType.POLYGON).size());*/
	}
	
	/*public void testTrivialCase() {
		System.out.println("============================");
		System.out.println("Trivial case");
		System.out.println("============================");
		
		Graph g = new Graph();
		
		Vertex v1 = new Vertex("1");
		Vertex v2 = new Vertex("2");
		
		g.addEdge(v1, v2);
		
		TCTree<Edge, Vertex> tc = new TCTree<Edge, Vertex>(g);
		
		for (TCTreeNode<Edge, Vertex> node:tc.getVertices()) {
			System.out.println(node.getName() + ": " + node.getSkeleton().getEdges());
		}
		
		assertEquals(0, tc.getVertices(TCType.R).size());
		assertEquals(0, tc.getVertices(TCType.B).size());
		assertEquals(0, tc.getVertices(TCType.P).size());
		assertEquals(1, tc.getVertices(TCType.T).size());
	}
	
	public void testGraphWithR() {
		// create process model graph
		ProcessModel p = new ProcessModel();
		
		Activity t1 = new Activity("1");
		Activity t3 = new Activity("3");
		Activity t4 = new Activity("4");
		Activity t10 = new Activity("10");

		XorGateway s2 = new XorGateway("2");
		XorGateway s6 = new XorGateway("6");
		XorGateway s7 = new XorGateway("7");
		XorGateway j5 = new XorGateway("5");
		XorGateway j8 = new XorGateway("8");
		XorGateway j9 = new XorGateway("9");
		
		p.addControlFlow(t1, s2);
		p.addControlFlow(s2, t3);
		p.addControlFlow(s2, s6);
		p.addControlFlow(t3, t4);
		p.addControlFlow(t4, j5);
		p.addControlFlow(s6, s7);
		p.addControlFlow(s6, j9);
		p.addControlFlow(s7, j9);
		p.addControlFlow(s7, j8);
		p.addControlFlow(j9, j8);
		p.addControlFlow(j8, j5);
		p.addControlFlow(j5, t10);
		p.addControlFlow(t10, t1);
		
		TCTree<ControlFlow<FlowNode>, FlowNode> tc = new TCTree<ControlFlow<FlowNode>, FlowNode>(p);
		
		assertEquals(tc.getVertices(TCType.B).size(), 1);
		assertEquals(tc.getVertices(TCType.R).size(), 1);
		assertEquals(tc.getVertices(TCType.P).size(), 3);
	}
	
	public void testSimpleR() {
		//		  ----- s3 -----------
		//		  |		 |			 |
		// t1 -- s2 --- j4 -- t5 -- j6 -- t7
		//	.	  						   .
		//  ................................ 
		
		ProcessModel p = new ProcessModel();
		
		Activity t1 = new Activity("n1");
		Activity t5 = new Activity("n5");
		Activity t7 = new Activity("n7");

		XorGateway s2 = new XorGateway("n2");
		XorGateway j6 = new XorGateway("n6");
		XorGateway s3 = new XorGateway("n3");
		XorGateway j4 = new XorGateway("n4");
		
		p.addControlFlow(t1, s2);
		p.addControlFlow(s2, s3);
		p.addControlFlow(s2, j4);
		p.addControlFlow(s3, j4);
		p.addControlFlow(s3, j6);
		p.addControlFlow(j4, t5);
		p.addControlFlow(t5, j6);
		p.addControlFlow(j6, t7);
		ControlFlow backEdge = p.addControlFlow(t7, t1);
		
		TCTree<ControlFlow<FlowNode>, FlowNode> tc = new TCTree<ControlFlow<FlowNode>,FlowNode>(p, backEdge);
		
		for (TCTreeNode<ControlFlow<FlowNode>,FlowNode> n:tc.getVertices()) {
			System.out.println(String.valueOf(n) + ": " + n.getSkeleton().getEdges());
			System.out.println(String.valueOf(n) + ": " + n.getSkeleton().getVirtualEdges());
		}
		
		assertEquals(0, tc.getVertices(TCType.B).size());
		assertEquals(1, tc.getVertices(TCType.R).size());
		assertEquals(2, tc.getVertices(TCType.P).size());
	}
	
	public void testGraphWithR2() {
		// create process model graph
		ProcessModel p = new ProcessModel();
		
		Activity t1 = new Activity("1");
		Activity t3 = new Activity("3");
		Activity t4 = new Activity("4");
		Activity t10 = new Activity("10");
		Activity t11 = new Activity("11");

		XorGateway s2 = new XorGateway("2");
		XorGateway s6 = new XorGateway("6");
		XorGateway s7 = new XorGateway("7");
		XorGateway j5 = new XorGateway("5");
		XorGateway j8 = new XorGateway("8");
		XorGateway j9 = new XorGateway("9");
		
		p.addControlFlow(t1, s2);
		p.addControlFlow(s2, t3);
		p.addControlFlow(s2, s6);
		p.addControlFlow(t3, t4);
		p.addControlFlow(t4, j5);
		p.addControlFlow(s6, s7);
		p.addControlFlow(s6, j9);
		p.addControlFlow(s7, j9);
		p.addControlFlow(s7, j8);
		p.addControlFlow(j9, t11);
		p.addControlFlow(t11, j8);
		p.addControlFlow(j8, j5);
		p.addControlFlow(j5, t10);
		ControlFlow backEdge = p.addControlFlow(t10, t1);
		
		TCTree<ControlFlow<FlowNode>, FlowNode> tc = new TCTree<ControlFlow<FlowNode>, FlowNode>(p, backEdge);
		
		for (TCTreeNode<ControlFlow<FlowNode>, FlowNode> n:tc.getVertices()) {
			System.out.println(String.valueOf(n) + ": " + n.getSkeleton().getEdges());
			System.out.println(String.valueOf(n) + ": " + n.getSkeleton().getVirtualEdges());
		}
		
		assertEquals(tc.getVertices(TCType.B).size(), 1);
		assertEquals(tc.getVertices(TCType.R).size(), 1);
		assertEquals(tc.getVertices(TCType.P).size(), 4);
	}
	
	public void testType1SepPair() {
		// 		   ---- S2 -----------------
		//		   |	 |				   |
		//	T1 -- S1 -- J1 -- T2 -- S3 -- J2 -- T3
		//	 .			 |___________|			 .
		//	 ..................................... 
		
		ProcessModel p = new ProcessModel();
		
		Activity t1 = new Activity("T1");
		Activity t2 = new Activity("T2");
		Activity t3 = new Activity("T3");
		
		XorGateway s1 = new XorGateway("S1");
		XorGateway s2 = new XorGateway("S2");
		XorGateway s3 = new XorGateway("S3");
		XorGateway j1 = new XorGateway("J1");
		XorGateway j2 = new XorGateway("J2");
		
		p.addControlFlow(t1, s1);
		p.addControlFlow(s1, s2);
		p.addControlFlow(s1, j1);
		p.addControlFlow(s2, j1);
		p.addControlFlow(s2, j2);
		p.addControlFlow(j1, t2);
		p.addControlFlow(t2, s3);
		p.addControlFlow(s3, j1);
		p.addControlFlow(s3, j2);
		p.addControlFlow(j2, t3);
		p.addControlFlow(t3, t1);
		
		TCTree<ControlFlow<FlowNode>, FlowNode> tc = new TCTree<ControlFlow<FlowNode>, FlowNode>(p);
		
		//assertEquals(tc.getVertices().size(), 5);
		//assertEquals(tc.getEdges().size(), 4);
		assertEquals(3, tc.getVertices(TCType.P).size());
		assertEquals(1, tc.getVertices(TCType.B).size());
		assertEquals(1, tc.getVertices(TCType.R).size());
		
	}
	
	public void testSomeBehavior() {
		// create process model graph
		ProcessModel p = new ProcessModel();
		
		Activity t1 = new Activity("T1");
		Activity t2 = new Activity("T2");
		Activity t3 = new Activity("T3");
		Activity t4 = new Activity("T4");
		Activity t5 = new Activity("T5");
		Activity t6 = new Activity("T6");
		Activity t7 = new Activity("T7");
		Activity t8 = new Activity("T8");
		Activity t9 = new Activity("T9");
		Activity t10 = new Activity("T10");
		Activity t11 = new Activity("T11");
		Activity t12 = new Activity("T12");
		Activity t13 = new Activity("T13");
		Activity t14 = new Activity("T14");
		
		XorGateway s1 = new XorGateway("S1");
		XorGateway s2 = new XorGateway("S2");
		XorGateway s3 = new XorGateway("S3");
		XorGateway j1 = new XorGateway("J1");
		XorGateway j2 = new XorGateway("J2");
		XorGateway j3 = new XorGateway("J3");
		
		p.addControlFlow(t1, s1);
		p.addControlFlow(s1, t2);
		p.addControlFlow(s1, t3);
		p.addControlFlow(s1, t10);
		p.addControlFlow(t2, s2);
		p.addControlFlow(t3, j1);
		p.addControlFlow(t10, t11);
		p.addControlFlow(s2, t4);
		p.addControlFlow(s2, t6);
		p.addControlFlow(s2, t7);
		p.addControlFlow(s2, j1);
		p.addControlFlow(j1, t9);
		p.addControlFlow(t4, t5);
		p.addControlFlow(t9, s3);
		p.addControlFlow(s3, j1);
		p.addControlFlow(t11, t12);
		p.addControlFlow(t12, j3);
		p.addControlFlow(s3, j2);
		p.addControlFlow(j2, t13);
		p.addControlFlow(t7, t8);
		p.addControlFlow(t8, j2);
		p.addControlFlow(t5, j3);
		p.addControlFlow(t6, j3);
		p.addControlFlow(t13, j3);
		p.addControlFlow(j3, t14);
		p.addControlFlow(t14, t1);
		
		TCTree<ControlFlow<FlowNode>, FlowNode> tc = new TCTree<ControlFlow<FlowNode>, FlowNode>(p);
		
		for (TCTreeNode<ControlFlow<FlowNode>, FlowNode> n:tc.getVertices()) {
			System.out.println(String.valueOf(n) + ": " + n.getSkeleton().getEdges());
			System.out.println(String.valueOf(n) + ": " + n.getBoundaryNodes());
		}
		
		System.out.println("Vertices: " + tc.countVertices());
		System.out.println("Edges: " + tc.countEdges());
		
		assertEquals(3, tc.getVertices(TCType.B).size());
		assertEquals(1, tc.getVertices(TCType.R).size());
		assertEquals(10, tc.getVertices(TCType.P).size());
	}*/
	
	/*public void testOneMoreComplexExample() {
		Graph g = new Graph();
		
		Vertex i = new Vertex("I");
		Vertex a = new Vertex("A");
		Vertex v18 = new Vertex("18");
		Vertex v51 = new Vertex("51");
		Vertex v6 = new Vertex("6");
		Vertex v56 = new Vertex("56");
		Vertex v55 = new Vertex("55");
		Vertex v33 = new Vertex("33");
		Vertex v46 = new Vertex("46");
		Vertex v38 = new Vertex("38");
		Vertex v37 = new Vertex("37");
		Vertex v10 = new Vertex("10");
		Vertex v3 = new Vertex("3");
		Vertex v42 = new Vertex("42");
		Vertex e = new Vertex("E");
		Vertex o = new Vertex("O");
		
		g.addEdge(i, a);
		g.addEdge(a, v18);
		g.addEdge(a, v51);
		g.addEdge(a, v6);
		g.addEdge(a, v33);
		g.addEdge(a, v42);
		g.addEdge(v18, e);
		g.addEdge(v51, v18);
		g.addEdge(v51, v6);
		g.addEdge(v51, v56);
		g.addEdge(v56, v55);
		g.addEdge(v56, e);
		g.addEdge(v55, v33);
		g.addEdge(v55, e);
		g.addEdge(v33, e);
		g.addEdge(v6, v46);
		g.addEdge(v46, v38);
		g.addEdge(v46, v10);
		g.addEdge(v38, v37);
		g.addEdge(v38, v10);
		g.addEdge(v37, v46);
		g.addEdge(v10, v3);
		g.addEdge(v3, v42);
		g.addEdge(v3, e);
		g.addEdge(v42, e);
		g.addEdge(e, o);
		g.addEdge(o, i);
		
		TCTree<Edge, Vertex> tc = new TCTree<Edge, Vertex>(g);
		
		for (TCTreeNode<Edge, Vertex> n:tc.getVertices()) {
			System.out.println(String.valueOf(n) + ": " + n.getSkeleton().getEdges());
		}
		
		assertEquals(4, tc.getVertices(TCType.P).size());
		assertEquals(2, tc.getVertices(TCType.B).size());
		assertEquals(1, tc.getVertices(TCType.R).size());
	}
	
	public void testNestedB() {
		Graph g = new Graph();
		
		Vertex v1 = new Vertex("1");
		Vertex v2 = new Vertex("2");
		Vertex v3 = new Vertex("3");
		Vertex v4 = new Vertex("4");
		Vertex v5 = new Vertex("5");
		Vertex v6 = new Vertex("6");
		
		g.addEdge(v1, v2);
		g.addEdge(v2, v4);
		g.addEdge(v2, v5);
		g.addEdge(v4, v5);
		g.addEdge(v2, v3);
		g.addEdge(v3, v4);
		g.addEdge(v3, v6);
		g.addEdge(v6, v1);
		
		TCTree<Edge, Vertex> tc = new TCTree<Edge, Vertex>(g);
		
		for (TCTreeNode<Edge, Vertex> node:tc.getVertices()) {
			System.out.println(node.getName() + ": " + node.getSkeleton().getEdges());
		}
		
		assertEquals(0, tc.getVertices(TCType.R).size());
		assertEquals(2, tc.getVertices(TCType.B).size());
		assertEquals(3, tc.getVertices(TCType.P).size());
	}
	
	public void testTripleNestedB() {
		Graph g = new Graph();
		
		Vertex v1 = new Vertex("1");
		Vertex v2 = new Vertex("2");
		Vertex v3 = new Vertex("3");
		Vertex v4 = new Vertex("4");
		Vertex v5 = new Vertex("5");
		Vertex v6 = new Vertex("6");
		Vertex v7 = new Vertex("7");
		
		g.addEdge(v1, v2);
		g.addEdge(v2, v4);
		g.addEdge(v2, v5);
		g.addEdge(v4, v5);
		g.addEdge(v2, v3);
		g.addEdge(v3, v4);
		g.addEdge(v3, v6);
		g.addEdge(v2, v7);
		g.addEdge(v5, v7);
		g.addEdge(v6, v1);
		
		TCTree<Edge, Vertex> tc = new TCTree<Edge, Vertex>(g);
		
		for (TCTreeNode<Edge, Vertex> node:tc.getVertices()) {
			System.out.println(node.getName() + ": " + node.getSkeleton().getEdges());
		}
		
		assertEquals(0, tc.getVertices(TCType.R).size());
		assertEquals(3, tc.getVertices(TCType.B).size());
		assertEquals(4, tc.getVertices(TCType.P).size());
	}*/

}
