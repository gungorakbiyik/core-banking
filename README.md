# core-banking

A core banking domain model in **Java 21 / Spring Boot**, built to practice
**Domain-Driven Design** tactical patterns on a realistic banking domain —
accounts, ledgers, double-entry posting, and money handling.

This is a hands-on learning project, public from day one. The goal isn't a
production banking system, but a clean, well-tested reference for how the DDD
building blocks — value objects, entities, aggregates, repositories, services —
come together in a domain where correctness actually matters.

> 📖 The reasoning behind this code is written up as articles:
> [Domain-Driven Design — Level 1 series](https://medium.com/@gungor.akbiyik)
> · [Account vs. Ledger — Foundations of Core Banking](https://medium.com/@gungor.akbiyik/account-vs-ledger-core-bankingin-temelleri-7958fa4b87b8)

## Tech

- Java 21
- Spring Boot 4
- Maven
- JUnit 5

> PostgreSQL, Kafka and Docker will be introduced as the domain grows — see the roadmap below.

## Domain Roadmap

DDD tactical patterns, built one at a time on a banking domain:

- [x] **Money** — immutable value object (currency-safe arithmetic, HALF_EVEN rounding)
- [ ] **Account** — aggregate root with balance invariants
- [ ] **Ledger & double-entry posting** — debit/credit, T-account consistency
- [ ] **Repository** — persistence abstraction for aggregates
- [ ] **Application services** — use cases (open account, deposit, transfer)
- [ ] **Integration tests** — Testcontainers + PostgreSQL

## Status

Early stage — building one pattern at a time, each with tests. Follow along.
