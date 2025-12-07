package Ham;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class App extends JFrame implements Runnable{

    //App class sets up the window and adds the GameObject Panel (which handles rendering)
    //set up window class in game files, NOT THE GAMEOBJECT CLASS
    //load images in

    GameObject game = new GameObject();
    public static Input input = new Input();

    public App() {

        String defaultTitle = "The Ham Engine";
        BufferedImage defaultIcon = loadImage("/Ham/defAssets/HamIcon.png");

        //set up window:
        this.setSize(1721, 1033);
        this.setTitle(defaultTitle);
        this.setIconImage(defaultIcon);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        //set up gameObject thread:
        this.add(game);
        Thread t2 = new Thread(game);
        t2.start();
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
    public BufferedImage loadImage(String path){
        BufferedImage img = null;

        try {
            URL imageUrl = getClass().getResource(path);
            assert imageUrl != null;
            img = ImageIO.read(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return img;
    }


    //manages Game Logic:
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
