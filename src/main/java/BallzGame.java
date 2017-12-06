import java.awt.event.KeyEvent;
import java.io.File;
import java.awt.Graphics2D;
import java.awt.Image;
import edu.illinois.cs.cs125.lib.zen.Zen;

/**
 * Class to play a game similar to the moile game "Ballz".
 * @author Liam Millett and Nick Miller
 */
public class BallzGame {




    /**
     * A game played by launching a ball. Tap/Press arrow keys to
     *     point to where you will shoot (left/right keys) &
     *     use SPACE to shoot.
     * @param unused
     */
    public static void main(final String[] unused) {

        /*
         * Starting location and current speed of our bouncing ball.
         */
        double x = 300, y = 450;
        double velocityX = 0;
        double velocityY = -Math.sqrt((5 * 5) - (velocityX * velocityX));
        boolean launched = false;
        int gameCount = 0;


        /*
         * handle to arrow image. IN PROGRESS AND NOT FUNCTIONING
         */
        // ClassLoader classLoader = BallzGame.class.getClassLoader();
        //    File arrowFile = new File(classLoader.getResource("arrow.png").getFile());
        //    Image image = Zen.getCachedImage(arrowFile.getAbsolutePath());

        while (Zen.isRunning()) {
            /*
             * Draw the ball.
             */
            Zen.fillOval((int) x, (int) y, 8, 8);


            /*
             * Draw Arrow Image    ALSO IN PROGRESS AND NOT FUNCTIONING
             */
           // Zen.drawImage(image, 300, 450);
           // Graphics2D graphicsBuffer = Zen.getBufferGraphics();
           // graphicsBuffer.translate(50, 50);
           // graphicsBuffer.drawImage(image, 0, 0, null);

            /*
             * Change Ball's direction (by chainging horizontal velocity) before launching.
             * Ball's velocity is 5.
             */
            if (!launched) {

                if (Zen.isVirtualKeyPressed(KeyEvent.VK_LEFT) && velocityX > -4.8) {
                    velocityX -= 0.1;
                }
                if (Zen.isVirtualKeyPressed(KeyEvent.VK_RIGHT) && velocityX < 4.8) {
                    velocityX += 0.1;
                }
                velocityY = -Math.sqrt((5 * 5) - (velocityX * velocityX));
            }
             Zen.drawText("Horizontal Velocity = " + velocityX, 400, 20);
             Zen.drawText("Vertical Velocity = " + -velocityY, 400, 40);
             Zen.drawText("Angle (degree) = "
                       + Math.acos(velocityX / 5) * (180 / Math.PI), 400, 60);
             Zen.drawText("Balls launched: " + gameCount, 20, 20);


            /*
             * Swap the background and foreground buffer, so the shifted image we created above is
             * now visible.
            */
            Zen.flipBuffer();
            Zen.sleep(5);

            /*
             * Launch the ball.
             */
            if (Zen.isVirtualKeyPressed(KeyEvent.VK_SPACE)) {
                 launched = true;
            }
            /*
             * move and bounce off walls.
             */
            if (launched) {
                if (x > Zen.getZenWidth()) {
                    velocityX *= -1;
                }
                if (x < 0) {
                    velocityX *= -1;
                }
                if (y < 0.0) {
                    velocityY *= -1;
                }
                x = x + velocityX;
                y = y + velocityY;

                if (y >= 450) { //ball crosses lower bound, we launch a new ball.
                    gameCount++;
                    launched = false;
                    y = 450;
                    velocityX = 0;

                }


            }

        }
    }

}
