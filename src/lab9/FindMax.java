package lab9;

/**
 * 
 * COMP 3021
 * 
This is a class that prints the maximum value of a given array of 90 elements

This is a single threaded version.

Create a multi-thread version with 3 threads:

one thread finds the max among the cells [0,29] 
another thread the max among the cells [30,59] 
another thread the max among the cells [60,89]

Compare the results of the three threads and print at console the max value.

 * 
 * @author valerio
 *
 */
public class FindMax {
	// this is an array of 90 elements
	// the max value of this array is 9999
	static int[] array = { 1, 34, 5, 6, 343, 5, 63, 5, 34, 2, 78, 2, 3, 4, 5, 234, 678, 543, 45, 67, 43, 2, 3, 4543,
			234, 3, 454, 1, 2, 3, 1, 9999, 34, 5, 6, 343, 5, 63, 5, 34, 2, 78, 2, 3, 4, 5, 234, 678, 543, 45, 67, 43, 2,
			3, 4543, 234, 3, 454, 1, 2, 3, 1, 34, 5, 6, 5, 63, 5, 34, 2, 78, 2, 3, 4, 5, 234, 678, 543, 45, 67, 43, 2,
			3, 4543, 234, 3, 454, 1, 2, 3 };

	public static void main(String[] args) {
		new FindMax().printMax();
	}

	public void printMax() {
		// below is the multi-threading code:
		int max1, max2, max3, max;
		threadThing thing1 = new threadThing(0,30);
		Thread t1 = new Thread(thing1);
		t1.start();
		try {
			t1.join();
		} catch (InterruptedException e) {}
		max1 = thing1.getMax();
		
		threadThing thing2 = new threadThing(30,60);
		Thread t2 = new Thread(thing2);
		t2.start();
		try {
			t2.join();
		} catch (InterruptedException e) {}
		max2 = thing2.getMax();
			
		threadThing thing3 = new threadThing(60,90);
		Thread t3 = new Thread(thing3);
		t3.start();
		try {
			t3.join();
		} catch (InterruptedException e) {}
		max3 = thing3.getMax();
		
		if(max1 >= max2) {
			max = max1;
		}else {
			max = max2;
		}
		
		if(max < max3) {
			max = max3;
		}
		
		System.out.println("the max value is " + max);
	}

	/**
	 * returns the max value in the array within a give range [begin,range]
	 * 
	 * @param begin
	 * @param end
	 * @return
	 */
	private int findMax(int begin, int end) {
		// you should NOT change this function
		int max = array[begin];
		for (int i = begin + 1; i <= end; i++) {
			if (array[i] > max) {
				max = array[i];
			}
		}
		return max;
	}
	
	private class threadThing implements Runnable{
		private int begin, end, max;
		
	      public threadThing(int begin, int end) {
	         this.begin = begin;
	         this.end = end;
	      }
	      
	      public int getMax() {
	    	  return max;
	      }
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			max = findMax(begin, end - 1);
		}
	}
}
