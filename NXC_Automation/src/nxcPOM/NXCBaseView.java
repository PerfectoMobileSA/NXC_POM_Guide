package nxcPOM;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.remote.RemoteWebDriver;

public class NXCBaseView {
	RemoteWebDriver driver;
	String url = "http://www.nxc.co.il/demoaut/index.php";
	
	// Page elements
	private By perfectoLogo    = By.xpath("//a[@href='http://www.perfectomobile.com']");
	private By userTextField   = By.xpath("//input[@name='username']");
	private By passwordField   = By.xpath("//input[@name='password' "
								 + "and @type='password']");
	private By submitButton    = By.xpath("//button[text()='Sign in']");
	
	public NXCBaseView(RemoteWebDriver driver){
		this.driver = driver;
	}
	
	public NXCBaseView init(){
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.get(url);
		if(driver.getTitle().contains("Perfecto Tours - Login")){
			return this;
		}
		else{
			throw new IllegalStateException();
		}
	}
	
	public MainView login(String username, String password){
		this.putUsername(username);
		this.putPassword(password);
		return this.submit();
	}
	
	public NXCBaseView putUsername(String username){
		driver.findElement(this.userTextField).sendKeys(username);
		return this;
	}
	
	public NXCBaseView putPassword(String password){
		driver.findElement(passwordField).sendKeys(password);
		return this;
	}
	
	public MainView submit(){
		driver.findElement(submitButton).click();
		return new MainView(driver);
	}
}
