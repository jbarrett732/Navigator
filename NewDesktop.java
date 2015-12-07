
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.GridLayout;

import java.util.ArrayList;

public class NewDesktop extends JFrame {

    private ImageIcon img_remote  = new ImageIcon("images/img_remote.png" );
    private ImageIcon img_virtual = new ImageIcon("images/img_virtual.png");
    
    private String[] names;
    private JPanel   vm_panel = new JPanel(new GridLayout(8,2));
    private JPanel   rm_panel = new JPanel(new GridLayout(8,2));

    //virtual machine variables
    private String  vm_name  = null;
    private Integer disk     = null;
    private String  os_type  = null;
    private String  iso_dvd  = null;
    private Integer memory   = null;
    private Integer vram     = null;
    private Integer vm_space = null;

    private JTextField text_vm_name = new JTextField();
    private JTextField text_disk    = new JTextField();
    private String[]                os_types;
    private JComboBox<String>       os_select;
    private JFileChooser            iso_select = new JFileChooser();
    private JButton                 iso_button = new JButton("Choose File");
    private JTextField text_memory  = new JTextField();
    private JTextField text_vram    = new JTextField();
    private JComboBox<String>       workspaces_vm;
    private JButton                 create_vm;

    //remote desktop variables
    private String  rm_name    = null;
    private String  ip_address = null;
    private String  user_name  = null;
    private Integer rm_space   = null;

    private JTextField text_rm_name    = new JTextField();
    private JTextField text_ip_address = new JTextField();
    private JTextField text_user_name  = new JTextField(); 
    private JComboBox<String>          workspaces_rm;
    private JButton                    create_rm;
 
    public NewDesktop(ArrayList<Workspace> list) {
        names = new String[list.size()];
        for(int i=0; i<list.size(); i++) {
            names[i] = list.get(i).name;
        }

        //setup for vm form
        workspaces_vm = new JComboBox<String>(names);
        Command cmd_os = new Command();
        cmd_os.getOSTypes();
        ArrayList<String> vbox_os_list = cmd_os.output; 
        os_types = new String[vbox_os_list.size()/6];
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
        os_select = new JComboBox<String>(os_types);       
        create_vm = new JButton("Create Virtual Machine");
        create_vm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Validator v = new Validator();
                vm_name  = v.alphanumeric(text_vm_name.getText());
                disk = v.getInt(text_disk.getText());
                os_type  = (String)os_select.getSelectedItem();
                if(iso_select.getSelectedFile() != null) {
                    iso_dvd  = iso_select.getSelectedFile().getAbsolutePath();
                } else {
                    iso_dvd  = null; 
                }
                memory = v.getInt(text_memory.getText()); 
                vram = v.getInt(text_vram.getText()); 
                vm_space = workspaces_vm.getSelectedIndex();

                System.out.println(vm_name);
                System.out.println(disk);
                System.out.println(os_type);
                System.out.println(iso_dvd);
                System.out.println(memory);
                System.out.println(vram);
                System.out.println(vm_space);
                if(vm_name    != null 
                  && disk     != null 
                  && os_type  != null 
                  && iso_dvd  != null 
                  && memory   != null 
                  && vram     != null 
                  && vm_space != null 
                ) {
                    //create virtual machine 
                }
            }
        });
        iso_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iso_select.showOpenDialog(vm_panel);
            }
        });

        //setup for vm form
        workspaces_rm = new JComboBox<String>(names);
        create_rm = new JButton("Create Remote Desktop");
        create_rm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Validator v = new Validator();
                rm_name    = v.alphanumeric(text_rm_name.getText());
                ip_address = v.ipv4Address(text_ip_address.getText());
                user_name  = v.alphanumeric(text_user_name.getText());
                rm_space = workspaces_rm.getSelectedIndex();

                System.out.println(rm_name);
                System.out.println(ip_address);
                System.out.println(user_name);
                System.out.println(rm_space);

                if(rm_name      != null 
                  && ip_address != null 
                  && user_name  != null 
                  && rm_space   != null
                ) {
                    //create remote desktop
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
        add(tabs);

        //set JFrame properties
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("new-desktop");
        //setIconImage(img_window.getImage());
        setSize(450,300);
        setVisible(true);
    }
}
