package at.htl.entity.dto;

import at.htl.entity.Result;

/**
 * Created by Lokal on 27.03.2017.
 */
public class PutMatchDto {
    private Result result;
    private boolean isActive;


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
