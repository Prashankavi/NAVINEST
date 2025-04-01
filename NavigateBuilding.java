package src;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class NavigateBuilding extends JFrame {

    private JSpinner startFloorSpinner;
    private JSpinner destFloorSpinner;
    private JComboBox<String> startNodeComboBox;
    private JComboBox<String> destNodeComboBox;
    private JLabel outputLabel;
    private JLabel marqueeLabel;
    private JTextArea outputTextArea;

    public NavigateBuilding() {
        setTitle("Navigate Building");
        setSize(2000, 1600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create a panel with a background image
        //JPanel is used for creating a container that can hold a group of components.
        //JPanel can contain other Swing components like buttons, labels, text fields, and even other panels.
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);//super for constructor
                Graphics2D g2d = (Graphics2D) g.create();//2D object
                // Set rendering hints for better image quality
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                ImageIcon backgroundImage = new ImageIcon("image.png");
                Image image = backgroundImage.getImage();
                //(0,0) is the top left corner of the image in the panel
                //width and height are called to set for whole body
                //this: The ImageObserver, often the component itself, to handle image updates.
                g2d.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                g2d.dispose();
            }
        };
        //layout are 8 types border(north,south,west,east,center)
        backgroundPanel.setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("VIGNAN INSTITUTE OF TECHNOLOGY AND SCIENCES", JLabel.CENTER);
        titleLabel.setFont(new Font("Algerian", Font.PLAIN, 40));
        titleLabel.setForeground(Color.RED);
        headerPanel.add(titleLabel, BorderLayout.NORTH);

        marqueeLabel = new JLabel("WELCOME TO THE BUILDING NAVIGATOR", JLabel.CENTER);
        marqueeLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        marqueeLabel.setForeground(new Color(255, 0, 170));
        headerPanel.add(marqueeLabel, BorderLayout.SOUTH);

        JPanel formContainer = new JPanel();
        formContainer.setOpaque(false);
        formContainer.setLayout(new BoxLayout(formContainer, BoxLayout.Y_AXIS));
        formContainer.setBorder(BorderFactory.createEmptyBorder(180, 450, 180, 450)); // Reduce the borders

        JPanel formBox = new JPanel();
        formBox.setOpaque(true);
        formBox.setBackground(new Color(255, 255, 255, 250)); // 191 is approximately 75% of 255
        formBox.setLayout(new GridBagLayout());
        formBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Reduce the insets for tighter spacing

        JLabel formHeader = new JLabel("NAVINEST", JLabel.CENTER);
        formHeader.setFont(new Font("Arial", Font.PLAIN, 30));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formBox.add(formHeader, gbc);

        gbc.gridwidth = 1;

        startFloorSpinner = createFloorSpinner();
        startNodeComboBox = new JComboBox<>();
        JPanel startSection = createFormSection("Starting Point", startFloorSpinner, startNodeComboBox);
        gbc.gridx = 0;
        gbc.gridy = 1;
        formBox.add(startSection, gbc);

        destFloorSpinner = createFloorSpinner();
        destNodeComboBox = new JComboBox<>();
        JPanel destSection = createFormSection("Destination Point", destFloorSpinner, destNodeComboBox);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formBox.add(destSection, gbc);

        JButton findPathButton = new JButton("Find Path");
        findPathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navigate();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formBox.add(findPathButton, gbc);

        outputLabel = new JLabel("", JLabel.CENTER);
        outputLabel.setFont(new Font("Arial", Font.PLAIN, 200));

        gbc.gridx = 0;
        gbc.gridy = 3;
        formBox.add(outputLabel, gbc);

        outputTextArea = new JTextArea(60, 50);
        outputTextArea.setFont(new Font("Arial", Font.PLAIN, 16));
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        outputTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);

        gbc.weighty = 2; // Add this line to set the vertical weight
        gbc.fill = GridBagConstraints.BOTH; // Ensure fill is set to both directions

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        formBox.add(scrollPane, gbc);


        


        formContainer.add(Box.createVerticalGlue());
        formContainer.add(formBox);
        formContainer.add(Box.createVerticalGlue());

        backgroundPanel.add(headerPanel, BorderLayout.NORTH);
        backgroundPanel.add(formContainer, BorderLayout.CENTER);

        add(backgroundPanel);

        // Update nodes initially
        updateNodes(startFloorSpinner, startNodeComboBox);
        updateNodes(destFloorSpinner, destNodeComboBox);
    }

    private JSpinner createFloorSpinner() {
        SpinnerNumberModel model = new SpinnerNumberModel(0, 0, 4, 1);
        JSpinner spinner = new JSpinner(model);
        Dimension spinnerSize = new Dimension(180, 25); // et the desired constant size for the spinner (width, height)

        JFormattedTextField textField = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        //textField.setColumns(5); // Set the preferred size by specifying the number of columns
        textField.setSize(20,90 );
        textField.setPreferredSize(new Dimension(500, 20)); // Increase size of the text field
        textField.setHorizontalAlignment(JTextField.CENTER);
        spinner.setPreferredSize(spinnerSize);
        spinner.setMinimumSize(spinnerSize);
        spinner.setMaximumSize(spinnerSize);

        spinner.addChangeListener(e -> {
            if (e.getSource() == startFloorSpinner) {
                updateNodes(startFloorSpinner, startNodeComboBox);
            } else if (e.getSource() == destFloorSpinner) {
                updateNodes(destFloorSpinner, destNodeComboBox);
            }
        });
        return spinner;
    }

    private JPanel createFormSection(String title, JSpinner floorSpinner, JComboBox<String> nodeComboBox) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(10));// distance from top to enter floor

        JLabel floorLabel = new JLabel("Enter floor:");
        floorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(floorLabel);
        panel.add(floorSpinner);

        JLabel nodeLabel = new JLabel("Enter node:");
        nodeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(nodeLabel);
        panel.add(nodeComboBox);

        return panel;
    }

    

    private void updateNodes(JSpinner floorSpinner, JComboBox<String> nodeComboBox) {
        String floor = floorSpinner.getValue().toString();
        String[] options;

        switch (floor) {
            case "0":
                options = new String[] {
                        "sickroom", "lab no 105", "lab no 106", "lab no 107", "washrooms",
                        "lab no 108A", "lab no 108B", "lab no 108C", "lab no 109C",
                        "lab no 109A/B", "Training/Placement Cell",
                        "entrance", "Administrative Office",
                        "student counter", "Academic section", "principal office/CEO Room",
                        "Examination Branch"
                };
                break;
            case "1":
                options = new String[] {
                        "Room no 201", "Room no 202", "Room no 203", "way to library and exit",
                        "Room no 204", "Washrooms1", "Room no 205", "Room no 206A", "Room no 206B",
                        "Room no 207", "Room no 208", "Room no 209", "Room no 210", "Washrooms2",
                        "Room no 211", "Room no 212", "Room no 213", "Room no 214", "Samskruthi Hall",
                        "Principal Office"
                };
                break;
            case "2":
                options = new String[] {
                        "Room no 301", "Room no 302", "Room no 303", "Room no 304", "Washrooms1",
                        "Room no 305", "Room no 306A", "Room no 306B", "Room no 307", "Room no 308",
                        "Room no 309", "Room no 310", "Room no 311", "Room no 312", "Room no 313",
                        "Washrooms2", "Room no 314", "Room no 315", "Room no 316", "Room no 317,318,319",
                        "Room no 320", "Room no 321", "Room no 322", "Room no 323"
                };
                break;
            case "3":
                options = new String[] {
                        "Room no 401", "Room no 402", "Room no 403 A&B", "Room no 404", "Washrooms1",
                        "Room no 405", "Room no 406", "Room no 407", "Room no 408", "Room no 409",
                        "Room no 410", "Room no 410A", "Room no 411", "Washrooms2", "Room no 412",
                        "Room no 413", "Room no 414", "Room no 416", "Room no 417",
                        "Room no 418", "Room no 419", "Room no 420"
                };
                break;
            case "4":
                options = new String[] {
                        "Room no 501", "Room no 502", "Room no 503A/B", "Room no 504",
                        "Room no 505A/B", "Washrooms1", "Room no 506", "Room no 507", "Room no 508",
                        "Room no 509", "Room no 510", "Room no 511", "Room no 512", "Washrooms2",
                        "Room no 513", "Room no 514", "Room no 515", "Room no 516", "Room no 517A", "Room no 517B",
                        "Room no 518"
                };
                break;
            default:
                options = new String[] {};
                break;
        }

        nodeComboBox.removeAllItems();
        for (String option : options) {
            nodeComboBox.addItem(option);
        }
    }

    private void navigate() {
        int startFloor = (int) startFloorSpinner.getValue();
        String startNode = (String) startNodeComboBox.getSelectedItem();
        int destFloor = (int) destFloorSpinner.getValue();
        String destNode = (String) destNodeComboBox.getSelectedItem();

        StringBuilder outputBuilder = new StringBuilder();

        // Simulate a server response for demonstration purposes
        if (startFloor == 0 && destFloor == 0) {
            outputBuilder.append(BuildingNavigation.ground(startNode, destNode));
        } else if (startFloor == 1 && destFloor == 1) {
            outputBuilder.append(BuildingNavigation.first(startNode, destNode));
        } else if (startFloor == 2 && destFloor == 2) {
            outputBuilder.append(BuildingNavigation.second(startNode, destNode));
        } else if (startFloor == 3 && destFloor == 3) {
            outputBuilder.append(BuildingNavigation.third(startNode, destNode));
        } else if (startFloor == 4 && destFloor == 4) {
            outputBuilder.append(BuildingNavigation.fourth(startNode, destNode));
        } else if (startFloor == 0 && destFloor == 1) {
            outputBuilder.append("Moving from\n");
            outputBuilder.append(BuildingNavigation.groundDouble(startNode));
            outputBuilder.append("way to next floor\n");
            outputBuilder.append(BuildingNavigation.firstDouble1(destNode));
        } else if (startFloor == 0 && destFloor == 2) {
            outputBuilder.append("Moving from\n");
            outputBuilder.append(BuildingNavigation.groundDouble(startNode));
            outputBuilder.append("way to next floor\n");
            outputBuilder.append("way to next floor\n");
            outputBuilder.append(BuildingNavigation.secondDouble1(destNode));
        } else if (startFloor == 0 && destFloor == 3) {
            outputBuilder.append("Moving from\n");
            outputBuilder.append(BuildingNavigation.groundDouble(startNode));
            outputBuilder.append("way to next floor\n");
            outputBuilder.append("way to next floor\n");
            outputBuilder.append("way to next floor\n");
            outputBuilder.append(BuildingNavigation.thirdDouble1(destNode));
        } else if (startFloor == 0 && destFloor == 4) {
            outputBuilder.append("Moving from\n");
            outputBuilder.append(BuildingNavigation.groundDouble(startNode));
            outputBuilder.append("way to next floor\n");
            outputBuilder.append("way to next floor\n");
            outputBuilder.append("way to next floor\n");
            outputBuilder.append("way to next floor\n");
            outputBuilder.append(BuildingNavigation.fourthDouble1(destNode));
        } else if (startFloor == 1 && destFloor == 0) {
            outputBuilder.append("Moving from\n");
            outputBuilder.append(BuildingNavigation.firstDouble(startNode));
            outputBuilder.append("way to down floor\n");
            outputBuilder.append(BuildingNavigation.groundDouble1(destNode));
        } else if (startFloor == 1 && destFloor == 2) {
            outputBuilder.append("Moving from\n");
            outputBuilder.append(BuildingNavigation.firstDouble(startNode));
            outputBuilder.append("way to next floor\n");
            outputBuilder.append(BuildingNavigation.secondDouble1(destNode));
        } else if (startFloor == 1 && destFloor == 3) {
            outputBuilder.append("Moving from\n");
            outputBuilder.append(BuildingNavigation.firstDouble(startNode));
            outputBuilder.append("way to next floor\n");
            outputBuilder.append("way to next floor\n");
            outputBuilder.append(BuildingNavigation.thirdDouble1(destNode));
        } else if (startFloor == 1 && destFloor == 4) {
            outputBuilder.append("Moving from\n");
            outputBuilder.append(BuildingNavigation.firstDouble(startNode));
            outputBuilder.append("way to next floor\n");
            outputBuilder.append("way to next floor\n");
            outputBuilder.append("way to next floor\n");
            outputBuilder.append(BuildingNavigation.fourthDouble1(destNode));
        } else if (startFloor == 2 && destFloor == 0) {
            outputBuilder.append("Moving from\n");
            outputBuilder.append(BuildingNavigation.secondDouble(startNode));
            outputBuilder.append("way to down floor\n");
            outputBuilder.append("way to down floor\n");
            outputBuilder.append(BuildingNavigation.groundDouble1(destNode));
        } else if (startFloor == 2 && destFloor == 1) {
            outputBuilder.append("Moving from\n");
            outputBuilder.append(BuildingNavigation.secondDouble(startNode));
            outputBuilder.append("way to down floor\n");
            outputBuilder.append(BuildingNavigation.firstDouble1(destNode));
        } else if (startFloor == 2 && destFloor == 3) {
            outputBuilder.append("Moving from\n");
            outputBuilder.append(BuildingNavigation.secondDouble(startNode));
            outputBuilder.append("way to next floor\n");
            outputBuilder.append(BuildingNavigation.thirdDouble1(destNode));
        } else if (startFloor == 2 && destFloor == 4) {
            outputBuilder.append("Moving from\n");
            outputBuilder.append(BuildingNavigation.secondDouble(startNode));
            outputBuilder.append("way to next floor\n");
            outputBuilder.append("way to next floor\n");
            outputBuilder.append(BuildingNavigation.fourthDouble1(destNode));
        } else if (startFloor == 3 && destFloor == 0) {
            outputBuilder.append("Moving from\n");
            outputBuilder.append(BuildingNavigation.thirdDouble(startNode));
            outputBuilder.append("way to down floor\n");
            outputBuilder.append("way to down floor\n");
            outputBuilder.append("way to down floor\n");
            outputBuilder.append(BuildingNavigation.groundDouble1(destNode));
        } else if (startFloor == 3 && destFloor == 1) {
            outputBuilder.append("Moving from\n");
            outputBuilder.append(BuildingNavigation.thirdDouble(startNode));
            outputBuilder.append("way to down floor\n");
            outputBuilder.append("way to down floor\n");
            outputBuilder.append(BuildingNavigation.firstDouble1(destNode));
        } else if (startFloor == 3 && destFloor == 2) {
            outputBuilder.append("Moving from\n");
            outputBuilder.append(BuildingNavigation.thirdDouble(startNode));
            outputBuilder.append("way to down floor\n");
            outputBuilder.append(BuildingNavigation.secondDouble1(destNode));
        } else if (startFloor == 3 && destFloor == 4) {
            outputBuilder.append("Moving from\n");
            outputBuilder.append(BuildingNavigation.thirdDouble(startNode));
            outputBuilder.append("way to next floor\n");
            outputBuilder.append(BuildingNavigation.fourthDouble1(destNode));
        } else if (startFloor == 4 && destFloor == 0) {
            outputBuilder.append("Moving from\n");
            outputBuilder.append(BuildingNavigation.fourthDouble(startNode));
            outputBuilder.append("way to down floor\n");
            outputBuilder.append("way to down floor\n");
            outputBuilder.append("way to down floor\n");
            outputBuilder.append("way to down floor\n");
            outputBuilder.append(BuildingNavigation.groundDouble1(destNode));
        } else if (startFloor == 4 && destFloor == 1) {
            outputBuilder.append("Moving from\n");
            outputBuilder.append(BuildingNavigation.fourthDouble(startNode));
            outputBuilder.append("way to down floor\n");
            outputBuilder.append("way to down floor\n");
            outputBuilder.append("way to down floor\n");
            outputBuilder.append(BuildingNavigation.firstDouble1(destNode));
        } else if (startFloor == 4 && destFloor == 2) {
            outputBuilder.append("Moving from\n");
            outputBuilder.append(BuildingNavigation.fourthDouble(startNode));
            outputBuilder.append("way to down floor\n");
            outputBuilder.append("way to down floor\n");
            outputBuilder.append(BuildingNavigation.secondDouble1(destNode));
        } else if (startFloor == 4 && destFloor == 3) {
            outputBuilder.append("Moving from\n");
            outputBuilder.append(BuildingNavigation.fourthDouble(startNode));
            outputBuilder.append("way to down floor\n");
            outputBuilder.append("way to down floor\n");
            outputBuilder.append(BuildingNavigation.thirdDouble1(destNode));
        } else {
            outputBuilder.append("Invalid floor selection.");
        }

        outputTextArea.setText(outputBuilder.toString());
    }


    
    


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NavigateBuilding example = new NavigateBuilding();
            example.setVisible(true);
        });
    }
}

