package com.brobro.bootoverlay;

import android.graphics.Color;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

public class OverlayConfig {
    public boolean transparent = false;
    public boolean progressBar = true;
    public boolean frameCounter = true;
    public String text1 = "BROBRO BOOT";
    public String text2 = "MAGISK OVERLAY";
    public int textColor = Color.rgb(0, 229, 255);
    public int accentColor = Color.rgb(255, 45, 85);
    public int backgroundColor = Color.rgb(0, 59, 70);
    public int fallbackWidth = 1080;
    public int fallbackHeight = 2400;
    public long durationMs = 60000L;
    public long minVisibleMs = 12000L;
    public long followBootanimMs = 7000L;
    public boolean extendOnRetrigger = true;
    public long bootRetrySeconds = 90L;
    public long bootRetryIntervalMs = 750L;
    public boolean nativeTextEnabled = false;
    public boolean nativeImageEnabled = true;
    public boolean logoEnabled = true;
    public boolean logoGeneratorEnabled = true;
    public String logoText = "Dave OS";
    public String logoFontMode = "pixel";
    public String logoFontName = "Pixel Retro";
    public String logoFontPath = "";
    public int logoFontSizePct = 100;
    public int logoLetterSpacing = 0;
    public int logoFillColor = Color.WHITE;
    public int logoBackgroundColor = Color.argb(120, 0, 59, 70);
    public int logoBoxWidthPct = 92;
    public int logoBoxHeightPct = 74;
    public boolean logoBoxBorderEnabled = true;
    public int logoBoxBorderColor = Color.WHITE;
    public int logoBoxBorderWidthPx = 4;
    public int logoTextDepthPx = 4;
    public boolean logoStrokeEnabled = false;
    public int logoStrokeColor = Color.BLACK;
    public boolean logoShadowEnabled = false;
    public int logoShadowColor = Color.BLACK;
    public boolean logoAntialias = false;
    public String imagePath = "/data/adb/modules/brobro_boot_overlay/logo.png";
    public String imagePosition = "center";
    public int imageWidthPct = 55;
    public int imageAlpha = 255;
    public String foregroundEffectMode = "none";
    public int foregroundEffectStrength = 18;
    public int foregroundEffectOpacity = 220;
    public String livebootEffectMode = "none";
    public int livebootEffectStrength = 18;
    public int livebootEffectOpacity = 120;
    public String backgroundEffectMode = "none";
    public int backgroundEffectStrength = 16;
    public int backgroundEffectOpacity = 18;
    public String overlayEffectMode = "none";
    public int overlayEffectStrength = 12;
    public int screenEffectStrength = 0;
    public String crtMode = "soft";
    public int crtStrength = 36;
    public int crtOpacity = 128;
    public int crtPixelation = 16;
    public int crtSoftness = 18;
    public int crtScanlines = 24;
    public int crtChromatic = 14;
    public int crtFlicker = 8;
    public String backgroundMode = "animated_gradient";
    public boolean gradientEnabled = true;
    public int gradientColor1 = Color.rgb(0, 59, 70);
    public int gradientColor2 = Color.rgb(255, 212, 71);
    public int gradientColor3 = Color.rgb(36, 192, 111);
    public String gradientSpeed = "fast";
    public String gradientPattern = "linear";
    public String gradientAnimation = "drift";
    public int gradientSpeedPercent = 185;
    public int gradientAngleDeg = 45;
    public int gradientScalePercent = 100;
    public int gradientCenterXPct = 50;
    public int gradientCenterYPct = 50;
    public int gradientColor2PositionPct = 36;
    public int gradientColor3PositionPct = 70;
    public int gradientBrightness = 0;
    public int gradientExposurePct = 0;
    public int gradientSaturationPct = 100;
    public int gradientContrastPct = 100;
    public int gradientGammaPct = 100;
    public int gradientVibrancePct = 100;
    public int gradientHueShiftDeg = 0;
    public int gradientYellowBoostPct = 0;
    public int gradientRedBoostPct = 0;
    public int gradientGreenBoostPct = 0;
    public int gradientBlueBoostPct = 0;
    public int gradientMotionXPct = 18;
    public int gradientMotionYPct = 12;
    public boolean gradientReverse = false;
    public boolean bootVideoEnabled = false;
    public String bootVideoPath = "/data/adb/modules/brobro_boot_overlay/boot_video.mp4";
    public String bootVideoFramesDir = "/data/adb/modules/brobro_boot_overlay/boot_video_frames";
    public String bootVideoManifestPath = "/data/adb/modules/brobro_boot_overlay/boot_video_frames/manifest.txt";
    public String bootVideoPosition = "bottom_right";
    public int bootVideoWidthPct = 28;
    public int bootVideoMarginPct = 4;
    public int bootVideoAlpha = 255;
    public boolean bootVideoLoop = true;
    public int bootVideoFps = 12;
    public int progressColor = Color.rgb(255, 212, 71);
    public int progressBorderColor = Color.argb(180, 255, 255, 255);
    public int progressBorderWidthPx = 2;
    public int progressPixelation = 0;
    public boolean progressShowPercent = false;
    public int safeTopInsetPct = 9;
    public boolean wallpaperLogoEnabled = true;
    public String wallpaperLogoPosition = "center";
    public int wallpaperLogoWidthPct = 45;
    public int wallpaperLogoAlpha = 255;
    public int preSurfaceSolidColor = Color.rgb(0, 59, 70);
    public boolean earlyFullscreen = true;
    public long startupDelayMs = 0L;
    public boolean livebootTextEnabled = true;
    public String livebootTextMode = "logcat_experimental";
    public String livebootTextDisplayMode = "full_screen";
    public int livebootTextColor = Color.argb(153, 183, 255, 247);
    public int livebootTextAlpha = 120;
    public int livebootTextBlur = 0;
    public int livebootTextPaddingPx = 24;
    public String livebootTextRevealMode = "word";
    public String livebootTextRevealSpeed = "fast";
    public String livebootTextSpeed = "medium";
    public int livebootTextSizePx = 22;
    public String livebootTextDensity = "normal";
    public String livebootTextPosition = "full";
    public boolean livebootTextBehindLogo = true;
    public String livebootTextLines = "[ OK ] loading Dave OS\n[ OK ] mounting modules\n[ OK ] SurfaceControl ready\n[ OK ] gradient renderer active\n[ OK ] logo.png loaded\n[....] synchronizing services\n[ OK ] boot sequence active";
    public boolean livebootFinalLinesEnabled = false;
    public long livebootFinalLinesLeadMs = 5000L;
    public String livebootFinalLines = "[ OK ] finalizing Dave OS\n[ OK ] preparing launcher\n[ OK ] boot sequence complete";
    public boolean debugFullscreen = false;
    public String layoutMode = "lower_third";
    public int verticalOffsetPct = 72;
    public int overlayHeightPct = 18;
    public String progressMode = "time_estimated";
    public int progressPositionPct = 82;
    public int progressWidthPct = 68;
    public int progressHeightPx = 18;
    public boolean progressRounded = true;
    public long progressDurationMs = 25000L;
    public long postBootHoldMs = 1000L;
    public boolean bootanimSyncEnabled = true;
    public long bootanimEndHoldMs = 0L;
    public long maxDurationMs = 120000L;
    public String loadedConfigPath = "";
    public boolean loadedConfigFile = false;

