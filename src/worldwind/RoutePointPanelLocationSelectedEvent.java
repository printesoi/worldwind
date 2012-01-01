package worldwind;

import java.util.EventObject;

public class RoutePointPanelLocationSelectedEvent extends EventObject{
    
    private String actionCommand;
    private Object source;

    public RoutePointPanelLocationSelectedEvent(Object source) {
        super(source);
        this.source = source;
        actionCommand = "";
    }
    
    public void setActionCommand(String command) {
        actionCommand = command;
    }
    
    public String getActionCommand() {
        return actionCommand;
    }

    public Object getSource() {
        return source;
    }
}
