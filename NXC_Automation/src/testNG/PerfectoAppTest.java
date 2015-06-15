package testNG;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

import nxcPOM.*;
import utils.PerfectoUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

import com.perfectomobile.selenium.util.EclipseConnector;

public class PerfectoAppTest {
  private RemoteWebDriver driver;
 
 @Test
public void f() {
    boolean res = false;
    NXCBaseView view = new NXCBaseView(this.driver);
    try{
        view.init().login("john", "Perfecto1");
        res = driver.findElement(By.xpath("//*[contains(text(),'Welcome "                                         + "back'])")).isDisplayed();
     }
     catch(Exception e){}

    Assert.assertTrue(res);
}
  
  @BeforeClass 
  public void beforeClass(){
    System.out.println("Run started");
    
    String browserName = "mobileOS";
    DesiredCapabilities capabilities = new DesiredCapabilities(browserName, "",Platform.ANY);
    
    String user = URLEncoder.encode("myPerfectoUsername", "UTF-8");
    String password = URLEncoder.encode("myPassword", "UTF-8");

    capabilities.setCapability("model", "iPhone-6");
    
    // Uncomment this block if you want to use    
    // a device which is opened in the recorder window     /* 
    try { 
        EclipseConnector connector = new EclipseConnector();
        String eclipseExecutionId = connector.getExecutionId();                     capabilities.setCapability("eclipseExecutionId", eclipseExecutionId);                     }
        catch (IOException ex){
                 ex.printStackTrace();
        }
    */

    this.driver = new RemoteWebDriver(new URL("https://" + user + ':' + password +                                                                   '@' + host + "/nexperience/wd/hub"), capabilities);
 }

  @AfterClass
  public void afterClass() {
    try{
        // Close the browser
        driver.close();
            
        // Download a pdf version of the execution report
        PerfectoUtils.downloadReport(driver, "pdf", "C:\\temp\\report.pdf");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    driver.quit();
  }
}
