package in.bananaa.pass.dto.scan;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import in.bananaa.pass.dto.GenericResponse;

public class ScanResponse extends GenericResponse implements Serializable {

    @SerializedName("member")
    private Member member;

    @SerializedName("allowed")
    private boolean allowed;

    @SerializedName("reason")
    private String reason;

    @SerializedName("startDate")
    private String startDate;

    @SerializedName("endDate")
    private String endDate;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        allowed = allowed;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
