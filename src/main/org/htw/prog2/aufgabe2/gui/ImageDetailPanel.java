package org.htw.prog2.aufgabe2.gui;

import org.htw.prog2.aufgabe2.DICOMFrame;
import org.htw.prog2.aufgabe2.DICOMFrameMark;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;

public class ImageDetailPanel extends JPanel {
    private MainFrame mainFrame;
    private DICOMFrame frame;
    private boolean showEdges;
    private JPopupMenu popupMenu;
    private JMenuItem delete;
    private DICOMFrameMark selectedMark;

    public DICOMFrame getFrame() {
        return frame;
    }

    public ImageDetailPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initPopupMenu();
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
                if (mouseWheelEvent.getWheelRotation() < 0) {
                    mainFrame.setPreviousDetailFrame();
                } else {
                    mainFrame.setNextDetailFrame();
                }
            }
        });

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int button = e.getButton();
                if (button == MouseEvent.BUTTON1){
                    frame.addMark(new DICOMFrameMark(e.getX(),e.getY()));
                    mainFrame.addMarkedFrame(frame);

                    FrameThumbnail selectedThumbnail = mainFrame.getImagelist().getSelectedThumbnail();
                    if (selectedThumbnail != null)
                        selectedThumbnail.setSelected(false);

                    LinkedList<FrameThumbnail> markedFrames = mainFrame.getImagelist().getMarkedFrames();
                    for (FrameThumbnail f:
                         markedFrames) {
                        if (f.getFrame().getNum() == frame.getNum()){
                            f.setSelected(true);
                            f.repaint();
                            mainFrame.getImagelist().setSelectedThumbnail(f);
                            break;
                        }
                    }
                }

                if (button == MouseEvent.BUTTON3){
                    LinkedList<DICOMFrameMark> marks = frame.getMarks();
                    if (!marks.isEmpty()){
                        for (DICOMFrameMark m:
                             marks) {
                            if  (m.isInMark(e.getX(),e.getY())){
                                selectedMark = m;
                                popupMenu.show(e.getComponent(),e.getX(),e.getY());
                                break;
                            }
                        }
                    }
                }
                repaint();
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
    }

    private void initPopupMenu(){
        popupMenu = new JPopupMenu();
        delete = new JMenuItem("Marke entfernen");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (frame.getMarks().contains(selectedMark)){
                    frame.removeMark(selectedMark);
                }
                if (frame.getMarks().isEmpty()){
                    mainFrame.getImagelist().removeSelectedFrameThumbnail();
                }
                repaint();
            }
        });
        popupMenu.add(delete);
    }

    public void paintComponent(Graphics g) {
        if(frame == null) {
            g.setColor(Color.BLACK);
            g.drawString("Bitte ein Bild laden", getWidth()/2, getHeight()/2);
        }
        else {
            if(showEdges) {
                g.drawImage(frame.getEdges(10), 0, 0, null);
            } else {
                g.drawImage(frame.getImage(), 0, 0, null);
            }
            LinkedList<DICOMFrameMark> marks = frame.getMarks();
            if (!marks.isEmpty()){
                for (DICOMFrameMark m:
                     marks){
                    g.setColor(Color.BLUE);
                    g.drawRect(m.getX(),m.getY(),m.getSize(),m.getSize());
                    g.setColor(Color.WHITE);
                    g.drawRect(m.getX()+1,m.getY()+1,m.getSize()-2,m.getSize()-2);
                }
            }
        }
    }

    public void setDetailFrame(DICOMFrame frame, boolean showEdges) {
        this.showEdges = showEdges;
        this.frame = frame;
        setPreferredSize(new Dimension(frame.getImage().getWidth(), frame.getImage().getHeight()));
    }
}
