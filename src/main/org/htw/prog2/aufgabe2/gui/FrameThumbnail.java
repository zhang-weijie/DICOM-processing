package org.htw.prog2.aufgabe2.gui;

import org.htw.prog2.aufgabe2.DICOMFrame;

public class FrameThumbnail extends ImageThumbnail {
    private DICOMFrame dicomFrame;
    public DICOMFrame getFrame(){
        return dicomFrame;
    }

    public FrameThumbnail(DICOMFrame dicomFrame, int size, int border) {
        super(dicomFrame, size, border);
        this.dicomFrame = dicomFrame;
    }

    @Override
    public String getDescription() {
        return "Markiert";
    }
}
