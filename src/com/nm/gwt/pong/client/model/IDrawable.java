package com.nm.gwt.pong.client.model;

import com.google.gwt.canvas.dom.client.Context2d;

/**
 * Represents Drawable elements on a Context 2D of a canvas.
 * @author Nir Moav
 */
public interface IDrawable {

	void draw(Context2d ctx) ;
}