class Node {
    String name;
    Node prev;
    Node next;

    Node(String name) {
        this.name = name;
        this.prev = null;
        this.next = null;
    }
}

class BuildingNavigation {

    // Function to create a new node
    public static Node createNode(String name) {
        return new Node(name);
    }

    // Function to insert a node at the end of the list (circular)
    public static void insertNode(Node[] head, String name) {
        Node newNode = createNode(name);
        if (head[0] == null) {
            head[0] = newNode;
            head[0].next = head[0];
            head[0].prev = head[0];
        } else {
            Node temp = head[0].prev;
            temp.next = newNode;
            newNode.prev = temp;
            newNode.next = head[0];
            head[0].prev = newNode;
        }
    }

    // Function to insert a node at the end of a non-circular doubly linked list
    public static void insertNodeDouble(Node[] head, String name) {
        Node newNode = createNode(name);
        if (head[0] == null) {
            head[0] = newNode;
        } else {
            Node temp = head[0];
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
            newNode.prev = temp;
        }
    }

    // Function to traverse the list from starting to destination (forward)
    public static String traverseForward(Node start, Node dest) {
        StringBuilder output = new StringBuilder();
        output.append("Path from ").append(start.name).append(" to ").append(dest.name).append(":\n");
        Node current = start;
        while (true) {
            output.append(current.name).append(" -> ");
            if (current == dest) {
                break;
            }
            current = current.next;
            if (current == start) {
                break;
            }
        }
        output.append("\n");
        return output.toString();
    }

