package motaz.pharaon.adu.ac.ae.traceroute.ui.laptop;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Random;

import motaz.pharaon.adu.ac.ae.traceroute.R;
import motaz.pharaon.adu.ac.ae.traceroute.audio.AudioManager;
import motaz.pharaon.adu.ac.ae.traceroute.data.model.LaptopPanel;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.laptop.LaptopPanelProcessor;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.laptop.LaptopSession;

public class LaptopViewBinder {
    /*
     * Purpose:
     * - Plain Java helper for wiring the Win98 desktop inside LaptopFragment.
     *
     * Responsibilities:
     * - Build one desktop icon per authored laptop panel.
     * - Open a single floating Win98 window when an icon is tapped (collecting that
     *   panel's evidence) and show its info text; close it with the window's X or by
     *   tapping the empty desktop.
     * - Route the invisible taskbar hit area to closing the whole laptop.
     *
     * Styling:
     * - Look comes from the laptop_* drawables/colours and the inflated
     *   view_laptop_desktop_icon layout. Art is resolved at runtime
     *   (laptop_desktop wallpaper, laptop_icon_<panelId>) with safe fallbacks,
     *   so the screen works before per-panel art is added.
     *
     * Depends on:
     * - LaptopFragment's inflated layout.
     * - LaptopSession for the panels, open/read state.
     *
     * Used by:
     * - LaptopFragment.
     */

    private final View rootView;
    private final FrameLayout desktop;
    private final LinearLayout iconContainer;
    private final View window;
    private final TextView windowTitle;
    private final TextView windowBody;
    private final View windowClose;
    private final View taskbarBack;

    private final Random random = new Random();

    public LaptopViewBinder(View rootView) {
        this.rootView = rootView;
        desktop = rootView.findViewById(R.id.laptop_root);
        iconContainer = rootView.findViewById(R.id.laptop_icon_container);
        window = rootView.findViewById(R.id.laptop_window);
        windowTitle = rootView.findViewById(R.id.laptop_window_title);
        windowBody = rootView.findViewById(R.id.laptop_window_body);
        windowClose = rootView.findViewById(R.id.laptop_window_close);
        taskbarBack = rootView.findViewById(R.id.laptop_taskbar_back);
    }

    public void bind(
            LaptopSession laptopSession,
            LaptopPanelProcessor panelProcessor,
            Runnable closeCallback
    ) {
        taskbarBack.setOnClickListener(view -> closeCallback.run());
        desktop.setOnClickListener(view -> hideWindow());
        windowClose.setOnClickListener(view -> hideWindow());

        buildIcons(laptopSession, panelProcessor);
        hideWindow();
    }

    private void buildIcons(LaptopSession laptopSession, LaptopPanelProcessor panelProcessor) {
        iconContainer.removeAllViews();

        List<LaptopPanel> panels = laptopSession.getPanels();
        for (LaptopPanel panel : panels) {
            View icon = LayoutInflater.from(rootView.getContext())
                    .inflate(R.layout.view_laptop_desktop_icon, iconContainer, false);

            ((TextView) icon.findViewById(R.id.laptop_icon_label)).setText(panel.getLabel());

            int iconRes = iconDrawableFor(panel.getPanelId());
            if (iconRes != 0) {
                ((ImageView) icon.findViewById(R.id.laptop_icon_image)).setImageResource(iconRes);
            }

            icon.setOnClickListener(view -> openWindow(panel, laptopSession, panelProcessor));
            iconContainer.addView(icon);
        }
    }

    private void openWindow(
            LaptopPanel panel,
            LaptopSession laptopSession,
            LaptopPanelProcessor panelProcessor
    ) {
        String body = laptopSession.openPanel(panel.getPanelId(), panelProcessor);
        AudioManager.getInstance().playPanelRead();

        windowTitle.setText(panel.getLabel());
        windowBody.setText(body);
        window.setVisibility(View.VISIBLE);
        placeWindowRandomly();
    }

    private void hideWindow() {
        window.setVisibility(View.GONE);
    }

    private void placeWindowRandomly() {
        window.post(() -> {
            int freeX = Math.max(0, desktop.getWidth() - window.getWidth());
            int freeY = Math.max(0, desktop.getHeight() - window.getHeight());

            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) window.getLayoutParams();
            params.leftMargin = freeX == 0 ? 0 : random.nextInt(freeX);
            params.topMargin = freeY == 0 ? 0 : random.nextInt(freeY);
            window.setLayoutParams(params);
        });
    }

    // Maps a panel to its art by convention: drawable laptop_icon_<panelId>.
    // Returns 0 if there is no matching drawable.
    private int iconDrawableFor(String panelId) {
        return rootView.getResources().getIdentifier(
                "laptop_icon_" + panelId, "drawable", rootView.getContext().getPackageName());
    }
}
