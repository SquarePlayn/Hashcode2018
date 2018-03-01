import java.util.ArrayList;

public class Taxi {

	int taxiID, goodStartBonus;
	ArrayList<Ride> assign = new ArrayList<>();
	int posx, posy;
	int unavailable = 0;
	Taxi (int taxiID, int goodStartBonus) {
		this.taxiID = taxiID;
		this.goodStartBonus = goodStartBonus;
	}
	
	
	
	int evaluateAssignment() {
		int tX = 0;
		int tY = 0;
		int time = 0;
		int timeOfArrival = 0;
		int score = 0;	
		for (Ride ride : assign) {
			timeOfArrival = Math.max(time + Math.abs(ride.a-tX) + Math.abs(ride.b-tY), ride.start);
			tX = ride.x;
			tY = ride.y;
			if (timeOfArrival == ride.start) {
				score += goodStartBonus + ride.drivingDistance;
				time = timeOfArrival + ride.drivingDistance;
			} else if (timeOfArrival + ride.drivingDistance < ride.finish) {
				score += ride.drivingDistance;
				time = timeOfArrival + ride.drivingDistance;
			} else {
				throw new IllegalArgumentException("Invalid assignment for taxi(" + taxiID + ").");
			}
		}
		
		return score;
	}
	
	int returnAssignment() {
		int tX = 0;
		int tY = 0;
		int time = 0;
		int timeOfArrival = 0;
		int score = 0;	
		System.out.print(assign.size() + " ");
		for (Ride ride : assign) {
			System.out.print(ride.rideID + " ");
			timeOfArrival = Math.max(time + Math.abs(ride.a-tX) + Math.abs(ride.b-tY), ride.start);
			tX = ride.x;
			tY = ride.y;
			if (timeOfArrival == ride.start) {
				score += goodStartBonus + ride.drivingDistance;
				time = timeOfArrival + ride.drivingDistance;
			} else if (timeOfArrival + ride.drivingDistance < ride.finish) {
				score += ride.drivingDistance;
				time = timeOfArrival + ride.drivingDistance;
			} else {
				throw new IllegalArgumentException("Invalid assignment for taxi(" + taxiID + ") on ride + "+ride.rideID+".");
			}
		}
		System.out.println("");
		
		return score;
	}

	
}
