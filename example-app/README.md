# Sunmi printer example

This example uses Capacitor 8 and the plugin from the parent directory.
Use Node.js 22 or newer, Java 21 with Android SDK 36 for Android builds,
and Xcode 26 or newer for iOS builds (minimum iOS 15).
Printing requires a supported Sunmi Android device.

Build the plugin first, then install and sync the example:

```bash
# From the repository root
npm install
npm run build
cd example-app
npm install
npm run build
npx cap sync
npx cap open android
```

Use `npm start` from this directory for the Vite development server.
