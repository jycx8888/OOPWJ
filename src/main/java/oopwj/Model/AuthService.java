package oopwj.Model;

import oopwj.Model.User;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AuthService {
    
    private final String STUDENT_FILE = "src/main/java/oopwj/Data/student.txt";
    private final String LECTURER_FILE = "src/main/java/oopwj/Data/lecturer.txt";
    private final String ACADEMIC_LEADER_FILE = "src/main/java/oopwj/Data/academicLeader.txt";
    private final String ADMIN_FILE = "src/main/java/oopwj/Data/admin.txt";

    /**
     * Authenticate user based on ID prefix and read from corresponding file
     * ID Prefixes:
     * - S: Student (reads from student.txt)
     * - AC: Academic Leader (reads from academicLeader.txt)
     * - L: Lecturer (reads from lecturer.txt)
     * - A: Administrative Staff (reads from admin.txt)
     */
    public User LoginUser(String userID, String password){
        String role;
        String filePath;
        int expectedLength;
        
        // Determine role and file based on ID prefix
        if(userID.startsWith("AC")){
            role = "Academic Leader";
            filePath = ACADEMIC_LEADER_FILE;
            expectedLength = 4;
        } else if(userID.startsWith("L")){
            role = "Lecturer";
            filePath = LECTURER_FILE;
            expectedLength = 5;
        } else if(userID.startsWith("A")){
            role = "Admin";
            filePath = ADMIN_FILE;
            expectedLength = 4;
        } else if(userID.startsWith("S")){
            role = "Student";
            filePath = STUDENT_FILE;
            expectedLength = 4;
        } else {
            return null; // Invalid ID prefix
        }
        
        // Read from the appropriate file and authenticate
        List<User> userList = readUsersFromFile(filePath, role, expectedLength);
        for(User user: userList){
            if(user.getUserID().equals(userID)){
                if(user.verifyPassword(password)){
                    return user;
                }
            }
        }
        return null;
    }
    
    private List<User> readUsersFromFile(String filePath, String role, int expectedLength){
        List<User> userList = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            String line;
            while((line = reader.readLine()) != null){
                if(line.trim().isEmpty()) continue;
                String[] tempUserData = line.split(",");
                
                if(tempUserData.length != expectedLength) continue;

                User user = new User(
                    tempUserData[0].trim(),      // userID
                    tempUserData[1].trim(),      // userName
                    tempUserData[2].trim(),      // password
                    tempUserData[3].trim(),      // email
                    role                         // userRole
                );
                userList.add(user);
            }
        } catch(FileNotFoundException e){
            System.out.println("File not found: " + filePath);
        } catch(IOException e){
            e.printStackTrace();
        }
        return userList;
    }
    
    public List<User> readAllUsersFromFiles(){
        List<User> userList = new ArrayList<>();
        
        // Read from student.txt
        userList.addAll(readUsersFromFile(STUDENT_FILE, "Student", 4));
        
        // Read from lecturer.txt
        userList.addAll(readUsersFromFile(LECTURER_FILE, "Lecturer", 5));
        
        // Read from academicLeader.txt
        userList.addAll(readUsersFromFile(ACADEMIC_LEADER_FILE, "Academic Leader", 4));
        
        // Read from admin.txt
        userList.addAll(readUsersFromFile(ADMIN_FILE, "Admin", 4));
        
        return userList;
    }
}