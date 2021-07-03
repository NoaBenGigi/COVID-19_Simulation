package UI;

import Country.Map;
import Country.Settlement;
import IO.SimulationFile;
import IO.StatisticsFile;
import Location.Point;
import Simulation.Clock;
import Simulation.Main;
import Virus.VirusManagement;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.List;
import javax.swing.event.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.lang.Object;
import java.util.concurrent.CyclicBarrier;
import javax.swing.table.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainWindow extends JFrame {
    public static MainWindow mainWindow=null;
    private StatisticTableView table;
    private final JSlider slider;
    private SimulationFile simulationFile;
    private StatisticsWindow statistic_win;
    private Caretaker_logFile caretakerLogFile=new Caretaker_logFile();;
    private MutationWindow mutationWindow;
    private static SaveLogFile f=null;
    public Map getMap() {
        return map;
    }
    public static SaveLogFile getF() {
        return f;
    }
    private Map map;
    private List<Settlement> settlements;
    public JSlider getSlider() {
        return slider;
    }
    public static MainWindow getInstance(){
        if(mainWindow == null)
            mainWindow=new MainWindow();
        return mainWindow;
    }
    private MainWindow(){
        super("Main Window");
        this.setFont(new Font("Courier", Font.BOLD, 30));
        MenuBar menuBar = new MenuBar();
        this.setJMenuBar(menuBar);
        this.setLayout(new BorderLayout());
        JPanel sd_panel = new JPanel();
        // slider settings and creation
        final int FPS_MIN = 0;
        final int FPS_MAX = 100;
        final int FPS_INIT = 25;    //initial frames per second
        slider = new JSlider(JSlider.HORIZONTAL, FPS_MIN, FPS_MAX, FPS_INIT);
        slider.setMinorTickSpacing(20);
        slider.setMajorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSize(150,100);
        slider.setFont(new Font("Courier", Font.BOLD, 12));
        sd_panel.add(slider);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent event) {
                int value = slider.getValue();
            }
        });
        this.add(sd_panel, BorderLayout.PAGE_START);

        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }
    public int getSliderValue() {	//get value from slider
        return (int)slider.getValue();
    }
    private class MenuBar extends JMenuBar{
        private final JMenuItem load;
        public MenuBar(){
            super();
            boolean flag = false;
            JMenu menuFile = new JMenu("File");
            JMenuItem statistics= new JMenuItem("Statistics", KeyEvent.VK_T);
            JMenuItem stop=new JMenuItem("Stop",new ImageIcon("src/Image/Stop.png"));
            JMenuItem pause =new JMenuItem("Pause",new ImageIcon("src/Image/Pause.png"));
            JMenuItem play =new JMenuItem("Play",new ImageIcon("src/Image/Play.png"));
            JMenuItem saveLogFile= new JMenuItem("Save log file");
            JMenuItem restoreLog=new JMenuItem("Restore log file");
            load= new JMenuItem("Load", KeyEvent.VK_T);
            load.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    simulationFile = new SimulationFile(Main.loadFileFunc());
                    map = simulationFile.GetMap();
                    /*if(f != null)
                        map.setLogFile(f);*/
                    settlements = map.GetSettlement();
                    for(Settlement settlement:map){
                        System.out.println(settlement);
                    }
                    statistic_win = new StatisticsWindow((JFrame) SwingUtilities.getWindowAncestor(getParent()));
                    PanelDrawing drawingMap = new PanelDrawing();
                    ((JFrame) SwingUtilities.getWindowAncestor(getParent())).getContentPane().add(drawingMap,BorderLayout.CENTER); //drawing the map
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            ((JFrame) SwingUtilities.getWindowAncestor(getParent())).getContentPane().setPreferredSize(new Dimension(1000,1000));
                            revalidate();
                            repaint();
                            pack();
                        }
                    });
                    map.ResetMap();
                    map.setCyclicBarrier(new CyclicBarrier(map.GetSettlement().size(), new Runnable() {
                        @Override
                        public void run() {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    getParent().revalidate();
                                    getParent().repaint();
                                    if(statistic_win != null)
                                        statistic_win.getTable().getModel().fireTableDataChanged();
                                }
                            });
                            Clock.nextTick();
                            System.out.println("Clock: "+Clock.now());
                            /*for (Settlement settlement : map) {
                                System.out.println(settlement.toString() + "\tnumOfPeople: " + settlement.GetPeople().size() +
                                        "\tnumOfSick\t" + settlement.GetSickPeopleList().size() + "\tnumOfDead\t" + settlement.GetDiedNum());
                            }*/
                            try {
                                Thread.sleep(100L *(100-getSliderValue())); // לשנות לפי הסליידר
                            } catch (InterruptedException interruptedException) {
                                interruptedException.printStackTrace();
                            }
                        }
                    }));
                    map.spawn_all();
                    /*saveLogFile.setEnabled(true);
                    saveLogFile.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if(simulationFile != null){
                                saveLogFile.setEnabled(true);
                                try {
                                    File file = saveFileFunc("logFile.log");
                                    f= new SaveLogFile(file);
                                    map.setLogFile(f);
                                    saveLogFile.setEnabled(false);
                                } catch ( FileNotFoundException fileNotFoundException) {
                                    fileNotFoundException.printStackTrace();
                                }
                            }

                        }
                    });*/
                    //menuFile.add(saveLogFile);

                    if(simulationFile != null){
                        load.setEnabled(false);
                        statistics.setEnabled(true);
                    }

                    play.setEnabled(true);
                    pause.setEnabled(true);
                    stop.setEnabled(true);
                }
            });
            menuFile.add(load);
            menuFile.addSeparator();

            statistics.setEnabled(simulationFile != null);
            statistics.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    statistic_win.setVisible(true);
                    statistic_win.setEnabled(true);
                    statistic_win.addWindowListener(new WindowListener() {
                        @Override
                        public void windowOpened(WindowEvent e) {statistics.setEnabled(false);}
                        @Override
                        public void windowClosing(WindowEvent e) {statistics.setEnabled(true);}
                        @Override
                        public void windowClosed(WindowEvent e) {}
                        @Override
                        public void windowIconified(WindowEvent e) {}
                        @Override
                        public void windowDeiconified(WindowEvent e) {}
                        @Override
                        public void windowActivated(WindowEvent e) {}
                        @Override
                        public void windowDeactivated(WindowEvent e) {}
                    });
                }
            });

            menuFile.add(statistics);
            menuFile.addSeparator();
            JMenuItem edit_mutation =new JMenuItem("Edit Mutations");
            edit_mutation.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mutationWindow=new MutationWindow((JFrame) SwingUtilities.getWindowAncestor(getParent()));
                    for(int i=0;i<3;i++)
                        for(int j=0;j<3;j++)
                            System.out.print(VirusManagement.getVariantsBooleanTable()[i][j] +"  ");
                        System.out.println();
                }
            });
            menuFile.add(edit_mutation);
            menuFile.addSeparator();

            saveLogFile.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        File file = saveFileFunc("logFile.log");
                        f= new SaveLogFile(file); // the first time we create log file
                        caretakerLogFile.addMemento(f.createMemento());
                    } catch ( FileNotFoundException fileNotFoundException) {
                        fileNotFoundException.printStackTrace();
                    }
                }
            });
            menuFile.add(saveLogFile);
            restoreLog.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(caretakerLogFile.getPrev_file()!= null){
                        f.setMemento(caretakerLogFile.getPrev_file());
                        System.out.println("Now the log file is: "+f.getFile().getName()+"\n and the path is: "+f.getFile().getPath());
                        caretakerLogFile.addMemento(f.createMemento());
                    }
                    else{
                        System.out.println("There is no available back-up for log file");
                    }
                }
            });
            menuFile.add(restoreLog);
            JMenuItem exit= new JMenuItem("Exit", KeyEvent.VK_T);
            exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            menuFile.add(exit);
            JMenu menuSimulation = new JMenu("Simulation");

            play.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    map.setPAUSE_simulation(false);
                    map.setSTOP_simulation(false);
                    map.setPLAY_simulation(true);
                    synchronized(map) {
                        map.notifyAll();
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            if(statistic_win != null)
                                statistic_win.setVisible(true);
                        }
                    });
                }
            });

            menuSimulation.add(play);
            menuSimulation.addSeparator();

            pause.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    map.setPLAY_simulation(false);
                    map.setSTOP_simulation(false);
                    map.setPAUSE_simulation(true);
                }
            });

            menuSimulation.add(pause);
            menuSimulation.addSeparator();

            stop.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    map.setSTOP_simulation(true);
                    map.setPLAY_simulation(false);
                    map.setPAUSE_simulation(false);
                    f = null;
                    load.setEnabled(true);
                    play.setEnabled(false);
                    pause.setEnabled(false);
                    stop.setEnabled(false);

                    statistics.setEnabled(false);

                    statistic_win.setVisible(false);
                    statistic_win =null;
                    Clock.resetClock();
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            revalidate();
                            repaint();
                        }
                    });
                }
            });

            menuSimulation.add(stop);
            menuSimulation.addSeparator();

            JMenuItem ticksPerDays = new JMenuItem("Set Ticks Per Day");
            ticksPerDays.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JDialog ticks_panel = new JDialog((Dialog) getParent(),"Ticks Per Day",true);
                    JLabel label = new JLabel("Ticks:");
                    ticks_panel.add(label);
                    label.setHorizontalAlignment(JLabel.CENTER);
                    label.setSize(180,100);
                    JSpinner s = new JSpinner();
                    s.setBounds(70, 70, 50, 40);
                    ticks_panel.setLayout(null);
                    ticks_panel.add(s);
                    ticks_panel.setSize(200, 200);
                    JButton save= new JButton("Save"){
                        {
                            setSize(70, 30);
                            setMaximumSize(getSize());
                        }
                    };
                    ticks_panel.add(save);
                    save.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Clock.SetTicksPerDays((int)s.getModel().getValue());
                            ticks_panel.setVisible(false);
                        }
                    });
                    ticks_panel.add(save);
                    ticks_panel.show();
                    ticks_panel.setDefaultCloseOperation(ticks_panel.HIDE_ON_CLOSE);
                    ticks_panel.pack();
                    ticks_panel.toFront();
                }
            });
            menuSimulation.add(ticksPerDays);
            JMenu menuHelp = new JMenu("Help");
            JMenuItem help = new JMenuItem("Help");
            help.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JDialog help_panel = new JDialog((JFrame) SwingUtilities.getWindowAncestor(getParent()),"Help",true);
                    JLabel help_label = new JLabel("<html>This program simulate the COVID-19 virus" +
                            "<br> In the program you can watch on demo of the virus and his impact on people" +
                            "<br> The user need to choose txt file that include map."+
                            "<br>In this program you can control on the speed of the simulation and see how the map change</html>");
                    help_panel.add(help_label);
                    help_panel.setDefaultCloseOperation(help_panel.HIDE_ON_CLOSE);
                    help_panel.pack();
                    help_panel.toFront();
                    help_panel.setVisible(true);
                }
            });

            menuHelp.add(help);
            menuHelp.addSeparator();
            JMenuItem about = new JMenuItem("About");
            about.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JDialog about_panel = new JDialog((JFrame) SwingUtilities.getWindowAncestor(getParent()),"Help",false);
                    JLabel about_label = new JLabel("<html>This program is written by:"+
                            "<br>Noa Ben-Gigi" +
                            "<br> Lion Dahan" +
                            "<br>Date : 28/04/2021 </html>");
                    about_panel.add(about_label);
                    about_panel.setDefaultCloseOperation(about_panel.HIDE_ON_CLOSE);
                    about_panel.pack();
                    about_panel.toFront();
                    about_panel.setVisible(true);
                }
            });
            menuHelp.add(about);
            this.add(menuFile);
            this.add(menuSimulation);
            this.add(menuHelp);
        }
    }
    private File saveFileFunc(String fileName) {

        FileDialog fd = new FileDialog((this) , "Please choose a file:", FileDialog.SAVE);
        fd.setFile(fileName);
        fd.setVisible(true);
        if (fd.getFile() == null)
            return null;
        File f = new File(fd.getDirectory(), fd.getFile());
        System.out.println(f.getPath());
        return f;
    }
    private class StatisticsWindow extends JDialog{
        private final JLabel lbPromat;
        private JTextField tbPromat;
        private JComboBox<String> select_combo;

        private final String[] select={ "Settlement Name" ,"Settlement type", "Ramzor Color", "Vaccine Doses" };
        public StatisticsWindow(JFrame window){
            super(window,"Statistic Window ",false); //create new window
            this.setLayout(new BorderLayout());

            JPanel centerPanel = new JPanel();
            JPanel northPanel = new JPanel();
            JPanel southPanel = new JPanel();

            table = new StatisticTableView(map.GetSettlement());
            this.tbPromat = table.getTbFilterText();
            northPanel.setLayout(new BoxLayout(northPanel,BoxLayout.LINE_AXIS));
            select_combo= new JComboBox<>(select);

            northPanel.add(select_combo);

            northPanel.add(this.add(this.lbPromat = new JLabel("     Filter TextField:")));
            northPanel.add(this.add(this.tbPromat));

            this.add(northPanel, BorderLayout.PAGE_START);

            centerPanel.add(table);
            this.add(centerPanel,BorderLayout.CENTER);

            southPanel.setLayout(new BoxLayout(southPanel,BoxLayout.LINE_AXIS));
            JButton save= new JButton("Save");
            southPanel.add(save);
            save.addActionListener(e -> {
                try {
                    StatisticsFile csv= new StatisticsFile(map);
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
            });
            JButton sick =new JButton("Add Sick");
            sick.addActionListener(e -> {
                int selectedRow = table.selectedRow();
                map.at(selectedRow).MakeSick();
                this.revalidate();
                this.repaint();
                window.revalidate();
                window.repaint();
            });
            southPanel.add(sick);
            JButton vaccines=new JButton("Add doses");
            vaccines.addActionListener(e -> {
                JDialog vaccines_panel=new JDialog((JFrame) SwingUtilities.getWindowAncestor(getParent()),"Vaccines Window",true);
                vaccines_panel.setLayout(new BorderLayout());
                JLabel label = new JLabel("Please enter number of vaccines");
                vaccines_panel.add(label, BorderLayout.NORTH);
                JTextField textVal = new JTextField();
                textVal.setSize(new Dimension(20,20));
                vaccines_panel.add(textVal, BorderLayout.CENTER);
                JButton saveVal= new JButton("OK"){
                    {
                        setSize(70, 30);
                        setMaximumSize(getSize());
                    }
                };
                saveVal.addActionListener(e12 -> {
                    String value = textVal.getText();
                    vaccines_panel.setVisible(false);
                    int selectedRow =  table.selectedRow();
                    table.getModel().setValueAt(value,selectedRow,4);
                    map.at(selectedRow).SetVaccineDosesNum(Integer.parseInt(value));
                });
                vaccines_panel.add(saveVal,BorderLayout.SOUTH);
                vaccines_panel.setDefaultCloseOperation(vaccines_panel.HIDE_ON_CLOSE);
                vaccines_panel.pack();
                vaccines_panel.show();
                vaccines_panel.toFront();
            });
            southPanel.add(vaccines);
            this.add(southPanel, BorderLayout.SOUTH);
            this.setDefaultCloseOperation(this.HIDE_ON_CLOSE);
            //this.setPreferredSize(new Dimension(800,500));
            this.pack();

        }
        public String[] getSelect(){return select;}
        public JComboBox<String> getSelect_combo(){return select_combo;}
        public StatisticTableView getTable(){return table;}
    }
    private class PanelDrawing extends JPanel {
        DrawingConnectionOnMap drawingConnectionOnMap;
        int x1, x2, y1, y2;
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawingConnectionOnMap = new DrawingConnectionOnMap();
            drawingConnectionOnMap.paintNeighbors(g);
            Font font = new Font("font", Font.PLAIN, 15);
            g.setFont(font);
            for (Settlement settlement : map) {
                x1 = settlement.GetLocation().getPosition().getX();
                y1 = settlement.GetLocation().getPosition().getY() + 10;
                x2 = x1 + settlement.GetLocation().getSize().getWidth();
                y2 = y1 + settlement.GetLocation().getSize().getHeight();
                g.setColor(settlement.GetRamzorColor().GetColor());
                g.fillRect(x1, y1, x2, y2);
                g.setColor(Color.BLACK);
                g.drawString(settlement.GetName(), x1, (y2 - y1) / 2 + y1);
            }
            this.addMouseListener(new RectClickListener());
            Graphics2D gr = (Graphics2D) g;
            gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        private class RectClickListener implements MouseListener {
            int x1, x2, y1, y2;
            @Override
            public void mouseClicked(MouseEvent e) {
                double Xclicked = e.getPoint().getX();
                double Yclicked = e.getPoint().getY();
                for (Settlement settlement : map) {
                    x1 = settlement.GetLocation().getPosition().getX();
                    y1 = settlement.GetLocation().getPosition().getY();
                    x2 = x1 + settlement.GetLocation().getSize().getWidth();
                    y2 = y1 + settlement.GetLocation().getSize().getHeight();
                    if ((Xclicked >= x1 && Xclicked <= x2) && (Yclicked <= y2 && Yclicked >= y1)) {
                        statistic_win.getTable().getTbFilterText().setText(settlement.GetName());
                        statistic_win.getTable().newFilter("Settlement Name");
                        statistic_win.setVisible(true);
                    }
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        }
    }
    private class DrawingConnectionOnMap{
        int x1,x2,y1,y2;
        public void paintNeighbors(Graphics g){
            for (Settlement settlement : map) {
                List<Settlement> neighborsSettlements = settlement.GetNeighbors();
                x1 = settlement.GetLocation().getPosition().getX();
                y1 = settlement.GetLocation().getPosition().getY() + 10;
                x2 = x1 + settlement.GetLocation().getSize().getWidth();
                y2 = y1 + settlement.GetLocation().getSize().getHeight();
                for (int i = 0; i< neighborsSettlements.size(); i++) {
                    Point MiddleOfSize_firstRect = new Point(middleRect(x1, x2), middleRect(y1, y2));
                    x1 = neighborsSettlements.get(i).GetLocation().getPosition().getX();
                    y1 = neighborsSettlements.get(i).GetLocation().getPosition().getY() + 10;
                    x2 = x1 + neighborsSettlements.get(i).GetLocation().getSize().getWidth();
                    y2 = y1 + neighborsSettlements.get(i).GetLocation().getSize().getHeight();
                    Point MiddleOfSize_secondRect = new Point(middleRect(x1, x2), middleRect(y1, y2));
                    int color = (settlement.GetRamzorColor().GetColor().getRGB() + neighborsSettlements.get(i).GetRamzorColor().GetColor().getRGB())/2;
                    g.setColor(new Color(color));
                    g.drawLine(MiddleOfSize_firstRect.getX(), MiddleOfSize_firstRect.getY(), MiddleOfSize_secondRect.getX(), MiddleOfSize_secondRect.getY());
                }
            }
            Graphics2D gr = (Graphics2D) g;
            gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        private int middleRect(int x1,int x2){ return ((x1+x2)/2); }
    }
    private class StatisticTableModel extends AbstractTableModel {
        private final String[] columnNames = {"Settlement Name", "Settlement type", "Ramzor Color", "Sick People%",
                "Vaccine Doses", "Died People Number", "People Number", "Healthy People Number", "Sick People Number"};
        private String selected_choice = "Settlement Name";

        /* public StatisticTableModel(Settlement[] Settlement) {
             this.Settlement = Settlement;
         }*/
        @Override
        public int getRowCount() {
            return settlements.size();
        }

        @Override
        public int getColumnCount() {
            return 9;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Settlement one_settlement = map.at(rowIndex);
            return switch (columnIndex) {
                case 0 -> one_settlement.GetName(); //name of the settlement
                case 1 -> one_settlement.getClass(); // type of settlement
                case 2 -> one_settlement.GetRamzorColor(); //ramzor color
                case 3 -> one_settlement.contagiousPercent() * 100; //contagion persent
                case 4 -> one_settlement.GetNumOfVaccineDoses();  //num of vaccine doses
                case 5 -> one_settlement.GetDiedNum(); //dead num
                case 6 -> one_settlement.GetPeople().size() +one_settlement.GetSickPeopleList().size();  // num of people
                case 7 -> one_settlement.GetPeople().size();         //non sick people
                case 8 -> one_settlement.GetSickPeopleList().size();        // sick people
                default -> null;
            };
        }
        public void setValueAt(String aValue, int row, int col) {
            int i = Integer.parseInt(aValue);
            if (col == 4) {
                map.at(row).SetVaccineDosesNum(i);
                System.out.println(map.at(row));
                this.fireTableDataChanged();
            }
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        public int getColIndex(String name) {
            String[] selected = statistic_win.getSelect();
            JComboBox<String> selected_combo = statistic_win.getSelect_combo();
            selected_combo.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() == selected_combo) {
                        switch (selected_combo.getItemAt(selected_combo.getSelectedIndex())) {
                            case "Settlement Name" -> selected_choice = "Settlement Name";
                            case "Settlement type" -> selected_choice = "Settlement type";
                            case "Ramzor Color" -> selected_choice = "Ramzor Color";
                            case "Vaccine Doses" -> selected_choice = "Vaccine Doses";
                        }
                    }
                }
            });
            for (int i = 0; i < columnNames.length; i++)
                if (selected_choice.equals(columnNames[i]))
                    return i;
            return 0;
        }

        @Override
        public Class getColumnClass(int column) {
            return getValueAt(0, column).getClass();
        }

        public String getSelected_choice() {
            return selected_choice;
        }

        @Override
        public void fireTableDataChanged() {
            fireTableChanged(new TableModelEvent(this, 0, getRowCount() - 1, TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE));
        }
    }
    private class StatisticTableView extends JPanel {
        private JTextField tbFilterText; //text field for the sort
        private TableRowSorter<StatisticTableModel> sorter;
        private JTable table;
        private final StatisticTableModel model;
        public TableRowSorter<StatisticTableModel> getSorter() {
            return sorter;
        }
        public StatisticTableView(List<Settlement> settlements) {
            model = new StatisticTableModel();   //create the model
            table = new JTable(model);               //create the view

            this.setLayout(new BorderLayout());
            table.setRowSorter(sorter = new TableRowSorter<StatisticTableModel>(model));
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.setPreferredScrollableViewportSize(new Dimension(500, 70));
            table.setFillsViewportHeight(true);
            this.add(new JScrollPane(table));
            //tbFilterText.setSize(new Dimension(20,20));
            this.tbFilterText = new JTextField();
            this.add(tbFilterText,BorderLayout.BEFORE_LINE_BEGINS);
            tbFilterText.setToolTipText("Filter Name Column");
            tbFilterText.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {newFilter(model.getSelected_choice()); }
                public void removeUpdate(DocumentEvent e) { newFilter(model.getSelected_choice()); }
                public void changedUpdate(DocumentEvent e) { newFilter(model.getSelected_choice()); }
            });
            //this.setPreferredSize(new Dimension(600,400));
        }

        public void setTbFilterText(JTextField tbFilterText) {
            this.tbFilterText = tbFilterText;
        }
        public JTextField getTbFilterText() {
            return tbFilterText;
        }
        public void newFilter(String colName) {
            try {
                sorter.setRowFilter(RowFilter.regexFilter(tbFilterText.getText(), model.getColIndex(colName)));
            } catch (java.util.regex.PatternSyntaxException e) {
                // If current expression doesn't parse, don't update.
            }
        }
        public StatisticTableModel getModel() {
            return model;
        }
        public int selectedRow() {
            return table.getRowSorter().convertRowIndexToModel(table.getSelectedRow());
        }
    }
    public class VaccinesWindow extends JDialog {
        private JLabel label;
        private JTextField textVal;
        private StatisticTableView table;
        public VaccinesWindow(StatisticTableView table){
            JDialog vaccines_panel=new JDialog((JFrame) SwingUtilities.getWindowAncestor(getParent()),"Vaccines Window",true);
            this.table=table;
            vaccines_panel.setLayout(new BorderLayout());
            label = new JLabel("Please enter number of vaccines");
            vaccines_panel.add(label, BorderLayout.NORTH);
            textVal = new JTextField();
            textVal.setSize(new Dimension(20,20));
            vaccines_panel.add(textVal, BorderLayout.CENTER);
            vaccines_panel.setDefaultCloseOperation(vaccines_panel.HIDE_ON_CLOSE);
            JButton saveVal= new JButton("OK"){
                {
                    setSize(70, 30);
                    setMaximumSize(getSize());
                }
            };
            saveVal.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String value = textVal.getText();
                    int selectedRow = table.selectedRow();
                    table.getModel().setValueAt(value, selectedRow, 4);
                    vaccines_panel.setVisible(false);
                    map.at(selectedRow).SetVaccineDosesNum(Integer.parseInt(value));
                }
            });
       /* table.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                String value = textVal.getText();
                int selectedRow =  table.selectedRow();
                table.getModel().setValueAt(value,selectedRow,4);
            }
        });*/
            vaccines_panel.add(saveVal,BorderLayout.SOUTH);
            vaccines_panel.show();
            vaccines_panel.setDefaultCloseOperation(vaccines_panel.HIDE_ON_CLOSE);
            vaccines_panel.pack();
            vaccines_panel.toFront();
            vaccines_panel.setVisible(true);
        }
    }

}
