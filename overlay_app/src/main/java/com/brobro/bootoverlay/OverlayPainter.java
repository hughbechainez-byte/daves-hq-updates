package com.brobro.bootoverlay;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.Log;

public class OverlayPainter {
    private static final String TAG = "BroBroBootOverlay";
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean nativeImageDisabled = false;
    private boolean imageDecodeAttempted = false;
    private boolean imageDrawLogged = false;
    private boolean gradientLogged = false;
    private boolean livebootLogged = false;
    private boolean blurDisabledLogged = false;
    private boolean foregroundEffectLogged = false;
    private boolean livebootEffectLogged = false;
    private boolean backgroundEffectLogged = false;
    private boolean overlayEffectLogged = false;
    private boolean screenEffectLogged = false;
    private boolean crtEffectLogged = false;
    private boolean logcatAttempted = false;
    private boolean logcatFallbackLogged = false;
    private boolean finalLinesLogged = false;
    private Bitmap logoBitmap;
    private String loadedImagePath = "";
    private String[] cachedLogcatLines;

    public void resetImageCache() {
        logoBitmap = null;
        loadedImagePath = "";
        imageDecodeAttempted = false;
        imageDrawLogged = false;
        nativeImageDisabled = false;
    }

    public void release() {
    }

    public void draw(Canvas canvas, OverlayConfig config, int width, int height, long elapsed, int frame) {
        draw(canvas, config, width, height, elapsed, frame, -1f, false);
    }

    public void draw(Canvas canvas, OverlayConfig config, int width, int height, long elapsed, int frame, float progressOverride, boolean finalFrame) {
        OverlayConfig safeConfig = config == null ? OverlayConfig.defaults() : config;
        int safeWidth = Math.max(1, width);
        int safeHeight = Math.max(1, height);

        drawBackground(canvas, safeConfig, safeWidth, safeHeight, elapsed);
        drawBackgroundEffect(canvas, safeConfig, safeWidth, safeHeight, elapsed);

        if (safeConfig.livebootTextEnabled) {
            drawLiveBootText(canvas, safeConfig, safeWidth, safeHeight, elapsed, progressOverride);
        }

        if (safeConfig.progressBar) {
            drawProgressBar(canvas, safeConfig, safeWidth, safeHeight, elapsed, progressOverride);
        }

        drawScreenEffect(canvas, safeConfig, safeWidth, safeHeight, elapsed);

        if (safeConfig.nativeImageEnabled && safeConfig.logoEnabled && !nativeImageDisabled) {
            drawImageSafely(canvas, safeConfig, safeWidth, safeHeight, elapsed, frame);
        } else if (nativeImageDisabled) {
            drawGeometryFallback(canvas, safeConfig, safeWidth, safeHeight, elapsed);
        }

        drawOverlayEffects(canvas, safeConfig, safeWidth, safeHeight, elapsed);
        drawCrtOverlay(canvas, safeConfig, safeWidth, safeHeight, elapsed);
    }

    public void drawEarlyFrame(Canvas canvas, OverlayConfig config, int width, int height) {
        OverlayConfig safeConfig = config == null ? OverlayConfig.defaults() : config;
        canvas.drawColor(opaque(safeConfig.preSurfaceSolidColor), PorterDuff.Mode.SRC);
    }

    private void drawBackground(Canvas canvas, OverlayConfig config, int width, int height, long elapsed) {
        try {
            GradientBackgroundRenderer.draw(canvas, paint, config, width, height, elapsed);
            if (!gradientLogged) {
                log("gradient enabled background_mode=" + config.backgroundMode
                        + " colors=" + OverlayConfig.toColorString(config.gradientColor1)
                        + "," + OverlayConfig.toColorString(config.gradientColor2)
                        + "," + OverlayConfig.toColorString(config.gradientColor3)
                        + " pattern=" + config.gradientPattern
                        + " animation=" + config.gradientAnimation
                        + " speed_pct=" + config.gradientSpeedPercent
                        + " angle_deg=" + config.gradientAngleDeg
                        + " scale_pct=" + config.gradientScalePercent
                        + " center=" + config.gradientCenterXPct + "," + config.gradientCenterYPct
                        + " stops=" + config.gradientColor2PositionPct + "," + config.gradientColor3PositionPct
                        + " brightness=" + config.gradientBrightness
                        + " saturation_pct=" + config.gradientSaturationPct
                        + " contrast_pct=" + config.gradientContrastPct
                        + " reverse=" + config.gradientReverse);
                gradientLogged = true;
            }
        } catch (Throwable throwable) {
            canvas.drawColor(opaque(config.gradientColor1), PorterDuff.Mode.SRC);
            log("gradient failure exception=" + throwable.getClass().getName() + " message=" + throwable.getMessage());
        }
    }

    private void drawImageSafely(Canvas canvas, OverlayConfig config, int width, int height, long elapsed, int frame) {
        try {
            Bitmap bitmap = logoBitmap(config);
            if (bitmap == null) {
                return;
            }

            float targetWidth = width * (clamp(config.imageWidthPct, 5, 100) / 100f);
            float scale = targetWidth / Math.max(1f, bitmap.getWidth());
            float targetHeight = bitmap.getHeight() * scale;
            float centerX = width * 0.5f;
            float centerY = logoCenterY(config, height);
            RectF dest = new RectF(
                    centerX - targetWidth * 0.5f,
                    centerY - targetHeight * 0.5f,
                    centerX + targetWidth * 0.5f,
                    centerY + targetHeight * 0.5f
            );

            paint.reset();
            paint.setAntiAlias(true);
            paint.setFilterBitmap(true);
            paint.setAlpha(clamp(config.imageAlpha, 0, 255));
            if (foregroundEffectEnabled(config)) {
                drawLogoEffect(canvas, bitmap, dest, config, elapsed, config.foregroundEffectMode, config.foregroundEffectStrength, config.foregroundEffectOpacity);
            } else {
                canvas.drawBitmap(bitmap, null, dest, paint);
            }
            if (!imageDrawLogged) {
                log("image draw success frame=" + frame + " dest=" + (int) dest.left + "," + (int) dest.top + "," + (int) dest.right + "," + (int) dest.bottom);
                imageDrawLogged = true;
            }
        } catch (Throwable throwable) {
            nativeImageDisabled = true;
            log("image draw failure exception=" + throwable.getClass().getName() + " message=" + throwable.getMessage());
        }
    }

