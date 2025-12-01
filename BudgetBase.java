// base code for student budget assessment
// Students do not need to use this code in their assessment, fine to junk it and do something different!

// user can enter in wages and loans and calculate total income
//
// To see GUI, run with java and select Box Url from Codio top line menu
//
// Layout - Uses GridBag layout in a straightforward way, every component has a (column, row) position in the UI grid
// Not the prettiest layout, but relatively straightforward
// Students who use IntelliJ or Eclipse may want to use the UI designers in these IDEs , instead of GridBagLayout

// Ehud Reiter Aug 204

package Budget;

// Swing imports
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;

public class BudgetBase extends JPanel {
    
    // UI Components
    private JFrame topLevelFrame;
    private GridBagConstraints layoutConstraints = new GridBagConstraints();
    
    // Income fields
    private JTextField wagesField, loansField, otherIncomeField;
    private JComboBox<String> wagesPeriod, loansPeriod, otherIncomePeriod;
    
    // Expense fields
    private JTextField foodField, rentField, otherExpenseField;
    private JComboBox<String> foodPeriod, rentPeriod, otherExpensePeriod;
    
    // Result fields
    private JTextField totalIncomeField, totalExpenseField, surplusField;
    private JButton calculateButton, exitButton, undoButton;
    
    // Undo functionality
    private Stack<BudgetState> undoStack = new Stack<>();
    private static final int MAX_UNDO_LEVELS = 10;
    
    // Constants for period conversion
    private static final double WEEKS_IN_MONTH = 4.3333333;
    private static final double MONTHS_IN_YEAR = 12.0;
    private static final double WEEKS_IN_YEAR = 52.0;
    
    public BudgetBase(JFrame frame) {
        topLevelFrame = frame;
        setLayout(new GridBagLayout());
        initComponents();
        initListeners();
        saveState(); // Initial state
    }
    
    private void initComponents() {
        int row = 0;
        
        // INCOME Section
        addComponent(new JLabel("INCOME"), 0, row++, 2, 1);
        
        // Wages
        addComponent(new JLabel("Wages"), 0, row);
        wagesField = createNumberField();
        addComponent(wagesField, 1, row);
        wagesPeriod = createPeriodComboBox();
        addComponent(wagesPeriod, 2, row++);
        
        // Loans
        addComponent(new JLabel("Loans"), 0, row);
        loansField = createNumberField();
        addComponent(loansField, 1, row);
        loansPeriod = createPeriodComboBox();
        addComponent(loansPeriod, 2, row++);
        
        // Other Income
        addComponent(new JLabel("Other Income"), 0, row);
        otherIncomeField = createNumberField();
        addComponent(otherIncomeField, 1, row);
        otherIncomePeriod = createPeriodComboBox();
        addComponent(otherIncomePeriod, 2, row++);
        
        // EXPENSES Section
        addComponent(new JLabel("EXPENSES"), 0, row++, 2, 1);
        
        // Food
        addComponent(new JLabel("Food"), 0, row);
        foodField = createNumberField();
        addComponent(foodField, 1, row);
        foodPeriod = createPeriodComboBox();
        addComponent(foodPeriod, 2, row++);
        
        // Rent
        addComponent(new JLabel("Rent"), 0, row);
        rentField = createNumberField();
        addComponent(rentField, 1, row);
        rentPeriod = createPeriodComboBox();
        addComponent(rentPeriod, 2, row++);
        
        // Other Expenses
        addComponent(new JLabel("Other Expenses"), 0, row);
        otherExpenseField = createNumberField();
        addComponent(otherExpenseField, 1, row);
        otherExpensePeriod = createPeriodComboBox();
        addComponent(otherExpensePeriod, 2, row++);
        
        // RESULTS Section
        addComponent(new JLabel("RESULTS"), 0, row++, 2, 1);
        
        // Total Income
        addComponent(new JLabel("Total Income"), 0, row);
        totalIncomeField = createResultField();
        addComponent(totalIncomeField, 1, row++, 2, 1);
        
        // Total Expenses
        addComponent(new JLabel("Total Expenses"), 0, row);
        totalExpenseField = createResultField();
        addComponent(totalExpenseField, 1, row++, 2, 1);
        
        // Surplus/Deficit
        addComponent(new JLabel("Surplus/Deficit"), 0, row);
        surplusField = createResultField();
        addComponent(surplusField, 1, row++, 2, 1);
        
        // BUTTONS
        JPanel buttonPanel = new JPanel(new FlowLayout());
        calculateButton = new JButton("Calculate");
        undoButton = new JButton("Undo");
        exitButton = new JButton("Exit");
        
        buttonPanel.add(calculateButton);
        buttonPanel.add(undoButton);
        buttonPanel.add(exitButton);
        
        addComponent(buttonPanel, 0, row++, 3, 1);
    }
    
    private JTextField createNumberField() {
        JTextField field = new JTextField("", 10);
        field.setHorizontalAlignment(JTextField.RIGHT);
        return field;
    }
    
    private JTextField createResultField() {
        JTextField field = new JTextField("0.00", 10);
        field.setHorizontalAlignment(JTextField.RIGHT);
        field.setEditable(false);
        return field;
    }
    
    private JComboBox<String> createPeriodComboBox() {
        return new JComboBox<>(new String[]{"Weekly", "Monthly", "Yearly"});
    }
    
