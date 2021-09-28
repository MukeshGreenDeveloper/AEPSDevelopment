package softwarepassanga.aeps1;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

public class GetBiometricDeviceMappingbycustomerId implements Parcelable {
    public static final Creator<GetBiometricDeviceMappingbycustomerId> CREATOR = new Creator<GetBiometricDeviceMappingbycustomerId>() {
        public GetBiometricDeviceMappingbycustomerId createFromParcel(Parcel parcel) {
            return new GetBiometricDeviceMappingbycustomerId(parcel);
        }

        public GetBiometricDeviceMappingbycustomerId[] newArray(int i) {
            return new GetBiometricDeviceMappingbycustomerId[i];
        }
    };
    @SerializedName("BiometricDeviceMappingId")
    private Long mBiometricDeviceMappingId;
    @SerializedName("BiometricDeviceNameId")
    private Long mBiometricDeviceNameId;
    @SerializedName("CSRID")
    private String mCSRID;
    @SerializedName("CustomerId")
    private Long mCustomerId;
    @SerializedName("DeviceID")
    private String mDeviceID;
    @SerializedName("BiometricDeviceName")
    private String mDeviceName;
    @SerializedName("MappingStatus")
    private Boolean mMappingStatus;
    @SerializedName("ModelNo")
    private String mModelNo;
    @SerializedName("SubmittedOn")
    private String mSubmittedOn;
    @SerializedName("TerminalID")
    private String mTerminalID;

    public int describeContents() {
        return 0;
    }

    public String getmDeviceName() {
        return this.mDeviceName;
    }

    public void setmDeviceName(String str) {
        this.mDeviceName = str;
    }

    public Long getBiometricDeviceMappingId() {
        return this.mBiometricDeviceMappingId;
    }

    public void setBiometricDeviceMappingId(Long l) {
        this.mBiometricDeviceMappingId = l;
    }

    public Long getBiometricDeviceNameId() {
        return this.mBiometricDeviceNameId;
    }

    public void setBiometricDeviceNameId(Long l) {
        this.mBiometricDeviceNameId = l;
    }

    public String getCSRID() {
        return this.mCSRID;
    }

    public void setCSRID(String str) {
        this.mCSRID = str;
    }

    public Long getCustomerId() {
        return this.mCustomerId;
    }

    public void setCustomerId(Long l) {
        this.mCustomerId = l;
    }

    public String getDeviceID() {
        return this.mDeviceID;
    }

    public void setDeviceID(String str) {
        this.mDeviceID = str;
    }

    public Boolean getMappingStatus() {
        return this.mMappingStatus;
    }

    public void setMappingStatus(Boolean bool) {
        this.mMappingStatus = bool;
    }

    public String getModelNo() {
        return this.mModelNo;
    }

    public void setModelNo(String str) {
        this.mModelNo = str;
    }

    public String getSubmittedOn() {
        return this.mSubmittedOn;
    }

    public void setSubmittedOn(String str) {
        this.mSubmittedOn = str;
    }

    public String getTerminalID() {
        return this.mTerminalID;
    }

    public void setTerminalID(String str) {
        this.mTerminalID = str;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(this.mBiometricDeviceMappingId);
        parcel.writeValue(this.mBiometricDeviceNameId);
        parcel.writeString(this.mCSRID);
        parcel.writeValue(this.mCustomerId);
        parcel.writeString(this.mDeviceID);
        parcel.writeValue(this.mMappingStatus);
        parcel.writeString(this.mModelNo);
        parcel.writeString(this.mSubmittedOn);
        parcel.writeString(this.mTerminalID);
        parcel.writeString(this.mDeviceName);
    }

    public GetBiometricDeviceMappingbycustomerId() {
    }

    protected GetBiometricDeviceMappingbycustomerId(Parcel parcel) {
        this.mBiometricDeviceMappingId = (Long) parcel.readValue(Long.class.getClassLoader());
        this.mBiometricDeviceNameId = (Long) parcel.readValue(Long.class.getClassLoader());
        this.mCSRID = parcel.readString();
        this.mCustomerId = (Long) parcel.readValue(Long.class.getClassLoader());
        this.mDeviceID = parcel.readString();
        this.mMappingStatus = (Boolean) parcel.readValue(Boolean.class.getClassLoader());
        this.mModelNo = parcel.readString();
        this.mSubmittedOn = parcel.readString();
        this.mTerminalID = parcel.readString();
        this.mDeviceName = parcel.readString();
    }
}
