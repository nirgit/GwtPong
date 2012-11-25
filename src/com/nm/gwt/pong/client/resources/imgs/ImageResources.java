package com.nm.gwt.pong.client.resources.imgs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface ImageResources extends ClientBundle {

	public static ImageResources INSTANCE = GWT.create(ImageResources.class) ;
	
	@Source("pad.png")
	ImageResource pad() ;
}
