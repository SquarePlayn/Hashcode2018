import java.io.FileReader;
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

	Main() {
		try {
			sc = new Scanner(new FileReader("videos_worth_spreading.in"));
			
			numberOfRows = sc.nextInt();
			numberOfColumns = sc.nextInt();
			amountOfVehicles = sc.nextInt();
			amountOfRides = sc.nextInt();
			goodStartBonus = sc.nextInt();
			stepLimit = sc.nextInt();
			
			rides = new Ride[amountOfRides];
						
			int a, b, x, y, start, finish;
			for (int rideID = 0; rideID < amountOfRides; rideID++) {
				a = sc.nextInt();
				b = sc.nextInt();
				x = sc.nextInt();
				y = sc.nextInt();
				start = sc.nextInt();
				finish = sc.nextInt();
				rides[rideID] = new Ride(a, b, x, y, start, finish);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sc.close();
		}	
	}	
	
	public static void main(String[] args) {
		new Main();
	}
}
