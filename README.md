GETTING STARTED WITH LIGHT WEIGHT REPORTER!
Light weight reporter is a simple web based report/dashboard generation tool. It lets you create simple reports/dashboard very easily. The package consists of war file that you need to deploy on a application server like tomcat.
Prerequisite
Tomcat Application Server v8.0 and above
Required JDBC Driver(s) to connect to database(s)
Installation
Copy the lwr.war file downloaded from sourceforge.net and place it under CATALINA_HOME/webapps/ directory. Light weight reporter uses JDBC to connect to database(s). The JDBC drivers for Postgres and MySQL are already bundled into the war file. To connect to any other database vendor, copy the required vendor specific JDBC jar file to CATALINA_HOME/webapps/lwr/WEB-INF/lib folder and restart the tomcate server
Step 1
Access the application using below URL, 

http://<hostname>:<port>/lwr 

Where, port -> port on which tomcat is listening
Step 2
Once you open this URL in your browsers you would be redirected to login page that asks you for username and password. The default username is "admin" and default password is "admin". It is highly recommended to change the password for "admin" user by navigating to Administration->User Management page. You can create more users.
Step 3
Create a database connection by going to Administrator -> Connection Management. connection is created by providing
Username
Password
JDBC Driver Class
JDBC Driver URL
Once these properties are keyed in, performn test connection to check if the connectivity is established using above mentioned properties. If test connection is successful, save the connection details.
The application lets you define one or more such connections pointing to different databases and with in a report we can pick data from different connections.

Step 4
Building a report by going to File -> New Report this involves multiple elements arranged in rows and columns. For each of the element you need to provide
Title
SQL Query
Chart Type
Database Connection
A single report having multiple elements can retrive data from one or more databases. Support Chart Types are
Pie Chart
Line Chart
Bar Chart
Bar Chart Stacked
Column Chart
Column Chart Stacked
Table Chart
All these charts are google charts and you need to be connected to internet for it to render the HTML
