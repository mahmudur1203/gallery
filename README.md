# Gallery App

Gallery App is an Android app that fetches albums from device storage, displays thumbnails with names and item counts, supports grid/list views, shows images within albums, uses Jetpack Compose with MVVM and Hilt, and includes unit & UI tests with Kover for test coverage reports.

- Supported Android Versions: API 21 (Lollipop) - API 35

# Tech Stack
- Jetpack Compose - UI framework
- MVVM + Clean Architecture 
- Hilt - Dependency Injection.
- Kotlin Coroutines & Flow - Asynchronous operations.
- Mockito & JUnit - Unit testing.
- Robolectric - Run Android tests on JVM
- Kover - Test coverage.


---

# 🚀 Getting Started

### **1️⃣ Clone the Repository**
```bash
git clone https://github.com/mahmudur1203/gallery.git
```
- Open in Android Studio and sync Gradle.

### **2️⃣ Run the App**

- Click Run (▶️) in Android Studio or run:
```bash
./gradlew installDebug
```
**📌 APK Location: app/build/outputs/apk/debug/{appname}.apk**

---

## ✅ Running Tests

### **🧪 Run Unit Tests**
```bash
./gradlew testDebugUnitTest
```
**📌 Report: app/build/reports/tests/testDebugUnitTest/index.html**

### **📱 Run UI Tests**

#### Prerequisites
- Device or Emulator Connected
- USB Debugging Enabled (for real devices)

```bash
./gradlew connectedDebugAndroidTest
```
**📌 Report: app/build/reports/androidTests/connected/index.html**

---

## 📊 Code Coverage Reports
```bash
./gradlew koverHtmlReport
```
**📌 Report: app/build/reports/kover/html/index.html**