    // Function to traverse the list from destination to starting (backward)
    public static String traverseBackward(Node start, Node dest) {
        StringBuilder output = new StringBuilder();
        output.append("Path from ").append(start.name).append(" to ").append(dest.name).append(":\n");
        Node current = start;
        while (true) {
            output.append(current.name).append(" -> ");
            if (current == dest) {
                break;
            }
            current = current.prev;
            if (current == start) {
                break;
            }
        }
        output.append("\n");
        return output.toString();
    }

    // Function to find the minimum number of nodes between start and destination
    public static String findMin(Node start, Node dest) {
        int countForward = 0;
        Node currentForward = start;
        while (true) {
            countForward++;
            if (currentForward == dest) {
                break;
            }
            currentForward = currentForward.next;
            if (currentForward == start) {
                break;
            }
        }

        int countBackward = 0;
        Node currentBackward = start;
        while (true) {
            countBackward++;
            if (currentBackward == dest) {
                break;
            }
            currentBackward = currentBackward.prev;
            if (currentBackward == start) {
                break;
            }
        }

        if (countForward < countBackward) {
            return traverseForward(start, dest);
            // return countForward;
        } else {
            return traverseBackward(start, dest);
            // return countBackward;
        }
    }

    // Function to initialize and find the optimal path on the ground floor
    // (circular list)
    public static String ground(String startNode, String destNode) {
        Node[] head = new Node[1];
        insertNode(head, "sickroom");
        insertNode(head, "lab no 105");
        insertNode(head, "lab no 106");
        insertNode(head, "lab no 107");
        insertNode(head, "washrooms");
        insertNode(head, "lab no 108A");
        insertNode(head, "lab no 108B");
        insertNode(head, "lab no 108C");
        insertNode(head, "lab no 109C");
        insertNode(head, "lab no 109A/B");
        insertNode(head, "steps down");
        insertNode(head, "Training/Placement Cell");
        insertNode(head, "steps to entrance");
        insertNode(head, "entrance");
        insertNode(head, "stepsup");
        insertNode(head, "Administrative Office");
        insertNode(head, "student counter");
        insertNode(head, "Academic section");
        insertNode(head, "principaloffice/CEO Room");
        insertNode(head, "Examination Branch");
        Node start = head[0];
        Node dest = head[0];
        while (start != null && !start.name.equals(startNode)) {
            start = start.next;
        }
        while (dest != null && !dest.name.equals(destNode)) {
            dest = dest.next;
        }

        if (start == null || dest == null) {
            return "Starting node or destination node not found.";
        }

        StringBuilder output = new StringBuilder();
        output.append(traverseForward(start, dest));
        output.append("\n\n\n\n\n");
        output.append(traverseBackward(start, dest));
        output.append("\n\n\n\n\n");
        output.append("Optimal path has ").append(findMin(start, dest));
        return output.toString();
    }

