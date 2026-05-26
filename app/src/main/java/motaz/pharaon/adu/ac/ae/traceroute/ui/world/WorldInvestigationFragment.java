package motaz.pharaon.adu.ac.ae.traceroute.ui.world;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import motaz.pharaon.adu.ac.ae.traceroute.GameView;
import motaz.pharaon.adu.ac.ae.traceroute.R;
import motaz.pharaon.adu.ac.ae.traceroute.core.GameUiEventListener;
import motaz.pharaon.adu.ac.ae.traceroute.core.GameRuntime;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.world.RoomDefinition;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.world.RoomId;

public class WorldInvestigationFragment extends Fragment implements GameUiEventListener {
    /*
     * Purpose:
     * - Fragment wrapper for the OpenGL apartment investigation view.
     *
     * Responsibilities:
     * - Own the GameView lifecycle.
     * - Place the GLSurfaceView inside a fragment so the project satisfies the fragment requirement.
     * - Stay focused on world rendering/input; handheld and terminal UI are separate fragments.
     *
     * Depends on:
     * - GameView for OpenGL rendering and touch capture.
     * - fragment_world_investigation.xml for the container.
     *
     * Used by:
     * - GameShellFragment.
     */

    private GameView gameView;
    private TextView roomTitleText;
    private TextView roomContextText;
    private TextView roomHotspotText;
    private TextView currentLeadText;
    private View debugOverlayView;
    private View debugToolRow;
    private TextView debugPanelToggle;
    private TextView debugOverlayToggle;
    private WorldDebugOverlayBinder debugOverlayBinder;
    private boolean debugOverlayVisible;
    private boolean debugToolsVisible;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_world_investigation, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FrameLayout gameViewContainer = view.findViewById(R.id.game_view_container);
        roomTitleText = view.findViewById(R.id.debug_room_title);
        roomContextText = view.findViewById(R.id.debug_room_context);
        roomHotspotText = view.findViewById(R.id.debug_label_room_hotspot);
        currentLeadText = view.findViewById(R.id.debug_current_lead);
        debugOverlayView = view.findViewById(R.id.debug_visualization_overlay);
        debugToolRow = view.findViewById(R.id.debug_tool_row);
        debugPanelToggle = view.findViewById(R.id.debug_panel_toggle);
        debugOverlayToggle = view.findViewById(R.id.debug_overlay_toggle);
        debugOverlayBinder = new WorldDebugOverlayBinder((FrameLayout) debugOverlayView);
        debugOverlayVisible = true;
        debugToolsVisible = false;
        debugPanelToggle.setOnClickListener(toggle -> toggleDebugTools());
        debugOverlayToggle.setOnClickListener(toggle -> toggleDebugOverlay());
        view.findViewById(R.id.debug_reset_run).setOnClickListener(reset -> confirmResetPlaythrough());
        view.findViewById(R.id.debug_unlock_core).setOnClickListener(unlock -> unlockCoreEvidenceForDebug());
        renderCurrentLead();

        gameView = new GameView(requireContext());
        gameView.setUiEventListener(this);

        gameViewContainer.addView(
                gameView,
                new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                )
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        if (gameView != null) {
            gameView.onResume();
        }
    }

    @Override
    public void onPause() {
        if (gameView != null) {
            gameView.onPause();
        }
        super.onPause();
    }




    @Override
    public void onOpenHandheldRequested() {
        if (getParentFragment() instanceof GameUiEventListener) {
            ((GameUiEventListener) getParentFragment()).onOpenHandheldRequested();
        }
    }

    @Override
    public void onOpenTerminalRequested() {
        if (getParentFragment() instanceof GameUiEventListener) {
            ((GameUiEventListener) getParentFragment()).onOpenTerminalRequested();
        }
    }

    @Override
    public void onEvidenceInspectionRequested(String evidenceId) {
        renderCurrentLead();
        if (getParentFragment() instanceof GameUiEventListener) {
            ((GameUiEventListener) getParentFragment()).onEvidenceInspectionRequested(evidenceId);
        }
    }

    @Override
    public void onRoomContextChanged(String roomName, String contextText, String hotspotHint) {
        if (roomTitleText != null) {
            roomTitleText.setText(roomName);
        }

        if (roomContextText != null) {
            roomContextText.setText(contextText);
        }

        if (roomHotspotText != null) {
            roomHotspotText.setText(hotspotHint);
        }

        if (debugOverlayBinder != null) {
            debugOverlayBinder.render(findCurrentRoomDefinition());
        }

        renderCurrentLead();
    }

    private void toggleDebugTools() {
        debugToolsVisible = !debugToolsVisible;
        debugToolRow.setVisibility(debugToolsVisible ? View.VISIBLE : View.GONE);
        debugPanelToggle.setText(debugToolsVisible ? "Debug -" : "Debug");
    }

    private void toggleDebugOverlay() {
        debugOverlayVisible = !debugOverlayVisible;
        debugOverlayView.setVisibility(debugOverlayVisible ? View.VISIBLE : View.GONE);
        debugOverlayToggle.setText(debugOverlayVisible ? "Overlay On" : "Overlay Off");

        if (debugOverlayVisible && debugOverlayBinder != null) {
            debugOverlayBinder.render(findCurrentRoomDefinition());
        }
    }

    private void confirmResetPlaythrough() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Reset investigation?")
                .setMessage("This clears collected evidence, current room, and final timeline progress.")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Reset", (dialog, which) -> {
                    GameRuntime.resetPlaythrough(requireContext().getApplicationContext());
                    if (requireActivity() != null) {
                        requireActivity().recreate();
                    }
                })
                .show();
    }

    private void unlockCoreEvidenceForDebug() {
        GameRuntime.getInstance().debugCollectAllEvidence();
        renderCurrentLead();
        Toast.makeText(requireContext(), "Core evidence unlocked.", Toast.LENGTH_SHORT).show();
    }

    private void renderCurrentLead() {
        if (currentLeadText != null) {
            currentLeadText.setText("Lead: " + GameRuntime.getInstance().getCurrentLead());
        }
    }

    private RoomDefinition findCurrentRoomDefinition() {
        RoomId currentRoomId = GameRuntime.getInstance().getCurrentRoomId();
        for (RoomDefinition roomDefinition : GameRuntime.getInstance().getRoomDefinitions()) {
            if (roomDefinition.getId() == currentRoomId) {
                return roomDefinition;
            }
        }

        return null;
    }
}
