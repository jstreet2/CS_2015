
public class Edge {
	
//private attributes
	private Node endpt1;
	private Node endpt2;
	private String bus;
	
	//constructor
	//assigns each edge 2 end points (first and second) and a bus line
	Edge(Node u, Node v, String busline) {
		endpt1=u;
		endpt2=v;
		bus=busline;
	}
	
	//returns edge's 1st end point
	Node firstEndpoint() {
		return this.endpt1;
	}
	
	//returns edge's 2nd end point
	Node secondEndpoint() {
		return this.endpt2;
	}
	
	//returns edge's bus line
	String getBusLine() {
		return this.bus;
	}
}