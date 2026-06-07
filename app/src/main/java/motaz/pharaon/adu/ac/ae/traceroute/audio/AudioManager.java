package motaz.pharaon.adu.ac.ae.traceroute.audio;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.ToneGenerator;

/*
 * Purpose:
 * - Manages ambient background audio and one-shot sound effects for the investigation.
 *
 * Responsibilities:
 * - Load and play the ambient apartment loop.
 * - Trigger short SFX (evidence collected, thread stabilized, laptop interaction).
 * - Release media resources on teardown.
 * - Fail silently when audio assets are absent, so the game still runs without audio.
 *
 * Used by:
 * - MainActivity / GameShellFragment for lifecycle hooks.
 * - GameController for SFX triggers (future pass).
 */
public class AudioManager {

    private static AudioManager instance;

    private MediaPlayer ambientPlayer;
    private SoundPool soundPool;
    private ToneGenerator toneGenerator;
    private boolean initialized;

    private AudioManager() {
    }

    public static synchronized AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public synchronized void initialize(Context context) {
        if (initialized) {
            return;
        }

        try {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(4)
                    .setAudioAttributes(audioAttributes)
                    .build();
            toneGenerator = new ToneGenerator(android.media.AudioManager.STREAM_MUSIC, 36);

            initialized = true;
        } catch (Exception ignored) {
            // Audio not critical for MVP.
        }
    }

    public synchronized void startAmbientLoop(Context context, String assetPath) {
        if (!initialized || assetPath == null || assetPath.trim().isEmpty()) {
            return;
        }

        stopAmbientLoop();

        try {
            ambientPlayer = new MediaPlayer();
            android.content.res.AssetFileDescriptor descriptor =
                    context.getAssets().openFd(assetPath);
            ambientPlayer.setDataSource(
                    descriptor.getFileDescriptor(),
                    descriptor.getStartOffset(),
                    descriptor.getLength()
            );
            descriptor.close();
            ambientPlayer.setLooping(true);
            ambientPlayer.setVolume(0.5f, 0.5f);
            ambientPlayer.prepare();
            ambientPlayer.start();
        } catch (Exception ignored) {
            ambientPlayer = null;
        }
    }

    public synchronized void stopAmbientLoop() {
        if (ambientPlayer != null) {
            try {
                ambientPlayer.stop();
                ambientPlayer.release();
            } catch (Exception ignored) {
            }
            ambientPlayer = null;
        }
    }

    public synchronized void playEvidenceAcquired() {
        playTone(ToneGenerator.TONE_PROP_ACK, 110);
    }

    public synchronized void playAlreadyCollected() {
        playTone(ToneGenerator.TONE_PROP_BEEP, 70);
    }

    public synchronized void playDeviceOpen() {
        playTone(ToneGenerator.TONE_PROP_PROMPT, 95);
    }

    public synchronized void playPanelRead() {
        playTone(ToneGenerator.TONE_DTMF_S, 75);
    }

    public synchronized void playTimelineSuccess() {
        playTone(ToneGenerator.TONE_CDMA_CONFIRM, 150);
    }

    public synchronized void playCaseComplete() {
        playTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 260);
    }

    public synchronized void release() {
        stopAmbientLoop();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
        if (toneGenerator != null) {
            toneGenerator.release();
            toneGenerator = null;
        }
        initialized = false;
    }

    private void playTone(int toneType, int durationMs) {
        if (!initialized || toneGenerator == null) {
            return;
        }

        try {
            toneGenerator.startTone(toneType, durationMs);
        } catch (RuntimeException ignored) {
            // Audio feedback must never block the demo.
        }
    }
}
