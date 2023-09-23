package converter.tool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import converter.tool.application.ApplicationFactory;
import converter.tool.application.iface.IApplication;


/**
 * Created by Andreea Draghici on 8/1/2023
 * Name of project: CurrencyConvertor
 */
public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        ApplicationFactory factory = new ApplicationFactory();
        IApplication application = factory.applicationRunner(args);
        application.run(args);

    }
}