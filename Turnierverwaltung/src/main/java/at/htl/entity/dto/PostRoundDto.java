package at.htl.entity.dto;

/**
 * Created by Lokal on 23.04.2017.
 */
public class PostRoundDto {
    private String name;
    private long toId;

    public PostRoundDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
        this.toId = toId;
    }
}
