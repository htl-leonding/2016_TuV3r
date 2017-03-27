package at.htl.entity.dto;

import at.htl.entity.Result;

/**
 * Created by Lokal on 27.03.2017.
 */
public class PostMatchDto {
    private Result result;
    private long team1Id;
    private long team2Id;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public long getTeam1Id() {
        return team1Id;
    }

    public void setTeam1Id(int team1Id) {
        this.team1Id = team1Id;
    }

    public long getTeam2Id() {
        return team2Id;
    }

    public void setTeam2Id(int team2Id) {
        this.team2Id = team2Id;
    }
}
