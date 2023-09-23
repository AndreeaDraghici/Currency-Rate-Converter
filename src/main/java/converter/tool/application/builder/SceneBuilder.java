package converter.tool.application.builder;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Created by Andreea Draghici on 9/23/2023
 * Name of project: CurrencyConvertor
 */
public class SceneBuilder {

    private static final Logger logger = LogManager.getLogger(SceneBuilder.class);

    private String viewPath;
    private String iconPath;

    public SceneBuilder() {
        this.viewPath = "";
        this.iconPath = "";
    }

    public SceneBuilder(String viewPath, String iconPath) {
        this.viewPath = viewPath;
        this.iconPath = iconPath;
    }

    public Scene buildScene() {

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(viewPath));
        AnchorPane stackPane = null;

        try {
            stackPane = loader.load();

        } catch (IOException exception) {
            logger.error("An error occurred when building the scene: %s", exception.getMessage());
            return null;
        }
        return new Scene(stackPane);
    }

    public Image getIcon(){
        return new Image(iconPath);
    }
}
