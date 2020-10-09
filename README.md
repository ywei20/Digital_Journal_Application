# Digital Journal Application
* This task is to build a command-line digital journal application. It is a system that allows users to add entries to their digital journal, and track the status of their entries by features such as date, category, priority and status(complete/incomplete). 
* which design patterns you used, where and why?
	* In this assignment, we mainly use **builder pattern** and **singleton pattern**. 

	* We apply **builder pattern** in **DigitalEntry class**, because this pattern allows us to produce different types of representations of a digital entry object using the same construction code. Without passing in a lot of parameters into a DigitalEntry constructor, we are able to create an object through executing a series of steps that are necessary for producing a particular Digital Entry.

	* We apply **singleton pattern** in classes of **CmdLineProcessor** and **ReadJournalData** class. This design pattern makes sure that a class has only one instance. Because we only need one CommandLine processor and one ReadCSV object in the main class, using a singlton pattern can help us achieve the goal. 




