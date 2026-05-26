# BFHL Spring Boot Backend API

A production-ready, highly clean, and layered Spring Boot REST API for character and digit classification. This project processes an input array containing mixed strings, digits, alphabets, and special characters. It categorizes them, calculates their sum, and constructs a reversed alternating-case alphabetic string.

## Tech Stack
- **Language**: Java 17+ (Compiles successfully with Java 24 JDK)
- **Framework**: Spring Boot 3.2.x
- **Build Tool**: Apache Maven 3.9+
- **Testing**: JUnit 5, Spring Boot Test, MockMvc
- **Architecture**: Layered Architecture (Controller -> Service -> Utility/Parser -> DTO)

---

## API Documentation

### 1. Process Data (POST)
Processes the incoming array, parses the strings, and classifies elements.

- **Route**: `/bfhl`
- **Method**: `POST`
- **Headers**: `Content-Type: application/json`
- **Success Response Code**: `200 OK`

#### Sample Request Body
```json
{
  "data": ["a", "1", "334", "4", "R", "$"]
}
```

#### Sample Response Body
```json
{
  "is_success": true,
  "user_id": "john_doe_17091999",
  "email": "john.doe@example.com",
  "roll_number": "ABCD123",
  "odd_numbers": ["1"],
  "even_numbers": ["334", "4"],
  "alphabets": ["A", "R"],
  "special_characters": ["$"],
  "sum": "339",
  "concat_string": "Ra"
}
```

---

### 2. Operation Code (GET)
Returns the operating code for verification.

- **Route**: `/bfhl`
- **Method**: `GET`
- **Success Response Code**: `200 OK`

#### Sample Response Body
```json
{
  "operation_code": 1
}
```

---

## Configuration Settings
You can modify the user profile settings returned in the API responses directly from the `src/main/resources/application.properties` file:

```properties
bfhl.user.name=john_doe
bfhl.user.dob=17091999
bfhl.user.email=john.doe@example.com
bfhl.user.roll-number=ABCD123
```
- `user_id` format in the response will automatically render as: `lowercase(bfhl.user.name)_bfhl.user.dob`.

---

## How to Run Locally

### Prerequisites
- JDK 17 or higher
- Maven 3.9+ (Or use the portable Maven provided in the workspace)

### Building the Project
Navigate to the project root directory and package the application:
```bash
# Using global maven
mvn clean package

# Or using the portable Maven downloaded in the scratch directory:
# Windows (PowerShell)
& "C:\Users\trish\.gemini\antigravity-ide\scratch\maven\apache-maven-3.9.6\bin\mvn.cmd" clean package
```

### Running Tests
To run all automated JUnit tests:
```bash
# Windows (PowerShell)
& "C:\Users\trish\.gemini\antigravity-ide\scratch\maven\apache-maven-3.9.6\bin\mvn.cmd" test
```

### Starting the Application
To run the Spring Boot application locally:
```bash
# Windows (PowerShell)
& "C:\Users\trish\.gemini\antigravity-ide\scratch\maven\apache-maven-3.9.6\bin\mvn.cmd" spring-boot:run
```
The application will start on port `8080`. You can test it at `http://localhost:8080/bfhl`.

---

## Deployment Steps

### 1. Push to GitHub
1. Create a new public repository on GitHub.
2. Initialize Git in the project root:
   ```bash
   git init
   git add .
   git commit -m "Initial commit of BFHL Spring Boot API"
   ```
3. Link the remote and push:
   ```bash
   git remote add origin <your-github-repo-url>
   git branch -M main
   git push -u origin main
   ```

### 2. Deploy on Render
1. Sign up on [Render](https://render.com/).
2. Create a new **Web Service** and connect your GitHub repository.
3. Configure the web service settings:
   - **Environment/Language**: `Docker` or `Java`
   - **Build Command**: `./mvnw clean install -DskipTests` (if using Maven Wrapper) or `mvn clean install -DskipTests`
   - **Start Command**: `java -jar target/bfhl-api-0.0.1-SNAPSHOT.jar`
   - **Java Version**: Set environment variable `JAVA_VERSION = 17` or `21`.
4. Deploy the service. Render will provision a public URL (e.g. `https://bfhl-api.onrender.com`).
5. Your endpoint will be accessible at: `https://your-domain.onrender.com/bfhl`.

### 3. Deploy on Railway
1. Sign up on [Railway](https://railway.app/).
2. Click **New Project** -> **Deploy from GitHub repo**.
3. Connect your repository. Railway automatically detects Maven/Spring Boot projects and handles building/running via Cloud Native Buildpacks (configuring Java 17+).
4. Expose the domain in the service settings to make it public.
5. Your endpoint will be accessible at: `https://your-domain.up.railway.app/bfhl`.
