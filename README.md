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

#### filter
_filter()_ produces a new stream that contains elements of the original stream that pass a given test (specified by a Predicate).

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
The above code filters for employees which are not null and whose salary is above a certain threshold and then collect them to a list.

#### findFirst
_findFirst()_ returns an Optional for the first entry in the stream; the Optional can, of course, be empty:

```
@Test
public void whenFindFirst_thenGetFirstEmployeeInStream() {
    Integer[] empIds = { 1, 2, 3, 4 };
    
    Employee employee = Stream.of(empIds)
      .map(employeeRepository::findById)
      .filter(e -> e != null)
      .filter(e -> e.getSalary() > 100000)
      .findFirst()
      .orElse(null);
    
    assertEquals(employee.getSalary(), new Double(200000));
}
```

Here, the first employee with the salary greater than 100000 is returned. If no such employee exists, then null is returned.

#### toArray
If we need to get an array out of the stream, we can simply use toArray():
```
@Test
public void whenStreamToArray_thenGetArray() {
    Employee[] employees = empList.stream().toArray(Employee[]::new);

    assertThat(empList.toArray(), equalTo(employees));
}
```
The syntax Employee[]::new creates an empty array of Employee – which is then filled with elements from the stream.

#### flatMap
A stream can hold complex data structures like Stream<List<String>>. In cases like this, flatMap() helps us to flatten the data structure to simplify further operations:
```
@Test
public void whenFlatMapEmployeeNames_thenGetNameStream() {
    List<List<String>> namesNested = Arrays.asList( 
      Arrays.asList("Jeff", "Bezos"), 
      Arrays.asList("Bill", "Gates"), 
      Arrays.asList("Mark", "Zuckerberg"));

    List<String> namesFlatStream = namesNested.stream()
      .flatMap(Collection::stream)
      .collect(Collectors.toList());

    assertEquals(namesFlatStream.size(), namesNested.size() * 2);
}
```
Here we are able to convert Stream<List<String>> to Stream<String> using flatMap.

#### peek
Sometimes we need to perform multiple operations on each element of the stream before any terminal operation is applied.
<br>_peek()_ can be useful in situations like this. Simply put, it performs the specified operation on each element of the stream and returns a new stream which can be used further. **peek() is an intermediate operation:**

```
@Test
public void whenIncrementSalaryUsingPeek_thenApplyNewSalary() {
    Employee[] arrayOfEmps = {
        new Employee(1, "Jeff Bezos", 100000.0), 
        new Employee(2, "Bill Gates", 200000.0), 
        new Employee(3, "Mark Zuckerberg", 300000.0)
    };

    List<Employee> empList = Arrays.asList(arrayOfEmps);
    
    empList.stream()
      .peek(e -> e.salaryIncrement(10.0))
      .peek(System.out::println)
      .collect(Collectors.toList());

    assertThat(empList, contains(
      hasProperty("salary", equalTo(110000.0)),
      hasProperty("salary", equalTo(220000.0)),
      hasProperty("salary", equalTo(330000.0))
    ));
}
```

## Lazy Evaluation
**Computation on the source data is only performed when the terminal operation is initiated, and source elements are consumed only as needed.**<br><br>
**All intermediate operations are lazy, so they’re not executed until a result of a processing is actually needed.**
```
@Test
public void whenFindFirst_thenGetFirstEmployeeInStream() {
    Integer[] empIds = { 1, 2, 3, 4 };
    
    Employee employee = Stream.of(empIds)
      .map(employeeRepository::findById)
      .filter(e -> e != null)
      .filter(e -> e.getSalary() > 100000)
      .findFirst()
      .orElse(null);
    
    assertEquals(employee.getSalary(), new Double(200000));
}
```
Stream performs the map and two filter operations, one element at a time. <br><br>
It first performs all the operations on id 1. Since the salary of id 1 is not greater than 100000, the processing moves on to the next element.<br><br>
Id 2 satisfies both of the filter predicates and hence the stream evaluates the terminal operation findFirst() and returns the result.<br><br>
No operations are performed on id 3 and 4.<br><br>
Processing streams lazily allows avoiding examining all the data when that’s not necessary. This behavior becomes even more important when the input stream is infinite and not just very large.

## Comparison Based Stream Operations

#### sorted
The _sorted()_ operation – sorts the stream elements based on the comparator passed we pass into it.
<br>For example, we can sort Employees based on their names:
```
@Test
public void whenSortStream_thenGetSortedStream() {
    List<Employee> employees = empList.stream()
      .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
      .collect(Collectors.toList());

    assertEquals(employees.get(0).getName(), "Bill Gates");
    assertEquals(employees.get(1).getName(), "Jeff Bezos");
    assertEquals(employees.get(2).getName(), "Mark Zuckerberg");
}
```
> short circuiting will not be applied for sorted().
> This means, in the example above, even if we had used 
> findFirst() after the sorted(), 
> the sorting of all the elements is done before applying the findFirst(). 
> This happens because the operation cannot know what the first element is until the entire stream is sorted.


#### min and max

As the name suggests, min() and max() return the minimum and maximum element in the stream respectively, based on a comparator. They return an Optional since a result may or may not exist (due to, say, filtering):

```
@Test
public void whenFindMin_thenGetMinElementFromStream() {
    Employee firstEmp = empList.stream()
      .min((e1, e2) -> e1.getId() - e2.getId())
      .orElseThrow(NoSuchElementException::new);

    assertEquals(firstEmp.getId(), new Integer(1));
}
```

We can also avoid defining the comparison logic by using Comparator.comparing():
```
@Test
public void whenFindMax_thenGetMaxElementFromStream() {
    Employee maxSalEmp = empList.stream()
      .max(Comparator.comparing(Employee::getSalary))
      .orElseThrow(NoSuchElementException::new);

    assertEquals(maxSalEmp.getSalary(), new Double(300000.0));
}
```

#### distinct
distinct() does not take any argument and returns the distinct elements in the stream, eliminating duplicates. It uses the equals() method of the elements to decide whether two elements are equal or not:

```
@Test
public void whenApplyDistinct_thenRemoveDuplicatesFromStream() {
    List<Integer> intList = Arrays.asList(2, 5, 3, 2, 4, 3);
    List<Integer> distinctIntList = intList.stream().distinct().collect(Collectors.toList());
    
    assertEquals(distinctIntList, Arrays.asList(2, 5, 3, 4));
}
```

#### allMatch, anyMatch, and noneMatch

```
@Test
public void whenApplyMatch_thenReturnBoolean() {
    List<Integer> intList = Arrays.asList(2, 4, 5, 6, 8);
    
    boolean allEven = intList.stream().allMatch(i -> i % 2 == 0);
    boolean oneEven = intList.stream().anyMatch(i -> i % 2 == 0);
    boolean noneMultipleOfThree = intList.stream().noneMatch(i -> i % 3 == 0);
    
    assertEquals(allEven, false);
    assertEquals(oneEven, true);
    assertEquals(noneMultipleOfThree, false);
}
```

## Java Stream Specializations

// TODO

## Reduction Operations
// TODO
## Advanced collect
// TODO


