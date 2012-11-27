package com.nm.gwt.pong.client.model;

import com.google.gwt.canvas.dom.client.Context2d;

/**
 * Date: 27/11/2012
 * Description: Represents Drawable elements on a Context 2D of a canvas.
 * @author Nir Moav
 */
public interface IDrawable {

	/**
	 * Draws the shape
	 * @param ctx
	 */
	void draw(Context2d ctx) ;
}
