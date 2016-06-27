package at.htl.gui;

import at.htl.entity.Match;
import at.htl.entity.Team;
import at.htl.entity.Tournament;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Philipp Krannich on 25.06.2016.
 */
public class JavaFXMainController implements Initializable {

    @FXML
    private ListView lv_teams, lv_matches, lv_tournaments;

    @FXML
    private TextField tv_id, tv_name, tv_haslost, tv_rank, tv_occupied;

    @FXML
    private Button bt_new;

    List<Team> teamList;
    List<Match> matchList;
    List<Tournament> tournamentList;

    Team selected;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        RestFunctions rf = new RestFunctions();
        String strTeams = rf.get("team");
        System.out.println(strTeams);

        String strMatches = rf.get("match");
        System.out.println(strMatches);

        try {
            String strTournaments = rf.get("tournament");
            System.out.println(strTournaments);

            this.tournamentList = new JsonFunctions().toTournamentList(strTournaments);

            int length = 1;
            String[] arrayTournaments;
            if (tournamentList.size() == 0) {
                arrayTournaments = new String[length];
            } else {
                arrayTournaments = new String[tournamentList.size()];
            }

            for (int i = 0; i < arrayTournaments.length; i++) {
                if (arrayTournaments.length == 0) {
                    arrayTournaments[i] = " KEINE TURNIERE VORHANDEN ";
                    break;
                }
                arrayTournaments[i] = "Turnier: " + tournamentList.get(i).getName() + " am " + tournamentList.get(i).getDate().toString() + " mit " + tournamentList.get(i).getTeams().size() + " Teilnehmern";

            }

            ObservableList<String> dataTournaments = FXCollections.observableArrayList(
                    arrayTournaments
            );
            lv_tournaments.setItems(dataTournaments);
        } catch (Exception e) {

        }


        JsonFunctions jf = new JsonFunctions();
        this.teamList = jf.toTeamList(strTeams);
        this.matchList = jf.toMatchList(strMatches);


        String[] arrayTeams = new String[teamList.size()];
        String[] arrayMatches = new String[matchList.size()];


        for (int i = 0; i < arrayTeams.length; i++) {
            arrayTeams[i] = teamList.get(i).getName();
        }

        for (int i = 0; i < arrayMatches.length; i++) {
            arrayMatches[i] = matchList.get(i).getTeam1().getName()
                    + " gegen " + matchList.get(i).getTeam2().getName() + " - "
                    + matchList.get(i).getResult();
        }


        ObservableList<String> dataTeams = FXCollections.observableArrayList(
                arrayTeams
        );

        ObservableList<String> dataMatches = FXCollections.observableArrayList(
                arrayMatches
        );

        lv_teams.setItems(dataTeams);
        lv_matches.setItems(dataMatches);

        lv_teams.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                // newValue + 1 = id
                Team t1 = teamList.get(newValue.intValue());
                selected = t1;
                change(t1);
            }
        });

        bt_new.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage createStage = new Stage();
                try {
                    createStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/create.fxml"))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                createStage.show();
            }
        });

    }


    private void change(Team team) {
        tv_id.setText(Long.toString(team.getId()));
        tv_name.setText(team.getName());
        tv_haslost.setText(Boolean.toString(team.HasLost()));
        tv_rank.setText(Integer.toString(team.getRank()));
        tv_occupied.setText(Boolean.toString(team.isOccupied()));
    }
}
