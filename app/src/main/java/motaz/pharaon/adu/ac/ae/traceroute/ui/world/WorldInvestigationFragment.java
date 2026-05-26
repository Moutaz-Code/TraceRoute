package motaz.pharaon.adu.ac.ae.traceroute.ui.world;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import motaz.pharaon.adu.ac.ae.traceroute.GameView;
import motaz.pharaon.adu.ac.ae.traceroute.R;
import motaz.pharaon.adu.ac.ae.traceroute.core.GameUiEventListener;

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
    }
}
