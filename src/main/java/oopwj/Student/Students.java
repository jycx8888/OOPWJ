package oopwj.Student;

import oopwj.Model.User;

public class Students extends User{
    
    protected String intakeCode;
    
    public Students(String userName, String userID, String password, String userEmail, String userRole, String intakeCode) {
        super(userName, userID, password, userEmail, userRole);
        this.intakeCode = intakeCode;
    }
    
    public String getIntakeCode(){
        return intakeCode;
    }
    
    public void setIntakeCode(String intakeCode){
        this.intakeCode = intakeCode;
    }
    
    public void registerForClasses(){
        
    }
    
    public String checkIndividualResults(){
        String tempUserID = this.getUserID();
         return tempUserID; 
    }

}
