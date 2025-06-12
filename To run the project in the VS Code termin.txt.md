To run the project in the VS Code terminal, follow these steps:

Start the Java backend:
Open a terminal in VS Code.
Navigate to the WasteManagementSystem directory: cd WasteManagementSystem
Compile and run the Java code using Maven: mvn clean install tomcat7:run (This assumes you have the Tomcat Maven plugin configured in your pom.xml). If you don't have the Tomcat plugin, you can use mvn clean install jetty:run if you have the Jetty plugin, or deploy the .war file to a separate Tomcat server.
Start the Node.js frontend (if applicable):
Open a new terminal in VS Code.
Navigate to the WasteManagementSystem directory: cd WasteManagementSystem
Install the Node.js dependencies: npm install
Start the Node.js server: npm start or node server.js (depending on your package.json configuration).
Access the application in the browser:
Open your web browser and navigate to the appropriate URL (e.g., http://localhost:8080 for the Java backend or http://localhost:3000 for the Node.js frontend).

