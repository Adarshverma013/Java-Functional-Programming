package com.example;

import java.util.List;

/**
 * Hello world!
 *
 */
public class FP01
{
    public static void main( String[] args )
    {
        printAllNumbersInFunctionalStyle(List.of(1, 2, 3, 4, 5));
        /**
         * Exercise
         * 1. Print Courses containing the word Spring
         * 2. Print Courses containing at least 4 letters
         * 3. Print the number of characters in each course name
         */
        List<String> courses = List.of("Spring","Spring Boot","API","PCF","Azure","Docker");
        // 1.
        System.out.println("Printing Courses containing the word Spring");
        courses.stream()
                .filter(x->x.contains("Spring"))
                .forEach(System.out::println);
        //2.
        System.out.println("Printing Courses containing at least 4 letters");
        courses.stream()
                .filter(x->x.length() >= 4)
                .forEach(System.out::println);
        //3.
        System.out.println("Printing the number of characters in each course name");
        courses.stream()
                .map(x->x.length())
                .forEach(System.out::println);

    }
    private static void printAllNumbersInFunctionalStyle(List<Integer> numbers) {
        // looping and printing using stream api
        numbers.stream()
                .forEach(System.out::println);
        // using filer
        System.out.println("Even numbers : ");
        /**
         * lambda expression replacing the anonymous inner class
         * println is a static method of System.out class
         */
        numbers.stream()
                .filter(x->{return (x%2 == 0);})
                .forEach(System.out::println);
        /**
         * Using Map
         */
        System.out.println("Printing square of numbers");
        numbers.stream()
                .map(x->x*x)
                .forEach(System.out::println);

    }
}
