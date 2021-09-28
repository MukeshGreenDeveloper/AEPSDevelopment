package softwarepassanga.aeps1;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class StarTekResponce {
    @SerializedName("PidData")
    @Expose
    private PiddataEntity piddata;

    public PiddataEntity getPiddata() {
        return this.piddata;
    }

    public void setPiddata(PiddataEntity piddataEntity) {
        this.piddata = piddataEntity;
    }

    public static class PiddataEntity {
        @SerializedName("Data")
        @Expose
        private DataEntity data;
        @SerializedName("DeviceInfo")
        @Expose
        private DeviceinfoEntity deviceinfo;
        @SerializedName("Hmac")
        @Expose
        private String hmac;
        @SerializedName("Resp")
        @Expose
        private RespEntity resp;
        @SerializedName("Skey")
        @Expose
        private SkeyEntity skey;

        public DeviceinfoEntity getDeviceinfo() {
            return this.deviceinfo;
        }

        public void setDeviceinfo(DeviceinfoEntity deviceinfoEntity) {
            this.deviceinfo = deviceinfoEntity;
        }

        public String getHmac() {
            return this.hmac;
        }

        public void setHmac(String str) {
            this.hmac = str;
        }

        public SkeyEntity getSkey() {
            return this.skey;
        }

        public void setSkey(SkeyEntity skeyEntity) {
            this.skey = skeyEntity;
        }

        public DataEntity getData() {
            return this.data;
        }

        public void setData(DataEntity dataEntity) {
            this.data = dataEntity;
        }

        public RespEntity getResp() {
            return this.resp;
        }

        public void setResp(RespEntity respEntity) {
            this.resp = respEntity;
        }
    }

    public static class DeviceinfoEntity {
        @SerializedName("additional_info")
        @Expose
        private AdditionalInfoEntity additionalInfo;
        @SerializedName("dc")
        @Expose

        /* renamed from: dc */
        private String f65dc;
        @SerializedName("dpId")
        @Expose
        private String dpid;
        @SerializedName("mc")
        @Expose

        /* renamed from: mc */
        private String f66mc;
        @SerializedName("mi")
        @Expose

        /* renamed from: mi */
        private String f67mi;
        @SerializedName("rdsId")
        @Expose
        private String rdsid;
        @SerializedName("rdsVer")
        @Expose
        private String rdsver;

        public AdditionalInfoEntity getAdditionalInfo() {
            return this.additionalInfo;
        }

        public void setAdditionalInfo(AdditionalInfoEntity additionalInfoEntity) {
            this.additionalInfo = additionalInfoEntity;
        }

        public String getMc() {
            return this.f66mc;
        }

        public void setMc(String str) {
            this.f66mc = str;
        }

        public String getMi() {
            return this.f67mi;
        }

        public void setMi(String str) {
            this.f67mi = str;
        }

        public String getDc() {
            return this.f65dc;
        }

        public void setDc(String str) {
            this.f65dc = str;
        }

        public String getRdsver() {
            return this.rdsver;
        }

        public void setRdsver(String str) {
            this.rdsver = str;
        }

        public String getRdsid() {
            return this.rdsid;
        }

        public void setRdsid(String str) {
            this.rdsid = str;
        }

        public String getDpid() {
            return this.dpid;
        }

        public void setDpid(String str) {
            this.dpid = str;
        }
    }

    public static class AdditionalInfoEntity {
        @SerializedName("Param")
        @Expose
        private List<ParamEntity> param;

        public List<ParamEntity> getParam() {
            return this.param;
        }

        public void setParam(List<ParamEntity> list) {
            this.param = list;
        }
    }

    public static class ParamEntity {
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("value")
        @Expose
        private String value;

        public String getValue() {
            return this.value;
        }

        public void setValue(String str) {
            this.value = str;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String str) {
            this.name = str;
        }
    }

    public static class SkeyEntity {
        @SerializedName("ci")
        @Expose

        /* renamed from: ci */
        private Long f68ci;
        @SerializedName("content")
        @Expose
        private String content;

        public String getContent() {
            return this.content;
        }

        public void setContent(String str) {
            this.content = str;
        }

        public Long getCi() {
            return this.f68ci;
        }

        public void setCi(Long l) {
            this.f68ci = l;
        }
    }

    public static class DataEntity {
        @SerializedName("content")
        @Expose
        private String content;
        @SerializedName("type")
        @Expose
        private String type;

        public String getContent() {
            return this.content;
        }

        public void setContent(String str) {
            this.content = str;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String str) {
            this.type = str;
        }
    }

    public static class RespEntity {
        @SerializedName("errCode")
        @Expose
        private Object errcode;
        @SerializedName("errInfo")
        @Expose
        private String errinfo;
        @SerializedName("fCount")
        @Expose
        private Object fcount;
        @SerializedName("fType")
        @Expose
        private Object ftype;
        @SerializedName("nmPoints")
        @Expose
        private Object nmpoints;
        @SerializedName("qScore")
        @Expose
        private Object qscore;

        public Object getQscore() {
            return this.qscore;
        }

        public void setQscore(Object obj) {
            this.qscore = obj;
        }

        public Object getNmpoints() {
            return this.nmpoints;
        }

        public void setNmpoints(Object obj) {
            this.nmpoints = obj;
        }

        public Object getFtype() {
            return this.ftype;
        }

        public void setFtype(Object obj) {
            this.ftype = obj;
        }

        public Object getFcount() {
            return this.fcount;
        }

        public void setFcount(Object obj) {
            this.fcount = obj;
        }

        public String getErrinfo() {
            return this.errinfo;
        }

        public void setErrinfo(String str) {
            this.errinfo = str;
        }

        public Object getErrcode() {
            return this.errcode;
        }

        public void setErrcode(Object obj) {
            this.errcode = obj;
        }
    }
}
