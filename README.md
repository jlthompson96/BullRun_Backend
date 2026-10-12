<p align="center">
  <img src="docs/assets/banner.svg" alt="BullRun Backend — the Spring Boot API behind BullRun" width="100%">
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-6db33f?style=for-the-badge&logo=openjdk&logoColor=white&labelColor=0d1410" alt="Java 21">
  <img src="https://img.shields.io/badge/Spring_Boot-3.4-6db33f?style=for-the-badge&logo=springboot&logoColor=white&labelColor=0d1410" alt="Spring Boot 3.4">
  <img src="https://img.shields.io/badge/Gradle-build-6db33f?style=for-the-badge&logo=gradle&logoColor=white&labelColor=0d1410" alt="Gradle">
  <img src="https://img.shields.io/badge/MongoDB-data-47a248?style=for-the-badge&logo=mongodb&logoColor=white&labelColor=0d1410" alt="MongoDB">
  <img src="https://img.shields.io/badge/Docker-ready-47a248?style=for-the-badge&logo=docker&logoColor=white&labelColor=0d1410" alt="Docker">
  <img src="https://img.shields.io/badge/license-MIT-47a248?style=for-the-badge&labelColor=0d1410" alt="MIT license">
</p>

# BullRun Backend

The Spring Boot API behind [BullRun](https://github.com/jlthompson96/BullRun_Frontend), an app for managing a stock portfolio and building financial literacy. It wraps the market data providers behind one API, turns news feeds into JSON, stores the portfolio in MongoDB, and reprices every holding on a schedule.

## What it does

<p align="center">
  <img src="docs/assets/capabilities.svg" alt="BullRun Backend capabilities: market data, index prices, stock news, portfolio, users and a nightly repricing job" width="100%">
</p>

- **Market data.** Latest price, previous close and company logo from [TwelveData](https://twelvedata.com/), and the company profile from [Polygon](https://polygon.io/).
- **Index prices.** The Dow Jones Industrial Average (`DJI`), S&P 500 (`SPX`) and Nasdaq Composite (`IXIC`) in one call, formatted for display.
- **Stock news.** Pulls a ticker's Yahoo Finance RSS feed and converts the XML to a JSON array of articles.
- **Portfolio.** Add a holding (its price and logo are fetched on the way in), update the shares you own, delete a holding, or check whether a symbol is already in the portfolio.
- **Portfolio history.** A scheduled job reprices every holding and saves a snapshot of the total portfolio value, which `/users/getPortfolioValueOverTime` returns as a time series.
- **Users.** List, look up and create user records.

## How it works

```mermaid
flowchart LR
    FE["📈 BullRun Frontend"] -- "HTTP Basic<br/>/stockData/* · /users/*" --> API
    subgraph API["🐂 BullRun Backend (Spring Boot)"]
        SC["StockDataController"]
        UC["UserController"]
        JOB["DailyStockDataService<br/>⏰ scheduled"]
    end
    SC --> TD["TwelveData<br/>price · EOD · logo"]
    SC --> PG["Polygon<br/>ticker reference"]
    SC --> YF["Yahoo Finance RSS"]
    JOB --> TD
    SC <--> DB[("MongoDB<br/>Stock · Users")]
    UC <--> DB
    JOB <--> DB
```

The repricing job runs once at startup and then at midnight, Monday to Saturday, New York time:

```mermaid
sequenceDiagram
    participant J as DailyStockDataService
    participant M as MongoDB
    participant T as TwelveData
    Note over J: @PostConstruct and cron 0 0 0 * * 1-6 (America/New_York)
    J->>M: Load every holding (Stock)
    loop each holding
        J->>T: Latest price and logo for symbol
        T-->>J: price, logo
        J->>J: currentValue = price × sharesOwned
    end
    J->>M: Save updated holdings
    J->>M: Save total portfolio value snapshot
```

## API

All endpoints except `/actuator/**` require HTTP Basic authentication. The user is an in-memory test account defined in [SecurityConfig.java](src/main/java/com/thompson/bullrun/config/SecurityConfig.java); replace it before exposing the API publicly. CORS is open to all origins.

### `/stockData`

| Method | Path | Input | Returns |
| --- | --- | --- | --- |
| `GET` | `/stockData/stockPrice` | `?symbol=AAPL` | TwelveData price |
| `GET` | `/stockData/previousClose` | `?symbol=AAPL` | TwelveData end-of-day quote |
| `GET` | `/stockData/companyLogo` | `?symbol=AAPL` | TwelveData logo URL |
| `GET` | `/stockData/companyProfile` | `?symbol=AAPL` | Polygon ticker details |
| `GET` | `/stockData/indexPrices` | — | `{ "DJI": "…", "SPX": "…", "IXIC": "…" }` |
| `GET` | `/stockData/stockNews` | `?symbol=AAPL` | JSON array of RSS items |
| `GET` | `/stockData/stockExistsInDB` | `?symbol=AAPL` | `true` / `false` |
| `POST` | `/stockData/addStock` | `{ "symbol", "name", "sharesOwned", "costBasis" }` | The saved stock, priced |
| `PUT` | `/stockData/updateSharesOwned` | `{ "symbol", "sharesOwned" }` | Confirmation message |
| `DELETE` | `/stockData/deleteStock` | `{ "stockTicker" }` | Confirmation message |

### `/users`

| Method | Path | Input | Returns |
| --- | --- | --- | --- |
| `GET` | `/users/getUserList` | — | All user records |
| `POST` | `/users/getUser` | User JSON | The matching user |
| `POST` | `/users/createUser` | `{ "firstName", "lastName", … }` | The created user |
| `GET` | `/users/getUserStocks` | — | Every holding in the portfolio |
| `GET` | `/users/getPortfolioValueOverTime` | — | `[{ "date", "totalValue" }, …]` |

There is also a plain `GET /health` check.

### Example

```bash
curl -u <user>:<password> "http://localhost:8080/stockData/stockPrice?symbol=AAPL"

curl -u <user>:<password> -X POST http://localhost:8080/stockData/addStock \
  -H "Content-Type: application/json" \
  -d '{"symbol":"AAPL","name":"Apple Inc.","sharesOwned":10,"costBasis":150.25}'
```

## Configuration

Secrets come from environment variables, referenced in [application.properties](src/main/resources/application.properties):

| Variable | Used for |
| --- | --- |
| `TWELVE_DATA_API_KEY` | Price, previous close, logo and index prices |
| `POLYGON_API_KEY` | Company profile |
| `MONGODB_URI` | MongoDB connection string |
| `MONGODB_DATABASE` | Database name |

The `prod` profile is active by default; `application-local.properties` is available for local overrides.

## Getting started

### Prerequisites

- Java 21
- A MongoDB database
- TwelveData and Polygon API keys

### Run locally

```bash
export TWELVE_DATA_API_KEY=...
export POLYGON_API_KEY=...
export MONGODB_URI=mongodb://localhost:27017
export MONGODB_DATABASE=bullrun

./gradlew bootRun
```

The API listens on http://localhost:8080.

### Run with Docker

The [Dockerfile](Dockerfile) packages the built jar on an ARM-compatible Azul Zulu JDK 21 image:

```bash
./gradlew bootJar
docker build -t bullrun-backend .
docker run -p 8080:8080 \
  -e TWELVE_DATA_API_KEY -e POLYGON_API_KEY -e MONGODB_URI -e MONGODB_DATABASE \
  bullrun-backend
```

## Project structure

```
src/main/java/com/thompson/bullrun/
├── BullRunApplication.java   # Entry point, enables scheduling
├── controller/               # StockDataController, UserController
├── services/                 # StockService, UserService, DailyStockDataService (scheduled repricing)
├── repositories/             # Spring Data MongoDB repositories
├── entities/                 # StockEntity, UserEntity, PortfolioValue
├── config/                   # Security (Basic auth, CORS), Mongo, RestTemplate, API properties
├── common/                   # Health check, REST and Mongo logging
└── exceptions/               # StockDataException
src/main/resources/
├── application*.properties   # Profiles and provider URLs
└── asciiBoot.txt             # The startup banner
```

## License

[MIT](LICENSE) © 2023 Joey Thompson
