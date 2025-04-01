# NavigateBuilding Application

## Overview
The NavigateBuilding application is a Java Swing-based graphical user interface (GUI) designed to help users navigate through a building by selecting starting and destination points on different floors. The application provides a user-friendly interface to select floors and nodes (rooms or areas) and displays the path between them.

## Features
- **Floor Selection**: Users can select the starting and destination floors using a spinner.
- **Node Selection**: Based on the selected floor, users can choose from a list of available nodes (rooms or areas) using combo boxes.
- **Pathfinding**: The application calculates and displays the path between the selected nodes, including directions for moving between floors.
- **Dynamic UI**: The UI updates dynamically based on user selections, providing a seamless experience.

## Requirements
- Java Development Kit (JDK) 8 or higher
- An IDE or text editor for Java development (e.g., IntelliJ IDEA, Eclipse, or NetBeans)

## Installation

1. **Clone the Repository**: Clone this repository to your local machine using the following command:

   ```bash
   git clone https://github.com/Prashankavi/NAVINEST.git
   ```

2. **Set Up the Project**: Open the project in your preferred IDE.

3. **Add Background Image**: Ensure that the image file (`image.png`) is placed in the correct directory where the application can access it. This image will be used as the background for the GUI.

4. **Compile and Run**: Compile the Java files and run the `NavigateBuilding` class. You can do this from your IDE or using the command line:

   ```bash
   javac NavigateBuilding.java
   java NavigateBuilding
   ```

## Usage

1. **Launch the Application**: Run the `NavigateBuilding` class to open the application window.
2. **Select Starting Point**:
   - Use the floor spinner to select the starting floor.
   - Choose the starting node from the dropdown list.
3. **Select Destination Point**:
   - Use the floor spinner to select the destination floor.
   - Choose the destination node from the dropdown list.
4. **Find Path**:
   - Click the "Find Path" button to calculate and display the path between the selected nodes in the text area.
5. **View Output**:
   - The output area will show the directions for navigating from the starting point to the destination, including any necessary transitions between floors.

## Code Structure
- `NavigateBuilding.java`: The main class that sets up the GUI and handles user interactions.
- `Node.java`: A class representing a node in the building (e.g., a room or area).
- `BuildingNavigation.java`: A class containing methods for pathfinding and node management.

## Customization
- **Nodes and Paths**: You can customize the nodes and paths by modifying the `updateNodes` and `BuildingNavigation` methods to reflect the actual layout of your building.
- **UI Design**: The appearance of the application can be modified by changing the layout, colors, fonts, and images used in the GUI.

## Contributing
Contributions are welcome! If you have suggestions for improvements or new features, feel free to submit a pull request or open an issue.

## License
This project is licensed under the MIT License. See the LICENSE file for more details.

## Acknowledgments
- This application was developed using Java Swing for the GUI.
- Special thanks to the contributors and the community for their support and feedback.
