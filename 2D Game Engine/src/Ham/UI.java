package Ham;

import javax.swing.JPanel;
import java.awt.Image;
import java.util.ArrayList;

public class UI extends JPanel {
    ArrayList<Integer> layer = new ArrayList<>();
    ArrayList<String> ID = new ArrayList<>();
    ArrayList<Image[][]> imgData = new ArrayList<>();
    ArrayList<Integer> imgX = new ArrayList<>();
    ArrayList<Integer> imgY = new ArrayList<>();
    ArrayList<Integer> x = new ArrayList<>();
    ArrayList<Integer> y = new ArrayList<>();
    ArrayList<Integer> width = new ArrayList<>();
    ArrayList<Integer> height = new ArrayList<>();
    ArrayList<Boolean> visible = new ArrayList<>();

    public void createUI(int layer, String ID, Image[][] imgData, int x, int y, int width, int height) {
        this.layer.add(layer);
        this.ID.add(ID);
        this.imgData.add(imgData);
        this.x.add(x);
        this.y.add(y);
        this.width.add(width);
        this.height.add(height);
        visible.add(false);
        imgX.add(0);
        imgY.add(0);
    }

    public void update(double dt) {

    }

}