    private void drawLiveBootText(Canvas canvas, OverlayConfig config, int width, int height, long elapsed, float progressOverride) {
        int alpha = clamp(config.livebootTextAlpha, 0, 255);
        int color = Color.argb(alpha, Color.red(config.livebootTextColor), Color.green(config.livebootTextColor), Color.blue(config.livebootTextColor));
        int scale = Math.max(2, config.livebootTextSizePx / 7);
        int lineHeight = scale * 9;
        if ("dense".equalsIgnoreCase(config.livebootTextDensity)) {
            lineHeight = scale * 7;
        } else if ("sparse".equalsIgnoreCase(config.livebootTextDensity)) {
            lineHeight = scale * 12;
        }
        float pxPerSecond;
        if ("fast".equalsIgnoreCase(config.livebootTextSpeed)) {
            pxPerSecond = 92f;
        } else if ("slow".equalsIgnoreCase(config.livebootTextSpeed)) {
            pxPerSecond = 34f;
        } else {
            pxPerSecond = 58f;
        }
        int safeTopInset = safeTopInsetPx(config, height);
        boolean fullScreen = "full_screen".equalsIgnoreCase(config.livebootTextDisplayMode);
        int left = fullScreen ? 0 : width / 18;
        int top = fullScreen ? safeTopInset : Math.max((int) (height * 0.52f), safeTopInset);
        int right = fullScreen ? width : width - width / 18;
        int bottom = fullScreen ? height : (int) (height * 0.88f);
        int padding = clamp(config.livebootTextPaddingPx, 0, Math.min(width, height) / 4);
        int textLeft = left + padding;
        int textRight = right - padding;
        int textTop = top + padding;
        int textBottom = bottom - padding;
        int maxChars = Math.max(8, (textRight - textLeft) / Math.max(1, scale * 6));
        String[] wrappedBase = wrapLines(livebootLines(config), maxChars);
        boolean finalPhase = finalLinesActive(config, elapsed, progressOverride);
        String[] finalLines = finalPhase ? configuredLines(config.livebootFinalLines) : new String[0];
        String[] wrappedFinal = finalLines.length == 0 ? new String[0] : wrapLines(finalLines, maxChars);
        paint.setColor(color);
        int save = canvas.save();
        canvas.clipRect(left, top, right, bottom);
        if (!blurDisabledLogged) {
            log("liveboot blur disabled for stability");
            blurDisabledLogged = true;
        }
        if (!livebootLogged) {
            log("liveboot_mode_selected=" + config.livebootTextMode
                    + " liveboot_text_display_mode=" + config.livebootTextDisplayMode
                    + " liveboot_text_bounds=" + left + "," + top + "," + right + "," + bottom
                    + " safe_top_inset_px=" + safeTopInset
                    + " liveboot_line_count=" + wrappedBase.length
                    + " repeat_lines=0"
                    + " reveal_mode=" + config.livebootTextRevealMode
                    + " reveal_speed=" + config.livebootTextRevealSpeed);
            livebootLogged = true;
        }
        if (finalPhase && wrappedFinal.length > 0) {
            drawFinalLinesPanel(
                    canvas,
                    config,
                    width,
                    height,
                    elapsed,
                    progressOverride,
                    textLeft,
                    textRight,
                    textTop,
                    textBottom,
                    lineHeight,
                    scale,
                    color,
                    wrappedFinal
            );
        } else {
            String[] wrapped = wrappedBase;
            float maxOffset = Math.max(0f, (wrapped.length - 1) * lineHeight);
            float offset = Math.min(elapsed / 1000f * pxPerSecond, maxOffset);
            int startY = (int) (textBottom - offset);
            for (int i = 0; i < wrapped.length; i++) {
                int y = startY + i * lineHeight;
                if (y < textTop - lineHeight || y > textBottom + lineHeight) {
                    continue;
                }
                long lineEntryMs = (long) ((i * lineHeight / Math.max(1f, pxPerSecond)) * 1000f);
                long visibleAgeMs = Math.max(0L, elapsed - lineEntryMs);
                String text = revealText(
                        wrapped[i],
                        visibleAgeMs,
                        config.livebootTextRevealMode,
                        config.livebootTextRevealSpeed
                );
                if (text.length() > 0) {
                    drawLivebootText(canvas, text, textLeft, y, scale, color, config, elapsed, i);
                }
            }
        }
        canvas.restoreToCount(save);
    }

