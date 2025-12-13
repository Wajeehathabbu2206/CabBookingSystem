package in.java.main;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class CabBookingSystemEnhanced extends JFrame {

    private ArrayList<Cab> cabs = new ArrayList<>();
    private ArrayList<Booking> bookings = new ArrayList<>();
    private JTable cabTable, bookingTable;
    private DefaultTableModel cabModel, bookingModel;
    
    // Dashboard components
    private JLabel totalCabsLabel, activeBookingsLabel, availableCabsLabel, revenueLabel;
    private JPanel statsPanel;
    private JComboBox<String> timeRangeCombo;
    private Timer dashboardTimer;

    // Enhanced Color Palette
    private final Color PRIMARY_COLOR = new Color(0x142C14);
    private final Color SECONDARY_COLOR = new Color(0x2D5128);
    private final Color ACCENT_COLOR = new Color(0x537B2F);
    private final Color LIGHT_ACCENT = new Color(0x8DA750);
    private final Color BACKGROUND_COLOR = new Color(0xE4EB9C);
    private final Color CARD_COLOR = new Color(0xFFFFFF);
    private final Color TEXT_PRIMARY = new Color(0x1A1A1A);
    private final Color TEXT_SECONDARY = new Color(0x666666);
    private final Color SUCCESS_COLOR = new Color(0x28A745);
    private final Color WARNING_COLOR = new Color(0xFFC107);
    private final Color DANGER_COLOR = new Color(0xDC3545);

    public CabBookingSystemEnhanced() {
        setTitle("🚗 Elite Car Booking System");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // Apply modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize sample data
        initializeSampleData();

        // -------------------- ENHANCED HEADER PANEL --------------------
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // -------------------- DYNAMIC STATISTICS DASHBOARD --------------------
        statsPanel = createStatsPanel();
        add(statsPanel, BorderLayout.WEST);

        // -------------------- MAIN TABS WITH MODERN STYLING --------------------
        JTabbedPane tabs = createModernTabbedPane();
        
        // Cab Management Panel
        JPanel cabPanel = createCabManagementPanel();
        tabs.add("🚕 Cab Management", cabPanel);

        // Booking Panel
        JPanel bookingPanel = createBookingPanel();
        tabs.add("📖 Booking Management", bookingPanel);

        // Analytics Panel
        JPanel analyticsPanel = createAnalyticsPanel();
        tabs.add("📊 Analytics", analyticsPanel);

        // Export Panel
        JPanel exportPanel = createExportPanel();
        tabs.add("💾 Export Data", exportPanel);

        add(tabs, BorderLayout.CENTER);

        // Initialize dashboard timer for real-time updates
        initializeDashboardTimer();
    }

    private void initializeSampleData() {
        // Add some sample cabs
        cabs.add(new Cab("CAB001", "John Doe", "Downtown", "Sedan", 250.0));
        cabs.add(new Cab("CAB002", "Jane Smith", "Airport", "SUV", 350.0));
        cabs.add(new Cab("CAB003", "Mike Johnson", "Mall", "Hatchback", 200.0));
        
        // Add some sample bookings
        bookings.add(new Booking("B001", "CAB001", "Alice Brown", "Downtown", "Airport", 250.0));
        bookings.add(new Booking("B002", "CAB002", "Bob Wilson", "Airport", "City Center", 350.0));
    }

    private void initializeDashboardTimer() {
        dashboardTimer = new Timer(5000, e -> updateDashboard()); // Update every 5 seconds
        dashboardTimer.start();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Main title with icon
        JLabel title = new JLabel(" Elite Car Booking System", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Real-time clock
        JLabel clockLabel = new JLabel();
        clockLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        clockLabel.setForeground(LIGHT_ACCENT);
        clockLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        // Update clock every second
        Timer clockTimer = new Timer(1000, e -> {
            clockLabel.setText(new java.text.SimpleDateFormat("HH:mm:ss | EEE, MMM d").format(new java.util.Date()));
        });
        clockTimer.start();

        // Contact info with better styling
        JLabel info = new JLabel(
            "<html><center style='color: #E4EB9C; font-size: 14px;'>"
            + "📍 123 Main Street, Cityville<br>"
            + "📞 +91 9876543210 | ✉ info@eliterentals.com</center></html>",
            SwingConstants.CENTER
        );
        info.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        headerPanel.add(title, BorderLayout.CENTER);
        headerPanel.add(clockLabel, BorderLayout.EAST);
        headerPanel.add(info, BorderLayout.SOUTH);

        return headerPanel;
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBackground(SECONDARY_COLOR);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        statsPanel.setPreferredSize(new Dimension(250, 0));

        // Stats title with refresh button
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(SECONDARY_COLOR);
        
        JLabel statsTitle = new JLabel("Live Dashboard");
        statsTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        statsTitle.setForeground(Color.WHITE);
        
        JButton refreshBtn = createIconButton("🔄", "Refresh Dashboard");
        refreshBtn.addActionListener(e -> updateDashboard());
        refreshBtn.setPreferredSize(new Dimension(30, 30));
        
        titlePanel.add(statsTitle, BorderLayout.WEST);
        titlePanel.add(refreshBtn, BorderLayout.EAST);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        statsPanel.add(titlePanel);

        // Time range selector
        JLabel timeLabel = new JLabel("Time Range:");
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        String[] timeRanges = {"Today", "This Week", "This Month", "All Time"};
        timeRangeCombo = new JComboBox<>(timeRanges);
        styleComboBox(timeRangeCombo);
        timeRangeCombo.addActionListener(e -> updateDashboard());
        
        statsPanel.add(timeLabel);
        statsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        statsPanel.add(timeRangeCombo);
        statsPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Stats cards
        totalCabsLabel = createDashboardStatCard("Total Cabs", "0", PRIMARY_COLOR);
        activeBookingsLabel = createDashboardStatCard("Active Bookings", "0", ACCENT_COLOR);
        availableCabsLabel = createDashboardStatCard("Available Cabs", "0", SUCCESS_COLOR);
        revenueLabel = createDashboardStatCard("Revenue", "₹0", WARNING_COLOR);

        statsPanel.add(totalCabsLabel);
        statsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        statsPanel.add(activeBookingsLabel);
        statsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        statsPanel.add(availableCabsLabel);
        statsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        statsPanel.add(revenueLabel);

        // Quick actions
        statsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        statsPanel.add(createQuickActionsPanel());

        // Initial dashboard update
        updateDashboard();

        return statsPanel;
    }

    private JLabel createDashboardStatCard(String title, String value, Color color) {
        JLabel card = new JLabel(
            "<html><div style='text-align:center; padding:15px; border-radius:8px; background:" + 
            String.format("#%06x", color.getRGB() & 0xFFFFFF) + "; color:white;'>" +
            "<b style='font-size:24px;'>" + value + "</b><br>" +
            "<span style='font-size:12px;'>" + title + "</span></div></html>"
        );
        card.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add click effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(CabBookingSystemEnhanced.this, 
                    title + " Details:\n" + getStatDetails(title), 
                    title + " Information", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        return card;
    }

    private String getStatDetails(String title) {
        switch (title) {
            case "Total Cabs":
                return "Sedan: " + cabs.stream().filter(c -> c.type.equals("Sedan")).count() + 
                       "\nSUV: " + cabs.stream().filter(c -> c.type.equals("SUV")).count() +
                       "\nHatchback: " + cabs.stream().filter(c -> c.type.equals("Hatchback")).count();
            case "Active Bookings":
                return "Confirmed: " + bookings.size() + 
                       "\nCompleted: " + bookings.stream().filter(b -> b.status != null && b.status.equals("Completed")).count();
            case "Available Cabs":
                long available = cabs.stream().filter(c -> 
                    bookings.stream().noneMatch(b -> b.cabId.equals(c.id) && 
                    (b.status == null || !b.status.equals("Completed")))).count();
                return "Available: " + available + "\nOn Trip: " + (cabs.size() - available);
            case "Revenue":
                double totalRevenue = bookings.stream().mapToDouble(b -> b.fare).sum();
                return "Today: ₹" + totalRevenue + 
                       "\nThis Week: ₹" + (totalRevenue * 7) +
                       "\nThis Month: ₹" + (totalRevenue * 30);
            default:
                return "No details available";
        }
    }

    private JPanel createQuickActionsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(SECONDARY_COLOR);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(LIGHT_ACCENT), 
            "Quick Actions", 
            TitledBorder.LEFT, 
            TitledBorder.TOP, 
            new Font("Segoe UI", Font.BOLD, 12), 
            Color.WHITE
        ));

        String[] actions = {"Add New Cab", "View All Bookings", "Generate Report", "System Settings"};
        Color[] colors = {ACCENT_COLOR, SUCCESS_COLOR, WARNING_COLOR, DANGER_COLOR};

        for (int i = 0; i < actions.length; i++) {
            JButton btn = createQuickActionButton(actions[i], colors[i]);
            panel.add(btn);
            if (i < actions.length - 1) {
                panel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }

        return panel;
    }

    private JButton createQuickActionButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 35));
        button.setBorder(BorderFactory.createEmptyBorder(8, 5, 8, 5));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });

        button.addActionListener(e -> handleQuickAction(text));

        return button;
    }

    private void handleQuickAction(String action) {
        switch (action) {
            case "Add New Cab":
                showQuickAddCabDialog();
                break;
            case "View All Bookings":
                JOptionPane.showMessageDialog(this, 
                    "Total Bookings: " + bookings.size() + 
                    "\nActive: " + bookings.stream().filter(b -> b.status == null || !b.status.equals("Completed")).count(),
                    "Booking Summary", 
                    JOptionPane.INFORMATION_MESSAGE);
                break;
            case "Generate Report":
                generateQuickReport();
                break;
            case "System Settings":
                showSystemSettings();
                break;
        }
    }

    private void showQuickAddCabDialog() {
        JDialog dialog = new JDialog(this, "Add New Cab", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(CARD_COLOR);

        JTextField idField = createStyledTextField();
        JTextField driverField = createStyledTextField();
        JTextField locationField = createStyledTextField();
        JTextField typeField = createStyledTextField();
        JTextField fareField = createStyledTextField();

        panel.add(new JLabel("Cab ID:"));
        panel.add(idField);
        panel.add(new JLabel("Driver:"));
        panel.add(driverField);
        panel.add(new JLabel("Location:"));
        panel.add(locationField);
        panel.add(new JLabel("Type:"));
        panel.add(typeField);
        panel.add(new JLabel("Fare:"));
        panel.add(fareField);

        JButton addBtn = createStyledButton("Add Cab", SUCCESS_COLOR);
        JButton cancelBtn = createStyledButton("Cancel", DANGER_COLOR);

        addBtn.addActionListener(e -> {
            try {
                if (idField.getText().isEmpty() || driverField.getText().isEmpty() || 
                    locationField.getText().isEmpty() || typeField.getText().isEmpty() || 
                    fareField.getText().isEmpty()) {
                    showErrorDialog("Please fill all fields!");
                    return;
                }

                Cab c = new Cab(idField.getText(), driverField.getText(), locationField.getText(),
                        typeField.getText(), Double.parseDouble(fareField.getText()));
                cabs.add(c);
                updateDashboard();
                showSuccessDialog("Cab added successfully!");
                dialog.dispose();
                
            } catch (NumberFormatException ex) {
                showErrorDialog("Please enter a valid fare amount!");
            }
        });

        cancelBtn.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addBtn);
        buttonPanel.add(cancelBtn);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void generateQuickReport() {
        double totalRevenue = bookings.stream().mapToDouble(b -> b.fare).sum();
        long totalCabs = cabs.size();
        long activeBookings = bookings.stream().filter(b -> b.status == null || !b.status.equals("Completed")).count();

        String report = String.format(
            "📊 QUICK REPORT\n\n" +
            "Total Cabs: %d\n" +
            "Active Bookings: %d\n" +
            "Total Revenue: ₹%.2f\n" +
            "Average Fare: ₹%.2f\n" +
            "Utilization Rate: %.1f%%",
            totalCabs, activeBookings, totalRevenue,
            totalRevenue / Math.max(bookings.size(), 1),
            (activeBookings * 100.0) / Math.max(totalCabs, 1)
        );

        JOptionPane.showMessageDialog(this, report, "Quick Report", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showSystemSettings() {
        JOptionPane.showMessageDialog(this, 
            "System Settings:\n\n" +
            "• Auto-refresh: Enabled\n" +
            "• Notifications: On\n" +
            "• Data Backup: Daily\n" +
            "• Theme: Dark/Light\n" +
            "• Language: English",
            "System Settings", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateDashboard() {
        // Update statistics
        long totalCabs = cabs.size();
        long activeBookings = bookings.stream().filter(b -> b.status == null || !b.status.equals("Completed")).count();
        long availableCabs = cabs.stream().filter(c -> 
            bookings.stream().noneMatch(b -> b.cabId.equals(c.id) && 
            (b.status == null || !b.status.equals("Completed")))).count();
        double totalRevenue = bookings.stream().mapToDouble(b -> b.fare).sum();

        // Simulate some dynamic changes for demo
        Random rand = new Random();
        if (rand.nextDouble() < 0.3) { // 30% chance to simulate a new booking
            totalRevenue += rand.nextInt(100) + 50;
        }

        // Update labels with animation effect
        updateStatLabel(totalCabsLabel, "Total Cabs", String.valueOf(totalCabs), PRIMARY_COLOR);
        updateStatLabel(activeBookingsLabel, "Active Bookings", String.valueOf(activeBookings), ACCENT_COLOR);
        updateStatLabel(availableCabsLabel, "Available Cabs", String.valueOf(availableCabs), SUCCESS_COLOR);
        updateStatLabel(revenueLabel, "Revenue", String.format("₹%.2f", totalRevenue), WARNING_COLOR);

        // Update tables
        refreshTables();
    }

    private void updateStatLabel(JLabel label, String title, String value, Color color) {
        label.setText(
            "<html><div style='text-align:center; padding:15px; border-radius:8px; background:" + 
            String.format("#%06x", color.getRGB() & 0xFFFFFF) + "; color:white;'>" +
            "<b style='font-size:24px;'>" + value + "</b><br>" +
            "<span style='font-size:12px;'>" + title + "</span></div></html>"
        );
    }

    private void refreshTables() {
        // Refresh cab table
        if (cabModel != null) {
            cabModel.setRowCount(0);
            for (Cab cab : cabs) {
                boolean isAvailable = bookings.stream().noneMatch(b -> 
                    b.cabId.equals(cab.id) && (b.status == null || !b.status.equals("Completed")));
                cabModel.addRow(new Object[]{
                    cab.id, cab.driver, cab.location, cab.type, 
                    "₹" + cab.fare, isAvailable ? "Available" : "On Trip"
                });
            }
        }

        // Refresh booking table
        if (bookingModel != null) {
            bookingModel.setRowCount(0);
            for (Booking booking : bookings) {
                String status = booking.status != null ? booking.status : "Confirmed";
                bookingModel.addRow(new Object[]{
                    booking.id, booking.cabId, booking.customer, 
                    booking.from, booking.to, "₹" + booking.fare, status
                });
            }
        }
    }

    private JButton createIconButton(String icon, String tooltip) {
        JButton button = new JButton(icon);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBackground(SECONDARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        button.setToolTipText(tooltip);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(LIGHT_ACCENT);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(SECONDARY_COLOR);
            }
        });
        
        return button;
    }

    private void styleComboBox(JComboBox<String> combo) {
        combo.setBackground(CARD_COLOR);
        combo.setForeground(TEXT_PRIMARY);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        combo.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(TEXT_SECONDARY, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    private JTabbedPane createModernTabbedPane() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(SECONDARY_COLOR);
        tabs.setForeground(Color.WHITE);
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return tabs;
    }

    // Add the Analytics Panel
    private JPanel createAnalyticsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Analytics & Insights");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(PRIMARY_COLOR);

        // Create sample analytics content
        JPanel analyticsContent = new JPanel(new GridLayout(2, 2, 15, 15));
        analyticsContent.setBackground(BACKGROUND_COLOR);

        // Add analytics cards
        analyticsContent.add(createAnalyticsCard("Revenue Trend", "", "Growing steadily", SUCCESS_COLOR));
        analyticsContent.add(createAnalyticsCard("Popular Routes", "", "Airport → Downtown", ACCENT_COLOR));
        analyticsContent.add(createAnalyticsCard("Peak Hours", "", "8-10 AM, 5-7 PM", WARNING_COLOR));
        analyticsContent.add(createAnalyticsCard("Customer Rating", "", "4.8/5.0", PRIMARY_COLOR));

        panel.add(title, BorderLayout.NORTH);
        panel.add(analyticsContent, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createAnalyticsCard(String title, String icon, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(color.darker(), 2),
            BorderFactory.createEmptyBorder(20, 15, 20, 15)
        ));

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        iconLabel.setForeground(Color.WHITE);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        valueLabel.setForeground(Color.WHITE);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(color);
        contentPanel.add(titleLabel, BorderLayout.NORTH);
        contentPanel.add(valueLabel, BorderLayout.CENTER);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(contentPanel, BorderLayout.CENTER);

        // Add click effect
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(CabBookingSystemEnhanced.this, 
                    title + " Analysis:\n\n" + getAnalyticsDetails(title), 
                    "Analytics Details", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return card;
    }

    private String getAnalyticsDetails(String title) {
        switch (title) {
            case "Revenue Trend":
                return "Last 7 days revenue:\n" +
                       "• Today: ₹" + (bookings.stream().mapToDouble(b -> b.fare).sum()) + "\n" +
                       "• Growth: +12% from last week";
            case "Popular Routes":
                return "Most booked routes:\n" +
                       "1. Airport → Downtown (35%)\n" +
                       "2. Mall → City Center (25%)\n" +
                       "3. Downtown → Airport (20%)";
            case "Peak Hours":
                return "Busiest hours:\n" +
                       "• Morning: 8:00 AM - 10:00 AM\n" +
                       "• Evening: 5:00 PM - 7:00 PM\n" +
                       "• Recommendation: Deploy more cabs";
            case "Customer Rating":
                return "Customer feedback:\n" +
                       "• Average Rating: 4.8/5.0\n" +
                       "• Positive Reviews: 94%\n" +
                       "• Response Time: < 5 mins";
            default:
                return "No analytics available";
        }
    }

    private JPanel createCabManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title with refresh button
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Cab Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(PRIMARY_COLOR);

        JButton refreshBtn = createIconButton("", "Refresh Table");
        refreshBtn.addActionListener(e -> refreshTables());

        titlePanel.add(title, BorderLayout.WEST);
        titlePanel.add(refreshBtn, BorderLayout.EAST);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Enhanced Table
        cabModel = new DefaultTableModel(new String[]{"Cab ID", "Driver", "Location", "Type", "Fare", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        cabTable = createStyledTable(cabModel);
        JScrollPane scrollPane = new JScrollPane(cabTable);
        scrollPane.setBorder(new LineBorder(PRIMARY_COLOR, 1));

        // Form Panel with modern styling
        JPanel formPanel = createCabFormPanel();

        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.SOUTH);

        // Initial table population
        refreshTables();

        return panel;
    }

    private JPanel createCabFormPanel() {
        JPanel formPanel = new JPanel(new GridLayout(2, 6, 10, 10));
        formPanel.setBackground(CARD_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Labels
        String[] labels = {"Cab ID", "Driver", "Location", "Type", "Fare", "Action"};
        for (String label : labels) {
            JLabel lbl = new JLabel(label);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lbl.setForeground(TEXT_PRIMARY);
            formPanel.add(lbl);
        }

        // Input fields
        JTextField cabId = createStyledTextField();
        JTextField driver = createStyledTextField();
        JTextField location = createStyledTextField();
        JTextField type = createStyledTextField();
        JTextField fare = createStyledTextField();

        formPanel.add(cabId);
        formPanel.add(driver);
        formPanel.add(location);
        formPanel.add(type);
        formPanel.add(fare);

        // Add Cab button with modern styling
        JButton addCab = createStyledButton("Add Cab", PRIMARY_COLOR);
        addCab.addActionListener(e -> {
            try {
                if (cabId.getText().isEmpty() || driver.getText().isEmpty() || 
                    location.getText().isEmpty() || type.getText().isEmpty() || 
                    fare.getText().isEmpty()) {
                    showErrorDialog("Please fill all fields!");
                    return;
                }

                Cab c = new Cab(cabId.getText(), driver.getText(), location.getText(),
                        type.getText(), Double.parseDouble(fare.getText()));
                cabs.add(c);
                updateDashboard();
                
                clearFields(cabId, driver, location, type, fare);
                showSuccessDialog("Cab added successfully!");
                
            } catch (NumberFormatException ex) {
                showErrorDialog("Please enter a valid fare amount!");
            }
        });

        formPanel.add(addCab);
        return formPanel;
    }

    private JPanel createBookingPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title with refresh button
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Booking Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(PRIMARY_COLOR);

        JButton refreshBtn = createIconButton("🔄", "Refresh Table");
        refreshBtn.addActionListener(e -> refreshTables());

        titlePanel.add(title, BorderLayout.WEST);
        titlePanel.add(refreshBtn, BorderLayout.EAST);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Enhanced Booking Table
        bookingModel = new DefaultTableModel(new String[]{"--------Booking ID", "Cab ID", "Customer", "From", "To", "Fare", "Status---------"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        bookingTable = createStyledTable(bookingModel);
        JScrollPane scrollPane = new JScrollPane(bookingTable);
        scrollPane.setBorder(new LineBorder(PRIMARY_COLOR, 1));

        // Form Panel
        JPanel formPanel = createBookingFormPanel();

        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.SOUTH);

        // Initial table population
        refreshTables();

        return panel;
    }

    private JPanel createBookingFormPanel() {
        JPanel formPanel = new JPanel(new GridLayout(2, 6, 10, 10));
        formPanel.setBackground(CARD_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(PRIMARY_COLOR, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Labels
        String[] labels = {"Booking ID", "Cab ID", "Customer", "From", "To", "Action"};
        for (String label : labels) {
            JLabel lbl = new JLabel(label);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lbl.setForeground(TEXT_PRIMARY);
            formPanel.add(lbl);
        }

        // Input fields
        JTextField bookingId = createStyledTextField();
        JTextField cabBookingId = createStyledTextField();
        JTextField customer = createStyledTextField();
        JTextField from = createStyledTextField();
        JTextField to = createStyledTextField();

        formPanel.add(bookingId);
        formPanel.add(cabBookingId);
        formPanel.add(customer);
        formPanel.add(from);
        formPanel.add(to);

        // Book Cab button
        JButton addBooking = createStyledButton("Book Cab", ACCENT_COLOR);
        addBooking.addActionListener(e -> {
            try {
                if (bookingId.getText().isEmpty() || cabBookingId.getText().isEmpty() || 
                    customer.getText().isEmpty() || from.getText().isEmpty() || 
                    to.getText().isEmpty()) {
                    showErrorDialog("Please fill all fields!");
                    return;
                }

                Optional<Cab> cabOpt = cabs.stream().filter(c -> c.id.equals(cabBookingId.getText())).findFirst();
                if (cabOpt.isPresent()) {
                    Cab c = cabOpt.get();
                    Booking b = new Booking(bookingId.getText(), c.id, customer.getText(),
                            from.getText(), to.getText(), c.fare);
                    bookings.add(b);
                    updateDashboard();
                    
                    clearFields(bookingId, cabBookingId, customer, from, to);
                    showSuccessDialog("Booking confirmed successfully!");
                } else {
                    showErrorDialog("Cab not found! Please check the Cab ID.");
                }
            } catch (Exception ex) {
                showErrorDialog("Invalid input! Please check your data.");
            }
        });

        formPanel.add(addBooking);
        return formPanel;
    }

    private JPanel createExportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel title = new JLabel("Export Data", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(PRIMARY_COLOR);

        JLabel description = new JLabel(
            "<html><center style='color: #666666; font-size: 14px;'>"
            + "Export your booking data to CSV format for external use.<br>"
            + "The file will be saved as 'bookings.csv' in the application directory.</center></html>",
            SwingConstants.CENTER
        );

        JButton exportBtn = createStyledButton(" Export to CSV", PRIMARY_COLOR);
        exportBtn.setPreferredSize(new Dimension(200, 50));
        exportBtn.addActionListener(e -> exportToCSV());

        contentPanel.add(title, gbc);
        contentPanel.add(description, gbc);
        contentPanel.add(exportBtn, gbc);

        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JComponent) {
                    ((JComponent) c).setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                }
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xF5F5F5));
                }
                return c;
            }
        };

        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(35);
        table.setSelectionBackground(LIGHT_ACCENT);
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setGridColor(new Color(0xDDDDDD));

        // Style table header
        JTableHeader header = table.getTableHeader();
        header.setBackground(PRIMARY_COLOR);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setReorderingAllowed(false);

        return table;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(TEXT_SECONDARY, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void clearFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }

    private void showSuccessDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void exportToCSV() {
        try (PrintWriter pw = new PrintWriter(new File("bookings.csv"))) {
            pw.println("BookingID,CabID,Customer,From,To,Fare,Status");
            for (Booking b : bookings) {
                pw.println(b.id + "," + b.cabId + "," + b.customer + "," + b.from + "," + b.to + "," + b.fare + "," + b.status);
            }
            showSuccessDialog("Data exported successfully to bookings.csv!");
        } catch (Exception e) {
            showErrorDialog("Error exporting data: " + e.getMessage());
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
        String id, cabId, customer, from, to, status;
        double fare;

        Booking(String id, String cabId, String customer, String from, String to, double fare) {
            this.id = id;
            this.cabId = cabId;
            this.customer = customer;
            this.from = from;
            this.to = to;
            this.fare = fare;
            this.status = "Confirmed";
        }
    }

    @Override
    public void dispose() {
        // Stop the timer when closing the application
        if (dashboardTimer != null) {
            dashboardTimer.stop();
        }
        super.dispose();
    }

    // -------------------- MAIN METHOD --------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            CabBookingSystemEnhanced system = new CabBookingSystemEnhanced();
            system.setVisible(true);
        });
    }
}
