package w3ex1;

public class W3ex1 {
	public static void main(String[] args){
		int[] arr = new int[1000];
		for(int i = 0; i < arr.length; i++){
			arr[i] = i + 1;
		}
		SumThread t1 = new SumThread(1, 0, 249, arr);
		SumThread t2 = new SumThread(2, 250, 499, arr);
		SumThread t3 = new SumThread(3, 500, 749, arr);
		SumThread t4 = new SumThread(4, 750, 999, arr);
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		
		try{
			t1.join();
			t2.join();
			t3.join();
			t4.join();
		}catch(InterruptedException e){}
		
		long totalSum = t1.getPartialSum() + t2.getPartialSum() + t3.getPartialSum() + t4.getPartialSum();
		System.out.println("Final total sum = " + totalSum);
	}
}

class SumThread extends Thread{
	private int id, start, end;
	private int[] arr;
	private long partialSum;
	
	public SumThread(int id, int start, int end, int[] arr) {
		this.id = id;
		this.start = start;
		this.end = end;
		this.arr= arr;
	}
	
	public long getPartialSum(){
		return partialSum;
	}
	@Override
	public void run(){
		long Par_sum = 0;
		for(int i = start; i <= end; i++){
			Par_sum += arr[i];
		}
		partialSum = Par_sum;
		System.out.println("Thread-" + id + "processed index " + start + " to " + end + ", partial sum = " + Par_sum );
		return;
	}
	
}
