package com.pic.lib.largeImageUtils.factory;

import android.graphics.BitmapRegionDecoder;

import java.io.IOException;

public interface BitmapDecoderFactory {
    BitmapRegionDecoder made() throws IOException;
    int[] getImageInfo();
}