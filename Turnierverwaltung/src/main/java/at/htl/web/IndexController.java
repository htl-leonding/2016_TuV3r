package at.htl.web;

import at.htl.entity.Team;
import at.htl.entity.Tournament;
import at.htl.logic.TeamFacade;
import at.htl.logic.TournamentFacade;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DualListModel;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJBAccessException;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeListener;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@RequestScoped
public class IndexController implements Serializable {
    @Inject
    TournamentFacade tournamentFacade;

    public Tournament getLatestTournament(){
        try {
            return tournamentFacade.findLatestTournament();
        }
        catch (EJBException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public List<Tournament> getTournaments(){
        try {
            return tournamentFacade.findAllTournaments();
        }
        catch (EJBException ex){
            ex.printStackTrace();
            return null;
        }
    }
    public List<Tournament> getClosedTournaments(){
        try {
            return tournamentFacade.findAllClosedTournaments();
        }
        catch (EJBException ex){
            ex.printStackTrace();
            return null;
        }
    }
}

