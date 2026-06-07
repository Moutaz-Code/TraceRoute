package motaz.pharaon.adu.ac.ae.traceroute.engine.render.postprocess;

import java.util.ArrayList;
import java.util.List;

import motaz.pharaon.adu.ac.ae.traceroute.engine.gl.Framebuffer;

/*
 * Purpose:
 * - Owns and sequences post-process passes after the main scene is rendered.
 *
 * For the MVP this stack is empty and the scene renders straight to the
 * device framebuffer.  Future passes (scanlines, vignette, glitch) can
 * be added via addPass() without touching the render pipeline.
 */
public class PostProcessStack {

    private final List<PostProcessPass> passes;

    public PostProcessStack() {
        passes = new ArrayList<>();
    }

    public void addPass(PostProcessPass pass) {
        passes.add(pass);
    }

    /**
     * Run all enabled passes.
     * Input is the main scene FBO; the final pass outputs to the default framebuffer.
     */
    public void apply(Framebuffer sceneFbo) {
        if (passes.isEmpty() || sceneFbo == null) {
            return;
        }

        for (int index = 0; index < passes.size(); index++) {
            PostProcessPass pass = passes.get(index);
            if (pass.isEnabled()) {
                Framebuffer output = (index == passes.size() - 1) ? null : sceneFbo;
                pass.apply(sceneFbo, output);
            }
        }
    }

    public void release() {
        for (PostProcessPass pass : passes) {
            pass.release();
        }
        passes.clear();
    }

    public boolean isEmpty() { return passes.isEmpty(); }
}
