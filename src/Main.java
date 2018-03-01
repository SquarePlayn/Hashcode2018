import java.io.FileReader;
import java.util.Arrays;
import java.util.Comparator;
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

		sortRides();

		if (true) {
			eAlgorithm();
		}

		getOutput();
	}

	public void eAlgorithm() {
		int searchdepth = stepLimit;
		for(Taxi taxi : taxis) {
			int t = 0;
			int rideInt = 0;
			while (t < stepLimit) {
				while(rideInt < amountOfRides && rides[rideInt].handled) {
					rideInt++;
				}
				int bestRide = rideInt;
				int earliestDone = Integer.MAX_VALUE;
				for(int r = 0; r < searchdepth; r++) {
				    if(rideInt + r >= amountOfRides) {
				        break;
                    }
					Ride ride = rides[rideInt + r];
                    if (ride.start > earliestDone - 1)
                        break;
                    if(ride.handled)
                        continue;
                    int distanceToRide = Math.abs(ride.a - taxi.posx) + Math.abs(ride.b - taxi.posy);
                    if(ride.start < t + distanceToRide)
                        continue;
                    int finishTime = t + distanceToRide + ride.drivingDistance;
                    if(finishTime < earliestDone) {
                        earliestDone = finishTime;
                        bestRide = rideInt + r;
                    }
				}
				if(earliestDone != Integer.MAX_VALUE) {
                    Ride ride = rides[bestRide];
                    ride.handled = true;
                    taxi.assign.add(ride);
                    taxi.posx = ride.x;
                    taxi.posy = ride.y;
                    rideInt = bestRide + 1;
                    t = ride.start + ride.drivingDistance;
                } else {
				    t += searchdepth;
                }
			}
		}

	}

	void sortRides() {
        Arrays.sort(rides, new Comparator<Ride>() {
            @Override
            public int compare(Ride r1, Ride r2) {
                return r1.start - r2.start;
            }
        });
    }

	void getOutput() {
	    int score = 0;
		for (int taxiID = 0; taxiID < amountOfVehicles; taxiID++) {
			score += taxis[taxiID].returnAssignment();
		}
        System.out.println("Score: "+score);
	}

	public static void main(String[] args) {
		new Main();
	}
}
