package org.htw.prog2.aufgabe2.gui;

import org.htw.prog2.aufgabe2.DICOMFrame;
import org.htw.prog2.aufgabe2.DICOMImage;

public class SeriesThumbnail extends ImageThumbnail {
    private DICOMImage image;
    public SeriesThumbnail(DICOMFrame dicomFrame, int size, int border) {
        super(dicomFrame, size, border);
    }

    public SeriesThumbnail(DICOMImage image, int size, int border) {
        super(image.getFrame(0),size,border);
        this.image = image;
    }

    @Override
    public String getDescription() {
        return "Series(" + image.getNumFrames() + ")";
    }
}
