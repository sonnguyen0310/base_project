package sng.com.base.model;

/**
 * Created by son.nguyen on 4/18/2016.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data<T> {

    @SerializedName("data")
    @Expose
    private T data;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("returnCode")
    @Expose
    private Integer returnCode;

    /**
     *
     * @return
     * The data
     */
    public T getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(T data) {
        this.data = data;
    }

    /**
     *
     * @return
     * The message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     * The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return
     * The returnCode
     */
    public Integer getReturnCode() {
        return returnCode;
    }

    /**
     *
     * @param returnCode
     * The returnCode
     */
    public void setReturnCode(Integer returnCode) {
        this.returnCode = returnCode;
    }

}