    public static OverlayConfig defaults() {
        return new OverlayConfig();
    }

    public static OverlayConfig fromArgs(String[] args) {
        OverlayConfig config = defaults();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("--config".equals(arg) && i + 1 < args.length) {
                File file = new File(args[++i]);
                config.loadedConfigPath = file.getAbsolutePath();
                config.loadFile(file);
            } else if ("--test".equals(arg)) {
                config.durationMs = 20000L;
                config.minVisibleMs = 15000L;
            } else {
                config.applyLine(arg);
            }
        }
        return config;
    }

    public void loadFile(File file) {
        if (file == null || !file.isFile()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            loadedConfigPath = file.getAbsolutePath();
            loadedConfigFile = true;
            String line;
            while ((line = reader.readLine()) != null) {
                applyLine(line);
            }
        } catch (IOException ignored) {
        }
    }

    public void applyLine(String rawLine) {
        if (rawLine == null) {
            return;
        }
        String line = rawLine.trim();
        if (line.length() == 0 || line.startsWith("#")) {
            return;
        }
        if ("transparent".equalsIgnoreCase(line)) {
            transparent = true;
            backgroundColor = Color.TRANSPARENT;
            return;
        }
        if ("dark".equalsIgnoreCase(line)) {
            transparent = false;
            backgroundColor = Color.rgb(3, 6, 12);
            return;
        }
        int equals = line.indexOf('=');
        if (equals < 0) {
            return;
        }

        String key = line.substring(0, equals).trim().toLowerCase(Locale.US);
        String value = line.substring(equals + 1).trim();

        try {
            if ("text1".equals(key)) {
                text1 = value;
            } else if ("text2".equals(key)) {
                text2 = value;
            } else if ("textcolor".equals(key)) {
                textColor = parseColor(value, textColor);
            } else if ("accentcolor".equals(key)) {
                accentColor = parseColor(value, accentColor);
            } else if ("background".equals(key)) {
                backgroundColor = parseColor(value, backgroundColor);
                transparent = Color.alpha(backgroundColor) == 0;
            } else if ("progressbar".equals(key)) {
                progressBar = parseBoolean(value);
            } else if ("framecounter".equals(key)) {
                frameCounter = parseBoolean(value);
            } else if ("fallbackwidth".equals(key)) {
                fallbackWidth = clampInt(value, 320, 10000, fallbackWidth);
            } else if ("fallbackheight".equals(key)) {
                fallbackHeight = clampInt(value, 320, 10000, fallbackHeight);
            } else if ("durationms".equals(key)) {
                durationMs = clampLong(value, 5000L, 300000L, durationMs);
            } else if ("minvisiblems".equals(key)) {
                minVisibleMs = clampLong(value, 1000L, 120000L, minVisibleMs);
            } else if ("followbootanimms".equals(key)) {
                followBootanimMs = clampLong(value, 0L, 120000L, followBootanimMs);
            } else if ("extend_on_retrigger".equals(key)) {
                extendOnRetrigger = parseBoolean(value);
            } else if ("boot_retry_seconds".equals(key)) {
                bootRetrySeconds = clampLong(value, 45L, 300L, bootRetrySeconds);
            } else if ("boot_retry_interval_ms".equals(key)) {
                bootRetryIntervalMs = clampLong(value, 250L, 5000L, bootRetryIntervalMs);
            } else if ("native_text_enabled".equals(key)) {
                nativeTextEnabled = parseBoolean(value);
            } else if ("native_image_enabled".equals(key)) {
                nativeImageEnabled = parseBoolean(value);
            } else if ("logo_enabled".equals(key)) {
                logoEnabled = parseBoolean(value);
            } else if ("logo_generator_enabled".equals(key)) {
                logoGeneratorEnabled = parseBoolean(value);
            } else if ("logo_text".equals(key)) {
                logoText = value.length() == 0 ? logoText : value;
            } else if ("logo_font_mode".equals(key)) {
                logoFontMode = value.length() == 0 ? logoFontMode : value;
            } else if ("logo_font_name".equals(key)) {
                logoFontName = value.length() == 0 ? logoFontName : value;
            } else if ("logo_font_path".equals(key)) {
                logoFontPath = value;
            } else if ("logo_font_size_pct".equals(key)) {
                logoFontSizePct = clampInt(value, 20, 300, logoFontSizePct);
            } else if ("logo_letter_spacing".equals(key)) {
                logoLetterSpacing = clampInt(value, -20, 80, logoLetterSpacing);
            } else if ("logo_fill_color".equals(key)) {
                logoFillColor = parseColor(value, logoFillColor);
            } else if ("logo_background_color".equals(key)) {
                logoBackgroundColor = parseColor(value, logoBackgroundColor);
            } else if ("logo_box_width_pct".equals(key)) {
                logoBoxWidthPct = clampInt(value, 40, 100, logoBoxWidthPct);
            } else if ("logo_box_height_pct".equals(key)) {
                logoBoxHeightPct = clampInt(value, 30, 100, logoBoxHeightPct);
            } else if ("logo_box_border_enabled".equals(key)) {
                logoBoxBorderEnabled = parseBoolean(value);
            } else if ("logo_box_border_color".equals(key)) {
                logoBoxBorderColor = parseColor(value, logoBoxBorderColor);
            } else if ("logo_box_border_width_px".equals(key)) {
                logoBoxBorderWidthPx = clampInt(value, 0, 24, logoBoxBorderWidthPx);
            } else if ("logo_text_depth_px".equals(key)) {
                logoTextDepthPx = clampInt(value, 0, 40, logoTextDepthPx);
            } else if ("logo_stroke_enabled".equals(key)) {
                logoStrokeEnabled = parseBoolean(value);
            } else if ("logo_stroke_color".equals(key)) {
                logoStrokeColor = parseColor(value, logoStrokeColor);
            } else if ("logo_shadow_enabled".equals(key)) {
                logoShadowEnabled = parseBoolean(value);
            } else if ("logo_shadow_color".equals(key)) {
                logoShadowColor = parseColor(value, logoShadowColor);
            } else if ("logo_antialias".equals(key)) {
                logoAntialias = parseBoolean(value);
            } else if ("image_path".equals(key)) {
                imagePath = value.length() == 0 ? imagePath : value;
            } else if ("image_position".equals(key) || "logo_position".equals(key)) {
                imagePosition = value.length() == 0 ? imagePosition : value;
            } else if ("image_width_pct".equals(key) || "logo_width_pct".equals(key)) {
                imageWidthPct = clampInt(value, 5, 100, imageWidthPct);
            } else if ("image_alpha".equals(key)) {
                imageAlpha = clampInt(value, 0, 255, imageAlpha);
            } else if ("foreground_effect_mode".equals(key)) {
                foregroundEffectMode = value.length() == 0 ? foregroundEffectMode : value;
            } else if ("foreground_effect_strength".equals(key)) {
                foregroundEffectStrength = clampInt(value, 0, 100, foregroundEffectStrength);
            } else if ("foreground_effect_opacity".equals(key)) {
                foregroundEffectOpacity = clampInt(value, 0, 255, foregroundEffectOpacity);
            } else if ("liveboot_effect_mode".equals(key)) {
                livebootEffectMode = value.length() == 0 ? livebootEffectMode : value;
            } else if ("liveboot_effect_strength".equals(key)) {
                livebootEffectStrength = clampInt(value, 0, 100, livebootEffectStrength);
            } else if ("liveboot_effect_opacity".equals(key)) {
                livebootEffectOpacity = clampInt(value, 0, 255, livebootEffectOpacity);
            } else if ("background_effect_mode".equals(key)) {
                backgroundEffectMode = value.length() == 0 ? backgroundEffectMode : value;
            } else if ("background_effect_strength".equals(key)) {
                backgroundEffectStrength = clampInt(value, 0, 100, backgroundEffectStrength);
            } else if ("background_effect_opacity".equals(key)) {
                backgroundEffectOpacity = clampInt(value, 0, 255, backgroundEffectOpacity);
            } else if ("overlay_effect_mode".equals(key)) {
                overlayEffectMode = value.length() == 0 ? overlayEffectMode : value;
            } else if ("overlay_effect_strength".equals(key)) {
                overlayEffectStrength = clampInt(value, 0, 100, overlayEffectStrength);
            } else if ("screen_effect_strength".equals(key)) {
                screenEffectStrength = clampInt(value, 0, 100, screenEffectStrength);
            } else if ("crt_mode".equals(key)) {
                crtMode = crtMode(value, crtMode);
            } else if ("crt_strength".equals(key)) {
                crtStrength = clampInt(value, 0, 100, crtStrength);
            } else if ("crt_opacity".equals(key)) {
                crtOpacity = clampInt(value, 0, 255, crtOpacity);
            } else if ("crt_pixelation".equals(key)) {
                crtPixelation = clampInt(value, 0, 100, crtPixelation);
            } else if ("crt_softness".equals(key)) {
                crtSoftness = clampInt(value, 0, 100, crtSoftness);
            } else if ("crt_scanlines".equals(key)) {
                crtScanlines = clampInt(value, 0, 100, crtScanlines);
            } else if ("crt_chromatic".equals(key)) {
                crtChromatic = clampInt(value, 0, 100, crtChromatic);
            } else if ("crt_flicker".equals(key)) {
                crtFlicker = clampInt(value, 0, 100, crtFlicker);
            } else if ("background_mode".equals(key)) {
                backgroundMode = value.length() == 0 ? backgroundMode : value;
            } else if ("gradient_enabled".equals(key)) {
                gradientEnabled = parseBoolean(value);
            } else if ("gradient_color_1".equals(key)) {
                gradientColor1 = parseColor(value, gradientColor1);
            } else if ("gradient_color_2".equals(key)) {
                gradientColor2 = parseColor(value, gradientColor2);
            } else if ("gradient_color_3".equals(key)) {
                gradientColor3 = parseColor(value, gradientColor3);
            } else if ("gradient_speed".equals(key)) {
                gradientSpeed = value.length() == 0 ? gradientSpeed : value;
                gradientSpeedPercent = GradientBackgroundRenderer.legacySpeedPercent(gradientSpeed);
            } else if ("gradient_pattern".equals(key)) {
                gradientPattern = gradientPattern(value, gradientPattern);
            } else if ("gradient_animation".equals(key)) {
                gradientAnimation = gradientAnimation(value, gradientAnimation);
            } else if ("gradient_speed_percent".equals(key)) {
                gradientSpeedPercent = clampInt(value, 20, 300, gradientSpeedPercent);
                gradientSpeed = GradientBackgroundRenderer.legacySpeedLabel(gradientSpeedPercent);
            } else if ("gradient_angle_deg".equals(key)) {
                gradientAngleDeg = clampInt(value, 0, 359, gradientAngleDeg);
            } else if ("gradient_scale_percent".equals(key)) {
                gradientScalePercent = clampInt(value, 50, 200, gradientScalePercent);
            } else if ("gradient_center_x_pct".equals(key)) {
                gradientCenterXPct = clampInt(value, 0, 100, gradientCenterXPct);
            } else if ("gradient_center_y_pct".equals(key)) {
                gradientCenterYPct = clampInt(value, 0, 100, gradientCenterYPct);
            } else if ("gradient_color_2_position_pct".equals(key)) {
                gradientColor2PositionPct = clampInt(value, 5, 90, gradientColor2PositionPct);
            } else if ("gradient_color_3_position_pct".equals(key)) {
                gradientColor3PositionPct = clampInt(value, 10, 95, gradientColor3PositionPct);
            } else if ("gradient_brightness".equals(key)) {
                gradientBrightness = clampInt(value, -60, 60, gradientBrightness);
            } else if ("gradient_exposure_pct".equals(key)) {
                gradientExposurePct = clampInt(value, -40, 40, gradientExposurePct);
            } else if ("gradient_saturation_pct".equals(key)) {
                gradientSaturationPct = clampInt(value, 0, 200, gradientSaturationPct);
            } else if ("gradient_contrast_pct".equals(key)) {
                gradientContrastPct = clampInt(value, 50, 150, gradientContrastPct);
            } else if ("gradient_gamma_pct".equals(key)) {
                gradientGammaPct = clampInt(value, 50, 200, gradientGammaPct);
            } else if ("gradient_vibrance_pct".equals(key)) {
                gradientVibrancePct = clampInt(value, 0, 200, gradientVibrancePct);
            } else if ("gradient_hue_shift_deg".equals(key)) {
                gradientHueShiftDeg = clampInt(value, -180, 180, gradientHueShiftDeg);
            } else if ("gradient_yellow_boost_pct".equals(key)) {
                gradientYellowBoostPct = clampInt(value, 0, 100, gradientYellowBoostPct);
            } else if ("gradient_red_boost_pct".equals(key)) {
                gradientRedBoostPct = clampInt(value, 0, 100, gradientRedBoostPct);
            } else if ("gradient_green_boost_pct".equals(key)) {
                gradientGreenBoostPct = clampInt(value, 0, 100, gradientGreenBoostPct);
            } else if ("gradient_blue_boost_pct".equals(key)) {
                gradientBlueBoostPct = clampInt(value, 0, 100, gradientBlueBoostPct);
            } else if ("gradient_motion_x_pct".equals(key)) {
                gradientMotionXPct = clampInt(value, 0, 100, gradientMotionXPct);
            } else if ("gradient_motion_y_pct".equals(key)) {
                gradientMotionYPct = clampInt(value, 0, 100, gradientMotionYPct);
            } else if ("gradient_reverse".equals(key)) {
                gradientReverse = parseBoolean(value);
            } else if ("boot_video_enabled".equals(key)) {
                bootVideoEnabled = parseBoolean(value);
            } else if ("boot_video_path".equals(key)) {
                bootVideoPath = value.length() == 0 ? bootVideoPath : value;
            } else if ("boot_video_frames_dir".equals(key)) {
                bootVideoFramesDir = value.length() == 0 ? bootVideoFramesDir : value;
            } else if ("boot_video_manifest_path".equals(key)) {
                bootVideoManifestPath = value.length() == 0 ? bootVideoManifestPath : value;
            } else if ("boot_video_position".equals(key)) {
                bootVideoPosition = videoPosition(value, bootVideoPosition);
            } else if ("boot_video_width_pct".equals(key)) {
                bootVideoWidthPct = clampInt(value, 10, 70, bootVideoWidthPct);
            } else if ("boot_video_margin_pct".equals(key)) {
                bootVideoMarginPct = clampInt(value, 0, 20, bootVideoMarginPct);
            } else if ("boot_video_alpha".equals(key)) {
                bootVideoAlpha = clampInt(value, 0, 255, bootVideoAlpha);
            } else if ("boot_video_loop".equals(key)) {
                bootVideoLoop = parseBoolean(value);
            } else if ("boot_video_fps".equals(key)) {
                bootVideoFps = clampInt(value, 4, 24, bootVideoFps);
            } else if ("progress_color".equals(key)) {
                progressColor = parseColor(value, progressColor);
            } else if ("progress_border_color".equals(key)) {
                progressBorderColor = parseColor(value, progressBorderColor);
            } else if ("progress_border_width_px".equals(key)) {
                progressBorderWidthPx = clampInt(value, 0, 24, progressBorderWidthPx);
            } else if ("progress_pixelation".equals(key)) {
                progressPixelation = clampInt(value, 0, 100, progressPixelation);
            } else if ("progress_show_percent".equals(key)) {
                progressShowPercent = parseBoolean(value);
            } else if ("safe_top_inset_pct".equals(key)) {
                safeTopInsetPct = clampInt(value, 0, 30, safeTopInsetPct);
            } else if ("wallpaper_logo_enabled".equals(key)) {
                wallpaperLogoEnabled = parseBoolean(value);
            } else if ("wallpaper_logo_position".equals(key)) {
                wallpaperLogoPosition = value.length() == 0 ? wallpaperLogoPosition : value;
            } else if ("wallpaper_logo_width_pct".equals(key)) {
                wallpaperLogoWidthPct = clampInt(value, 5, 100, wallpaperLogoWidthPct);
            } else if ("wallpaper_logo_alpha".equals(key)) {
                wallpaperLogoAlpha = clampInt(value, 0, 255, wallpaperLogoAlpha);
            } else if ("pre_surface_solid_color".equals(key)) {
                preSurfaceSolidColor = parseColor(value, preSurfaceSolidColor);
            } else if ("early_fullscreen".equals(key)) {
                earlyFullscreen = parseBoolean(value);
            } else if ("startup_delay_ms".equals(key)) {
                startupDelayMs = clampLong(value, 0L, 10000L, startupDelayMs);
            } else if ("liveboot_text_enabled".equals(key)) {
                livebootTextEnabled = parseBoolean(value);
            } else if ("liveboot_text_mode".equals(key)) {
                livebootTextMode = value.length() == 0 ? livebootTextMode : value;
            } else if ("liveboot_text_display_mode".equals(key)) {
                livebootTextDisplayMode = value.length() == 0 ? livebootTextDisplayMode : value;
            } else if ("liveboot_text_color".equals(key)) {
                livebootTextColor = parseColor(value, livebootTextColor);
            } else if ("liveboot_text_alpha".equals(key)) {
                livebootTextAlpha = clampInt(value, 0, 255, livebootTextAlpha);
            } else if ("liveboot_text_blur".equals(key)) {
                // Retained for old config compatibility, but intentionally ignored.
                livebootTextBlur = 0;
            } else if ("liveboot_text_padding_px".equals(key)) {
                livebootTextPaddingPx = clampInt(value, 0, 240, livebootTextPaddingPx);
            } else if ("liveboot_text_reveal_mode".equals(key)) {
                livebootTextRevealMode = revealMode(value, livebootTextRevealMode);
            } else if ("liveboot_text_reveal_speed".equals(key)) {
                livebootTextRevealSpeed = revealSpeed(value, livebootTextRevealSpeed);
            } else if ("liveboot_text_speed".equals(key)) {
                livebootTextSpeed = value.length() == 0 ? livebootTextSpeed : value;
            } else if ("liveboot_text_size_px".equals(key)) {
                livebootTextSizePx = clampInt(value, 8, 96, livebootTextSizePx);
            } else if ("liveboot_text_density".equals(key)) {
                livebootTextDensity = value.length() == 0 ? livebootTextDensity : value;
            } else if ("liveboot_text_position".equals(key)) {
                livebootTextPosition = value.length() == 0 ? livebootTextPosition : value;
            } else if ("liveboot_text_behind_logo".equals(key)) {
                livebootTextBehindLogo = parseBoolean(value);
            } else if ("liveboot_text_lines".equals(key)) {
                livebootTextLines = unescapeNewlines(value);
            } else if ("liveboot_final_lines_enabled".equals(key)) {
                livebootFinalLinesEnabled = parseBoolean(value);
            } else if ("liveboot_final_lines_lead_ms".equals(key)) {
                livebootFinalLinesLeadMs = clampLong(value, 500L, 30000L, livebootFinalLinesLeadMs);
            } else if ("liveboot_final_lines".equals(key)) {
                livebootFinalLines = unescapeNewlines(value);
            } else if ("debug_fullscreen".equals(key)) {
                debugFullscreen = parseBoolean(value);
            } else if ("layout_mode".equals(key)) {
                layoutMode = value.length() == 0 ? layoutMode : value;
            } else if ("vertical_offset_pct".equals(key)) {
                verticalOffsetPct = clampInt(value, 0, 100, verticalOffsetPct);
            } else if ("overlay_height_pct".equals(key)) {
                overlayHeightPct = clampInt(value, 8, 100, overlayHeightPct);
            } else if ("progress_mode".equals(key)) {
                progressMode = value.length() == 0 ? progressMode : value;
            } else if ("progress_enabled".equals(key)) {
                progressBar = parseBoolean(value);
            } else if ("progress_duration_ms".equals(key)) {
                progressDurationMs = clampLong(value, 5000L, 180000L, progressDurationMs);
            } else if ("post_boot_hold_ms".equals(key)) {
                postBootHoldMs = clampLong(value, 0L, 15000L, postBootHoldMs);
            } else if ("bootanim_sync_enabled".equals(key)) {
                bootanimSyncEnabled = parseBoolean(value);
            } else if ("bootanim_end_hold_ms".equals(key)) {
                bootanimEndHoldMs = clampLong(value, 0L, 15000L, bootanimEndHoldMs);
            } else if ("max_duration_ms".equals(key)) {
                maxDurationMs = clampLong(value, 5000L, 300000L, maxDurationMs);
            } else if ("progress_position_pct".equals(key)) {
                progressPositionPct = clampInt(value, 1, 99, progressPositionPct);
            } else if ("progress_width_pct".equals(key)) {
                progressWidthPct = clampInt(value, 5, 100, progressWidthPct);
            } else if ("progress_height_px".equals(key)) {
                progressHeightPx = clampInt(value, 2, 120, progressHeightPx);
            } else if ("progress_rounded".equals(key)) {
                progressRounded = parseBoolean(value);
            }
        } catch (RuntimeException ignored) {
        }
    }

    public String toModuleConfigText() {
        StringBuilder builder = new StringBuilder();
        builder.append(transparent ? "transparent\n" : "dark\n");
        builder.append("text1=").append(clean(text1)).append('\n');
        builder.append("text2=").append(clean(text2)).append('\n');
        builder.append("textcolor=").append(toColorString(textColor)).append('\n');
        builder.append("accentcolor=").append(toColorString(accentColor)).append('\n');
        builder.append("background=").append(toColorString(backgroundColor)).append('\n');
        builder.append("progressbar=").append(progressBar ? "1" : "0").append('\n');
        builder.append("framecounter=").append(frameCounter ? "1" : "0").append('\n');
        builder.append("fallbackwidth=").append(fallbackWidth).append('\n');
        builder.append("fallbackheight=").append(fallbackHeight).append('\n');
        builder.append("durationms=").append(durationMs).append('\n');
        builder.append("minvisiblems=").append(minVisibleMs).append('\n');
        builder.append("followbootanimms=").append(followBootanimMs).append('\n');
        builder.append("extend_on_retrigger=").append(extendOnRetrigger ? "1" : "0").append('\n');
        builder.append("boot_retry_seconds=").append(bootRetrySeconds).append('\n');
        builder.append("boot_retry_interval_ms=").append(bootRetryIntervalMs).append('\n');
        builder.append("native_text_enabled=").append(nativeTextEnabled ? "1" : "0").append('\n');
        builder.append("native_image_enabled=").append(nativeImageEnabled ? "1" : "0").append('\n');
        builder.append("logo_enabled=").append(logoEnabled ? "1" : "0").append('\n');
        builder.append("logo_generator_enabled=").append(logoGeneratorEnabled ? "1" : "0").append('\n');
        builder.append("logo_text=").append(clean(logoText)).append('\n');
        builder.append("logo_font_mode=").append(clean(logoFontMode)).append('\n');
        builder.append("logo_font_name=").append(clean(logoFontName)).append('\n');
        builder.append("logo_font_path=").append(clean(logoFontPath)).append('\n');
        builder.append("logo_font_size_pct=").append(logoFontSizePct).append('\n');
        builder.append("logo_letter_spacing=").append(logoLetterSpacing).append('\n');
        builder.append("logo_fill_color=").append(toColorString(logoFillColor)).append('\n');
        builder.append("logo_background_color=").append(toColorString(logoBackgroundColor)).append('\n');
        builder.append("logo_box_width_pct=").append(logoBoxWidthPct).append('\n');
        builder.append("logo_box_height_pct=").append(logoBoxHeightPct).append('\n');
        builder.append("logo_box_border_enabled=").append(logoBoxBorderEnabled ? "1" : "0").append('\n');
        builder.append("logo_box_border_color=").append(toColorString(logoBoxBorderColor)).append('\n');
        builder.append("logo_box_border_width_px=").append(logoBoxBorderWidthPx).append('\n');
        builder.append("logo_text_depth_px=").append(logoTextDepthPx).append('\n');
        builder.append("logo_stroke_enabled=").append(logoStrokeEnabled ? "1" : "0").append('\n');
        builder.append("logo_stroke_color=").append(toColorString(logoStrokeColor)).append('\n');
        builder.append("logo_shadow_enabled=").append(logoShadowEnabled ? "1" : "0").append('\n');
        builder.append("logo_shadow_color=").append(toColorString(logoShadowColor)).append('\n');
        builder.append("logo_antialias=").append(logoAntialias ? "1" : "0").append('\n');
        builder.append("image_path=").append(clean(imagePath)).append('\n');
        builder.append("image_position=").append(clean(imagePosition)).append('\n');
        builder.append("image_width_pct=").append(imageWidthPct).append('\n');
        builder.append("logo_position=").append(clean(imagePosition)).append('\n');
        builder.append("logo_width_pct=").append(imageWidthPct).append('\n');
        builder.append("image_alpha=").append(imageAlpha).append('\n');
        builder.append("foreground_effect_mode=").append(clean(foregroundEffectMode)).append('\n');
        builder.append("foreground_effect_strength=").append(foregroundEffectStrength).append('\n');
        builder.append("foreground_effect_opacity=").append(foregroundEffectOpacity).append('\n');
        builder.append("liveboot_effect_mode=").append(clean(livebootEffectMode)).append('\n');
        builder.append("liveboot_effect_strength=").append(livebootEffectStrength).append('\n');
        builder.append("liveboot_effect_opacity=").append(livebootEffectOpacity).append('\n');
        builder.append("background_effect_mode=").append(clean(backgroundEffectMode)).append('\n');
        builder.append("background_effect_strength=").append(backgroundEffectStrength).append('\n');
        builder.append("background_effect_opacity=").append(backgroundEffectOpacity).append('\n');
        builder.append("overlay_effect_mode=").append(clean(overlayEffectMode)).append('\n');
        builder.append("overlay_effect_strength=").append(overlayEffectStrength).append('\n');
        builder.append("screen_effect_strength=").append(screenEffectStrength).append('\n');
        builder.append("crt_mode=").append(clean(crtMode)).append('\n');
        builder.append("crt_strength=").append(crtStrength).append('\n');
        builder.append("crt_opacity=").append(crtOpacity).append('\n');
        builder.append("crt_pixelation=").append(crtPixelation).append('\n');
        builder.append("crt_softness=").append(crtSoftness).append('\n');
        builder.append("crt_scanlines=").append(crtScanlines).append('\n');
        builder.append("crt_chromatic=").append(crtChromatic).append('\n');
        builder.append("crt_flicker=").append(crtFlicker).append('\n');
        builder.append("background_mode=").append(clean(backgroundMode)).append('\n');
        builder.append("gradient_enabled=").append(gradientEnabled ? "1" : "0").append('\n');
        builder.append("gradient_color_1=").append(toColorString(gradientColor1)).append('\n');
        builder.append("gradient_color_2=").append(toColorString(gradientColor2)).append('\n');
        builder.append("gradient_color_3=").append(toColorString(gradientColor3)).append('\n');
        builder.append("gradient_speed=").append(clean(gradientSpeed)).append('\n');
        builder.append("gradient_pattern=").append(clean(gradientPattern)).append('\n');
        builder.append("gradient_animation=").append(clean(gradientAnimation)).append('\n');
        builder.append("gradient_speed_percent=").append(gradientSpeedPercent).append('\n');
        builder.append("gradient_angle_deg=").append(gradientAngleDeg).append('\n');
        builder.append("gradient_scale_percent=").append(gradientScalePercent).append('\n');
        builder.append("gradient_center_x_pct=").append(gradientCenterXPct).append('\n');
        builder.append("gradient_center_y_pct=").append(gradientCenterYPct).append('\n');
        builder.append("gradient_color_2_position_pct=").append(gradientColor2PositionPct).append('\n');
        builder.append("gradient_color_3_position_pct=").append(gradientColor3PositionPct).append('\n');
        builder.append("gradient_brightness=").append(gradientBrightness).append('\n');
        builder.append("gradient_exposure_pct=").append(gradientExposurePct).append('\n');
        builder.append("gradient_saturation_pct=").append(gradientSaturationPct).append('\n');
        builder.append("gradient_contrast_pct=").append(gradientContrastPct).append('\n');
        builder.append("gradient_gamma_pct=").append(gradientGammaPct).append('\n');
        builder.append("gradient_vibrance_pct=").append(gradientVibrancePct).append('\n');
        builder.append("gradient_hue_shift_deg=").append(gradientHueShiftDeg).append('\n');
        builder.append("gradient_yellow_boost_pct=").append(gradientYellowBoostPct).append('\n');
        builder.append("gradient_red_boost_pct=").append(gradientRedBoostPct).append('\n');
        builder.append("gradient_green_boost_pct=").append(gradientGreenBoostPct).append('\n');
        builder.append("gradient_blue_boost_pct=").append(gradientBlueBoostPct).append('\n');
        builder.append("gradient_motion_x_pct=").append(gradientMotionXPct).append('\n');
        builder.append("gradient_motion_y_pct=").append(gradientMotionYPct).append('\n');
        builder.append("gradient_reverse=").append(gradientReverse ? "1" : "0").append('\n');
        builder.append("progress_color=").append(toColorString(progressColor)).append('\n');
        builder.append("progress_border_color=").append(toColorString(progressBorderColor)).append('\n');
        builder.append("progress_border_width_px=").append(progressBorderWidthPx).append('\n');
        builder.append("progress_pixelation=").append(progressPixelation).append('\n');
        builder.append("progress_show_percent=").append(progressShowPercent ? "1" : "0").append('\n');
        builder.append("safe_top_inset_pct=").append(safeTopInsetPct).append('\n');
        builder.append("wallpaper_logo_enabled=").append(wallpaperLogoEnabled ? "1" : "0").append('\n');
        builder.append("wallpaper_logo_position=").append(clean(wallpaperLogoPosition)).append('\n');
        builder.append("wallpaper_logo_width_pct=").append(wallpaperLogoWidthPct).append('\n');
        builder.append("wallpaper_logo_alpha=").append(wallpaperLogoAlpha).append('\n');
        builder.append("pre_surface_solid_color=").append(toColorString(preSurfaceSolidColor)).append('\n');
        builder.append("early_fullscreen=").append(earlyFullscreen ? "1" : "0").append('\n');
        builder.append("startup_delay_ms=").append(startupDelayMs).append('\n');
        builder.append("liveboot_text_enabled=").append(livebootTextEnabled ? "1" : "0").append('\n');
        builder.append("liveboot_text_mode=").append(clean(livebootTextMode)).append('\n');
        builder.append("liveboot_text_display_mode=").append(clean(livebootTextDisplayMode)).append('\n');
        builder.append("liveboot_text_color=").append(toColorString(livebootTextColor)).append('\n');
        builder.append("liveboot_text_alpha=").append(livebootTextAlpha).append('\n');
        builder.append("liveboot_text_blur=0\n");
        builder.append("liveboot_text_padding_px=").append(livebootTextPaddingPx).append('\n');
        builder.append("liveboot_text_reveal_mode=").append(clean(livebootTextRevealMode)).append('\n');
        builder.append("liveboot_text_reveal_speed=").append(clean(livebootTextRevealSpeed)).append('\n');
        builder.append("liveboot_text_speed=").append(clean(livebootTextSpeed)).append('\n');
        builder.append("liveboot_text_size_px=").append(livebootTextSizePx).append('\n');
        builder.append("liveboot_text_density=").append(clean(livebootTextDensity)).append('\n');
        builder.append("liveboot_text_position=").append(clean(livebootTextPosition)).append('\n');
        builder.append("liveboot_text_behind_logo=").append(livebootTextBehindLogo ? "1" : "0").append('\n');
        builder.append("liveboot_text_lines=").append(escapeNewlines(livebootTextLines)).append('\n');
        builder.append("liveboot_final_lines_enabled=").append(livebootFinalLinesEnabled ? "1" : "0").append('\n');
        builder.append("liveboot_final_lines_lead_ms=").append(livebootFinalLinesLeadMs).append('\n');
        builder.append("liveboot_final_lines=").append(escapeNewlines(livebootFinalLines)).append('\n');
        builder.append("debug_fullscreen=").append(debugFullscreen ? "1" : "0").append('\n');
        builder.append("layout_mode=").append(clean(layoutMode)).append('\n');
        builder.append("vertical_offset_pct=").append(verticalOffsetPct).append('\n');
        builder.append("overlay_height_pct=").append(overlayHeightPct).append('\n');
        builder.append("progress_enabled=").append(progressBar ? "1" : "0").append('\n');
        builder.append("progress_mode=").append(clean(progressMode)).append('\n');
        builder.append("progress_duration_ms=").append(progressDurationMs).append('\n');
        builder.append("post_boot_hold_ms=").append(postBootHoldMs).append('\n');
        builder.append("bootanim_sync_enabled=").append(bootanimSyncEnabled ? "1" : "0").append('\n');
        builder.append("bootanim_end_hold_ms=").append(bootanimEndHoldMs).append('\n');
        builder.append("max_duration_ms=").append(maxDurationMs).append('\n');
        builder.append("progress_position_pct=").append(progressPositionPct).append('\n');
        builder.append("progress_width_pct=").append(progressWidthPct).append('\n');
        builder.append("progress_height_px=").append(progressHeightPx).append('\n');
        builder.append("progress_rounded=").append(progressRounded ? "1" : "0").append('\n');
        return builder.toString();
    }

    public String summary() {
        return "native_text_enabled=" + (nativeTextEnabled ? "1" : "0")
                + " native_image_enabled=" + (nativeImageEnabled ? "1" : "0")
                + " logo_enabled=" + (logoEnabled ? "1" : "0")
                + " logo_generator_enabled=" + (logoGeneratorEnabled ? "1" : "0")
                + " logo_text=" + clean(logoText)
                + " logo_font_mode=" + clean(logoFontMode)
                + " logo_font_name=" + clean(logoFontName)
                + " logo_background_color=" + toColorString(logoBackgroundColor)
                + " logo_box_width_pct=" + logoBoxWidthPct
                + " logo_box_height_pct=" + logoBoxHeightPct
                + " logo_box_border_enabled=" + (logoBoxBorderEnabled ? "1" : "0")
                + " logo_box_border_width_px=" + logoBoxBorderWidthPx
                + " logo_text_depth_px=" + logoTextDepthPx
                + " image_path=" + clean(imagePath)
                + " image_position=" + clean(imagePosition)
                + " image_width_pct=" + imageWidthPct
                + " image_alpha=" + imageAlpha
                + " foreground_effect_mode=" + clean(foregroundEffectMode)
                + " foreground_effect_strength=" + foregroundEffectStrength
                + " foreground_effect_opacity=" + foregroundEffectOpacity
                + " liveboot_effect_mode=" + clean(livebootEffectMode)
                + " liveboot_effect_strength=" + livebootEffectStrength
                + " liveboot_effect_opacity=" + livebootEffectOpacity
                + " background_effect_mode=" + clean(backgroundEffectMode)
                + " background_effect_strength=" + backgroundEffectStrength
                + " background_effect_opacity=" + backgroundEffectOpacity
                + " overlay_effect_mode=" + clean(overlayEffectMode)
                + " overlay_effect_strength=" + overlayEffectStrength
                + " screen_effect_strength=" + screenEffectStrength
                + " crt_mode=" + clean(crtMode)
                + " crt_strength=" + crtStrength
                + " crt_opacity=" + crtOpacity
                + " crt_pixelation=" + crtPixelation
                + " crt_softness=" + crtSoftness
                + " crt_scanlines=" + crtScanlines
                + " crt_chromatic=" + crtChromatic
                + " crt_flicker=" + crtFlicker
                + " background_mode=" + clean(backgroundMode)
                + " gradient_enabled=" + (gradientEnabled ? "1" : "0")
                + " gradient_color_1=" + toColorString(gradientColor1)
                + " gradient_color_2=" + toColorString(gradientColor2)
                + " gradient_color_3=" + toColorString(gradientColor3)
                + " gradient_speed=" + clean(gradientSpeed)
                + " gradient_pattern=" + clean(gradientPattern)
                + " gradient_animation=" + clean(gradientAnimation)
                + " gradient_speed_percent=" + gradientSpeedPercent
                + " gradient_angle_deg=" + gradientAngleDeg
                + " gradient_scale_percent=" + gradientScalePercent
                + " gradient_center=" + gradientCenterXPct + "," + gradientCenterYPct
                + " gradient_stops=" + gradientColor2PositionPct + "," + gradientColor3PositionPct
                + " gradient_brightness=" + gradientBrightness
                + " gradient_exposure_pct=" + gradientExposurePct
                + " gradient_saturation_pct=" + gradientSaturationPct
                + " gradient_contrast_pct=" + gradientContrastPct
                + " gradient_gamma_pct=" + gradientGammaPct
                + " gradient_vibrance_pct=" + gradientVibrancePct
                + " gradient_hue_shift_deg=" + gradientHueShiftDeg
                + " gradient_yellow_boost_pct=" + gradientYellowBoostPct
                + " gradient_red_boost_pct=" + gradientRedBoostPct
                + " gradient_green_boost_pct=" + gradientGreenBoostPct
                + " gradient_blue_boost_pct=" + gradientBlueBoostPct
                + " gradient_motion_x_pct=" + gradientMotionXPct
                + " gradient_motion_y_pct=" + gradientMotionYPct
                + " gradient_reverse=" + (gradientReverse ? "1" : "0")
                + " progress_color=" + toColorString(progressColor)
                + " progress_border_color=" + toColorString(progressBorderColor)
                + " progress_border_width_px=" + progressBorderWidthPx
                + " progress_pixelation=" + progressPixelation
                + " progress_show_percent=" + (progressShowPercent ? "1" : "0")
                + " safe_top_inset_pct=" + safeTopInsetPct
                + " wallpaper_logo_enabled=" + (wallpaperLogoEnabled ? "1" : "0")
                + " wallpaper_logo_position=" + clean(wallpaperLogoPosition)
                + " wallpaper_logo_width_pct=" + wallpaperLogoWidthPct
                + " wallpaper_logo_alpha=" + wallpaperLogoAlpha
                + " pre_surface_solid_color=" + toColorString(preSurfaceSolidColor)
                + " early_fullscreen=" + (earlyFullscreen ? "1" : "0")
                + " startup_delay_ms=" + startupDelayMs
                + " liveboot_text_enabled=" + (livebootTextEnabled ? "1" : "0")
                + " liveboot_text_mode=" + clean(livebootTextMode)
                + " liveboot_text_display_mode=" + clean(livebootTextDisplayMode)
                + " liveboot_text_padding_px=" + livebootTextPaddingPx
                + " liveboot_text_reveal_mode=" + clean(livebootTextRevealMode)
                + " liveboot_text_reveal_speed=" + clean(livebootTextRevealSpeed)
                + " liveboot_text_speed=" + clean(livebootTextSpeed)
                + " liveboot_text_size_px=" + livebootTextSizePx
                + " liveboot_text_lines=" + summarizeLines(livebootTextLines)
                + " liveboot_final_lines_enabled=" + (livebootFinalLinesEnabled ? "1" : "0")
                + " liveboot_final_lines_lead_ms=" + livebootFinalLinesLeadMs
                + " liveboot_final_lines=" + summarizeLines(livebootFinalLines)
                + " debug_fullscreen=" + (debugFullscreen ? "1" : "0")
                + " layout_mode=" + clean(layoutMode)
                + " vertical_offset_pct=" + verticalOffsetPct
                + " overlay_height_pct=" + overlayHeightPct
                + " background=" + toColorString(backgroundColor)
                + " transparent=" + (transparent ? "1" : "0")
                + " progress_mode=" + clean(progressMode)
                + " progress_duration_ms=" + progressDurationMs
                + " post_boot_hold_ms=" + postBootHoldMs
                + " bootanim_sync_enabled=" + (bootanimSyncEnabled ? "1" : "0")
                + " bootanim_end_hold_ms=" + bootanimEndHoldMs
                + " max_duration_ms=" + maxDurationMs
                + " progress_position_pct=" + progressPositionPct
                + " progress_width_pct=" + progressWidthPct
                + " progress_height_px=" + progressHeightPx
                + " progress_rounded=" + (progressRounded ? "1" : "0")
                + " text1=" + clean(text1)
                + " text2=" + clean(text2);
    }

    public static int parseColor(String value, int fallback) {
        try {
            String v = value.trim();
            if (v.matches("#[0-9a-fA-F]{6}")) {
                return Color.parseColor(v);
            }
            if (v.matches("#[0-9a-fA-F]{8}")) {
                long parsed = Long.parseLong(v.substring(1), 16);
                return (int) parsed;
            }
        } catch (RuntimeException ignored) {
        }
        return fallback;
    }

    public static String toColorString(int color) {
        return String.format(Locale.US, "#%08X", color);
    }

    private static boolean parseBoolean(String value) {
        String normalized = value.trim().toLowerCase(Locale.US);
        return "1".equals(normalized) || "true".equals(normalized) || "yes".equals(normalized) || "on".equals(normalized);
    }

    private static int clampInt(String value, int min, int max, int fallback) {
        int parsed = Integer.parseInt(value);
        return Math.max(min, Math.min(max, parsed));
    }

    private static long clampLong(String value, long min, long max, long fallback) {
        long parsed = Long.parseLong(value);
        return Math.max(min, Math.min(max, parsed));
    }

    private static String revealMode(String value, String fallback) {
        String normalized = value.trim().toLowerCase(Locale.US);
        return "instant".equals(normalized) || "word".equals(normalized) || "char".equals(normalized)
                ? normalized
                : fallback;
    }

    private static String revealSpeed(String value, String fallback) {
        String normalized = value.trim().toLowerCase(Locale.US);
        return "slow".equals(normalized) || "medium".equals(normalized) || "fast".equals(normalized)
                ? normalized
                : fallback;
    }

    private static String crtMode(String value, String fallback) {
        String normalized = value.trim().toLowerCase(Locale.US);
        return "off".equals(normalized) || "soft".equals(normalized) || "strong".equals(normalized) || "vhs".equals(normalized)
                ? normalized
                : fallback;
    }

    private static String gradientPattern(String value, String fallback) {
        String normalized = value.trim().toLowerCase(Locale.US);
        return "linear".equals(normalized)
                || "radial".equals(normalized)
                || "sweep".equals(normalized)
                || "bands".equals(normalized)
                || "radial_bands".equals(normalized)
                || "checker".equals(normalized)
                || "diamond".equals(normalized)
                || "rings".equals(normalized)
                ? normalized
                : fallback;
    }

    private static String gradientAnimation(String value, String fallback) {
        String normalized = value.trim().toLowerCase(Locale.US);
        return "drift".equals(normalized)
                || "pulse".equals(normalized)
                || "rotate".equals(normalized)
                || "wave".equals(normalized)
                || "orbit".equals(normalized)
                || "shimmer".equals(normalized)
                || "glide".equals(normalized)
                || "ripple".equals(normalized)
                || "breathe".equals(normalized)
                || "static".equals(normalized)
                ? normalized
                : fallback;
    }

    private static String videoPosition(String value, String fallback) {
        String normalized = value.trim().toLowerCase(Locale.US);
        return "top_left".equals(normalized)
                || "top_right".equals(normalized)
                || "bottom_left".equals(normalized)
                || "bottom_right".equals(normalized)
                ? normalized
                : fallback;
    }

    private static String clean(String value) {
        if (value == null) {
            return "";
        }
        return value.replace('\n', ' ').replace('\r', ' ').trim();
    }

    private static String escapeNewlines(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\r", "").replace("\n", "\\n");
    }

    private static String unescapeNewlines(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\n", "\n").replace("\\\\", "\\");
    }

    private static String summarizeLines(String value) {
        if (value == null) {
            return "";
        }
        String trimmed = value.replace('\n', ' ').replace('\r', ' ').trim();
        return trimmed.length() > 80 ? trimmed.substring(0, 80) + "..." : trimmed;
    }
}
