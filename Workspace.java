
public class Workspace {

    public String  name   = null;
    public Integer view_x = null;
    public Integer view_y = null;
    public Integer work_x = null;
    public Integer work_y = null;
    public Integer work_w = null;
    public Integer work_h = null;

    public Workspace(String data) {
        String[] values = data.split(",");
        if(values.length == 7) {
            name = values[0];
            view_x = Integer.parseInt(values[1]);
            view_y = Integer.parseInt(values[2]);
            work_x = Integer.parseInt(values[3]);
            work_y = Integer.parseInt(values[4]);
            work_w = Integer.parseInt(values[5]);
            work_h = Integer.parseInt(values[6]);
        }
    }
}
