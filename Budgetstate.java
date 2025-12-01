package Budget;

import javax.swing.*;

public class BudgetState {
    private String wages, loans, otherIncome;
    private String food, rent, otherExpenses;
    private String wagesPeriod, loansPeriod, otherIncomePeriod;
    private String foodPeriod, rentPeriod, otherExpensesPeriod;
    
    public BudgetState(BudgetBase budget) {
        // Save income values
        this.wages = budget.getWagesField().getText();
        this.loans = budget.getLoansField().getText();
        this.otherIncome = budget.getOtherIncomeField().getText();
        
        // Save expense values
        this.food = budget.getFoodField().getText();
        this.rent = budget.getRentField().getText();
        this.otherExpenses = budget.getOtherExpenseField().getText();
        
        // Save period selections
        this.wagesPeriod = (String) ((JComboBox<?>)budget.getWagesField().getParent().getComponent(2)).getSelectedItem();
        this.loansPeriod = (String) ((JComboBox<?>)budget.getLoansField().getParent().getComponent(5)).getSelectedItem();
        this.otherIncomePeriod = (String) ((JComboBox<?>)budget.getOtherIncomeField().getParent().getComponent(8)).getSelectedItem();
        this.foodPeriod = (String) ((JComboBox<?>)budget.getFoodField().getParent().getComponent(11)).getSelectedItem();
        this.rentPeriod = (String) ((JComboBox<?>)budget.getRentField().getParent().getComponent(14)).getSelectedItem();
        this.otherExpensesPeriod = (String) ((JComboBox<?>)budget.getOtherExpenseField().getParent().getComponent(17)).getSelectedItem();
    }
    
    public void restore(BudgetBase budget) {
        // Restore income values
        budget.getWagesField().setText(wages);
        budget.getLoansField().setText(loans);
        budget.getOtherIncomeField().setText(otherIncome);
        
        // Restore expense values
        budget.getFoodField().setText(food);
        budget.getRentField().setText(rent);
        budget.getOtherExpenseField().setText(otherExpenses);
        
        // Restore period selections
        ((JComboBox<?>)budget.getWagesField().getParent().getComponent(2)).setSelectedItem(wagesPeriod);
        ((JComboBox<?>)budget.getLoansField().getParent().getComponent(5)).setSelectedItem(loansPeriod);
        ((JComboBox<?>)budget.getOtherIncomeField().getParent().getComponent(8)).setSelectedItem(otherIncomePeriod);
        ((JComboBox<?>)budget.getFoodField().getParent().getComponent(11)).setSelectedItem(foodPeriod);
        ((JComboBox<?>)budget.getRentField().getParent().getComponent(14)).setSelectedItem(rentPeriod);
        ((JComboBox<?>)budget.getOtherExpenseField().getParent().getComponent(17)).setSelectedItem(otherExpensesPeriod);
    }
    
    // Getters for testing
    public String getWages() { return wages; }
    public String getLoans() { return loans; }
    public String getOtherIncome() { return otherIncome; }
    public String getFood() { return food; }
    public String getRent() { return rent; }
    public String getOtherExpenses() { return otherExpenses; }
}