package motaz.pharaon.adu.ac.ae.traceroute;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import motaz.pharaon.adu.ac.ae.traceroute.audio.AudioManager;
import motaz.pharaon.adu.ac.ae.traceroute.core.GameRuntime;
import motaz.pharaon.adu.ac.ae.traceroute.ui.shell.GameShellFragment;

public class MainActivity extends AppCompatActivity {
    /*
     * Purpose:
     * - Android entry point for the game.
     *
     * Responsibilities:
     * - Own Android lifecycle events.
     * - Host the root GameShellFragment.
     * - Avoid owning game-specific UI or case logic directly.
     *
     * Depends on:
     * - GameShellFragment for the actual game shell.
     * - activity_main.xml for the fragment container.
     *
     * Used by:
     * - Android OS through AndroidManifest.xml.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        GameRuntime.initialize(getApplicationContext());
        AudioManager.getInstance().initialize(getApplicationContext());
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_main);
        hideSystemBars();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment_container, new GameShellFragment())
                    .commit();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemBars();
        }
    }

    @Override
    protected void onDestroy() {
        if (isFinishing()) {
            AudioManager.getInstance().release();
        }
        super.onDestroy();
    }

    private void hideSystemBars() {
        View decorView = getWindow().getDecorView();
        WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(getWindow(), decorView);

        controller.hide(WindowInsetsCompat.Type.systemBars());
        controller.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );
    }
}
