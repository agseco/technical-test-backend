# About my solution

The core logic resides within domain model, encapsulating behavior alongside data. This ensures business rules are explicit and self-contained.

The application follows the Ports & Adapters (Hexagonal Architecture) approach, where the domain is decoupled from frameworks and infrastructure. However, implementation of incoming ports (e.g., a use case interface) has been omitted for simplicity.

Additionally and also for simplicity, some Spring annotations have been leaked into the domain, they could be removed with further refactoring.

Finally, I have put an emphasis in preventing concurrent modifications to the wallet balance. The repository layer enforces pessimistic locking to ensure transactional integrity.

Please, also see _Improvements_ section bellow.

# How to run it

Running tests suite:

```shell
mvn test
```

Running app with `develop` profile (default):

```shell
mvn spring-boot:run
```

The app is preloaded with a wallet whose ID is `b4efa483-376d-4ef5-9383-cf16cb49f28d`.


## API

Fetch/get wallet request:

```shell
curl --location 'localhost:8090/wallet/b4efa483-376d-4ef5-9383-cf16cb49f28d'
```

Top up request:

```shell
curl --location --request PUT 'localhost:8090/wallet/b4efa483-376d-4ef5-9383-cf16cb49f28d' \
--header 'Content-Type: application/json' \
--data '{
    "cardNumber": "4242424242424242",
    "amount": 50
}'
```

# Improvements

Potential improvements should this be a real project:

- Structure
  - Using `Amount` entity/abstraction at domain level, instead of using `BigDecimal` directly
  - The `Wallet` domain entity may contain the list of transactions
  - Payment domain classes could be split into its own subdomain, i.e. separated from `wallet`
  - Using `@Configuration`/`@Bean` for wiring, instead of @Autowired and removing `@Autowired`
  - Find a way to get rid of `@Transactional` annotation in the domain
  - Usage of ArchUnit to enforce Ports&Adapters architecture, dependency direction and prevent illegal imports, among other
- Tests
  - Integration tests with Testcontainers, using the actual DB used in production
  - It'd be ideal to cover in an integration test that concurrent modifications of the wallet are actually not allowed
  - Base integration test class (e.g. to avoid repeating `@ActiveProfiles("test")`)
  - End to end/acceptance test which mocks the payment gateway (in such case `WalletControllerIT` may be redundant)
- Other
  - Security filters to prevent attacks such as SQL Injection
  - Proper error responses (JSON) instead of empty body
  - Logging and metrics
