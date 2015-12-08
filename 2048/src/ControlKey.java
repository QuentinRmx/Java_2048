import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by evanzyker on 25/11/15.
 */
public class ControlKey implements KeyListener {

    public Vue vue;
    public Model model;
    private final int DOWN = 40;
    private final int UP = 38;
    private final int LEFT = 37;
    private final int RIGHT = 39;
    private final int DEBUG = 96;

    public ControlKey(Vue vue, Model model) {
        this.vue = vue;
        this.model = model;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case UP:
                model.move("up");
                break;
            case DOWN:
                model.move("down");
                break;
            case LEFT:
                model.move("left");
                break;
            case RIGHT:
                model.move("right");
                break;
            case DEBUG:
                //model.triggerWin();
                break;
        }
        vue.refresh();
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
