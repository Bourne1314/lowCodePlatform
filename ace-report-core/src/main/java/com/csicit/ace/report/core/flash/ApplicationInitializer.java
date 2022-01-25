package com.csicit.ace.report.core.flash;

import com.stimulsoft.base.exception.StiException;
import com.stimulsoft.flex.StiFlexConfig;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;

/**
 * Application initialization.
 */
public class ApplicationInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(final ServletContextEvent event) {
        try {
            // configuration application
            StiFlexConfig stiConfig = initConfig();
            // Setup custom properties
            stiConfig.getProperties().setProperty("Engine.Type", "Java");
            stiConfig.getProperties().setProperty("Appearance.DateFormat", "yyyy");
            stiConfig.getProperties().setProperty("Appearance.VariablesPanelColumns", "3");
            stiConfig.getProperties().setProperty("Appearance.FullScreenMode", "true");
            stiConfig.getProperties().setProperty("Appearance.ShowTooltipsHelp", "false");
            stiConfig.getProperties().setProperty("Viewer.Toolbar.ShowAboutButton", "false");
            stiConfig.setLoadClass(MyLoadAction.class);
            stiConfig.setSaveClass(MySaveAction.class);
            stiConfig.setLocalizationAction(MyLocalizationAction.class);
            StiFlexConfig.init(stiConfig);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void contextDestroyed(final ServletContextEvent event) {
        // empty
    }

    public StiFlexConfig initConfig() throws StiException, IOException {
        // Properties properties = new Properties();
        // load your own Properties;
        // InputStream inStream = getClass().getResourceAsStream("RESOURCE_PATH");
        // properties.load(inStream);
        // return new StiFlexConfig(properties);
        return new StiFlexConfig();
    }

}
