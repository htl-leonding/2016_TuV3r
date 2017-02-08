package at.htl.web;

import at.htl.entity.Team;
import at.htl.entity.Tournament;
import at.htl.logic.TeamFacade;
import at.htl.logic.TournamentFacade;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import at.htl.logic.TournamentSystems;
import org.omnifaces.util.Messages;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SlideEndEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
import javax.el.MethodExpression;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import javax.faces.application.NavigationHandler;

@Named
@SessionScoped
public class NewTournamentController implements Serializable {

    //private static final Logger logger = LogManager.getLogger(NewTournamentController.class);

    @Inject
    private TournamentFacade tournamentFacade;

    @Inject
    private TournamentSystems systems;

    @Inject
    private TeamFacade teamFacade;

    private DualListModel<String> types;
    private List<String> selectedTypes = new ArrayList<>();
    private int groupSize = 3;
    private int pointsWin = 3;
    private int pointsDraw = 1;
    private String tournamentName ="";
    private List<Team> teams = new ArrayList<>();
    private String tournamentSystem;
    private String groupPhaseIcon;
    private String newName;
    private List<Team> deleteTeams = new ArrayList<>();
    private String redirect="http://localhost:8080/Turnierverwaltung/currentTournament.xhtml";

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }

    public List<Team> getDeleteTeams() {
        return deleteTeams;
    }

    public void setDeleteTeams(List<Team> deleteTeams) {
        this.deleteTeams = deleteTeams;
    }

    public Tournament getLatestTournament() {
        return tournamentFacade.findLatestTournament();
    }

    public List<Tournament> getTournaments() {
        return tournamentFacade.findAllTournaments();
    }

    public List<Tournament> getClosedTournaments() {
        return tournamentFacade.findAllClosedTournaments();
    }

    /**
     * Fügt die verschiedenen Systeme in die Source-List ein
     */
    @PostConstruct
    public void setupPickList() {
        List<String> typesSource = new ArrayList<String>();
        List<String> typesTarget = new ArrayList<String>();

        typesSource.add("Gruppenphase");
        typesSource.add("KO-System");

        types = new DualListModel<>(typesSource, typesTarget);
    }

    /**
     * Wird aufgerufen, wenn bei der Picklist ein Element auf die andere Seite geschoben
     * wird und fügt das Element zur Liste hinzu. Gibt dann eine Message zurück.
     *
     * @param event
     */
    public void onTransfer(TransferEvent event) {
        FacesMessage msg = new FacesMessage();
        StringBuilder detail = new StringBuilder();

        for (String s : (List<String>) event.getItems()) {
            detail.append(s).append("<br />");
            if (!selectedTypes.contains(s)) {
                selectedTypes.add(s);
                msg.setSummary("Turnierart übertragen");
            } else {
                selectedTypes.remove(s);
                msg.setSummary("Turnierart entfernt");
            }
        }

        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        msg.setDetail(detail.toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    /***
     * Benachrichtigt den Benutzer, dass der Turniername geändert wurde
     */
    public void notify(String summary, String detail) {
        FacesMessage msg = new FacesMessage();
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        msg.setSummary(summary);
        msg.setDetail(detail);
        FacesContext.getCurrentInstance().addMessage(null,msg);
    }

    /***
     * Speichert die Daten und wechselt zur Turnierdurchführ-Seite
     * @param actionEvent
     */
    public void buttonAction(ActionEvent actionEvent) {
        persistInput();
        RequestContext requestContext = RequestContext.getCurrentInstance();
        requestContext.execute("window.open('"+ redirect +"','_self')");
    }

    /***
     * Speichert Informationen, die für ein Turnier essentiell sind
     */
    private void persistInput(){
        if(tournamentName.isEmpty()) setTournamentName("Turnier");
        Tournament tournament = new Tournament(tournamentName, LocalDate.now(), true,getPointsWin(),
                getPointsDraw(),getGroupSize(),selectedTypes.contains("Gruppenphase"),getTournamentSystem(), teams);
        tournament= tournamentFacade.save(tournament);
        for (Team team : teams) {
            team.setTournament(tournament);
            teamFacade.save(team);
        }
        teams = new ArrayList<>();
        tournamentName="";
    }
    public void onGroupSizeSlideEnd(SlideEndEvent event) {
        setGroupSize(event.getValue());
    }
    public void onPointsWinSlideEnd(SlideEndEvent event) {
        setPointsWin(event.getValue());
    }
    public void onPointsDrawSlideEnd(SlideEndEvent event) {
        setPointsDraw(event.getValue());
    }
    //endregion

    /***
     * Das Icon des Buttons wird verändert, abhängig davon, ob eine Gruppenphase gewählt wurde
     * @return
     */
    public String getGroupPhaseIcon() {
        return selectedTypes.contains("Gruppenphase") ? "ui-icon-check" : "ui-icon-close";
    }


    public String getTournamentSystem() {
        for (String s : selectedTypes) {
            if (!s.equals("Gruppenphase")) {
                for (String types : types.getSource()) {
                    if (types.equals(s))
                        return s;
                }
            }
        }
        return "KO-System";
    }

    public void setTournamentSystem(String tournamentSystem) {
        this.tournamentSystem = tournamentSystem;
    }

    public List<String> getSelectedTypes() {
        return selectedTypes;
    }

    public void setSelectedTypes(List<String> selectedTypes) {
        this.selectedTypes = selectedTypes;
    }

    public DualListModel<String> getTypes() {
        return types;
    }

    public void setTypes(DualListModel<String> types) {
        this.types = types;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }

    public int getPointsDraw() {
        return pointsDraw;
    }

    public void setPointsDraw(int pointsDraw) {
        this.pointsDraw = pointsDraw;
    }

    public int getPointsWin() {
        return pointsWin;
    }

    public void setPointsWin(int pointsWin) {
        this.pointsWin = pointsWin;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public void checkboxTicked(Team team){
        if(deleteTeams.contains(team)){
            deleteTeams.remove(team);
        }
        else{
            deleteTeams.add(team);
        }
    }
    public void deleteRows(ActionEvent ev){
        notify(deleteTeams.size()+" Teams","Entfernt");
        for (Team deleteTeam : deleteTeams) {
            teams.remove(deleteTeam);
        }
        deleteTeams = new ArrayList<>();
    }
    public void newColumn(ActionEvent ev){
        teams.add(new Team(getNewName(),false));
        notify("Team "+getNewName(),"Hinzugefügt");
    }
    public void handleKeyEvent() {
        //update key inputs
    }
}