    // Function to initialize and find the optimal path on the first floor (circular
    // list)
    public static String first(String startNode, String destNode) {
        Node[] head = new Node[1];
        insertNode(head, "Room no 201");
        insertNode(head, "Room no 202");
        insertNode(head, "Room no 203");
        insertNode(head, "way to library and exit");
        insertNode(head, "Room no 204");
        insertNode(head, "Washrooms1");
        insertNode(head, "Room no 205");
        insertNode(head, "Room no 206A");
        insertNode(head, "Room no 206B");
        insertNode(head, "Room no 207");
        insertNode(head, "Room no 208");
        insertNode(head, "Room no 209");
        insertNode(head, "Room no 210");
        insertNode(head, "Washrooms2");
        insertNode(head, "Room no 211");
        insertNode(head, "Room no 212");
        insertNode(head, "Room no 213");
        insertNode(head, "Room no 214");
        insertNode(head, "Samskruthi Hall");
        insertNode(head, "Principal Office");

        Node start = head[0];
        Node dest = head[0];
        while (start != null && !start.name.equals(startNode)) {
            start = start.next;
        }
        while (dest != null && !dest.name.equals(destNode)) {
            dest = dest.next;
        }

        if (start == null || dest == null) {
            return "Starting node or destination node not found.";
        }

        StringBuilder output = new StringBuilder();
        output.append(traverseForward(start, dest));
        output.append("\n\n\n\n\n");
        output.append(traverseBackward(start, dest));
        output.append("\n\n\n\n\n");
        output.append("Optimal path has ").append(findMin(start, dest));
        return output.toString();
    }

