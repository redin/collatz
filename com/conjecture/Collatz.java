package com.conjecture;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class Collatz {

    private static final BigInteger TWO = new BigInteger("2");
    private static final BigInteger THREE = new BigInteger("3");
    private static BigInteger MIN = THREE.add(TWO);
    private static final int threads = Runtime.getRuntime().availableProcessors();
    private static final ThreadPoolExecutor executor =
            (ThreadPoolExecutor) Executors.newFixedThreadPool(threads);
    private static final Object lock = new Object();
    private static final BlockingQueue<StringBuffer> q = new ArrayBlockingQueue<>(threads);

    public static void main(String[] args) throws InterruptedException {
        BigInteger i = MIN;
        if(args != null && args.length == 1 ) {
            i = new BigInteger(args[0]);
        }
        for (int j = 0; j < threads; j++) {
            try {
                q.put(new StringBuffer());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        executor.prestartAllCoreThreads();
        if(!i.mod(TWO).equals(BigInteger.ONE)){
            i = i.add(BigInteger.ONE);
        }        
        do {
            System.out.println(q.take());
            Runnable r = new SingleCollatz(i);
            executor.submit(r);
            i = i.add(TWO);
        } while(true);
    }

    static BigInteger even(BigInteger a){
        return a.divide(TWO);
    }

    static BigInteger odd( BigInteger b){
        return b.multiply(THREE).add(BigInteger.ONE);
    }

    private static class SingleCollatz implements Runnable{
        BigInteger c;
        StringBuffer s = new StringBuffer();
        public SingleCollatz(BigInteger p) {
            this.c = p;
        }
        public void run() {
            s.append(c);
            BigInteger i = this.c;
            List<BigInteger> END = Collections.synchronizedList(new ArrayList<>());
            do{
                END.add(i);
                if(i.mod(TWO).equals(BigInteger.ONE)){
                    i = odd(i);
                }else{
                    i = even(i);
                }
                s.append(" ");
                s.append(i);
                if(END.contains(i)){
                    System.out.println(s);
                    System.exit(0);
                }
            }while(MIN.min(i).equals(MIN));
            synchronized (lock){
                MIN = c;
            }
            try {
                q.put(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            s.append(" "+'\u2713');
        }
    }
}
