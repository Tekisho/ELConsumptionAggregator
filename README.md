# ELConsumptionAggregator
A lightweight JavaFX utility for parsing Excel files (.xls/.xlsx) of a specific format to calculate and export average hourly power consumption per day.
---

## Features

- Import and parse Excel spreadsheets (.xls, .xlsx) with a predefined data structure
- Calculate average hourly electricity consumption per day
- Export processed data back to Excel format
- User-friendly graphical interface built with JavaFX
- Supports multi-platform deployment (Windows, Linux, macOS)
---

## Requirements


- For running the installer version, no separate Java installation is required as the runtime is bundled.


- For building from source or running the JAR manually, Java Development Kit (JDK) 17 or higher and JavaFX SDK 21.0.7 are required.

---

## Installation

### Option 1: Using the Installer (Recommended)

Download and run the latest installer for your platform from the [Releases](https://github.com/Tekisho/ELConsumptionAggregator/releases) page.  
This will install the application with all necessary dependencies, including a bundled Java runtime.

### Option 2: Build from Source

1. **Clone this repository:**
   ```sh
   git clone https://github.com/Tekisho/ELConsumptionAggregator.git
   cd ELConsumptionAggregator
   ```

2. **Build the project with Maven:**
   ```sh
   mvn package
   ```
   After building, the JAR file will appear in the `target` folder.


3. **Download and unpack the JavaFX SDK:**  
   Get JavaFX SDK from [https://gluonhq.com/products/javafx/](https://gluonhq.com/products/javafx/)  
   and unpack it, for example, to `C:\Work_Programs\JavaFX\javafx-sdk-21.0.7`

---

## Contributing

- Issue Tracker: [https://github.com/Tekisho/ELConsumptionAggregator/issues](https://github.com/Tekisho/ELConsumptionAggregator/issues)
- Source Code: [https://github.com/Tekisho/ELConsumptionAggregator](https://github.com/Tekisho/ELConsumptionAggregator)

Pull requests and suggestions are very welcome :3

---

## Support

If you encounter issues or have questions, please [open an issue](https://github.com/Tekisho/ELConsumptionAggregator/issues).

---

## License

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for details.