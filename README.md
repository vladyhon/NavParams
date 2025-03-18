# NavParams
A simple Kotlin library that enables seamless navigation parameter passing in Jetpack Compose and Android Navigation, built with **Kotlin Flow** support.

## Why NavParams?
Passing parameters back in Jetpack Navigation is a common pain point. Traditional approaches like `savedStateHandle` often lead to **duplicate event triggers** or require manual cleanup.

NavParams solves this by providing:
- ✅ **One-time event consumption** (prevents duplicate triggers)
- ✅ **Seamless Flow integration** (for real-time updates)
- ✅ **Lightweight & easy-to-use API**

---

## Installation
To use NavParams in your project, add the dependency:

```kotlin
dependencies {
    implementation("io.github.vladyhon.navparams:navparams:0.1.0")
}
```

## Usage
### 1. Emitting Parameters
When navigating from **Screen B** back to **Screen A**, you can add a value:

```kotlin
navParams.add("user_update", updatedUser)
```

### 2. Subscribing to Parameters
On Screen A, subscribe to the event:

```kotlin
lifecycleScope.launch {
    navParams.subscribe<User>("user_update").collect { consumable ->
        val user = consumable.getAndConsume()
        // make changes
    }
}
```
This ensures that the user update is only received once.

### 3. Preventing Multiple Triggers

NavParams prevents duplicate triggers by wrapping emitted values in a ConsumableParameter class. This guarantees that each event is processed only once.

### 4. Clearing Parameters (if needed)
If you want to manually reset a parameter, you can clear it after consumption:

```kotlin
consumable.consume() // This marks the event as consumed, so it won't be triggered again
```

## Contributing

Feel free to open an issue or pull request if you have improvements! Contributions are always welcome.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.