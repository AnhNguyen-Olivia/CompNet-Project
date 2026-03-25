package w3ex2;

public class w3ex2 {
	public static void main(String[] args){
		Cinema cinema = new Cinema();
		BookingThread t1 = new BookingThread(1, 8, cinema);
		BookingThread t2 = new BookingThread(2, 8, cinema);
		BookingThread t3 = new BookingThread(3, 8, cinema);
		
		t1.start();
		t2.start();
		t3.start();
	}
}

class Cinema{
	int availableSeats = 20;
	
	public synchronized boolean bookSeats(int id, int numSeats){
		System.out.println("BookingThread-" + id + ", current seats = " + availableSeats);
		if(numSeats > availableSeats){
			return false;
		}
		availableSeats -= numSeats;
		return true;
	}
}

class BookingThread extends Thread{
	private int id, numberOfSeats;
	private Cinema cinema;
	
	public BookingThread(int id, int numberOfSeats, Cinema cinema){
		this.id = id;
		this.numberOfSeats = numberOfSeats;
		this.cinema = cinema;
	}
	
	@Override
	public void run(){
		System.out.println("BookingThread-" + id + " requests " + numberOfSeats + " seats");
		boolean success = cinema.bookSeats(id, numberOfSeats);
		if(success) {
			System.out.println("BookingThread-" + id + " booking successful. Remaining seats = " + cinema.availableSeats);
			}else {
				System.out.println("BookingThread-" + id + " booking failed. Remaining seats = " + cinema.availableSeats);
			}	
		return;
		}
}
