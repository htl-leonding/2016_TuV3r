package at.htl.gui;

import at.htl.entity.Team;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Philipp Krannich on 26.06.2016.
 */
public class JavaFXCreateController implements Initializable {
    @FXML
    private TextField tf_name, tf_rank;

    @FXML
    private CheckBox cb_haslost;

    @FXML
    private Button bt_create;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        RestFunctions rf = new RestFunctions();
        String str = rf.get("team");

        JsonFunctions jf = new JsonFunctions();
        List<Team> teamList = jf.toTeamList(str);

        long lastId = teamList.get(teamList.size() - 1).getId();

        bt_create.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String name = tf_name.getText();
                int rank = Integer.parseInt(tf_rank.getText());
                boolean haslost = false;
                if (cb_haslost.isSelected()) {
                    haslost = true;
                }

                Team t1 = new Team(name, haslost, rank);
                t1.setId(lastId + 1);
                rf.post(t1, "team");

                Stage s = (Stage) bt_create.getScene().getWindow();
                s.close();
            }
        });
    }
}
