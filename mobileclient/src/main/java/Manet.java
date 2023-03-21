import java.util.Date;

public class Manet {
    private Integer netid;

    private Boolean netstatus;

    private Date startTime ;

    private Integer capacity;

    private Integer connectAmount;

    public Integer getNetid() {
        return netid;
    }

    public void setNetid(Integer netid) {
        this.netid = netid;
    }

    public Boolean getNetstatus() {
        return netstatus;
    }

    public void setNetstatus(Boolean netstatus) {
        this.netstatus = netstatus;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getConnectAmount() {
        return connectAmount;
    }

    public void setConnectAmount(Integer connectAmount) {
        this.connectAmount = connectAmount;
    }
}