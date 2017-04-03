package at.htl.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.ws.Endpoint;

/**
 * Created by Laurenz on 10.12.2015.
 */
public class Result {
    private int pointsFirstTeam;
    private int pointsSecondTeam;

    public Result(String res) {
        String[] results =  res.split(":");
        pointsFirstTeam = Integer.parseInt(results[0]);
        pointsSecondTeam = Integer.parseInt(results[1]);
    }

    public Result(int pointsFirstTeam, int pointsSecondTeam) {
        this.pointsFirstTeam = pointsFirstTeam;
        this.pointsSecondTeam = pointsSecondTeam;
    }

    public Result() {
    }

    public int getPointsFirstTeam() {
        return pointsFirstTeam;
    }

    public void setPointsFirstTeam(int pointsFirstTeam) {
        this.pointsFirstTeam = pointsFirstTeam;
    }

    public int getPointsSecondTeam() {
        return pointsSecondTeam;
    }

    public void setPointsSecondTeam(int pointsSecondTeam) {
        this.pointsSecondTeam = pointsSecondTeam;
    }

    @Override
    public String toString() {
        return pointsFirstTeam + ":"+pointsSecondTeam;
    }
}
