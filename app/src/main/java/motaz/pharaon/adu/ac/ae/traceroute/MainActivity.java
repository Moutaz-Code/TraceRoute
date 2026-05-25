package motaz.pharaon.adu.ac.ae.traceroute;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment_container, new GameShellFragment())
                    .commit();
        }
    }
}
