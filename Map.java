import java.io.BufferedReader;
import java.util.Stack;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.ArrayList;

public class Map {

	//private class attributes
	private Node start;
	private Node end;
	private boolean visited = true;
	private boolean unvisited = false;
	private Graph g;
	private Node previous;
	private Node current;
	private int scaleFactor;
	private int mapWidth;
	private int mapLength;
	private int busChanges;
	private Stack s;
	private Edge ed;
	private Node e;
	private Node path[];
	private int numOfBusChange[];
	private String busLine[];

	// constructor
	@SuppressWarnings("unused")
	public Map(String inputFile) throws NumberFormatException, IOException, GraphException, MapException {

		// counter to keep track of where we are in file
		int ctr = 0;

		// String to store input line-by-line
		String str;

		// read from file
		BufferedReader input;
		input = new BufferedReader(new FileReader(inputFile));

		//if input file contains nothing, throw exception
		if (input == null) {
			throw new MapException("Invalid Map");
		}

		//read first 4 lines to get non-map building values from file
		scaleFactor = Integer.parseInt(input.readLine());
		mapWidth = Integer.parseInt(input.readLine());
		mapLength = Integer.parseInt(input.readLine());
		busChanges = Integer.parseInt(input.readLine());

		//create a graph of appropriate size
		g = new Graph(mapWidth * mapLength);

		//char to keep track of whether or not we're at a letter in the text file
		char letter = 0;

		// loop while input file has lines
		while ((str = input.readLine()) != null) {

			// loop through input file
			for (int i = 0; i < str.length(); i++) {

				//CHECKING EVEN LINES
				if (ctr % 2 == 0) {

					if (str.charAt(i) == '0') {
						// then assign node to be start point
						//calculate where in graph node would be
						start = g.getNode(((ctr / 2) * this.mapWidth) + (i / 2));

						if (letter != 0) {
							//if it's not a letter, create node to attach to 'start'
							Node prev = g.getNode(((ctr / 2) * this.mapWidth) + (i / 2) - 1);
							g.insertEdge(start, prev, letter + "");

							//reset letter counter
							letter = 0;
						}
					}

					else if (str.charAt(i) == '1') {
						// assign end point
						end = g.getNode(((ctr / 2) * this.mapWidth) + (i / 2));

						if (letter != 0) {

							//if not a letter, attach previous and current nodes with edge
							Node prev = g.getNode(((ctr / 2) * this.mapWidth) + (i / 2) - 1);
							g.insertEdge(end, prev, letter + "");
							letter = 0;
						}
					}

					else if (str.charAt(i) == '+') {
						// create a standard node with value from
						// where it would be in the node list
						Node n = g.getNode(((ctr / 2) * this.mapWidth) + (i / 2));

						if (letter != 0) {

							//attach previous and standard node, reset letter counter
							Node prev = g.getNode(((ctr / 2) * this.mapWidth) + (i / 2) - 1);
							g.insertEdge(n, prev, letter + "");
							letter = 0;

						}
					}

					else if (str.charAt(i) == ' ') {
						// if there's a space, there's no letter so letter=0
						letter = 0;
					}

					else {
						// store the letter at current spot
						letter = str.charAt(i);
					}
				}

				//CHECKING ODD LINES
				else {

					// connect bus lines to line above and line below
					// if the character is not a space (or is a letter)
					if (!(str.charAt(i) == ' ')) {

						// calculate nodes to attach
						// find previous node
						previous = g.getNode(((ctr - 1) / 2) * this.mapWidth + (i / 2));

						// find current node
						current = g.getNode(((ctr + 1) / 2) * this.mapWidth + (i / 2));

						// insert edge in graph between these 2 nodes
						g.insertEdge(previous, current, str.charAt(i) + "");
					}
				}
			} 
			
			//reset letter and increment counter
			letter = 0;
			ctr++;
		} 
		//close the input file
		input.close();
	}

	//method to return a map's graph
	Graph getGraph() throws MapException {
		
		//if graph exists, return it
		if (g != null) {
			return g;
		}
		else
			throw new MapException("Graph does not exist!");
	}

	//public method to find path between start and end nodes
	public Iterator findPath() {
		try {

			//create stack to hold nodes
			s = new Stack<Node>();

			//array to hold bus lines
			//set start index to be null
			busLine = new String[mapWidth*mapLength];
			busLine[start.getName()] = "";
			
			//array to hold number of bus changes
			//set start index to be null
			numOfBusChange = new int[mapWidth*mapLength];
			numOfBusChange[start.getName()] = 0; 
			
			//array to hold nodes in path
			//set start index to be null
			path = new Node[mapWidth*mapLength];
			path[start.getName()] = null;
			
			//if a path exists between start and end
			if (DFSpath(start, end) == true) {
				Node current = end;
				
				//create an array list to hold path
				ArrayList<Node> graphPath = new ArrayList<Node>();
				//add the last node to the list
				graphPath.add(end);
				
				//loop through while path array is not null
				while(path[current.getName()] != null){

					//add each node to array list in reverse order
					current = path[current.getName()];
					graphPath.add(current);
				}

				//reverse the nodes in the list (since we added them end > start)
				Collections.reverse(graphPath);
				//return its iterator
				return graphPath.iterator();
				
			} else if (DFSpath(start, end) == false) {
				// return null if there's no path
				return null;
			}
			
		} catch (GraphException e) {
			return null;
		}
		
		return null;
	}

	private boolean DFSpath(Node b, Node u) throws GraphException {

		// set start node to visited & add it to stack
		b.setMark(visited);
		s.push(b);
		
		//loop while the stack has contents
		while (!s.empty()) {
			
			//create node from top of stack
			Node v = (Node) s.pop();
					
			//if this node = start node, return true (path found)
			if (v.getName() == u.getName()) {
				return true;
			}

			//create iterator to represent incident edges on top-of-stack node
			Iterator incEdges = g.incidentEdges(v);

			//loop while this iterator has contents
			while (incEdges.hasNext()) {
				
				//get first incident edge
				ed = (Edge) incEdges.next();
				
				//get second end point of this incident edge
				e = ed.secondEndpoint();
				
				//if this next node = start, make it the 1st end point
				if (e.getName() == v.getName()) {
					e = ed.firstEndpoint();
				}

				// if this node has not been visited, 
				if (e.getMark() == unvisited) {

					//if bus array at index from top of stack is null, assign it to be the bus line
					String busline = ed.getBusLine();
					if(busLine[v.getName()] == "") {
						busLine[v.getName()] = busline;
					}
					
					//get new number of bus changes from bus change array
					int newNumofBusChange = numOfBusChange[v.getName()];
					
					//if edge's bus line is not stored in the array at start node's spot, 
					//increment bus changes
					if(!busline.equals(busLine[v.getName()])) {
						newNumofBusChange++;
					}
					
					//if our bus changes is <= number from text file,
					if(newNumofBusChange <= busChanges){
						
						//set end point node to visited
						e.setMark(visited);
						
						//add top of stack node to path array
						path[e.getName()] = v;
						
						//assign bus line to array at end point spot
						busLine[e.getName()] = busline;
						
						//update number of bus changes remaining
						numOfBusChange[e.getName()] = newNumofBusChange;
						
						//push end point node to stack
						s.push(e);
					}
				}
			}

		}
		//else, there is no path --> return false
		return false;
	}

} // end of file