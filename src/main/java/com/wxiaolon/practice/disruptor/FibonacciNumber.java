package com.wxiaolon.practice.disruptor;

public class FibonacciNumber {
    private Integer previous;

    private Integer current;

    public Integer getPrevious() {
        return previous;
    }

    public void setPrevious(Integer previous) {
        this.previous = previous;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    @Override
    public String toString() {
        return "FibonacciNumber{" + "previous=" + previous + ", current=" + current + "}";
    }
}
