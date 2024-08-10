Assignment 1 
(BONUS MARK IMPLEMENTED)

Setting up environment:
Step (1): Compile the documents using command "javac *.java" 
          This should compile all the provided files. 
            

Step (2) Check that the port is not being used using command "lsof -i :1099 "

Step (3) If it is, use command "kill PID"

Start the JAVA RMI: 
 Run the command "rmiregistry &", this should run the program on port 1099

Start the Server: 
 Run the command "java CalculatorServer", this should start the server. The following message should appear:
calculator/Client1 is ready.
calculator/Client2 is ready.
calculator/Client3 is ready.
calculator/Client4 is ready.
calculator/Client5 is ready.
Server ready

Start the Client: 
In a different terminal, start the client server by running the command "java CalculatorServer"

The test should run and show outputs. 

          


