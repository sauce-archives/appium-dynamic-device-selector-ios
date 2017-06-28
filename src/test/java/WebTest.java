import io.appium.java_client.ios.IOSDriver;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class WebTest extends AbstractTest {
	private static String TESTOBJECT_API_KEY_WEB = getEnvOrFail("TESTOBJECT_API_KEY_WEB");
	private static String TESTOBJECT_APP_ID_WEB = getEnvOrDefault("TESTOBJECT_APP_ID_WEB", "1");
	private static String AUTOMATION_NAME = getEnvOrDefault("AUTOMATION_NAME", null);
	private static String PLATFORM_VERSION = getEnvOrDefault("PLATFORM_VERSION", null);

	// Credentials differ slightly for the web test so we override setup().
	@Before
	@Override
	public void setup() throws MalformedURLException {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("testobject_api_key", TESTOBJECT_API_KEY_WEB);
		capabilities.setCapability("platformName", "ios");
		capabilities.setCapability("platformVersion", PLATFORM_VERSION);
		capabilities.setCapability("testobject_app_id", TESTOBJECT_APP_ID_WEB);
		capabilities.setCapability("testobject_appium_version", TESTOBJECT_APPIUM_VERSION);
		capabilities.setCapability("testobject_cache_device", TESTOBJECT_CACHE_DEVICE);
		capabilities.setCapability("privateDevicesOnly", getEnvOrDefault("PRIVATE_DEVICES_ONLY","false"));

		if (AUTOMATION_NAME != null) {
			capabilities.setCapability("automationName", AUTOMATION_NAME);
		}

		URL endpoint = new URL(APPIUM_SERVER);

		// We generate a random UUID for later lookup in logs for debugging
		String testUUID = UUID.randomUUID().toString();
		System.out.println("TestUUID: " + testUUID);
		capabilities.setCapability("testobject_testuuid", testUUID);

		driver = new IOSDriver(endpoint, capabilities);

		System.out.println(driver.getCapabilities().getCapability("testobject_test_report_url"));
		System.out.println(driver.getCapabilities().getCapability("testobject_test_live_view_url"));
	}

	@Test
	public void openWebpageAndTakeScreenshot() throws InterruptedException {
		String url = "http://www.amazon.com";
		driver.get(url);
		driver.rotate(ScreenOrientation.LANDSCAPE);
		Thread.sleep(1000);
		driver.rotate(ScreenOrientation.PORTRAIT);

		takeScreenshot();
	}

	private void takeScreenshot() {
		try {
			driver.getScreenshotAs(OutputType.FILE);
		} catch (Exception e) {
			System.out.println("Exception while saving screenshot: " + e.getMessage());
		}
	}
}