    private void drawFinalLinesPanel(Canvas canvas, OverlayConfig config, int width, int height, long elapsed,
                                     float progressOverride, int textLeft, int textRight, int textTop, int textBottom,
                                     int lineHeight, int scale, int color, String[] wrappedFinal) {
        if (!finalLinesLogged) {
            log("liveboot final lines active count=" + wrappedFinal.length
                    + " lead_ms=" + config.livebootFinalLinesLeadMs);
            finalLinesLogged = true;
        }
        int panelPadding = Math.max(12, config.livebootTextPaddingPx);
        int panelLeft = panelPadding;
        int panelRight = width - panelPadding;
        int panelTop = Math.max(textTop, (int) (height * 0.70f));
        int panelBottom = Math.min(height - panelPadding, textBottom);
        if (panelBottom <= panelTop + lineHeight) {
            panelTop = Math.max(textTop, height / 2);
            panelBottom = Math.min(height - panelPadding, height - panelPadding);
        }
        int bgColor = Color.argb(190, 0, 0, 0);
        paint.reset();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(bgColor);
        RectF background = new RectF(panelLeft, panelTop, panelRight, panelBottom);
        canvas.drawRoundRect(background, 22f, 22f, paint);

        int maxChars = Math.max(8, (panelRight - panelLeft - panelPadding * 2) / Math.max(1, scale * 6));
        String[] wrapped = wrapLines(wrappedFinal, maxChars);
        long finalPhaseStartMs = Math.max(0L, config.progressDurationMs - config.livebootFinalLinesLeadMs);
        long finalPhaseElapsedMs = Math.max(0L, elapsed - finalPhaseStartMs);
        int startY = panelTop + panelPadding;
        int maxLines = Math.max(1, ((panelBottom - panelTop) - panelPadding * 2) / lineHeight);
        int startIndex = Math.max(0, wrapped.length - maxLines);
        for (int i = startIndex; i < wrapped.length; i++) {
            long staggerMs = (i - startIndex) * 140L;
            long visibleAgeMs = Math.max(0L, finalPhaseElapsedMs - staggerMs);
            String text = revealText(
                    wrapped[i],
                    visibleAgeMs,
                    config.livebootTextRevealMode,
                    config.livebootTextRevealSpeed
            );
            if (text.length() > 0) {
                drawLivebootText(canvas, text, panelLeft + panelPadding, startY + (i - startIndex) * lineHeight, scale, color, config, elapsed, i);
            }
        }
    }

    private void drawLivebootText(Canvas canvas, String text, int x, int y, int scale, int color,
                                  OverlayConfig config, long elapsed, int index) {
        String effectMode = normalized(config.livebootEffectMode, "none");
        if ("none".equals(effectMode)) {
            effectMode = normalized(config.foregroundEffectMode, "none");
        }
        if (!"none".equals(effectMode)) {
            drawForegroundTextEffect(canvas, text, x, y, scale, color, config, elapsed, index, effectMode, config.livebootEffectStrength, config.livebootEffectOpacity);
        } else {
            drawPixelText(canvas, text, x, y, scale, color);
        }
    }

    private void drawForegroundTextEffect(Canvas canvas, String text, int x, int y, int scale, int color,
                                          OverlayConfig config, long elapsed, int index, String mode, int strengthSetting, int opacitySetting) {
        if (!foregroundEffectLogged) {
            log("foreground effect mode=" + mode
                    + " strength=" + clamp(strengthSetting, 0, 100)
                    + " opacity=" + clamp(opacitySetting, 0, 255));
            foregroundEffectLogged = true;
        }
        int strength = clamp(strengthSetting, 0, 100);
        int baseAlpha = clamp(opacitySetting, 0, 255);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int modePhase = (int) ((elapsed / 33L) + index) & 3;
        int xJitter = "crt_strong".equalsIgnoreCase(mode) ? (modePhase == 0 ? -2 : modePhase == 2 ? 2 : 0)
                : (modePhase == 0 ? -1 : (modePhase == 2 ? 1 : 0));
        int yJitter = "crt_strong".equalsIgnoreCase(mode) ? (int) (((elapsed / 90L) + index) & 3) - 1 : (int) (((elapsed / 120L) + index) & 1);
        int haloAlpha = clamp(70 + strength, 40, 200);
        int haloScale = Math.max(1, "crt_strong".equalsIgnoreCase(mode) ? scale / 4 : scale / 6);

        paint.setColor(Color.argb(haloAlpha, 0, 0, 0));
        drawPixelText(canvas, text, x - haloScale, y + haloScale, scale, paint.getColor());
        drawPixelText(canvas, text, x + haloScale, y - haloScale, scale, paint.getColor());

        int cyan = Color.argb(clamp((baseAlpha * 2) / 3, 0, 255), 0, 240, 255);
        int magenta = Color.argb(clamp((baseAlpha * 2) / 3, 0, 255), 255, 60, 185);
        drawPixelText(canvas, text, x + xJitter, y + yJitter, scale, cyan);
        drawPixelText(canvas, text, x - xJitter, y - yJitter, scale, magenta);

        int coreAlpha = clamp(baseAlpha, 0, 255);
        int coreColor = Color.argb(coreAlpha, red, green, blue);
        drawPixelText(canvas, text, x, y, scale, coreColor);

        if ("crt_strong".equalsIgnoreCase(mode) || "vhs_glow".equalsIgnoreCase(mode)) {
            int scanAlpha = clamp(10 + strength / 3, 10, 52);
            paint.setColor(Color.argb(scanAlpha, 255, 255, 255));
            int step = Math.max(5, scale * 2);
            for (int yy = y - scale; yy < y + scale * 8; yy += step) {
                canvas.drawRect(x - scale, yy, x + (text.length() * scale * 6), yy + 1f, paint);
            }
        }
    }

