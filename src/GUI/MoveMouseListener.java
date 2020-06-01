package GUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.*;
 
public class MoveMouseListener implements MouseListener, MouseMotionListener {
    JComponent target;
    JFrame frame;
    java.awt.Point start_drag;
    java.awt.Point start_loc;
 
    public MoveMouseListener(JComponent target, JFrame frame) {
        this.target = target;
        this.frame = frame;
    }
 
    public static JFrame getFrame(Container target) {
        if (target instanceof JFrame) {
            return (JFrame) target;
        }
        return getFrame(target.getParent());
    }
 
    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {
        this.start_drag = this.getScreenLocation(e);
        this.start_loc = this.getFrame(this.target).getLocation( );
    }
    public void mouseReleased(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {
        java.awt.Point current = this.getScreenLocation(e);
        java.awt.Point offset = new java.awt.Point(
                (int)current.getX( )-(int)start_drag.getX( ),
                (int)current.getY( )-(int)start_drag.getY( ));
        JFrame frame = this.getFrame(target);
        java.awt.Point new_location = new java.awt.Point(
                (int)(this.start_loc.getX( )+offset.getX( )),
                (int)(this.start_loc.getY( )+offset.getY( )));
        frame.setLocation(new_location);
    }
 
    java.awt.Point getScreenLocation(MouseEvent e) {
        java.awt.Point cursor = e.getPoint( );
        java.awt.Point target_location = this.target.getLocationOnScreen( );
        return new java.awt.Point(
                (int)(target_location.getX( )+cursor.getX( )),
                (int)(target_location.getY( )+cursor.getY( )));
    }
}