# Smart Scheduler

> An intelligent study scheduler and to-do list manager powered by AI, built natively for Android.

[English](./README.md) | [中文](./README_zh-CN.md)

![Android](https://img.shields.io/badge/Android-Kotlin-green?logo=android)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-Modern_UI-blue)
![Room Database](https://img.shields.io/badge/Room-Database-blue?logo=sqlite)
![Hilt](https://img.shields.io/badge/Dagger_Hilt-DI-orange)

An Android application tailored for students and individual developers, designed to help them efficiently manage out their study schedules, track fixed university classes, and break down daily to-do tasks dynamically using an AI-generated schedule engine.

## Features

- **AI-Powered Schedule Generation**: Automatically estimates duration for tasks and allocates them intelligently around fixed timeline events.
- **Fixed University Classes**: Import fixed classes, events, and schedules. Supports detailed RRULE repeating mechanisms.
- **Smart To-Do List**: Quick task entry, support for "Daily Repeating" functions, which advances the deadline gracefully.
- **Local Persistence via Room**: Entirely offline-first state management utilizing SQLite under the hood. Absolutely no data tracking.
- **Modern Jetpack Compose UI**: Clean, reactive, edge-to-edge UI using Material Design 3 and StateFlow.

## Quick Start

### Prerequisites

- Android Studio Koala Feature Drop (or newer)
- Java Development Kit (JDK) 17 
- Physical Android Device (Android 8.0+) or Emulator

### Installation

```bash
# Clone the repository
git clone https://github.com/USERNAME/smart-scheduler-android.git
cd smart-scheduler-android
```

Open the project folder directly in **Android Studio**. Make sure the Gradle sync completes successfully. 

### Usage

1. Click on the green `▶ Run` button in Android Studio.
2. Ensure your Android phone is connected via USB/Wi-Fi Debugging and authorized.
3. Open the app, navigate to `Todo`, add multiple tasks, and generate your Smart Schedule!

## Project Structure

```
smart-scheduler-android/
├── app/
│   ├── src/main/java/com/smartscheduler/app/
│   │   ├── data/       # Room Entities, DAOs, and Repositories
│   │   ├── di/         # Dagger Hilt Dependency Injection Modules
│   │   ├── domain/     # Core Models and Business Logic
│   │   └── ui/         # Jetpack Compose Screens and ViewModels
│   └── build.gradle.kts
└── build.gradle.kts
```

## Contributing

Pull requests are always welcome. For major changes, please open an issue first to discuss what you would like to change.

## License

[MIT](./LICENSE) © 2026 Author Name
