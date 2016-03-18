package at.htl.web;

import at.htl.entity.Team;
import at.htl.entity.Tournament;
import at.htl.logic.TournamentFacade;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.SlideEndEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DualListModel;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@SessionScoped
@Named
public class NewTournamentController implements Serializable {
    @Inject
    TournamentFacade tournamentFacade;


    private DualListModel<String> types;
    private List<String> selectedTypes = new ArrayList<String>();
    List<String> typesSource = new ArrayList<String>();
    List<String> typesTarget = new ArrayList<String>();
    private int teamCount = 10;
    private int groupSize = 3;
    private int pointsWin = 3;
    private int pointsDraw = 1;
    private List<Team> teams;
    private String tournamentSystem;


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

        typesSource.add("Gruppenphase");
        typesSource.add("Leitersystem");
        typesSource.add("Schweizersystem");
        typesSource.add("Doppel-KO-System");
        typesSource.add("KO-System");

        types = new DualListModel<String>(typesSource, typesTarget);
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
                msg.setSummary("Items Transferred");
            } else {
                selectedTypes.remove(s);
                msg.setSummary("Items removed");
            }
        }

        msg.setSeverity(FacesMessage.SEVERITY_INFO);

        msg.setDetail(detail.toString());

        FacesContext.getCurrentInstance().addMessage(null, msg);

    }

    /*public List<Team> updateTeamList(){
        for (int i = 1; i < teamCount+1; i++) {
            teams.add(new Team("Team "+i,false));
        }
        System.out.println(getSelectedTypes().size()+"-"+getTeamCount()+"-"+getGroupSize()+"-"+getPointsDraw());
        return teams;
    }*/
    public void onSlideEnd(SlideEndEvent event) {
        System.out.println("**************************** slideEnd: " + teamCount + " / " + event.getValue());
        getTeams();
    }
    /*
    public void onSelect(SelectEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Selected", event.getObject().toString()));
    }

    public void onUnselect(UnselectEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Unselected", event.getObject().toString()));
    }

    public void onReorder() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "List Reordered", null));
    }

    <p:commandButton id="typesSubmit" value="Submit"
                                 update="displayTypes"
                                 oncomplete="PF('typesDialog').show()"
                                 style="margin-top:5px"/>
                <p:dialog modal="true" showEffect="fade" hideEffect="fade" widgetVar="typesDialog" styleClass="center">
                    <h:panelGrid id="displayTypes" columns="2">
                        <h:outputText value="Ausgewählte Turniersysteme: " style="font-weight:bold"/>
                        <ui:repeat value="#{newTournamentController.selectedTypes}" var="item">
                            <h:outputText value="#{item}" style="margin-right:5px"/>
                        </ui:repeat>
                    </h:panelGrid>
                </p:dialog>
*/


    public String determineGroupPhaseIcon() {
        if (selectedTypes.contains("Gruppenphase"))
            return "ui-icon-check";
        return "ui-icon-close";
    }

    public String getTournamentSystem() {
        for (String s : selectedTypes) {
            if (!s.equals("Gruppenphase")) {
                for (String types : typesSource) {
                    if (types.equals(s))
                        return s;
                }
            }
        }
        return "none";
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

    public int getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(int teamCount) {
        this.teamCount = teamCount;
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
        System.out.println("******************************* getTeams " + teamCount);
        teams = new ArrayList<Team>();
        for (int i = 1; i < teamCount + 1; i++) {
            teams.add(new Team("Team " + i, false));
        }
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
}
