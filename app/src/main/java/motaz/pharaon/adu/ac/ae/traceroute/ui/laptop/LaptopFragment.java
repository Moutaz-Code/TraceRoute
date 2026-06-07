package motaz.pharaon.adu.ac.ae.traceroute.ui.laptop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import motaz.pharaon.adu.ac.ae.traceroute.R;
import motaz.pharaon.adu.ac.ae.traceroute.core.GameRuntime;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.laptop.LaptopPanelProcessor;
import motaz.pharaon.adu.ac.ae.traceroute.gameplay.laptop.LaptopSession;

public class LaptopFragment extends Fragment {
    /*
     * Purpose:
     * - Stylized victim-laptop screen the player opens during investigation.
     *
     * Responsibilities:
     * - Show the Win98 desktop (wallpaper + one icon per laptop panel) full screen.
     * - Open a panel's window when its icon is tapped, which can unlock evidence.
     * - Host as a full-screen overlay fragment (like the handheld), not a dialog,
     *   so it fills the landscape screen simply and reliably.
     *
     * Depends on:
     * - LaptopSession for laptop panel/read state.
     * - LaptopPanelProcessor for opening authored panels.
     * - GameRuntime for evidence/thread unlocks.
     *
     * Used by:
     * - GameShellFragment.
     */

    private LaptopSession laptopSession;
    private LaptopPanelProcessor panelProcessor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_laptop, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        laptopSession = new LaptopSession(GameRuntime.getInstance().getLaptopContent());
        panelProcessor = new LaptopPanelProcessor(GameRuntime.getInstance());

        LaptopViewBinder viewBinder = new LaptopViewBinder(view);
        viewBinder.bind(laptopSession, panelProcessor,
                () -> getParentFragmentManager().popBackStack());
    }
}
