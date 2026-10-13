# ⚡ Charge the Robot

> **Draw · Connect · Charge**  
> An offline physics puzzle game featuring 40 missions across four worlds. Guide electric sparks into the sleeping robot to awaken the core!

---

## 📥 Download Android APK

The official Android application is available for direct download via GitHub Releases:

👉 **[Download Charge the Robot APK (v1.0.0)](https://github.com/akhilboss118-an/charge-the-robot/releases/latest)**

---

## ✨ Features

- **40 Handcrafted Missions**: 4 worlds with increasing complexity, introducing splitters, magnets, bounce pads, and crushing hazards.
- **Physics-Driven Drawing**: Draw conductive paths directly on screen to steer energy orbs into the robot's receiver socket.
- **Offline HTML Game**: Standalone offline bundle (`charge-the-robot.html`) that can be opened locally in any browser without an internet connection.
- **Built-in Audio Synthesizer**: Pure Web Audio API sound effects and ambient melodies with zero external sound dependencies.
- **Native Android Wrapper**: Complete Android Studio project (`android-project/`) using an optimized WebView with local asset streaming.

---

## 📱 Android Project (`android-project`)

This repository contains the complete Android wrapper source code.

### Building the APK Locally

Ensure you have Java 17+ and the Android SDK configured:

```bash
cd android-project

# On Linux / macOS:
./gradlew assembleDebug

# On Windows:
gradlew.bat assembleDebug
```

The generated APK will be located in:
`android-project/app/build/outputs/apk/debug/app-debug.apk`

---

## 📂 Repository Structure

```text
├── charge-the-robot.html   # Standalone offline game bundle
├── manifest.json           # Web App Manifest
├── sw.js                   # Service Worker for offline asset caching
├── favicon.svg             # Vector site icon
├── icon-192.png            # App icon (192x192)
├── icon-512.png            # App icon (512x512)
├── icon-maskable.png       # Android adaptive/maskable icon
├── icon.jpg                # Artwork asset
├── android-project/        # Clean Android Studio wrapper project
│   ├── app/                # Android app module with embedded assets
│   ├── gradle/             # Gradle wrapper binaries
│   ├── build.gradle.kts    # Gradle build script
│   └── settings.gradle.kts # Project settings
├── .gitignore              # Ignores build caches, local SDK paths, and binary APKs
└── README.md               # Project documentation
```

---

## 🛠️ Controls

- **Mouse / Touch**: Click/tap and drag anywhere on the playfield to draw conductive wire.
- **Release**: Sparks will automatically begin flowing from the emitter.
- **Reset / Retry**: Tap the retry icon if sparks miss the receiver.
