package converter.tool.application;

import converter.tool.util.Constants;
import javafx.application.Application;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import converter.tool.application.builder.SceneBuilder;
import converter.tool.application.iface.IApplication;

/**
 * Created by Andreea Draghici on 9/23/2023
 * Name of project: CurrencyConvertor
 */
public class GUIApplication extends Application implements IApplication {

    private static final Logger logger = LogManager.getLogger(GUIApplication.class);

    private SceneBuilder sceneBuilder;

    private AnchorPane stackPane;

    private static Window window;

    public static Stage mainStage;

    public GUIApplication() {
        this.sceneBuilder = new SceneBuilder(Constants.FXML, Constants.LOGO_JAVA);
    }

    @Override
    public void run(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GUIApplication.mainStage = primaryStage;
        logger.info("Application is running...");

        try {
            primaryStage.setTitle(String.format("%s%s", Constants.TOOL_NAME, Constants.TOOL_VERSION));
            primaryStage.getIcons().add(sceneBuilder.getIcon());
            primaryStage.setScene(sceneBuilder.buildScene());
            primaryStage.setResizable(false);
            primaryStage.show();
            setWindow(primaryStage);

        } catch (Exception exception) {
            logger.error(String.format("Could start the GUI application due to: %s", exception.getMessage()));

        }
    }

    public static Window getWindow() {
        return window.getScene().getWindow();
    }

    private static void setWindow(Window window) {
        GUIApplication.window = window;
    }

}
