package Ham;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import static Ham.App.input;

public class GameObject extends JPanel implements Runnable{

    //object data
    public ArrayList<Integer> layer = new ArrayList<>();
    public ArrayList<String> ID = new ArrayList<>();
    public ArrayList<Image[][]> img = new ArrayList<>();
    public ArrayList<Integer> width = new ArrayList<>();
    public ArrayList<Integer> height = new ArrayList<>();
    public ArrayList<Integer> x = new ArrayList<>();
    public ArrayList<Integer> velX = new ArrayList<>();
    public ArrayList<Integer> y = new ArrayList<>();
    public ArrayList<Integer> velY = new ArrayList<>();
    public ArrayList<Integer> speed = new ArrayList<>();
    public ArrayList<Boolean> moving = new ArrayList<>();
    public ArrayList<Integer> dir = new ArrayList<>();
    public ArrayList<Double> animSpeed = new ArrayList<>();
    public ArrayList<Double> animTime = new ArrayList<>();
    public ArrayList<Integer> animFrame = new ArrayList<>();
    public ArrayList<Rectangle> hitbox = new ArrayList<>();
    public ArrayList<Double> time = new ArrayList<>();


    //layer data:
    ArrayList<Integer> layerZeroIndex = new ArrayList<>();
    ArrayList<Integer> layerOneIndex = new ArrayList<>();
    ArrayList<Integer> layerTwoIndex = new ArrayList<>();

    //global vars:
    public static int ammoCount = 5;
    public static int frame = 0;

    //UI (no duh):
    UI ui = new UI();

    public void createObject(int layer, String ID, Image[][] img ,int x, int y, int speed,int width, int height, int hitWidth, int hitHeight, int dir, double animSpeed) {

        int ind = this.layer.size();
        switch (layer) {
            case (0):
                layerZeroIndex.add(ind);
                break;
            case (1):
                layerOneIndex.add(ind);
                break;
            case (2):
                layerTwoIndex.add(ind);
                break;
        }

        this.layer.add(layer);
        this.ID.add(ID);
        this.img.add(img);
        this.x.add(x);
        this.y.add(y);
        this.speed.add(speed);
        this.width.add(width);
        this.height.add(height);
        this.dir.add(dir);
        this.animSpeed.add(animSpeed);
        moving.add(false);
        animTime.add(0d);
        animFrame.add(0);
        velX.add(0);
        velY.add(0);
        Rectangle rect = new Rectangle(x, y, hitWidth, hitHeight);
        hitbox.add(rect);
        time.add(0.0);

    }

    public void removeObject(int ind) {
        switch (layer.get(ind)) {
            case (0):
                layerZeroIndex.remove(ind);
                break;
            case (1):
                layerOneIndex.remove(ind);
                break;
            case (2):
                layerTwoIndex.remove(ind);
                break;
        }

        layer.remove(ind);
        ID.remove(ind);
        img.remove(ind);
        x.remove(ind);
        y.remove(ind);
        speed.remove(ind);
        width.remove(ind);
        height.remove(ind);
        dir.remove(ind);
        moving.remove(ind);
        animSpeed.remove(ind);
        animTime.remove(ind);
        animFrame.remove(ind);
        velX.remove(ind);
        velY.remove(ind);
        hitbox.remove(ind);
        time.remove(ind);
    }


    public void update(double dt) {
        frame++;
        System.out.println("FPS: " + 1/dt);
        System.out.println("Object Count: " + layer.size());

        for (int ind = 0; ind < layer.size(); ind++) {
            moveObject(dt, ind);
        }
    }

    public void moveObject(double dt, int ind){
        int x = this.x.get(ind);
        int y = this.y.get(ind);

        //angled movement math:
        int tarX = x + velX.get(ind);
        int tarY = y + velY.get(ind);
        double distX = x - tarX;
        double distY = y - tarY;
        double dist = Math.sqrt(distX * distX + distY * distY);

        if (dist > 0) {
            distX /= dist;
            distY /= dist;
        }

        this.x.set(ind, x + (int)(velX.get(ind) * dt * distX));
        this.y.set(ind, y + (int)(velY.get(ind) * dt * distY));

        //get rect:
        Rectangle newRect = this.hitbox.get(ind);
        newRect.x = x;
        newRect.y = y;
        hitbox.set(ind, newRect);

        //collision detection:
        if (layer.get(ind) == 0) {
            for (int layerInd : layerZeroIndex) {
                if (hitbox.get(ind).intersects(hitbox.get(layerInd))) {

                }
            }
        } else if (layer.get(ind) == 1) {
            for (int layerInd : layerOneIndex) {
                if (hitbox.get(ind).intersects(hitbox.get(layerInd))) {

                }
            }
        } else if (layer.get(ind) == 2) {
            for (int layerInd : layerOneIndex) {
                if (hitbox.get(ind).intersects(hitbox.get(layerInd))) {

                }
            }
        } else {
            System.err.println("Layer Error: Layer not Found\nObject num = " + ind);
        }

    }


