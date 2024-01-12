# Retrospective Service API

This Spring Boot application provides a RESTful API for managing retrospectives and associated feedback items.

## Endpoints

### 1. Get All Retrospectives
- **Endpoint:** `/retrospectives`
- **Method:** GET
- **Description:** Retrieve a paginated list of all retrospectives.
- **Parameters:**
    - `currentPage` (optional): Page number (default: 0)
    - `size` (optional): Number of items per page (default: 10)

### 2. Search Retrospectives by Date
- **Endpoint:** `/retrospectives/searchByDate`
- **Method:** GET
- **Description:** Search retrospectives based on a specified date.
- **Parameters:**
    - `date` (required): Date in the format 'yyyy-MM-dd'
    - `currentPage` (optional): Page number (default: 0)
    - `size` (optional): Number of items per page (default: 10)

### 3. Create a Retrospective
- **Endpoint:** `/retrospectives/create`
- **Method:** POST
- **Description:** Create a new retrospective.
- **Request Body:** Retrospective object

### 4. Add Feedback to a Retrospective
- **Endpoint:** `/retrospectives/{retrospectiveName}/addFeedback`
- **Method:** POST
- **Description:** Add feedback to a specified retrospective.
- **Path Variable:** `retrospectiveName`
- **Request Body:** Feedback object

### 5. Update Feedback Item in a Retrospective
- **Endpoint:** `/retrospectives/{retrospectiveName}/updateFeedbackItem`
- **Method:** PUT
- **Description:** Update a feedback item in a specified retrospective.
- **Path Variable:** `retrospectiveName`
- **Request Body:** Feedback object

## Dependencies
- Spring Boot
- Spring Data JPA
- Spring Web
- SLF4J for logging

## Usage
### Prerequisites
- Java Development Kit (JDK) installed
### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/jayamalichathu/Retrospective.git
2. Import the project into your preferred Integrated Development Environment (IDE).
3. Run the application.
- Ensure the application is running.
- Use your preferred API client (e.g., Postman) to interact with the provided endpoints.


