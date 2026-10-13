# ⚡ Charge the Robot

> **Draw · Connect · Charge**  
> An offline-ready physics puzzle game featuring 40 missions across four worlds. Guide electric sparks into the sleeping robot to awaken the core!

---

## 🎮 Play Online

Play directly in any modern desktop or mobile browser without installation:

👉 **[https://akhilboss118-an.github.io/charge-the-robot/](https://akhilboss118-an.github.io/charge-the-robot/)**

---

## ✨ Features

- **40 Handcrafted Missions**: 4 worlds with increasing complexity, introducing splitters, magnets, bounce pads, and crushing hazards.
- **Physics-Driven Drawing**: Draw conductive paths directly on screen to steer energy orbs into the robot's receiver socket.
- **Progressive Web App (PWA)**: Full offline support powered by `sw.js` and `manifest.json`. Installable directly to your home screen or desktop.
- **Responsive & Mobile-Optimized**: Smooth 60 FPS canvas rendering with touch controls, viewport scaling, and haptic vibration feedback.
- **Built-in Audio Synthesizer**: Pure Web Audio API sound effects and ambient melodies with zero external sound asset dependencies.
- **Android App Included**: An Android Studio project (`android-project/`) using an optimized WebView with local asset streaming.

---

## 🚀 How to Deploy on GitHub Pages

1. **Push this repository to GitHub**:
   ```bash
   git remote add origin https://github.com/<your-username>/<your-repo-name>.git
   git branch -M main
   git push -u origin main
   ```

2. **Enable GitHub Pages**:
   - In your GitHub repository, navigate to **Settings** > **Pages** (under "Code and automation").
   - Under **Build and deployment**:
     - **Source**: Select **GitHub Actions** (the included `.github/workflows/deploy-pages.yml` workflow will automatically build and publish your game on every push).
     - *Alternative*: You can also choose **Deploy from a branch**, select branch `main`, and folder `/(root)`.
   - Within 1–2 minutes, your game will be live at `https://<your-username>.github.io/<your-repo-name>/`.

---

## 📱 Android Project (`android-project`)

This repository includes a native Android wrapper project ready to build with Android Studio or Gradle.

### Building the APK Locally

Ensure you have Java 17+ and the Android SDK installed:

```bash
cd android-project

# On Linux / macOS:
./gradlew assembleDebug

# On Windows:
gradlew.bat assembleDebug
```

The generated APK will be located in:
`android-project/app/build/outputs/apk/debug/app-debug.apk`

### Publishing APK via GitHub Releases

To keep the repository fast and lightweight, binary APKs are ignored by `.gitignore`. To share the APK on GitHub:

1. Go to your repository on GitHub.
2. Click **Releases** on the right sidebar > **Draft a new release**.
3. Create a tag (e.g. `v1.0.0`), set a release title, and drag-and-drop your `ChargeTheRobot.apk` into the release binary attachment area.
4. Click **Publish release**. Users can now download the APK directly from your releases page!

---

## 📂 Repository Structure

```text
├── index.html              # Primary entry point for GitHub Pages & web browsers
├── charge-the-robot.html   # Standalone offline game bundle
├── manifest.json           # Web App Manifest for PWA installation
├── sw.js                   # Service Worker for offline asset caching
├── favicon.svg             # Vector site icon
├── icon-192.png            # PWA app icon (192x192)
├── icon-512.png            # PWA app icon (512x512)
├── icon-maskable.png       # Android adaptive/maskable icon
├── icon.jpg                # Artwork asset
├── android-project/        # Clean Android Studio wrapper source project
│   ├── app/                # Android app module with embedded assets
│   ├── gradle/             # Gradle wrapper binaries
│   ├── build.gradle.kts    # Gradle build script
│   └── settings.gradle.kts # Project settings
├── .github/
│   └── workflows/
│       └── deploy-pages.yml # Automated GitHub Pages deployment workflow
├── .gitignore              # Ignores build caches, local SDK paths, and binary APKs
└── README.md               # Project documentation
```

---

## 🛠️ Controls

- **Mouse / Touch**: Click/tap and drag anywhere on the playfield to draw conductive wire.
- **Release**: Sparks will automatically begin flowing from the emitter.
- **Reset / Retry**: Tap the retry icon if sparks miss the receiver.
