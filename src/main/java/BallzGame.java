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
                double m = .5;
                while(true) {
                    boolean withinXRange = false;
                    boolean withinYRange = false;
                  //  System.out.println(vX*m);
                    if(x - vX*m > squares[i][0] && x - vX*m < squares[i][0] + squares[i][2]) {
                        withinXRange = true;
                    }
                    if(y - vY*m > squares[i][1] && y - vY*m < squares[i][1] + squares[i][2]) {
                        withinYRange = true;
                    }
                    if(withinXRange && withinYRange) {
                        m = m * 1.1;
                       // System.out.println("increasing multiplier");
                    } else {
                        if(withinYRange) {
                            velocityY = velocityY * -1;
                            System.out.println("choosing vertical");
                            squares[i] = null;
                            return true;
                        }
                        if(withinXRange) {
                            velocityX = velocityX * -1;
                            System.out.println("choosing horizontal");
                            squares[i] = null;
                            return true;
                        }
                        if(!withinXRange && !withinYRange) {
                            velocityX = velocityX * -1;
                            System.out.println("choosing random");
                            squares[i] = null;
                            return true;
                        }
                    }

                }
            }
        }
        return false;
    }

    public static boolean checkGoal(double x, double y, double x2, double y2) {
        if (x <= (x2 + 30) && x >= x2 && y <= (y2 + 30) && y >= y2) {
            return true;
        }
        return false;
    }


    public static boolean addSquare(int a, int b, int c, int d) {
        if(numSquares >= 10){
            return false;
        }
        numSquares += 1;
        squares[numSquares] = new int [] {a,b,c,d};
        return true;
    }
    public static int numSquares;
    public static void drawSquares() {
        for (int i = 0; i < squares.length; i++) {
            if (squares[i] != null) {
                Zen.fillRect(squares[i][0],squares[i][1],squares[i][2],squares[i][2]);
            }
        }
    }
     public static int [][] squares = new int[40][4];
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
    public static void addRandomBoxes(int numBox){
        Random rand = new Random();
        for(int i = 0; i < numBox; i++) {
            System.out.println("adding box");
            addSquare(41 * rand.nextInt(14) + 20, 41 * rand.nextInt(5) + 100, 40, 1);
        }
    }
    public static void main(final String[] unused) { ///MAIN METHOD///
        Random rand = new Random();
        for(int i = 0; i < squares.length - 3; i++) {
            addSquare(41 * rand.nextInt(14) + 20, 41 * rand.nextInt(5) + 100, 40, 1);
        }
        /*
         * Starting location and current speed of our bouncing ball.
         */
        int gameCount = 0;
        int score = 0;

        double x2 = 450 * Math.random(); // generate random square.
        double y2 = 40 * Math.random();

        boolean hasHitGoal = false;




        /*
         * Get the path to our arrow image.
         */
        ClassLoader classLoader = BallzGame.class.getClassLoader();
        File arrowFile = new File(classLoader.getResource("arrow.png").getFile());
        Image image = Zen.getCachedImage(arrowFile.getAbsolutePath());

        addRandomBoxes(10);

        while (Zen.isRunning()) {

            Zen.setColor(255, 255, 255);
            Zen.drawText(score + " Points  /  " + gameCount + " Attempts", 30, 30);

            Zen.setColor(190, 240, 255); //blue-green
            Zen.fillRect((int) x2 + 20, (int) y2 + 20, 30, 30); // box
            Zen.fillRect(0, 460, Zen.getZenWidth(), Zen.getZenHeight()); //ground
            /*
             * Draw the ball.
             */
            Zen.setColor(255, 0, 0); // red
            Zen.fillOval((int) x - 4, (int) y - 4, 8, 8); //ball
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

                Zen.sleep(10);

                /*
                 * Arrow draw
                 * Get a new graphics buffer.
                 * Note that this has to be called again every time we flip
                 * buffers.
                 */
                Graphics2D graphicsBuffer = Zen.getBufferGraphics();
                graphicsBuffer.translate(x - 12, 430);
                graphicsBuffer.rotate(-Math.acos(velocityX / 5), 12, 4);
                /*
                 * Scale the buffer smaller.
                 */
                graphicsBuffer.scale(0.04, 0.01);

                /*
                 * Draw our sprite at the current buffer origin.
                 */
                graphicsBuffer.drawImage(image, 0, 0, null);

                //Zen.setColor(255, 255, 255);
                //Zen.drawText("Horizontal Velocity = " + velocityX, 400, 20);
                //Zen.drawText("Vertical Velocity = " + -velocityY, 400, 40);
                //Zen.drawText("Angle (degree) = "
                  //        + Math.acos(velocityX / 5) * (180 / Math.PI), 400, 60);
                //Zen.drawText("Balls launched: " + gameCount, 20, 20);

            }



            /*
             * Swap the background and foreground buffer, so the shifted image we created above is
             * now visible.
            */
            Zen.flipBuffer();


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
                x = x + 0.2 * velocityX;
                y = y + 0.2 * velocityY;

                if (y >= 450) { //ball crosses lower bound, we launch a new ball.
                    gameCount++;
                    launched = false;
                    y = 450;
                    velocityX = 0;
                }
                checkBounce(x, y, velocityX, velocityY, true);

                if (!hasHitGoal) {
                    hasHitGoal = checkGoal(x, y, x2, y2);
                }


                if (hasHitGoal && !launched) {
                    score += 1;
                    numSquares = 0;
                    addRandomBoxes(10);
                    x2 = 450 * Math.random();
                    y2 = 40 * Math.random();

                }

            }
            if (gameCount >= 15) {
                Zen.drawText("GAME OVER", Zen.getZenWidth() / 2, Zen.getZenHeight() / 2);
                Zen.drawText("Final Score: " + score,
                        Zen.getZenWidth() / 2, Zen.getZenHeight() / 2 + 30);
            }





        }
    }

}