    private void drawLogoEffect(Canvas canvas, Bitmap bitmap, RectF dest, OverlayConfig config, long elapsed, String mode, int strengthSetting, int opacitySetting) {
        int strength = clamp(strengthSetting, 0, 100);
        float phase = (elapsed % 2200L) / 2200f;
        float shift = Math.max(0.8f, (strength / 45f));
        if ("crt_strong".equalsIgnoreCase(mode)) {
            shift *= 1.6f;
        } else if ("vhs_glow".equalsIgnoreCase(mode)) {
            shift *= 1.2f;
        }
        int alpha = clamp(opacitySetting, 0, 255);
        paint.reset();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setAlpha(clamp((int) (alpha * 0.45f), 0, 255));
        canvas.drawBitmap(bitmap, null, offsetRect(dest, -shift, shift), paint);
        canvas.drawBitmap(bitmap, null, offsetRect(dest, shift, -shift), paint);

        int redTint = Color.argb(clamp((int) (alpha * 0.30f), 0, 255), 255, 80, 180);
        int cyanTint = Color.argb(clamp((int) (alpha * 0.28f), 0, 255), 0, 230, 255);
        paint.setColorFilter(null);
        paint.setAlpha(Color.alpha(redTint));
        canvas.drawBitmap(bitmap, null, offsetRect(dest, -shift * 0.6f, 0f), paint);
        paint.setAlpha(Color.alpha(cyanTint));
        canvas.drawBitmap(bitmap, null, offsetRect(dest, shift * 0.6f, 0f), paint);

        paint.setAlpha(alpha);
        canvas.drawBitmap(bitmap, null, dest, paint);

        if (strength > 0) {
            paint.reset();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            int scanAlpha = clamp(10 + strength / 2, 10, "crt_strong".equalsIgnoreCase(mode) ? 72 : 48);
            paint.setColor(Color.argb(scanAlpha, 255, 255, 255));
            float lineStep = Math.max(5f, dest.height() / ("crt_strong".equalsIgnoreCase(mode) ? 18f : 24f));
            float offset = (phase * lineStep);
            for (float y = dest.top + offset; y < dest.bottom; y += lineStep) {
                canvas.drawRect(dest.left, y, dest.right, y + 1f, paint);
            }
        }
    }

    private RectF offsetRect(RectF rect, float dx, float dy) {
        return new RectF(rect.left + dx, rect.top + dy, rect.right + dx, rect.bottom + dy);
    }

    private void drawOverlayEffects(Canvas canvas, OverlayConfig config, int width, int height, long elapsed) {
        String mode = normalized(config.overlayEffectMode, "none");
        int strength = clamp(config.overlayEffectStrength, 0, 100);
        if ("none".equals(mode) || strength <= 0) {
            return;
        }
        if (!overlayEffectLogged) {
            log("overlay effect mode=" + mode + " strength=" + strength);
            overlayEffectLogged = true;
        }
        paint.reset();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        if ("scanlines".equals(mode)) {
            int alpha = clamp(10 + strength / 3, 10, 45);
            paint.setColor(Color.argb(alpha, 0, 0, 0));
            int step = Math.max(5, 14 - strength / 10);
            for (int y = 0; y < height; y += step) {
                canvas.drawRect(0f, y, width, y + 1f, paint);
            }
        } else if ("flicker".equals(mode)) {
            int alpha = clamp((int) (6 + (Math.sin(elapsed / 120f) + 1f) * (strength / 8f)), 0, 32);
            canvas.drawColor(Color.argb(alpha, 0, 0, 0), android.graphics.PorterDuff.Mode.SRC_OVER);
        } else if ("vignette".equals(mode)) {
            int edgeAlpha = clamp(16 + strength / 2, 12, 72);
            paint.setColor(Color.argb(edgeAlpha, 0, 0, 0));
            int edge = Math.max(20, Math.min(width, height) / 10);
            canvas.drawRect(0, 0, width, edge, paint);
            canvas.drawRect(0, height - edge, width, height, paint);
            canvas.drawRect(0, 0, edge, height, paint);
            canvas.drawRect(width - edge, 0, width, height, paint);
        } else if ("crt_strong".equals(mode)) {
            int alpha = clamp(10 + strength / 3, 10, 45);
            paint.setColor(Color.argb(alpha, 255, 255, 255));
            int step = Math.max(4, 10 - strength / 18);
            for (int y = 0; y < height; y += step) {
                canvas.drawRect(0f, y, width, y + 1f, paint);
            }
            int flicker = clamp((int) (8 + (Math.sin(elapsed / 80f) + 1f) * (strength / 9f)), 0, 30);
            canvas.drawColor(Color.argb(flicker, 0, 0, 0), PorterDuff.Mode.SRC_OVER);
        } else if ("hue_warp".equals(mode)) {
            int alpha = clamp(8 + strength / 5, 8, 28);
            paint.setColor(Color.argb(alpha, 0, 255, 180));
            canvas.drawRect(0, 0, width, height, paint);
        }
    }

