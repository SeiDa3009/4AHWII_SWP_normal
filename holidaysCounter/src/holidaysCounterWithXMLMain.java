import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.application.Application;

public class holidaysCounterWithXMLMain {

    static NodeList fixedDates;
    static NodeList mutableDates;
    static Scanner reader = new Scanner(System.in);
    static int years;
    static LocalDate startDate, endDate;
    static List<LocalDate> fixedDatesList = new ArrayList<>();
    static List<String> mutableDatesList = new ArrayList<>();
    static int mutableMonday = 0, mutableTuesday = 0, mutableWednesday = 0, mutableThursday = 0, mutableFriday = 0;
    static int fixedMonday = 0, fixedTuesday = 0, fixedWednesday = 0, fixedThursday = 0, fixedFriday = 0;
    static int monday, tuesday, wednesday, thursday, friday, allTogether, fixedDays, mutableDays;
    static File filename = new File("C:\\Users\\david\\OneDrive\\Schule\\4AHWII\\SWP\\SWP-Rubner\\Normal_github\\projects\\holidaysCounter\\holidayDates.xml");
    //static  primaryStage;



    public static void main(String[] args) {
        holidaysCounterWithXMLMain mainProgram = new holidaysCounterWithXMLMain();
        mainProgram.startToEndDate();
        mainProgram.fileReader();
        mainProgram.datesInLists();
        mainProgram.checkIfFixedHoliday();
        mainProgram.checkIfMutableHoliday();
        mainProgram.calculateResults();
        mainProgram.showResults();
    }
    void fileReader(){
        //mithilfe von https://www.javatpoint.com/how-to-read-xml-file-in-java:

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(filename);
            doc.getDocumentElement().normalize();
            fixedDates = doc.getElementsByTagName("fixedDates");
            mutableDates = doc.getElementsByTagName("mutableDates");
        }
        catch (Exception e) {
            System.out.println("An error has occured!");
        }
    }
    void startToEndDate(){
        System.out.print("How many years do you want to calculate?: ");
        years = reader.nextInt();
        System.out.print("On what date should we start [yyyy/mm/dd]?: ");
        startDate = LocalDate.of(reader.nextInt(),reader.nextInt(),reader.nextInt());
        endDate = startDate.plusYears(years);
    }
    void datesInLists(){

        for(int i = 0; i < fixedDates.getLength(); i++){
            Node node = fixedDates.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE){
                Element eElement = (Element) node;
                fixedDatesList.add(LocalDate.of((LocalDate.now().getYear()),
                        Integer.parseInt(eElement.getElementsByTagName("Month").item(0).getTextContent()),
                        Integer.parseInt(eElement.getElementsByTagName("Day").item(0).getTextContent())));
            }
        }

        for(int k = 1; k < years; k++){
            for (int i = 0; i < fixedDates.getLength(); i++) {
                fixedDatesList.add(fixedDatesList.get(i).plusYears(k));
            }
        }

        for (int i = 0; i < mutableDates.getLength(); i++){
            Node node = mutableDates.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE){
                Element eElement = (Element) node;
                mutableDatesList.add(eElement.getElementsByTagName("DayOfWeek").item(0).getTextContent());
            }
        }
    }
    void checkIfFixedHoliday(){
        for (int i = 0; i < fixedDatesList.size(); i++){

            switch(fixedDatesList.get(i).getDayOfWeek()){
                case MONDAY:
                    fixedMonday++;
                    break;
                case TUESDAY:
                    fixedTuesday++;
                    break;
                case WEDNESDAY:
                    fixedWednesday++;
                    break;
                case THURSDAY:
                    fixedThursday++;
                    break;
                case FRIDAY:
                    fixedFriday++;
                    break;
            }
        }
    }
    void checkIfMutableHoliday(){
        for (int i = 0; i < mutableDatesList.size(); i++){
            switch (mutableDatesList.get(i)){
                case "MONDAY":
                    mutableMonday++;
                    break;
                case "TUESDAY":
                    mutableTuesday++;
                    break;
                case "WEDNESDAY":
                    mutableWednesday++;
                    break;
                case "THURSDAY":
                    mutableThursday++;
                    break;
                case "FRIDAY":
                    mutableFriday++;
                    break;
            }
        }
    }
    void calculateResults(){
        monday = fixedMonday + (mutableMonday*years);
        tuesday = fixedTuesday + (mutableTuesday*years);
        wednesday = fixedWednesday + (mutableWednesday*years);
        thursday = fixedThursday + (mutableThursday*years);
        friday = fixedFriday + (mutableFriday*years);

        fixedDays = fixedMonday + fixedTuesday + fixedWednesday + fixedThursday + fixedFriday;
        mutableDays = (mutableMonday + mutableTuesday + mutableWednesday + mutableThursday + mutableFriday) * years;
        allTogether = monday + tuesday + wednesday + thursday + friday;
    }
    void showResults(){
        //Ausgabe aller Daten
        //System.out.println(fixedDatesList);

        System.out.println("\nHolidays:");
        System.out.println("Monday: " + monday + " days");
        System.out.println("Tuesday: " + tuesday + " days");
        System.out.println("Wednesday: " + wednesday + " days");
        System.out.println("Thursday: " + thursday + " days");
        System.out.println("Friday: " + friday + " days");
        System.out.println(" ");
        System.out.println("Fixed: " + fixedDays + " days");
        System.out.println("Mutable: " + mutableDays + " days");
        System.out.println("All together: " + allTogether + " days");
    }
    void showGraphics(){
        String itemA = "Monday";
        String itemB = "Tuesday";
        String itemC = "Wednesday";
        String itemD = "Thursday";
        String itemE = "Friday";



    }
}
