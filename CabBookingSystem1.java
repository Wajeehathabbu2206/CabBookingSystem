package in.java.main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

public class CabBookingSystem1 extends JFrame {

    private ArrayList<Cab> cabs = new ArrayList<>();
    private ArrayList<Booking> bookings = new ArrayList<>();
    private JTable cabTable, bookingTable;
    private DefaultTableModel cabModel, bookingModel;

    public CabBookingSystem1() {
        setTitle("Elite Car Booking System");
        setSize(900, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // -------------------- Color Palette --------------------
        Color deepGreen = new Color(0x142C14);
        Color comfyGreen = new Color(0x2D5128);
        Color pineGreen = new Color(0x537B2F);
        Color midGreen = new Color(0x8DA750);
        Color mellowGreen = new Color(0xE4EB9C);

        // -------------------- HEADER PANEL --------------------
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(mellowGreen);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JLabel title = new JLabel("Elite Car Booking System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(deepGreen);

        JLabel info = new JLabel(
            "<html><center>123 Main Street, Cityville<br>" +
            "Contact: +91 9876543210 | Email: info@eliterentals.com</center></html>",
            SwingConstants.CENTER
        );
        info.setFont(new Font("Arial", Font.PLAIN, 14));
        info.setForeground(comfyGreen);

        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(info, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // -------------------- MAIN TABS --------------------
        JTabbedPane tabs = new JTabbedPane();

        // -------------------- CAB MANAGEMENT PANEL --------------------
        JPanel cabPanel = new JPanel(new BorderLayout());
        cabPanel.setBackground(comfyGreen);

        cabModel = new DefaultTableModel(new String[]{"Cab ID", "Driver", "Location", "Type", "Fare"}, 0);
        cabTable = new JTable(cabModel);
        cabPanel.add(new JScrollPane(cabTable), BorderLayout.CENTER);

        JPanel cabForm = new JPanel(new GridLayout(2, 5, 5, 5));
        cabForm.setBackground(midGreen);

        JTextField cabId = new JTextField();
        JTextField driver = new JTextField();
        JTextField location = new JTextField();
        JTextField type = new JTextField();
        JTextField fare = new JTextField();

        cabForm.add(new JLabel("Cab ID"));
        cabForm.add(new JLabel("Driver"));
        cabForm.add(new JLabel("Location"));
        cabForm.add(new JLabel("Type"));
        cabForm.add(new JLabel("Fare"));

        cabForm.add(cabId);
        cabForm.add(driver);
        cabForm.add(location);
        cabForm.add(type);
        cabForm.add(fare);

        JButton addCab = new JButton("Add Cab");
        addCab.setBackground(deepGreen);
        addCab.setForeground(Color.WHITE);

        addCab.addActionListener(e -> {
            try {
                Cab c = new Cab(cabId.getText(), driver.getText(), location.getText(),
                        type.getText(), Double.parseDouble(fare.getText()));
                cabs.add(c);
                cabModel.addRow(new Object[]{c.id, c.driver, c.location, c.type, c.fare});
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input!");
            }
        });

        cabPanel.add(cabForm, BorderLayout.NORTH);
        cabPanel.add(addCab, BorderLayout.SOUTH);

        // -------------------- BOOKING PANEL --------------------
        JPanel bookingPanel = new JPanel(new BorderLayout());
        bookingPanel.setBackground(pineGreen);

        bookingModel = new DefaultTableModel(new String[]{"Booking ID", "Cab ID", "Customer", "From", "To", "Fare"}, 0);
        bookingTable = new JTable(bookingModel);
        bookingPanel.add(new JScrollPane(bookingTable), BorderLayout.CENTER);

        JPanel bookingForm = new JPanel(new GridLayout(2, 5, 5, 5));
        bookingForm.setBackground(mellowGreen);

        JTextField bookingId = new JTextField();
        JTextField cabBookingId = new JTextField();
        JTextField customer = new JTextField();
        JTextField from = new JTextField();
        JTextField to = new JTextField();

        bookingForm.add(new JLabel("Booking ID"));
        bookingForm.add(new JLabel("Cab ID"));
        bookingForm.add(new JLabel("Customer"));
        bookingForm.add(new JLabel("From"));
        bookingForm.add(new JLabel("To"));

        bookingForm.add(bookingId);
        bookingForm.add(cabBookingId);
        bookingForm.add(customer);
        bookingForm.add(from);
        bookingForm.add(to);

        JButton addBooking = new JButton("Book Cab");
        addBooking.setBackground(deepGreen);
        addBooking.setForeground(Color.WHITE);

        addBooking.addActionListener(e -> {
            try {
                Optional<Cab> cabOpt = cabs.stream().filter(c -> c.id.equals(cabBookingId.getText())).findFirst();
                if (cabOpt.isPresent()) {
                    Cab c = cabOpt.get();
                    Booking b = new Booking(bookingId.getText(), c.id, customer.getText(),
                            from.getText(), to.getText(), c.fare);
                    bookings.add(b);
                    bookingModel.addRow(new Object[]{b.id, b.cabId, b.customer, b.from, b.to, b.fare});
                } else {
                    JOptionPane.showMessageDialog(this, "Cab not found!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input!");
            }
        });

        bookingPanel.add(bookingForm, BorderLayout.NORTH);
        bookingPanel.add(addBooking, BorderLayout.SOUTH);

        // -------------------- EXPORT PANEL --------------------
        JPanel exportPanel = new JPanel(new FlowLayout());
        JButton exportCSV = new JButton("Export to CSV");

        exportCSV.addActionListener(e -> exportToCSV());
        exportPanel.add(exportCSV);

        // Add tabs
        tabs.add("Cab Management", cabPanel);
        tabs.add("Booking", bookingPanel);
        tabs.add("Export", exportPanel);

        add(tabs, BorderLayout.CENTER);
    }

    private void exportToCSV() {
        try (PrintWriter pw = new PrintWriter(new File("bookings.csv"))) {
            pw.println("----------------------------------Booking Record------------------------------------------");
            pw.println("BookingID ,CabID ,Customer ,From ,To ,Fare ");
            for (Booking b : bookings) {
                pw.println(b.id + " ," + b.cabId + " ," + b.customer + " ," + b.from + " ," + b.to + " ," + b.fare);
            }
            JOptionPane.showMessageDialog(this, "Exported to bookings.csv");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error exporting CSV");
        }
    }

    // -------------------- INNER CLASSES --------------------
    class Cab {
        String id, driver, location, type;
        double fare;

        Cab(String id, String driver, String location, String type, double fare) {
            this.id = id;
            this.driver = driver;
            this.location = location;
            this.type = type;
            this.fare = fare;
        }
    }

    class Booking {
        String id, cabId, customer, from, to;
        double fare;

        Booking(String id, String cabId, String customer, String from, String to, double fare) {
            this.id = id;
            this.cabId = cabId;
            this.customer = customer;
            this.from = from;
            this.to = to;
            this.fare = fare;
        }
    }

    // -------------------- MAIN METHOD --------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CabBookingSystem1().setVisible(true));
    }
}
