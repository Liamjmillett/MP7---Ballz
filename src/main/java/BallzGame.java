import java.awt.event.KeyEvent;
import java.io.File;
import java.awt.Graphics2D;
import java.awt.Image;
import edu.illinois.cs.cs125.lib.zen.Zen;
import java.util.Random;
/**
 * Class to play a game similar to the moile game "Ballz".
 * @author Liam Millett and Nick Miller
 */
public class BallzGame {
    public static boolean checkBounce(double x, double y, double vY, double vX, boolean checkEntrance) {
        for (int i = 0; i < squares.length; i++) {
            if (squares[i] != null && x > squares[i][0] && x < squares[i][0] + squares[i][2] && y > squares[i][1] && y < squares[i][1] + squares[i][2]) {
                System.out.println("got here");
                if(checkEntrance && checkBounce(x - vX*10, y, vX, vY, false)) {
                    System.out.println("got here y");
                    velocityX = velocityX * -1;
                }
                if(checkEntrance && checkBounce(x, y - vY*10, vX, vY, false)) {
                    System.out.println("got here x");
                   velocityY = velocityY * -1;
                }
                return true;
            }
        }


        return false;
     };
    public static boolean addSquare(int a, int b, int c, int d) {
        System.out.println(squares.length);
        if(numSquares <= 8) {
            squares[numSquares] = new int [] {a,b,c,d};
            numSquares += 1;
            return true;
        }
        return false;
    }
    public static int numSquares;
    public static void drawSquares() {
        for (int i = 0; i < squares.length; i++) {
            if (squares[i] != null) {
                Zen.fillRect(squares[i][0],squares[i][1],squares[i][2],squares[i][2]);
            }
        }
    }
     public static int [][] squares = new int[10][4];
     static double x = 300, y = 450;
     static double velocityX = 0;
     static double velocityY = -Math.sqrt((5 * 5) - (velocityX * velocityX));
     static boolean launched = false;



    /**
     * A game played by launching a ball. Tap/Press arrow keys to
     *     point to where you will shoot (left/right keys) &
     *     use SPACE to shoot.
     * @param unused
     */
    public static void main(final String[] unused) {
        Random rand = new Random();
        for(int i = 0; i < squares.length - 3; i++) {
            addSquare(rand.nextInt(300), rand.nextInt(300), 40, 1);
        }
        /*
         * Starting location and current speed of our bouncing ball.
         */
        int gameCount = 0;

        double x2 = 450 * Math.random(); // generate random square.
        double y2 = 440 * Math.random();


        /*
         * Get the path to our sprite image.
         */
        ClassLoader classLoader = BallzGame.class.getClassLoader();
        File arrowFile = new File(classLoader.getResource("arrow.png").getFile());
        Image image = Zen.getCachedImage(arrowFile.getAbsolutePath());

        while (Zen.isRunning()) {


            Zen.setColor(190, 240, 255); //blue-green
            Zen.fillRect((int) x2 + 20, (int) y2 + 20, 40, 40); // box
            Zen.fillRect(0, 460, Zen.getZenWidth(), Zen.getZenHeight()); //ground
            /*
             * Draw the ball.
             */
            Zen.setColor(255, 0, 0); // red
            Zen.fillOval((int) x, (int) y, 8, 8); //ball
            drawSquares();

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

                /*
                 * Arrow draw
                 * Get a new graphics buffer.
                 * Note that this has to be called again every time we flip
                 * buffers.
                 */
                Graphics2D graphicsBuffer = Zen.getBufferGraphics();
                graphicsBuffer.translate(x, 445);
                graphicsBuffer.rotate(-Math.acos(velocityX / 5), 0, 0);
                /*
                 * Scale the buffer smaller.
                 */
                graphicsBuffer.scale(0.04, 0.01);

                /*
                 * Draw our sprite at the current buffer origin.
                 */
                graphicsBuffer.drawImage(image, -8, 0, null);

                Zen.setColor(255, 255, 255);
                Zen.drawText("Horizontal Velocity = " + velocityX, 400, 20);
                Zen.drawText("Vertical Velocity = " + -velocityY, 400, 40);
                Zen.drawText("Angle (degree) = "
                          + Math.acos(velocityX / 5) * (180 / Math.PI), 400, 60);
                Zen.drawText("Balls launched: " + gameCount, 20, 20);

            }



            /*
             * Swap the background and foreground buffer, so the shifted image we created above is
             * now visible.
            */
            Zen.flipBuffer();
            //Zen.sleep(5);

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
                x = x + 0.1*velocityX;
                y = y + 0.1*velocityY;

                if (y >= 450) { //ball crosses lower bound, we launch a new ball.
                    gameCount++;
                    launched = false;
                    y = 450;
                    velocityX = 0;
                }
                checkBounce(x,y,velocityX,velocityY, true);

            }





        }
    }

}
