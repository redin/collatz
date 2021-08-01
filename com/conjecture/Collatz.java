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
    private static final ThreadPoolExecutor executor =
            (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()-1);
    private static  final Object lock = new Object();
    private static final int size = Runtime.getRuntime().availableProcessors()-1;
    private static final BlockingQueue<StringBuilder> q = new ArrayBlockingQueue<>(size);

    public static void main(String[] args) throws InterruptedException {
        BigInteger i = MIN;
        if(args != null && args.length == 1 ) {
            i = new BigInteger(args[0]);
        }
        for (int j = 0; j < size; j++) {
            try {
                q.put(new StringBuilder());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        executor.prestartAllCoreThreads();
        do {
            StringBuilder token = q.take();
            System.out.println(token);
            Runnable r = new SingleCollatz(i);
            executor.submit(r);
            i = i.add(BigInteger.ONE);
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
        public SingleCollatz(BigInteger p) {
            this.c = p;
        }
        public void run() {
            StringBuilder s = new StringBuilder();
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
