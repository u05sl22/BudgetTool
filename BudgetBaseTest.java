package Budget;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.awt.*;

public class BudgetBaseTest {
    
    private BudgetBase budget;
    private JFrame frame;
    
    @BeforeEach
    public void setUp() {
        frame = new JFrame();
        budget = new BudgetBase(frame);
    }
    
    @Test
    public void testInitialState() {
        assertEquals("0.00", budget.getTotalIncomeField().getText());
        assertEquals("0.00", budget.getTotalExpenseField().getText());
        assertEquals("0.00", budget.getSurplusField().getText());
        assertEquals(Color.BLACK, budget.getSurplusField().getForeground());
    }
    
    @Test
    public void testBasicIncomeCalculation() {
        budget.getWagesField().setText("1000");
        budget.getLoansField().setText("500");
        budget.getOtherIncomeField().setText("200");
        budget.calculateTotals();
        
        assertEquals("1700.00", budget.getTotalIncomeField().getText());
    }
    
    @Test
    public void testBasicExpenseCalculation() {
        budget.getFoodField().setText("300");
        budget.getRentField().setText("800");
        budget.getOtherExpenseField().setText("100");
        budget.calculateTotals();
        
        assertEquals("1200.00", budget.getTotalExpenseField().getText());
    }
    
    @Test
    public void testSurplusDeficitColoring() {
        // Test surplus (black)
        budget.getWagesField().setText("2000");
        budget.getFoodField().setText("1000");
        budget.calculateTotals();
        assertEquals(Color.BLACK, budget.getSurplusField().getForeground());
        
        // Test deficit (red)
        budget.getWagesField().setText("500");
        budget.getFoodField().setText("1000");
        budget.calculateTotals();
        assertEquals(Color.RED, budget.getSurplusField().getForeground());
    }
    
    @Test
    public void testEmptyFieldHandling() {
        budget.getWagesField().setText("");
        budget.getLoansField().setText("500");
        budget.calculateTotals();
        
        // Empty field should be treated as 0
        assertEquals("500.00", budget.getTotalIncomeField().getText());
    }
    
    @Test
    public void testInvalidInputHandling() {
        budget.getWagesField().setText("abc");
        budget.calculateTotals();
        
        // Should handle invalid input gracefully
        assertNotNull(budget.getTotalIncomeField().getText());
    }
    
    @Test
    public void testUndoFunctionality() {
        // Record initial state
        String initialWages = budget.getWagesField().getText();
        
        // Make a change
        budget.getWagesField().setText("1500");
        budget.saveState();
        budget.calculateTotals();
        
        // Perform undo
        budget.undo();
        
        // Verify undo worked
        assertEquals(initialWages, budget.getWagesField().getText());
    }
    
    @Test
    public void testMultipleUndo() {
        // Record initial state
        String initialWages = budget.getWagesField().getText();
        String initialLoans = budget.getLoansField().getText();
        
        // First change
        budget.getWagesField().setText("1000");
        budget.saveState();
        
        // Second change
        budget.getLoansField().setText("500");
        budget.saveState();
        
        // First undo - should go back to first change
        budget.undo();
        assertEquals("1000", budget.getWagesField().getText());
        assertEquals(initialLoans, budget.getLoansField().getText());
        
        // Second undo - should go back to initial state
        budget.undo();
        assertEquals(initialWages, budget.getWagesField().getText());
        assertEquals(initialLoans, budget.getLoansField().getText());
    }
    
    @Test
    public void testBudgetStateCreation() {
        // Set some values
        budget.getWagesField().setText("1000");
        budget.getLoansField().setText("500");
        
        // Create state
        BudgetState state = new BudgetState(budget);
        
        // Verify state captured correctly
        assertEquals("1000", state.getWages());
        assertEquals("500", state.getLoans());
    }
    
    @Test
    public void testBudgetStateRestoration() {
        // Set initial values
        budget.getWagesField().setText("1000");
        budget.getLoansField().setText("500");
        
        // Create state
        BudgetState state = new BudgetState(budget);
        
        // Change values
        budget.getWagesField().setText("2000");
        budget.getLoansField().setText("1000");
        
        // Restore state
        state.restore(budget);
        
        // Verify restoration
        assertEquals("1000", budget.getWagesField().getText());
        assertEquals("500", budget.getLoansField().getText());
    }
}