package mpd;

import java.util.ArrayList;
import java.util.Collections;

public class ThreadedMinimumPairwiseDistance implements MinimumPairwiseDistance {


    public ThreadedMinimumPairwiseDistance(){
        //this empty constructor needs to be here for ThreadedMain.java
    }



    @Override
    public long minimumPairwiseDistance(int[] values) {
        int numThreads = 4;

        minimumTracker minArrayList = new minimumTracker();

        // Create and start a bunch of threads
        Thread[] threads = new Thread[numThreads];
        // Create and start thread i
        threads[0] = new Thread(new threadRunnable(values, 0,0,1,1,false, minArrayList));
        threads[0].start();

        threads[1] = new Thread(new threadRunnable(values, (1/2), 0, 1,1,false, minArrayList));
        threads[1].start();

        threads[2] = new Thread(new threadRunnable(values, (1/2), (1/2), 1,1,false, minArrayList));
        threads[2].start();

        threads[3] = new Thread(new threadRunnable(values, (1/2), 0, 1,1,true, minArrayList));
        threads[3].start();



        // Wait for all the threads to finish
        for (int i=0; i<numThreads; ++i) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



        return minArrayList.getMin();
    }



    private class minimumTracker{
        private ArrayList<Long> minimums = new ArrayList<Long>();

        public long getMin() {
            return Collections.min(minimums);
        }

        public synchronized void addMinimum(long minim) {
            minimums.add(minim);
        }

    }


    private class threadRunnable implements Runnable {

        private int[] inputArray;

        private minimumTracker mins;
        private int m1;
        private int m2;
        private int m3;
        private int m4;
        private boolean m5;

        public threadRunnable(int[] inputArray,int m1, int m2, int m3, int m4, boolean m5, minimumTracker mins){

            this.inputArray = inputArray;
            this.mins = mins;
            this.m1 =m1;
            this.m2 =m2;
            this.m3 =m3;
            this.m4  =m4;
            this.m5 =m5;
        }

        @Override
        public void run() {
            int N = this.inputArray.length;
            long result = Integer.MAX_VALUE;


            if (!m5){
                for (int i = (m1 * N); i < (N / m3); ++i) {
                    for (int j = (m2 * N); (j + (m4 * (N/2))) < i; ++j) {
                        // Gives us all the pairs (i, j) where 0 <= j < i < values.length
                        long diff = Math.abs(this.inputArray[i] - this.inputArray[j]);
                        if (diff < result) {
                            result = diff;
                        }
                    }
                }
            }


            else {
                for (int j = (m1 * N); (j +(m4*(N/2))) < (N / m3); ++j) {
                    for (int i = (m2 * N); i < j; ++i) {
                        // Gives us all the pairs (i, j) where 0 <= j < i < values.length
                        long diff = Math.abs(this.inputArray[i] - this.inputArray[j]);
                        if (diff < result) {
                            result = diff;
                        }
                    }
                }
            }


            this.mins.addMinimum(result);
        }
    }


}
