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

    // Hàm public static để có thể gọi từ bất kỳ đâu mà không cần new đối tượng
    public static void setup(JScrollPane jScrollPane) {
        
        // 1. TĂNG TỐC ĐỘ LĂN CHUỘT VÀ ẨN THANH TRƯỢT
        jScrollPane.getVerticalScrollBar().setUnitIncrement(40); 
        jScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        
        // 2. THÊM TÍNH NĂNG VUỐT / KÉO THẢ ĐỂ CUỘN
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            private Point startPt;
            private Point startViewPos;

            @Override
            public void eventDispatched(AWTEvent event) {
                if (event instanceof MouseEvent) {
                    MouseEvent me = (MouseEvent) event;
                    
                    // Chỉ kích hoạt khi chuột nằm trong JScrollPane
                    if (!SwingUtilities.isDescendingFrom(me.getComponent(), jScrollPane)) {
                        return;
                    }
                    
                    // Bỏ qua nếu đang bấm vào nút bấm (vd: Thêm vào giỏ)
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
                            
                            // 2. NHÂN ĐÔI TỐC ĐỘ VUỐT CHẠM (Thêm * 2)
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