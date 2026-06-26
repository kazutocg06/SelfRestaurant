package Controller;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class KineticScroller {

    public static void setup(JScrollPane jScrollPane) {
        
        jScrollPane.getVerticalScrollBar().setUnitIncrement(40); 
        jScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            private Point startPt;
            private Point startViewPos;

            @Override
            public void eventDispatched(AWTEvent event) {
                if (event instanceof MouseEvent) {
                    MouseEvent me = (MouseEvent) event;
                    
                    if (!SwingUtilities.isDescendingFrom(me.getComponent(), jScrollPane)) {
                        return;
                    }
                    
                    if (me.getComponent() instanceof JButton) {
                        return;
                    }

                    if (me.getID() == MouseEvent.MOUSE_PRESSED) {
                        startPt = me.getLocationOnScreen();
                        startViewPos = jScrollPane.getViewport().getViewPosition();
                    } 
                    else if (me.getID() == MouseEvent.MOUSE_DRAGGED) {
                        if (startPt != null && startViewPos != null) {
                            Point currentPt = me.getLocationOnScreen();
                            
                            int deltaY = (startPt.y - currentPt.y) * (2); 
                            
                            Point newViewPos = new Point(startViewPos.x, startViewPos.y + deltaY);
                            
                            int maxY = jScrollPane.getViewport().getView().getHeight() - jScrollPane.getViewport().getHeight();
                            if (newViewPos.y < 0) newViewPos.y = 0;
                            if (newViewPos.y > maxY) newViewPos.y = Math.max(0, maxY);
                            
                            jScrollPane.getViewport().setViewPosition(newViewPos);
                        }
                    }
                    else if (me.getID() == MouseEvent.MOUSE_RELEASED) {
                        startPt = null;
                        startViewPos = null;
                    }
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
    }
}