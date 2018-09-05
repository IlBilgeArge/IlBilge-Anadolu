/**
 * TODO LISENSE
 */
package main.java.com.ilbilge.ilbilgeanadolu.browser;

import java.io.IOException;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import main.java.com.ilbilge.ilbilgeanadolu.tools.InfoTool;
import main.java.com.ilbilge.ilbilgeanadolu.tools.InfoTool;

/**
 * @author GOXR3PLUS
 *
 */
public class WebBrowserController extends StackPane {
	
	/** The logger. */
	private final Logger logger = Logger.getLogger(getClass().getName());
	
	public static final String VERSION = "Versiyon 1.0.1";
	
	public static boolean MOVING_TITLES_ENABLED = true;
	
	//------------------------------------------------------------
	@FXML
	private Button showVersion;
	
	@FXML
	private TabPane tabPane;
	
	@FXML
	private JFXButton addTab;
	
	// -------------------------------------------------------------
	
	/**
	 * Constructor
	 */
	public WebBrowserController() {
		
		// ------------------------------------FXMLLOADER ----------------------------------------
		FXMLLoader loader = new FXMLLoader(getClass().getResource(InfoTool.FXMLS + "WebBrowserController.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "", ex);
		}
	}
	
	/**
	 * Called as soon as .fxml is initialized [[SuppressWarningsSpartan]]
	 */
	@FXML
	private void initialize() {
		
		//tabPane
		tabPane.getTabs().clear();
		createAndAddNewTab();
		
		//addTab
		addTab.setOnAction(a -> createAndAddNewTab());
		
	}
	
	/**
	 * Creates a new tab for the web browser ->Directing to a specific web site [[SuppressWarningsSpartan]]
	 * 
	 * @param webSite
	 */
	public WebBrowserTabController createAndAddNewTab(String... webSite) {
		
		//Create
		WebBrowserTabController webBrowserTab = createNewTab(webSite);
		
		//Add the tab
		tabPane.getTabs().add(webBrowserTab.getTab());
		
		return webBrowserTab;
	}
	
	/**
	 * Creates a new tab for the web browser ->Directing to a specific web site [[SuppressWarningsSpartan]]
	 * 
	 * @param webSite
	 */
	public WebBrowserTabController createNewTab(String... webSite) {
		
		//Create
		Tab tab = new Tab("");
		WebBrowserTabController webBrowserTab = new WebBrowserTabController(this, tab, webSite.length == 0 ? null : webSite[0]);
		tab.setOnClosed(c -> {
			
			//Check the tabs number
			if (tabPane.getTabs().isEmpty())
				createAndAddNewTab();
			
			// Delete cache for navigate back
			webBrowserTab.webEngine.load("about:blank");
			
			//Delete cookies  Experimental!!! 
			//java.net.CookieHandler.setDefault(new java.net.CookieManager())
			
		});
		
		return webBrowserTab;
	}
	
	/**
	 * Closes the tabs to the right of the given Tab
	 * 
	 * @param tab
	 */
	public void closeTabsToTheRight(Tab givenTab) {
		//Return if size <= 1
		if (tabPane.getTabs().size() <= 1)
			return;
		
		//The start
		int start = tabPane.getTabs().indexOf(givenTab);
		
		//Remove the appropriate items
		tabPane.getTabs().stream()
				//filter
				.filter(tab -> tabPane.getTabs().indexOf(tab) > start)
				//Collect the all to a list
				.collect(Collectors.toList()).forEach(this::removeTab);
		
	}
	
	/**
	 * Closes the tabs to the left of the given Tab
	 * 
	 * @param tab
	 */
	public void closeTabsToTheLeft(Tab givenTab) {
		//Return if size <= 1
		if (tabPane.getTabs().size() <= 1)
			return;
		
		//The start
		int start = tabPane.getTabs().indexOf(givenTab);
		
		//Remove the appropriate items
		tabPane.getTabs().stream()
				//filter
				.filter(tab -> tabPane.getTabs().indexOf(tab) < start)
				//Collect the all to a list
				.collect(Collectors.toList()).forEach(this::removeTab);
		
	}
	
	/**
	 * Removes this Tab from the TabPane
	 * 
	 * @param tab
	 */
	public void removeTab(Tab tab) {
		tabPane.getTabs().remove(tab);
		tab.getOnClosed().handle(null);
	}
	
	/**
	 * @return the tabPane
	 */
	public TabPane getTabPane() {
		return tabPane;
	}
	
	/**
	 * Sets the moving titles enabled or disabled on all the tabs
	 * 
	 * @param value
	 */
	public void setMovingTitlesEnabled(boolean value) {
		MOVING_TITLES_ENABLED = value;
		tabPane.getTabs().forEach(tab -> ( (WebBrowserTabController) tab.getContent() ).setMovingTitleEnabled(value));
	}
	
	/**
	 * This is a list holding all the proposed websites for the user
	 */
	
	public static final SortedSet<String> WEBSITE_PROPOSALS = new TreeSet<>(
			Arrays.asList(
			"https://www.heryerdeyazilim.com","https://www.facebook.com","https://www.youtube.com"
			));
	
}
