package motaz.pharaon.adu.ac.ae.traceroute.engine.entity;

import motaz.pharaon.adu.ac.ae.traceroute.engine.render.RenderPipeline;

/*
 * Purpose:
 * - Marker interface for objects that can be submitted to the RenderPipeline.
 *
 * For the MVP, WorldInvestigationScene renders directly without a general
 * renderable list.  This interface is available for future expansion.
 */
public interface Renderable {
    void render(RenderPipeline renderPipeline);
}
