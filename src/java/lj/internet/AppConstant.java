package lj.internet;

public interface AppConstant {

    /*
     * 文件相关常量
     * */
//	class DirNames
//	{
//		public final static String APP_DIR="mxtool"+File.separator;
//		public final static String PHOTO_DIR=APP_DIR+"photos"+File.separator;
//	}
//	class FileNames
//	{
//		public final static String PHOTO_NAME_TEMP="photoName.temp";
//		public final static String LOCATION_INFO_TEMP="locationInfo.temp";
//		public final static String PHOTO_INDEX_FILE="photoIndex.txt";
//	}
    class VisitServerResultCode
    {
        public final static int RESULT_CODE_OK=0;//成功
        public final static int RESULT_CODE_NETWORKERROR=1;//网络错误
        public final static int RESULT_CODE_OTHERERROR=2;//其他错误
        public final static int RESULT_CODE_STOPBYUSER=3;//用户取消请求
        public final static int RESULT_CODE_SERVERERRROR=4;//服务器错误
        public final static int RESULT_CODE_FILENOTFOUND=5;//请求的资源找不到

    }
    class HttpParamRe
    {
        public final static String PARAM_NAME="paramName";
        public final static String PARAM_VALUE="paramValue";
    }
//	class SharedPreferenceName
//	{
//		public final static String PHOTO_INFO="photoInfo";
//		public final static String TEMP_PHOTO_PATH="tempPhotoPath";
//		public final static String TEMP_PHOTO_LOCATION_INFO="tempLocationInfo";
//	}
}
