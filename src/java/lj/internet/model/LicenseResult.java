package lj.internet.model;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 15-6-14
 * Time: 上午12:23
 * To change this template use File | Settings | File Templates.
 */
public class LicenseResult {
    protected long  id;//id
    protected String licenseSerial;//标示的序列化字符串
    protected boolean enable;//是否有效
    protected int expire;//有效期时间，单位天
    protected Date createTime;//创建时间
    protected Date lastTime;//最后访问时间
    protected long  remoteId;//饭店ID

    private static LicenseResult licenseResult=null;
    public static LicenseResult getInstance(){
        if(licenseResult==null){
            licenseResult=new LicenseResult();
        }
        return licenseResult;
    }
    private LicenseResult(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRemoteId() {
        return remoteId;
    }
    public void setRemoteId(long remoteId) {
        this.remoteId = remoteId;
    }
    public String getLicenseSerial() {
        return licenseSerial;
    }
    public void setLicenseSerial(String licenseSerial) {
        this.licenseSerial = licenseSerial;
    }
    public boolean isEnable() {
        return enable;
    }
    public void setEnable(boolean enable) {
        this.enable = enable;
    }
    public int getExpire() {
        return expire;
    }
    public void setExpire(int expire) {
        this.expire = expire;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getLastTime() {
        return lastTime;
    }
    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }
}
