import java.util.ArrayList;

public class Taxi {

	
	ArrayList<Ride> assign = new ArrayList<>();
	
	
	Taxi () {
		
	}
	
	
	
	int evaluateAssignment() {
		int tX = 0;
		int tY = 0;
		int time = 0;
		int timeOfArrival = 0;
		
		for (Ride ride : assign) {
			timeOfArrival = time + Math.abs(ride.a-tX) + Math.abs(ride.b-tY);
			
		}

		
		return 0;
	}

	
}