    private void drawCrtOverlay(Canvas canvas, OverlayConfig config, int width, int height, long elapsed) {
        String mode = normalized(config.crtMode, "soft");
        if ("off".equals(mode)) {
            return;
        }
        int strength = clamp(config.crtStrength, 0, 100);
        int opacity = clamp(config.crtOpacity, 0, 255);
        int pixelation = clamp(config.crtPixelation, 0, 100);
        int softness = clamp(config.crtSoftness, 0, 100);
        int scanlines = clamp(config.crtScanlines, 0, 100);
        int chromatic = clamp(config.crtChromatic, 0, 100);
        int flicker = clamp(config.crtFlicker, 0, 100);
        if (!crtEffectLogged) {
            log("crt mode=" + mode
                    + " strength=" + strength
                    + " opacity=" + opacity
                    + " pixelation=" + pixelation
                    + " softness=" + softness
                    + " scanlines=" + scanlines
                    + " chromatic=" + chromatic
                    + " flicker=" + flicker);
            crtEffectLogged = true;
        }

        int scanStep = Math.max(3, 16 - (strength / 9) - (scanlines / 10));
        int scanAlpha = clamp((int) (opacity * (0.10f + (scanlines / 200f))), 0, 120);
        int flickerAlpha = clamp((int) ((Math.sin(elapsed / 90f) + 1f) * (flicker / 2.2f)), 0, 36);
        int chromaShift = Math.max(1, chromatic / 24);
        int bandStep = Math.max(2, 8 - pixelation / 18);
        int softnessAlpha = clamp((int) (opacity * (softness / 220f)), 0, 80);

        paint.reset();
        paint.setAntiAlias(false);
        paint.setStyle(Paint.Style.FILL);

        if (softnessAlpha > 0) {
            paint.setColor(Color.argb(softnessAlpha, 0, 0, 0));
            canvas.drawRect(0, 0, width, height, paint);
        }

        if (chromatic > 0) {
            paint.setColor(Color.argb(clamp(chromatic * 2, 0, 90), 255, 64, 148));
            canvas.drawRect(-chromaShift, 0, width * 0.5f, height, paint);
            paint.setColor(Color.argb(clamp(chromatic * 2, 0, 90), 0, 220, 255));
            canvas.drawRect(width * 0.5f, 0, width + chromaShift, height, paint);
        }

        paint.setColor(Color.argb(scanAlpha, 0, 0, 0));
        for (int y = 0; y < height; y += scanStep) {
            canvas.drawRect(0f, y, width, y + 1f, paint);
        }

        if (bandStep > 2) {
            paint.setColor(Color.argb(clamp(opacity / 7, 0, 24), 255, 255, 255));
            for (int x = 0; x < width; x += bandStep * 6) {
                canvas.drawRect(x, 0, x + 1f, height, paint);
            }
        }

        if (flickerAlpha > 0) {
            canvas.drawColor(Color.argb(flickerAlpha, 0, 0, 0), PorterDuff.Mode.SRC_OVER);
        }
    }

    private void drawBackgroundEffect(Canvas canvas, OverlayConfig config, int width, int height, long elapsed) {
        String mode = normalized(config.backgroundEffectMode, "none");
        int strength = clamp(config.backgroundEffectStrength, 0, 100);
        if ("none".equals(mode) || strength <= 0) {
            return;
        }
        if (!backgroundEffectLogged) {
            log("background effect mode=" + mode
                    + " strength=" + strength
                    + " opacity=" + clamp(config.backgroundEffectOpacity, 0, 255));
            backgroundEffectLogged = true;
        }
        paint.reset();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        if ("crt_strong".equals(mode)) {
            int alpha = clamp(config.backgroundEffectOpacity, 0, 255);
            int bandAlpha = clamp(8 + strength / 6, 8, 36);
            paint.setColor(Color.argb(bandAlpha, 255, 255, 255));
            int step = Math.max(4, 14 - strength / 10);
            for (int y = 0; y < height; y += step) {
                canvas.drawRect(0f, y, width, y + 1f, paint);
            }
            canvas.drawColor(Color.argb(alpha, 0, 0, 0), PorterDuff.Mode.SRC_OVER);
        } else if ("hue_warp".equals(mode)) {
            int alpha = clamp(config.backgroundEffectOpacity, 0, 255);
            float phase = (elapsed % 4000L) / 4000f;
            int tint = Color.HSVToColor(alpha, new float[]{(phase * 360f) % 360f, 0.65f, 0.8f});
            canvas.drawColor(tint, PorterDuff.Mode.SRC_OVER);
        } else if ("scanlines".equals(mode)) {
            int alpha = clamp(config.backgroundEffectOpacity, 0, 255);
            paint.setColor(Color.argb(alpha, 0, 0, 0));
            int step = Math.max(5, 18 - strength / 8);
            for (int y = 0; y < height; y += step) {
                canvas.drawRect(0f, y, width, y + 1f, paint);
            }
        }
    }

    private void drawScreenEffect(Canvas canvas, OverlayConfig config, int width, int height, long elapsed) {
        int strength = clamp(config.screenEffectStrength, 0, 100);
        if (strength <= 0) {
            return;
        }
        if (!screenEffectLogged) {
            log("screen effect mode=screen_haze strength=" + strength);
            screenEffectLogged = true;
        }
        paint.reset();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        int veilAlpha = clamp(6 + (int) (strength * 0.65f), 0, 56);
        canvas.drawColor(Color.argb(veilAlpha, 232, 240, 246), PorterDuff.Mode.SRC_OVER);

        int lineAlpha = clamp(4 + strength / 5, 4, 26);
        paint.setColor(Color.argb(lineAlpha, 255, 255, 255));
        int step = Math.max(7, 18 - strength / 10);
        int offset = (int) ((elapsed / 40L) % step);
        for (int y = -offset; y < height; y += step) {
            canvas.drawRect(0f, y, width, y + 1f, paint);
        }
        paint.setColor(Color.argb(clamp(lineAlpha / 2, 2, 12), 0, 0, 0));
        for (int x = step / 2; x < width; x += step * 4) {
            canvas.drawRect(x, 0f, x + 1f, height, paint);
        }
    }

    private boolean foregroundEffectEnabled(OverlayConfig config) {
        String mode = normalized(config.foregroundEffectMode, "none");
        return !"none".equals(mode);
    }

    private boolean finalLinesActive(OverlayConfig config, long elapsed, float progressOverride) {
        if (!config.livebootFinalLinesEnabled
                || config.livebootFinalLines == null
                || config.livebootFinalLines.trim().length() == 0) {
            return false;
        }
        long startMs = Math.max(0L, config.progressDurationMs - config.livebootFinalLinesLeadMs);
        if (elapsed >= startMs) {
            return true;
        }
        float threshold = startMs / Math.max(1f, (float) config.progressDurationMs);
        return progressOverride >= threshold;
    }

