# Java Functional Programming

## Introduction to Stream API

Streams are wrappers around a data source, allowing us to operate with that data source and making bulk processing convenient and fast.<br>
A stream does not store data thus its not a Data Structure. It also never modifies underlying data source.

### Stream Creation


```
private static Employee[] arrayOfEmps = {
    new Employee(1, "Jeff Bezos", 100000.0), 
    new Employee(2, "Bill Gates", 200000.0), 
    new Employee(3, "Mark Zuckerberg", 300000.0)
};

Stream<Employee> employeeStream = Stream.of(arrayOfEmps);
```

We can also obtain stream from an existing list :
```
private static List<Employee> empList = Arrays.asList(arrayOfEmps);
Stream<Employee> employeeStream = empList.stream();
```

And we can use `Stream.of()` :<br>
``` Stream.of(arrayOfEmps[0],arrayOfEmps[1],arrayOfEmps[2]) ```

Or we can simply use ``` Stream.builder() ``` : <br>
```
Stream.Builder<Employee> empStreamBuilder = Stream.builder();

empStreamBuilder.accept(arrayOfEmps[0]);
empStreamBuilder.accept(arrayOfEmps[1]);
empStreamBuilder.accept(arrayOfEmps[2]);

Stream<Employee> empStream = empStreamBuilder.build();
```

### Java Stream Operations

#### forEach
It is simplest and most common operation. It takes **Consumer Functional Interface** as input. <br>
It loops over the stream elements and applies the provided function.
```
@Test
public void whenIncrementSalaryForEachEmployee_thenApplyNewSalary() {    
    empList.stream().forEach(e -> e.salaryIncrement(10.0));
    
    assertThat(empList, contains(
      hasProperty("salary", equalTo(110000.0)),
      hasProperty("salary", equalTo(220000.0)),
      hasProperty("salary", equalTo(330000.0))
    ));
}
```
> forEach() operation is a terminal operation i.e. after the operation stream pipeline is considered consumed and can no longer be used.

#### map
_map()_ produces a new stream after applying a function to each element of the original stream. The new stream could be of different type.
```
@Test
public void whenMapIdToEmployees_thenGetEmployeeStream() {
    Integer[] empIds = { 1, 2, 3 };
    
    List<Employee> employees = Stream.of(empIds)
      .map(employeeRepository::findById)
      .collect(Collectors.toList());
    
    assertEquals(employees.size(), empIds.length);
}
```
Here, we obtain an Integer stream of employee ids from an array.<br> Each Integer is passed to the function employeeRepository::findById() – which returns the corresponding Employee object;<br> this effectively forms an Employee stream.
> map() is an intermediate operation i.e. it returns a stream.
> Also map takes Function as input which is a functional interface that accepts some parameters and returns a value.

#### filter

This produces a new stream that contains elements of the original stream that pass a given test (specified by a Predicate).
```
@Test
public void whenFilterEmployees_thenGetFilteredStream() {
    Integer[] empIds = { 1, 2, 3, 4 };
    
    List<Employee> employees = Stream.of(empIds)
      .map(employeeRepository::findById)
      .filter(e -> e != null)
      .filter(e -> e.getSalary() > 200000)
      .collect(Collectors.toList());
    
    assertEquals(Arrays.asList(arrayOfEmps[2]), employees);
}
```
In the example above, we first filter out null references for invalid employee ids and then again apply a filter to only keep employees with salaries over a certain threshold.

> filter() input type is Predicate which is a functional interface.<br>
> Predicate accepts some parameters and return type is boolean.

#### collect
We saw how collect() works in the previous example;<br> Its one of the common ways to get stuff out of the stream once we are done with all the processing:
```
@Test
public void whenCollectStreamToList_thenGetList() {
    List<Employee> employees = empList.stream().collect(Collectors.toList());
    
    assertEquals(empList, employees);
}
```
_collect()_ performs mutable fold operations (repackaging elements to some data structures and applying some additional logic, concatenating them, etc.) on data elements held in the Stream instance.
<br><br>
The strategy for this operation is provided via the Collector interface implementation. <br>In the example above, we used the toList collector to collect all Stream elements into a List instance.