    public void updatePlayer(double dt) {
        int ind = ID.indexOf("p1");
        int speed = this.speed.get(ind);

        //up-down:
        if (input.keys[KeyEvent.VK_W]) {
            velY.set(ind, -speed);
            this.dir.set(ind, 3);
        }
        if (input.keys[KeyEvent.VK_S]) {
            velY.set(ind, speed);
            this.dir.set(ind, 2);
        }
        if (!input.keys[KeyEvent.VK_W] && !input.keys[KeyEvent.VK_S]) {
            velY.set(ind, 0);
        }

        //left-right:
        if (input.keys[KeyEvent.VK_A]) {
            velX.set(ind, -speed);
            this.dir.set(ind, 1);
        }
        if (input.keys[KeyEvent.VK_D]) {
            velX.set(ind, speed);
            this.dir.set(ind, 0);
        }
        if (!input.keys[KeyEvent.VK_D] && !input.keys[KeyEvent.VK_A]) {
            velX.set(ind, 0);
        }

        //move check
        if (!input.keys[KeyEvent.VK_D] && !input.keys[KeyEvent.VK_A] && !input.keys[KeyEvent.VK_W] && !input.keys[KeyEvent.VK_S]) {
            this.moving.set(ind, false);
        } else {
            this.moving.set(ind, true);
        }

        //gun:
        if (input.keys[KeyEvent.VK_E] && ammoCount > 0) {
            int width = this.width.get(ind) /2;
            int height = this.height.get(ind) / 2;
            //createObject(1, "bull", null, x.get(ind) + height, y.get(ind) + width, 700, 5, 5, dir.get(ind), 0);
        }

        //animFrame
        if (moving.get(ind)) {
            double time = animTime.get(ind);
            animTime.set(ind, time + dt);
            if (animTime.get(ind) >= animSpeed.get(ind)) {
                if (animFrame.get(ind) == 2) {
                    animFrame.set(ind, 1);
                } else {
                    animFrame.set(ind, 2);
                }
                animTime.set(ind, 0d);
            }
        } else {
            animTime.set(ind, 0d);
        }

        //reset hitbox
        Rectangle rect = new Rectangle(x.get(ind) + 20, y.get(ind) + 10, width.get(ind) -20, height.get(ind) - 10);
        hitbox.set(ind, rect);


    }

    public void updateZomb(int ind, double dt) {

            int indP = this.ID.indexOf("p1");
            int px = this.x.get(indP);
            int py = this.y.get(indP);

            int dir = this.dir.get(ind);

            //advanced movement tech
            double distX = px - x.get(ind);
            double distY = py - y.get(ind);
            double dist = Math.sqrt(distX * distX + distY * distY);
            if (dist > 0) {
                distX /= dist;
                distY /= dist;
            }

            int speed = this.speed.get(ind);

            int deadZone = -50;

            //startup control
            moving.set(ind, true);


            //move updates:
            if (dir < 4) {

                //up-down:
                if (y.get(ind) < py - deadZone && y.get(ind) > py + deadZone) {
                    this.velY.set(ind, 0);
                } else if (y.get(ind) < py || y.get(ind) > py) {
                    if (y.get(ind) < py) {
                        this.velY.set(ind, speed);
                        this.dir.set(ind, 2);
                    } else if (y.get(ind) > py + deadZone) {
                        velY.set(ind, speed);
                        this.dir.set(ind, 3);
                    }
                }

                //right-left:
                if (x.get(ind) < px - deadZone && x.get(ind) > px + deadZone) {
                    this.velX.set(ind, 0);
                } else if (x.get(ind) < px || x.get(ind) > px) {
                    if (x.get(ind) < px) {
                        this.velX.set(ind, speed);
                        this.dir.set(ind, 0);
                    } else if (x.get(ind) > px) {
                        this.velX.set(ind, speed);
                        this.dir.set(ind, 1);
                    }
                }
            }

            //animation control:
            double time = animTime.get(ind);
            animTime.set(ind, time + dt);
            if (animTime.get(ind) >= animSpeed.get(ind)) {
                int maxFrame;
                if (dir >= 4) {
                    maxFrame = 3;
                } else {
                    maxFrame = 2;
                }
                if (animFrame.get(ind) < maxFrame) {
                    int newFrame = animFrame.get(ind) + 1;
                    animFrame.set(ind, newFrame);
                } else {
                    if (dir == 4) {
                        this.dir.set(ind, 1);
                        animFrame.set(ind, 1);
                    } else if (dir == 5) {
                        animSpeed.set(ind, 10000.0);
                        animFrame.set(ind, 3);
                    } else {
                        animFrame.set(ind, 1);
                    }
                }
                animTime.set(ind, 0d);
            }

            //hit reg:
            if (dir < 4) {
                Rectangle rect = new Rectangle(x.get(ind) + 20, y.get(ind) + 10, width.get(ind) - 20, height.get(ind) - 10);
                Rectangle rectP = hitbox.get(indP);
                if (rect.intersects(rectP)) {
                    this.time.set(ind, 0d);
                    this.dir.set(ind, 5);
                    velX.set(ind, 0);
                    velY.set(ind, 0);
                    animTime.set(ind, 0d);
                    animFrame.set(ind, 0);

                } else {
                    hitbox.set(ind, rect);
                }
            }

            this.time.set(ind, this.time.get(ind) + dt);
            if (dir == 5 && this.time.get(ind) >= 8) {
                removeObject(ind);
            }
    }

