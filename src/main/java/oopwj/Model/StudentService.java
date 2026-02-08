package oopwj.Model;

import java.io.*;
import java.util.*;

public class StudentService {

    private final String MODULES_FILE = "src\\main\\java\\oopwj\\Data\\modules.txt";
    private final String ENROLLMENT_FILE = "src\\main\\java\\oopwj\\Data\\enrollment.txt";
    private final String FEEDBACK_FILE = "src\\main\\java\\oopwj\\Data\\studentFeedback.txt";
    private final String STUDENT_FILE = "src\\main\\java\\oopwj\\Data\\student.txt"; 
    
    private final String QUIZ_FILE = "src\\main\\java\\oopwj\\Data\\Quiz.txt";
    private final String QUESTION_FILE = "src\\main\\java\\oopwj\\Data\\question.txt";
    private final String ANSWER_FILE = "src\\main\\java\\oopwj\\Data\\student_answers.txt";
    private final String GRADE_FILE = "src\\main\\java\\oopwj\\Data\\Grade.txt";
    private final String FINAL_GRADE_FILE = "src\\main\\java\\oopwj\\Data\\FinalGrade.txt";
    
    private final String SCHEDULE_FILE = "src\\main\\java\\oopwj\\Data\\class_schedule.txt";
    private final String VENUE_FILE = "src\\main\\java\\oopwj\\Data\\class.txt";

