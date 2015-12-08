import javax.swing.event.MenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by evanzyker on 04/12/15.
 */
public class ControlMenu implements ActionListener{

    private Model model;
    private Vue vue;

    public ControlMenu(Vue v, Model m) {
        vue = v;
        model = m;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vue.sizeLow){
            model.setGridSize(4);
            vue.redraw();
        }else if (e.getSource() == vue.sizeMedium){
            model.setGridSize(5);
            vue.redraw();
        }else if (e.getSource() == vue.sizeBig){
            model.setGridSize(6);
            vue.redraw();
        }else if (e.getSource() == vue.restart){
            model.setGridSize(4);
            vue.redraw();
        }
        vue.setControlMenu(this);
    }
}
