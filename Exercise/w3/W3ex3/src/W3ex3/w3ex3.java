package W3ex3;

public class w3ex3 {
	public static void main(String[] args){
		System.out.println("Matrix");
		int[][] matrix = new int[6][6];
		for(int i = 0; i < 6; i++){
			for(int j = 0; j < 6; j++){
				int randomNum = (int)(Math.random() * 101);
				matrix[i][j] = randomNum;
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		
		MaxThread t1 = new MaxThread(1, 0, matrix);
		MaxThread t2 = new MaxThread(2, 1, matrix);
		MaxThread t3 = new MaxThread(3, 2, matrix);
		MaxThread t4 = new MaxThread(4, 3, matrix);
		MaxThread t5 = new MaxThread(5, 4, matrix);
		MaxThread t6 = new MaxThread(6, 5, matrix);
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t6.start();
		
		try{
			t1.join();
			t2.join();
			t3.join();
			t4.join();
			t5.join();
			t6.join();
		}catch(InterruptedException e){}
		
		int[] rowMax = new int[6];
		rowMax[0] = t1.getMax();
		rowMax[1] = t2.getMax();
		rowMax[2] = t3.getMax();
		rowMax[3] = t4.getMax();
		rowMax[4] = t5.getMax();
		rowMax[5] = t6.getMax();
		
		int finalMax = rowMax[0];
		for(int i = 1; i < 6; i++){
			if(rowMax[i] > finalMax){
				finalMax = rowMax[i];
			}
		}
		System.out.println();
		System.out.println("Final matrix max = " + finalMax);
	}
}

class MaxThread extends Thread{
	private int id, row, max;
	private int[][] matrix;
	
	public MaxThread(int id, int row, int[][] matrix){
		this.id = id;
		this.row = row;
		this.matrix = matrix;
	}
	
	public int getMax(){
		return max;
	}
	
	@Override
	public void run(){
		max = matrix[row][0];
		for(int j = 1; j < 6; j++){
			if(matrix[row][j] > max){
				max = matrix[row][j];
			}
		}
		System.out.println("Thread-" + id + " row " + row + " max = " + max);
		return;
	}
}
