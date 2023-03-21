import java.io.Serializable;

/**
 * @Author: hmq
 */

public class ServerResponse<T> implements Serializable {

    private ResponseCode responseCode;
    private T data;

    public ServerResponse() {
    }

    public ServerResponse(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public ServerResponse(ResponseCode responseCode, T data) {
        this.responseCode = responseCode;
        this.data = data;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

