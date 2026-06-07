package motaz.pharaon.adu.ac.ae.traceroute.engine.render.postprocess;

import motaz.pharaon.adu.ac.ae.traceroute.engine.gl.Framebuffer;

/*
 * Purpose:
 * - Base class for a single post-process render pass (scanlines, vignette, glitch).
 *
 * For the MVP no post-processing is active. This scaffold lets a future
 * pass be dropped into PostProcessStack without changing any call sites.
 */
public abstract class PostProcessPass {

    private boolean enabled;

    protected PostProcessPass() {
        this.enabled = false;
    }

    /** Apply this pass: sample inputFbo's color texture, output to outputFbo (or default). */
    public abstract void apply(Framebuffer inputFbo, Framebuffer outputFbo);

    /** Release any GPU resources owned by this pass. */
    public abstract void release();

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
