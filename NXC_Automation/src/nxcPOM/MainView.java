package nxcPOM;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;	

public class MainView extends NXCBaseView{
	
	private By ocrPage            = By.xpath("//a[text()='OCR Page']");
	private By termsAndConditions = By.xpath("//a[text()='View Terms "
									+ "and conditions']");
	private By selectSeat         = By.xpath("//a[text()='Select your "
									+ "seat']");
	private By myDetails          = By.xpath("//a[text()='My Details']");
	
	public MainView(RemoteWebDriver driver){
		super(driver);
		if(!(driver.getTitle().contains("Perfecto Tours - Main"))){
			throw new IllegalStateException();
		}
	}
	
	public OCRPageView clickOcrLink(){
		this.driver.findElement(ocrPage).click();
		return new OCRPageView(this.driver);
	}
}
