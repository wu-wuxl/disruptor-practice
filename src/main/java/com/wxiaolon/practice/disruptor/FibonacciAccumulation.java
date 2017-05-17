package com.wxiaolon.practice.disruptor;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.concurrent.Executors;

public class FibonacciAccumulation {
    // Read the previous event in RingBuffer and write the next one.
    private static final int BUFFER_SIZE = 2;

    public static void main(String[] args) {
        final Disruptor<FibonacciNumber> disruptor = new Disruptor<>(FibonacciNumber::new, BUFFER_SIZE,
                Executors.defaultThreadFactory());

        final RingBuffer<FibonacciNumber> ringBuffer = disruptor.getRingBuffer();

        // Compute and journal.
        disruptor.handleEventsWith(fibonacciNumberHandler(ringBuffer), journalHandler());

        disruptor.start();

        // The instance is created by disruptor.
        // Just change the instance's state.
        ringBuffer.publishEvent((event, sequence) -> {
            event.setPrevious(1);
            event.setCurrent(1);
        });
    }

    private static EventHandler<FibonacciNumber> fibonacciNumberHandler(final RingBuffer<FibonacciNumber> nextBuffer) {
        return (currentNumber, sequence, batchOfEnd) -> {
            if (currentNumber.getCurrent() < 0) {
                return;
            }

            nextBuffer.publishEvent((nextNumber, toSequence) -> {
                nextNumber.setPrevious(currentNumber.getCurrent());
                nextNumber.setCurrent(currentNumber.getPrevious() + currentNumber.getCurrent());
            });
        };
    }

    private static EventHandler<FibonacciNumber> journalHandler() {
        return (currentNumber, sequence, batchOfEnd) ->
                System.out.println(sequence + "\t" + currentNumber);
    }
}