    // Function to initialize and find the optimal path on the second floor
    // (circular list)
    public static String second(String startNode, String destNode) {
        Node[] head = new Node[1];
        insertNode(head, "Room no 301");
        insertNode(head, "Room no 302");
        insertNode(head, "Room no 303");
        insertNode(head, "Room no 304");
        insertNode(head, "Washrooms1");
        insertNode(head, "Room no 305");
        insertNode(head, "Room no 306A");
        insertNode(head, "Room no 306B");
        insertNode(head, "Room no 307");
        insertNode(head, "Room no 308");
        insertNode(head, "Room no 309");
        insertNode(head, "Room no 310");
        insertNode(head, "Room no 311");
        insertNode(head, "Room no 312");
        insertNode(head, "Room no 313");
        insertNode(head, "Washrooms2");
        insertNode(head, "Room no 314");
        insertNode(head, "Room no 315");
        insertNode(head, "Room no 316");
        insertNode(head, "Room no 317,318,319");
        insertNode(head, "Room no 320");
        insertNode(head, "Room no 321");
        insertNode(head, "Room no 322");
        insertNode(head, "Room no 323");

        Node start = head[0];
        Node dest = head[0];
        while (start != null && !start.name.equals(startNode)) {
            start = start.next;
        }
        while (dest != null && !dest.name.equals(destNode)) {
            dest = dest.next;
        }

        if (start == null || dest == null) {
            return "Starting node or destination node not found.";
        }

        StringBuilder output = new StringBuilder();
        output.append(traverseForward(start, dest));
        output.append("\n\n\n\n\n");
        output.append(traverseBackward(start, dest));
        output.append("\n\n\n\n\n");
        output.append("Optimal path has ").append(findMin(start, dest));
        return output.toString();
    }

    // Function to initialize and find the optimal path on the third floor (circular
    // list)
    public static String third(String startNode, String destNode) {
        Node[] head = new Node[1];
        insertNode(head, "Room no 401");
        insertNode(head, "Room no 402");
        insertNode(head, "Room no 403 A&B");
        insertNode(head, "Room no 404");
        insertNode(head, "Washrooms1");
        insertNode(head, "Room no 405");
        insertNode(head, "Room no 406");
        insertNode(head, "Room no 407");
        insertNode(head, "Room no 408");
        insertNode(head, "Room no 409");
        insertNode(head, "Room no 410");
        insertNode(head, "Room no 410A");
        insertNode(head, "Room no 411");
        insertNode(head, "Washrooms2");
        insertNode(head, "Room no 412");
        insertNode(head, "Room no 413");
        insertNode(head, "Room no 414");
        insertNode(head, "Room no 416");
        insertNode(head, "Room no 417");
        insertNode(head, "Room no 418");
        insertNode(head, "Room no 419");
        insertNode(head, "Room no 420");

        Node start = head[0];
        Node dest = head[0];
        while (start != null && !start.name.equals(startNode)) {
            start = start.next;
        }
        while (dest != null && !dest.name.equals(destNode)) {
            dest = dest.next;
        }

        if (start == null || dest == null) {
            return "Starting node or destination node not found.";
        }

        StringBuilder output = new StringBuilder();
        output.append(traverseForward(start, dest));
        output.append("\n\n\n\n\n");
        output.append(traverseBackward(start, dest));
        output.append("\n\n\n\n\n");
        output.append("Optimal path has ").append(findMin(start, dest));
        return output.toString();
    }

