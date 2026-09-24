# r8lim8r 🚦

A **policy-driven, multi-tier API rate limiter written in Java**.

`r8lim8r` dynamically applies different rate-limiting policies based on a user's subscription tier. The project is designed around separation of concerns, making it easy to introduce new rate-limiting algorithms and policy-resolution rules without changing the core rate limiter.

## ✨ Features

* 🎯 **Tier-based rate limiting**

    * `FREE`
    * `PREMIUM`
    * `ENTERPRISE`
* 🔀 **Policy-based architecture**
* 🧩 Pluggable rate-limiting strategies
* 🪟 Fixed Window rate-limiting implementation
* 🏗️ Designed to support additional strategies such as:

    * Sliding Window
    * Token Bucket
* 💾 Separate state stores for different rate-limiting algorithms
* 🔌 Policy resolution is separated from rate-limit execution
* 🧪 Simple command-line example for experimenting with the limiter

## 🏛️ Architecture

The project separates the rate limiter into several components:

```text
                    ┌──────────────────┐
                    │      User        │
                    │  ID + Tier       │
                    └────────┬─────────┘
                             │
                             ▼
                    ┌──────────────────┐
                    │   RateLimiter    │
                    └────────┬─────────┘
                             │
                             ▼
                    ┌──────────────────┐
                    │ PolicyResolver   │
                    └────────┬─────────┘
                             │
                ┌────────────┼────────────┐
                ▼            ▼            ▼
             FREE        PREMIUM     ENTERPRISE
                │            │            │
                └────────────┼────────────┘
                             ▼
                    ┌──────────────────┐
                    │ RateLimitPolicy  │
                    │                  │
                    │ limit            │
                    │ window size      │
                    │ strategy         │
                    └────────┬─────────┘
                             │
                             ▼
                    ┌──────────────────┐
                    │ Rate Limiting    │
                    │    Strategy      │
                    └────────┬─────────┘
                             │
              ┌──────────────┼──────────────┐
              ▼              ▼              ▼
        Fixed Window   Sliding Window  Token Bucket
              │
              ▼
         State Store
```

The `RateLimiter` itself is intentionally small: it resolves a policy for the user and delegates the actual decision to the policy's strategy.

## 📁 Project Structure

```text
r8lim8r/
│
├── entities/
│   └── User.java
│
├── enums/
│   └── Tier.java
│
├── policies/
│   └── RateLimitPolicy.java
│
├── policyResolver/
│   ├── PolicyResolver.java
│   └── DefaultPolicyResolver.java
│
├── rateLimitingStrategy/
│   ├── RateLimitingStrategy.java
│   ├── FixedWindowRateLimitingStrategy.java
│   ├── SlidingWindowRateLimitingStrategy.java
│   └── TokenBucketRateLimitingStrategy.java
│
├── stateStore/
│   ├── FixedWindowState.java
│   ├── FixedWindowStore.java
│   ├── SlidingWindowState.java
│   ├── SlidingWindowStore.java
│   ├── TokenBucketState.java
│   └── TokenBucketStore.java
│
├── RateLimiter.java
└── RunRateLimiter.java
```

The repository currently contains separate packages for users, tiers, policies, policy resolution, strategies, and state management.

## ⚙️ Current Policies

The default policy resolver currently defines the following limits:

| Tier       | Requests | Window    |
| ---------- | -------- | --------- |
| FREE       | 2        | 2 seconds |
| PREMIUM    | 4        | 2 seconds |
| ENTERPRISE | 10       | 2 seconds |

All three tiers currently use the `FixedWindowRateLimitingStrategy`.

These values are configured in `DefaultPolicyResolver` and can be changed without modifying the core `RateLimiter`.

## 🔄 How It Works

A request follows this flow:

```text
Request
   │
   ▼
User
   │
   ▼
RateLimiter.isRequestAllowed(user)
   │
   ▼
PolicyResolver.resolve(user)
   │
   ▼
RateLimitPolicy
   │
   ▼
RateLimitingStrategy.isRequestAllowed(...)
   │
   ▼
Allowed / Rate Limited
```

