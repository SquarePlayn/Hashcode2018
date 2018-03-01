public class Ride {
	
	int rideID, a, b, x, y, start, finish, drivingDistance;
	
	
	Ride(int rideID, int a, int b, int x, int y, int start, int finish) {
		this.rideID = rideID;
		this.a = a;
		this.b = b;
		this.x = x;
		this.y = y;
		this.start = start;
		this.finish = finish;
		drivingDistance = Math.abs(a - x) + Math.abs(b - y);
	}
	
	
	
	
	
	
}