    public List<String> getAllModules() {
        List<String> modules = new ArrayList<>();
        File file = new File(MODULES_FILE);
        if (!file.exists()) return modules;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) modules.add(line.trim());
            }
        } catch (IOException e) { e.printStackTrace(); }
        return modules;
    }

    public List<String> getEnrolledCourses(String studentID) {
        List<String> enrolled = new ArrayList<>();
        File file = new File(ENROLLMENT_FILE);
        if (!file.exists()) return enrolled;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3 && data[0].trim().equals(studentID)) {
                    enrolled.add(data[1].trim() + "," + data[2].trim());
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return enrolled;
    }

    public boolean enrollCourse(String studentID, String courseString) {
        String[] parts = courseString.split(",");
        if (parts.length < 2) return false;
        String courseID = parts[0].trim();
        String courseName = parts[1].trim();

        for (String c : getEnrolledCourses(studentID)) {
            if (c.startsWith(courseID)) return false;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ENROLLMENT_FILE, true))) {
            writer.write(studentID + "," + courseID + "," + courseName);
            writer.newLine();
            return true;
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    public boolean unenrollCourse(String studentID, String courseString) {
        String[] parts = courseString.split(",");
        if (parts.length < 1) return false;
        String courseID = parts[0].trim();

        File file = new File(ENROLLMENT_FILE);
        if (!file.exists()) return false;

        List<String> lines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2) {
                    if (data[0].trim().equals(studentID) && data[1].trim().equals(courseID)) {
                        found = true;
                    } else {
                        lines.add(line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (found) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String l : lines) {
                    writer.write(l);
                    writer.newLine();
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public String getLecturerIdByCourse(String courseID) {
        File file = new File(MODULES_FILE);
        if (!file.exists()) return "Unknown";
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4 && data[0].trim().equalsIgnoreCase(courseID)) {
                    return data[3].trim();
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return "Unknown";
    }

    public boolean submitFeedback(String studentID, String courseID, String lecturerID, String comment) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FEEDBACK_FILE, true))) {
            writer.write(studentID + "," + courseID + "," + lecturerID + "," + comment);
            writer.newLine();
            return true;
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    public boolean isValidPassword(String password) {
        if (password == null || password.length() < 7 || password.length() > 14) return false;
        boolean hasLower = false, hasUpper = false, hasDigit = false, hasSymbol = false;
        String symbols = "!@#$%^";
        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (symbols.indexOf(c) != -1) hasSymbol = true;
            else if (c == ',') return false; 
        }
        return hasLower && hasUpper && hasDigit && hasSymbol;
    }

    public boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) return false;
        return name.matches("^[a-zA-Z ]+$");
    }

    public boolean updateName(String userId, String newName) { return updateField(userId, 1, newName); }
    public boolean updatePassword(String userId, String newPass) { return updateField(userId, 2, newPass); }

    private boolean updateField(String userId, int index, String val) {
        List<String> lines = new ArrayList<>();
        boolean updated = false;
        File file = new File(STUDENT_FILE); 
        if (!file.exists()) return false;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4 && data[0].equals(userId)) {
                    data[index] = val; 
                    lines.add(String.join(",", data));
                    updated = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) { e.printStackTrace(); return false; }

        if (updated) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String l : lines) { writer.write(l); writer.newLine(); }
                return true;
            } catch (IOException e) { e.printStackTrace(); }
        }
        return false;
    }

    public boolean hasQuizForModule(String moduleID) {
        File file = new File(QUIZ_FILE);
        if (!file.exists()) return false;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[1].trim().equalsIgnoreCase(moduleID)) return true;
            }
        } catch (IOException e) { e.printStackTrace(); }
        return false;
    }

    public List<String[]> getQuizzesForModule(String moduleID) {
        List<String[]> quizList = new ArrayList<>();
        File file = new File(QUIZ_FILE);
        if (!file.exists()) return quizList;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[1].trim().equalsIgnoreCase(moduleID)) {
                    quizList.add(new String[]{parts[0].trim(), parts[2].trim()});
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return quizList;
    }

    public List<String[]> getQuizDetails(String quizID) {
        List<String[]> objectiveList = new ArrayList<>();
        List<String[]> subjectiveList = new ArrayList<>();
        File file = new File(QUESTION_FILE);
        if (!file.exists()) return new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) continue; 
                if (parts[1].trim().equalsIgnoreCase(quizID)) {
                    String qID = parts[0].trim();
                    String qText = parts[3].trim();
                    String type = parts[parts.length - 1].trim();

                    if (type.equalsIgnoreCase("Subjective")) {
                        subjectiveList.add(new String[]{qID, qText, "Subjective"});
                    } else if (type.equalsIgnoreCase("Objective")) {
                        if (parts.length >= 10) {
                            objectiveList.add(new String[]{qID, qText, "Objective", parts[4].trim(), parts[5].trim(), parts[6].trim(), parts[7].trim()});
                        }
                    }
                }
            }
        } catch (IOException e) { e.printStackTrace(); }

        List<String[]> finalQuestions = new ArrayList<>();
        finalQuestions.addAll(objectiveList);
        finalQuestions.addAll(subjectiveList);
        return finalQuestions;
    }

    public boolean submitQuizAnswers(String studentID, String moduleID, String quizID, List<String[]> answersData) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ANSWER_FILE, true))) {
            for (String[] entry : answersData) {
                String safeAns = entry[1].replace(",", "，");
                writer.write(studentID + "," + moduleID + "," + quizID + "," + entry[0] + "," + entry[2] + "," + safeAns);
                writer.newLine();
            }
            return true;
        } catch (IOException e) { e.printStackTrace(); return false; }
    }

    public List<String[]> getFinalGradesForModule(String studentID, String moduleID) {
        List<String[]> gradeList = new ArrayList<>();
        File file = new File(FINAL_GRADE_FILE);
        if (!file.exists()) return gradeList;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    if (parts[0].trim().equals(studentID) && parts[1].trim().equalsIgnoreCase(moduleID)) {
                        String comment = parts[5].trim().replace("\"", "");
                        gradeList.add(new String[]{parts[2].trim(), parts[3].trim(), parts[4].trim(), comment});
                    }
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return gradeList;
    }

    public List<String[]> getDetailedResults(String studentID, String moduleID, String quizID) {
        List<String[]> results = new ArrayList<>();
        File file = new File(GRADE_FILE);
        if (!file.exists()) return results;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7 && parts[0].trim().equals(studentID) && 
                    parts[1].trim().equalsIgnoreCase(moduleID) && 
                    parts[2].trim().equalsIgnoreCase(quizID)) {
                    results.add(new String[]{parts[3].trim(), parts[4].trim(), parts[5].trim(), parts[6].trim()});
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return results;
    }

    public List<String[]> getStudentTimetable(String studentID, String selectedDay) {
        List<String[]> schedule = new ArrayList<>();
        
        List<String> enrolledStrings = getEnrolledCourses(studentID);
        List<String> enrolledIDs = new ArrayList<>();
        for (String s : enrolledStrings) {
            enrolledIDs.add(s.split(",")[0].trim()); 
        }

        Map<String, String> venueMap = new HashMap<>();
        File venueFile = new File(VENUE_FILE);
        if (venueFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(venueFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2) venueMap.put(parts[0].trim(), parts[1].trim());
                }
            } catch (IOException e) { e.printStackTrace(); }
        }

        Map<String, String> moduleNameMap = new HashMap<>();
        File moduleFile = new File(MODULES_FILE);
        if (moduleFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(moduleFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        moduleNameMap.put(parts[0].trim(), parts[1].trim());
                    }
                }
            } catch (IOException e) { e.printStackTrace(); }
        }

        File file = new File(SCHEDULE_FILE);
        if (!file.exists()) return schedule;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String classID = parts[0].trim();
                    String modID = parts[1].trim();
                    String day = parts[3].trim();
                    
                    if (enrolledIDs.contains(modID) && day.equalsIgnoreCase(selectedDay)) {
                        String venue = venueMap.getOrDefault(classID, "Venue TBD");
                        String modName = moduleNameMap.getOrDefault(modID, modID); 
                        
                        schedule.add(new String[]{ 
                            modID,             
                            parts[2].trim(),   
                            parts[4].trim(),   
                            parts[5].trim(),   
                            venue,             
                            modName            
                        });
                    }
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        
        Collections.sort(schedule, Comparator.comparing(o -> o[2]));
        
        return schedule;
    }
}