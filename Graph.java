import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

//graph implemented with adjacency matrix
public class Graph implements GraphADT {

	//class attributes
	private int numNodes;
	// adjacency matrix to hold edges
	private Edge[][] adjMatrix;
	// array to hold nodes
	private Node[] list;

	//constructor
	Graph(int n) {

		numNodes = n;
		//n x n adjacency matrix
		adjMatrix = new Edge[n][n];
		//array to hold nodes
		list = new Node[n];

		//fill list with a node at each spot
		for (int i = 0; i < n; i++) {
			list[i] = new Node(i);
		}
	}

	@Override
	//method to add an edge to matrix
	public void insertEdge(Node nodeu, Node nodev, String busLine) throws GraphException {

		// if nodes not in graph, throw exception
		if (nodeu == null || nodev == null || nodeu.getName() < 0 || nodev.getName() < 0 
				|| nodeu.getName() >= numNodes || nodev.getName() >= numNodes) {
			throw new GraphException("Error: node(s) not present in graph");	
		}

		//if spot in adjacency matrix is not empty, there is already an edge --> throw exception
		if (adjMatrix[nodeu.getName()][nodev.getName()] != null) {
			throw new GraphException("Error: edge already exists");
		}

		//add edge to appropriate spot in adjacency matrix
		adjMatrix[nodeu.getName()][nodev.getName()] = new Edge(nodeu, nodev, busLine);
		
		//add its opposite edge (switch u and v) to keep matrix accurate
		adjMatrix[nodev.getName()][nodeu.getName()] = new Edge(nodev, nodeu, busLine);

	}

	@Override
	//method to return a node with given name
	public Node getNode(int name) throws GraphException {

		//check that node is valid, if not throw exception
		if (name > numNodes || name < 0) {
			throw new GraphException("Node not in graph");
		}
		
		// loop through list of nodes
		for (int i = 0; i < numNodes; i++) {

			//if you cannot find a match, throw exception
			if (list[i] == null) {
				throw new GraphException("Node not present");
			}
			
			//return node once you find it in list
			if (list[i].getName() == name) {
				return list[i];
			}
		}
		
		//if not found, return null
		return null;
	}

	@Override
	//method to return edges incident on given node
	public Iterator<?> incidentEdges(Node u) throws GraphException {

		//create array list to serve as iterator
		List<Edge> iter = new ArrayList<Edge>();
		
		//check if nodes are valid
		if (numNodes <= u.getName() || u.getName() < 0) {
			throw new GraphException("ERROR: Node is not in graph");
		}

		// loop through adjacency matrix
		for (int i = 0; i < numNodes; i++) {

			//if there is an edge stored in the adjacency matrix with node,
			if(adjMatrix[u.getName()][i] != null) {
				
				//add it to the list
				iter.add(adjMatrix[u.getName()][i]);
			}
		}
		//return contents of list via iterator
		return iter.iterator();
	}

	@Override
	//method to return edge between two given nodes
	public Edge getEdge(Node u, Node v) throws GraphException {

		//if either node is invalid, throw exception
		if (u == null || v == null || u.getName() >= numNodes || u.getName() < 0 
				|| v.getName() >= numNodes || v.getName() < 0) {
			throw new GraphException("ERROR: Nodes are not present in graph. Therefore there is no edge");
		}

		//if spot in matrix holds no edge, throw exception
		if ((adjMatrix[u.getName()][v.getName()]) == null) {
			throw new GraphException("No edge present");
		}

		//otherwise, as long as there is a bus line in the matrix return that spot
		if (adjMatrix[u.getName()][v.getName()].getBusLine() != null 
				|| adjMatrix[v.getName()][u.getName()].getBusLine() != null) {
			return adjMatrix[u.getName()][v.getName()];
		}

		return null;
	}

	@Override
	//method to determine whether or not two nodes are adjacent
	public boolean areAdjacent(Node u, Node v) throws GraphException {

		//if nodes are invalid, throw exception
		if(u == null  || v == null || u.getName() < 0 || v.getName() >= numNodes) 
			throw new GraphException("ERROR: Nodes are not adjacent");
		
		//if there is an edge at that spot in the matrix, return true
		if(adjMatrix[u.getName()][v.getName()] != null) return true;
		
		//else, they are not adjacent nodes
		return false;
	}
}