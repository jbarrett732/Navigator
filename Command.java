import java.util.ArrayList;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class Command {

    public ArrayList<String> output = new ArrayList<String>();
    public int exitValue = 0;
    public String error_msg = "";
    public String excpt_msg = "";

    public Command() { }

    public void getDesktopInfo() {
        exec("wmctrl -d");
    }

    public void makeSticky(String name) {
        exec("wmctrl -F -r "+name+" -b add,sticky"); 
    }

    public void makeAbove(String name) {
        exec("wmctrl -F -r "+name+" -b add,above"); 
    }

    public void makeMaximized(String name) {
        exec("wmctrl -F -r "+name+" -b add,maximized_vert,maximized_horz"); 
    }

    public void moveViewPort(Integer x, Integer y) {
        exec("wmctrl -o "+x.toString()+","+y.toString());
    }

    public void moveWindow(String name, Integer x, Integer y, Integer w, Integer h) {
        exec("wmctrl -F -r "+name+" -e 0,"+x.toString()+","+y.toString()+","+w.toString()+","+h.toString());
    }

    public void getOSTypes() {
        exec("VBoxManage list ostypes");
    }

    public void createRemoteDesktop(String userName, String ipAddr) {
        Runtime.getRuntime().exec("rdesktop -u "+userName+" -g 100% -PKD "+ipAddr);
    }

    private void exec(String cmd) {
        Process p;
        try {
            p = Runtime.getRuntime().exec(cmd);
            p.waitFor();

            exitValue = p.exitValue();
            
            InputStreamReader o_stream = new InputStreamReader(p.getInputStream());
            BufferedReader    o_reader = new BufferedReader(o_stream);
            String o_line = "";
            while((o_line = o_reader.readLine()) != null) {
                output.add(o_line);
            } 
            
            InputStreamReader e_stream = new InputStreamReader(p.getInputStream());
            BufferedReader    e_reader = new BufferedReader(e_stream);
            String e_line = "";
            while((e_line = e_reader.readLine()) != null) {
                error_msg += e_line + "\n";
            } 

        } catch (Exception e) {
            excpt_msg = e.getMessage(); 
        }
    }

    public void printStats() {
        System.out.println("Output:");
        for(int i=0; i<output.size(); i++) {
            System.out.println(output.get(i));
        }
        System.out.println("Exit Value:"+exitValue);
        System.out.println("Error Msg:"+error_msg);
        System.out.println("Excpt Msg:"+excpt_msg);
    }

}
