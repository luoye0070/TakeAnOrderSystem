package lj.common

import javax.imageio.ImageIO
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.image.BufferedImage

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-11
 * Time: 下午4:50
 * To change this template use File | Settings | File Templates.
 */
class ValidationCode {
    public static final int UPPERCASE_LAWERCASE_NUMBER=0; //大写字母小写字母数字
    public static final int UPPERCASE_LAWERCASE=1;         //大写字母小写字母
    public static final int UPPERCASE_NUMBER=2;            //大写字母数字
    public static final int LAWERCASE_NUMBER=3;           //小写字母数字
    public static final int UPPERCASE=4;                   //大写字母
    public static final int LAWERCASE=5;                   //小写字母
    public static final int NUMBER=6;                       //数字
    /*获取一个验证码字符串
    params.digit 验证码位数
    params.codeType 验证码类型,数字0到6
    */
    public static String getAuthCodeStr(int digit,int codeType){
        String authCode=null;
        try {
            def baseCode=["ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789",
                    "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz",
                    "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789",
                    "abcdefghijklmnopqrstuvwxyz0123456789",
                    "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
                    "abcdefghijklmnopqrstuvwxyz",
                    "0123456789"] as String[];

            if(digit<=0){
                digit=4;
            }
            if(codeType<0||codeType>=baseCode.length){
                codeType=UPPERCASE_LAWERCASE_NUMBER;
            }
            int charCount= 26*2+10;
            int baseCodeIndex=0;
            switch (codeType){
                case UPPERCASE_LAWERCASE_NUMBER:
                    charCount= 26*2+10;
                    baseCodeIndex=0;
                    break;
                case UPPERCASE_LAWERCASE:
                    charCount= 26*2;
                    baseCodeIndex=1;
                    break;
                case UPPERCASE_NUMBER:
                    charCount= 26+10;
                    baseCodeIndex=2;
                    break;
                case LAWERCASE_NUMBER:
                    charCount= 26+10;
                    baseCodeIndex=3;
                    break;
                case UPPERCASE:
                    charCount= 26;
                    baseCodeIndex=4;
                    break;
                case LAWERCASE:
                    charCount= 26;
                    baseCodeIndex=5;
                    break;
                case NUMBER:
                    charCount= 10;
                    baseCodeIndex=6;
                    break;
            }
            //生成随机数
            StringBuffer temp=new StringBuffer("");
            for(int i=0;i<digit;i++){
                int idx=new Random().nextInt(charCount);
                //println("idx:"+idx);
                if(idx>=baseCode[baseCodeIndex].length()){
                    idx=0;
                }
                temp.append(baseCode[baseCodeIndex].getChars()[idx]);
            }
            authCode=temp.toString();
            return authCode;
        }
        catch (Exception ex){
            println("getAuthCodeStr error:"+ex.message)
            return authCode;
        }
    }
    /**
     * 2.7.1	验证码生成
     * <p>生成验证码数字和图片，这个地方是用于需要输入验证码的地方</p>
     * @author 刘兆国
     * @version 2013-10-11
     * @param params 传入的参数map
     * params.digit int 验证码的位数，非必须，默认为4
     * params.width int 生成的验证码图片宽度，非必须，默认为60
     * params.height int 生成的验证码图片的高度，非必须，默认为30
     * params.backGround Color 生成的验证码图片的背景，非必须，默认为 Color.WHITE
     * params.codeType 验证码类型,数字0到6
     * @return 返回一个Map，成功时返回验证码字符串和验证码图片对象，失败是返回失败原因
     * [recode: [code:0,msg:"成功"],authCode:authCode,acImgObj:baos.toByteArray()];成功
     * [recode: [code:-2,msg:"未知错误"]];未知的错误
     * @Date: 13-10-11
     * @Time: 下午12:18
     */
    public static def getauthCode(params) {
        try {
            if (!params) {
                return [recode: [code:-1,msg:"缺少参数"]];
            }
            Color[] colors=[Color.BLUE,Color.GREEN,Color.ORANGE,Color.PINK,Color.RED];
            int edgeDistance=5;
            int digit=Number.toInteger(params.digit);//验证码的位数
            int codeType=Number.toInteger(params.codeType);//验证码类型
            int width=Number.toInteger(params.width);//图片宽
            int height=Number.toInteger(params.height);//图片高
            Color backGround=Color.WHITE; //背景颜色
            if(params.backGround){
                backGround=params.backGround;
            }
            if(digit<=0){
                digit=4;
            }
            if(width<=0){
                width=60;
            }
            if(height<=0){
                height=30;
            }
            //获取每一个字符的宽度
            int charWidth=(width-2*edgeDistance)/digit;
//        int fontSize=charWidth;
//        if(charWidth<(height-edgeDistance*2)){
//            fontSize=charWidth;
//        }
//        else{
//            fontSize=height-edgeDistance*2;
//        }
            int fontSize=height-edgeDistance*2;

            //生成验证码
            String authCode=getAuthCodeStr(digit,codeType);

            //将随机生成的字符串写入到图片对象中
            BufferedImage tag = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = (Graphics2D) tag.getGraphics();
            g2.setBackground(backGround);
            g2.clearRect(0, 0, width, height);
            g2.setColor(Color.BLUE);

            //绘制干扰线
            g2.setColor(getRandColor(160,200));
            Random random = new Random();
            for (int i = 0; i < 155; i++) {
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                int xl = random.nextInt(12);
                int yl = random.nextInt(12);
                g2.drawLine(x, y, x + xl, y + yl);
            }

            g2.setFont(new Font("Serif", Font.ITALIC|Font.BOLD, fontSize));
            //一个一个字符的输出到图片对象
            for(int i=0;i<authCode.length();i++){
                int idx=new Random().nextInt(colors.length);
                g2.setColor(colors[idx]);
                g2.drawString(new String(authCode.getChars()[i]),edgeDistance+i*charWidth,height-2*edgeDistance);
            }
            //ByteArrayOutputStream baos=new ByteArrayOutputStream();
            //ImageIO.write(tag,"PNG",baos);
            //return [recode: [code:0,msg:"成功"],authCode:authCode,acImgObj:baos.toByteArray()];
            return [recode: [code:0,msg:"成功"],authCode:authCode,acImgObj:tag];
        }
        catch (Exception ex){
            println("getAuthCode error:"+ex.message)
            return [recode:[code:-2,msg:"未知错误"]];
        }
    }
    static Color getRandColor(int fc, int bc) {// 给定范围获得随机颜色
        Random random = new Random();
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}
