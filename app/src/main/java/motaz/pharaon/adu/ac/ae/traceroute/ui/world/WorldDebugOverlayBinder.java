package motaz.pharaon.adu.ac.ae.traceroute.ui.world;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import motaz.pharaon.adu.ac.ae.traceroute.gameplay.hotspot.Hotspot;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.hotspot.HotspotActionType;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.world.RoomDefinition;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.world.VisualAnnotation;

public class WorldDebugOverlayBinder {
    /*
     * Purpose:
     * - Draws Android debug labels over the OpenGL world without becoming touch input.
     *
     * Responsibilities:
     * - Label functional hotspot areas with opaque amber boxes.
     * - Label non-functional visual scene elements with blue-outline boxes.
     * - Keep labels visually distinct from translucent OpenGL touchzones.
     */

    private static final int FUNCTIONAL_FILL = Color.rgb(245, 218, 122);
    private static final int FUNCTIONAL_STROKE = Color.rgb(5, 8, 10);
    private static final int NON_FUNCTIONAL_FILL = Color.argb(185, 8, 25, 42);
    private static final int NON_FUNCTIONAL_STROKE = Color.rgb(117, 204, 255);

    private final FrameLayout overlay;

    public WorldDebugOverlayBinder(FrameLayout overlay) {
        this.overlay = overlay;
    }

    public void render(RoomDefinition roomDefinition) {
        overlay.removeAllViews();
        if (roomDefinition == null) {
            return;
        }

        overlay.post(() -> {
            overlay.removeAllViews();
            renderLegend();
            renderVisualAnnotations(roomDefinition.getVisualAnnotations());
            renderHotspotLabels(roomDefinition.getHotspots());
        });
    }

    private void renderLegend() {
        TextView functional = createLabel(
                "AMBER = FUNCTIONAL LABEL",
                Color.rgb(5, 8, 10),
                FUNCTIONAL_FILL,
                FUNCTIONAL_STROKE
        );
        FrameLayout.LayoutParams functionalParams = new FrameLayout.LayoutParams(190, 30);
        functionalParams.leftMargin = 20;
        functionalParams.topMargin = Math.max(96, overlay.getHeight() - 82);
        overlay.addView(functional, functionalParams);

        TextView visual = createLabel(
                "BLUE = VISUAL ONLY",
                Color.rgb(210, 238, 255),
                NON_FUNCTIONAL_FILL,
                NON_FUNCTIONAL_STROKE
        );
        FrameLayout.LayoutParams visualParams = new FrameLayout.LayoutParams(170, 30);
        visualParams.leftMargin = 20;
        visualParams.topMargin = Math.max(132, overlay.getHeight() - 46);
        overlay.addView(visual, visualParams);
    }

    private void renderVisualAnnotations(List<VisualAnnotation> annotations) {
        for (VisualAnnotation annotation : annotations) {
            TextView label = createLabel(
                    "VISUAL: " + annotation.getLabel(),
                    Color.rgb(210, 238, 255),
                    NON_FUNCTIONAL_FILL,
                    NON_FUNCTIONAL_STROKE
            );
            addNormalizedBox(label, annotation.getX(), annotation.getY(), annotation.getWidth(), annotation.getHeight());
        }
    }

    private void renderHotspotLabels(List<Hotspot> hotspots) {
        for (Hotspot hotspot : hotspots) {
            TextView label = createLabel(
                    "TAP: " + buildHotspotLabel(hotspot),
                    Color.rgb(5, 8, 10),
                    FUNCTIONAL_FILL,
                    FUNCTIONAL_STROKE
            );
            addNormalizedBox(label, hotspot.getX(), hotspot.getY(), hotspot.getWidth(), hotspot.getHeight());
        }
    }

    private TextView createLabel(String text, int textColor, int fillColor, int strokeColor) {
        TextView textView = new TextView(overlay.getContext());
        textView.setText(text);
        textView.setTextColor(textColor);
        textView.setTextSize(10);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setGravity(Gravity.CENTER);
        textView.setIncludeFontPadding(false);
        textView.setPadding(6, 4, 6, 4);
        textView.setSingleLine(false);
        textView.setClickable(false);
        textView.setFocusable(false);
        textView.setBackground(createBoxBackground(fillColor, strokeColor));
        return textView;
    }

    private GradientDrawable createBoxBackground(int fillColor, int strokeColor) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(fillColor);
        drawable.setStroke(3, strokeColor);
        drawable.setCornerRadius(0);
        return drawable;
    }

    private void addNormalizedBox(TextView label, float x, float y, float width, float height) {
        int overlayWidth = overlay.getWidth();
        int overlayHeight = overlay.getHeight();
        if (overlayWidth == 0 || overlayHeight == 0) {
            return;
        }

        int left = Math.round(x * overlayWidth);
        int top = Math.round(y * overlayHeight);
        int boxWidth = Math.max(72, Math.round(width * overlayWidth));
        int boxHeight = Math.max(32, Math.round(height * overlayHeight));

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(boxWidth, boxHeight);
        params.leftMargin = clamp(left, 0, Math.max(0, overlayWidth - boxWidth));
        params.topMargin = clamp(top, 0, Math.max(0, overlayHeight - boxHeight));
        label.setLayoutParams(params);

        overlay.addView(label);
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private String buildHotspotLabel(Hotspot hotspot) {
        if (hotspot.getActionType() == HotspotActionType.NAVIGATE_ROOM) {
            if ("nav_previous".equals(hotspot.getId())) {
                return "Prev room";
            }
            if ("nav_next".equals(hotspot.getId())) {
                return "Next room";
            }
            return "Go " + hotspot.getTargetRoomId();
        }

        if (hotspot.getActionType() == HotspotActionType.OPEN_HANDHELD) {
            return "Handheld";
        }

        if (hotspot.getActionType() == HotspotActionType.OPEN_TERMINAL) {
            return "Laptop terminal";
        }

        if (hotspot.getActionType() == HotspotActionType.INSPECT_EVIDENCE) {
            return cleanPayloadLabel(hotspot.getPayloadId());
        }

        return hotspot.getActionType().name();
    }

    private String cleanPayloadLabel(String payloadId) {
        if (payloadId == null || payloadId.trim().isEmpty()) {
            return "Evidence";
        }

        String[] pieces = payloadId.split("_");
        StringBuilder builder = new StringBuilder();
        for (int index = 1; index < pieces.length; index++) {
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(pieces[index].toLowerCase(Locale.US));
        }

        if (builder.length() == 0) {
            return payloadId;
        }

        return builder.substring(0, 1).toUpperCase(Locale.US) + builder.substring(1);
    }
}