    // Function to initialize and find the optimal path on the fourth floor
    // (circular list)
    public static String fourth(String startNode, String destNode) {
        Node[] head = new Node[1];
        insertNode(head, "Room no 501");
        insertNode(head, "Room no 502");
        insertNode(head, "Room no 503A/B");
        insertNode(head, "Room no 504");
        insertNode(head, "Room no 505A/B");
        insertNode(head, "Washrooms1");
        insertNode(head, "Room no 506");
        insertNode(head, "Room no 507");
        insertNode(head, "Room no 508");
        insertNode(head, "Room no 509");
        insertNode(head, "Room no 510");
        insertNode(head, "Room no 511");
        insertNode(head, "Room no 512");
        insertNode(head, "Washrooms2");
        insertNode(head, "Room no 513");
        insertNode(head, "Room no 514");
        insertNode(head, "Room no 515");
        insertNode(head, "Room no 516");
        insertNode(head, "Room no 517A");
        insertNode(head, "Room no 517B");
        insertNode(head, "Room no 518");

        Node start = head[0];
        Node dest = head[0];
        while (start != null && !start.name.equals(startNode)) {
            start = start.next;
        }
        while (dest != null && !dest.name.equals(destNode)) {
            dest = dest.next;
        }

        if (start == null || dest == null) {
            return "Starting node or destination node not found.";
        }

        StringBuilder output = new StringBuilder();
        output.append(traverseForward(start, dest));
        output.append("\n\n\n\n\n");
        output.append(traverseBackward(start, dest));
        output.append("\n\n\n\n\n");
        output.append("Optimal path has ").append(findMin(start, dest));
        return output.toString();
    }

    // Function to find the node and print the list from that node (non-circular
    // doubly linked list)
    public static String starts(Node head, String nodeName) {
        Node current = head;
        while (current != null) {
            if (current.name.equals(nodeName)) {
                break;
            }
            current = current.next;
        }

        if (current == null) {
            return "Node not found.";
        }

        int distanceToStart = 0;
        int distanceToEnd = 0;

        Node temp = head;
        while (temp != null && temp != current) {
            distanceToStart++;
            temp = temp.next;
        }

        temp = current.next;
        while (temp != null) {
            distanceToEnd++;
            temp = temp.next;
        }

        StringBuilder output = new StringBuilder();
        if (distanceToStart <= distanceToEnd) {
            temp = current;
            while (temp != null) {
                output.append(temp.name).append(" -> ");
                temp = temp.prev;
            }
            output.append("\n");
        } else {
            temp = current;
            while (temp != null) {
                output.append(temp.name).append(" -> ");
                temp = temp.next;
            }
            output.append("\n");
        }
        return output.toString();
    }

