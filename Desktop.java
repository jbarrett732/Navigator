
import java.util.ArrayList;

public class Desktop {

    public Integer desk_n  = null;
    public boolean current = false;
    public Integer desk_w  = null;
    public Integer desk_h  = null;
    public Integer view_x  = null;
    public Integer view_y  = null;
    public Integer work_x  = null;
    public Integer work_y  = null;
    public Integer work_w  = null;
    public Integer work_h  = null;
    public String  name    = null;

    public Integer rows    = null;
    public Integer cols    = null;
    public Integer select  = null;

    public ArrayList<Workspace> spaces;
   

    private boolean debug  = false;

    public Desktop(String data, boolean verbose) {
        debug = verbose;
        String[] values = data.split("\\s+");
        if(values.length > 9) {
            desk_n  = Integer.parseInt(values[0]);        
            current = (values[1].equals("*")) ? true : false; 
            if(values[2].equals("DG:")) {
                String[] desk_geometry = values[3].split("x");
                desk_w = (desk_geometry.length == 2) ? Integer.parseInt(desk_geometry[0]) : null;
                desk_h = (desk_geometry.length == 2) ? Integer.parseInt(desk_geometry[1]) : null;
            }
            if(values[4].equals("VP:")) {
                String[] view_port = values[5].split(",");
                view_x = (view_port.length == 2) ? Integer.parseInt(view_port[0]) : null;
                view_y = (view_port.length == 2) ? Integer.parseInt(view_port[1]) : null;
            }
            if(values[6].equals("WA:")) {
                String[] work_coord = values[7].split(",");
                work_x = (work_coord.length == 2) ? Integer.parseInt(work_coord[0]) : null;
                work_y = (work_coord.length == 2) ? Integer.parseInt(work_coord[1]) : null;
                String[] work_geometry = values[8].split("x");
                work_w = (work_geometry.length == 2) ? Integer.parseInt(work_geometry[0]) : null;
                work_h = (work_geometry.length == 2) ? Integer.parseInt(work_geometry[1]) : null;
            }
            int index = 2 + data.indexOf(" ", data.indexOf("x", data.indexOf("WA:")));
            name = data.substring(index); 
            rows = desk_h / (work_h + work_y);
            cols = desk_w / (work_w + work_x);

            spaces = new ArrayList<Workspace>(rows*cols);
            for(int i=0; i<rows; i++) {
               for(int j=0; j<cols; j++) {
                   String info = Integer.toString(i * cols + j)+"-Workspace"; 
                   info += "," + Integer.toString(j * (work_w + work_x)); 
                   info += "," + Integer.toString(i * (work_h + work_y)); 
                   info += "," + Integer.toString((j * (work_w + work_x)) + work_x); 
                   info += "," + Integer.toString((i * (work_h + work_y)) + work_y); 
                   info += "," + Integer.toString(work_w); 
                   info += "," + Integer.toString(work_h); 
                   Workspace wk = new Workspace(info);
                   spaces.add(wk);
                   if(wk.view_x.equals(view_x) && wk.view_y.equals(view_y)) {
                       select = i*cols + j;
                   }
               } 
            }
        }

        if(debug) {
            System.out.println("desk_n  = <"+ desk_n  +">");
            System.out.println("current = <"+ current +">");
            System.out.println("desk_w  = <"+ desk_w  +">");
            System.out.println("desk_h  = <"+ desk_h  +">");
            System.out.println("view_x  = <"+ view_x  +">");
            System.out.println("view_y  = <"+ view_y  +">");
            System.out.println("work_x  = <"+ work_x  +">");
            System.out.println("work_y  = <"+ work_y  +">");
            System.out.println("work_w  = <"+ work_w  +">");
            System.out.println("work_h  = <"+ work_h  +">");
            System.out.println("name    = <"+ name    +">");
            System.out.println("rows    = <"+ rows    +">");
            System.out.println("cols    = <"+ cols    +">");
        }
    }

    public Integer getNumSpaces() {
        if(rows != null && cols != null) {
            return rows*cols;
        } else {
            return null; 
        }
    }

    public Integer selectX() {
        return spaces.get(select).view_x;
    }

    public Integer selectY() {
        return spaces.get(select).view_y;
    }
}
