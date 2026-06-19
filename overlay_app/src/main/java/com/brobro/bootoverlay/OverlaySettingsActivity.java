package com.brobro.bootoverlay;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Dialog;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import org.json.JSONObject;

public class OverlaySettingsActivity extends Activity {
    private static final String PREFS = "overlay_settings";
    private static final String APP_CONFIG_FILE = "overlay_config.txt";
    private static final String APP_LOGO_FILE = "logo.png";
    private static final String LOGO_GENERATION_SIGNATURE_VERSION = "3";
    private static final String FIXED_LOGO_TEXT = "Dave OS";
    private static final String APK_VERSION = "1.0.12 (39)";
    private static final String EXPECTED_UPDATE_PROJECT = "BroBro Boot Overlay";
    private static final String EXPECTED_UPDATE_PACKAGE = "com.brobro.bootoverlay";
    private static final String DEFAULT_MANIFEST_URL = "https://hughbechainez-byte.github.io/daves-hq-updates/brobro/updates.json";
    private static final String FALLBACK_MANIFEST_URL_1 = "https://raw.githubusercontent.com/hughbechainez-byte/daves-hq-updates/main/brobro/updates.json";
    private static final String FALLBACK_MANIFEST_URL_2 = "https://cdn.jsdelivr.net/gh/hughbechainez-byte/daves-hq-updates@main/brobro/updates.json";
    private static final String UPDATE_CACHE_PREFS = "overlay_update_cache";
    private static final String UPDATE_CACHE_JSON = "manifest_json";
    private static final String UPDATE_CACHE_SOURCE = "manifest_source";
    private static final String UPDATE_CACHE_TIME = "manifest_time";
    private static final String UPDATE_CHANNEL_ID = "brobro_boot_overlay_updates";
    private static final int UPDATE_NOTIFICATION_ID = 1009;
    private static final int REQUEST_FONT_FILE = 4102;
    private static final int REQUEST_LOCAL_APK_FILE = 4103;
    private static final int REQUEST_LOCAL_MODULE_FILE = 4104;
    private static final int REQUEST_POST_NOTIFICATIONS = 4105;

