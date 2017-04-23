package at.htl.entity.dto;

import at.htl.entity.Result;

/**
 * Created by Lokal on 27.03.2017.
 */
public class PutMatchDto {
    private Result result;
    private boolean isActive;
    private String startTime;


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
