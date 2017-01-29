package at.htl.gui;

import at.htl.entity.Match;
import at.htl.entity.Result;
import at.htl.entity.Team;
import at.htl.entity.Tournament;
import org.primefaces.json.JSONArray;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Philipp Krannich on 25.06.2016.
 */
public class JsonFunctions {

    public List<Team> toTeamList(String input) {
        String full = "{\"teams\": " + input + "}";
        JSONObject jsonObject = new JSONObject(full);
        JSONArray jsonArray = jsonObject.getJSONArray("teams");
        List<Team> teamList = new ArrayList<>();

        for (Object obj : jsonArray) {
            JSONObject curr = (JSONObject) obj;
            String name = curr.getString("name");
            int rank = curr.getInt("rank");
            int id = curr.getInt("id");
            boolean occupied = curr.getBoolean("occupied");
            //String group = curr.getString("group");
            Team team = new Team(name, occupied, rank);
            team.setId(id);
            teamList.add(team);
        }

        return teamList;
    }

    public List<Tournament> toTournamentList(String input) {
        String full = "{\"matches\": " + input + "}";
        JSONObject jsonObject = new JSONObject(full);
        JSONArray jsonArray = jsonObject.getJSONArray("matches");
        List<Tournament> tournamentList = new ArrayList<>();

        for (Object obj : jsonArray) {
            JSONObject parent = (JSONObject) obj;
            JSONObject tournament = parent.getJSONObject("tournament");

            int id = tournament.getInt("id");
            String name = tournament.getString("name");
            JSONObject dateObj = tournament.getJSONObject("date");
            int year = dateObj.getInt("year");
            int month = dateObj.getInt("monthValue");
            int day = dateObj.getInt("dayOfMonth");
            LocalDate date = LocalDate.parse(Integer.toString(day) + "." + Integer.toString(month) + "." + Integer.toString(year));

            boolean isActive = tournament.getBoolean("active");
            int pointsWin = tournament.getInt("pointsWin");
            int pointsDraw = tournament.getInt("pointsDraw");
            int groupsize = tournament.getInt("groupSize");
            boolean groupPhase = false;
            Object groupPhaseobj = tournament.get("groupPhase");
            if (groupPhaseobj != null) {
                groupPhase = Boolean.parseBoolean(groupPhaseobj.toString());
            }
            String system = "";
            Object systemObj = tournament.get("system");
            if (systemObj != null) {
                system = systemObj.toString();
            }

            JSONArray teams = tournament.getJSONArray("teams");
            List<Team> teamList = new ArrayList<>();

            for (Object team : teams) {
                JSONObject curr = (JSONObject) obj;

                int idC = curr.getInt("id");
                String nameC = curr.getString("name");
                int rankC = curr.getInt("rank");
                boolean occupiedC = curr.getBoolean("occupied");

                Team teamC = new Team(nameC, occupiedC, rankC);
                teamC.setId(idC);
                teamList.add(teamC);
            }

            Tournament tour1 = new Tournament(name, date, isActive, pointsWin, pointsDraw, groupsize, groupPhase, system, teamList);
            tour1.setId(id);

            boolean contains = false;
            for (Tournament tournament1 : tournamentList) {
                if (id == tournament1.getId()) {
                    contains = true;
                }
            }

            if (!contains) {
                tournamentList.add(tour1);
            }
        }
        return tournamentList;
    }

    public List<Match> toMatchList(String input) {
        String full = "{\"matches\": " + input + "}";
        JSONObject jsonObject = new JSONObject(full);
        JSONArray jsonArray = jsonObject.getJSONArray("matches");
        List<Match> matchList = new ArrayList<>();

        for (Object obj : jsonArray) {
            try {
                JSONObject curr = (JSONObject) obj;
                int id = curr.getInt("id");

                JSONObject team1 = curr.getJSONObject("team1");
                String team1Name = team1.getString("name");
                int rankTeam1 = team1.getInt("rank");
                int idTeam1 = team1.getInt("id");
                boolean occupiedTeam1 = team1.getBoolean("occupied");

                Team t1 = new Team(team1Name, occupiedTeam1, rankTeam1);
                t1.setId(idTeam1);

                JSONObject team2 = curr.getJSONObject("team2");
                String team2Name = team2.getString("name");
                int rankTeam2 = team2.getInt("rank");
                int idTeam2 = team2.getInt("id");
                boolean occupiedTeam2 = team2.getBoolean("occupied");

                Team t2 = new Team(team2Name, occupiedTeam2, rankTeam2);
                t2.setId(idTeam2);

                String result = curr.getString("result");
                boolean isActive = curr.getBoolean("active");

                Match m = new Match(isActive, t1, t2, new Result(result));
                matchList.add(m);
            } catch (JSONException ex) {

            }

        }

        return matchList;
    }
}