    public static String destiny(Node head, String nodeName) {
        Node current = head;
        while (current != null) {
            if (current.name.equals(nodeName)) {
                break;
            }
            current = current.next;
        }

        if (current == null) {
            return "Node not found.";
        }

        int distanceToStart = 0;
        int distanceToEnd = 0;

        Node temp = head;
        while (temp != null && temp != current) {
            distanceToStart++;
            temp = temp.next;
        }

        temp = current.next;
        while (temp != null) {
            distanceToEnd++;
            temp = temp.next;
        }

        StringBuilder output = new StringBuilder();
        if (distanceToStart <= distanceToEnd) {
            temp = head;
            while (temp != current) {
                output.append(temp.name).append(" -> ");
                temp = temp.next;
            }
            output.append(current.name).append("\n");
        } else {
            temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }

            while (temp != current) {
                output.append(temp.name).append(" -> ");
                temp = temp.prev;
            }
            output.append(current.name).append("\n");
        }
        return output.toString();
    }

    // Function to initialize and print path from a specific node on the ground
    // floor (non-circular list)
    public static String groundDouble(String nodeName) {
        Node[] head = new Node[1];
        insertNodeDouble(head, "Administrative Office");
        insertNodeDouble(head, "Administrative Office");
        insertNodeDouble(head, "student counter");
        insertNodeDouble(head, "Academic section");
        insertNodeDouble(head, "principaloffice/CEO Room");
        insertNodeDouble(head, "Examination Branch");
        insertNodeDouble(head, "sickroom");
        insertNodeDouble(head, "lab no 105");
        insertNodeDouble(head, "lab no 106");
        insertNodeDouble(head, "lab no 107");
        insertNodeDouble(head, "washrooms");
        insertNodeDouble(head, "lab no 108A");
        insertNodeDouble(head, "lab no 108B");
        insertNodeDouble(head, "lab no 108C");
        insertNodeDouble(head, "lab no 109C");
        insertNodeDouble(head, "lab no 109A/B");
        insertNodeDouble(head, "steps down");
        insertNodeDouble(head, "Training/Placement Cell");
        insertNodeDouble(head, "steps to entrance");
        insertNodeDouble(head, "entrance");
        insertNodeDouble(head, "stepsup");
        return starts(head[0], nodeName);
    }

    // Function to initialize and print path from a specific node on the first floor
    // (non-circular list)
    public static String firstDouble(String nodeName) {
        Node[] head = new Node[1];
        insertNodeDouble(head, "Room no 201");
        insertNodeDouble(head, "Room no 202");
        insertNodeDouble(head, "Room no 203");
        insertNodeDouble(head, "way to library and exit");
        insertNodeDouble(head, "Room no 204");
        insertNodeDouble(head, "Washrooms1");
        insertNodeDouble(head, "Room no 205");
        insertNodeDouble(head, "Room no 206A");
        insertNodeDouble(head, "Room no 206B");
        insertNodeDouble(head, "Room no 207");
        insertNodeDouble(head, "Room no 208");
        insertNodeDouble(head, "Room no 209");
        insertNodeDouble(head, "Room no 210");
        insertNodeDouble(head, "Washrooms2");
        insertNodeDouble(head, "Room no 211");
        insertNodeDouble(head, "Room no 212");
        insertNodeDouble(head, "Room no 213");
        insertNodeDouble(head, "Room no 214");
        insertNodeDouble(head, "Samskruthi Hall");
        insertNodeDouble(head, "Principal Office");

        return starts(head[0], nodeName);
    }

    public static String secondDouble(String nodeName) {
        Node[] head = new Node[1];
        insertNodeDouble(head, "Room no 301");
        insertNodeDouble(head, "Room no 302");
        insertNodeDouble(head, "Room no 303");
        insertNodeDouble(head, "Room no 304");
        insertNodeDouble(head, "Washrooms1");
        insertNodeDouble(head, "Room no 305");
        insertNodeDouble(head, "Room no 306A");
        insertNodeDouble(head, "Room no 306B");
        insertNodeDouble(head, "Room no 307");
        insertNodeDouble(head, "Room no 308");
        insertNodeDouble(head, "Room no 309");
        insertNodeDouble(head, "Room no 310");
        insertNodeDouble(head, "Room no 311");
        insertNodeDouble(head, "Room no 312");
        insertNodeDouble(head, "Room no 313");
        insertNodeDouble(head, "Washrooms2");
        insertNodeDouble(head, "Room no 314");
        insertNodeDouble(head, "Room no 315");
        insertNodeDouble(head, "Room no 316");
        insertNodeDouble(head, "Room no 317,318,319");
        insertNodeDouble(head, "Room no 320");
        insertNodeDouble(head, "Room no 321");
        insertNodeDouble(head, "Room no 322");
        insertNodeDouble(head, "Room no 323");

        return starts(head[0], nodeName);
    }

    public static String thirdDouble(String nodeName) {
        Node[] head = new Node[1];
        insertNodeDouble(head, "Room no 401");
        insertNodeDouble(head, "Room no 402");
        insertNodeDouble(head, "Room no 403 A&B");
        insertNodeDouble(head, "Room no 404");
        insertNodeDouble(head, "Washrooms1");
        insertNodeDouble(head, "Room no 405");
        insertNodeDouble(head, "Room no 406");
        insertNodeDouble(head, "Room no 407");
        insertNodeDouble(head, "Room no 408");
        insertNodeDouble(head, "Room no 409");
        insertNodeDouble(head, "Room no 410");
        insertNodeDouble(head, "Room no 410A");
        insertNodeDouble(head, "Room no 411");
        insertNodeDouble(head, "Washrooms2");
        insertNodeDouble(head, "Room no 412");
        insertNodeDouble(head, "Room no 413");
        insertNodeDouble(head, "Room no 414");
        insertNodeDouble(head, "Room no 416");
        insertNodeDouble(head, "Room no 417");
        insertNodeDouble(head, "Room no 418");
        insertNodeDouble(head, "Room no 419");
        insertNodeDouble(head, "Room no 420");

        return starts(head[0], nodeName);
    }

    public static String fourthDouble(String nodeName) {
        Node[] head = new Node[1];
        insertNodeDouble(head, "Room no 501");
        insertNodeDouble(head, "Room no 502");
        insertNodeDouble(head, "Room no 503A/B");
        insertNodeDouble(head, "Room no 504");
        insertNodeDouble(head, "Room no 505A/B");
        insertNodeDouble(head, "Washrooms1");
        insertNodeDouble(head, "Room no 506");
        insertNodeDouble(head, "Room no 507");
        insertNodeDouble(head, "Room no 508");
        insertNodeDouble(head, "Room no 509");
        insertNodeDouble(head, "Room no 510");
        insertNodeDouble(head, "Room no 511");
        insertNodeDouble(head, "Room no 512");
        insertNodeDouble(head, "Washrooms2");
        insertNodeDouble(head, "Room no 513");
        insertNodeDouble(head, "Room no 514");
        insertNodeDouble(head, "Room no 515");
        insertNodeDouble(head, "Room no 516");
        insertNodeDouble(head, "Room no 517A");
        insertNodeDouble(head, "Room no 517B");
        insertNodeDouble(head, "Room no 518");

        return starts(head[0], nodeName);
    }

    public static String groundDouble1(String nodeName) {
        Node[] head = new Node[1];
        insertNodeDouble(head, "Administrative Office");
        insertNodeDouble(head, "student counter");
        insertNodeDouble(head, "Academic section");
        insertNodeDouble(head, "principaloffice/CEO Room");
        insertNodeDouble(head, "Examination Branch");
        insertNodeDouble(head, "sickroom");
        insertNodeDouble(head, "lab no 105");
        insertNodeDouble(head, "lab no 106");
        insertNodeDouble(head, "lab no 107");
        insertNodeDouble(head, "washrooms");
        insertNodeDouble(head, "lab no 108A");
        insertNodeDouble(head, "lab no 108B");
        insertNodeDouble(head, "lab no 108C");
        insertNodeDouble(head, "lab no 109C");
        insertNodeDouble(head, "lab no 109A/B");
        insertNodeDouble(head, "steps down");
        insertNodeDouble(head, "Training/Placement Cell");
        insertNodeDouble(head, "steps to entrance");
        insertNodeDouble(head, "entrance");
        insertNodeDouble(head, "stepsup");
        return destiny(head[0], nodeName);
    }

    public static String firstDouble1(String nodeName) {
        Node[] head = new Node[1];
        insertNodeDouble(head, "Room no 201");
        insertNodeDouble(head, "Room no 202");
        insertNodeDouble(head, "Room no 203");
        insertNodeDouble(head, "way to library and exit");
        insertNodeDouble(head, "Room no 204");
        insertNodeDouble(head, "Washrooms1");
        insertNodeDouble(head, "Room no 205");
        insertNodeDouble(head, "Room no 206A");
        insertNodeDouble(head, "Room no 206B");
        insertNodeDouble(head, "Room no 207");
        insertNodeDouble(head, "Room no 208");
        insertNodeDouble(head, "Room no 209");
        insertNodeDouble(head, "Room no 210");
        insertNodeDouble(head, "Washrooms2");
        insertNodeDouble(head, "Room no 211");
        insertNodeDouble(head, "Room no 212");
        insertNodeDouble(head, "Room no 213");
        insertNodeDouble(head, "Room no 214");
        insertNodeDouble(head, "Samskruthi Hall");
        insertNodeDouble(head, "Principal Office");

        return destiny(head[0], nodeName);
    }

    public static String secondDouble1(String nodeName) {
        Node[] head = new Node[1];
        insertNodeDouble(head, "Room no 301");
        insertNodeDouble(head, "Room no 302");
        insertNodeDouble(head, "Room no 303");
        insertNodeDouble(head, "Room no 304");
        insertNodeDouble(head, "Washrooms1");
        insertNodeDouble(head, "Room no 305");
        insertNodeDouble(head, "Room no 306A");
        insertNodeDouble(head, "Room no 306B");
        insertNodeDouble(head, "Room no 307");
        insertNodeDouble(head, "Room no 308");
        insertNodeDouble(head, "Room no 309");
        insertNodeDouble(head, "Room no 310");
        insertNodeDouble(head, "Room no 311");
        insertNodeDouble(head, "Room no 312");
        insertNodeDouble(head, "Room no 313");
        insertNodeDouble(head, "Washrooms2");
        insertNodeDouble(head, "Room no 314");
        insertNodeDouble(head, "Room no 315");
        insertNodeDouble(head, "Room no 316");
        insertNodeDouble(head, "Room no 317,318,319");
        insertNodeDouble(head, "Room no 320");
        insertNodeDouble(head, "Room no 321");
        insertNodeDouble(head, "Room no 322");
        insertNodeDouble(head, "Room no 323");

        return destiny(head[0], nodeName);
    }

    public static String thirdDouble1(String nodeName) {
        Node[] head = new Node[1];
        insertNodeDouble(head, "Room no 401");
        insertNodeDouble(head, "Room no 402");
        insertNodeDouble(head, "Room no 403 A&B");
        insertNodeDouble(head, "Room no 404");
        insertNodeDouble(head, "Washrooms1");
        insertNodeDouble(head, "Room no 405");
        insertNodeDouble(head, "Room no 406");
        insertNodeDouble(head, "Room no 407");
        insertNodeDouble(head, "Room no 408");
        insertNodeDouble(head, "Room no 409");
        insertNodeDouble(head, "Room no 410");
        insertNodeDouble(head, "Room no 410A");
        insertNodeDouble(head, "Room no 411");
        insertNodeDouble(head, "Washrooms2");
        insertNodeDouble(head, "Room no 412");
        insertNodeDouble(head, "Room no 413");
        insertNodeDouble(head, "Room no 414");
        insertNodeDouble(head, "Room no 416");
        insertNodeDouble(head, "Room no 417");
        insertNodeDouble(head, "Room no 418");
        insertNodeDouble(head, "Room no 419");
        insertNodeDouble(head, "Room no 420");

        return destiny(head[0], nodeName);
    }


    public static String fourthDouble1(String nodeName) {
        Node[] head = new Node[1];
        insertNodeDouble(head, "Room no 501");
        insertNodeDouble(head, "Room no 502");
        insertNodeDouble(head, "Room no 503A/B");
        insertNodeDouble(head, "Room no 504");
        insertNodeDouble(head, "Room no 505A/B");
        insertNodeDouble(head, "Washrooms1");
        insertNodeDouble(head, "Room no 506");
        insertNodeDouble(head, "Room no 507");
        insertNodeDouble(head, "Room no 508");
        insertNodeDouble(head, "Room no 509");
        insertNodeDouble(head, "Room no 510");
        insertNodeDouble(head, "Room no 511");
        insertNodeDouble(head, "Room no 512");
        insertNodeDouble(head, "Washrooms2");
        insertNodeDouble(head, "Room no 513");
        insertNodeDouble(head, "Room no 514");
        insertNodeDouble(head, "Room no 515");
        insertNodeDouble(head, "Room no 516");
        insertNodeDouble(head, "Room no 517A");
        insertNodeDouble(head, "Room no 517B");
        insertNodeDouble(head, "Room no 518");

        return destiny(head[0], nodeName);
    }
}