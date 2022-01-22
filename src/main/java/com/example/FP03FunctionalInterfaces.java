package com.example;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class FP03FunctionalInterfaces {
    public static void main(String[] args) {
        /**
         * Here we understand how lambdas internally works
         * A functional interface is an interface having one abstract method
         * Predicate, Function and Consumer are functional interfaces
         * Behind each lambda expression there is a functional interface
         */
        List<Integer> nums = List.of(2,3,3,34,54,1,12);

        /**
         * These both predicate are same
         * 2nd one shows what is exactly going on
         * A predicate is used to test something
         */
        Predicate<Integer> evenPredicate = x -> x % 2 == 0;
        Predicate<Integer> evenPredicate2 = new Predicate<Integer>() {
            @Override
            public boolean test(Integer x) {
                return x%2 == 0;
            }
        };

        /**
         * A function takes a input and return an output
         */
        Function<Integer, Integer> squareFunction = x -> x * x;
        Function<Integer, Integer> squareFunction2 = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer x) {
                return x*x;
            }
        };
        /**
         * Consumer consumes what it gets
         */
        Consumer<Integer> sysoutConsumer = System.out::println;
        Consumer<Integer> sysoutConsumer2 = new Consumer<Integer>() {
            @Override
            public void accept(Integer x) {
                System.out.println(x);
            }
        };

        nums.stream()
                .filter(evenPredicate)
                .map(squareFunction)
                .forEach(sysoutConsumer);
        /**
         * One more example of Functional Interface is Binary Operator
         */

        BinaryOperator<Integer> sumBinaryOperator = Integer::sum;
        BinaryOperator<Integer> sumBinaryOperator2 = new BinaryOperator<Integer>() {
            @Override
            public Integer apply(Integer a, Integer b) {
                return a+b;
            }
        };

        int sum = nums.stream()
                        .reduce(0, sumBinaryOperator);


    }
}
