import java.io.FileReader;
import java.util.*;

public class Main {

    private int backcutoff = 7550;
    private int optimalScore = 0;
    private int optimalCutoff = 0;
    private int timeCutoff = 1500;
    private ArrayList<ArrayList> bestSolution;

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
			sc = new Scanner(new FileReader("Input/"+"c_no_hurry.in"));

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
			cAlgorithm();
		}


		if(false) {
		    scoreOverTimeAlgorithm();
        }

        if(false) {
            //centerFinder();
            for(int i=0; i<200; i++) {
                scoreOverTimeAlgorithm();
                getOutput();
                backcutoff += 10;
                for(int p=0; p<amountOfVehicles; p++) {
                    taxis[p] = new Taxi(p, goodStartBonus);
                }
                for(Ride ride : rides) {
                    ride.handled = false;
                }
                System.out.println("Optial cutoff: "+optimalCutoff);
                System.out.println("Gives score: "+optimalScore);
            }
        }

		getOutput();
	}

	public void cAlgorithm() {
	    Taxi taxi = taxis[0];
        while (taxi.time < stepLimit) {
            int smallestDist = Integer.MAX_VALUE;
            Ride bestRide = null;
            Ride scond = null;
            int smal2 = Integer.MAX_VALUE;
            for(Ride ride : rides) {
                if(ride.handled)
                    continue;
                int dist = Math.abs(taxi.posx-ride.a)+Math.abs(taxi.posy-ride.b);
                if(taxi.time+dist+ride.drivingDistance >= ride.finish)
                    continue;
                if (dist < smallestDist) {
                    scond = bestRide;
                    smal2 = smallestDist;
                    smallestDist = dist;
                    bestRide = ride;
                }
            }
            if(bestRide == null) {
                taxi.time = stepLimit;
            } else {
                if(false && scond != null) {
                    bestRide = scond;
                    smallestDist = smal2;
                }
                bestRide.handled = true;
                taxi.assign.add(bestRide);
                taxi.time = taxi.time + smallestDist + bestRide.drivingDistance;
                taxi.posx = bestRide.x;
                taxi.posy = bestRide.y;
            }
            Arrays.sort(taxis, new Comparator<Taxi>() {
                @Override
                public int compare(Taxi t1, Taxi t2) {
                    return t1.time - t2.time;
                }
            });
            taxi = taxis[0];
        }
        Arrays.sort(taxis, new Comparator<Taxi>() {
            @Override
            public int compare(Taxi t1, Taxi t2) {
                return t1.taxiID - t2.taxiID;
            }
        });
    }

    public void centerFinder() {
	    long x = 0;
	    long y = 0;
	    for(Ride ride : rides) {
	        x += ride.a;
	        y += ride.b;
        }
        System.out.println("X: "+(x/amountOfRides)+" Y: "+(y/amountOfRides));
    }

    public void scoreOverTimeAlgorithm() {
        Taxi taxi = taxis[0];
        while (taxi.time < stepLimit) {
            float largestScore = 0;
            Ride bestRide = null;
            for(Ride ride : rides) {
                if(ride.handled)
                    continue;
                int dist = Math.abs(taxi.posx-ride.a)+Math.abs(taxi.posy-ride.b);
                if(taxi.time+dist+ride.drivingDistance > ride.finish) {
                    continue;
                }
                int finishTime = Math.max(taxi.time + dist, ride.start) + ride.drivingDistance;
                int backDist = Math.abs(ride.x - 2801) + Math.abs(ride.y - 1056);
                if(backDist > backcutoff && (stepLimit - finishTime > timeCutoff)) {
                    continue;
                }
                float score = (float)ride.drivingDistance / (finishTime - taxi.time);
                if (score > largestScore) {
                    largestScore = score;
                    bestRide = ride;
                }
            }
            if(bestRide == null) {
                taxi.time = stepLimit;
            } else {
                bestRide.handled = true;
                taxi.assign.add(bestRide);
                int dist = Math.abs(taxi.posx-bestRide.a)+Math.abs(taxi.posy-bestRide.b);
                taxi.time = Math.max(taxi.time + dist, bestRide.start) + bestRide.drivingDistance;
                taxi.posx = bestRide.x;
                taxi.posy = bestRide.y;
            }
            Arrays.sort(taxis, new Comparator<Taxi>() {
                @Override
                public int compare(Taxi t1, Taxi t2) {
                    return t1.time - t2.time;
                }
            });
            taxi = taxis[0];
        }
        Arrays.sort(taxis, new Comparator<Taxi>() {
            @Override
            public int compare(Taxi t1, Taxi t2) {
                return t1.taxiID - t2.taxiID;
            }
        });
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
		improveE();
	}
	
	void improveE() {
		for (int taxiID = 0; taxiID < amountOfVehicles; taxiID++) {
			Taxi taxi = taxis[taxiID];
			taxi.evaluateAssignment();
			int tX = taxi.tX;
			int tY = taxi.tY;
			int time = taxi.time;
			int bestTime = Integer.MAX_VALUE;
			Ride bestRide = null;
			for (Ride ride : rides) {
				if (!ride.handled) {
					int testTime = Math.abs(ride.a - tX) + Math.abs(ride.b - tY) + time;
					if (testTime < stepLimit - ride.drivingDistance && testTime < bestTime && testTime < ride.start) {
						bestTime = testTime;
						bestRide = ride;
					}
				}
			}
			try {
				bestRide.handled = true;
				taxi.assign.add(bestRide);
				taxiID--;				
			} catch (Exception e) {
				
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
		/*if(score > optimalScore) {
		    optimalCutoff = backcutoff;
		    optimalScore = score;
        }*/
        System.out.println("Score: "+score);
	}

	public static void main(String[] args) {
		new Main();
	}

}