    private String revealText(String text, long visibleAgeMs, String mode, String speed) {
        if (text == null || text.length() == 0 || "instant".equalsIgnoreCase(mode)) {
            return text == null ? "" : text;
        }
        long intervalMs;
        if ("char".equalsIgnoreCase(mode)) {
            intervalMs = "slow".equalsIgnoreCase(speed) ? 16L : ("medium".equalsIgnoreCase(speed) ? 9L : 4L);
            int visibleChars = (int) Math.min(text.length(), (visibleAgeMs / intervalMs) + 1L);
            return text.substring(0, visibleChars);
        }

        intervalMs = "slow".equalsIgnoreCase(speed) ? 120L : ("medium".equalsIgnoreCase(speed) ? 70L : 35L);
        int visibleWords = (int) ((visibleAgeMs / intervalMs) + 1L);
        int wordsSeen = 0;
        boolean inWord = false;
        for (int i = 0; i < text.length(); i++) {
            boolean whitespace = Character.isWhitespace(text.charAt(i));
            if (!whitespace && !inWord) {
                inWord = true;
                wordsSeen++;
            } else if (whitespace) {
                inWord = false;
            }
            if (wordsSeen > visibleWords) {
                return text.substring(0, i);
            }
        }
        return text;
    }

    private String[] livebootLines(OverlayConfig config) {
        if ("logcat_experimental".equalsIgnoreCase(config.livebootTextMode)) {
            String[] logcat = tryLogcatLines();
            if (logcat != null && logcat.length > 0) {
                return logcat;
            }
        }
        String[] configured = configuredLines(config.livebootTextLines);
        return configured.length == 0 ? new String[]{
                "[ OK ] loading Dave OS",
                "[ OK ] mounting modules",
                "[ OK ] SurfaceControl ready",
                "[ OK ] gradient renderer active",
                "[ OK ] logo.png loaded",
                "[....] synchronizing services",
                "[ OK ] boot sequence active"
        } : configured;
    }

    private String[] configuredLines(String rawValue) {
        String raw = rawValue == null ? "" : rawValue.trim();
        if (raw.length() == 0) {
            return new String[0];
        }
        String[] split = raw.split("\\r?\\n");
        int count = 0;
        for (String line : split) {
            if (line.trim().length() > 0) {
                count++;
            }
        }
        if (count == 0) {
            return new String[0];
        }
        String[] lines = new String[count];
        int index = 0;
        for (String line : split) {
            String trimmed = line.trim();
            if (trimmed.length() > 0) {
                lines[index++] = trimmed;
            }
        }
        return lines;
    }

    private String[] appendLines(String[] first, String[] second) {
        if (second.length == 0) {
            return first;
        }
        String[] combined = new String[first.length + second.length];
        System.arraycopy(first, 0, combined, 0, first.length);
        System.arraycopy(second, 0, combined, first.length, second.length);
        return combined;
    }

