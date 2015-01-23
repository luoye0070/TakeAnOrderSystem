package lj.internet.model;

public class UploadResult {
    @Override
    public String toString() {
        return "UploadResult [path=" + path + ", size=" + size + ", ctime="
                + ctime + ", mtime=" + mtime + ", md5=" + md5 + ", fs_id="
                + fs_id + "]";
    }
    public String path="";//	string	是	该文件的绝对路径。
    public long size=0;//	uint64	否	文件字节大小。
    public long ctime=0;//	uint64	否	文件创建时间。
    public long mtime=0;//	uint64	否	文件修改时间。
    public String md5="";//	string	否	文件的md5签名。
    public long fs_id=0;//	uint64	否	文件在PCS的临时唯一标识ID。
    public UploadResult(String path, long size, long ctime, long mtime,
                        String md5, long fs_id) {
        super();
        this.path = path;
        this.size = size;
        this.ctime = ctime;
        this.mtime = mtime;
        this.md5 = md5;
        this.fs_id = fs_id;
    }
    public UploadResult() {
        super();
    }

}
