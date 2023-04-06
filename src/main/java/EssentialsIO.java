import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;


public class EssentialsIO {

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
    public static JSONArray readJSONArrayFromUrl(String url) throws IOException, JSONException {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            return new JSONArray(jsonText);
        } catch (UnknownHostException e) {
            throw new UnknownHostException("Invalid URL or no Internet! Check your connection or your input file!");
        }
    }
    public static JSONObject readJSONObjectFromUrl(String url) throws IOException, JSONException, ParseException {
        try (InputStream is = new URL(url).openStream()) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            JSONParser parser = new JSONParser();
            return (JSONObject) parser.parse(jsonText);
        } catch (java.net.UnknownHostException e) {
            throw new UnknownHostException("Invalid URL or no Internet! Check your connection or your input file!");
        }
    }
    private static void runMain(File sf) throws JSONException, IOException, ParseException, java.text.ParseException {
        File selectedFile = sf;
    }


    public static void main(String[] args) throws JSONException, IOException, ParseException, java.text.ParseException {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() throws JSONException {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }
                final JFrame frame = new JFrame("File Explorer");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                JFileChooser fileSelect = new JFileChooser();
                Path currentRelativePath = Paths.get("");
                String s = currentRelativePath.toAbsolutePath().toString();
                fileSelect.setCurrentDirectory(new File (s));
                FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON File","json");
                fileSelect.setFileFilter(filter);
                frame.pack();
                frame.setLocationByPlatform(true);
                frame.setVisible(true);
                File selectedFile = null;
                JSONArray newUserData = new JSONArray();
                String UUID;
                if (fileSelect.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileSelect.getSelectedFile();
                } else {
                    System.exit(0);
                }
                frame.dispose();

                try {
                    long user_id = 0;
                    final String ANSI_RESET = "\u001B[0m";
                    final String ANSI_RED = "\u001B[31m";
                    String token = null;
                    String email = null;
                    String frequency = null;
                    String instructure_domain = null;
                    String grading_period_title = null;
                    int counter = 1;
                    JSONObject newStudentData = new JSONObject();
                    JSONArray newCourseData = new JSONArray();
                    UUID = UUIDGenerator.generateType1UUID().toString();

                    System.out.println("UUID: " + UUID);
                    newStudentData.put("UUID", UUID);

                    JSONParser parser = new JSONParser();
                    org.json.simple.JSONArray studentData = (org.json.simple.JSONArray) parser.parse(new FileReader(selectedFile));
                    System.out.println("Pulling Data...");
                    for (Object object : studentData) {
                        JSONObject userData = (JSONObject) object;
                        token = (String) userData.get("token");
                        instructure_domain = (String) userData.get("instructure_domain");
                        email = (String) userData.get("email");
                        frequency = (String) userData.get("frequency");
                        newStudentData.put("token", token);
                        newStudentData.put("instructure_domain", instructure_domain);
                        newStudentData.put("email", email);
                        newStudentData.put("frequency", frequency);
                    }
                    System.out.println("Success!");
                    System.out.println("Retrieving Course Data From the Web...");
                    newStudentData.put("enrollments", newCourseData);

                    JSONArray json = readJSONArrayFromUrl("https://" + instructure_domain + "/api/v1/courses?per_page=999999&access_token=" + token);
                    String courseData = json.toString();
                    boolean found_User_Id = false;
                    org.json.simple.JSONArray courseJSON = (org.json.simple.JSONArray) parser.parse(courseData);
                    for (Object object : courseJSON) {
                        String grading_period_id = null;
                        JSONObject course = (JSONObject) object;

                        org.json.simple.JSONArray enrollments = (org.json.simple.JSONArray) course.get("enrollments");    //Looks for array called enrollments
                        if (enrollments != null && !found_User_Id) {      // Grabs user_id
                            for (Object c : enrollments) {
                                JSONObject enrollment_list = (JSONObject) c;
                                user_id = (long) enrollment_list.get("user_id");
                                newStudentData.put("user_id", user_id);
                                found_User_Id = true;
                            }
                        }

                        Boolean hide_final_grades = (Boolean) course.get("hide_final_grades");
                        Boolean access_restricted_by_date = (Boolean) course.get("access_restricted_by_date");
                        if(access_restricted_by_date == null && !hide_final_grades) {
                            long id = (long) course.get("id");
                            String course_code = (String) course.get("course_code");
                            JSONObject jsonObj = readJSONObjectFromUrl("https://" + instructure_domain + "/api/v1/courses/" + id + "/grading_periods?per_page=999999&access_token=" + token);
                            String enrollmentData = jsonObj.toString();
                            org.json.simple.JSONObject enrollmentsJSON = (org.json.simple.JSONObject) parser.parse(enrollmentData);
                            org.json.simple.JSONArray grading_period = (org.json.simple.JSONArray) enrollmentsJSON.get("grading_periods");
                            Date lastEnd = new Date();
                            for (Object c : grading_period) {
                                JSONObject grading_item = (JSONObject) c;

                                String start_date = (String) grading_item.get("start_date");
                                String end_date = (String) grading_item.get("end_date");
                                start_date = start_date.substring(0, 9);
                                end_date = end_date.substring(0, 9);
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = new Date();
                                String todays_date = sdf.format(date);
                                Date today = sdf.parse(todays_date);
                                Date start = sdf.parse(start_date);
                                Date end = sdf.parse(end_date);

                                if (today.compareTo(start) >= 0 && today.compareTo(end) <= 0 && today.compareTo(lastEnd) <= today.compareTo(start)) {
                                    grading_period_id = (String) grading_item.get("id");
                                    grading_period_title = (String) grading_item.get("title");
                                    lastEnd = sdf.parse(end_date);
                                }
                            }
                            Object gradeScore = null;
                            if (grading_period_id != null) {
                                org.json.JSONArray gradeJson = readJSONArrayFromUrl("https://" + instructure_domain + "/api/v1/courses/" + id + "/enrollments?user_id=" + user_id + "&grading_period_id=" + grading_period_id + "&per_page=999999&access_token=" + token);
                                String gradeData = gradeJson.toString();
                                org.json.simple.JSONArray gradePeriod = (org.json.simple.JSONArray) parser.parse(gradeData);       //courseId shouldn't be a long? It works as an int?
                                for (Object t : gradePeriod) {
                                    JSONObject gData = (JSONObject) t;
                                    org.json.simple.JSONObject grades = (org.json.simple.JSONObject) gData.get("grades");
                                    gradeScore = grades.get("current_score");
                                }
                            }
                            if (grading_period_id != null) {
                                JSONObject newCardData = new JSONObject();
                                newCardData.put("course_id", id);
                                newCardData.put("grading_period_id", grading_period_id);
                                if (course_code != null) {
                                    newCardData.put("course_name", course_code);
                                }
                                if (grading_period_id.length() == 0) {
                                    System.out.println(ANSI_RED + "Error: Course " + counter + " does not have an id. Please check the output file" + ANSI_RESET);
                                }
                                if (grading_period_title != null) {
                                    newCardData.put("grading_period_title", grading_period_title);
                                } else {
                                    System.out.println(ANSI_RED + "Error: Course " + counter + " does not have a title. Please check the output file" + ANSI_RESET);
                                }
                                try {
                                    newCardData.put("lastGrade", Double.valueOf(gradeScore.toString()));   // Weird error if "return (Double) gradeScore;". Somehow works if courseId in Line 44 is replaced with an int, like 6221. idfk, find explanation later
                                } catch (NullPointerException e) {
                                    newCardData.put("lastGrade", -1);
                                }
                                newCourseData.put(newCardData);
                            }
                        }
                    }
                    newUserData.put(newStudentData);

                    System.out.println("Done!");
                } catch (IOException | java.text.ParseException | ParseException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Writing data to file...");
                try {
                    String filepathName;
                    filter = new FileNameExtensionFilter("Folder","dir");
                    fileSelect.setFileFilter(filter);
                    fileSelect.setCurrentDirectory(new File (s));
                    fileSelect.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    fileSelect.setAcceptAllFileFilterUsed(false);
                    fileSelect.setDialogTitle("Write to Folder");
                    fileSelect.setApproveButtonText("Write");
                    fileSelect.setApproveButtonToolTipText("Writes the resulting file to the selected directory.");
                    if (fileSelect.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                        filepathName = fileSelect.getSelectedFile() + "\\InstructureStudentData" + UUID + ".json";
                    }
                    else {
                        filepathName = s + "InstructureStudentData" + UUID + ".json";
                    }
                    System.out.println("Current relative path is: " + s);

                    FileWriter fw = new FileWriter(filepathName);
                    fw.write(newUserData.toString());
                    System.out.println("Successfully written!");
                    System.out.println("Check the output folder.");
                    fw.close();
                }
                catch (Exception e) {
                    e.getStackTrace();
                }
                System.exit(0);
            }
        });
    }
}