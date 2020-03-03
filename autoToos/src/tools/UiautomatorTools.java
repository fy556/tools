package tools;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiSelector;

public class UiautomatorTools {
	
	
	public UiObject getObject_id(String resourceId){
		return new UiObject(new UiSelector().resourceId(resourceId));
	}
	
	public UiObject getObject_text(String text){
		return new UiObject(new UiSelector().text(text));
	}
	
	public UiObject getObject_className(String className){
		return new UiObject(new UiSelector().className(className));
	}


}
