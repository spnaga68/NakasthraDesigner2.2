package pasu.nakshatraDesigners.utils;

public class NetworkState {
    public final Status status;
    public final String msg;
    public static final NetworkState LOADED;
    public static final NetworkState LOADING;
    public static final NetworkState MAXPAGE;
    public enum Status {
        RUNNING,
        SUCCESS,
        FAILED,
        MAX
    }
    public   NetworkState(Status status, String msg) {
        this.status = status;
        this.msg = msg;
    }
     static {
        LOADED=new NetworkState(Status.SUCCESS,"Success");
        LOADING=new NetworkState(Status.RUNNING,"Running");
        MAXPAGE=new NetworkState(Status.MAX,"No More page");
    }
    public Status getStatus() {
        return status;
    }
    public String getMsg() {
        return msg;
    }
}
