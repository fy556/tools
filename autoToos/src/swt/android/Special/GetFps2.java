package swt.android.Special;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class GetFps2 {
	//清空之前采样的数据，防止统计重复的时间
    private static String clearCommand = "adb shell dumpsys SurfaceFlinger --latency-clear";
    private static long jumpingFrames = 0; //jank次数，跳帧数
    private static long totalFrames = 0;  //统计的总帧数
    private static long appsumFps = 0;
    private static float lostFrameRate = 0; //丢帧率
    private static float fps; //fps值
    DateFormat dateFormat=new SimpleDateFormat();
    String deviceID;
    
    public static void main(String args[]) throws IOException, Exception{
    	GetFps2 getFps2=new GetFps2();
    
    	System.out.println("请输入测试次数(大约1.2s中获取一次信息");
    	Scanner scanner=new Scanner(System.in);
    	int n=scanner.nextInt();
    	String deviceID=getFps2.getdeviceID();
    	for(int i=0;i<n;i++){
    		appsumFps=0;
    		totalFrames = 0;
    		jumpingFrames = 0; 
    	
    		getInfo(deviceID, "com.aispeech.sample");
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
                        System.out.println("该帧所花费的时间为："+onceTime);
//                        System.out.println("理论该app 1s绘制帧数为:"+1000/onceTime);
//                        appsumFps+=(1000/onceTime);
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
            	System.out.println("vsyncOverTimes="+vsyncOverTimes);
//            	System.out.println("在该段时间内APP理论1s内平均绘制帧数为="+appsumFps/totalFrames);
            	
            	System.out.println("totalFrames="+totalFrames);
            	System.out.println("jumpingFrames="+jumpingFrames);
            	
            	long a=totalFrames + vsyncOverTimes;
//            	System.out.println("totalFrames + vsyncOverTimes="+a);
            	
                fps = ((float)totalFrames/(float)a)*60;
                System.out.println("fps="+fps);
//                fps = totalFrames;
                lostFrameRate = jumpingFrames / totalFrames;
                long SM=60-jumpingFrames;
//                long SM=60*(totalFrames-jumpingFrames)/totalFrames;
                System.out.println("SM="+SM);
//                System.out.println("lostFrameRate="+lostFrameRate);
                info[0] = fps;
//                info[1] = (float) Commons.streamDouble(lostFrameRate * 100);
            }else {
                System.err.println("无FPS信息");
            }
//            Thread.sleep(1000-(time2-time1));
            
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
    
    
    public String getdeviceID() throws Exception, IOException{
    	List<String> devicesID=new ArrayList<String>();
    	BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("adb devices").getInputStream(),"UTF-8"));
    	String s;
    	while ((s=bufferedReader.readLine())!=null) {
    		if(s.contains("device")&&!s.contains("attached")){
    			devicesID.add(s.trim().split("\\s+")[0]);
    		}
		}
    	if(devicesID.size()>0){
    		System.out.println("默认获取第一个设备的ID为："+devicesID.get(0));
    		deviceID=devicesID.get(0);
    	}
    	return deviceID;
    }
    
    public void get(String deviceName,String packageName){
    	String gfxCMD = "adb -s " + deviceName + " shell dumpsys SurfaceFlinger --latency " + packageName;
    }
    
}
