
import java.util.ArrayList;

public class cellFloor {
	boolean visited = false;
	boolean calculated = false;
	int sumMinPass = Integer.MAX_VALUE;
	
	String type ;
	public cellFloor(String type) {
		this.type = type;
	}
	public void changeSumPass(int sum) {
		this.sumMinPass = sum;	
	}
	public void isVisited() {
		visited = true;
	}

}
