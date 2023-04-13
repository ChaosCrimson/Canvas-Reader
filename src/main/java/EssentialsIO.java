import net.miginfocom.swing.MigLayout;
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
import java.util.Date;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.Objects;


public class EssentialsIO extends JFrame{
    static JSONArray newUserData = new JSONArray();
    static String UUID = UUIDGenerator.generateType1UUID().toString();
    static Path currentRelativePath = Paths.get("");
    static String s = currentRelativePath.toAbsolutePath().toString();

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


    public static void main(String[] args) throws JSONException {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() throws JSONException {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

                final JFrame frame = new JFrame("File Explorer");
                final JFileChooser fileSelect = new JFileChooser();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                fileSelect.setCurrentDirectory(new File (s));
                FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON File","json");
                fileSelect.setFileFilter(filter);
                frame.pack();
                frame.setLocationByPlatform(true);
                frame.setVisible(true);
                File selectedFile = null;
                if (fileSelect.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                    selectedFile = fileSelect.getSelectedFile();
                } else {
                    System.exit(0);
                }
                frame.dispose();


                JFrame frame2 = new JFrame("File 2 Explorer");
                JLabel lbl1, lbl2, lbl3, lbl4, info1, info2, info3, info4;
                JTextField txtfld1, txtfld2, txtfld3;
                JRadioButton rButton1, rButton2;
                Font font = new Font("Tahoma", Font.PLAIN, 13);
                Icon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("infoButton_16x16.png")));
                frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame2.setSize(1800, 900);
                frame2.setLayout(new MigLayout("insets 20,hidemode 3",
                        // columns
                        "[fill]" + "[fill]" + "[fill]" + "[]"+ "[fill]",
                        // rows
                        "10[] 10[] 10[] 10[] 10[]"));
                frame2.pack();
                frame2.setLocationByPlatform(true);
                frame2.setVisible(true);

                JFrame buttonBar = new JFrame();
                buttonBar.setLayout(new MigLayout("insets dialog,alignx right","[button,fill]",null));
                JButton okButton = new JButton();
                okButton.setText("OK");

                lbl1 = new JLabel("Canvas Domain");
                lbl2 = new JLabel("API Token");
                lbl3 = new JLabel("Email");
                lbl4 = new JLabel("Frequency");
                lbl1.setFont(font);
                lbl2.setFont(font);
                lbl3.setFont(font);
                lbl4.setFont(font);

                txtfld1 = new JTextField();
                txtfld2 = new JTextField();
                txtfld3 = new JTextField();
                txtfld1.setFont(font);
                txtfld2.setFont(font);
                txtfld3.setFont(font);

                info1 = new JLabel(icon);
                info2= new JLabel(icon);
                info3= new JLabel(icon);
                info4 = new JLabel(icon);
                info1.setToolTipText("Test tooltip");

                rButton1 = new JRadioButton("Daily");
                rButton2 = new JRadioButton("Weekly");
                rButton1.setFont(font);
                rButton2.setFont(font);

                //http://www.miglayout.com/QuickStart.pdf

                frame2.add(lbl1, "cell 0 0");   //Cell column row
                frame2.add(info1, "cell 1 0");
                frame2.add(txtfld1, "cell 2 0, span 3, width 150:150:200, height 22, growx 200, shrink 0");
                frame2.add(lbl2, "cell 0 1");
                frame2.add(info2, "cell 1 1");
                frame2.add(txtfld2, "cell 2 1, span 4, width 500:500:500, height 22, growx 200, shrink 0");
                frame2.add(lbl3, "cell 0 2");
                frame2.add(info3, "cell 1 2");
                frame2.add(txtfld3, "cell 2 2, span 4, width 200:200:250, height 22, growx 200, shrink 0");
                frame2.add(lbl4, "cell 0 3");   //Cell column row
                frame2.add(info4, "cell 1 3");
                frame2.add(rButton1, "cell 2 3, span 2, width 100:100:150, height 22, growx 100, shrink 0");
                frame2.add(rButton2, "cell 4 3, span 2, width 100:100:150, height 22, growx 100, shrink 0");
                frame2.add(okButton, "dock south,align right, gap 20, pad -19 -19 -20 -20, width 75:75:75, height 28, growx 100, shrink 0");

                File finalSelectedFile = selectedFile;
                okButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ex) {
                        try {
                            frame2.dispose();
                            dataGather(finalSelectedFile);
                            dataWrite(fileSelect, frame);
                        } catch (IOException | ParseException | java.text.ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });


                //frame2.add(panel);
                frame2.repaint();
                frame2.pack();

                // This is where everything happens (Put into "finish" button


            }
        });
    }

    public static void dataGather(File finalSelectedFile) throws IOException, ParseException, java.text.ParseException {
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

        System.out.println("UUID: " + UUID);
        newStudentData.put("UUID", UUID);

        JSONParser parser = new JSONParser();
        org.json.simple.JSONArray studentData = (org.json.simple.JSONArray) parser.parse(new FileReader(finalSelectedFile));
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
    }
    public static void dataWrite(JFileChooser fileSelect, JFrame frame) {
        System.out.println("Writing data to file...");
        try {
            String filepathName;
            final FileNameExtensionFilter filter = new FileNameExtensionFilter("Folder","dir");
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
    }

}
