/**
 * Created by evanzyker on 25/11/15.
 */
public class ControlMaster {

    public Model model;
    public Vue vue;
    public ControlKey controlKey;
    public ControlMenu controlMenu;

    public ControlMaster(Model model){
        this.model = model;
        vue = new Vue(this.model);
        controlKey = new ControlKey(vue, this.model);
        controlMenu = new ControlMenu(vue, this.model);

        //Assignation des controllers
        vue.setControlKey(controlKey);
        vue.setControlMenu(controlMenu);

        //Lancement de la fenêtre
        vue.setVisible(true);
    }
}
