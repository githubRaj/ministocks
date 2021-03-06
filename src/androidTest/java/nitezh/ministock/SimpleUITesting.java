package nitezh.ministock;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.view.KeyEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Gurkomal Rao, Jefferson Casimir, Nicholas Fong on 3/14/2018.
 * Tests are done using UI Automator
 */

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class SimpleUITesting {

    UiDevice mDevice;

    @Before
    public void setup() {
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @After
    public void finish() {
        mDevice.pressBack();    //After every test go back to home screen
    }

    @Test
    public void clickRefresh() throws UiObjectNotFoundException {
        selectRefresh();
    }

    @Test
    public void clickListItemTest() throws UiObjectNotFoundException {
        int index = 0;
        UiScrollable listView = new UiScrollable(new UiSelector());
        listView.setMaxSearchSwipes(100);
        listView.waitForExists(3000);
        listView.getChild(new UiSelector().clickable(true).index(index)).click();
    }

    @Test
    public void clickPreferencesTest() throws UiObjectNotFoundException {
        selectPreferences();
    }

    @Test
    public void clickStockSetupTest() throws UiObjectNotFoundException {
        selectPreferences();                        // Click Preferences Button
        selectStockSetup();                        // Click Stocks setup
        setStock(0, Arrays.asList(KeyEvent.KEYCODE_K));     // Add 2nd Stock
        setStock(0, Arrays.asList(KeyEvent.KEYCODE_M, KeyEvent.KEYCODE_M, KeyEvent.KEYCODE_D));     // Change 1st Stock
        removeStock(1);                     // Remove 2nd Stock
    }

    @Test
    public void clickUpdatePrices() throws UiObjectNotFoundException {
        selectPreferences();                        // Click Preferences Button
        updatePrices();                             // Click Update prices now
    }

    @Test
    public void settingsMenuTest() throws UiObjectNotFoundException {
        selectPreferences();
        clickMenuItem("Settings");      // Enter Settings Menu
        clickMenuItem("Widget views");
        mDevice.pressBack();
        clickMenuItem("Appearance");
        mDevice.pressBack();
        clickMenuItem("Update options");
        mDevice.pressBack();
        clickMenuItem("Backup and Restore (BETA)");
        mDevice.pressBack();
        mDevice.pressBack();
    }

    @Test
    public void portfolioMenuTest() throws UiObjectNotFoundException {
        selectPreferences();
        clickMenuItem("Portfolio");      // Enter Portfolio Menu
        mDevice.pressBack();
    }

    @Test
    public void helpMenuTest() throws UiObjectNotFoundException {
        selectPreferences();
        clickMenuItem("Help");      // Enter Help Menu
        mDevice.pressBack();
    }

    @Test
    public void aboutMenuTest() throws UiObjectNotFoundException {
        selectPreferences();
        clickMenuItem("About");      // Enter About Menu
        mDevice.pressBack();
    }

    private void clickMenuItem(String itemName) throws UiObjectNotFoundException {
        UiScrollable menuListView = new UiScrollable(new UiSelector());
        menuListView.setMaxSearchSwipes(100);
        menuListView.scrollTextIntoView(itemName);
        menuListView.waitForExists(3000);
        UiObject menuListItem =
                menuListView.getChildByText(new UiSelector().className(android.widget.TextView.class.getName()),
                        itemName);
        menuListItem.click();
    }

    private void selectPreferences() throws UiObjectNotFoundException {
        String preferencesResourceId = "nitezh.ministock:id/prefs_but";
        UiObject button = mDevice.findObject(new UiSelector().resourceId(preferencesResourceId));
        button.clickAndWaitForNewWindow();
    }

    private void selectRefresh() throws UiObjectNotFoundException {
        String preferencesResourceId = "nitezh.ministock:id/test_but";
        UiObject button = mDevice.findObject(new UiSelector().resourceId(preferencesResourceId));
        button.clickAndWaitForNewWindow();
    }

    private void selectStockSetup() throws UiObjectNotFoundException {
        String stockSetup = "Stocks setup";
        UiScrollable preferencesListView = new UiScrollable(new UiSelector());
        preferencesListView.setMaxSearchSwipes(100);
        preferencesListView.scrollTextIntoView(stockSetup);
        preferencesListView.waitForExists(3000);
        UiObject preferencesListItem =
                preferencesListView.getChildByText(new UiSelector().className(android.widget.TextView.class.getName()),
                        stockSetup);
        preferencesListItem.click();
    }

    private UiObject selectStockView(int index) throws UiObjectNotFoundException {
        String searchFieldResourceId = "android:id/search_src_text";
        UiScrollable stockListView = new UiScrollable(new UiSelector());
        stockListView.getChild(new UiSelector().clickable(true).index(index)).click();
        UiObject searchField = mDevice.findObject(new UiSelector().resourceId(searchFieldResourceId));
        return searchField;
    }

    private void setStock(int index, String symbolToAdd) throws UiObjectNotFoundException {
        UiObject searchField = selectStockView(index);
        searchField.setText(symbolToAdd);
        searchField.clickAndWaitForNewWindow(3000);
        mDevice.pressDPadDown();
        mDevice.pressDPadUp();
        mDevice.pressEnter();
    }

    private void setStock(int index, List<Integer> keyCodes) throws UiObjectNotFoundException {
        UiObject searchField = selectStockView(index);
        searchField.clearTextField();
        for (Integer keyCode : keyCodes)
            mDevice.pressKeyCode(keyCode);
        searchField.clickAndWaitForNewWindow(2000);
        mDevice.pressDPadDown();
        mDevice.pressDPadUp();
        mDevice.pressEnter();
    }

    private void removeStock(int index) throws UiObjectNotFoundException {
        UiObject searchField = selectStockView(index);
        for (int i = 0; i < 8; i++) // 8 is max length of a stock, defined in stock suggestions
            mDevice.pressKeyCode(KeyEvent.KEYCODE_DEL);
        searchField.clickAndWaitForNewWindow(2000);
        mDevice.pressKeyCode(KeyEvent.KEYCODE_SPACE);
        mDevice.pressDPadDown();
        mDevice.pressDPadDown();
        mDevice.pressDPadDown();
        mDevice.pressEnter();
    }

    private void updatePrices() throws UiObjectNotFoundException {
        String updatePricesNow = "Update prices now";
        UiScrollable preferencesListView = new UiScrollable(new UiSelector());
        preferencesListView.setMaxSearchSwipes(100);
        preferencesListView.scrollTextIntoView(updatePricesNow);
        preferencesListView.waitForExists(3000);
        UiObject preferencesListItem =
                preferencesListView.getChildByText(new UiSelector().className(android.widget.TextView.class.getName()),
                        updatePricesNow);
        preferencesListItem.click();
    }
}
