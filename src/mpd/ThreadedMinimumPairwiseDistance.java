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
        threads[0] = new Thread(new threadRunnable(values, 1, minArrayList));
        threads[0].start();

        threads[1] = new Thread(new threadRunnable(values, 2, minArrayList));
        threads[1].start();

        threads[2] = new Thread(new threadRunnable(values, 3, minArrayList));
        threads[2].start();

        threads[3] = new Thread(new threadRunnable(values, 4, minArrayList));
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
        private int threadnum;

        public threadRunnable(int[] inputArray, int threadnum, minimumTracker mins){

            this.inputArray = inputArray;
            this.mins = mins;
            this.threadnum =threadnum;

        }

        @Override
        public void run() {
            int N = this.inputArray.length;
            long result = Integer.MAX_VALUE;


            if (threadnum==1){
                for (int i = 0; i < (N / 2); ++i) {
                    for (int j = 0; j < i; ++j) {
                        long diff = Math.abs(this.inputArray[i] - this.inputArray[j]);
                        if (diff < result) {
                            result = diff;
                        }
                    }
                }
            }

            if (threadnum==2){
                for (int i = (N / 2); i < N; ++i) {
                    for (int j = 0; (j + (N / 2))< i; ++j) {
                        long diff = Math.abs(this.inputArray[i] - this.inputArray[j]);
                        if (diff < result) {
                            result = diff;
                        }
                    }
                }
            }

            if (threadnum==3){
                for (int i = (N / 2); i < N; ++i) {
                    for (int j = (N / 2); j < i; ++j) {
                        long diff = Math.abs(this.inputArray[i] - this.inputArray[j]);
                        if (diff < result) {
                            result = diff;
                        }
                    }
                }
            }

            if (threadnum==4){
                for (int j = 0; j < (N / 2); ++j) {
                    for (int i = (N / 2); i < (j + (N / 2)); ++i) {
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
