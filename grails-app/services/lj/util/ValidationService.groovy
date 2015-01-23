package lj.util

import lj.enumCustom.AuthCodeType
import lj.enumCustom.ReCode

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage

/*****
 * 验证服务
 * ******/
class ValidationService {

    def serviceMethod() {

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
     * @return 返回一个Map，成功时返回验证码字符串和验证码图片对象，失败是返回失败原因
     * [recode: [code:0,msg:"成功"],authCode:authCode,acImgObj:baos.toByteArray()];成功
     * [recode: [code:-2,msg:"未知错误"]];未知的错误
     * @Date: 13-10-11
     * @Time: 下午12:18
     */
    @Override
    def getauthCode(params) {
        try {
            if (!params) {
                return [recode: ReCode.NO_ENOUGH_PARAMS];
            }
            Random random = new Random();
            String baseCode="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            String baseCode1="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
            String baseCode2="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            String baseCode3="abcdefghijklmnopqrstuvwxyz0123456789";
            String baseCode4="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            String baseCode5="abcdefghijklmnopqrstuvwxyz";
            String baseCode6="0123456789";
            Color[] colors=[Color.BLUE,Color.GREEN,Color.ORANGE,Color.PINK,Color.RED];
            int edgeDistance=5;
            int digit=lj.Number.toInteger(params.digit);//验证码的位数
            int width=lj.Number.toInteger(params.width);//图片宽
            int height=lj.Number.toInteger(params.height);//图片高
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

            //生成随机数
            String authCode="";
            StringBuffer temp=new StringBuffer("");
            for(int i=0;i<digit;i++){
                int idx=new Random().nextInt(26*2+10);
                //println("idx:"+idx);
                temp.append(baseCode.getChars()[idx]);

            }
            authCode=temp.toString();

            BufferedImage tag = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = (Graphics2D) tag.getGraphics();
            g2.setBackground(backGround);
            g2.clearRect(0, 0, width, height);
            g2.setColor(Color.BLUE);
            //绘制干扰线
            g2.setColor(getRandColor(160,200));
            for (int i = 0; i < 155; i++) {
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                int xl = random.nextInt(12);
                int yl = random.nextInt(12);
                g2.drawLine(x, y, x + xl, y + yl);
            }

            //将随机生成的字符串写入到图片对象中
            //g2.setFont(new Font("Serif", Font.ITALIC|Font.BOLD, fontSize));
            g2.setFont(new Font("Times New Roman", Font.HANGING_BASELINE, fontSize));
            //一个一个字符的输出到图片对象
            for(int i=0;i<authCode.length();i++){
                //int idx=new Random().nextInt(colors.length);
                //g2.setColor(colors[idx]);
                g2.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));
                g2.drawString(new String(authCode.getChars()[i]),edgeDistance+i*charWidth,height-2*edgeDistance);
            }
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            ImageIO.write(tag,"PNG",baos);
            return [recode: ReCode.OK,authCode:authCode,acImgObj:baos.toByteArray()];
        }
        catch (Exception ex){
            return [recode: ReCode.OTHER_ERROR];
        }
    }
    Color getRandColor(int fc, int bc) {// 给定范围获得随机颜色
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
    /*获取一个验证码字符串
    params.digit 验证码位数
    params.codeType 验证码类型,数字0到6
    */
    def getAuthCodeStr(def params){
        try {
            def baseCode=["ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789",
            "abcdefghijklmnopqrstuvwxyz0123456789",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
            "abcdefghijklmnopqrstuvwxyz",
            "0123456789"] as String[];

            int digit=lj.Number.toInteger(params.digit);//验证码的位数
            int codeType=lj.Number.toInteger(params.codeType);//验证码类型
            if(digit<=0){
                digit=4;
            }
            if(codeType<0||codeType>=baseCode.length){
                codeType=AuthCodeType.UPPERCASE_LAWERCASE_NUMBER;
            }
            int charCount= 26*2+10;
            int baseCodeIndex=0;
            switch (codeType){
                case AuthCodeType.UPPERCASE_LAWERCASE_NUMBER:
                    charCount= 26*2+10;
                    baseCodeIndex=0;
                    break;
                case AuthCodeType.UPPERCASE_LAWERCASE:
                    charCount= 26*2;
                    baseCodeIndex=1;
                    break;
                case AuthCodeType.UPPERCASE_NUMBER:
                    charCount= 26+10;
                    baseCodeIndex=2;
                    break;
                case AuthCodeType.LAWERCASE_NUMBER:
                    charCount= 26+10;
                    baseCodeIndex=3;
                    break;
                case AuthCodeType.UPPERCASE:
                    charCount= 26;
                    baseCodeIndex=4;
                    break;
                case AuthCodeType.LAWERCASE:
                    charCount= 26;
                    baseCodeIndex=5;
                    break;
                case AuthCodeType.NUMBER:
                    charCount= 10;
                    baseCodeIndex=6;
                    break;
            }
            //生成随机数
            String authCode="";
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
            return [recode: ReCode.OK,authCode:authCode];
        }
        catch (Exception ex){
            return [recode: ReCode.OTHER_ERROR];
        }
    }

}