For the current fixed-window implementation:

1. The user's ID is used to retrieve or create rate-limit state.
2. The current timestamp is checked against the window start.
3. If the window has expired, the counter is reset.
4. If the request count has reached the configured limit, the request is rejected.
5. Otherwise, the request counter is incremented and the request is allowed.

## 🚀 Getting Started

### Prerequisites

You need a Java Development Kit installed on your machine.

Verify Java:

```bash
java -version
javac -version
```

### Clone the Repository

```bash
git clone https://github.com/riteshasutkar/r8lim8r.git
cd r8lim8r
```

### Compile

Because the repository is currently a small Java project without a visible Maven or Gradle build configuration, it can be compiled directly with `javac`.

From the repository root:

```bash
javac entities/*.java \
      enums/*.java \
      policies/*.java \
      policyResolver/*.java \
      rateLimitingStrategy/*.java \
      stateStore/*.java \
      RateLimiter.java \
      RunRateLimiter.java
```

### Run

```bash
java RunRateLimiter
```

## 🖥️ CLI Example

`RunRateLimiter` creates three example users:

```text
Alice   → FREE
Bob     → PREMIUM
Charlie → ENTERPRISE
```

The program accepts:

```text
A → Alice
B → Bob
C → Charlie
```

For example:

```text
A
Request for Alice: Request allowed

A
Request for Alice: Request allowed

A
Request for Alice: OHHHHH NOOOO!!!
Request rate limited
```

The demo application creates the three users and passes their requests through the same `RateLimiter` and `DefaultPolicyResolver` used by the core implementation.

## 🧱 Core Components

### `RateLimiter`

The main entry point for checking whether a request should be accepted.

```java
RateLimiter rateLimiter = new RateLimiter(resolver);

boolean allowed = rateLimiter.isRequestAllowed(user);
```

It does not contain the rate-limiting algorithm itself. Instead, it resolves the appropriate policy and delegates the decision to the configured strategy.

### `RateLimitPolicy`

Represents the configuration for a rate limit:

```text
limit
windowSizeMillis
strategy
```

This makes the policy independent of the algorithm used to enforce it.

### `PolicyResolver`

Determines which `RateLimitPolicy` should apply to a particular user.

The default implementation resolves the policy using the user's subscription tier.

This separation also makes it possible to introduce different policy-resolution rules in the future, such as:

* Promotional limits
* Company-specific limits
* Black Friday / flash-sale limits
* Region-specific limits
* User-specific limits

### `RateLimitingStrategy`

The strategy abstraction allows different algorithms to be plugged into a policy.

Currently the project contains:

```text
FixedWindowRateLimitingStrategy
SlidingWindowRateLimitingStrategy
TokenBucketRateLimitingStrategy
```

The fixed-window implementation is currently functional, while the sliding-window and token-bucket implementations are placeholders.

### `StateStore`

Rate-limiting algorithms need state to keep track of requests/tokens.

The project separates this state from the strategy itself:

```text
FixedWindowStore
SlidingWindowStore
TokenBucketStore
```

This keeps the algorithm and state-management responsibilities separate.

## 🧠 Design Principles

The project demonstrates several useful software-design concepts.

### Strategy Pattern

Different rate-limiting algorithms implement the same strategy interface.

```text
RateLimitingStrategy
        │
        ├── FixedWindow
        ├── SlidingWindow
        └── TokenBucket
```

This allows the algorithm to be changed without changing the `RateLimiter`.

### Resolver / Policy Pattern

Policy selection is handled separately from rate-limit execution.

```text
User → PolicyResolver → RateLimitPolicy → Strategy
```

This avoids putting tier-specific business logic directly inside the rate limiter.

### Separation of Concerns

Each component has a focused responsibility:

