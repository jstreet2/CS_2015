
public class Node {
	
	//private attributes
	private boolean my_mark;
	private int my_name;
	private boolean visited=true;
	private boolean unvisited=false;
	
	//constructor
	Node(int name) {
		my_name=name;
		my_mark=false;
	}
	
	//sets a node to "visited" or "unvisited 
	void setMark(boolean mark) {
		my_mark=mark;
	}
	
	//returns true/false if node is visited/unvisited
	boolean getMark() {
		return this.my_mark;
	}
	
	//returns number assigned to node between 0 and graph size
	int getName() {
		return this.my_name;
	}
}