package tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Tools {
	public void execCmd(String cmd) {
		System.out.println(cmd);  
		try {  
			Process p = Runtime.getRuntime().exec(cmd);  
			InputStream input = p.getInputStream();  
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String s = null;
			while((s=reader.readLine())!=null){
				System.out.println(s);
			}
			reader.close();
		} catch (IOException e) {  
			e.printStackTrace();  
		}  
	}
	
	public  static void main(String agrs[]){
		Tools tools=new Tools();
//		String cmd=
		tools.execCmd("python E:\\Study\\data\\py\\2.py");
	}
}
