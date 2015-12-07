

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;

public class Navigator extends JFrame {

    //lists
    private ArrayList<JButton>   buttons;

    //
    private Desktop desktop  = null;

    //properties
    private Color background = new Color(77,77,77);
    private Color foreground = new Color(0,183,230);
    private Color highlight  = new Color(183,0,230);
   

    //images
    private ImageIcon img_add     = new ImageIcon("images/img_add.png"    );
    private ImageIcon img_local   = new ImageIcon("images/img_local.png"  );
    private ImageIcon img_remote  = new ImageIcon("images/img_remote.png" );
    private ImageIcon img_virtual = new ImageIcon("images/img_virtual.png");
    private ImageIcon img_window  = new ImageIcon("images/img_window.png" );

    private boolean debug = false;

    public Navigator(boolean verbose) { 
        debug = verbose;

        Command cmd_info = new Command();    
        cmd_info.getDesktopInfo();
        ArrayList<String> info = cmd_info.output;
        for(int i=0; i<info.size(); i++) {
            //only if current desktop
            if(info.get(i).contains("*")) {
                desktop  = new Desktop(info.get(i), debug);
            }
        }

        if(desktop != null) {
            Integer num_spaces = desktop.getNumSpaces();
            JPanel panel = new JPanel(new GridLayout());
            buttons = new ArrayList<JButton>(num_spaces + 1);        

            JButton button = new JButton("Add"); 
            button.setIcon(img_add);
            button.setBackground(background);
            button.setForeground(foreground);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(debug) {
                        if(desktop.spaces != null) { 
                            for(int k=0; k<desktop.spaces.size(); k++) 
                                System.out.println(desktop.spaces.get(k).name);
                        }
                    }
                    new NewDesktop(desktop.spaces); 
                }
            });
            buttons.add(button);
            panel.add(button);

            for(int i=0; i<num_spaces; i++) {
                button = new JButton(desktop.spaces.get(i).name); 
                button.setIcon(img_local);
                if(desktop.select.equals(i)) {
                    button.setBackground(highlight);
                } else {
                    button.setBackground(background);
                }
                button.setForeground(foreground);
                int id = i;
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        buttons.get(desktop.select+1).setBackground(background);
                        buttons.get(id+1).setBackground(highlight);
                        desktop.select = id; 
                        Command cmd_move = new Command();
                        cmd_move.moveViewPort(desktop.selectX(), desktop.selectY());
                    }
                });
                buttons.add(button);
                panel.add(button);
            }
            add(panel);
        }

        //set JFrame properties
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("navigator");
        setUndecorated(true);
        setIconImage(img_window.getImage());
        setSize(desktop.work_w, 20);
        setVisible(true);

        Command cmd_stick = new Command();
        cmd_stick.makeSticky("navigator");
        Command cmd_above = new Command();
        cmd_above.makeAbove("navigator");
    }

    public static void main(String[] args) {
        if(args.length == 1) {
            if(args[0].equals("true")) {
                new Navigator(true);
            } else { 
                new Navigator(false);
            } 
        } else { 
            System.out.println("Please provide a boolean debug arguement");
        }
    }
}
