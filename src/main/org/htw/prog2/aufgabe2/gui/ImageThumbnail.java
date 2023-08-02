package org.htw.prog2.aufgabe2.gui;

import org.htw.prog2.aufgabe2.DICOMFrame;
import org.htw.prog2.aufgabe2.DICOMImage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class ImageThumbnail extends JPanel {
    private int border;
    private int size;
    private boolean isSelected;
    protected DICOMFrame frame;

    public ImageThumbnail(DICOMFrame dicomFrame, int size, int border) {
        this.border = border;
        this.size = size;

        BufferedImage frameImage = new BufferedImage(size-2*border, size-2*border, BufferedImage.TYPE_4BYTE_ABGR);
        Image scaledImage = dicomFrame.getImage().getScaledInstance(size - 2 * border, size - 2 * border, Image.SCALE_SMOOTH);
        frameImage.getGraphics().drawImage(scaledImage, 0,0, null);
        frame = new DICOMFrame(frameImage,dicomFrame.getNum());
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(frame.getImage(), border, border, null);
        g.setColor(Color.WHITE);
        g.drawString(getDescription(), 5 * border, size - 10 * border);
        if (isSelected) {
            g.setColor(Color.BLUE);
        } else {
            g.setColor(Color.GRAY);
        }
        g.drawRect(0, 0, size, size);
        g.drawRect(1, 1, size - 1, size - 1);
        g.drawRect(2, 2, size - 2, size - 2);
        g.drawRect(3, 3, size - 3, size - 3);
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public abstract String getDescription();
}