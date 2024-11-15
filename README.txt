****************
* IUDoubleLinkedList
* CS221
* 11/15/2024
* Nathan Marquis
**************** 

OVERVIEW:

Creates a double linked list implementation of an indexed unsorted list which can 
represent any generic value. Uses nodes with next and previous references, and implements 
a List Iterator interface that supports Iterator as well. Test class included that tests
for proper behavior, with regards to concurrency as well. 


INCLUDED FILES:

 * IUDoubleLinkedList.java - source file with double linked list and list iterator logic
 * Node.java - source file defining double linked nodes used in IUDoubleLinkedList.java
 * ListTester.java - test file used to verify proper behaviour of above files
 * README - this file ; explanation of program


COMPILING AND RUNNING:

 From the directory containing all source files, compile the
 driver class (and all dependencies) with the command:
 $ javac ListTester.java

 Run the compiled class file with the command:
 $ java ListTester

 Console output will give the results after the program finishes
 with the total number and proportion of tests passed.


PROGRAM DESIGN AND IMPORTANT CONCEPTS:

 IUDoubleLinkedList is an implementation of IndexedUnsortedList which uses a 
 double linked node as it's base unit. A Node is an object that has a next and 
 previous reference as well as a stored value of generic type. The double linked 
 list uses these nodes strung together using their next/previous to form a 
 list of data that can be interacted with using the methods as described in 
 the IndexedUnsortedList interface. The head and tail of the list are tracked,
 as well as the size and modification count of each list object. There is also 
 an implementation of ListIterator that extends the functionality of a normal
 Iterator with additional methods. ListIterators are used within the primary list 
 methods to simplify and streamline the codebase and eliminate code duplication.

 The code design could be improved by optimizing for cases where an element is
 added closer to the end of the list. Currently any searching or adding of elements
 is done starting from the head which is not the most efficient way. 

TESTING:

 Testing is accomplished using the ListTester class. It exhaustively goes through
 the majority of change scenarios when using the IndexedUnsortedList methods or 
 the ListIterator methods. It begins with tests resulting in empty lists and goes 
 up to tests resulting in three element lists. Each change scenario has a test
 list which covers most possible combinations of the aforementioned methods as 
 performed on said scenario. Concurrency test were performed to ensure that 
 changes performed outside of a ListIterator would terminate said iterator to
 prevent out-of-date interaction with the list it originally assigned. The code
 has a fixed set of inputs as described in the ListTester class, eliminating the 
 possibility for users to input bad data. As a backup, the list is generic and able
 to take in any type of object as a reference to a node.There are not remaining 
 bugs or issues that I am aware of in my code, but there could be added more test
 scenarios and list tests to achieve a more complete picture. 


DISCUSSION:
 
 Discuss the issues you encountered during programming (development)
 and testing. What problems did you have? What did you have to research
 and learn on your own? What kinds of errors did you get? How did you 
 fix them?
 
 What parts of the project did you find challenging? Is there anything
 that finally "clicked" for you in the process of working on this project?

 As an extension of the single linked list, I had expected the double linked
 list to go smoother than it actually did. I struggled initially with a large
 amount of bugs resulting from me forgetting to assign all of the proper 
 references needed when having double the amount of links between all the nodes
 in my list. There were multiple cases of bugs introduced because there was not
 a next or previous node assigned when I executed the add or remove methods.
 There were tons of null references too because of this issue where the list
 would try to use the getNext() or getElement() on a null node. This was 
 VERY confusing for a while until I fully drew out each change scenario as 
 compared to my actual code executing line by line. This was the most effective
 way for me to figure out where my program was forgetting to assign references 
 in edge cases. The final and most impactful improvement was when I converted 
 my IndexedUnsortedList methods to use the List Iterator as their base, which
 removed most of my redundant code and made it MUCH more readable as a codebase. 
 Any issue could then be traced back to my quality of the iterator methods 
 instead of chasing bugs and references all around my program and test class. 
 The final part that was challenging for me was accidental issues introduced in 
 my test class that were expecting incorrect outputs. It took me a long time to
 figure out that my IUDoubleLinkedList was doing fine but these final couple of 
 classes were actually due to an incorrectly written test item that expected 
 False instead of True on hasNext() or some other method call. The debugger 
 helped me massively in this respect and it helped me much more deeply and quickly
 decipher problems in my code.
 
 
EXTRA CREDIT:

 N/A


----------------------------------------------------------------------------