package com.example;

import javax.swing.text.html.HTMLDocument;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamApiExamples {
    public static void main(String[] args) {

        // Java Stream Creation
        Employee[] arrayOfEmps = {
                new Employee(1, "Jeff Bezos", 100000.0),
                new Employee(2, "Bill Gates", 200000.0),
                new Employee(3, "Mark Zuckerberg", 300000.0)
        };

        // 1. Using Stream.of
        Stream<Employee> employeeStream = Stream.of(arrayOfEmps);
        // or
        List<Employee> employeeList = Arrays.asList(arrayOfEmps);
        employeeStream = employeeList.stream();
        // or
        employeeStream = Stream.of(arrayOfEmps[0],arrayOfEmps[1],arrayOfEmps[2]);
        // or Using stream builder
        Stream.Builder<Employee> streamBuilder = Stream.builder();
        streamBuilder.accept(arrayOfEmps[0]);
        streamBuilder.accept(arrayOfEmps[1]);
        streamBuilder.accept(arrayOfEmps[2]);
        employeeStream = streamBuilder.build();

        // Java Stream Operations

        // 1. forEach
        System.out.println("For each demo: ");
        employeeList.stream().forEach(System.out::println);

        // 2. map
        Integer[] empIds = {1,2,3};
        List<Integer> newEmpIds = Stream.of(empIds)
                .map(e -> 2*e)
                .collect(Collectors.toList());

        System.out.println("Map demo: ");
        newEmpIds.forEach(System.out::println);

        // 3. filter
        List<Employee> filteredEmployees = employeeList.stream()
                .filter(e->e.getId()>=2)
                .collect(Collectors.toList());
        System.out.println("Filter demo: ");
        filteredEmployees.forEach(System.out::println);

        // 4. findFirst
        Employee employee = employeeList.stream()
                .filter(e->e.getSalary()>100000.0)
                .findFirst()
                .orElse(null);
        System.out.println("findFirst demo: \n"+employee);

        // 5. toArray - covert to normal array
        Employee[] employees2 = employeeList.stream()
                .toArray(Employee[]::new); // .toArray(size->new Employee[size]);

        // 6. flatMap
        List<List<String>> namesNested = Arrays.asList(
                Arrays.asList("Jeff", "Bezos"),
                Arrays.asList("Bill", "Gates"),
                Arrays.asList("Mark", "Zuckerberg"));

        List<String> namesFlatStream = namesNested.stream()
                .flatMap(Collection::stream)
                // .flatMap(list->list.stream())
                .collect(Collectors.toList());
        System.out.println("Flat Map Demo: ");
        namesFlatStream.forEach(System.out::println);

        // 7. peek -- intermediate operation to print or manipulate data stream
        System.out.println("Peek demo: ");
        List<Employee> peekDemoList = employeeList.stream()
                .peek(e->e.setSalary(e.getSalary()+10))
                .peek(System.out::println)
                .collect(Collectors.toList());

        // Partition by
        Map<Boolean,List<Employee> > var = employeeList.stream()
                .collect(Collectors.partitioningBy(e->e.getSalary()>200000));
        // Grouping by
        Map<Double,List<Employee> > var2 = employeeList.stream()
                .collect(Collectors.groupingBy(e->e.getSalary()));
        // Mapping
        Map<String,List<Double> > var3 = employeeList.stream()
                .collect(Collectors.groupingBy(e->e.getName(),Collectors.mapping(e->employee.getSalary(),Collectors.toCollection(ArrayList::new))));


    }
}
