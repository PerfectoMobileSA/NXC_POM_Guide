package nxcPOM;

import org.openqa.selenium.remote.RemoteWebDriver;

public class OCRPageView extends NXCBaseView {
	
	public OCRPageView(RemoteWebDriver driver){
		super(driver);
		if(!(driver.getTitle().contains("Perfecto Tours - OCR"))){
			throw new IllegalStateException();
		}
	}
}