| Component              | Responsibility                   |
| ---------------------- | -------------------------------- |
| `User`                 | Represents the caller            |
| `Tier`                 | Defines subscription levels      |
| `RateLimitPolicy`      | Defines rate-limit configuration |
| `PolicyResolver`       | Selects the policy               |
| `RateLimiter`          | Coordinates the request decision |
| `RateLimitingStrategy` | Implements the algorithm         |
| `StateStore`           | Maintains algorithm state        |
| `RunRateLimiter`       | Demonstrates the system          |

## 🔮 Roadmap

The repository already contains placeholders for multiple rate-limiting algorithms.

Potential improvements include:

* [ ] Complete Sliding Window implementation
* [ ] Complete Token Bucket implementation
* [ ] Add unit tests
* [ ] Add concurrent/thread-safe state handling
* [ ] Add Maven or Gradle build configuration
* [ ] Add configurable limits instead of hard-coded values
* [ ] Add configurable window durations
* [ ] Add Redis-backed distributed state storage
* [ ] Add HTTP/API integration example
* [ ] Return rate-limit metadata such as:

    * Remaining requests
    * Reset time
    * Retry-after duration
* [ ] Add benchmarks for different algorithms
* [ ] Add CI/CD
* [ ] Add API documentation

## ⚠️ Current Limitations

This repository is currently a **demonstration/reference implementation**, rather than a production-ready distributed API gateway component.

In particular:

* The default policies are hard-coded.
* The default resolver uses a fixed in-memory configuration.
* Fixed-window state is in memory.
* Sliding Window is not implemented yet.
* Token Bucket is not implemented yet.
* There is no persistent/distributed state store.
* There is no HTTP server or framework integration.
* There are currently no visible automated tests in the repository.
* The CLI runner is intended for demonstration purposes.

## 🛠️ Extending the Project

To add a new rate-limiting algorithm, implement `RateLimitingStrategy`:

```java
public class MyRateLimitingStrategy implements RateLimitingStrategy {

    @Override
    public boolean isRequestAllowed(
            User user,
            RateLimitPolicy policy) {

        // Your algorithm here

        return true;
    }
}
```

Then associate it with a policy:

```java
new RateLimitPolicy()
    .setLimit(100)
    .setWindowSize(60_000)
    .setStrategy(new MyRateLimitingStrategy());
```

This allows new algorithms to be introduced without changing the core `RateLimiter`.

## 📚 Rate-Limiting Algorithms

### Fixed Window

Counts requests within a fixed time interval.

```text
|---------- 2 seconds ----------|
  1  2  3  4  5  ...

If limit = 4:

✓ ✓ ✓ ✓ ✗
```

**Pros**

* Simple
* Easy to understand
* Low memory overhead

**Cons**

* Can produce bursts around window boundaries

### Sliding Window

Tracks requests over a continuously moving time window.

```text
        ← 2 second window →
             |---------|
        • • • • •
```

It generally provides smoother rate limiting than a fixed window, but requires more state management.

### Token Bucket

Maintains a bucket of tokens that are consumed by requests and replenished over time.

```text
        Token refill
             ↓
       ┌───────────┐
       │ ● ● ● ● ● │
       └─────┬─────┘
             │
             ▼
          Request
```

Token Bucket is useful when you want to allow controlled bursts while maintaining an average request rate.

## 🤝 Contributing

Contributions are welcome.

A typical workflow:

```bash
git checkout -b feature/my-improvement
```

Make your changes, test them, and then open a pull request.

Good areas for contribution include:

* New rate-limiting algorithms
* Thread-safe implementations
* Persistent state stores
* Unit and integration tests
* Build tooling
* Documentation
* Performance improvements

## 📄 License

No license file is currently present in the repository.

If you intend to distribute or accept external contributions to this project, consider adding an appropriate open-source license.

## 👤 Author

Created by **Ritesh Asutkar**.

Repository:

https://github.com/riteshasutkar/r8lim8r

---

⭐ If this project is useful for learning about rate limiting, policy-based design, and the Strategy Pattern, consider giving it a star.
