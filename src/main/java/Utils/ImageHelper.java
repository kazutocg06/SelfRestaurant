package Utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageHelper {
    
    public static ImageIcon getCroppedImage(String imageName, int targetWidth, int targetHeight) {
        try {
            File imgFile = new File("D:/DoAnPTTKTHTTT/SelfRestaurant/src/main/java/images/" + imageName);
            if (!imgFile.exists()) return null; 
            
            BufferedImage originalImage = ImageIO.read(imgFile);

            double scaleX = (double) targetWidth / originalImage.getWidth();
            double scaleY = (double) targetHeight / originalImage.getHeight();
            double scale = Math.max(scaleX, scaleY); 

            int scaledWidth = (int) (originalImage.getWidth() * scale);
            int scaledHeight = (int) (originalImage.getHeight() * scale);
            Image scaledImg = originalImage.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);

            BufferedImage roundedImg = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = roundedImg.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Area clipArea = new Area(new RoundRectangle2D.Float(0, 0, targetWidth, targetHeight, 16, 16));
            clipArea.add(new Area(new Rectangle2D.Float(0, targetHeight / 2.0f, targetWidth, targetHeight / 2.0f)));
            g2d.setClip(clipArea);

            int x = (targetWidth - scaledWidth) / 2;
            int y = (targetHeight - scaledHeight) / 2;

            g2d.drawImage(scaledImg, x, y, null);
            g2d.dispose();

            return new ImageIcon(roundedImg);
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}