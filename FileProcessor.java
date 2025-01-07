package FileProcessor_pkg;


import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileProcessor {

    // Class to hold Instrument details
    static class InstrumentDetails {
        String id;
        String name;
        String isin;
        double unitPrice;

        InstrumentDetails(String id, String name, String isin, double unitPrice) {
            this.id = id;
            this.name = name;
            this.isin = isin;
            this.unitPrice = unitPrice;
        }
    }

    // Class to hold Position details
    static class PositionDetails {
        String id;
        String instrumentId;
        int quantity;

        PositionDetails(String id, String instrumentId, int quantity) {
            this.id = id;
            this.instrumentId = instrumentId;
            this.quantity = quantity;
        }
    }

    public static void main(String[] args) {
        String inputDir = "C://Users//Thivashini//eclipse-workspace//Data_Processor//src//FileProcessor_pkg//app/";
        String outputDir ="C://Users//Thivashini//eclipse-workspace//Data_Processor//src//FileProcessor_pkg//app/";

        String instrumentFile = inputDir + "InstrumentDetails.csv";
        String positionFile = inputDir + "PositionDetails.csv";
        String outputFile = outputDir + "PositionReport.csv";

        try {
            // Load data from input files
            Map<String, InstrumentDetails> instruments = loadInstrumentDetails(instrumentFile);
            List<PositionDetails> positions = loadPositionDetails(positionFile);

            // Process data and generate the output report
            generatePositionReport(instruments, positions, outputFile);

            System.out.println("PositionReport.csv has been generated successfully in " + outputDir);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error processing files: " + e.getMessage());
        }
    }

    // Method to load InstrumentDetails.csv
    private static Map<String, InstrumentDetails> loadInstrumentDetails(String filePath) throws IOException {
        Map<String, InstrumentDetails> instruments = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String id = parts[0].trim();
                String name = parts[1].trim();
                String isin = parts[2].trim();
                double unitPrice = Double.parseDouble(parts[3].trim());
                instruments.put(id, new InstrumentDetails(id, name, isin, unitPrice));
            }
        }
        return instruments;
    }

    // Method to load PositionDetails.csv
    private static List<PositionDetails> loadPositionDetails(String filePath) throws IOException {
        List<PositionDetails> positions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String id = parts[0].trim();
                String instrumentId = parts[1].trim();
                int quantity = Integer.parseInt(parts[2].trim());
                positions.add(new PositionDetails(id, instrumentId, quantity));
            }
        }
        return positions;
    }

    // Method to generate PositionReport.csv
    private static void generatePositionReport(Map<String, InstrumentDetails> instruments,
                                               List<PositionDetails> positions, String outputFilePath) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath))) {
            // Write header
            bw.write("ID,PositionID,ISIN,Quantity,Total Price");
            bw.newLine();

            // Process each position and generate the report
            for (PositionDetails position : positions) {
                InstrumentDetails instrument = instruments.get(position.instrumentId);
                if (instrument != null) {
                    double totalPrice = position.quantity * instrument.unitPrice;
                    bw.write(String.format("%s,%s,%s,%d,%.2f",
                            instrument.id, position.id, instrument.isin, position.quantity, totalPrice));
                    bw.newLine();
                }
            }
        }
    }
}
