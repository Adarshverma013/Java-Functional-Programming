package com.example;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FP02 {
    public static void main(String[] args) {
        /**
         * Add the numbers using stream reduce
         */
        List<Integer> nums = List.of(2,3,3,34,54,1,12);
        Integer sum = nums.stream()
                .reduce(0,(a,b)->{return a+b;});
        System.out.println("Sum = "+sum);
        /**
         * Maximum and minimum value of the list
         */
        Integer min_val = nums.stream()
                .reduce(nums.get(0),(a,b)->{return a>b?b:a;});
        Integer max_val = nums.stream()
                .reduce(nums.get(0),(a,b)->{return a<b?b:a;});
        System.out.println("Min value is = "+min_val);
        System.out.println("Max value is = "+max_val);
        /**
         * 1.Square every number and find sum
         * 2. Sum of odd numbers
         */
        // 1.
        Integer sum_of_sq = nums.stream()
                .map(x->x*x)
                .reduce(0,Integer::sum);
        System.out.println("Sum of square = "+sum_of_sq);
        // 2.
        Integer sum_of_odd = nums.stream()
                .filter(x->{return (x%2 == 1);})
                .reduce(0,Integer::sum);
        System.out.println("Sum of odd numbers = "+sum_of_odd);
        /**
         * Distinct and sorted
         */
        System.out.println("Distinct Values ");
        nums.stream()
                .distinct()
                .forEach(System.out::println);
        System.out.println("Sorted Values ");
        nums.stream()
                .sorted()
                .forEach(System.out::println);
        /**
         * Using comparator in sorted
         * Comparator.comparing((a,b)->{//condition})
         */
        System.out.println("Reverse Sorted Values ");
        nums.stream()
                .sorted(Comparator.reverseOrder())
                .forEach(System.out::println);

        /**
         * Collecting the values using collect
         * 1. double values
         * 2. even values
         */
        List<Integer> doubledValues = nums.stream()
                .map(x->x*2)
                .collect(Collectors.toList());
        System.out.println("Doubled Values ");
        doubledValues.stream()
                .forEach(System.out::println);
        List<Integer> evenValues = nums.stream()
                .filter(x->{return x%2==0;})
                .collect(Collectors.toList());
        System.out.println("Even Values ");
        evenValues.stream()
                .forEach(System.out::println);
    }
}