    private String[] tryLogcatLines() {
        if (logcatAttempted) {
            return cachedLogcatLines;
        }
        logcatAttempted = true;
        log("logcat mode requested");
        try {
            Process process = new ProcessBuilder("logcat", "-d", "-v", "brief", "-t", "400").redirectErrorStream(true).start();
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()));
            java.util.ArrayList<String> lines = new java.util.ArrayList<>();
            java.util.LinkedHashSet<String> uniqueLines = new java.util.LinkedHashSet<>();
            String line;
            while ((line = reader.readLine()) != null && lines.size() < 200) {
                String trimmed = sanitizeLogcatLine(line);
                if (trimmed.length() > 0 && uniqueLines.add(trimmed)) {
                    lines.add(trimmed);
                }
            }
            process.waitFor();
            if (!lines.isEmpty()) {
                log("logcat mode start success lines=" + lines.size());
                cachedLogcatLines = lines.toArray(new String[0]);
                return cachedLogcatLines;
            }
            logcatFallback("empty");
        } catch (Throwable throwable) {
            logcatFallback(throwable.getClass().getSimpleName() + ":" + throwable.getMessage());
        }
        return null;
    }

    private String sanitizeLogcatLine(String line) {
        if (line == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < line.length() && builder.length() < 160; i++) {
            char c = line.charAt(i);
            if (c >= 32 && c < 127) {
                builder.append(c);
            }
        }
        return builder.toString().trim();
    }

    private void logcatFallback(String reason) {
        if (!logcatFallbackLogged) {
            log("logcat mode fallback reason=" + reason);
            logcatFallbackLogged = true;
        }
    }

    private String[] wrapLines(String[] lines, int maxChars) {
        java.util.ArrayList<String> wrapped = new java.util.ArrayList<>();
        for (String line : lines) {
            String value = line == null ? "" : line.trim();
            if (value.length() == 0) {
                continue;
            }
            while (value.length() > maxChars) {
                wrapped.add(value.substring(0, maxChars));
                value = value.substring(maxChars);
            }
            wrapped.add(value);
        }
        if (wrapped.isEmpty()) {
            wrapped.add("[ OK ] boot sequence active");
        }
        return wrapped.toArray(new String[0]);
    }

    private void drawPixelText(Canvas canvas, String text, int x, int y, int scale, int color) {
        int cursor = x;
        paint.setColor(color);
        for (int i = 0; i < text.length(); i++) {
            String[] glyph = glyph(text.charAt(i));
            for (int gy = 0; gy < glyph.length; gy++) {
                String row = glyph[gy];
                for (int gx = 0; gx < row.length(); gx++) {
                    if (row.charAt(gx) == '1') {
                        canvas.drawRect(cursor + gx * scale, y + gy * scale, cursor + (gx + 1) * scale, y + (gy + 1) * scale, paint);
                    }
                }
            }
            cursor += (glyph[0].length() + 1) * scale;
        }
    }

    private String[] glyph(char raw) {
        char c = Character.toUpperCase(raw);
        switch (c) {
            case 'A': return new String[]{"01110","10001","10001","11111","10001","10001","10001"};
            case 'B': return new String[]{"11110","10001","10001","11110","10001","10001","11110"};
            case 'C': return new String[]{"01111","10000","10000","10000","10000","10000","01111"};
            case 'D': return new String[]{"11110","10001","10001","10001","10001","10001","11110"};
            case 'E': return new String[]{"11111","10000","10000","11110","10000","10000","11111"};
            case 'F': return new String[]{"11111","10000","10000","11110","10000","10000","10000"};
            case 'G': return new String[]{"01111","10000","10000","10111","10001","10001","01111"};
            case 'H': return new String[]{"10001","10001","10001","11111","10001","10001","10001"};
            case 'I': return new String[]{"111","010","010","010","010","010","111"};
            case 'J': return new String[]{"00111","00010","00010","00010","10010","10010","01100"};
            case 'K': return new String[]{"10001","10010","10100","11000","10100","10010","10001"};
            case 'L': return new String[]{"10000","10000","10000","10000","10000","10000","11111"};
            case 'M': return new String[]{"10001","11011","10101","10101","10001","10001","10001"};
            case 'N': return new String[]{"10001","11001","10101","10011","10001","10001","10001"};
            case 'O': return new String[]{"01110","10001","10001","10001","10001","10001","01110"};
            case 'P': return new String[]{"11110","10001","10001","11110","10000","10000","10000"};
            case 'Q': return new String[]{"01110","10001","10001","10001","10101","10010","01101"};
            case 'R': return new String[]{"11110","10001","10001","11110","10100","10010","10001"};
            case 'S': return new String[]{"01111","10000","10000","01110","00001","00001","11110"};
            case 'T': return new String[]{"11111","00100","00100","00100","00100","00100","00100"};
            case 'U': return new String[]{"10001","10001","10001","10001","10001","10001","01110"};
            case 'V': return new String[]{"10001","10001","10001","10001","01010","01010","00100"};
            case 'W': return new String[]{"10001","10001","10001","10101","10101","11011","10001"};
            case 'X': return new String[]{"10001","01010","00100","00100","00100","01010","10001"};
            case 'Y': return new String[]{"10001","10001","01010","00100","00100","00100","00100"};
            case 'Z': return new String[]{"11111","00010","00100","00100","01000","10000","11111"};
            case '0': return new String[]{"01110","10001","10011","10101","11001","10001","01110"};
            case '1': return new String[]{"00100","01100","00100","00100","00100","00100","01110"};
            case '2': return new String[]{"01110","10001","00001","00010","00100","01000","11111"};
            case '3': return new String[]{"11110","00001","00001","01110","00001","00001","11110"};
            case '4': return new String[]{"00010","00110","01010","10010","11111","00010","00010"};
            case '5': return new String[]{"11111","10000","10000","11110","00001","00001","11110"};
            case '6': return new String[]{"01110","10000","10000","11110","10001","10001","01110"};
            case '7': return new String[]{"11111","00001","00010","00100","01000","01000","01000"};
            case '8': return new String[]{"01110","10001","10001","01110","10001","10001","01110"};
            case '9': return new String[]{"01110","10001","10001","01111","00001","00001","01110"};
            case ':': return new String[]{"0","1","0","0","1","0","0"};
            case '.': return new String[]{"0","0","0","0","0","0","1"};
            case ',': return new String[]{"0","0","0","0","0","1","1"};
            case '/': return new String[]{"00001","00010","00100","01000","10000","00000","00000"};
            case '\\': return new String[]{"10000","01000","00100","00010","00001","00000","00000"};
            case '_': return new String[]{"00000","00000","00000","00000","00000","00000","11111"};
            case '+': return new String[]{"000","010","010","111","010","010","000"};
            case '=': return new String[]{"000","111","000","111","000","000","000"};
            case '[': return new String[]{"111","100","100","100","100","100","111"};
            case ']': return new String[]{"111","001","001","001","001","001","111"};
            case '(': return new String[]{"01","10","10","10","10","10","01"};
            case ')': return new String[]{"10","01","01","01","01","01","10"};
            case '{': return new String[]{"011","010","010","110","010","010","011"};
            case '}': return new String[]{"110","010","010","011","010","010","110"};
            case '<': return new String[]{"001","010","100","100","100","010","001"};
            case '>': return new String[]{"100","010","001","001","001","010","100"};
            case '|': return new String[]{"1","1","1","1","1","1","1"};
            case '!': return new String[]{"1","1","1","1","1","0","1"};
            case ';': return new String[]{"0","1","0","0","1","1","0"};
            case '*': return new String[]{"00000","10101","01110","11111","01110","10101","00000"};
            case '#': return new String[]{"01010","11111","01010","01010","11111","01010","00000"};
            case '-': return new String[]{"000","000","000","111","000","000","000"};
            case ' ': return new String[]{"000","000","000","000","000","000","000"};
            default: return new String[]{"11111","00001","00010","00100","01000","00000","01000"};
        }
    }

    private float logoCenterY(OverlayConfig config, int height) {
        float safeTopInset = safeTopInsetPx(config, height);
        if ("upper".equalsIgnoreCase(config.imagePosition)) {
            return Math.max(height * 0.38f, safeTopInset + height * 0.10f);
        }
        if ("lower".equalsIgnoreCase(config.imagePosition)) {
            return Math.max(height * 0.60f, safeTopInset + height * 0.30f);
        }
        return Math.max(height * 0.52f, safeTopInset + height * 0.20f);
    }

    private Bitmap logoBitmap(OverlayConfig config) {
        String path = config.imagePath == null ? "" : config.imagePath.trim();
        if (logoBitmap != null && loadedImagePath.equals(path)) {
            return logoBitmap;
        }
        if (imageDecodeAttempted && loadedImagePath.equals(path)) {
            return logoBitmap;
        }
        imageDecodeAttempted = true;
        loadedImagePath = path;
        try {
            logoBitmap = BitmapFactory.decodeFile(path);
            if (logoBitmap == null) {
                nativeImageDisabled = true;
                log("image decode failure path=" + path + " result=null");
            } else {
                log("image decode success path=" + path + " width=" + logoBitmap.getWidth() + " height=" + logoBitmap.getHeight());
            }
        } catch (Throwable throwable) {
            nativeImageDisabled = true;
            log("image decode failure path=" + path + " exception=" + throwable.getClass().getName() + " message=" + throwable.getMessage());
            logoBitmap = null;
        }
        return logoBitmap;
    }

    private void drawGeometryFallback(Canvas canvas, OverlayConfig config, int width, int height, long elapsed) {
        float phase = (elapsed % 2400L) / 2400f;
        float base = Math.min(width, height);
        float boxWidth = base * 0.48f;
        float boxHeight = base * 0.18f;
        float left = (width - boxWidth) * 0.5f;
        float top = height * 0.41f;
        RectF box = new RectF(left, top, left + boxWidth, top + boxHeight);
        paint.reset();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(withAlpha(config.gradientColor1, 210));
        canvas.drawRoundRect(box, boxHeight * 0.18f, boxHeight * 0.18f, paint);

        paint.setColor(withAlpha(config.gradientColor2, 225));
        float accentWidth = boxWidth * (0.22f + 0.05f * (float) Math.sin(phase * Math.PI * 2.0));
        canvas.drawRect(box.left + boxWidth * 0.08f, box.top + boxHeight * 0.18f,
                box.left + boxWidth * 0.08f + accentWidth, box.bottom - boxHeight * 0.18f, paint);

        paint.setColor(withAlpha(config.gradientColor3, 220));
        canvas.drawRect(box.left + boxWidth * 0.34f, box.top + boxHeight * 0.32f,
                box.right - boxWidth * 0.10f, box.top + boxHeight * 0.50f, paint);

        paint.setColor(Color.BLACK);
        paint.setAlpha(90);
        canvas.drawRoundRect(box, boxHeight * 0.18f, boxHeight * 0.18f, paint);
    }

    private void drawProgressBar(Canvas canvas, OverlayConfig config, int width, int height, long elapsed, float progressOverride) {
        float progress;
        if (progressOverride >= 0f) {
            progress = progressOverride;
        } else {
            progress = Math.min(1f, elapsed / Math.max(1f, (float) config.progressDurationMs));
        }

        float barWidth = width * (clamp(config.progressWidthPct, 5, 100) / 100f);
        float left = (width - barWidth) * 0.5f;
        float top = height * (clamp(config.progressPositionPct, 1, 99) / 100f);
        float barHeight = Math.max(2f, config.progressHeightPx);
        RectF track = new RectF(left, top, left + barWidth, top + barHeight);
        RectF fill = new RectF(left, top, left + barWidth * progress, top + barHeight);
        float radius = config.progressRounded ? barHeight * 0.5f : 0f;
        int borderWidth = clamp(config.progressBorderWidthPx, 0, 24);
        int pixelation = clamp(config.progressPixelation, 0, 100);

        paint.reset();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(withAlpha(Color.BLACK, 92));
        canvas.drawRoundRect(track, radius, radius, paint);
        if (borderWidth > 0) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(borderWidth);
            paint.setColor(config.progressBorderColor);
            canvas.drawRoundRect(track, radius, radius, paint);
            paint.setStyle(Paint.Style.FILL);
        }

        if (pixelation <= 0) {
            paint.setColor(config.progressColor);
            canvas.drawRoundRect(fill, radius, radius, paint);
        } else {
            int segments = clamp(4 + pixelation / 6, 4, 80);
            float segmentWidth = barWidth / segments;
            float gap = Math.max(1f, segmentWidth * 0.18f);
            float fillLimit = track.left + barWidth * progress;
            for (int i = 0; i < segments; i++) {
                float segLeft = track.left + i * segmentWidth + gap * 0.5f;
                float segRight = Math.min(track.left + (i + 1) * segmentWidth - gap * 0.5f, fillLimit);
                if (segRight <= segLeft) {
                    continue;
                }
                canvas.drawRect(segLeft, track.top + gap * 0.35f, segRight, track.bottom - gap * 0.35f, paint);
            }
        }

        if (config.progressShowPercent) {
            String text = Math.round(progress * 100f) + "%";
            paint.reset();
            paint.setAntiAlias(true);
            paint.setColor(Color.WHITE);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(Math.max(12f, barHeight * 0.82f));
            Paint.FontMetrics fm = paint.getFontMetrics();
            float cx = track.centerX();
            float cy = track.centerY() - (fm.ascent + fm.descent) * 0.5f;
            canvas.drawText(text, cx, cy, paint);
        }
    }

    private int withAlpha(int color, int alpha) {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }

    private int opaque(int color) {
        return Color.rgb(Color.red(color), Color.green(color), Color.blue(color));
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private int safeTopInsetPx(OverlayConfig config, int height) {
        int pct = clamp(config.safeTopInsetPct, 0, 30);
        return Math.max(0, (height * pct) / 100);
    }

    private String normalized(String value, String fallback) {
        if (value == null) {
            return fallback;
        }
        String trimmed = value.trim();
        return trimmed.length() == 0 ? fallback : trimmed.toLowerCase();
    }

    private void log(String message) {
        Log.i(TAG, message);
        System.out.println(TAG + ": " + message);
    }
}
