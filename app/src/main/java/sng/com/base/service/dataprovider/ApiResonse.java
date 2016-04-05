package sng.com.base.service.dataprovider;

/**
 * Created by son.nguyen on 3/19/2016.
 */
public class ApiResonse<T> {
    private String message;
    private T data;
    private String code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
