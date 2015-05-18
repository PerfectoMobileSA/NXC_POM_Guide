import static org.junit.Assert.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;

import nxcPOM.*;

public class MobileRemoteTest {

	public static void main(String[] args) throws IOException {

		System.out.println("Run started");
		String browserName = "mobileOS";
		DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "", Platform.ANY);
		String host = args[0];
		String user = URLEncoder.encode(args[1], "UTF-8");
		String password = URLEncoder.encode(args[2], "UTF-8");
		capabilities.setCapability("description", args[3]);
		capabilities.setCapability("platformName", "Android");
		//capabilities.setCapability("deviceName", args[3]);

		RemoteWebDriver driver = new RemoteWebDriver(new URL("https://" + user + ':' + password + '@' + host + "/nexperience/wd/hub"), capabilities);
		
		try {
         // write your code here
			
			NXCBaseView nxcView = new NXCBaseView(driver);
			
			// Load page, login and go to OCR Page
			nxcView.init().login("John", "Perfecto1").clickOcrLink();
			
			// Switch to visual driver, to perform text checkpoint
			switchToContext(driver, "VISUAL");
			
			// Perform the checkpoint
			driver.findElement(By.linkText("Normal Pressed Focused"));
			
			// Switch back to webview context
			switchToContext(driver, "WEBVIEW");
		
		} 
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Close the browser
			driver.close();
			
			// Download a pdf version of the execution report
			downloadReport(driver, "pdf", "C:\\temp\\report.pdf");
			
			// Release the driver
			driver.quit();
		}
		
		System.out.println("Run ended");
	}
	
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}
	
	/**
	 * Download the report. 
	 * type - pdf, html, csv, xml
	 * Example: downloadReport(driver, "pdf", "C:\\test\\report");
	 * 
	 */
	private static void downloadReport(RemoteWebDriver driver, String type, String fileName) throws IOException {
		try { 
			String command = "mobile:report:download"; 
			Map<String, Object> params = new HashMap<>(); 
			params.put("type", type); 
			String report = (String)driver.executeScript(command, params); 
			File reportFile = new File(fileName + "." + type); 
			BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(reportFile)); 
			byte[] reportBytes = OutputType.BYTES.convertFromBase64Png(report); 
			output.write(reportBytes); output.close(); 
		} catch (Exception ex) { 
			System.out.println("Got exception " + ex); }
		}
	
	/*
	// Load page
	nxcView = nxcView.init();
	
	// Put credentials and submit
	nxcView = nxcView.putUsername("John").putPassword("Perfecto1");
	nxcView = nxcView.submit();
	
	// Go to OCR Page. Casting necessary.
	nxcView = ((MainView) nxcView).clickOcrLink();
	*/

	/**
	 * Download all the report attachments with a certain type.
	 * type - video, image, vital, network
	 * Examples:
	 * downloadAttachment("video", "C:\\test\\video", "flv");
	 * downloadAttachment("image", "C:\\test\\Image", "jpg");
	 */
	private void downloadAttachment(RemoteWebDriver driver, String type, String fileName, String suffix) throws IOException {
		try {
			String command = "mobile:report:attachment";
			boolean done = false;
			int index = 0;

			while (!done) {
				Map<String, Object> params = new HashMap<>();	

				params.put("type", type);
				params.put("index", Integer.toString(index));

				String attachment = (String)driver.executeScript(command, params);
				
				if (attachment == null) { 
					done = true; 
				}
				else { 
					File file = new File(fileName + index + "." + suffix); 
					BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file)); 
					byte[] bytes = OutputType.BYTES.convertFromBase64Png(attachment);	
					output.write(bytes); 
					output.close(); 
					index++; }
			}
		} catch (Exception ex) { 
			System.out.println("Got exception " + ex); 
		}
	}


	private static void switchToContext(RemoteWebDriver driver, String context) {
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		Map<String,String> params = new HashMap<String,String>();
		params.put("name", context);
		executeMethod.execute(DriverCommand.SWITCH_TO_CONTEXT, params);
	}

	private String getCurrentContextHandle(RemoteWebDriver driver) {		  
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		String context =  (String) executeMethod.execute(DriverCommand.GET_CURRENT_CONTEXT_HANDLE, null);
		return context;
	}

	private List<String> getContextHandles(RemoteWebDriver driver) {		  
		RemoteExecuteMethod executeMethod = new RemoteExecuteMethod(driver);
		List<String> contexts =  (List<String>) executeMethod.execute(DriverCommand.GET_CONTEXT_HANDLES, null);
		return contexts;
	}

}