    @Override
    public void run() {
        while (true) {
            repaint();
            try {
                Thread.sleep(16);
            } catch (Exception e) {}
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        ArrayList<Integer> layerZeroIndex = new ArrayList<>();
        ArrayList<Integer> layerOneIndex = new ArrayList<>();
        ArrayList<Integer> layerTwoIndex = new ArrayList<>();

        for (int ind = 0; ind < layer.size(); ind++) {
            int layer = this.layer.get(ind);
            switch (layer) {
                case (0):
                    layerZeroIndex.add(ind);
                    break;
                case (1):
                    layerOneIndex.add(ind);
                    break;
                case (2):
                    layerTwoIndex.add(ind);
                    break;
            }
        }

        for (int ind : layerZeroIndex) {
            Image[][] img = this.img.get(ind);
            int frame = animFrame.get(ind);
            g.drawImage(img[frame][dir.get(ind)], x.get(ind), y.get(ind), width.get(ind), height.get(ind), null);
        }
        for (int ind : layerOneIndex) {
            Image[][] img = this.img.get(ind);
            int frame = animFrame.get(ind);
            g.drawImage(img[frame][dir.get(ind)], x.get(ind), y.get(ind), width.get(ind), height.get(ind), null);
        }
        for (int ind : layerTwoIndex) {
            Image[][] img = this.img.get(ind);
            int frame = animFrame.get(ind);
            g.drawImage(img[frame][dir.get(ind)], x.get(ind), y.get(ind), width.get(ind), height.get(ind), null);
        }

        //load UI:
        ArrayList<Integer> layerUI = ui.layer;
        ArrayList<String> idUI = ui.ID;
        ArrayList<Image[][]> imgUI = ui.imgData;
        ArrayList<Integer> imgX = ui.imgX;
        ArrayList<Integer> imgY = ui.imgY;
        ArrayList<Integer> xUI = ui.x;
        ArrayList<Integer> yUI = ui.y;
        ArrayList<Integer> widthUI = ui.width;
        ArrayList<Integer> heightUI = ui.height;
        ArrayList<Boolean> visibleUI = ui.visible;

        ArrayList<Integer> layerZeroUI = new ArrayList<>();
        ArrayList<Integer> layerOneUI = new ArrayList<>();

        for (int ind = 0; ind < layerUI.size(); ind++) {
            switch (layerUI.get(ind)) {
                case (0):
                    layerZeroUI.add(ind);
                case (1):
                    layerOneUI.add(ind);
            }
        }

        //render UI:
        for (int ind : layerZeroUI) {
            if (visibleUI.get(ind)) {
                Image[][] img = imgUI.get(ind);
                g.drawImage(img[imgY.get(ind)][imgX.get(ind)], xUI.get(ind), yUI.get(ind), widthUI.get(ind), heightUI.get(ind), null);
            }
        }
        for (int ind : layerOneUI) {
            if (visibleUI.get(ind)) {
                Image[][] img = imgUI.get(ind);
                g.drawImage(img[imgY.get(ind)][imgX.get(ind)], xUI.get(ind), yUI.get(ind), widthUI.get(ind), heightUI.get(ind), null);
            }
        }
    }
}