import java.io.FileReader;
import java.util.*;

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
			sc = new Scanner(new FileReader("Input/" + "d_metropolis.in"));
			
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

		if (false) {
			eAlgorithm();
		}
		if (false) {
			WoutKoen();
		}

		if(false) {
		    cAlgorithm();
        }

        if(true) {
            //centerFinder();
		    scoreOverTimeAlgorithm();
        }

		getOutput();
	}

	public void cAlgorithm() {
	    Taxi taxi = taxis[0];
        while (taxi.time < stepLimit) {
            int smallestDist = Integer.MAX_VALUE;
            Ride bestRide = null;
            for(Ride ride : rides) {
                if(ride.handled)
                    continue;
                int dist = Math.abs(taxi.posx-ride.a)+Math.abs(taxi.posy-ride.b);
                if(taxi.time+dist+ride.drivingDistance > ride.finish)
                    continue;
                if (dist < smallestDist) {
                    smallestDist = dist;
                    bestRide = ride;
                }
            }
            if(bestRide == null) {
                taxi.time = stepLimit;
            } else {
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
                if(taxi.time+dist+ride.drivingDistance >= ride.finish)
                    continue;
                int finishTime = Math.max(taxi.time + dist, ride.start) + ride.drivingDistance;
                int backDist = Math.abs(ride.x - 2801) + Math.abs(ride.y - 1056);
                if(backDist > 8000) {
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

	public void WoutKoen() {
		sortRides();
		for (int T = 0; T < stepLimit; T++) {
			for (Taxi t: taxis) {
				if (T >= t.unavailable) {
					Ride best = null;
					double max = 0;
					for (Ride r : rides) {
						int distance = Math.abs(t.posx-r.a)+Math.abs(t.posy-r.b);
						if (distance + T < r.finish-r.drivingDistance && !r.handled) {
							if (r.drivingDistance/(distance+r.drivingDistance)>max) {
								max = r.drivingDistance/(distance+r.drivingDistance);
								best = r;
							}
						}
					}
					if (best!=null) {
						System.out.println("Assigned a car, at T: " + T);
						t.assign.add(best);
						t.unavailable = T + Math.abs(t.posx-best.a)+Math.abs(t.posy-best.b) + best.drivingDistance;
						t.posx = best.x;
						t.posy = best.y;
						best.handled = true;
					}
				}
			}
		}
	}
}
