package converter.tool.application;

import converter.tool.application.iface.IApplication;

/**
 * Created by Andreea Draghici on 9/23/2023
 * Name of project: CurrencyConvertor
 */
public class ApplicationFactory {

    public IApplication applicationRunner(String[] args) {
        if (args.length == 0) {
            return new GUIApplication();
        } else {
            throw new RuntimeException("CLI option is not implemented. The tool is just running using Graphical User Interface functionality!!");
        }
    }
}
