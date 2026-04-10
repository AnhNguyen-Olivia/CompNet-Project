package w3ex4;
import java.util.*;

public class w3ex4 {
	public static void main(String[] args){
		BufferedBound buffer = new BufferedBound();
		
		ProducerThread p1 = new ProducerThread(1, buffer);
		ProducerThread p2 = new ProducerThread(2, buffer);
		ConsumerThread c1 = new ConsumerThread(1, buffer);
		ConsumerThread c2 = new ConsumerThread(2, buffer);
		
		p1.start();
		p2.start();
		c1.start();
		c2.start();
	}
}

class BufferedBound{
	private Queue<Integer> buffer = new LinkedList<>();
	private final int capacity = 5;
	
	public synchronized void put (int value){
		while(buffer.size() == capacity){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		buffer.add(value);
		notifyAll();
	}
	
	public synchronized int get(){
		while(buffer.isEmpty()){
			try{
				wait();
			}catch(InterruptedException e){e.printStackTrace();}
		}
		
		int value = buffer.poll();
		notifyAll();
		return value;
	}
}

class ProducerThread extends Thread{
	private int id;
	private BufferedBound buffer;
	
	public ProducerThread(int id, BufferedBound buffer){
		this.id = id;
		this.buffer = buffer;
	}
	
	@Override
	public void run(){
		for(int i = 0; i < 10; i++){
			int value = (int)(Math.random() * 101);
			buffer.put(value);
			System.out.println("Producer-" + id + " produced " + value);
		}
	}	
}

class ConsumerThread extends Thread{
	private int id;
	private BufferedBound buffer;
	
	public ConsumerThread(int id, BufferedBound buffer){
		this.id = id;
		this.buffer = buffer;
	}
	
	@Override
	public void run(){
		for(int i = 0; i < 10; i++) {
			int value = buffer.get();
			System.out.println("Consumer-" + id + " consumed " + value);
		}
	}	
}
