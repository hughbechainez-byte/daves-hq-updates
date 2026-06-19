package com.brobro.bootoverlay;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;

public final class GradientBackgroundRenderer {
    private GradientBackgroundRenderer() {
    }

    public static void draw(Canvas canvas, Paint paint, OverlayConfig config, int width, int height, long elapsed) {
        int fallbackColor = config.backgroundColor == Color.TRANSPARENT
                ? config.gradientColor1
                : config.backgroundColor;
        canvas.drawColor(opaque(fallbackColor), PorterDuff.Mode.SRC);
        if (!config.gradientEnabled) {
            return;
        }
        boolean animated = "animated_gradient".equalsIgnoreCase(config.backgroundMode)
                || "color_cycle".equalsIgnoreCase(config.backgroundMode);
        if (!animated) {
            return;
        }

        int color1 = adjusted(config.gradientColor1, config);
        int color2 = adjusted(config.gradientColor2, config);
        int color3 = adjusted(config.gradientColor3, config);
        if ("color_cycle".equalsIgnoreCase(config.backgroundMode)) {
            float base = animationPhase(config.gradientSpeedPercent, elapsed);
            color1 = cycleHue(color1, base, 0f);
            color2 = cycleHue(color2, base, 0.18f);
            color3 = cycleHue(color3, base, 0.36f);
        }
        if (config.gradientReverse) {
            int swap = color1;
            color1 = color3;
            color3 = swap;
        }
        int[] colors = new int[]{color1, color2, color3, color1};
        float secondStop = clamp(config.gradientColor2PositionPct, 5, 90) / 100f;
        float thirdStop = clamp(config.gradientColor3PositionPct, 10, 95) / 100f;
        if (thirdStop <= secondStop + 0.03f) {
            thirdStop = Math.min(0.95f, secondStop + 0.03f);
        }
        float[] positions = new float[]{0f, secondStop, thirdStop, 1f};
        float phase = animationPhase(config.gradientSpeedPercent, elapsed);
        String animation = normalized(config.gradientAnimation, "drift");
        if ("static".equals(animation)) {
            phase = 0f;
        }

        float cycle = phase * (float) (Math.PI * 2.0);
        float baseAngle = config.gradientAngleDeg;
        float centerX = width * (clamp(config.gradientCenterXPct, 0, 100) / 100f);
        float centerY = height * (clamp(config.gradientCenterYPct, 0, 100) / 100f);
        float patternScale = clamp(config.gradientScalePercent, 50, 200) / 100f;
        float pulse = 1f;

        if ("drift".equals(animation)) {
            centerX += width * 0.18f * (float) Math.sin(cycle);
            centerY += height * 0.12f * (float) Math.cos(cycle * 0.83f);
        } else if ("pulse".equals(animation)) {
            pulse = 0.78f + 0.22f * (0.5f + 0.5f * (float) Math.sin(cycle));
            baseAngle += 18f * (float) Math.sin(cycle);
            centerX += width * 0.04f * (float) Math.sin(cycle);
            centerY += height * 0.03f * (float) Math.cos(cycle);
        } else if ("rotate".equals(animation)) {
            baseAngle += phase * 360f;
            centerX += width * 0.10f * (float) Math.cos(cycle);
            centerY += height * 0.08f * (float) Math.sin(cycle);
        } else if ("wave".equals(animation)) {
            baseAngle += 32f * (float) Math.sin(cycle);
            centerX += width * 0.12f * (float) Math.sin(cycle * 1.4f);
            centerY += height * 0.10f * (float) Math.cos(cycle * 0.9f);
            pulse = 0.88f + 0.12f * (float) Math.sin(cycle * 1.7f);
        } else if ("wave_3d".equals(animation) || "3d_wave".equals(animation)) {
            baseAngle += 18f * (float) Math.sin(cycle * 0.8f);
            centerX += width * 0.14f * (float) Math.sin(cycle * 1.2f);
            centerY += height * 0.08f * (float) Math.sin(cycle * 0.6f + 0.7f);
            pulse = 0.80f + 0.20f * (0.5f + 0.5f * (float) Math.sin(cycle * 1.5f));
        } else if ("orbit".equals(animation)) {
            centerX += width * 0.22f * (float) Math.cos(cycle);
            centerY += height * 0.16f * (float) Math.sin(cycle);
            baseAngle += phase * 180f;
        } else if ("shimmer".equals(animation)) {
            centerX += width * 0.30f * (float) Math.sin(cycle);
            baseAngle += 10f * (float) Math.sin(cycle);
            pulse = 0.92f + 0.08f * (float) Math.sin(cycle * 2f);
        } else if ("glide".equals(animation)) {
            centerX += width * 0.16f * (float) Math.sin(cycle * 0.5f);
            centerY += height * 0.16f * (float) Math.sin(cycle * 0.75f + 1.2f);
            baseAngle += 6f * (float) Math.sin(cycle);
        } else if ("ripple".equals(animation)) {
            pulse = 0.82f + 0.18f * (0.5f + 0.5f * (float) Math.sin(cycle * 1.6f));
            baseAngle += 14f * (float) Math.sin(cycle * 0.5f);
            centerX += width * 0.06f * (float) Math.sin(cycle * 0.9f);
            centerY += height * 0.06f * (float) Math.cos(cycle * 0.7f);
        } else if ("breathe".equals(animation)) {
            pulse = 0.68f + 0.32f * (0.5f + 0.5f * (float) Math.sin(cycle));
            centerY += height * 0.04f * (float) Math.sin(cycle * 0.5f);
        }

        paint.reset();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        String pattern = normalized(config.gradientPattern, "linear");
        if ("checker".equals(pattern)) {
            drawChecker(
                    canvas,
                    paint,
                    width,
                    height,
                    colors,
                    phase,
                    patternScale,
                    pulse,
                    baseAngle,
                    centerX,
                    centerY
            );
            return;
        } else if ("diamond".equals(pattern)) {
            drawDiamond(canvas, paint, width, height, colors, positions, baseAngle, centerX, centerY, patternScale, pulse);
            return;
        }
        Shader shader;
        if ("radial".equals(pattern) || "radial_bands".equals(pattern)) {
            boolean bands = "radial_bands".equals(pattern);
            float radius = Math.max(width, height) * (bands ? 0.28f : 0.78f) * patternScale * pulse;
            shader = new RadialGradient(
                    centerX,
                    centerY,
                    Math.max(1f, radius),
                    colors,
                    positions,
                    bands ? Shader.TileMode.MIRROR : Shader.TileMode.CLAMP
            );
        } else if ("sweep".equals(pattern)) {
            SweepGradient sweep = new SweepGradient(centerX, centerY, colors, positions);
            Matrix matrix = new Matrix();
            matrix.setRotate(baseAngle, centerX, centerY);
            sweep.setLocalMatrix(matrix);
            shader = sweep;
        } else {
            double radians = Math.toRadians(baseAngle);
            float dx = (float) Math.cos(radians);
            float dy = (float) Math.sin(radians);
            float length = (float) Math.hypot(width, height) * patternScale * pulse;
            float startX = centerX - dx * length * 0.5f;
            float startY = centerY - dy * length * 0.5f;
            float endX = centerX + dx * length * 0.5f;
            float endY = centerY + dy * length * 0.5f;
            Shader.TileMode tileMode = "bands".equals(pattern) ? Shader.TileMode.MIRROR : Shader.TileMode.CLAMP;
            if ("bands".equals(pattern)) {
                float travel = (float) Math.sin(cycle) * length * 0.36f;
                startX += dx * travel;
                startY += dy * travel;
                endX += dx * travel;
                endY += dy * travel;
                endX = startX + dx * length * 0.36f;
                endY = startY + dy * length * 0.36f;
            }
            shader = new LinearGradient(startX, startY, endX, endY, colors, positions, tileMode);
        }

        paint.setShader(shader);
        canvas.drawRect(0f, 0f, width, height, paint);
        paint.setShader(null);
    }

