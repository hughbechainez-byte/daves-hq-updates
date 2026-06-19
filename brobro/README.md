# GitHub Pages Update Host

This folder is a publishable GitHub Pages scaffold for BroBro Boot Overlay updates.

Publish these files to your GitHub Pages source branch or `docs/` folder:

- `updates.json`
- `index.html`

The app's update checker reads the direct `updates.json` URL from the published Pages site first, then falls back to mirrors and a cached last-good manifest on device.
The manifest now identifies itself as `BroBro Boot Overlay` with package `com.brobro.bootoverlay`, so cached data from unrelated projects gets rejected automatically.
Current app-side primary URL:

`https://hughbechainez-byte.github.io/daves-hq-updates/updates.json`

Fallback mirrors are also checked in order.

Fill in the `sha256` fields before publishing each release.
