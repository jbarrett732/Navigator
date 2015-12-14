

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;
import javax.swing.JOptionPane;

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
                    //new NewDesktop(desktop.spaces); 
                    newDesktop(); 
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

    private void newDesktop() {
        //make form JFrame
        JFrame form_frame = new JFrame();
        JPanel vm_panel   = new JPanel(new GridLayout(8,2));
        JPanel rm_panel   = new JPanel(new GridLayout(8,2));
        JTextField text_vm_name = new JTextField();
        JTextField text_disk    = new JTextField();
        JTextField text_memory  = new JTextField();
        JTextField text_vram    = new JTextField();
        JTextField text_rm_name    = new JTextField();
        JTextField text_ip_address = new JTextField();
        JTextField text_user_name  = new JTextField();

        //setup for vm form
        JComboBox<String> workspaces_vm = new JComboBox<String>(desktop.getNameSpaces());
        Command cmd_os = new Command();
        cmd_os.getOSTypes();
        ArrayList<String> vbox_os_list = cmd_os.output;
        String[] os_types = new String[vbox_os_list.size()/6];
        int index_count = 0;
        for(int i=0; i<vbox_os_list.size(); i++) {
            if(vbox_os_list.get(i).trim().matches("^ID:(.*)")) {
                String[] split = vbox_os_list.get(i).split("\\s+");
                if(split.length == 2) {
                    os_types[index_count] = split[1];
                    index_count++;
                }
            }
        }
        JComboBox<String> os_select = new JComboBox<String>(os_types);
        JFileChooser iso_select = new JFileChooser();
        JButton create_vm = new JButton("Create Virtual Machine");
        create_vm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Validator v = new Validator();
                String vm_name  = v.alphanumeric(text_vm_name.getText());
                Integer disk = v.getInt(text_disk.getText());

                String os_type  = (String)os_select.getSelectedItem();
                
                String iso_dvd = null;
                if(iso_select.getSelectedFile() != null) {
                    iso_dvd  = iso_select.getSelectedFile().getAbsolutePath();
                } else {
                    iso_dvd  = null;
                }
                Integer memory = v.getInt(text_memory.getText());
                Integer vram = v.getInt(text_vram.getText());
                Integer vm_space = workspaces_vm.getSelectedIndex();

                if(vm_name    != null
                  && disk     != null
                  && os_type  != null
                  && iso_dvd  != null
                  && memory   != null
                  && vram     != null
                  && vm_space != null
                ) {
                    //create virtual machine
                    Workspace desk_space = desktop.spaces.get(vm_space);
                    buttons.get(vm_space+1).setIcon(img_virtual);
                    buttons.get(vm_space+1).setBackground(highlight);
                    buttons.get(desktop.select+1).setBackground(background);
                    desktop.select = vm_space;

                    //set JFrame properties
                    JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                    frame.setTitle(vm_name);
                    frame.setSize(400,400);
                    frame.setVisible(true);

                    //move window into workspace
                    Command move_cmd1 = new Command();
                    move_cmd1.moveWindow(
                        vm_name,
                        desk_space.work_x,
                        desk_space.work_y,
                        desk_space.work_w,
                        desk_space.work_h
                    );
                    //move_cmd1.printStats();

                    Command move_cmd2 = new Command();
                    move_cmd2.moveViewPort(
                        desk_space.view_x,
                        desk_space.view_y
                    );
                    //move_cmd2.printStats();

                    Command move_cmd3 = new Command();
                    move_cmd3.makeMaximized(vm_name);
                    //move_cmd3.printStats();

                    form_frame.setVisible(false);
                    form_frame.dispose();

                    JOptionPane.showMessageDialog(frame, vm_name+" Virtual Machine Created Successfully!");
                }
            }
        });
        JButton iso_button = new JButton("Choose File");
        iso_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iso_select.showOpenDialog(vm_panel);
            }
        });

        //setup for rm form
        JComboBox<String> workspaces_rm = new JComboBox<String>(desktop.getNameSpaces());
        JButton create_rm = new JButton("Create Remote Desktop");
        create_rm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Validator v = new Validator();
                String rm_name    = v.alphanumeric(text_rm_name.getText());
                String ip_address = v.ipv4Address(text_ip_address.getText());
                String user_name  = v.alphanumeric(text_user_name.getText());
                Integer rm_space = workspaces_rm.getSelectedIndex();

                if(rm_name      != null
                  && ip_address != null
                  && user_name  != null
                  && rm_space   != null
                ) {
                    //create remote desktop
                    Workspace desk_space = desktop.spaces.get(rm_space);
                    buttons.get(rm_space+1).setIcon(img_remote);
                    buttons.get(rm_space+1).setBackground(highlight);
                    buttons.get(desktop.select+1).setBackground(background);
                    desktop.select = rm_space;

                    //set JFrame properties
                    JFrame frame = new JFrame();
                    frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                    frame.setTitle(rm_name);
                    //setIconImage(img_window.getImage());
                    frame.setSize(400,400);
                    frame.setVisible(true);

                    //move window into workspace
                    Command move_cmd1 = new Command();
                    move_cmd1.moveWindow(
                        rm_name,
                        desk_space.work_x,
                        desk_space.work_y,
                        desk_space.work_w,
                        desk_space.work_h
                    );
                    //move_cmd1.printStats();

                    Command move_cmd2 = new Command();
                    move_cmd2.moveViewPort(
                        desk_space.view_x,
                        desk_space.view_y
                    );
                    //move_cmd2.printStats();

                    Command move_cmd3 = new Command();
                    move_cmd3.makeMaximized(rm_name);
                    //move_cmd3.printStats();

                    form_frame.setVisible(false);
                    form_frame.dispose();

                    JOptionPane.showMessageDialog(frame, rm_name+" Remote Desktop Created Successfully!");
                }
            }
        });

        //build virtual machine panel
        vm_panel.add(new JLabel("Virtual Machine Name"));
        vm_panel.add(text_vm_name);
        vm_panel.add(new JLabel("Dynamic Disk Size"));
        vm_panel.add(text_disk);
        vm_panel.add(new JLabel("Operating System Type"));
        vm_panel.add(os_select);
        vm_panel.add(new JLabel("Boot Image DVD"));
        vm_panel.add(iso_button);
        vm_panel.add(new JLabel("Memory Size"));
        vm_panel.add(text_memory);
        vm_panel.add(new JLabel("Virtual Ram Size"));
        vm_panel.add(text_vram);
        vm_panel.add(new JLabel("Workspace"));
        vm_panel.add(workspaces_vm);
        vm_panel.add(new JLabel(""));
        vm_panel.add(create_vm);

        //build remote panel
        rm_panel.add(new JLabel("Remote Desktop Name"));
        rm_panel.add(text_rm_name);
        rm_panel.add(new JLabel("IP Address"));
        rm_panel.add(text_ip_address);
        rm_panel.add(new JLabel("User Name"));
        rm_panel.add(text_user_name);
        rm_panel.add(new JLabel("Workspace"));
        rm_panel.add(workspaces_rm);
        for(int i=0; i<6; i++) {
            rm_panel.add(new JLabel(""));
        }
        rm_panel.add(new JLabel(""));
        rm_panel.add(create_rm);


        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("New Remote Desktop",  img_remote, rm_panel,  "Creates A New Remote Desktop in a Workspace");
        tabs.addTab("New Virtual Machine", img_virtual, vm_panel, "Creates A New Virtual Machine in a Workspace");
        form_frame.add(tabs);

        //set form JFrame properties
        form_frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        form_frame.setTitle("new-desktop");
        form_frame.setSize(450,300);
        form_frame.setVisible(true);
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
