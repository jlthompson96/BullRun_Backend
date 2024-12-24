# BullRun Backend
<img src = "https://github.com/jlthompson96/BullRun_Frontend/blob/main/src/assets/Designer.png" height= "200px" alt = "BullRun Logo">
A Spring application to manage financial portfolios and provide financial literacy to users.

## Goals
- [ ] Individual Stock Analysis
- [ ] Learning Center to help users gain financial literacy
- [ ] Track portfolio performance over time
- [ ] Keep up with the latest news in the market
- [ ] Dashboard to track all of the above

## Technologies
- Java 21 <img align="left" alt="Java" width="26px" src="https://github.com/jlthompson96/vscode-material-icon-theme/blob/master/icons/java.svg" />
- Spring Boot <img align="left" alt="JavaScipt" width="26px" src="https://github.com/jlthompson96/vscode-material-icon-theme/blob/master/icons/mint.svg" />
- Gradle <img align="left" alt="JavaScipt" width="26px" src="https://github.com/jlthompson96/vscode-material-icon-theme/blob/master/icons/gradle.svg" />
- MongoDB <img align="left" alt="JavaScipt" width="26px" src="https://github.com/jlthompson96/vscode-material-icon-theme/blob/master/icons/mint.svg" />
- TwelveData API <img align="left" alt="JavaScipt" width="26px" src="https://avatars.githubusercontent.com/u/59389925?s=200&v=4" />


# Controller Endpoints Documentation

## Table of Contents

- [StockDataController](#stockdatacontroller)
    - [GET `/companyProfile`](#get-companyprofile)
    - [GET `/stockPrice`](#get-stockprice)
    - [GET `/companyLogo`](#get-companylogo)
    - [GET `/previousClose`](#get-previousclose)
    - [GET `/stockNews`](#get-stocknews)
    - [GET `/stockExistsInDB`](#get-stockexistsindb)
    - [POST `/addStock`](#post-addstock)
    - [DELETE `/deleteStock`](#delete-deletestock)
    - [PUT `/updateSharesOwned`](#put-updatesharesowned)
- [UserController](#usercontroller)
    - [GET `/getUserList`](#get-getuserlist)
    - [POST `/getUser`](#post-getuser)
    - [POST `/createUser`](#post-createuser)
    - [GET `/getUserStocks`](#get-getuserstocks)

---

## StockDataController

This controller handles all operations related to stock data, such as fetching company profiles, stock prices, and managing stock records.

### Endpoints

- **GET `/companyProfile`**
    - Method: `getCompanyProfile`
    - Description: Retrieves the profile information of a company.
    - Request: No parameters required.
    - Response:
      ```json
      {
        "name": "Company Name",
        "industry": "Industry",
        "description": "Description"
      }
      ```
    - Errors:
        - `404 Not Found`: If the company profile is not available.

- **GET `/stockPrice`**
    - Method: `getStockPrice`
    - Description: Fetches the current stock price of a company.
    - Request:
      ```json
      {
        "symbol": "AAPL"
      }
      ```
    - Response:
      ```json
      {
        "symbol": "AAPL",
        "price": 150.25
      }
      ```
    - Errors:
        - `400 Bad Request`: If the symbol is missing or invalid.

- **GET `/companyLogo`**
    - Method: `getCompanyLogo`
    - Description: Retrieves the logo of a company.
    - Request:
      ```json
      {
        "symbol": "AAPL"
      }
      ```
    - Response:
      ```json
      {
        "logoUrl": "https://example.com/logo.png"
      }
      ```
    - Errors:
        - `404 Not Found`: If the logo is not available.

- **GET `/previousClose`**
    - Method: `getPreviousClose`
    - Description: Retrieves the previous closing price of a stock.
    - Request:
      ```json
      {
        "symbol": "AAPL"
      }
      ```
    - Response:
      ```json
      {
        "symbol": "AAPL",
        "previousClose": 149.50
      }
      ```

- **GET `/stockNews`**
    - Method: `getRSSFeed`
    - Description: Fetches the latest stock-related news.
    - Response: Returns an array of news articles.

- **GET `/stockExistsInDB`**
    - Method: `stockExistsInDB`
    - Description: Checks if a stock exists in the database.
    - Request:
      ```json
      {
        "symbol": "AAPL"
      }
      ```
    - Response:
      ```json
      {
        "exists": true
      }
      ```

- **POST `/addStock`**
    - Method: `addStock`
    - Description: Adds a new stock to the database.
    - Request:
      ```json
      {
        "symbol": "AAPL",
        "shares": 10
      }
      ```
    - Response:
      ```json
      {
        "message": "Stock added successfully."
      }
      ```

- **DELETE `/deleteStock`**
    - Method: `deleteStock`
    - Description: Deletes a stock from the database.
    - Request:
      ```json
      {
        "symbol": "AAPL"
      }
      ```
    - Response:
      ```json
      {
        "message": "Stock deleted successfully."
      }
      ```

- **PUT `/updateSharesOwned`**
    - Method: `updateSharesOwned`
    - Description: Updates the number of shares owned for a stock.
    - Request:
      ```json
      {
        "symbol": "AAPL",
        "shares": 20
      }
      ```
    - Response:
      ```json
      {
        "message": "Shares updated successfully."
      }
      ```

---

## UserController

This controller manages user-related operations such as fetching user details and managing user accounts.

### Endpoints

- **GET `/getUserList`**
    - Method: `getUserList`
    - Description: Retrieves a list of all users.
    - Response:
      ```json
      [
        {
          "id": 1,
          "name": "John Doe"
        },
        {
          "id": 2,
          "name": "Jane Smith"
        }
      ]
      ```

- **POST `/getUser`**
    - Method: `getUser`
    - Description: Retrieves the details of a specific user.
    - Request:
      ```json
      {
        "userId": 1
      }
      ```
    - Response:
      ```json
      {
        "id": 1,
        "name": "John Doe",
        "email": "john.doe@example.com"
      }
      ```

- **POST `/createUser`**
    - Method: `createUser`
    - Description: Creates a new user account.
    - Request:
      ```json
      {
        "name": "John Doe",
        "email": "john.doe@example.com"
      }
      ```
    - Response:
      ```json
      {
        "message": "User created successfully.",
        "userId": 1
      }
      ```

- **GET `/getUserStocks`**
    - Method: `getUserStocks`
    - Description: Retrieves the stocks owned by a specific user.
    - Request:
      ```json
      {
        "userId": 1
      }
      ```
    - Response:
      ```json
      [
        {
          "symbol": "AAPL",
          "shares": 10
        },
        {
          "symbol": "GOOG",
          "shares": 5
        }
      ]
      ```

