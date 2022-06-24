

## Business Features Implemented
- tracking items
- tracking item view counts
- tracking item quantity for sale
- retrieve all items
- retrieve item details
- surge pricing
- order an item
- authenticate the user orders

## Java Command
Java 11 runtime is required the operating system in order to start up the application using the following command.

```
java -jar gilded-rose-1.0-SNAPSHOT.jar
```
## Maven Commands
Do clean build
```
mvn clean package
```
Run unit tests and integration tests
```
mvn clean verify
```
Run only integration tests
```
mvn failsafe:integration-test -Pit-tests
```

## Uber Surge Pricing Logic

A table called item_view keeps track of the number of views an item. When a view on an item occurs, an entry of item id with timestamp is created into the table.  A query is performed to get the rows count of an item between the current timestamp and the timestamp of an hour ago.  If the result of the count is greater than 10 then a 10% increase on the selling price will happen.  Whenever a customer request to buy an item, that query will be performed and the selling price will be updated accordingly.

When a user submits order request, it is not counted as a view thus no entry is created.

## System Design
The system contains the following layers suggested by industry practices.
- controllers
- facades
- services
- repositories

## Data Model
This describes the model in the database.
```sql
-- store items
CREATE TABLE item (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(200),
    price NUMERIC(20, 2) NOT NULL CHECK (price > 0)
);
```
```sql
-- store the view count on items
CREATE TABLE item_view (
    id INT AUTO_INCREMENT PRIMARY KEY,
    item_Id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```
```sql
-- store the quantity for sale for items
CREATE TABLE item_stock (
    id INT AUTO_INCREMENT PRIMARY KEY,
    item_id INT NOT NULL UNIQUE,
    quantity_for_sell INT NOT NULL,
    FOREIGN KEY (item_id) REFERENCES item (id)
);
```
## Data Access Transfer Object
A dto (data access transfer) object is defined for converting data from and to the data model.  DTOs are used in the controller and facade layers while the data model object are seen in the service and repository layers.

## Security
By using Spring security, the endpoint for ordering an item is secured.  Any request of ordering must be authenticated with basic authentication, namely the username and password.

## API Documentation
An api documentation is generated by using Swagger Inspector to inspect the running of the requests on localhost.  The openapi.yaml contains the Restful endpoint.
## Requests and Responses

### Find all Items

Http Request Url
```http request
localhost:8088/api/gilded-rose/inventory/items
```
Response
```
HTTP/1.1 200 
Connection: keep-alive
Content-Type: application/json
Date: Thu, 23 Jun 2022 18:38:04 GMT
Keep-Alive: timeout=60
Transfer-Encoding: chunked

[
    {
        "description": "Mirror",
        "id": 1,
        "name": "Fisheye Store Mirror",
        "price": 100.25
    },
    {
        "description": "Painting",
        "id": 2,
        "name": "USS Maine Ship's Portrait",
        "price": 500.0
    },
    {
        "description": "Painting",
        "id": 3,
        "name": "Oak Island",
        "price": 300.0
    },
    {
        "description": "Rack",
        "id": 4,
        "name": "Bullet-Holed Deer Rack",
        "price": 200.0
    }
]
```
### Find item by name
Http Request Url
```http request
localhost:8088/api/gilded-rose/inventory/item/'Fisheye Store Mirror'
```
Response
```
HTTP/1.1 200 
Connection: keep-alive
Content-Type: application/json
Date: Thu, 23 Jun 2022 18:43:07 GMT
Keep-Alive: timeout=60
Transfer-Encoding: chunked

{
    "description": "Mirror",
    "id": 1,
    "name": "Fisheye Store Mirror",
    "price": 100.25
}
```
### Order an item
Http Request Url
```http request Url
localhost:8088/api/gilded-rose/ordering/request
```
Authorization
```
Type: Basic Auth
username: foo
password: bar
```
Request body
```json
{
  "name": "Fisheye Store Mirror",
  "quantity": 1
}
```
Response
```
HTTP/1.1 201 
Connection: keep-alive
Content-Type: application/json
Date: Thu, 23 Jun 2022 18:48:15 GMT
Keep-Alive: timeout=60
Location: /createdOrder/
Transfer-Encoding: chunked

{
    "errorMessage": null,
    "name": "Fisheye Store Mirror",
    "orderNumber": "60ee1f2d-a607-4492-8a45-b8141be62065",
    "price": 100.25,
    "quantity": 1
}
```
### Sample commands using HTTPie
```
http localhost:8088/api/gilded-rose/inventory/item/'Fisheye Store Mirror'
http localhost:8088/api/gilded-rose/inventory/item/"USS Maine Ship's Portrait"
http localhost:8088/api/gilded-rose/inventory/items
http -a foo:bar POST localhost:8088/api/gilded-rose/ordering/request name='Fisheye Store Mirror' quantity=1
```

### Unit tests
Unit tests are implemented on most of the classes except the following class not covering all unit tests due to time restriction.

- InventoryFacade
- OrderFacade
- OrderController
- InventoryController

### Integration Tests
Integrations tests are implemented using SpringBoot TestRestTemplate to send the Http request to the web server to test business scenarios.  It tests all the communication between layers out web server and from web server to the SpringBoot application.

The tests cover the following business scenarios
- Retrieve all items successfully
- Find an item by name successfully
- Fail to find an item not existing
- Order an item with the base price successfully
- Order an item with the surged price successfully
- Fail to order an item not existing
- Fail to order an item if not sufficient quantity for sale
- Keep track of item quantity for sale
- Keep track of view count on items

### Improvement
- Use Cucumber for BDD testing
- Implement test on simulating high number of concurrent ordering on an item
- Implement Case-sensitive search
- Enable foreign key constraints define in Entity class
- Implement pagination
- The unique constraints are dropped in the schema.sql for integration test due to violating constraint when executing data.sql
- spring.jpa.hibernate.ddl-auto is set to create-drop for integration testing
- Enable federated SSO






