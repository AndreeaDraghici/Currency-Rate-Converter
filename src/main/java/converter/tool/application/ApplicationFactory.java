package converter.tool.application;

import converter.tool.application.iface.IApplication;

import static converter.tool.util.Constants.TOOL_IS_JUST_RUNNING_USING_GRAPHICAL_USER_INTERFACE_FUNCTIONALITY;

/**
 * Created by Andreea Draghici on 9/23/2023
 * Name of project: CurrencyConvertor
 */
public class ApplicationFactory {

    public IApplication applicationRunner(String[] args) {
        if (args.length == 0) {
            return new GUIApplication();
        } else {
            throw new RuntimeException(TOOL_IS_JUST_RUNNING_USING_GRAPHICAL_USER_INTERFACE_FUNCTIONALITY);
        }
    }
}
