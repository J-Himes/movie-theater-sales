# movie-theater-sales

Note: I used sqlite3 for creating and querying a local database in both of my programs. If you have not used it before you may need to install it. The following link provides a setup walkthrough and link to download the necessary files:

https://www.tutorialspoint.com/sqlite/sqlite_installation.htm


I created one program in Python and one program in Java. Prior to running each program you must use the provided .sql file to create a .db file.

In Windows Powershell, the process is as follows:

1) Navigate to the python_program_files directory via your terminal.

2) Run the following command in your terminal:

Get-Content movie_theater_sales.sql | sqlite3 movie_theater_sales.db

3) Check to see if the .db file was properly created by running the following:

sqlite3 movie_theater_sales.db

.schema // This will show you the DDL statements used to create the database.

.quit // To quit sqlite3


For the Java program, you can navigate to the java_program_files/databases directory and follow the same steps as above.


To view the DDL statements and insertions I made into each table you can use the following commands from either of the Python or Java programs' directories that contain the .db file:

sqlite3 movie_theater_sales.db

.dump

.quit


I created three tables to store the necessary data, including one for theaters, one for movies, and one for sales. Both the Theaters and Movies tables use a unique ID as the primary key, and the Sales table uses a composite key for its primary key that references the relevant theater, movie, and date of sales.


The following dates contain sales data in both programs:

1) 05/27/2024

2) 11/13/2024

3) 12/26/2024


Both the Python and Java programs require Python and Java to be installed and added to PATH appropriately.


To run the Python program you can follow these steps:

1) Navigate to the python_program_files directory in your terminal.

2) Run the following command:

python query_program.py


To run the Java program you can follow these steps:

1) Navigate to the java_program_files directory in your terminal.

2) Run the following commands:

javac -d bin src/QueryProgram.java

java -cp ".;library/sqlite-jdbc-3.47.1.0.jar;bin" QueryProgram


For the supplemental task I added additional query functionalities to the Python program to retrieve information on theaters and movies. I also included additional data points for testing to bring the total number of sales entries to 36 and added query parameterization in both programs for additional security against SQL injection attacks.