    private EditText moduleId;
    private EditText gradientColor1;
    private EditText gradientColor2;
    private EditText gradientColor3;
    private EditText logoFillColor;
    private EditText logoBackgroundColor;
    private EditText logoBoxBorderColor;
    private EditText progressColor;
    private EditText progressBorderColor;
    private EditText livebootTextColor;
    private EditText livebootTextLines;
    private EditText livebootFinalLines;
    private Spinner gradientPattern;
    private Spinner gradientAnimation;
    private Spinner backgroundMode;
    private Spinner logoFontMode;
    private Spinner progressDuration;
    private Spinner livebootDisplayMode;
    private Spinner livebootFeedMode;
    private Spinner livebootRevealMode;
    private Spinner livebootRevealSpeed;
    private Spinner livebootSpeed;
    private Spinner livebootDensity;
    private Spinner livebootFinalLead;
    private Spinner foregroundEffectMode;
    private Spinner livebootEffectMode;
    private Spinner backgroundEffectMode;
    private Spinner overlayEffectMode;
    private Spinner wallpaperLogoPosition;
    private CheckBox livebootEnabled;
    private CheckBox livebootFinalLinesEnabled;
    private CheckBox gradientReverse;
    private CheckBox wallpaperLogoEnabled;
    private SeekBar gradientSpeedPercent;
    private SeekBar gradientAngle;
    private SeekBar gradientScale;
    private SeekBar gradientCenterX;
    private SeekBar gradientCenterY;
    private SeekBar gradientStop2;
    private SeekBar gradientStop3;
    private SeekBar gradientBrightness;
    private SeekBar gradientExposure;
    private SeekBar gradientSaturation;
    private SeekBar gradientContrast;
    private SeekBar gradientGamma;
    private SeekBar gradientVibrance;
    private SeekBar gradientHueShift;
    private SeekBar gradientYellowBoost;
    private SeekBar gradientRedBoost;
    private SeekBar gradientGreenBoost;
    private SeekBar gradientBlueBoost;
    private SeekBar gradientMotionX;
    private SeekBar gradientMotionY;
    private SeekBar logoSize;
    private SeekBar logoBoxWidth;
    private SeekBar logoBoxHeight;
    private SeekBar logoBoxBorderWidth;
    private SeekBar logoTextDepth;
    private SeekBar progressWidth;
    private SeekBar progressHeight;
    private SeekBar progressBorderWidth;
    private SeekBar progressPixelation;
    private SeekBar foregroundEffectStrength;
    private SeekBar foregroundEffectOpacity;
    private SeekBar livebootEffectStrength;
    private SeekBar livebootEffectOpacity;
    private SeekBar backgroundEffectStrength;
    private SeekBar backgroundEffectOpacity;
    private SeekBar overlayEffectStrength;
    private SeekBar screenEffectStrength;
    private Spinner crtMode;
    private SeekBar crtStrength;
    private SeekBar crtOpacity;
    private SeekBar crtPixelation;
    private SeekBar crtSoftness;
    private SeekBar crtScanlines;
    private SeekBar crtChromatic;
    private SeekBar crtFlicker;
    private SeekBar wallpaperLogoWidth;
    private SeekBar wallpaperLogoAlpha;
    private SeekBar livebootAlpha;
    private SeekBar livebootSize;
    private CheckBox progressShowPercent;
    private BootOverlayView preview;
    private BootOverlayView stickyPreview;
    private CheckBox previewFullscreen;
    private Dialog previewFullscreenDialog;
    private BootOverlayView previewFullscreenView;
    private TextView status;
    private TextView diagnosticsText;
    private TextView gradientSpeedValue;
    private TextView gradientAngleValue;
    private TextView gradientScaleValue;
    private TextView gradientCenterValue;
    private TextView gradientStopsValue;
    private TextView gradientGradingValue;
    private TextView gradientExposureValue;
    private TextView gradientColorPopValue;
    private TextView gradientMotionValue;
    private TextView foregroundEffectValue;
    private TextView foregroundEffectOpacityValue;
    private TextView livebootEffectValue;
    private TextView livebootEffectOpacityValue;
    private TextView backgroundEffectValue;
    private TextView backgroundEffectOpacityValue;
    private TextView overlayEffectValue;
    private TextView screenEffectValue;
    private TextView crtSummaryValue;
    private TextView wallpaperLogoValue;
    private TextView wallpaperLogoAlphaValue;
    private TextView logoBoxWidthValue;
    private TextView logoBoxHeightValue;
    private TextView logoBoxBorderValue;
    private TextView logoTextDepthValue;
    private TextView progressWidthValue;
    private TextView progressHeightValue;
    private TextView progressBorderWidthValue;
    private TextView progressPixelationValue;
    private TextView updateSummary;
    private TextView presetModeValue;
    private LinearLayout styleTab;
    private LinearLayout feedTab;
    private LinearLayout applyTab;
    private Button styleTabButton;
    private Button feedTabButton;
    private Button applyTabButton;
    private Spinner bootProfileSlot;
    private EditText bootProfileName;
    private static final String[] PROFILE_LABELS = new String[]{"Profile 1", "Profile 2", "Profile 3", "Profile 4", "Profile 5"};
    private String importedFontPath = "";
    private String lastGeneratedLogoSignature = "";
    private String lastApplyStatus = "Not applied in this app session.";
    private String pendingLocalInstallKind = "";
    private String bootPresetMode = "manual";
    private boolean loading = false;
    private boolean launchUpdateCheckStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildUi();
        ensureNotificationPermission();
        load();
        updatePreview();
        checkForUpdatesOnLaunch();
    }

    private void buildUi() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(12, 16, 22));
        root.setPadding(0, topInsetPx(), 0, 0);

        TextView title = label("BroBro Boot Overlay v1.0.12", 24, true);
        title.setPadding(dp(18), dp(16), dp(18), dp(10));
        root.addView(title);

        LinearLayout previewDock = new LinearLayout(this);
        previewDock.setOrientation(LinearLayout.VERTICAL);
        previewDock.setPadding(dp(18), 0, dp(18), dp(10));
        stickyPreview = new BootOverlayView(this);
        stickyPreview.setPreviewMode(true);
        previewDock.addView(stickyPreview, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(240)
        ));
        Button previewRefresh = button("Refresh Preview");
        previewRefresh.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) { updatePreview(); }
        });
        previewDock.addView(previewRefresh);
        previewFullscreen = checkbox("Full-screen preview", false);
        previewFullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (previewFullscreen.isChecked()) {
                    showFullscreenPreview();
                } else {
                    dismissFullscreenPreview();
                }
            }
        });
        previewDock.addView(previewFullscreen);
        root.addView(previewDock);

        LinearLayout tabs = new LinearLayout(this);
        tabs.setOrientation(LinearLayout.HORIZONTAL);
        tabs.setPadding(dp(10), 0, dp(10), dp(8));
        root.addView(tabs);

        styleTabButton = tabButton("Style");
        feedTabButton = tabButton("LiveBoot Feed");
        applyTabButton = tabButton("Apply / Diagnostics");
        tabs.addView(styleTabButton);
        tabs.addView(feedTabButton);
        tabs.addView(applyTabButton);

        ScrollView scroll = new ScrollView(this);
        LinearLayout host = new LinearLayout(this);
        host.setOrientation(LinearLayout.VERTICAL);
        host.setPadding(dp(18), 0, dp(18), dp(18));
        scroll.addView(host);
        root.addView(scroll, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
        ));

        styleTab = tabContainer();
        feedTab = tabContainer();
        applyTab = tabContainer();
        host.addView(styleTab);
        host.addView(feedTab);
        host.addView(applyTab);

        buildStyleTab();
        buildFeedTab();
        buildApplyTab();

        status = label("", 14, false);
        status.setPadding(dp(18), dp(8), dp(18), dp(14));
        root.addView(status);

        setContentView(root);
        wireTabs();
        addWatchers();
        showTab(styleTab);
    }

    private void buildStyleTab() {
        LinearLayout gradientGroup = collapsibleGroup(styleTab, "Gradient Studio", false);
        gradientPattern = spinner(gradientGroup, "Pattern", new String[]{"linear", "radial", "sweep", "bands", "radial_bands", "checker", "diamond", "rings"}, "linear");
        gradientAnimation = spinner(gradientGroup, "Animation", GradientBackgroundRenderer.animationOptions(), "drift");
        gradientColor1 = colorField(gradientGroup, "Color 1", "#FF003B46");
        gradientColor2 = colorField(gradientGroup, "Color 2", "#FFFFD447");
        gradientColor3 = colorField(gradientGroup, "Color 3", "#FF24C06F");
        gradientSpeedPercent = slider(gradientGroup, "Animation speed", 20, 300, 185);
        gradientSpeedValue = label("185%", 12, false);
        gradientGroup.addView(gradientSpeedValue);
        gradientAngle = slider(gradientGroup, "Pattern angle", 0, 359, 45);
        gradientAngleValue = label("45 degrees", 12, false);
        gradientGroup.addView(gradientAngleValue);
        gradientScale = slider(gradientGroup, "Pattern scale", 50, 200, 100);
        gradientScaleValue = label("100%", 12, false);
        gradientGroup.addView(gradientScaleValue);
        gradientCenterX = slider(gradientGroup, "Pattern center X", 0, 100, 50);
        gradientCenterY = slider(gradientGroup, "Pattern center Y", 0, 100, 50);
        gradientCenterValue = label("Center: 50%, 50%", 12, false);
        gradientGroup.addView(gradientCenterValue);
        gradientStop2 = slider(gradientGroup, "Middle color position", 5, 90, 36);
        gradientStop3 = slider(gradientGroup, "Third color position", 10, 95, 70);
        gradientStopsValue = label("Color stops: 36%, 70%", 12, false);
        gradientGroup.addView(gradientStopsValue);
        gradientReverse = checkbox("Reverse gradient colors", false);
        gradientGroup.addView(gradientReverse);

        LinearLayout gradingGroup = collapsibleGroup(styleTab, "Color Grading", false);
        gradientBrightness = slider(gradingGroup, "Brightness / darkness", -60, 60, 0);
        gradientExposure = slider(gradingGroup, "Exposure", -40, 40, 0);
        gradientExposureValue = label("Exposure: 0", 12, false);
        gradingGroup.addView(gradientExposureValue);
        gradientSaturation = slider(gradingGroup, "Saturation", 0, 200, 100);
        gradientContrast = slider(gradingGroup, "Contrast", 50, 150, 100);
        gradientGamma = slider(gradingGroup, "Gamma", 50, 200, 100);
        gradientVibrance = slider(gradingGroup, "Vibrance", 0, 200, 100);
        gradientHueShift = slider(gradingGroup, "Hue shift", -180, 180, 0);
        gradientYellowBoost = slider(gradingGroup, "Yellow pop (legacy)", 0, 100, 0);
        gradientYellowBoost.setVisibility(View.GONE);
        gradientGradingValue = label("Brightness: 0  Exposure: 0  Saturation: 100%  Contrast: 100%  Gamma: 100%  Vibrance: 100%  Hue: 0", 12, false);
        gradingGroup.addView(gradientGradingValue);
        gradientMotionX = slider(gradingGroup, "Motion X", 0, 100, 18);
        gradientMotionY = slider(gradingGroup, "Motion Y", 0, 100, 12);
        gradientMotionValue = label("Motion: X 18%  Y 12%", 12, false);
        gradingGroup.addView(gradientMotionValue);

        LinearLayout globalColorGroup = collapsibleGroup(styleTab, "Global Color Pop", false);
        globalColorGroup.addView(label("Boost the main color channels across the full gradient.", 12, false));
        gradientRedBoost = slider(globalColorGroup, "Red pop", 0, 100, 0);
        gradientGreenBoost = slider(globalColorGroup, "Green pop", 0, 100, 0);
        gradientBlueBoost = slider(globalColorGroup, "Blue pop", 0, 100, 0);
        gradientColorPopValue = label("Red: 0  Green: 0  Blue: 0", 12, false);
        globalColorGroup.addView(gradientColorPopValue);

        LinearLayout screenGroup = collapsibleGroup(styleTab, "Screen Filter", false);
        screenGroup.addView(label("Adds a soft screen layer over everything except the logo.", 12, false));
        screenEffectStrength = slider(screenGroup, "Screen haze", 0, 100, 0);
        screenEffectValue = label("Screen haze: 0%", 12, false);
        screenGroup.addView(screenEffectValue);

        LinearLayout effectsGroup = collapsibleGroup(styleTab, "Effects", false);
        backgroundMode = spinner(effectsGroup, "Background mode", new String[]{"animated_gradient", "color_cycle", "solid"}, "animated_gradient");
        foregroundEffectMode = spinner(effectsGroup, "Logo effect", new String[]{"none", "crt_soft", "crt_strong", "vhs_glow"}, "none");
        foregroundEffectStrength = slider(effectsGroup, "Logo/Text effect strength", 0, 100, 18);
        foregroundEffectValue = label("Logo/Text effect strength: 18%", 12, false);
        effectsGroup.addView(foregroundEffectValue);
        foregroundEffectOpacity = slider(effectsGroup, "Logo effect opacity", 0, 255, 220);
        foregroundEffectOpacityValue = label("Logo effect opacity: 220", 12, false);
        effectsGroup.addView(foregroundEffectOpacityValue);
        livebootEffectMode = spinner(effectsGroup, "LiveBoot effect", new String[]{"none", "crt_soft", "crt_strong", "vhs_glow"}, "none");
        livebootEffectStrength = slider(effectsGroup, "LiveBoot effect strength", 0, 100, 18);
        livebootEffectValue = label("LiveBoot effect strength: 18%", 12, false);
        effectsGroup.addView(livebootEffectValue);
        livebootEffectOpacity = slider(effectsGroup, "LiveBoot effect opacity", 0, 255, 120);
        livebootEffectOpacityValue = label("LiveBoot effect opacity: 120", 12, false);
        effectsGroup.addView(livebootEffectOpacityValue);
        backgroundEffectMode = spinner(effectsGroup, "Background effect", new String[]{"none", "scanlines", "hue_warp", "crt_strong"}, "none");
        backgroundEffectStrength = slider(effectsGroup, "Background effect strength", 0, 100, 16);
        backgroundEffectValue = label("Background effect strength: 16%", 12, false);
        effectsGroup.addView(backgroundEffectValue);
        backgroundEffectOpacity = slider(effectsGroup, "Background effect opacity", 0, 255, 18);
        backgroundEffectOpacityValue = label("Background effect opacity: 18", 12, false);
        effectsGroup.addView(backgroundEffectOpacityValue);
        overlayEffectMode = spinner(effectsGroup, "Whole-screen effect", new String[]{"none", "scanlines", "flicker", "vignette"}, "none");
        overlayEffectStrength = slider(effectsGroup, "Whole-screen effect strength", 0, 100, 12);
        overlayEffectValue = label("Whole-screen effect strength: 12%", 12, false);
        effectsGroup.addView(overlayEffectValue);
        wallpaperLogoEnabled = checkbox("Show logo in live wallpaper", true);
        effectsGroup.addView(wallpaperLogoEnabled);
        wallpaperLogoPosition = spinner(effectsGroup, "Wallpaper logo position", new String[]{"upper", "center", "lower"}, "center");
        wallpaperLogoWidth = slider(effectsGroup, "Wallpaper logo width", 20, 90, 45);
        wallpaperLogoValue = label("Wallpaper logo width: 45%", 12, false);
        effectsGroup.addView(wallpaperLogoValue);
        wallpaperLogoAlpha = slider(effectsGroup, "Wallpaper logo alpha", 0, 255, 255);
        wallpaperLogoAlphaValue = label("Wallpaper logo alpha: 255", 12, false);
        effectsGroup.addView(wallpaperLogoAlphaValue);
        Button wallpaper = button("Use Current Gradient as Live Wallpaper");
        wallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLiveWallpaperPicker();
            }
        });
        effectsGroup.addView(wallpaper);

        LinearLayout crtGroup = collapsibleGroup(styleTab, "CRT Studio", false);
        crtGroup.addView(label("This section applies an old-screen look across the whole boot layout.", 12, false));
        crtMode = spinner(crtGroup, "CRT mode", new String[]{"off", "soft", "strong", "vhs"}, "soft");
        crtStrength = slider(crtGroup, "CRT strength", 0, 100, 36);
        crtOpacity = slider(crtGroup, "CRT opacity", 0, 255, 128);
        crtPixelation = slider(crtGroup, "Pixelation", 0, 100, 16);
        crtSoftness = slider(crtGroup, "Softness", 0, 100, 18);
        crtScanlines = slider(crtGroup, "Scanlines", 0, 100, 24);
        crtChromatic = slider(crtGroup, "Chromatic shift", 0, 100, 14);
        crtFlicker = slider(crtGroup, "Flicker", 0, 100, 8);
        crtSummaryValue = label("CRT: mode=soft strength=36 opacity=128 pixelation=16 softness=18 scanlines=24 chromatic=14 flicker=8", 12, false);
        crtGroup.addView(crtSummaryValue);

        LinearLayout logoGroup = collapsibleGroup(styleTab, "Dave OS Logo", false);
        logoGroup.addView(label("Dave OS is rendered as a PNG for boot safety.", 12, false));
        logoFontMode = spinner(logoGroup, "Font", new String[]{"Pixel Retro", "Sans", "Serif", "Monospace", "Imported Font"}, "Pixel Retro");
        Button importFont = button("Import TTF/OTF Font");
        importFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                importFontFile();
            }
        });
        logoGroup.addView(importFont);
        logoSize = slider(logoGroup, "Logo size", 20, 200, 100);
        logoFillColor = colorField(logoGroup, "Logo color", "#FFFFFFFF");
        logoBackgroundColor = colorField(logoGroup, "Logo text background", "#78003B46");
        logoBoxWidth = slider(logoGroup, "Logo box width", 40, 100, 92);
        logoBoxWidthValue = label("Logo box width: 92%", 12, false);
        logoGroup.addView(logoBoxWidthValue);
        logoBoxHeight = slider(logoGroup, "Logo box height", 30, 100, 74);
        logoBoxHeightValue = label("Logo box height: 74%", 12, false);
        logoGroup.addView(logoBoxHeightValue);
        logoBoxBorderColor = colorField(logoGroup, "Logo box border color", "#FFFFFFFF");
        logoBoxBorderWidth = slider(logoGroup, "Logo box border width", 0, 24, 4);
        logoBoxBorderValue = label("Logo box border width: 4 px", 12, false);
        logoGroup.addView(logoBoxBorderValue);
        logoTextDepth = slider(logoGroup, "Logo text 3D / blur depth", 0, 24, 4);
        logoTextDepthValue = label("Logo text depth: 4 px", 12, false);
        logoGroup.addView(logoTextDepthValue);

        LinearLayout progressGroup = collapsibleGroup(styleTab, "Progress Bar", false);
        progressGroup.addView(label("Adjust the boot progress bar independently from the rest of the screen.", 12, false));
        progressDuration = spinner(progressGroup, "Duration", new String[]{"15s", "20s", "25s", "30s"}, "25s");
        progressColor = colorField(progressGroup, "Bar color", "#FFFFD447");
        progressBorderColor = colorField(progressGroup, "Border color", "#B4FFFFFF");
        progressWidth = slider(progressGroup, "Bar width", 20, 100, 68);
        progressWidthValue = label("Bar width: 68%", 12, false);
        progressGroup.addView(progressWidthValue);
        progressHeight = slider(progressGroup, "Bar height", 2, 40, 18);
        progressHeightValue = label("Bar height: 18 px", 12, false);
        progressGroup.addView(progressHeightValue);
        progressBorderWidth = slider(progressGroup, "Border width", 0, 12, 2);
        progressBorderWidthValue = label("Border width: 2 px", 12, false);
        progressGroup.addView(progressBorderWidthValue);
        progressPixelation = slider(progressGroup, "Pixelate bar", 0, 100, 0);
        progressPixelationValue = label("Pixelation: 0%", 12, false);
        progressGroup.addView(progressPixelationValue);
        progressShowPercent = checkbox("Show percentage label", false);
        progressGroup.addView(progressShowPercent);
    }

    private void buildFeedTab() {
        LinearLayout feedGroup = collapsibleGroup(feedTab, "LiveBoot Feed", false);
        livebootEnabled = checkbox("LiveBoot feed enabled", true);
        feedGroup.addView(livebootEnabled);
        livebootDisplayMode = spinner(feedGroup, "Display mode", new String[]{"behind_logo", "full_screen"}, "full_screen");
        livebootFeedMode = spinner(feedGroup, "Feed mode", new String[]{"pseudo", "logcat_experimental"}, "logcat_experimental");
        feedGroup.addView(label("Logcat feed is experimental; if unavailable, the app falls back to pseudo boot lines.", 12, false));

        Button fullScreenLogcat = button("Use Full-Screen Logcat");
        fullScreenLogcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading = true;
                livebootEnabled.setChecked(true);
                setSpinner(livebootDisplayMode, "full_screen");
                setSpinner(livebootFeedMode, "logcat_experimental");
                setSpinner(livebootRevealMode, "word");
                setSpinner(livebootRevealSpeed, "fast");
                setSpinner(livebootSpeed, "medium");
                setSpinner(livebootDensity, "normal");
                setSlider(livebootAlpha, 120);
                setSlider(livebootSize, 22);
                loading = false;
                updatePreview();
                setStatus("Full-screen logcat preset selected.");
            }
        });
        feedGroup.addView(fullScreenLogcat);

        livebootTextLines = multilineField(feedGroup, "Pseudo/custom boot text", defaultLivebootLines());

        LinearLayout lineButtons = row();
        Button resetLines = button("Reset Default Lines");
        resetLines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                livebootTextLines.setText(defaultLivebootLines());
                updatePreview();
            }
        });
        Button addSamples = button("Add Sample Lines");
        addSamples.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                livebootTextLines.append("\n[ OK ] zygote warming services\n[ OK ] package manager scan\n[ OK ] Dave OS surface ready");
                updatePreview();
            }
        });
        lineButtons.addView(resetLines);
        lineButtons.addView(addSamples);
        feedGroup.addView(lineButtons);

        livebootTextColor = colorField(feedGroup, "Text color", "#99B7FFF7");
        livebootAlpha = slider(feedGroup, "Opacity", 0, 255, 120);
        feedGroup.addView(label("Blur is disabled because it caused boot-time rendering artifacts.", 12, false));
        livebootSize = slider(feedGroup, "Text size", 8, 64, 22);
        livebootSpeed = spinner(feedGroup, "Scroll speed", new String[]{"slow", "medium", "fast"}, "medium");
        livebootDensity = spinner(feedGroup, "Density", new String[]{"sparse", "normal", "dense"}, "normal");
        livebootRevealMode = spinner(feedGroup, "Reveal mode", new String[]{"instant", "word", "char"}, "word");
        livebootRevealSpeed = spinner(feedGroup, "Reveal speed", new String[]{"slow", "medium", "fast"}, "fast");

        LinearLayout finalGroup = collapsibleGroup(feedTab, "Final Boot Lines", false);
        livebootFinalLinesEnabled = checkbox("Show custom lines near the estimated end of boot", false);
        finalGroup.addView(livebootFinalLinesEnabled);
        livebootFinalLead = spinner(finalGroup, "Start before estimated finish", new String[]{"3s", "5s", "8s"}, "5s");
        livebootFinalLines = multilineField(finalGroup, "Custom final lines", defaultFinalLines());
        finalGroup.addView(label("These lines are appended once and do not restart the logcat stream.", 12, false));
    }

    private void buildApplyTab() {
        moduleId = field(applyTab, "Module ID", "brobro_boot_overlay");
        Button apply = button("Apply to Boot Module");
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyToBootModule();
            }
        });
        applyTab.addView(apply);

        Button reload = button("Reload from Module");
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadFromModule(true);
            }
        });
        applyTab.addView(reload);

        applyTab.addView(label("Module updates preserve the installed config and logo.", 12, false));

        applyTab.addView(sectionTitle("Updates"));
        applyTab.addView(label("Updates check the primary manifest, then mirrors, with a cached fallback on device.", 12, false));
        Button checkUpdates = button("Check for Updates");
        checkUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkForUpdates(false);
            }
        });
        applyTab.addView(checkUpdates);
        updateSummary = label("", 12, false);
        updateSummary.setTextIsSelectable(true);
        applyTab.addView(updateSummary);

        applyTab.addView(sectionTitle("Boot Profiles"));
        applyTab.addView(label("Save up to five app-side profiles, then load one before applying to boot.", 12, false));
        bootProfileSlot = spinner(applyTab, "Profile slot", profileLabelsForSpinner(), profileLabelForSlot(1));
        bootProfileName = field(applyTab, "Profile name", "Profile 1");
        Button saveProfile = button("Save Current to Slot");
        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfileSlot();
            }
        });
        applyTab.addView(saveProfile);
        Button renameProfile = button("Rename Selected Slot");
        renameProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                renameProfileSlot();
            }
        });
        applyTab.addView(renameProfile);
        Button loadProfile = button("Load Slot");
        loadProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadProfileSlot(true);
                updatePresetModeValue("manual");
            }
        });
        applyTab.addView(loadProfile);
        Button cycleProfiles = button("Cycle Presets");
        cycleProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cycleProfileSlot(false);
            }
        });
        applyTab.addView(cycleProfiles);
        Button shuffleProfiles = button("Shuffle Presets");
        shuffleProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cycleProfileSlot(true);
            }
        });
        applyTab.addView(shuffleProfiles);
        presetModeValue = label("Preset mode: manual", 12, false);
        applyTab.addView(presetModeValue);
        Button applyProfile = button("Apply Selected Slot");
        applyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadProfileSlot(false);
                updatePresetModeValue("manual");
                applyToBootModule();
            }
        });
        applyTab.addView(applyProfile);

        Button reset = button("Return to Default Settings");
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyConfigToFields(OverlayConfig.defaults());
                updatePreview();
                setStatus("Default settings restored in the app. Apply to Boot Module to make them active at boot.");
            }
        });
        applyTab.addView(reset);

        diagnosticsText = label("", 12, false);
        diagnosticsText.setVisibility(View.GONE);

        Button refreshDiagnostics = button("Refresh Diagnostics");
        refreshDiagnostics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshDiagnostics();
            }
        });
        applyTab.addView(refreshDiagnostics);
    }

    private void wireTabs() {
        styleTabButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) { showTab(styleTab); }
        });
        feedTabButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) { showTab(feedTab); }
        });
        applyTabButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) { refreshDiagnostics(); showTab(applyTab); }
        });
    }

    private void showTab(LinearLayout active) {
        styleTab.setVisibility(active == styleTab ? View.VISIBLE : View.GONE);
        feedTab.setVisibility(active == feedTab ? View.VISIBLE : View.GONE);
        applyTab.setVisibility(active == applyTab ? View.VISIBLE : View.GONE);
        markTab(styleTabButton, active == styleTab);
        markTab(feedTabButton, active == feedTab);
        markTab(applyTabButton, active == applyTab);
    }

    private void markTab(Button button, boolean active) {
        button.setTextColor(Color.WHITE);
        button.setBackgroundColor(active ? Color.rgb(0, 92, 96) : Color.rgb(36, 43, 54));
    }

    private void load() {
        loading = true;
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        moduleId.setText(prefString(prefs, "moduleId", "brobro_boot_overlay"));
        gradientColor1.setText(prefString(prefs, "gradientColor1", "#FF003B46"));
        gradientColor2.setText(prefString(prefs, "gradientColor2", "#FFFFD447"));
        gradientColor3.setText(prefString(prefs, "gradientColor3", "#FF24C06F"));
        progressColor.setText(prefString(prefs, "progressColor", "#FFFFD447"));
        progressBorderColor.setText(prefString(prefs, "progressBorderColor", "#B4FFFFFF"));
        logoFillColor.setText(prefString(prefs, "logoFillColor", "#FFFFFFFF"));
        logoBackgroundColor.setText(prefString(prefs, "logoBackgroundColor", "#78003B46"));
        logoBoxBorderColor.setText(prefString(prefs, "logoBoxBorderColor", "#FFFFFFFF"));
        livebootTextColor.setText(prefString(prefs, "livebootTextColor", "#99B7FFF7"));
        livebootTextLines.setText(prefString(prefs, "livebootTextLines", defaultLivebootLines()));
        livebootFinalLines.setText(prefString(prefs, "livebootFinalLines", defaultFinalLines()));
        importedFontPath = prefString(prefs, "importedFontPath", "");
        setSpinner(gradientPattern, prefString(prefs, "gradientPattern", "linear"));
        setSpinner(gradientAnimation, prefString(prefs, "gradientAnimation", "drift"));
        setSpinner(backgroundMode, prefString(prefs, "backgroundMode", "animated_gradient"));
        setSpinner(logoFontMode, prefString(prefs, "logoFontMode", "Pixel Retro"));
        setSpinner(progressDuration, prefString(prefs, "progressDuration", "25s"));
        setSpinner(livebootDisplayMode, prefString(prefs, "livebootDisplayMode", "full_screen"));
        setSpinner(livebootFeedMode, prefString(prefs, "livebootFeedMode", "logcat_experimental"));
        setSpinner(livebootRevealMode, prefString(prefs, "livebootRevealMode", "word"));
        setSpinner(livebootRevealSpeed, prefString(prefs, "livebootRevealSpeed", "fast"));
        setSpinner(livebootSpeed, prefString(prefs, "livebootSpeed", "medium"));
        setSpinner(livebootDensity, prefString(prefs, "livebootDensity", "normal"));
        setSpinner(livebootFinalLead, prefString(prefs, "livebootFinalLead", "5s"));
        setSpinner(foregroundEffectMode, prefString(prefs, "foregroundEffectMode", "none"));
        setSpinner(livebootEffectMode, prefString(prefs, "livebootEffectMode", "none"));
        setSpinner(backgroundEffectMode, prefString(prefs, "backgroundEffectMode", "none"));
        setSpinner(overlayEffectMode, prefString(prefs, "overlayEffectMode", "none"));
        setSpinner(crtMode, prefString(prefs, "crtMode", "soft"));
        setSpinner(wallpaperLogoPosition, prefString(prefs, "wallpaperLogoPosition", "center"));
        refreshProfileSlotSpinnerLabels();
        setProfileSlotSelection(prefInt(prefs, "bootProfileSlot", 1));
        int legacyGradientSpeed = GradientBackgroundRenderer.legacySpeedPercent(
                prefString(prefs, "gradientSpeed", "fast")
        );
        setSlider(gradientSpeedPercent, prefInt(prefs, "gradientSpeedPercent", legacyGradientSpeed));
        setSlider(gradientAngle, prefInt(prefs, "gradientAngleDeg", 45));
        setSlider(gradientScale, prefInt(prefs, "gradientScalePercent", 100));
        setSlider(gradientCenterX, prefInt(prefs, "gradientCenterXPct", 50));
        setSlider(gradientCenterY, prefInt(prefs, "gradientCenterYPct", 50));
        setSlider(gradientStop2, prefInt(prefs, "gradientStop2Pct", 36));
        setSlider(gradientStop3, prefInt(prefs, "gradientStop3Pct", 70));
        setSlider(gradientBrightness, prefInt(prefs, "gradientBrightness", 0));
        setSlider(gradientExposure, prefInt(prefs, "gradientExposurePct", 0));
        setSlider(gradientSaturation, prefInt(prefs, "gradientSaturationPct", 100));
        setSlider(gradientContrast, prefInt(prefs, "gradientContrastPct", 100));
        setSlider(gradientGamma, prefInt(prefs, "gradientGammaPct", 100));
        setSlider(gradientVibrance, prefInt(prefs, "gradientVibrancePct", 100));
        setSlider(gradientHueShift, prefInt(prefs, "gradientHueShiftDeg", 0));
        setSlider(gradientYellowBoost, prefInt(prefs, "gradientYellowBoostPct", 0));
        setSlider(gradientRedBoost, prefInt(prefs, "gradientRedBoostPct", 0));
        setSlider(gradientGreenBoost, prefInt(prefs, "gradientGreenBoostPct", 0));
        setSlider(gradientBlueBoost, prefInt(prefs, "gradientBlueBoostPct", 0));
        setSlider(gradientMotionX, prefInt(prefs, "gradientMotionXPct", 18));
        setSlider(gradientMotionY, prefInt(prefs, "gradientMotionYPct", 12));
        setSlider(logoSize, prefInt(prefs, "logoSize", 100));
        setSlider(logoBoxWidth, prefInt(prefs, "logoBoxWidthPct", 92));
        setSlider(logoBoxHeight, prefInt(prefs, "logoBoxHeightPct", 74));
        setSlider(logoBoxBorderWidth, prefInt(prefs, "logoBoxBorderWidthPx", 4));
        setSlider(logoTextDepth, prefInt(prefs, "logoTextDepthPx", 4));
        setSlider(progressWidth, prefInt(prefs, "progressWidthPct", 68));
        setSlider(progressHeight, prefInt(prefs, "progressHeightPx", 18));
        setSlider(progressBorderWidth, prefInt(prefs, "progressBorderWidthPx", 2));
        setSlider(progressPixelation, prefInt(prefs, "progressPixelation", 0));
        setSlider(foregroundEffectStrength, prefInt(prefs, "foregroundEffectStrength", 18));
        setSlider(foregroundEffectOpacity, prefInt(prefs, "foregroundEffectOpacity", 220));
        setSlider(livebootEffectStrength, prefInt(prefs, "livebootEffectStrength", 18));
        setSlider(livebootEffectOpacity, prefInt(prefs, "livebootEffectOpacity", 120));
        setSlider(backgroundEffectStrength, prefInt(prefs, "backgroundEffectStrength", 16));
        setSlider(backgroundEffectOpacity, prefInt(prefs, "backgroundEffectOpacity", 18));
        setSlider(overlayEffectStrength, prefInt(prefs, "overlayEffectStrength", 12));
        setSlider(screenEffectStrength, prefInt(prefs, "screenEffectStrength", 0));
        setSlider(crtStrength, prefInt(prefs, "crtStrength", 36));
        setSlider(crtOpacity, prefInt(prefs, "crtOpacity", 128));
        setSlider(crtPixelation, prefInt(prefs, "crtPixelation", 16));
        setSlider(crtSoftness, prefInt(prefs, "crtSoftness", 18));
        setSlider(crtScanlines, prefInt(prefs, "crtScanlines", 24));
        setSlider(crtChromatic, prefInt(prefs, "crtChromatic", 14));
        setSlider(crtFlicker, prefInt(prefs, "crtFlicker", 8));
        setSlider(wallpaperLogoWidth, prefInt(prefs, "wallpaperLogoWidthPct", 45));
        setSlider(wallpaperLogoAlpha, prefInt(prefs, "wallpaperLogoAlpha", 255));
        setSlider(livebootAlpha, prefInt(prefs, "livebootAlpha", 120));
        setSlider(livebootSize, prefInt(prefs, "livebootSize", 22));
        livebootEnabled.setChecked(prefBoolean(prefs, "livebootEnabled", true));
        livebootFinalLinesEnabled.setChecked(prefBoolean(prefs, "livebootFinalLinesEnabled", false));
        wallpaperLogoEnabled.setChecked(prefBoolean(prefs, "wallpaperLogoEnabled", true));
        progressShowPercent.setChecked(prefBoolean(prefs, "progressShowPercent", false));
        gradientReverse.setChecked(prefBoolean(prefs, "gradientReverse", false));
        updatePresetModeValue(prefString(prefs, "bootProfileMode", "manual"));
        syncProfileNameField(selectedProfileSlot());
        updateGradientControlLabels();
        updateEffectsControlLabels();
        updateLogoControlLabels();
        updateProgressControlLabels();
        loading = false;
        reloadFromModule(false);
    }

    private void persist() {
        getSharedPreferences(PREFS, MODE_PRIVATE).edit()
                .putString("moduleId", moduleId.getText().toString().trim())
                .putString("gradientColor1", gradientColor1.getText().toString().trim())
                .putString("gradientColor2", gradientColor2.getText().toString().trim())
                .putString("gradientColor3", gradientColor3.getText().toString().trim())
                .putString("progressColor", progressColor.getText().toString().trim())
                .putString("progressBorderColor", progressBorderColor.getText().toString().trim())
                .putString("logoFillColor", logoFillColor.getText().toString().trim())
                .putString("logoBackgroundColor", logoBackgroundColor.getText().toString().trim())
                .putString("logoBoxBorderColor", logoBoxBorderColor.getText().toString().trim())
                .putString("livebootTextColor", livebootTextColor.getText().toString().trim())
                .putString("livebootTextLines", livebootTextLines.getText().toString())
                .putString("livebootFinalLines", livebootFinalLines.getText().toString())
                .putString("importedFontPath", importedFontPath)
                .putString("bootProfileMode", bootPresetMode)
                .putString("gradientPattern", selected(gradientPattern))
                .putString("gradientAnimation", selected(gradientAnimation))
                .putString("backgroundMode", selected(backgroundMode))
                .putString(
                        "gradientSpeed",
                        GradientBackgroundRenderer.legacySpeedLabel(sliderValue(gradientSpeedPercent))
                )
                .putString("logoFontMode", selected(logoFontMode))
                .putString("progressDuration", selected(progressDuration))
                .putString("livebootDisplayMode", selected(livebootDisplayMode))
                .putString("livebootFeedMode", selected(livebootFeedMode))
                .putString("livebootRevealMode", selected(livebootRevealMode))
                .putString("livebootRevealSpeed", selected(livebootRevealSpeed))
                .putString("livebootSpeed", selected(livebootSpeed))
                .putString("livebootDensity", selected(livebootDensity))
                .putString("livebootFinalLead", selected(livebootFinalLead))
                .putString("foregroundEffectMode", selected(foregroundEffectMode))
                .putString("livebootEffectMode", selected(livebootEffectMode))
                .putString("backgroundEffectMode", selected(backgroundEffectMode))
                .putString("overlayEffectMode", selected(overlayEffectMode))
                .putString("crtMode", selected(crtMode))
                .putString("wallpaperLogoPosition", selected(wallpaperLogoPosition))
                .putInt("gradientSpeedPercent", sliderValue(gradientSpeedPercent))
                .putInt("gradientAngleDeg", sliderValue(gradientAngle))
                .putInt("gradientScalePercent", sliderValue(gradientScale))
                .putInt("gradientCenterXPct", sliderValue(gradientCenterX))
                .putInt("gradientCenterYPct", sliderValue(gradientCenterY))
                .putInt("gradientStop2Pct", sliderValue(gradientStop2))
                .putInt("gradientStop3Pct", sliderValue(gradientStop3))
                .putInt("gradientBrightness", sliderValue(gradientBrightness))
                .putInt("gradientExposurePct", sliderValue(gradientExposure))
                .putInt("gradientSaturationPct", sliderValue(gradientSaturation))
                .putInt("gradientContrastPct", sliderValue(gradientContrast))
                .putInt("gradientGammaPct", sliderValue(gradientGamma))
                .putInt("gradientVibrancePct", sliderValue(gradientVibrance))
                .putInt("gradientHueShiftDeg", sliderValue(gradientHueShift))
                .putInt("gradientYellowBoostPct", sliderValue(gradientYellowBoost))
                .putInt("gradientRedBoostPct", sliderValue(gradientRedBoost))
                .putInt("gradientGreenBoostPct", sliderValue(gradientGreenBoost))
                .putInt("gradientBlueBoostPct", sliderValue(gradientBlueBoost))
                .putInt("bootProfileSlot", selectedProfileSlot())
                .putInt("gradientMotionXPct", sliderValue(gradientMotionX))
                .putInt("gradientMotionYPct", sliderValue(gradientMotionY))
                .putInt("logoSize", sliderValue(logoSize))
                .putInt("logoBoxWidthPct", sliderValue(logoBoxWidth))
                .putInt("logoBoxHeightPct", sliderValue(logoBoxHeight))
                .putInt("logoBoxBorderWidthPx", sliderValue(logoBoxBorderWidth))
                .putInt("logoTextDepthPx", sliderValue(logoTextDepth))
                .putInt("progressWidthPct", sliderValue(progressWidth))
                .putInt("progressHeightPx", sliderValue(progressHeight))
                .putInt("progressBorderWidthPx", sliderValue(progressBorderWidth))
                .putInt("progressPixelation", sliderValue(progressPixelation))
                .putInt("foregroundEffectStrength", sliderValue(foregroundEffectStrength))
                .putInt("foregroundEffectOpacity", sliderValue(foregroundEffectOpacity))
                .putInt("livebootEffectStrength", sliderValue(livebootEffectStrength))
                .putInt("livebootEffectOpacity", sliderValue(livebootEffectOpacity))
                .putInt("backgroundEffectStrength", sliderValue(backgroundEffectStrength))
                .putInt("backgroundEffectOpacity", sliderValue(backgroundEffectOpacity))
                .putInt("overlayEffectStrength", sliderValue(overlayEffectStrength))
                .putInt("screenEffectStrength", sliderValue(screenEffectStrength))
                .putInt("crtStrength", sliderValue(crtStrength))
                .putInt("crtOpacity", sliderValue(crtOpacity))
                .putInt("crtPixelation", sliderValue(crtPixelation))
                .putInt("crtSoftness", sliderValue(crtSoftness))
                .putInt("crtScanlines", sliderValue(crtScanlines))
                .putInt("crtChromatic", sliderValue(crtChromatic))
                .putInt("crtFlicker", sliderValue(crtFlicker))
                .putInt("wallpaperLogoWidthPct", sliderValue(wallpaperLogoWidth))
                .putInt("wallpaperLogoAlpha", sliderValue(wallpaperLogoAlpha))
                .putInt("livebootAlpha", sliderValue(livebootAlpha))
                .putInt("livebootSize", sliderValue(livebootSize))
                .putBoolean("livebootEnabled", livebootEnabled.isChecked())
                .putBoolean("livebootFinalLinesEnabled", livebootFinalLinesEnabled.isChecked())
                .putBoolean("wallpaperLogoEnabled", wallpaperLogoEnabled.isChecked())
                .putBoolean("progressShowPercent", progressShowPercent.isChecked())
                .putBoolean("gradientReverse", gradientReverse.isChecked())
                .putString(profileNameKey(selectedProfileSlot()), bootProfileName == null ? "" : bootProfileName.getText().toString().trim())
                .apply();
        writeAppConfigFile();
    }

    private String prefString(SharedPreferences prefs, String key, String fallback) {
        Object value = prefs.getAll().get(key);
        if (value == null) {
            return fallback;
        }
        return String.valueOf(value);
    }

    private int prefInt(SharedPreferences prefs, String key, int fallback) {
        Object value = prefs.getAll().get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            try {
                return Integer.parseInt(((String) value).trim());
            } catch (Exception ignored) {
            }
        }
        if (value instanceof Boolean) {
            return ((Boolean) value) ? 1 : 0;
        }
        return fallback;
    }

    private boolean prefBoolean(SharedPreferences prefs, String key, boolean fallback) {
        Object value = prefs.getAll().get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue() != 0;
        }
        if (value instanceof String) {
            String text = ((String) value).trim();
            if ("1".equals(text) || "true".equalsIgnoreCase(text) || "yes".equalsIgnoreCase(text)) {
                return true;
            }
            if ("0".equals(text) || "false".equalsIgnoreCase(text) || "no".equalsIgnoreCase(text)) {
                return false;
            }
        }
        return fallback;
    }

    private OverlayConfig currentConfig() {
        OverlayConfig config = OverlayConfig.defaults();
        config.logoText = FIXED_LOGO_TEXT;
        config.logoGeneratorEnabled = true;
        config.nativeTextEnabled = false;
        config.nativeImageEnabled = true;
        config.logoEnabled = true;
        config.logoFontMode = fontModeForConfig();
        config.logoFontName = selected(logoFontMode);
        config.logoFontPath = importedFontPath;
        config.logoFontSizePct = sliderValue(logoSize);
        config.logoFillColor = OverlayConfig.parseColor(logoFillColor.getText().toString(), config.logoFillColor);
        config.logoBackgroundColor = OverlayConfig.parseColor(
                logoBackgroundColor.getText().toString(),
                config.logoBackgroundColor
        );
        config.logoBoxWidthPct = sliderValue(logoBoxWidth);
        config.logoBoxHeightPct = sliderValue(logoBoxHeight);
        config.logoBoxBorderColor = OverlayConfig.parseColor(
                logoBoxBorderColor.getText().toString(),
                config.logoBoxBorderColor
        );
        config.logoBoxBorderWidthPx = sliderValue(logoBoxBorderWidth);
        config.logoBoxBorderEnabled = config.logoBoxBorderWidthPx > 0;
        config.logoTextDepthPx = sliderValue(logoTextDepth);
        config.imagePath = "/data/adb/modules/" + cleanModuleId() + "/logo.png";
        config.imagePosition = "center";
        config.imageWidthPct = 55;
        config.backgroundMode = selected(backgroundMode);
        config.gradientEnabled = true;
        config.gradientColor1 = OverlayConfig.parseColor(gradientColor1.getText().toString(), config.gradientColor1);
        config.gradientColor2 = OverlayConfig.parseColor(gradientColor2.getText().toString(), config.gradientColor2);
        config.gradientColor3 = OverlayConfig.parseColor(gradientColor3.getText().toString(), config.gradientColor3);
        config.gradientPattern = selected(gradientPattern);
        config.gradientAnimation = selected(gradientAnimation);
        config.gradientSpeedPercent = sliderValue(gradientSpeedPercent);
        config.gradientSpeed = GradientBackgroundRenderer.legacySpeedLabel(config.gradientSpeedPercent);
        config.gradientAngleDeg = sliderValue(gradientAngle);
        config.gradientScalePercent = sliderValue(gradientScale);
        config.gradientCenterXPct = sliderValue(gradientCenterX);
        config.gradientCenterYPct = sliderValue(gradientCenterY);
        config.gradientColor2PositionPct = sliderValue(gradientStop2);
        config.gradientColor3PositionPct = Math.max(
                config.gradientColor2PositionPct + 3,
                sliderValue(gradientStop3)
        );
        config.gradientBrightness = sliderValue(gradientBrightness);
        config.gradientExposurePct = sliderValue(gradientExposure);
        config.gradientSaturationPct = sliderValue(gradientSaturation);
        config.gradientContrastPct = sliderValue(gradientContrast);
        config.gradientGammaPct = sliderValue(gradientGamma);
        config.gradientVibrancePct = sliderValue(gradientVibrance);
        config.gradientHueShiftDeg = sliderValue(gradientHueShift);
        config.gradientYellowBoostPct = sliderValue(gradientYellowBoost);
        config.gradientRedBoostPct = sliderValue(gradientRedBoost);
        config.gradientGreenBoostPct = sliderValue(gradientGreenBoost);
        config.gradientBlueBoostPct = sliderValue(gradientBlueBoost);
        config.gradientMotionXPct = sliderValue(gradientMotionX);
        config.gradientMotionYPct = sliderValue(gradientMotionY);
        config.gradientReverse = gradientReverse.isChecked();
        config.backgroundColor = config.gradientColor1;
        config.preSurfaceSolidColor = config.gradientColor1;
        config.foregroundEffectMode = selected(foregroundEffectMode);
        config.foregroundEffectStrength = sliderValue(foregroundEffectStrength);
        config.foregroundEffectOpacity = sliderValue(foregroundEffectOpacity);
        config.livebootEffectMode = selected(livebootEffectMode);
        config.livebootEffectStrength = sliderValue(livebootEffectStrength);
        config.livebootEffectOpacity = sliderValue(livebootEffectOpacity);
        config.backgroundEffectMode = selected(backgroundEffectMode);
        config.backgroundEffectStrength = sliderValue(backgroundEffectStrength);
        config.backgroundEffectOpacity = sliderValue(backgroundEffectOpacity);
        config.overlayEffectMode = selected(overlayEffectMode);
        config.overlayEffectStrength = sliderValue(overlayEffectStrength);
        config.screenEffectStrength = sliderValue(screenEffectStrength);
        config.crtMode = selected(crtMode);
        config.crtStrength = sliderValue(crtStrength);
        config.crtOpacity = sliderValue(crtOpacity);
        config.crtPixelation = sliderValue(crtPixelation);
        config.crtSoftness = sliderValue(crtSoftness);
        config.crtScanlines = sliderValue(crtScanlines);
        config.crtChromatic = sliderValue(crtChromatic);
        config.crtFlicker = sliderValue(crtFlicker);
        config.progressBar = true;
        config.progressColor = OverlayConfig.parseColor(progressColor.getText().toString(), config.progressColor);
        config.progressBorderColor = OverlayConfig.parseColor(
                progressBorderColor.getText().toString(),
                config.progressBorderColor
        );
        config.progressMode = "time_estimated";
        config.progressDurationMs = durationMs();
        config.maxDurationMs = 120000L;
        config.postBootHoldMs = 1000L;
        config.bootanimSyncEnabled = true;
        config.bootanimEndHoldMs = 0L;
        config.progressPositionPct = 82;
        config.progressWidthPct = sliderValue(progressWidth);
        config.progressHeightPx = sliderValue(progressHeight);
        config.progressBorderWidthPx = sliderValue(progressBorderWidth);
        config.progressPixelation = sliderValue(progressPixelation);
        config.progressRounded = true;
        config.progressShowPercent = progressShowPercent.isChecked();
        config.livebootTextEnabled = livebootEnabled.isChecked();
        config.livebootTextDisplayMode = selected(livebootDisplayMode);
        config.livebootTextMode = selected(livebootFeedMode);
        config.livebootTextColor = OverlayConfig.parseColor(livebootTextColor.getText().toString(), config.livebootTextColor);
        config.livebootTextAlpha = sliderValue(livebootAlpha);
        config.livebootTextBlur = 0;
        config.livebootTextPaddingPx = 24;
        config.livebootTextRevealMode = selected(livebootRevealMode);
        config.livebootTextRevealSpeed = selected(livebootRevealSpeed);
        config.livebootTextSpeed = selected(livebootSpeed);
        config.livebootTextSizePx = sliderValue(livebootSize);
        config.livebootTextDensity = selected(livebootDensity);
        config.livebootTextPosition = "full";
        config.livebootTextBehindLogo = true;
        config.livebootTextLines = livebootTextLines.getText().toString();
        config.livebootFinalLinesEnabled = livebootFinalLinesEnabled.isChecked();
        config.livebootFinalLinesLeadMs = finalLeadMs();
        config.livebootFinalLines = livebootFinalLines.getText().toString();
        config.wallpaperLogoEnabled = wallpaperLogoEnabled.isChecked();
        config.wallpaperLogoPosition = selected(wallpaperLogoPosition);
        config.wallpaperLogoWidthPct = sliderValue(wallpaperLogoWidth);
        config.wallpaperLogoAlpha = sliderValue(wallpaperLogoAlpha);
        config.earlyFullscreen = true;
        config.startupDelayMs = 0L;
        config.debugFullscreen = false;
        return config;
    }

    private void applyConfigToFields(OverlayConfig config) {
        loading = true;
        gradientColor1.setText(OverlayConfig.toColorString(config.gradientColor1));
        gradientColor2.setText(OverlayConfig.toColorString(config.gradientColor2));
        gradientColor3.setText(OverlayConfig.toColorString(config.gradientColor3));
        progressColor.setText(OverlayConfig.toColorString(config.progressColor));
        progressBorderColor.setText(OverlayConfig.toColorString(config.progressBorderColor));
        logoFillColor.setText(OverlayConfig.toColorString(config.logoFillColor));
        logoBackgroundColor.setText(OverlayConfig.toColorString(config.logoBackgroundColor));
        logoBoxBorderColor.setText(OverlayConfig.toColorString(config.logoBoxBorderColor));
        livebootTextColor.setText(OverlayConfig.toColorString(config.livebootTextColor));
        setSpinner(gradientPattern, config.gradientPattern);
        setSpinner(gradientAnimation, config.gradientAnimation);
        setSpinner(backgroundMode, config.backgroundMode);
        setSpinner(logoFontMode, labelForFontMode(config.logoFontMode));
        setSpinner(progressDuration, durationLabel(config.progressDurationMs));
        setSlider(progressWidth, config.progressWidthPct);
        setSlider(progressHeight, config.progressHeightPx);
        setSlider(progressBorderWidth, config.progressBorderWidthPx);
        setSlider(progressPixelation, config.progressPixelation);
        setSpinner(livebootDisplayMode, config.livebootTextDisplayMode);
        setSpinner(livebootFeedMode, config.livebootTextMode);
        setSpinner(livebootRevealMode, config.livebootTextRevealMode);
        setSpinner(livebootRevealSpeed, config.livebootTextRevealSpeed);
        setSpinner(livebootSpeed, config.livebootTextSpeed);
        setSpinner(livebootDensity, config.livebootTextDensity);
        setSpinner(livebootFinalLead, finalLeadLabel(config.livebootFinalLinesLeadMs));
        setSpinner(foregroundEffectMode, config.foregroundEffectMode);
        setSpinner(livebootEffectMode, config.livebootEffectMode);
        setSpinner(backgroundEffectMode, config.backgroundEffectMode);
        setSpinner(overlayEffectMode, config.overlayEffectMode);
        setSpinner(crtMode, config.crtMode);
        setSpinner(wallpaperLogoPosition, config.wallpaperLogoPosition);
        setSlider(gradientSpeedPercent, config.gradientSpeedPercent);
        setSlider(gradientAngle, config.gradientAngleDeg);
        setSlider(gradientScale, config.gradientScalePercent);
        setSlider(gradientCenterX, config.gradientCenterXPct);
        setSlider(gradientCenterY, config.gradientCenterYPct);
        setSlider(gradientStop2, config.gradientColor2PositionPct);
        setSlider(gradientStop3, config.gradientColor3PositionPct);
        setSlider(gradientBrightness, config.gradientBrightness);
        setSlider(gradientExposure, config.gradientExposurePct);
        setSlider(gradientSaturation, config.gradientSaturationPct);
        setSlider(gradientContrast, config.gradientContrastPct);
        setSlider(gradientGamma, config.gradientGammaPct);
        setSlider(gradientVibrance, config.gradientVibrancePct);
        setSlider(gradientHueShift, config.gradientHueShiftDeg);
        setSlider(gradientYellowBoost, config.gradientYellowBoostPct);
        setSlider(gradientRedBoost, config.gradientRedBoostPct);
        setSlider(gradientGreenBoost, config.gradientGreenBoostPct);
        setSlider(gradientBlueBoost, config.gradientBlueBoostPct);
        setSlider(gradientMotionX, config.gradientMotionXPct);
        setSlider(gradientMotionY, config.gradientMotionYPct);
        setSlider(logoSize, config.logoFontSizePct);
        setSlider(logoBoxWidth, config.logoBoxWidthPct);
        setSlider(logoBoxHeight, config.logoBoxHeightPct);
        setSlider(logoBoxBorderWidth, config.logoBoxBorderWidthPx);
        setSlider(logoTextDepth, config.logoTextDepthPx);
        progressShowPercent.setChecked(config.progressShowPercent);
        setSlider(foregroundEffectStrength, config.foregroundEffectStrength);
        setSlider(foregroundEffectOpacity, config.foregroundEffectOpacity);
        setSlider(livebootEffectStrength, config.livebootEffectStrength);
        setSlider(livebootEffectOpacity, config.livebootEffectOpacity);
        setSlider(backgroundEffectStrength, config.backgroundEffectStrength);
        setSlider(backgroundEffectOpacity, config.backgroundEffectOpacity);
        setSlider(overlayEffectStrength, config.overlayEffectStrength);
        setSlider(screenEffectStrength, config.screenEffectStrength);
        setSlider(crtStrength, config.crtStrength);
        setSlider(crtOpacity, config.crtOpacity);
        setSlider(crtPixelation, config.crtPixelation);
        setSlider(crtSoftness, config.crtSoftness);
        setSlider(crtScanlines, config.crtScanlines);
        setSlider(crtChromatic, config.crtChromatic);
        setSlider(crtFlicker, config.crtFlicker);
        setSlider(wallpaperLogoWidth, config.wallpaperLogoWidthPct);
        setSlider(wallpaperLogoAlpha, config.wallpaperLogoAlpha);
        setSlider(livebootAlpha, config.livebootTextAlpha);
        setSlider(livebootSize, config.livebootTextSizePx);
        livebootEnabled.setChecked(config.livebootTextEnabled);
        gradientReverse.setChecked(config.gradientReverse);
        wallpaperLogoEnabled.setChecked(config.wallpaperLogoEnabled);
        livebootTextLines.setText(config.livebootTextLines);
        livebootFinalLinesEnabled.setChecked(config.livebootFinalLinesEnabled);
        livebootFinalLines.setText(config.livebootFinalLines);
        importedFontPath = config.logoFontPath == null ? "" : config.logoFontPath;
        updateGradientControlLabels();
        updateEffectsControlLabels();
        updateLogoControlLabels();
        updateProgressControlLabels();
        loading = false;
        if (stickyPreview != null) {
            stickyPreview.setConfig(currentConfig());
        }
    }

    private void updatePreview() {
        if (loading) {
            return;
        }
        persist();
        generateLogoFromText();
        OverlayConfig config = currentConfig();
        config.imagePath = appLogoFile().getAbsolutePath();
        stickyPreview.resetImageCache();
        if (previewFullscreenView != null) {
            previewFullscreenView.resetImageCache();
        }
        stickyPreview.setConfig(config);
        if (previewFullscreenView != null) {
            previewFullscreenView.setConfig(config);
        }
        setStatus("Preview refreshed.");
    }

    private void showFullscreenPreview() {
        if (previewFullscreenDialog != null && previewFullscreenDialog.isShowing()) {
            return;
        }
        previewFullscreenView = new BootOverlayView(this);
        previewFullscreenView.setPreviewMode(false);
        previewFullscreenView.setConfig(currentConfig());

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(Color.rgb(0, 0, 0));
        layout.addView(previewFullscreenView, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0,
                1f
        ));

        Button close = button("Close Full-Screen Preview");
        close.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (previewFullscreen != null) {
                    previewFullscreen.setChecked(false);
                }
                dismissFullscreenPreview();
            }
        });
        layout.addView(close);

        previewFullscreenDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        previewFullscreenDialog.setContentView(layout);
        previewFullscreenDialog.setOnDismissListener(new android.content.DialogInterface.OnDismissListener() {
            @Override public void onDismiss(android.content.DialogInterface dialog) {
                if (previewFullscreen != null && previewFullscreen.isChecked()) {
                    previewFullscreen.setChecked(false);
                }
                previewFullscreenDialog = null;
                previewFullscreenView = null;
            }
        });
        previewFullscreenDialog.show();
    }

    private void dismissFullscreenPreview() {
        if (previewFullscreenDialog != null) {
            previewFullscreenDialog.dismiss();
            previewFullscreenDialog = null;
            previewFullscreenView = null;
        }
    }

    private void applyToBootModule() {
        persist();
        generateLogoFromText();
        String module = cleanModuleId();
        if (module.length() == 0 || module.contains("/") || module.contains(" ")) {
            setStatus("Module ID is invalid.");
            return;
        }

        try {
            OverlayConfig config = currentConfig();
            File temp = new File(getCacheDir(), "brobro_overlay_config.txt");
            try (FileOutputStream output = new FileOutputStream(temp, false)) {
                output.write(config.toModuleConfigText().getBytes(StandardCharsets.UTF_8));
            }
            String target = "/data/adb/modules/" + module + "/config";
            String logoTarget = "/data/adb/modules/" + module + "/logo.png";
            String command = "cp " + shellQuote(temp.getAbsolutePath()) + " " + shellQuote(target)
                    + " && cp " + shellQuote(appLogoFile().getAbsolutePath()) + " " + shellQuote(logoTarget)
                    + " && chmod 0644 " + shellQuote(target) + " " + shellQuote(logoTarget)
                    + " && grep -q '^liveboot_text_display_mode=' " + shellQuote(target)
                    + " && test -s " + shellQuote(logoTarget);
            Process process = new ProcessBuilder("su", "-c", command).redirectErrorStream(true).start();
            String output = readProcessOutput(process);
            int code = process.waitFor();
            if (code == 0) {
                lastApplyStatus = "Applied successfully.";
                setStatus("Applied successfully. Reboot to test boot overlay.");
            } else {
                lastApplyStatus = "Failed to write module config/logo. exit=" + code + " output=" + limit(output);
                setStatus(lastApplyStatus);
            }
        } catch (Exception exception) {
            lastApplyStatus = "Root permission needed or apply failed: " + exception.getMessage();
            setStatus(lastApplyStatus);
        }
        refreshDiagnostics();
    }

    private void reloadFromModule(boolean showStatus) {
        try {
            String configText = runSuCapture("cat " + shellQuote("/data/adb/modules/" + cleanModuleId() + "/config"));
            OverlayConfig config = OverlayConfig.defaults();
            String[] lines = configText.split("\\r?\\n");
            for (String line : lines) {
                config.applyLine(line);
            }
            applyConfigToFields(config);
            byte[] logoBytes = runSuBytes("cat " + shellQuote("/data/adb/modules/" + cleanModuleId() + "/logo.png"));
            if (logoBytes.length > 0) {
                try (FileOutputStream output = new FileOutputStream(appLogoFile(), false)) {
                    output.write(logoBytes);
                }
            }
            persist();
            updatePreview();
            if (showStatus) {
                setStatus("Reloaded from module.");
            }
            if (stickyPreview != null) {
                stickyPreview.setConfig(config);
            }
        } catch (Exception exception) {
            if (showStatus) {
                setStatus("Reload failed: " + exception.getMessage());
            }
        }
    }

    private void refreshDiagnostics() {
        if (diagnosticsText == null) {
            return;
        }
        String module = cleanModuleId();
        String configPath = "/data/adb/modules/" + module + "/config";
        String logoPath = "/data/adb/modules/" + module + "/logo.png";
        String config = "";
        String log = "";
        String moduleVersion = "unknown";
        try {
            config = runSuCapture("cat " + shellQuote(configPath));
        } catch (Exception exception) {
            config = "unreadable: " + exception.getMessage();
        }
        try {
            moduleVersion = runSuCapture("grep '^version=' " + shellQuote("/data/adb/modules/" + module + "/module.prop") + " 2>/dev/null || true").trim();
        } catch (Exception ignored) {
        }
        try {
            log = runSuCapture("tail -n 80 /data/local/tmp/brobro_native_boot.log 2>/dev/null || true");
        } catch (Exception exception) {
            log = "unreadable: " + exception.getMessage();
        }
        diagnosticsText.setText("APK version: " + APK_VERSION
                + "\nModule version: " + moduleVersion
                + "\nConfig path: " + configPath
                + "\nLogo path: " + logoPath
                + "\nCurrent logo size: " + appLogoFile().length() + " bytes"
                + "\nLast apply status: " + lastApplyStatus
                + "\n\nCurrent config excerpt:\n" + limitLong(config, 1800)
                + "\n\nLast boot log excerpt:\n" + limitLong(log, 2200));
    }

    private void checkForUpdates(final boolean installAfterCheck) {
        clearCachedManifest();
        setUpdateStatus("Checking primary manifest + mirrors...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ManifestFetchResult fresh = fetchManifestWithFallback();
                    UpdatePlan plan;
                    String manifestSource;
                    long manifestFetchedAtMs;
                    if (fresh != null) {
                        saveCachedManifest(fresh.sourceUrl, fresh.json);
                        plan = readUpdatePlanFromJson(fresh.json);
                        manifestSource = fresh.sourceUrl;
                        manifestFetchedAtMs = fresh.fetchedAtMs;
                    } else {
                        String bundled = loadBundledManifest();
                        if (bundled != null) {
                            plan = readUpdatePlanFromJson(bundled);
                            manifestSource = "bundled fallback";
                            manifestFetchedAtMs = 0L;
                        } else {
                            throw new IOException("Unable to resolve manifest host.");
                        }
                    }
                    plan.manifestSource = manifestSource;
                    plan.manifestFetchedAtMs = manifestFetchedAtMs;
                    if (!plan.hasUpdate()) {
                        setUpdateStatus((fresh == null ? "Network unavailable; using bundled update info.\n\n" : "Already current.\n\n") + plan.summary());
                        return;
                    }
                    if (!installAfterCheck) {
                        postUpdateNotification(plan);
                        showUpdatePrompt(plan);
                        return;
                    }
                    installUpdatePlan(plan);
                } catch (final Exception exception) {
                    setUpdateStatus("Update failed: " + exception.getMessage());
                }
            }
        }).start();
    }

    private void checkForUpdatesOnLaunch() {
        if (launchUpdateCheckStarted) {
            return;
        }
        launchUpdateCheckStarted = true;
        checkForUpdates(false);
    }

    private void ensureNotificationPermission() {
        if (Build.VERSION.SDK_INT < 33) {
            return;
        }
        if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            return;
        }
        requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_POST_NOTIFICATIONS);
    }

    private void showUpdatePrompt(final UpdatePlan plan) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(OverlaySettingsActivity.this);
                builder.setTitle("Update available");
                builder.setMessage(plan.summary());
                builder.setPositiveButton("Update Now", new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    installUpdatePlan(plan);
                                } catch (final Exception exception) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            setUpdateStatus("Update failed: " + exception.getMessage());
                                            refreshDiagnostics();
                                        }
                                    });
                                }
                            }
                        }).start();
                    }
                });
                builder.setNegativeButton("Later", null);
                builder.show();
            }
        });
    }

    private void postUpdateNotification(final UpdatePlan plan) {
        try {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (manager == null) {
                return;
            }
            if (Build.VERSION.SDK_INT >= 26) {
                NotificationChannel existing = manager.getNotificationChannel(UPDATE_CHANNEL_ID);
                if (existing == null) {
                    NotificationChannel channel = new NotificationChannel(
                            UPDATE_CHANNEL_ID,
                            "BroBro Boot Overlay Updates",
                            NotificationManager.IMPORTANCE_HIGH);
                    channel.setDescription("Update availability alerts for BroBro Boot Overlay");
                    manager.createNotificationChannel(channel);
                }
            }
            Intent intent = new Intent(this, OverlaySettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    this,
                    1009,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            Notification.Builder builder = Build.VERSION.SDK_INT >= 26
                    ? new Notification.Builder(this, UPDATE_CHANNEL_ID)
                    : new Notification.Builder(this);
            String appVersion = plan.app == null ? "unknown" : plan.app.versionName;
            String moduleVersion = plan.module == null ? "unknown" : plan.module.versionName;
            builder.setContentTitle("BroBro Boot Overlay update available");
            builder.setContentText(plan.app == null && plan.module == null
                    ? "A new update is ready."
                    : "App " + appVersion + " and module " + moduleVersion + " are available.");
            builder.setSmallIcon(android.R.drawable.sym_def_app_icon);
            builder.setContentIntent(pendingIntent);
            builder.setAutoCancel(true);
            builder.setOnlyAlertOnce(true);
            builder.setPriority(Notification.PRIORITY_HIGH);
            builder.setCategory(Notification.CATEGORY_STATUS);
            manager.notify(UPDATE_NOTIFICATION_ID, builder.build());
        } catch (Exception ignored) {
        }
    }

    private void installUpdatePlan(UpdatePlan plan) throws Exception {
        try {
            if (plan.module != null && plan.module.url.length() > 0) {
                File moduleFile = downloadToFile(plan.module.url, "brobro_boot_overlay_module.zip");
                verifySha256IfPresent(moduleFile, plan.module.sha256);
                installModuleWithRoot(moduleFile);
            }
            if (plan.app != null && plan.app.url.length() > 0) {
                File appFile = downloadToFile(plan.app.url, "brobro_boot_overlay_update.apk");
                verifySha256IfPresent(appFile, plan.app.sha256);
                installApkWithRoot(appFile);
            }
            clearCachedManifest();
            setUpdateStatus("Update task finished.\n\n" + plan.summary());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    refreshDiagnostics();
                    promptRebootAfterUpdate(plan);
                }
            });
        } catch (Exception exception) {
            setUpdateStatus("Update failed: " + exception.getMessage());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    refreshDiagnostics();
                }
            });
        }
    }

    private UpdatePlan readUpdatePlan(String manifestUrl) throws Exception {
        return readUpdatePlanFromJson(fetchText(manifestUrl));
    }

    private UpdatePlan readUpdatePlanFromJson(String manifestText) throws Exception {
        JSONObject manifest = new JSONObject(manifestText);
        validateManifestIdentity(manifest);
        UpdatePlan plan = new UpdatePlan();
        plan.localAppCode = installedAppVersionCode();
        plan.localAppVersion = installedAppVersionName();
        plan.localModuleCode = installedModuleVersionCode();
        plan.localModuleVersion = installedModuleVersionName();

        if (manifest.has("latestVersionCode") || manifest.has("apkUrl") || manifest.has("moduleUrl")) {
            plan.app = new RemotePayload(
                    manifest.optInt("latestVersionCode", plan.localAppCode),
                    manifest.optString("latestVersionName", "unknown"),
                    manifest.optString("apkUrl", ""),
                    manifest.optString("sha256Apk", "")
            );
            plan.module = new RemotePayload(
                    manifest.optInt("latestVersionCode", plan.localModuleCode),
                    manifest.optString("latestVersionName", "unknown"),
                    manifest.optString("moduleUrl", ""),
                    manifest.optString("sha256Module", "")
            );
        } else {
            JSONObject app = manifest.optJSONObject("app");
            JSONObject module = manifest.optJSONObject("module");
            plan.app = app == null ? null : new RemotePayload(
                    app.optInt("versionCode", plan.localAppCode),
                    app.optString("versionName", "unknown"),
                    app.optString("url", ""),
                    app.optString("sha256", "")
            );
            plan.module = module == null ? null : new RemotePayload(
                    module.optInt("versionCode", plan.localModuleCode),
                    module.optString("versionName", "unknown"),
                    module.optString("url", ""),
                    module.optString("sha256", "")
            );
        }
        return plan;
    }

    private ManifestFetchResult fetchManifestWithFallback() {
        String[] urls = new String[]{DEFAULT_MANIFEST_URL, FALLBACK_MANIFEST_URL_1, FALLBACK_MANIFEST_URL_2};
        for (String url : urls) {
            try {
                String json = fetchText(url);
                validateManifestIdentity(new JSONObject(json));
                return new ManifestFetchResult(url, json, System.currentTimeMillis());
            } catch (Exception exception) {
            }
        }
        return null;
    }

    private void setUpdateStatus(final String text) {
        final String value = text == null ? "" : text;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (updateSummary != null) {
                    updateSummary.setText(value);
                }
                setStatus(limitLong(value, 300));
            }
        });
    }

    private String fetchText(String urlString) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);
        connection.setUseCaches(false);
        connection.setRequestMethod("GET");
        connection.connect();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
            return builder.toString();
        } finally {
            connection.disconnect();
        }
    }

    private CachedManifest loadCachedManifest() {
        SharedPreferences prefs = getSharedPreferences(UPDATE_CACHE_PREFS, MODE_PRIVATE);
        String json = prefs.getString(UPDATE_CACHE_JSON, "");
        if (json == null || json.trim().length() == 0) {
            return null;
        }
        String source = prefs.getString(UPDATE_CACHE_SOURCE, DEFAULT_MANIFEST_URL);
        long fetchedAt = prefs.getLong(UPDATE_CACHE_TIME, 0L);
        try {
            validateManifestIdentity(new JSONObject(json));
            return new CachedManifest(source, json, fetchedAt);
        } catch (Exception exception) {
            clearCachedManifest();
            setUpdateStatus("Ignored cached manifest from a different project.");
            return null;
        }
    }

    private void saveCachedManifest(String sourceUrl, String json) {
        if (json == null || json.trim().length() == 0) {
            return;
        }
        getSharedPreferences(UPDATE_CACHE_PREFS, MODE_PRIVATE).edit()
                .putString(UPDATE_CACHE_SOURCE, sourceUrl == null ? DEFAULT_MANIFEST_URL : sourceUrl)
                .putString(UPDATE_CACHE_JSON, json)
                .putLong(UPDATE_CACHE_TIME, System.currentTimeMillis())
                .apply();
    }

    private void clearCachedManifest() {
        getSharedPreferences(UPDATE_CACHE_PREFS, MODE_PRIVATE).edit().clear().apply();
    }

    private String loadBundledManifest() {
        int resourceId = getResources().getIdentifier("update_fallback", "raw", getPackageName());
        if (resourceId == 0) {
            return null;
        }
        try (InputStream input = getResources().openRawResource(resourceId);
             BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
            return builder.toString();
        } catch (Exception ignored) {
            return null;
        }
    }

    private void validateManifestIdentity(JSONObject manifest) throws Exception {
        if (manifest == null) {
            throw new Exception("Manifest missing.");
        }
        String project = manifest.optString("project", "").trim();
        String packageName = manifest.optString("package", "").trim();
        if (project.length() == 0 || packageName.length() == 0) {
            throw new Exception("Manifest missing BroBro identity.");
        }
        if (!EXPECTED_UPDATE_PROJECT.equalsIgnoreCase(project)) {
            throw new Exception("Manifest project mismatch.");
        }
        if (!EXPECTED_UPDATE_PACKAGE.equalsIgnoreCase(packageName)) {
            throw new Exception("Manifest package mismatch.");
        }
    }

    private String formatTime(long epochMs) {
        if (epochMs <= 0L) {
            return "unknown time";
        }
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        format.setTimeZone(java.util.TimeZone.getDefault());
        return format.format(new java.util.Date(epochMs));
    }

    private File downloadToFile(String urlString, String name) throws IOException {
        File destination = new File(getCacheDir(), name);
        HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);
        connection.setUseCaches(false);
        connection.setRequestMethod("GET");
        connection.connect();
        try (InputStream input = connection.getInputStream();
             FileOutputStream output = new FileOutputStream(destination, false)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = input.read(buffer)) >= 0) {
                output.write(buffer, 0, read);
            }
        } finally {
            connection.disconnect();
        }
        return destination;
    }

    private static final class CachedManifest {
        final String sourceUrl;
        final String json;
        final long fetchedAtMs;

        CachedManifest(String sourceUrl, String json, long fetchedAtMs) {
            this.sourceUrl = sourceUrl == null ? DEFAULT_MANIFEST_URL : sourceUrl;
            this.json = json == null ? "" : json;
            this.fetchedAtMs = fetchedAtMs;
        }
    }

    private static final class ManifestFetchResult {
        final String sourceUrl;
        final String json;
        final long fetchedAtMs;

        ManifestFetchResult(String sourceUrl, String json, long fetchedAtMs) {
            this.sourceUrl = sourceUrl == null ? DEFAULT_MANIFEST_URL : sourceUrl;
            this.json = json == null ? "" : json;
            this.fetchedAtMs = fetchedAtMs;
        }
    }

    private void verifySha256IfPresent(File file, String expected) throws Exception {
        if (file == null || !file.isFile() || expected == null || expected.trim().length() == 0) {
            return;
        }
        String actual = sha256(file);
        if (!actual.equalsIgnoreCase(expected.trim())) {
            throw new Exception("SHA-256 mismatch for " + file.getName());
        }
    }

    private String sha256(File file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (InputStream input = new java.io.FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = input.read(buffer)) >= 0) {
                digest.update(buffer, 0, read);
            }
        }
        byte[] hash = digest.digest();
        StringBuilder builder = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            builder.append(String.format(Locale.US, "%02x", b));
        }
        return builder.toString();
    }

    private void installApkWithRoot(File apkFile) throws Exception {
        if (apkFile == null || !apkFile.isFile()) {
            throw new Exception("APK download missing.");
        }
        String command = "pm install -r --dont-kill " + shellQuote(apkFile.getAbsolutePath())
                + " || pm install -r " + shellQuote(apkFile.getAbsolutePath());
        String output = runSuCapture(command);
        setUpdateStatus("APK install command sent.\n" + limitLong(output, 250));
    }

    private void installModuleWithRoot(File moduleZip) throws Exception {
        if (moduleZip == null || !moduleZip.isFile()) {
            throw new Exception("Module ZIP download missing.");
        }
        String script =
                "MAGISK_BIN=\"\"; " +
                "for CANDIDATE in /system_ext/bin/magisk /system/bin/magisk /data/adb/magisk/magisk; do " +
                "if [ -x \"$CANDIDATE\" ]; then MAGISK_BIN=\"$CANDIDATE\"; break; fi; " +
                "done; " +
                "if [ -z \"$MAGISK_BIN\" ]; then MAGISK_BIN=\"$(command -v magisk 2>/dev/null || true)\"; fi; " +
                "if [ -z \"$MAGISK_BIN\" ]; then echo 'magisk binary unavailable'; exit 127; fi; " +
                "echo 'Magisk binary:' \"$MAGISK_BIN\"; " +
                "\"$MAGISK_BIN\" --install-module " + shellQuote(moduleZip.getAbsolutePath());
        String output = runSuCapture(script);
        setUpdateStatus("Module install command sent.\n" + limitLong(output, 250));
    }

    private int installedAppVersionCode() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            return (int) info.getLongVersionCode();
        } catch (Exception ignored) {
            return 0;
        }
    }

    private String installedAppVersionName() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            return info.versionName == null ? "unknown" : info.versionName;
        } catch (Exception ignored) {
            return "unknown";
        }
    }

    private int installedModuleVersionCode() {
        try {
            String text = runSuCapture("grep '^versionCode=' " + shellQuote("/data/adb/modules/" + cleanModuleId() + "/module.prop") + " 2>/dev/null || true").trim();
            String value = text.contains("=") ? text.substring(text.indexOf('=') + 1).trim() : text;
            return Integer.parseInt(value);
        } catch (Exception ignored) {
            return 0;
        }
    }

    private String installedModuleVersionName() {
        try {
            String text = runSuCapture("grep '^version=' " + shellQuote("/data/adb/modules/" + cleanModuleId() + "/module.prop") + " 2>/dev/null || true").trim();
            String value = text.contains("=") ? text.substring(text.indexOf('=') + 1).trim() : text;
            return value.length() == 0 ? "unknown" : value;
        } catch (Exception ignored) {
            return "unknown";
        }
    }

    private static final class RemotePayload {
        final int versionCode;
        final String versionName;
        final String url;
        final String sha256;

        RemotePayload(int versionCode, String versionName, String url, String sha256) {
            this.versionCode = versionCode;
            this.versionName = versionName == null ? "unknown" : versionName;
            this.url = url == null ? "" : url;
            this.sha256 = sha256 == null ? "" : sha256;
        }
    }

    private static final class UpdatePlan {
        RemotePayload app;
        RemotePayload module;
        int localAppCode;
        int localModuleCode;
        String localAppVersion;
        String localModuleVersion;
        String manifestSource = "unknown";
        long manifestFetchedAtMs = 0L;

        boolean hasUpdate() {
            return (app != null && app.versionCode > localAppCode) || (module != null && module.versionCode > localModuleCode);
        }

        String summary() {
            StringBuilder builder = new StringBuilder();
            builder.append("Manifest source: ").append(manifestSource == null ? "unknown" : manifestSource);
            if (manifestFetchedAtMs > 0L) {
                builder.append(" @ ").append(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(new java.util.Date(manifestFetchedAtMs)));
            }
            builder.append('\n');
            builder.append("Installed app: ").append(localAppVersion).append(" (").append(localAppCode).append(")\n");
            builder.append("Remote app: ").append(app == null ? "unknown" : app.versionName).append(" (")
                    .append(app == null ? localAppCode : app.versionCode).append(")\n");
            builder.append("Installed module: ").append(localModuleVersion).append(" (").append(localModuleCode).append(")\n");
            builder.append("Remote module: ").append(module == null ? "unknown" : module.versionName).append(" (")
                    .append(module == null ? localModuleCode : module.versionCode).append(")\n");
            return builder.toString();
        }
    }

    private void promptRebootAfterUpdate(final UpdatePlan plan) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reboot now?");
        builder.setMessage("The APK and module update task finished.\n\n" + plan.summary());
        builder.setPositiveButton("Reboot Now", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {
                try {
                    runSuCapture("reboot");
                    setUpdateStatus("Reboot command sent.");
                } catch (Exception exception) {
                    setUpdateStatus("Reboot failed: " + exception.getMessage());
                }
            }
        });
        builder.setNegativeButton("Later", null);
        builder.show();
    }

    private void saveProfileSlot() {
        try {
            int slot = selectedProfileSlot();
            if (slot < 1 || slot > PROFILE_LABELS.length) {
                setStatus("Choose a profile slot first.");
                return;
            }
            getSharedPreferences(PREFS, MODE_PRIVATE).edit()
                    .putString(profileKey(slot), currentConfig().toModuleConfigText())
                    .putString(profileNameKey(slot), currentProfileName())
                    .apply();
            updatePresetModeValue("manual");
            setStatus("Saved current settings to " + displayProfileName(slot) + ".");
        } catch (Exception exception) {
            setStatus("Could not save profile: " + exception.getMessage());
        }
    }

    private void loadProfileSlot(boolean showStatus) {
        try {
            int slot = selectedProfileSlot();
            if (slot < 1 || slot > PROFILE_LABELS.length) {
                if (showStatus) {
                    setStatus("Choose a profile slot first.");
                }
                return;
            }
            String text = getSharedPreferences(PREFS, MODE_PRIVATE).getString(profileKey(slot), "");
            if (text == null || text.trim().length() == 0) {
                if (showStatus) {
                    setStatus(displayProfileName(slot) + " is empty.");
                }
                return;
            }
            OverlayConfig config = OverlayConfig.defaults();
            for (String line : text.split("\\r?\\n")) {
                config.applyLine(line);
            }
            applyConfigToFields(config);
            persist();
            updatePreview();
            syncProfileNameField(slot);
            if (showStatus) {
                setStatus("Loaded " + displayProfileName(slot) + ".");
            }
        } catch (Exception exception) {
            if (showStatus) {
                setStatus("Could not load profile: " + exception.getMessage());
            }
        }
    }

    private int selectedProfileSlot() {
        return bootProfileSlot == null ? 1 : bootProfileSlot.getSelectedItemPosition() + 1;
    }

    private String profileKey(int slot) {
        return "bootProfile" + slot;
    }

    private String profileNameKey(int slot) {
        return "bootProfileName" + slot;
    }

    private String currentProfileName() {
        return bootProfileName == null ? "" : bootProfileName.getText().toString().trim();
    }

    private void syncProfileNameField(int slot) {
        if (bootProfileName == null) {
            return;
        }
        String value = getSharedPreferences(PREFS, MODE_PRIVATE).getString(profileNameKey(slot), PROFILE_LABELS[Math.max(0, slot - 1)]);
        bootProfileName.setText(value == null || value.trim().length() == 0 ? PROFILE_LABELS[Math.max(0, slot - 1)] : value);
    }

    private String profileLabelForSlot(int slot) {
        int safeSlot = Math.max(1, Math.min(PROFILE_LABELS.length, slot));
        String value = getSharedPreferences(PREFS, MODE_PRIVATE).getString(profileNameKey(safeSlot), PROFILE_LABELS[safeSlot - 1]);
        return value == null || value.trim().length() == 0 ? PROFILE_LABELS[safeSlot - 1] : value.trim();
    }

    private String[] profileLabelsForSpinner() {
        String[] labels = new String[PROFILE_LABELS.length];
        for (int i = 0; i < PROFILE_LABELS.length; i++) {
            labels[i] = profileLabelForSlot(i + 1);
        }
        return labels;
    }

    private void refreshProfileSlotSpinnerLabels() {
        if (bootProfileSlot == null) {
            return;
        }
        int selectedSlot = selectedProfileSlot();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, profileLabelsForSpinner());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bootProfileSlot.setAdapter(adapter);
        setProfileSlotSelection(selectedSlot);
    }

    private void setProfileSlotSelection(int slot) {
        if (bootProfileSlot == null) {
            return;
        }
        int index = Math.max(1, Math.min(PROFILE_LABELS.length, slot)) - 1;
        bootProfileSlot.setSelection(index, false);
    }

    private void updatePresetModeValue(String mode) {
        bootPresetMode = mode == null || mode.trim().length() == 0 ? "manual" : mode.trim().toLowerCase(Locale.US);
        if (presetModeValue != null) {
            presetModeValue.setText("Preset mode: " + bootPresetMode);
        }
    }

    private String displayProfileName(int slot) {
        String value = getSharedPreferences(PREFS, MODE_PRIVATE).getString(profileNameKey(slot), PROFILE_LABELS[Math.max(0, slot - 1)]);
        if (value == null || value.trim().length() == 0) {
            return PROFILE_LABELS[Math.max(0, slot - 1)];
        }
        return value.trim();
    }

    private void renameProfileSlot() {
        int slot = selectedProfileSlot();
        String name = currentProfileName();
        if (slot < 1 || slot > PROFILE_LABELS.length) {
            setStatus("Choose a profile slot first.");
            return;
        }
        if (name.length() == 0) {
            setStatus("Enter a profile name first.");
            return;
        }
        getSharedPreferences(PREFS, MODE_PRIVATE).edit()
                .putString(profileNameKey(slot), name)
                .apply();
        refreshProfileSlotSpinnerLabels();
        setStatus("Renamed slot " + slot + " to " + name + ".");
    }

    private void cycleProfileSlot(boolean shuffle) {
        int current = selectedProfileSlot();
        int next;
        if (shuffle) {
            next = 1 + (int) (SystemClock.uptimeMillis() % PROFILE_LABELS.length);
            updatePresetModeValue("random");
        } else {
            next = current >= PROFILE_LABELS.length ? 1 : current + 1;
            updatePresetModeValue("cycle");
        }
        setProfileSlotSelection(next);
        syncProfileNameField(next);
        persist();
        loadProfileSlot(true);
    }

    private void writeAppConfigFile() {
        try {
            OverlayConfig config = currentConfig();
            config.imagePath = appLogoFile().getAbsolutePath();
            try (FileOutputStream output = new FileOutputStream(new File(getFilesDir(), APP_CONFIG_FILE), false)) {
                output.write(config.toModuleConfigText().getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception exception) {
            setStatus("Local config write failed: " + exception.getMessage());
        }
    }

    private boolean generateLogoFromText() {
        String signature = selected(logoFontMode)
                + "|" + LOGO_GENERATION_SIGNATURE_VERSION
                + "|" + sliderValue(logoSize)
                + "|" + logoFillColor.getText().toString().trim()
                + "|" + logoBackgroundColor.getText().toString().trim()
                + "|" + logoBoxBorderColor.getText().toString().trim()
                + "|" + sliderValue(logoBoxWidth)
                + "|" + sliderValue(logoBoxHeight)
                + "|" + sliderValue(logoBoxBorderWidth)
                + "|" + sliderValue(logoTextDepth)
                + "|" + importedFontPath;
        if (signature.equals(lastGeneratedLogoSignature) && appLogoFile().isFile()) {
            return false;
        }
        try {
            Bitmap bitmap = Bitmap.createBitmap(1100, 300, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            canvas.drawColor(Color.TRANSPARENT);
            float boxWidth = 1100f * (sliderValue(logoBoxWidth) / 100f);
            float boxHeight = 300f * (sliderValue(logoBoxHeight) / 100f);
            float boxLeft = (1100f - boxWidth) * 0.5f;
            float boxTop = (300f - boxHeight) * 0.5f;
            float boxRight = boxLeft + boxWidth;
            float boxBottom = boxTop + boxHeight;
            float radius = Math.max(16f, Math.min(boxWidth, boxHeight) * 0.10f);
            android.graphics.RectF box = new android.graphics.RectF(boxLeft, boxTop, boxRight, boxBottom);
            paint.setColor(OverlayConfig.parseColor(
                    logoBackgroundColor.getText().toString(),
                    Color.argb(120, 0, 59, 70)
            ));
            canvas.drawRoundRect(box, radius, radius, paint);
            int borderWidth = sliderValue(logoBoxBorderWidth);
            if (borderWidth > 0) {
                Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                borderPaint.setStyle(Paint.Style.STROKE);
                borderPaint.setStrokeWidth(borderWidth);
                borderPaint.setColor(OverlayConfig.parseColor(
                        logoBoxBorderColor.getText().toString(),
                        Color.WHITE
                ));
                canvas.drawRoundRect(
                        new android.graphics.RectF(
                                box.left + borderWidth * 0.5f,
                                box.top + borderWidth * 0.5f,
                                box.right - borderWidth * 0.5f,
                                box.bottom - borderWidth * 0.5f
                        ),
                        Math.max(0f, radius - borderWidth * 0.5f),
                        Math.max(0f, radius - borderWidth * 0.5f),
                        borderPaint
                );
            }

            paint.setAlpha(255);
            int depth = sliderValue(logoTextDepth);
            if ("Pixel Retro".equals(selected(logoFontMode))) {
                drawPixelLogo(canvas, paint, box, depth);
            } else {
                Typeface typeface = resolveLogoTypeface();
                if (typeface == null) {
                    setStatus("Logo generation failed: selected font unavailable.");
                    return false;
                }
                int fillColor = OverlayConfig.parseColor(logoFillColor.getText().toString(), Color.WHITE);
                int shadowColor = OverlayConfig.parseColor(logoBackgroundColor.getText().toString(), Color.BLACK);
                if (depth > 0) {
                    paint.setShadowLayer(Math.max(1f, depth * 0.9f), depth / 3f, depth / 3f, shadowColor);
                }
                paint.setColor(fillColor);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTypeface(typeface);
                float desiredSize = 142f * (sliderValue(logoSize) / 100f);
                float maxWidth = Math.max(1f, box.width() - 48f);
                float maxHeight = Math.max(1f, box.height() - 48f);
                paint.setTextSize(desiredSize);
                float textWidth = paint.measureText(FIXED_LOGO_TEXT);
                if (textWidth > maxWidth) {
                    paint.setTextSize(Math.max(24f, desiredSize * (maxWidth / textWidth)));
                }
                Paint.FontMetrics metrics = paint.getFontMetrics();
                float textHeight = metrics.descent - metrics.ascent;
                if (textHeight > maxHeight) {
                    float adjustedSize = paint.getTextSize() * (maxHeight / textHeight);
                    paint.setTextSize(Math.max(24f, adjustedSize));
                    metrics = paint.getFontMetrics();
                }
                float centerX = box.centerX();
                float baseline = box.centerY() - (metrics.ascent + metrics.descent) * 0.5f;
                if (depth > 0) {
                    Paint shadowPaint = new Paint(paint);
                    shadowPaint.setColor(shadowColor);
                    shadowPaint.setAlpha(120);
                    shadowPaint.setShadowLayer(Math.max(1f, depth * 0.9f), depth / 3f, depth / 3f, shadowColor);
                    canvas.drawText(FIXED_LOGO_TEXT, centerX + depth / 3f, baseline + depth / 3f, shadowPaint);
                }
                paint.clearShadowLayer();
                canvas.drawText(FIXED_LOGO_TEXT, centerX, baseline, paint);
            }
            try (FileOutputStream output = new FileOutputStream(appLogoFile(), false)) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            }
            lastGeneratedLogoSignature = signature;
            return true;
        } catch (Exception exception) {
            setStatus("Logo generation failed: " + exception.getMessage());
            return false;
        }
    }

    private Typeface resolveLogoTypeface() {
        String selected = selected(logoFontMode);
        if ("Sans".equals(selected)) {
            return Typeface.SANS_SERIF;
        }
        if ("Serif".equals(selected)) {
            return Typeface.SERIF;
        }
        if ("Monospace".equals(selected)) {
            return Typeface.MONOSPACE;
        }
        if ("Imported Font".equals(selected) && importedFontPath.length() > 0) {
            try {
                File file = new File(importedFontPath);
                return file.isFile() ? Typeface.createFromFile(file) : null;
            } catch (Exception ignored) {
                return null;
            }
        }
        return Typeface.SANS_SERIF;
    }

    private void drawPixelLogo(Canvas canvas, Paint paint, android.graphics.RectF box, int depth) {
        int desiredScale = Math.max(8, (24 * sliderValue(logoSize)) / 100);
        int baseWidth = Math.max(1, pixelTextWidth(FIXED_LOGO_TEXT, 1));
        int baseHeight = 7;
        int fitWidth = Math.max(8, (int) Math.floor((box.width() - 48f) / baseWidth));
        int fitHeight = Math.max(8, (int) Math.floor((box.height() - 48f) / baseHeight));
        int scale = Math.max(8, Math.min(desiredScale, Math.max(8, Math.min(fitWidth, fitHeight))));
        int width = pixelTextWidth(FIXED_LOGO_TEXT, scale);
        int x = Math.round(box.left + (box.width() - width) / 2f);
        int y = Math.round(box.top + (box.height() - 7f * scale) / 2f);
        int fillColor = OverlayConfig.parseColor(logoFillColor.getText().toString(), Color.WHITE);
        int shadowColor = OverlayConfig.parseColor(logoBackgroundColor.getText().toString(), Color.BLACK);
        if (depth > 0) {
            drawPixelString(canvas, FIXED_LOGO_TEXT, x + Math.max(1, depth / 3), y + Math.max(1, depth / 3), scale, shadowColor, paint);
            paint.setAlpha(120);
        }
        paint.setAlpha(255);
        drawPixelString(canvas, FIXED_LOGO_TEXT, x, y, scale, fillColor, paint);
    }

    private int pixelTextWidth(String text, int scale) {
        int width = 0;
        for (int i = 0; i < text.length(); i++) {
            width += (pixelGlyph(text.charAt(i))[0].length() + 1) * scale;
        }
        return Math.max(0, width - scale);
    }

    private void drawPixelString(Canvas canvas, String text, int x, int y, int scale, int color, Paint paint) {
        int cursor = x;
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < text.length(); i++) {
            String[] glyph = pixelGlyph(text.charAt(i));
            for (int gy = 0; gy < glyph.length; gy++) {
                for (int gx = 0; gx < glyph[gy].length(); gx++) {
                    if (glyph[gy].charAt(gx) == '1') {
                        canvas.drawRect(cursor + gx * scale, y + gy * scale, cursor + (gx + 1) * scale, y + (gy + 1) * scale, paint);
                    }
                }
            }
            cursor += (glyph[0].length() + 1) * scale;
        }
    }

    private String[] pixelGlyph(char raw) {
        char c = Character.toUpperCase(raw);
        switch (c) {
            case 'A': return new String[]{"01110","10001","10001","11111","10001","10001","10001"};
            case 'D': return new String[]{"11110","10001","10001","10001","10001","10001","11110"};
            case 'E': return new String[]{"11111","10000","10000","11110","10000","10000","11111"};
            case 'O': return new String[]{"01110","10001","10001","10001","10001","10001","01110"};
            case 'S': return new String[]{"01111","10000","10000","01110","00001","00001","11110"};
            case 'V': return new String[]{"10001","10001","10001","10001","01010","01010","00100"};
            case ' ': return new String[]{"000","000","000","000","000","000","000"};
            default: return new String[]{"11111","00001","00010","00100","01000","00000","01000"};
        }
    }

    private void importFontFile() {
        pendingLocalInstallKind = "";
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_FONT_FILE);
    }

    private void pickLocalInstall(String kind) {
        pendingLocalInstallKind = kind == null ? "" : kind;
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, "apk".equalsIgnoreCase(pendingLocalInstallKind) ? REQUEST_LOCAL_APK_FILE : REQUEST_LOCAL_MODULE_FILE);
        setStatus("Choose a local " + pendingLocalInstallKind + " file.");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null || data.getData() == null) {
            return;
        }
        if (requestCode != REQUEST_FONT_FILE) {
            if (requestCode != REQUEST_LOCAL_APK_FILE && requestCode != REQUEST_LOCAL_MODULE_FILE) {
                return;
            }
        }
        if (requestCode == REQUEST_LOCAL_APK_FILE || requestCode == REQUEST_LOCAL_MODULE_FILE) {
            handleLocalInstallResult(requestCode, data);
            return;
        }
        try (InputStream input = getContentResolver().openInputStream(data.getData());
             FileOutputStream output = new FileOutputStream(new File(getFilesDir(), "logo_font.ttf"), false)) {
            if (input == null) {
                setStatus("Font import failed: no input stream.");
                return;
            }
            byte[] buffer = new byte[8192];
            int read;
            while ((read = input.read(buffer)) >= 0) {
                output.write(buffer, 0, read);
            }
            importedFontPath = new File(getFilesDir(), "logo_font.ttf").getAbsolutePath();
            setSpinner(logoFontMode, "Imported Font");
            persist();
            updatePreview();
            setStatus("Imported font.");
        } catch (Exception exception) {
            setStatus("Font import failed: " + exception.getMessage());
        }
    }

    private void handleLocalInstallResult(int requestCode, Intent data) {
        if (data == null || data.getData() == null) {
            setStatus("No file selected.");
            return;
        }
        String kind = requestCode == REQUEST_LOCAL_MODULE_FILE ? "module" : "apk";
        File destination = new File(getCacheDir(), "apk".equals(kind) ? "brobro_local_install.apk" : "brobro_local_module.zip");
        try (InputStream input = getContentResolver().openInputStream(data.getData());
             FileOutputStream output = new FileOutputStream(destination, false)) {
            if (input == null) {
                setStatus("Local install failed: no input stream.");
                return;
            }
            byte[] buffer = new byte[8192];
            int read;
            while ((read = input.read(buffer)) >= 0) {
                output.write(buffer, 0, read);
            }
            if ("apk".equals(kind)) {
                installApkWithRoot(destination);
                setStatus("Local APK install sent from inside the app.");
            } else {
                installModuleWithRoot(destination);
                setStatus("Local module install sent from inside the app.");
            }
            refreshDiagnostics();
        } catch (Exception exception) {
            setStatus("Local install failed: " + exception.getMessage());
        } finally {
            pendingLocalInstallKind = "";
        }
    }

    private void addWatchers() {
        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { updatePreview(); }
            @Override public void afterTextChanged(Editable s) {}
        };
        gradientColor1.addTextChangedListener(watcher);
        gradientColor2.addTextChangedListener(watcher);
        gradientColor3.addTextChangedListener(watcher);
        logoFillColor.addTextChangedListener(watcher);
        logoBackgroundColor.addTextChangedListener(watcher);
        logoBoxBorderColor.addTextChangedListener(watcher);
        progressColor.addTextChangedListener(watcher);
        progressBorderColor.addTextChangedListener(watcher);
        livebootTextColor.addTextChangedListener(watcher);
        livebootTextLines.addTextChangedListener(watcher);
        livebootFinalLines.addTextChangedListener(watcher);
        livebootEnabled.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) { updatePreview(); }
        });
        livebootFinalLinesEnabled.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) { updatePreview(); }
        });
        wallpaperLogoEnabled.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) { updatePreview(); }
        });
        gradientReverse.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) { updatePreview(); }
        });
        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { updatePreview(); }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        };
        gradientPattern.setOnItemSelectedListener(spinnerListener);
        gradientAnimation.setOnItemSelectedListener(spinnerListener);
        backgroundMode.setOnItemSelectedListener(spinnerListener);
        logoFontMode.setOnItemSelectedListener(spinnerListener);
        progressDuration.setOnItemSelectedListener(spinnerListener);
        livebootDisplayMode.setOnItemSelectedListener(spinnerListener);
        livebootFeedMode.setOnItemSelectedListener(spinnerListener);
        livebootRevealMode.setOnItemSelectedListener(spinnerListener);
        livebootRevealSpeed.setOnItemSelectedListener(spinnerListener);
        livebootSpeed.setOnItemSelectedListener(spinnerListener);
        livebootDensity.setOnItemSelectedListener(spinnerListener);
        livebootFinalLead.setOnItemSelectedListener(spinnerListener);
        foregroundEffectMode.setOnItemSelectedListener(spinnerListener);
        livebootEffectMode.setOnItemSelectedListener(spinnerListener);
        backgroundEffectMode.setOnItemSelectedListener(spinnerListener);
        overlayEffectMode.setOnItemSelectedListener(spinnerListener);
        crtMode.setOnItemSelectedListener(spinnerListener);
        wallpaperLogoPosition.setOnItemSelectedListener(spinnerListener);
        bootProfileSlot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                syncProfileNameField(position + 1);
                updatePreview();
                if (!loading) {
                    updatePresetModeValue("manual");
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
        SeekBar.OnSeekBarChangeListener sliderListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateGradientControlLabels();
                updateEffectsControlLabels();
                updateLogoControlLabels();
                updateProgressControlLabels();
                if (fromUser) {
                    updatePreview();
                }
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) { updatePreview(); }
        };
        gradientSpeedPercent.setOnSeekBarChangeListener(sliderListener);
        gradientAngle.setOnSeekBarChangeListener(sliderListener);
        gradientScale.setOnSeekBarChangeListener(sliderListener);
        gradientCenterX.setOnSeekBarChangeListener(sliderListener);
        gradientCenterY.setOnSeekBarChangeListener(sliderListener);
        gradientStop2.setOnSeekBarChangeListener(sliderListener);
        gradientStop3.setOnSeekBarChangeListener(sliderListener);
        gradientBrightness.setOnSeekBarChangeListener(sliderListener);
        gradientExposure.setOnSeekBarChangeListener(sliderListener);
        gradientSaturation.setOnSeekBarChangeListener(sliderListener);
        gradientContrast.setOnSeekBarChangeListener(sliderListener);
        gradientGamma.setOnSeekBarChangeListener(sliderListener);
        gradientVibrance.setOnSeekBarChangeListener(sliderListener);
        gradientHueShift.setOnSeekBarChangeListener(sliderListener);
        gradientYellowBoost.setOnSeekBarChangeListener(sliderListener);
        gradientRedBoost.setOnSeekBarChangeListener(sliderListener);
        gradientGreenBoost.setOnSeekBarChangeListener(sliderListener);
        gradientBlueBoost.setOnSeekBarChangeListener(sliderListener);
        gradientMotionX.setOnSeekBarChangeListener(sliderListener);
        gradientMotionY.setOnSeekBarChangeListener(sliderListener);
        logoSize.setOnSeekBarChangeListener(sliderListener);
        logoBoxWidth.setOnSeekBarChangeListener(sliderListener);
        logoBoxHeight.setOnSeekBarChangeListener(sliderListener);
        logoBoxBorderWidth.setOnSeekBarChangeListener(sliderListener);
        logoTextDepth.setOnSeekBarChangeListener(sliderListener);
        progressWidth.setOnSeekBarChangeListener(sliderListener);
        progressHeight.setOnSeekBarChangeListener(sliderListener);
        progressBorderWidth.setOnSeekBarChangeListener(sliderListener);
        progressPixelation.setOnSeekBarChangeListener(sliderListener);
        foregroundEffectStrength.setOnSeekBarChangeListener(sliderListener);
        foregroundEffectOpacity.setOnSeekBarChangeListener(sliderListener);
        livebootEffectStrength.setOnSeekBarChangeListener(sliderListener);
        livebootEffectOpacity.setOnSeekBarChangeListener(sliderListener);
        backgroundEffectStrength.setOnSeekBarChangeListener(sliderListener);
        backgroundEffectOpacity.setOnSeekBarChangeListener(sliderListener);
        overlayEffectStrength.setOnSeekBarChangeListener(sliderListener);
        screenEffectStrength.setOnSeekBarChangeListener(sliderListener);
        crtStrength.setOnSeekBarChangeListener(sliderListener);
        crtOpacity.setOnSeekBarChangeListener(sliderListener);
        crtPixelation.setOnSeekBarChangeListener(sliderListener);
        crtSoftness.setOnSeekBarChangeListener(sliderListener);
        crtScanlines.setOnSeekBarChangeListener(sliderListener);
        crtChromatic.setOnSeekBarChangeListener(sliderListener);
        crtFlicker.setOnSeekBarChangeListener(sliderListener);
        wallpaperLogoWidth.setOnSeekBarChangeListener(sliderListener);
        wallpaperLogoAlpha.setOnSeekBarChangeListener(sliderListener);
        livebootAlpha.setOnSeekBarChangeListener(sliderListener);
        livebootSize.setOnSeekBarChangeListener(sliderListener);
        progressShowPercent.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) { updatePreview(); }
        });
    }

    private EditText field(LinearLayout parent, String label, String defaultValue) {
        parent.addView(label(label, 14, false));
        EditText editText = new EditText(this);
        editText.setSingleLine(true);
        editText.setText(defaultValue);
        styleInput(editText);
        parent.addView(editText);
        return editText;
    }

    private EditText multilineField(LinearLayout parent, String label, String defaultValue) {
        parent.addView(label(label, 14, false));
        EditText editText = new EditText(this);
        editText.setText(defaultValue);
        editText.setSingleLine(false);
        editText.setMinLines(7);
        editText.setMaxLines(12);
        editText.setGravity(Gravity.TOP);
        styleInput(editText);
        parent.addView(editText);
        return editText;
    }

    private EditText colorField(LinearLayout parent, String label, String defaultValue) {
        EditText editText = field(parent, label, defaultValue);
        Button pick = button("Pick " + label);
        pick.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) { showColorPicker(editText); }
        });
        parent.addView(pick);
        return editText;
    }

    private Spinner spinner(LinearLayout parent, String label, String[] values, String selectedValue) {
        parent.addView(label(label, 14, false));
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(8), dp(6), dp(8), dp(6));
        box.setBackgroundColor(Color.rgb(24, 31, 41));
        Spinner spinner = new Spinner(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        setSpinner(spinner, selectedValue);
        box.addView(spinner, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        parent.addView(box);
        return spinner;
    }

    private SeekBar slider(LinearLayout parent, String label, int min, int max, int value) {
        parent.addView(label(label, 14, false));
        SeekBar seekBar = new SeekBar(this);
        seekBar.setMax(max - min);
        seekBar.setProgress(value - min);
        seekBar.setTag(new int[]{min, max});
        parent.addView(seekBar);
        return seekBar;
    }

    private int sliderValue(SeekBar seekBar) {
        int[] bounds = (int[]) seekBar.getTag();
        return bounds[0] + seekBar.getProgress();
    }

    private void setSlider(SeekBar seekBar, int value) {
        int[] bounds = (int[]) seekBar.getTag();
        seekBar.setProgress(clamp(value, bounds[0], bounds[1]) - bounds[0]);
    }

    private void updateGradientControlLabels() {
        if (gradientSpeedValue != null && gradientSpeedPercent != null) {
            int speed = sliderValue(gradientSpeedPercent);
            float seconds = GradientBackgroundRenderer.animationDurationMs(speed) / 1000f;
            gradientSpeedValue.setText(speed + "%  (" + String.format(java.util.Locale.US, "%.1f", seconds) + "s cycle)");
        }
        if (gradientAngleValue != null && gradientAngle != null) {
            gradientAngleValue.setText(sliderValue(gradientAngle) + " degrees");
        }
        if (gradientScaleValue != null && gradientScale != null) {
            gradientScaleValue.setText(sliderValue(gradientScale) + "%");
        }
        if (gradientCenterValue != null && gradientCenterX != null && gradientCenterY != null) {
            gradientCenterValue.setText("Center: " + sliderValue(gradientCenterX)
                    + "%, " + sliderValue(gradientCenterY) + "%");
        }
        if (gradientStopsValue != null && gradientStop2 != null && gradientStop3 != null) {
            int second = sliderValue(gradientStop2);
            int third = Math.max(second + 3, sliderValue(gradientStop3));
            gradientStopsValue.setText("Color stops: " + second + "%, " + third + "%");
        }
        if (gradientGradingValue != null
                && gradientBrightness != null
                && gradientExposure != null
                && gradientSaturation != null
                && gradientContrast != null
                && gradientGamma != null
                && gradientVibrance != null
                && gradientHueShift != null) {
            gradientGradingValue.setText("Brightness: " + sliderValue(gradientBrightness)
                    + "  Exposure: " + sliderValue(gradientExposure)
                    + "  Saturation: " + sliderValue(gradientSaturation)
                    + "%  Contrast: " + sliderValue(gradientContrast)
                    + "%  Gamma: " + sliderValue(gradientGamma)
                    + "%  Vibrance: " + sliderValue(gradientVibrance)
                    + "%  Hue: " + sliderValue(gradientHueShift));
        }
        if (gradientExposureValue != null && gradientExposure != null) {
            gradientExposureValue.setText("Exposure: " + sliderValue(gradientExposure));
        }
        if (gradientColorPopValue != null && gradientRedBoost != null && gradientGreenBoost != null && gradientBlueBoost != null) {
            gradientColorPopValue.setText("Red: " + sliderValue(gradientRedBoost)
                    + "  Green: " + sliderValue(gradientGreenBoost)
                    + "  Blue: " + sliderValue(gradientBlueBoost));
        }
        if (gradientMotionValue != null && gradientMotionX != null && gradientMotionY != null) {
            gradientMotionValue.setText("Motion: X " + sliderValue(gradientMotionX)
                    + "%  Y " + sliderValue(gradientMotionY) + "%");
        }
        if (screenEffectValue != null && screenEffectStrength != null) {
            screenEffectValue.setText("Screen haze: " + sliderValue(screenEffectStrength) + "%");
        }
    }

    private void updateEffectsControlLabels() {
        if (foregroundEffectValue != null && foregroundEffectStrength != null) {
            foregroundEffectValue.setText("Logo effect strength: " + sliderValue(foregroundEffectStrength) + "%");
        }
        if (foregroundEffectOpacityValue != null && foregroundEffectOpacity != null) {
            foregroundEffectOpacityValue.setText("Logo effect opacity: " + sliderValue(foregroundEffectOpacity));
        }
        if (livebootEffectValue != null && livebootEffectStrength != null) {
            livebootEffectValue.setText("LiveBoot effect strength: " + sliderValue(livebootEffectStrength) + "%");
        }
        if (livebootEffectOpacityValue != null && livebootEffectOpacity != null) {
            livebootEffectOpacityValue.setText("LiveBoot effect opacity: " + sliderValue(livebootEffectOpacity));
        }
        if (backgroundEffectValue != null && backgroundEffectStrength != null) {
            backgroundEffectValue.setText("Background effect strength: " + sliderValue(backgroundEffectStrength) + "%");
        }
        if (backgroundEffectOpacityValue != null && backgroundEffectOpacity != null) {
            backgroundEffectOpacityValue.setText("Background effect opacity: " + sliderValue(backgroundEffectOpacity));
        }
        if (overlayEffectValue != null && overlayEffectStrength != null) {
            overlayEffectValue.setText("Whole-screen effect strength: " + sliderValue(overlayEffectStrength) + "%");
        }
        if (crtSummaryValue != null
                && crtMode != null
                && crtStrength != null
                && crtOpacity != null
                && crtPixelation != null
                && crtSoftness != null
                && crtScanlines != null
                && crtChromatic != null
                && crtFlicker != null) {
            crtSummaryValue.setText("CRT: mode=" + selected(crtMode)
                    + " strength=" + sliderValue(crtStrength)
                    + " opacity=" + sliderValue(crtOpacity)
                    + " pixelation=" + sliderValue(crtPixelation)
                    + " softness=" + sliderValue(crtSoftness)
                    + " scanlines=" + sliderValue(crtScanlines)
                    + " chromatic=" + sliderValue(crtChromatic)
                    + " flicker=" + sliderValue(crtFlicker));
        }
        if (wallpaperLogoValue != null && wallpaperLogoWidth != null) {
            wallpaperLogoValue.setText("Wallpaper logo width: " + sliderValue(wallpaperLogoWidth) + "%");
        }
        if (wallpaperLogoAlphaValue != null && wallpaperLogoAlpha != null) {
            wallpaperLogoAlphaValue.setText("Wallpaper logo alpha: " + sliderValue(wallpaperLogoAlpha));
        }
    }

    private void updateLogoControlLabels() {
        if (logoBoxWidthValue != null && logoBoxWidth != null) {
            logoBoxWidthValue.setText("Logo box width: " + sliderValue(logoBoxWidth) + "%");
        }
        if (logoBoxHeightValue != null && logoBoxHeight != null) {
            logoBoxHeightValue.setText("Logo box height: " + sliderValue(logoBoxHeight) + "%");
        }
        if (logoBoxBorderValue != null && logoBoxBorderWidth != null) {
            logoBoxBorderValue.setText("Logo box border width: " + sliderValue(logoBoxBorderWidth) + " px");
        }
        if (logoTextDepthValue != null && logoTextDepth != null) {
            logoTextDepthValue.setText("Logo text depth: " + sliderValue(logoTextDepth) + " px");
        }
    }

    private void updateProgressControlLabels() {
        if (progressWidthValue != null && progressWidth != null) {
            progressWidthValue.setText("Bar width: " + sliderValue(progressWidth) + "%");
        }
        if (progressHeightValue != null && progressHeight != null) {
            progressHeightValue.setText("Bar height: " + sliderValue(progressHeight) + " px");
        }
        if (progressBorderWidthValue != null && progressBorderWidth != null) {
            progressBorderWidthValue.setText("Border width: " + sliderValue(progressBorderWidth) + " px");
        }
        if (progressPixelationValue != null && progressPixelation != null) {
            progressPixelationValue.setText("Pixelation: " + sliderValue(progressPixelation) + "%");
        }
    }

    private LinearLayout tabContainer() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setVisibility(View.GONE);
        return layout;
    }

    private LinearLayout row() {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        return row;
    }

    private Button tabButton(String text) {
        Button button = button(text);
        button.setTextSize(12);
        button.setSingleLine(false);
        button.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        return button;
    }

    private Button button(String text) {
        Button button = new Button(this);
        button.setText(text);
        return button;
    }

    private TextView sectionTitle(String text) {
        return label(text, 18, true);
    }

    private LinearLayout collapsibleGroup(LinearLayout parent, String title, boolean expanded) {
        Button header = button(title);
        header.setAllCaps(false);
        header.setTextColor(Color.WHITE);
        header.setBackgroundColor(Color.rgb(24, 31, 41));
        LinearLayout body = new LinearLayout(this);
        body.setOrientation(LinearLayout.VERTICAL);
        body.setVisibility(expanded ? View.VISIBLE : View.GONE);
        header.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                body.setVisibility(body.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });
        parent.addView(header);
        parent.addView(body);
        return body;
    }

    private TextView label(String text, int sp, boolean bold) {
        TextView view = new TextView(this);
        view.setText(text);
        view.setTextSize(sp);
        view.setTextColor(Color.WHITE);
        view.setPadding(0, dp(10), 0, dp(6));
        view.setGravity(Gravity.START);
        view.setTypeface(null, bold ? Typeface.BOLD : Typeface.NORMAL);
        return view;
    }

    private CheckBox checkbox(String text, boolean checked) {
        CheckBox box = new CheckBox(this);
        box.setText(text);
        box.setTextColor(Color.WHITE);
        box.setChecked(checked);
        return box;
    }

    private void styleInput(EditText editText) {
        editText.setTextColor(Color.WHITE);
        editText.setHintTextColor(Color.rgb(150, 160, 170));
        editText.setBackgroundColor(Color.rgb(27, 34, 44));
        editText.setPadding(dp(12), dp(8), dp(12), dp(8));
    }

    private void showColorPicker(EditText target) {
        Dialog dialog = new Dialog(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dp(24), dp(24), dp(24), dp(24));
        layout.setBackgroundColor(Color.rgb(18, 22, 30));
        int initial = OverlayConfig.parseColor(target.getText().toString(), Color.WHITE);
        float[] hsv = new float[3];
        Color.colorToHSV(initial, hsv);
        layout.addView(label("Touch the color field to set saturation and lightness", 13, false));
        ColorPlaneView plane = new ColorPlaneView(this);
        plane.setColor(initial);
        layout.addView(plane, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(240)
        ));
        layout.addView(label("Hue", 12, false));
        SeekBar hue = new SeekBar(this);
        hue.setMax(360);
        hue.setProgress(Math.round(hsv[0]));
        layout.addView(hue);
        layout.addView(label("Alpha", 12, false));
        SeekBar alpha = new SeekBar(this);
        alpha.setMax(255);
        alpha.setProgress(Color.alpha(initial));
        layout.addView(alpha);
        TextView swatch = label(OverlayConfig.toColorString(initial), 18, true);
        swatch.setBackgroundColor(initial);
        layout.addView(swatch);
        ColorPlaneView.Listener update = new ColorPlaneView.Listener() {
            @Override public void onColorChanged(int color) {
                String value = OverlayConfig.toColorString(color);
                target.setText(value);
                swatch.setText(value);
                swatch.setBackgroundColor(color);
            }
        };
        plane.setListener(update);
        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                plane.setHue(hue.getProgress());
                plane.setAlphaValue(alpha.getProgress());
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        };
        hue.setOnSeekBarChangeListener(listener);
        alpha.setOnSeekBarChangeListener(listener);
        Button done = button("Done");
        done.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) { dialog.dismiss(); updatePreview(); }
        });
        layout.addView(done);
        dialog.setContentView(layout);
        dialog.show();
    }

    private void openLiveWallpaperPicker() {
        persist();
        try {
            Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
            intent.putExtra(
                    WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                    new ComponentName(this, GradientWallpaperService.class)
            );
            startActivity(intent);
            setStatus("Opening Android live wallpaper preview.");
        } catch (Exception exception) {
            try {
                startActivity(new Intent(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER));
                setStatus("Opening Android live wallpaper chooser.");
            } catch (Exception fallback) {
                setStatus("Live wallpaper picker unavailable: " + fallback.getMessage());
            }
        }
    }

    private String selected(Spinner spinner) {
        Object item = spinner.getSelectedItem();
        return item == null ? "" : item.toString();
    }

    private void setSpinner(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            Object item = spinner.getItemAtPosition(i);
            if (item != null && item.toString().equalsIgnoreCase(value)) {
                spinner.setSelection(i);
                return;
            }
        }
    }

    private String fontModeForConfig() {
        String value = selected(logoFontMode);
        if ("Pixel Retro".equals(value)) {
            return "pixel";
        }
        if ("Imported Font".equals(value)) {
            return "imported";
        }
        return value.toLowerCase();
    }

    private String labelForFontMode(String mode) {
        if ("pixel".equalsIgnoreCase(mode)) {
            return "Pixel Retro";
        }
        if ("imported".equalsIgnoreCase(mode)) {
            return "Imported Font";
        }
        if ("serif".equalsIgnoreCase(mode)) {
            return "Serif";
        }
        if ("monospace".equalsIgnoreCase(mode)) {
            return "Monospace";
        }
        return "Sans";
    }

    private long durationMs() {
        String value = selected(progressDuration);
        if ("15s".equals(value)) {
            return 15000L;
        }
        if ("20s".equals(value)) {
            return 20000L;
        }
        if ("30s".equals(value)) {
            return 30000L;
        }
        return 25000L;
    }

    private String durationLabel(long ms) {
        if (ms <= 15000L) {
            return "15s";
        }
        if (ms <= 20000L) {
            return "20s";
        }
        if (ms >= 30000L) {
            return "30s";
        }
        return "25s";
    }

    private String defaultLivebootLines() {
        return "[ OK ] loading Dave OS\n[ OK ] mounting modules\n[ OK ] SurfaceControl ready\n[ OK ] gradient renderer active\n[ OK ] logo.png loaded\n[....] synchronizing services\n[ OK ] boot sequence active";
    }

    private String defaultFinalLines() {
        return "[ OK ] finalizing Dave OS\n[ OK ] preparing launcher\n[ OK ] boot sequence complete";
    }

    private long finalLeadMs() {
        String value = selected(livebootFinalLead);
        if ("3s".equals(value)) {
            return 3000L;
        }
        if ("8s".equals(value)) {
            return 8000L;
        }
        return 5000L;
    }

    private String finalLeadLabel(long ms) {
        if (ms <= 3000L) {
            return "3s";
        }
        if (ms >= 8000L) {
            return "8s";
        }
        return "5s";
    }

    private File appLogoFile() {
        return new File(getFilesDir(), APP_LOGO_FILE);
    }

    private String cleanModuleId() {
        return moduleId == null ? "brobro_boot_overlay" : moduleId.getText().toString().trim();
    }

    private String shellQuote(String value) {
        return "'" + value.replace("'", "'\\''") + "'";
    }

    private String runSuCapture(String command) throws Exception {
        Process process = new ProcessBuilder("su", "-c", command).redirectErrorStream(true).start();
        String output = readProcessOutput(process);
        int code = process.waitFor();
        if (code != 0) {
            throw new Exception("su exit=" + code + " output=" + limit(output));
        }
        return output;
    }

    private byte[] runSuBytes(String command) throws Exception {
        Process process = new ProcessBuilder("su", "-c", command).redirectErrorStream(false).start();
        java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
        InputStream input = process.getInputStream();
        byte[] buffer = new byte[8192];
        int read;
        while ((read = input.read(buffer)) >= 0) {
            output.write(buffer, 0, read);
        }
        int code = process.waitFor();
        if (code != 0) {
            return new byte[0];
        }
        return output.toByteArray();
    }

    private boolean runSuToFile(String command, File destination) throws Exception {
        Process process = new ProcessBuilder("su", "-c", command).redirectErrorStream(false).start();
        boolean wrote = false;
        try (InputStream input = process.getInputStream();
             FileOutputStream output = new FileOutputStream(destination, false)) {
            byte[] buffer = new byte[65536];
            int read;
            while ((read = input.read(buffer)) >= 0) {
                output.write(buffer, 0, read);
                wrote = true;
            }
        }
        int code = process.waitFor();
        if (code != 0 || !wrote) {
            destination.delete();
            return false;
        }
        return true;
    }

    private String readProcessOutput(Process process) throws Exception {
        InputStream input = process.getInputStream();
        java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int read;
        while ((read = input.read(buffer)) >= 0) {
            output.write(buffer, 0, read);
        }
        return new String(output.toByteArray(), StandardCharsets.UTF_8);
    }

    private String limit(String value) {
        if (value == null) {
            return "";
        }
        String trimmed = value.trim();
        return trimmed.length() > 300 ? trimmed.substring(0, 300) : trimmed;
    }

    private String limitLong(String value, int max) {
        if (value == null) {
            return "";
        }
        String trimmed = value.trim();
        return trimmed.length() > max ? trimmed.substring(0, max) : trimmed;
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private void setStatus(String value) {
        if (status != null) {
            status.setText(value);
        }
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private int topInsetPx() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return getResources().getDimensionPixelSize(resourceId);
        }
        return dp(24);
    }
}