    private static int cycleHue(int color, float phase, float offset) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[0] = (hsv[0] + phase * 360f + offset * 360f) % 360f;
        return Color.HSVToColor(Color.alpha(color), hsv);
    }

    private static void drawChecker(
            Canvas canvas,
            Paint paint,
            int width,
            int height,
            int[] colors,
            float phase,
            float patternScale,
            float pulse,
            float angle,
            float centerX,
            float centerY
    ) {
        float tile = Math.max(48f, Math.min(width, height) * 0.12f * patternScale * pulse);
        float offset = (float) Math.sin(phase * Math.PI * 2.0) * tile;
        float extent = (float) Math.hypot(width, height);
        int start = (int) Math.floor(-extent / tile) - 2;
        int end = (int) Math.ceil(extent * 2f / tile) + 2;
        int save = canvas.save();
        canvas.rotate(angle, centerX, centerY);
        for (int row = start; row <= end; row++) {
            for (int column = start; column <= end; column++) {
                int index = Math.floorMod(row + column, 3);
                paint.setColor(colors[index]);
                float left = column * tile + offset;
                float top = row * tile + offset * 0.55f;
                canvas.drawRect(left, top, left + tile + 1f, top + tile + 1f, paint);
            }
        }
        canvas.restoreToCount(save);
    }

    private static void drawDiamond(
            Canvas canvas,
            Paint paint,
            int width,
            int height,
            int[] colors,
            float[] positions,
            float angle,
            float centerX,
            float centerY,
            float patternScale,
            float pulse
    ) {
        float size = Math.max(width, height) * 0.82f * patternScale * pulse;
        Path diamond = new Path();
        diamond.moveTo(centerX, centerY - size * 0.5f);
        diamond.lineTo(centerX + size * 0.5f, centerY);
        diamond.lineTo(centerX, centerY + size * 0.5f);
        diamond.lineTo(centerX - size * 0.5f, centerY);
        diamond.close();
        int save = canvas.save();
        canvas.clipPath(diamond);
        LinearGradient gradient = new LinearGradient(
                centerX - size * 0.5f,
                centerY - size * 0.5f,
                centerX + size * 0.5f,
                centerY + size * 0.5f,
                colors,
                positions,
                Shader.TileMode.MIRROR
        );
        paint.setShader(gradient);
        int rotateSave = canvas.save();
        canvas.rotate(angle, centerX, centerY);
        canvas.drawRect(new RectF(0f, 0f, width, height), paint);
        canvas.restoreToCount(rotateSave);
        canvas.restoreToCount(save);
        paint.setShader(null);
    }

    public static long animationDurationMs(int speedPercent) {
        int safeSpeed = clamp(speedPercent, 20, 300);
        return Math.max(1500L, 650000L / safeSpeed);
    }

    public static int legacySpeedPercent(String speed) {
        if ("slow".equalsIgnoreCase(speed)) {
            return 60;
        }
        if ("medium".equalsIgnoreCase(speed)) {
            return 100;
        }
        return 185;
    }

    public static String[] animationOptions() {
        return new String[]{
                "drift",
                "pulse",
                "rotate",
                "wave",
                "wave_3d",
                "orbit",
                "shimmer",
                "glide",
                "ripple",
                "breathe",
                "static"
        };
    }

    public static String legacySpeedLabel(int speedPercent) {
        if (speedPercent <= 75) {
            return "slow";
        }
        if (speedPercent <= 135) {
            return "medium";
        }
        return "fast";
    }

    private static float animationPhase(int speedPercent, long elapsed) {
        long duration = animationDurationMs(speedPercent);
        return (elapsed % duration) / (float) duration;
    }

    private static int opaque(int color) {
        return Color.rgb(Color.red(color), Color.green(color), Color.blue(color));
    }

    private static int adjusted(int color, OverlayConfig config) {
        float saturation = clamp(config.gradientSaturationPct, 0, 200) / 100f;
        float contrast = clamp(config.gradientContrastPct, 50, 150) / 100f;
        float brightness = clamp(config.gradientBrightness, -60, 60) * 2.55f;
        float exposure = clamp(config.gradientExposurePct, -40, 40) * 3.0f;
        float gamma = clamp(config.gradientGammaPct, 50, 200) / 100f;
        float vibrance = clamp(config.gradientVibrancePct, 0, 200) / 100f;
        float hueShift = config.gradientHueShiftDeg;
        float yellowBoost = clamp(config.gradientYellowBoostPct, 0, 100) / 100f;
        float redBoost = clamp(config.gradientRedBoostPct, 0, 100) / 100f;
        float greenBoost = clamp(config.gradientGreenBoostPct, 0, 100) / 100f;
        float blueBoost = clamp(config.gradientBlueBoostPct, 0, 100) / 100f;
        float red = Color.red(color);
        float green = Color.green(color);
        float blue = Color.blue(color);
        float luminance = red * 0.2126f + green * 0.7152f + blue * 0.0722f;
        red = luminance + (red - luminance) * saturation * vibrance;
        green = luminance + (green - luminance) * saturation * vibrance;
        blue = luminance + (blue - luminance) * saturation * vibrance;
        red = (red - 128f) * contrast + 128f + brightness + exposure;
        green = (green - 128f) * contrast + 128f + brightness + exposure;
        blue = (blue - 128f) * contrast + 128f + brightness + exposure;
        red += redBoost * 56f;
        green += greenBoost * 56f;
        blue += blueBoost * 56f;
        red += yellowBoost * 52f;
        green += yellowBoost * 42f;
        blue -= yellowBoost * 18f;
        float[] hsv = new float[3];
        Color.RGBToHSV(clampColor(red), clampColor(green), clampColor(blue), hsv);
        hsv[0] = (hsv[0] + hueShift + 360f) % 360f;
        hsv[1] = Math.max(0f, Math.min(1f, hsv[1] * vibrance));
        hsv[2] = (float) Math.pow(Math.max(0f, Math.min(1f, hsv[2])), 1f / Math.max(0.5f, gamma));
        return Color.HSVToColor(hsv);
    }

    private static int clampColor(float value) {
        return Math.max(0, Math.min(255, Math.round(value)));
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private static String normalized(String value, String fallback) {
        return value == null || value.trim().length() == 0 ? fallback : value.trim().toLowerCase();
    }
}
