package org.htw.prog2.aufgabe2.gui;

import org.htw.prog2.aufgabe2.DICOMFrame;
import org.htw.prog2.aufgabe2.DICOMFrameMark;
import org.htw.prog2.aufgabe2.DICOMImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.LinkedList;

public class ImageListPanel extends JPanel {
    private DICOMImage image;
    private SeriesThumbnail thumbnailPanel;
    private LinkedList<FrameThumbnail> markedFrames;
    private MainFrame mainFrame;
    private FrameThumbnail selectedThumbnail;
    private JPopupMenu popupMenu;
    private JMenuItem delete;
    private JMenuItem save;

    public FrameThumbnail getSelectedThumbnail() {
        return selectedThumbnail;
    }

    public void setSelectedThumbnail(FrameThumbnail selectedThumbnail) {
        this.selectedThumbnail = selectedThumbnail;
    }

    public LinkedList<FrameThumbnail> getMarkedFrames() {
        return markedFrames;
    }

    public ImageListPanel(int width, MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        markedFrames = new LinkedList<>();
        initPopupMenu();
        JLabel name = new JLabel("Bilder");
//        thumbnailPanel = new JPanel();
        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);
        add(name);
        add(Box.createVerticalStrut(10));
        name.setAlignmentX(0);
        setPreferredSize(new Dimension(width, 400));
    }

    private void initPopupMenu() {
        popupMenu = new JPopupMenu();
        delete = new JMenuItem("Entfernen");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DICOMFrame frame = selectedThumbnail.getFrame();
                LinkedList<DICOMFrameMark> marks = frame.getMarks();
                int size = marks.size();
                for (int i = size; i > 0; i--) {
                    frame.removeMark(marks.get(i - 1));
                }
                removeSelectedFrameThumbnail();
                mainFrame.getDetailPanel().repaint();
            }
        });

        save = new JMenuItem("Speichern");
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
                chooser.setFileFilter(new FileNameExtensionFilter("DICOM Frame", "png"));
                chooser.showSaveDialog(getParent());
                File f = chooser.getSelectedFile();
                BufferedImage bufferedImage = selectedThumbnail.getFrame().getImage();
                try {
                    ImageIO.write(bufferedImage,"png",f);
                } catch (IOException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
            }
        });
        popupMenu.add(delete);
        popupMenu.add(save);
    }

    public void removeSelectedFrameThumbnail(){
        markedFrames.remove(selectedThumbnail);
        remove(selectedThumbnail);
        selectedThumbnail = null;
        updateSize();
        repaint();
    }

    public void setSelectedFrame(DICOMFrame frame) {
        if (!markedFrames.isEmpty()) {
            for (FrameThumbnail f :
                    markedFrames)
                f.setSelected(f.getFrame() == frame);
        }
    }

    public void setImage(DICOMImage newImage) {
        image = newImage;
        SeriesThumbnail thumbnailPanel = new SeriesThumbnail(newImage, getWidth() - 5, 1);
        thumbnailPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                for (FrameThumbnail f :
                        markedFrames) {
                    f.setSelected(false);
                }
                updateImageListPanel(newImage, thumbnailPanel);
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {
            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {
            }
        });

        add(thumbnailPanel);
        repaint();
//        revalidate();
    }


    private void updateImageListPanel(DICOMImage image, SeriesThumbnail thumbnail) {
        if (thumbnailPanel != null) {
            thumbnailPanel.setSelected(false);
        }

        thumbnailPanel = thumbnail;
        thumbnailPanel.setSelected(true);
        repaint();

        mainFrame.setImage(image);
        mainFrame.getSeriesSlider().setEnabled(true);
        mainFrame.getSeriesSlider().setValue(1);
        mainFrame.getEdgeCheckBox().setEnabled(true);
        mainFrame.getEdgeCheckBox().setSelected(false);
        mainFrame.setDetailSeries(image);
    }

    private void updateSize() {
        setPreferredSize(new Dimension(getWidth(), getWidth() * (2 + markedFrames.size())));
    }

    public void addMarkedFrame(DICOMFrame frame) {
        for (FrameThumbnail f :
                markedFrames) {
            if (frame.getNum() == f.getFrame().getNum())
                return;
        }

        FrameThumbnail thumbnail = new FrameThumbnail(frame, getWidth() - 15, 1);
        thumbnail.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedThumbnail != null) {
                    selectedThumbnail.setSelected(false);
                }
                selectedThumbnail = thumbnail;
                int button = e.getButton();
                if (button == MouseEvent.BUTTON1) {
//                    thumbnailPanel.setSelected(false);
                    thumbnail.setSelected(true);
                    mainFrame.getSeriesSlider().setValue(thumbnail.getFrame().getNum());
                    repaint();
                }

                if (button == MouseEvent.BUTTON3)
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        markedFrames.add(thumbnail);
        updateSize();
        repaintFrameThumbnail();
    }

    private void repaintFrameThumbnail() {
        if (!markedFrames.isEmpty()) {
            for (FrameThumbnail frameThumbnail :
                    markedFrames) {
                add(frameThumbnail);
            }
        }
        repaint();
    }

    public void removeMarkedFrame(DICOMFrame frame) {
        for (FrameThumbnail f :
                markedFrames) {
            if (f.getFrame() == frame) {
                markedFrames.remove(f);
                break;
            }
        }
        updateSize();
        repaintFrameThumbnail();
    }
}