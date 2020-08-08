package swt.android.Special;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class GetFps {
	//清空之前采样的数据，防止统计重复的时间
    private static String clearCommand = "adb shell dumpsys SurfaceFlinger --latency-clear";
    private static long jumpingFrames = 0; //jank次数，跳帧数
    private static long totalFrames = 0;  //统计的总帧数
    private static float lostFrameRate = 0; //丢帧率
    private static float fps; //fps值
    DateFormat dateFormat=new SimpleDateFormat();
    
    public static void main(String args[]) throws InterruptedException{
    
    	System.out.println("请输入测试次数(大约1.2s中获取一次信息");
    	Scanner scanner=new Scanner(System.in);
    	int n=scanner.nextInt();
    	
    	for(int i=0;i<n;i++){
    		totalFrames = 0;
    		jumpingFrames = 0; 
    	
    		getInfo("1672010062", "com.aispeech.sample");
		
    		
    	}
    	
    }
    

    public static float[] getInfo(String deviceName, String packageName) throws InterruptedException{
        String gfxCMD = "adb -s " + deviceName + " shell dumpsys gfxinfo " + packageName;
        float[] info = new float[2];
        int vsyncOverTimes = 0; // 垂直同步次数
        try {
            Runtime.getRuntime().exec(clearCommand);
            long time1=new Date().getTime();
            Process process = Runtime.getRuntime().exec(gfxCMD);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(),"UTF-8"));
            String line; 
            int i=0;
            boolean flag = false;
            while ((line = reader.readLine()) != null){
            	i++;
//            	System.out.println("i="+i+","+line);
                if (line.length() > 0){
                    if (line.contains("Execute")){
                        flag = true;
                        continue;
                    }
                    if (line.contains("View hierarchy")){
                        break;
                    }
                    if(line.contains(packageName)){
                    	continue;
                    }
                    if (flag){
                    	
                    	System.out.println("i="+i+","+line);
                        String[] times = line.trim().split("\\s+");
                        
                        //计算一帧所花费的时间
                        float onceTime = Float.parseFloat(times[0]) + Float.parseFloat(times[1]) + Float.parseFloat(times[2]) + Float.parseFloat(times[3]);
                        totalFrames += 1; //统计总帧数
                        if (onceTime > 16.67){//以Android定义的60FPS为标准
                            jumpingFrames += 1; // 统计跳帧jank数
                            //统计额外花费垂直同步脉冲的数量
                            if (onceTime % 16.67 == 0){
                                vsyncOverTimes += onceTime / 16.67 - 1;
                            }else {
                                vsyncOverTimes += Math.floor(onceTime / 16.67); //向下取整即可
                            }
                        }
                    }
                }
            }
            long time2=new Date().getTime();
//            System.out.println("time2-time1="+(time2-time1));
            if (totalFrames > 0){
            	
            	System.out.println("totalFrames="+totalFrames);
            	System.out.println("jumpingFrames="+jumpingFrames);
            	
            	long a=totalFrames + vsyncOverTimes;
//            	System.out.println("totalFrames + vsyncOverTimes="+a);
            	
                fps = ((float)totalFrames/(float)a)*60;
                System.out.println("fps="+fps);
//                fps = totalFrames;
                lostFrameRate = jumpingFrames / totalFrames;
//                long SM=60-jumpingFrames;
                long SM=60*(totalFrames-jumpingFrames)/totalFrames;
                System.out.println("SM="+SM);
               
//                System.out.println("lostFrameRate="+lostFrameRate);
                info[0] = fps;
                
//                info[1] = (float) Commons.streamDouble(lostFrameRate * 100);
            }else {
                System.err.println("无FPS信息");
            }
            Thread.sleep(1000-(time2-time1));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return info;
    }

    public static double getFps(String deviceName,String packageName) throws InterruptedException{
        return getInfo(deviceName,packageName)[0];
    }

    public static double getLostFrameRate(String deviceName,String packageName) throws InterruptedException{
        return getInfo(deviceName,packageName)[1];
    }
}