    private void addComponent(Component component, int gridx, int gridy) {
        addComponent(component, gridx, gridy, 1, 1);
    }
    
    private void addComponent(Component component, int gridx, int gridy, int gridwidth, int gridheight) {
        layoutConstraints.fill = GridBagConstraints.HORIZONTAL;
        layoutConstraints.gridx = gridx;
        layoutConstraints.gridy = gridy;
        layoutConstraints.gridwidth = gridwidth;
        layoutConstraints.gridheight = gridheight;
        layoutConstraints.insets = new Insets(2, 2, 2, 2);
        add(component, layoutConstraints);
    }
    
    private void initListeners() {
        // Exit button
        exitButton.addActionListener(e -> System.exit(0));
        
        // Calculate button
        calculateButton.addActionListener(e -> calculateTotals());
        
        // Undo button
        undoButton.addActionListener(e -> undo());
        
        // Spreadsheet behavior - update on focus loss and Enter key
        ActionListener updateListener = e -> {
            saveState();
            calculateTotals();
        };
        
        FocusAdapter focusAdapter = new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                saveState();
                calculateTotals();
            }
        };
        
        // Add listeners to all input fields
        JTextField[] fields = {wagesField, loansField, otherIncomeField, 
                              foodField, rentField, otherExpenseField};
        for (JTextField field : fields) {
            field.addActionListener(updateListener);
            field.addFocusListener(focusAdapter);
        }
        
        // Add listeners to period combo boxes
        JComboBox<?>[] periods = {wagesPeriod, loansPeriod, otherIncomePeriod,
                                 foodPeriod, rentPeriod, otherExpensePeriod};
        for (JComboBox<?> period : periods) {
            period.addActionListener(updateListener);
        }
    }
    
    public void calculateTotals() {
        double totalIncome = calculateTotalIncome();
        double totalExpenses = calculateTotalExpenses();
        double surplus = totalIncome - totalExpenses;
        
        totalIncomeField.setText(String.format("%.2f", totalIncome));
        totalExpenseField.setText(String.format("%.2f", totalExpenses));
        surplusField.setText(String.format("%.2f", surplus));
        
        // Set color based on surplus/deficit
        if (surplus < 0) {
            surplusField.setForeground(Color.RED);
        } else {
            surplusField.setForeground(Color.BLACK);
        }
    }
    
    private double calculateTotalIncome() {
        double wages = getConvertedValue(wagesField, (String) wagesPeriod.getSelectedItem());
        double loans = getConvertedValue(loansField, (String) loansPeriod.getSelectedItem());
        double otherIncome = getConvertedValue(otherIncomeField, (String) otherIncomePeriod.getSelectedItem());
        
        return wages + loans + otherIncome;
    }
    
    private double calculateTotalExpenses() {
        double food = getConvertedValue(foodField, (String) foodPeriod.getSelectedItem());
        double rent = getConvertedValue(rentField, (String) rentPeriod.getSelectedItem());
        double otherExpenses = getConvertedValue(otherExpenseField, (String) otherExpensePeriod.getSelectedItem());
        
        return food + rent + otherExpenses;
    }
    
    private double getConvertedValue(JTextField field, String period) {
        double value = getTextFieldValue(field);
        if (Double.isNaN(value)) return 0.0;
        
        // Convert to yearly equivalent for consistent calculation
        switch (period) {
            case "Weekly":
                return value * WEEKS_IN_YEAR;
            case "Monthly":
                return value * MONTHS_IN_YEAR;
            case "Yearly":
            default:
                return value;
        }
    }
    
    private double getTextFieldValue(JTextField field) {
        String fieldString = field.getText().trim();
        
        if (fieldString.isEmpty()) {
            return 0.0;
        }
        
        try {
            return Double.parseDouble(fieldString);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(topLevelFrame, 
                "Please enter a valid number in: " + getFieldName(field),
                "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return Double.NaN;
        }
    }
    
    private String getFieldName(JTextField field) {
        if (field == wagesField) return "Wages";
        if (field == loansField) return "Loans";
        if (field == otherIncomeField) return "Other Income";
        if (field == foodField) return "Food";
        if (field == rentField) return "Rent";
        if (field == otherExpenseField) return "Other Expenses";
        return "Field";
    }
    
    // UNDO FUNCTIONALITY
    private void saveState() {
        if (undoStack.size() >= MAX_UNDO_LEVELS) {
            undoStack.remove(0); // Remove oldest if at capacity
        }
        undoStack.push(new BudgetState(this));
    }
    
    private void undo() {
        if (undoStack.size() > 1) { // Keep initial state
            undoStack.pop(); // Remove current state
            BudgetState previousState = undoStack.peek();
            previousState.restore(this);
            calculateTotals();
        } else {
            JOptionPane.showMessageDialog(topLevelFrame, 
                "No more actions to undo", "Undo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // Getters for testing
    public JTextField getWagesField() { return wagesField; }
    public JTextField getLoansField() { return loansField; }
    public JTextField getTotalIncomeField() { return totalIncomeField; }
    public JTextField getSurplusField() { return surplusField; }
    public JButton getUndoButton() { return undoButton; }
    
    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Student Budget Assessment");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            BudgetBase contentPane = new BudgetBase(frame);
            contentPane.setOpaque(true);
            frame.setContentPane(contentPane);
            
            frame.pack();
            frame.setVisible(true);
        });
    }
}