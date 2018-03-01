import javafx.util.Pair;

import java.io.FileReader;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main {
	
	private Scanner sc = null;	
	
	static int numberOfRows; // < 10^4
	static int numberOfColumns; // < 10^4
	static int amountOfVehicles;  // < 10^3
	static int amountOfRides; // < 10^4
	static int goodStartBonus; // < 10^4
	static int stepLimit; // < 10^9
	
	Ride[] rides;
	Taxi[] taxis;

	Main() {
		try {
			sc = new Scanner(new FileReader("Input/" + "e_high_bonus.in"));
			
			numberOfRows = sc.nextInt();
			numberOfColumns = sc.nextInt();
			amountOfVehicles = sc.nextInt();
			amountOfRides = sc.nextInt();
			goodStartBonus = sc.nextInt();
			stepLimit = sc.nextInt();
			
			rides = new Ride[amountOfRides];
			taxis = new Taxi[amountOfVehicles];
			for(int i=0; i<amountOfVehicles; i++) {
				taxis[i] = new Taxi(i, goodStartBonus);
			}

			int a, b, x, y, start, finish;
			for (int rideID = 0; rideID < amountOfRides; rideID++) {
				a = sc.nextInt();
				b = sc.nextInt();
				x = sc.nextInt();
				y = sc.nextInt();
				start = sc.nextInt();
				finish = sc.nextInt();
				rides[rideID] = new Ride(rideID, a, b, x, y, start, finish);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sc.close();
		}

		if (false) {
			eAlgorithm();
		}

		getOutput();
	}

	public void eAlgorithm() {
		int searchdepth = 100;
		for(Taxi taxi : taxis) {
			int t = 0;
			int rideInt = 0;
			while (t < stepLimit) {
				while(rides[rideInt].handled) {
					rideInt++;
				}
				int bestRide = rideInt;
				int earliestDone = Integer.MAX_VALUE;
				for(int r = 0; r < searchdepth; r++) {
					Ride ride = rides[rideInt + r];
					if(ride.handled) {
						continue;
					}
					int driveToTime = ride.a - taxi.posx;
				}
			}
		}

	}

	void getOutput() {
		for (int taxiID = 0; taxiID < amountOfVehicles; taxiID++) {
			taxis[taxiID].returnAssignment();
		}
	}

	public static void main(String[] args) {
		new Main();
	}
}
