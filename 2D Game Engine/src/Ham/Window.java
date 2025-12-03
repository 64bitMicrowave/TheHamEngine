package Ham;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Window extends JFrame implements Runnable{

    GameObject game = new GameObject();
    static BufferedImage[][] playerImg = null;
    static BufferedImage[][] zombImg = null;
    public static Input input = new Input();

    public Window() {
        //set up window:
        this.setSize(1721, 1033);
        this.setTitle("2D Game Engine");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        //set up gameObject thread:
        this.add(game);
        Thread t2 = new Thread(game);
        t2.start();

        //set up player image:
        BufferedImage img = loadImage("assets/YellowMan.png");
        playerImg = spriteSheetSetup(img, 16, 16, 4, 4);
        game.createObject(
                1,
                "p1",
                playerImg,
                this.getWidth()/2,
                this.getHeight()/2,
                300,
                100,
                100,
                0,
                0.3
        );

        //set up zomb boi:
        img = loadImage("assets/zombSheet.png");
        zombImg = spriteSheetSetup(img, 16, 16, 5, 4);

    }

    //Sets up sprite sheet:
    public static BufferedImage[][] spriteSheetSetup(BufferedImage img, int width, int height, int numRows, int numCol) {

        BufferedImage[][] returnImg = new BufferedImage[numCol][numRows];

        for (int i = 0; i < numCol; i++) {
            for (int j = 0; j < numRows; j++) {
                returnImg[i][j] = img.getSubimage(i * width, j * height, width, height);
            }
        }

        return returnImg;
    }

    //loads image (i don't want to type it 100 times, ok?)
    public static BufferedImage loadImage(String path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    @Override
    public void run() {


        addKeyListener(input);

        while (true) {
            game.update(getDT());
            try {
                Thread.sleep(16);
            } catch (Exception e) {}
        }
    }

    //Delta Time
    public static Long lastTime = System.nanoTime();
    public static double getDT() {
        long now = System.nanoTime();
        double deltaTime = (now - lastTime) / 1_000_000_000.0f;
        lastTime = now;
        return deltaTime;
    }

}
