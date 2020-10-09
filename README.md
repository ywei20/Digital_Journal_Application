# Assignment 9: Digital Journal Application


## Introduction
This task is to build a command-line digital journal application. It is a system that allows users to add entries to their digital journal, and track the status of their entries by features like date, category, priority, and status (complete/incomplete). 

## Data Structure
A Digital Entry consists of the following information: 
	- text(required), 
	- completed(false by default), 
	- date(required), 
	- priority(optional, 1 is highest priority, 3 is lowest, 3 if not specified),
	- category(user-specified string to group related Digital Entry).
This application stores all entries in a CSV file, such as the following:
```
{
"id","text","completed","date","priority","category"
"1","Finished HW9.","Yes","8/5/2020","1","school"
"2","I'm going to GHC 2020, scored tickets this morning.Yay","No","8/10/2020","?","?"
}
```

## Functionality
The system supports the following functionality:
	- Add a new digital entry. The CSV file will be updated when new digital entry added.
	- Complete an existing digital entry. Update the status of existing digital entry to complete in the CSV file.
	- Display digital entries. When user requests display of the list of digital entry, all digital results will be printed. The results can also be filterd and/or sort by users' arguments.

The program accepts the following command line arguments in any order:
```
{
--csv-file <path/to/file>
--add-entry
--entry-text <description of entry>
--completed
--date <date>
--priority <1, 2, or 3>
--category <a category name>
--complete-entry <id>
--display
--show-incomplete
--show-category <category>
--sort-by-date
--sort-by-priority	
}
```

## Design Pattern
* which design patterns you used, where and why?
	* In this program, I mainly use **builder pattern** and **singleton pattern**. 

	* I applied **builder pattern** in **DigitalEntry class**, because this pattern allows us to produce different types of representations of a digital entry object using the same construction code. Without passing in a lot of parameters into a DigitalEntry constructor, I am able to create an object through executing a series of steps that are necessary for producing a particular Digital Entry.

	* Besides, I also applied **singleton pattern** in classes of **CmdLineProcessor** and **ReadJournalData** class. This design pattern makes sure that a class has only one instance. Because we only need one CommandLine processor and one ReadCSV object in the main class, using a singlton pattern can help us achieve the goal. 




