package com.itit.util;

import ch.qos.logback.classic.Logger;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 视频的帧数页的截取
 */
public class VideoUtil {
    private static final Logger log  = (Logger) LoggerFactory.getLogger("test");
    /**
     * <h5>功能:获取一张视频截图并保存同名的jpg文件到视频同目录下</h5>
     *
     * @param filePath 视频文件全路径
     * @return
     */
    public static Map<String, Object> getScreenshot(String filePath,int GET_FRAMES_LENGTH) {
        return getScreenshot(filePath, null,GET_FRAMES_LENGTH);
    }

    /**
     * <h5>功能:获取一张视频截图并保存同名的jpg文件到指定目录</h5>
     *
     * @param filePath 视频文件地址
     * @param saveImagePath 截图的图片存放路径(绝对路径,不包含文件名称)
     * @return
     */
    public static Map<String,Object> getScreenshot(String filePath,String saveImagePath,int GET_FRAMES_LENGTH){
        log.info("开始截取",filePath);

        Map<String,Object> result = new HashMap<String, Object>();
        FFmpegFrameGrabber grabber;
        try{
            grabber = FFmpegFrameGrabber.createDefault(filePath);
            //视频路径
            String targerFilePath = filePath.substring(0, filePath.lastIndexOf("\\"));
            //文件名
            String FileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
            System.out.println(FileName);
            //图片名称
            String headerName = FileName.substring(0,FileName.lastIndexOf("."));
            grabber.start();
            int videoLength = grabber.getLengthInFrames();
            System.out.println(videoLength);
            Frame frame = null;
            int i = 0;
            while (i<videoLength){
                // 过滤前5帧,避免出现全黑的图片,依自己情况而定(每循环一次取一帧)
                frame = grabber.grabFrame();
                if((i>GET_FRAMES_LENGTH) && (frame.image != null)){
                    break;
                }
                i++;
            }

            Java2DFrameConverter converter = new Java2DFrameConverter();
            //绘制图片
            BufferedImage bi = converter.getBufferedImage(frame);

            //获取存放图片文件路径
            String imagePath = targerFilePath + File.separator + headerName + ".jpg";
            if(null != saveImagePath && !saveImagePath.trim().equals("")){
                imagePath = saveImagePath +File.separator +  headerName + ".jpg";
            }
            File file = new File(imagePath);
            ImageIO.write(bi,"jpg",file);



            // 拼接Map信息
            result.put("videoLength", videoLength); // 视频总帧数
            result.put("videoWide", bi.getWidth()); // 视频的宽
            result.put("videoHigh", bi.getHeight());// 频的高
            long duration = grabber.getLengthInTime() / (1000 * 1000); // 此视频时长(s/秒)
            result.put("format", grabber.getFormat()); // 视频的格式
            result.put("imgPath", file.getPath());
            result.put("duration", duration);

            grabber.stop();
            log.info("视频文件[{}]截图结束,图片地址为[{}]", filePath, imagePath);
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
