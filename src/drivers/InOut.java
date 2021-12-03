package drivers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

import resources.Alumni;
import resources.Donation;
import resources.Event;
import resources.Host;
import resources.Training;

public class InOut {

    private Scanner in;
    private Scanner alumniFileIn;
    private Scanner eventFileIn;
    private Scanner donationsFileIn;
    private Scanner trainingFileIn;
    private File donationsFile;
    private File alumniFile;
    private File eventFile;
    private File trainingFile;
    private PrintWriter alumniSaved;
    private PrintWriter eventSaved;
    private PrintWriter donationsSaved;
    private PrintWriter trainingSaved;
    private TreeMap<Integer, Alumni> alumniMap;
    private TreeMap<Integer, Event> eventMap;
    private TreeMap<Integer, Training> trainingMap;
    private ArrayList<Donation> donationList;

    /**
     * Initiate File, Scanner and PrintWriter
     * Call Methods to fill Maps with existing information
     * 
     * @throws FileNotFoundException
     */
    public InOut() throws FileNotFoundException {
        alumniFile = new File("alumni.txt");
        alumniFileIn = new Scanner(alumniFile);
        eventFile = new File("events.txt");
        eventFileIn = new Scanner(eventFile);
        donationsFile = new File("donations.txt");
        donationsFileIn = new Scanner(donationsFile);
        trainingFile = new File("training.txt");
        trainingFileIn = new Scanner(trainingFile);
        in = new Scanner(System.in);
        existingAlumni();
        existingEvents();
        existingDonations();
        existingTrainingEvents();
    }

    /**
     * Save All Information to Files
     * Close all Scanners and PrintWriters
     * 
     * @throws FileNotFoundException
     */
    public void closeEverythingAndSave() throws FileNotFoundException {
        alumniSaved = new PrintWriter("alumni.txt");
        eventSaved = new PrintWriter("events.txt");
        donationsSaved = new PrintWriter("donations.txt");
        trainingSaved = new PrintWriter("training.txt");

        for (Alumni alumni : alumniMap.values()) {
            alumniSaved.println(alumni.save());
        }

        for (Event event : eventMap.values()) {
            eventSaved.println(event.save());
            eventSaved.println(event.saveDateTime());
            eventSaved.println(event.saveHost());
            eventSaved.println(event.saveAttendants());

        }

        for (Donation donation : donationList) {
            donationsSaved.println(donation.save());
        }

        for (Training training : trainingMap.values()) {
            trainingSaved.println(training.save());
            trainingSaved.println(training.saveDateTime());
            trainingSaved.println(training.saveHost());
            trainingSaved.println(training.saveAttendants());
        }

        in.close();
        alumniFileIn.close();
        eventFileIn.close();
        alumniSaved.close();
        eventSaved.close();
        trainingSaved.close();
        donationsSaved.close();

    }

    // ==================== EXISTING ====================

    /**
     * Create and Fill a TreeMap with Alumni Objects from a text file
     */
    public void existingAlumni() {
        alumniMap = new TreeMap<>();
        while (alumniFileIn.hasNextLine()) {
            String line = alumniFileIn.nextLine();
            String[] s = line.split("%");
            int id = Integer.parseInt(s[0]);
            String name = s[1];
            String address = s[2];
            String major = s[3];
            int gradYear = Integer.parseInt(s[4]);
            String job = s[5];
            String organization = s[6];
            String password = s[7];
            Alumni a = new Alumni(id, name, address, major, gradYear, job, organization, password);
            alumniMap.put(id, a);
        }
    }

    /**
     * Create and fill a ArrayList with Donation Objects from text file
     */
    public void existingDonations() {
        donationList = new ArrayList<>();

        while (donationsFileIn.hasNextLine()) {
            String line = donationsFileIn.nextLine();
            String[] s = line.split("%");
            int alumniID = Integer.parseInt(s[0]);
            int eventID = Integer.parseInt(s[1]);
            double amount = Double.parseDouble(s[2]);
            int year = Integer.parseInt(s[3]);
            int month = Integer.parseInt(s[4]);
            int day = Integer.parseInt(s[5]);
            int hour = Integer.parseInt(s[6]);
            int min = Integer.parseInt(s[7]);
            int sec = Integer.parseInt(s[8]);
            LocalDateTime ldt = extractDateTimeDonation(year, month, day, hour, min, sec);
            new Donation(alumniID, eventID, amount);
            donationList.add(new Donation(alumniID, eventID, amount, ldt));
        }

    }

    /**
     * Create and Fill a TreeMap with Events from a text file
     */
    public void existingEvents() {
        eventMap = new TreeMap<>();
        while (eventFileIn.hasNextLine()) {
            // event info
            String line = eventFileIn.nextLine();
            String[] s = line.split("%");
            int id = Integer.parseInt(s[0]);
            String name = s[1];
            int room = Integer.parseInt(s[2]);
            int totalSpots = Integer.parseInt(s[3]);
            Alumni guestSpeaker = extractGuestSpeaker(Integer.parseInt(s[4]));
            // dateTime info
            String dateTimeString = eventFileIn.nextLine();
            LocalDateTime dateTime = extractDateTime(dateTimeString);
            // host info
            String h = eventFileIn.nextLine();
            Host host = extractHost(h);
            // attending alumni info
            String list = eventFileIn.nextLine();
            ArrayList<Integer> att = extractAttendants(list);

            Event e = new Event(id, name, room, totalSpots, dateTime, att, host, guestSpeaker);
            eventMap.put(id, e);
        }
    }

    /**
     * Create and fill a TreeMap with Training Events from a text file
     */
    public void existingTrainingEvents() {
        trainingMap = new TreeMap<>();
        while (trainingFileIn.hasNextLine()) {
            String line = trainingFileIn.nextLine();
            String[] s = line.split("%");
            int id = Integer.parseInt(s[0]);
            String name = s[1];
            int room = Integer.parseInt(s[2]);
            int totalSpots = Integer.parseInt(s[3]);
            Alumni guestSpeaker = extractGuestSpeaker(Integer.parseInt(s[4]));
            String skill = s[5];
            // dateTime info
            String dateTimeString = trainingFileIn.nextLine();
            LocalDateTime dateTime = extractDateTime(dateTimeString);

            // host info
            String h = trainingFileIn.nextLine();
            Host host = extractHost(h);
            // attending alumni info
            String list = trainingFileIn.nextLine();
            ArrayList<Integer> att = extractAttendants(list);

            Training t = new Training(id, name, room, totalSpots, dateTime, att, host, skill, guestSpeaker);
            trainingMap.put(id, t);
        }
    }

    /**
     * Extract Attendants from a string and add them to an ArrayList
     * 
     * @param list Line from text file containing Attendants
     * @return ArrayList of Attendants
     */
    private ArrayList<Integer> extractAttendants(String list) {
        String[] listArr = list.split("%");
        ArrayList<Integer> att = new ArrayList<>();
        for (int i = 0; i < listArr.length; i++) {
            att.add(Integer.parseInt(listArr[i]));
        }
        return att;
    }

    /**
     * Extract LocalDateTime information from a string and create a LDT obj
     * 
     * @param dateTimeString Line from text file containing LDT info
     * @return LocalDateTime Object
     */
    private LocalDateTime extractDateTime(String dateTimeString) {
        String[] dt = dateTimeString.split("%");
        int year = Integer.parseInt(dt[0]);
        int month = Integer.parseInt(dt[1]);
        int dayOfMonth = Integer.parseInt(dt[2]);
        int hour = Integer.parseInt(dt[3]);
        int minute = Integer.parseInt(dt[4]);
        return LocalDateTime.of(year, month, dayOfMonth, hour, minute);
    }

    /**
     * Get the guest speaker alumni obj from
     * 
     * @param guestSpeakerId
     * @return
     */
    private Alumni extractGuestSpeaker(int guestSpeakerId) {
        Alumni guestSpeaker;
        if (guestSpeakerId == 0) {
            guestSpeaker = null;
        } else {
            guestSpeaker = alumniMap.get(guestSpeakerId);
        }
        return guestSpeaker;
    }

    private LocalDateTime extractDateTimeDonation(int year, int month, int day, int hour, int min, int sec) {
        return LocalDateTime.of(year, month, day, hour, min, sec);
    }

    /**
     * Extract Host information from a string and create a Host obj
     * 
     * @param h Line form text file containing Host info
     * @return Host object
     */
    private Host extractHost(String h) {
        String[] hArr = h.split("%");
        int hostId = Integer.parseInt(hArr[0]);
        String topic = hArr[1];
        long phone = Long.parseLong(hArr[2]);
        String email = hArr[3];
        String hostName = alumniMap.get(hostId).getName();
        String hostAdd = alumniMap.get(hostId).getAddress();
        String hostMaj = alumniMap.get(hostId).getMajor();
        int hostGY = alumniMap.get(hostId).getGradYear();
        String hostJob = alumniMap.get(hostId).getJob();
        String hostOrg = alumniMap.get(hostId).getOrganization();
        return new Host(hostId, hostName, hostAdd, hostMaj, hostGY, hostJob, hostOrg, topic, phone, email);
    }

    // ==================== GETTERS ====================

    // ----- ALUMNI -----

    /**
     * Get toString call for an Alumni Obj
     * 
     * @param id Alumni ID
     * @return Alumni toString
     */
    public String getAlumni(int id) {
        return alumniMap.get(id).toString();
    }

    /**
     * Get Mailing Address of Alumni
     * 
     * @param id Alumni ID
     * @return Alumni's Mailing Address
     */
    public String getAlumniAddress(int id) {
        return alumniMap.get(id).getAddress();
    }

    /**
     * Get Graduation Year of Alumni
     * 
     * @param id Alumni ID
     * @return Alumni's Graduation Year
     */
    public int getAlumniGradYear(int id) {
        return alumniMap.get(id).getGradYear();
    }

    /**
     * Get Job Tile of ALumni
     * 
     * @param id Alumni ID
     * @return Alumni's Job Title
     */
    public String getAlumniJob(int id) {
        return alumniMap.get(id).getJob();
    }

    /**
     * Get Major of ALumni
     * 
     * @param id Alumni ID
     * @return Alumni's Major
     */
    public String getAlumniMajor(int id) {
        return alumniMap.get(id).getMajor();
    }

    /**
     * Get Name of ALumni
     * 
     * @param id Alumni ID
     * @return Alumni's Name
     */
    public String getAlumniName(int id) {
        return alumniMap.get(id).getName();
    }

    /**
     * Get Employing Organization of Alumni
     * 
     * @param id Alumni ID
     * @return Alumni's Employing Organization
     */
    public String getAlumniOrg(int id) {
        return alumniMap.get(id).getOrganization();
    }

    // ----- EVENTS -----

    /**
     * Get toString call for an Event Obj
     * 
     * @param id Event ID
     * @return Event toString
     */
    public String getEvent(int id) {
        return eventMap.get(id).toString();
    }

    /**
     * Get Host of Event
     * 
     * @param id Event ID
     * @return Event's Host
     */
    public String getEventHost(int id) {
        return eventMap.get(id).getHosttoString();
    }

    /**
     * Get Year of Event
     * 
     * @param id Event ID
     * @return Year in which Event takes place
     */
    public int getEventYear(int id) {
        return eventMap.get(id).getYear();
    }

    /**
     * Get Month of Event
     * 
     * @param id Event ID
     * @return Month in which Event takes place
     */
    public int getEventMonth(int id) {
        return eventMap.get(id).getMonth();
    }

    /**
     * Get Day of Event
     * 
     * @param id Event ID
     * @return Day on which Event takes place
     */
    public int getEventDay(int id) {
        return eventMap.get(id).getDay();
    }

    /**
     * Get Hour of Event
     * 
     * @param id Event ID
     * @return Hour that the Event starts
     */
    public int getEventHour(int id) {
        return eventMap.get(id).getHour();
    }

    /**
     * Get Minute of Event
     * 
     * @param id Event ID
     * @return Minute that the Event starts
     */
    public int getEventMin(int id) {
        return eventMap.get(id).getMinute();
    }

    /**
     * Get ID of Host of Event
     * 
     * @param id Event ID
     * @return Event's Host's ID
     */
    public int getHostId(int id) {
        return eventMap.get(id).getHostId();
    }
    // ----- TRAINING -----

    /**
     * Get toString call for a Training Obj
     * 
     * @param id Training ID
     * @return Training toString
     */
    public String getTraining(int id) {
        return trainingMap.get(id).toString();
    }

    /**
     * Get Host of Training
     * 
     * @param id Training ID
     * @return Training's Host's toString
     */
    public String getTrainingHost(int id) {
        return trainingMap.get(id).getHosttoString();
    }

    // ----- RAND -----

    /**
     * Get Password for Alumni
     * 
     * @param id Alumni ID
     * @return ALumni's Password
     */
    public String getPassword(int id) {
        return alumniMap.get(id).getPassword();
    }

    // ==================== SETTERS ====================

    // ----- ALUMNI -----

    /**
     * Set the Mailing Address for Alumni
     * 
     * @param id      Alumni ID
     * @param address New Mailing Address for Alumni
     */
    public void setAlumniAddress(int id, String address) {
        alumniMap.get(id).setAddress(address);
    }

    /**
     * Set the Graduation Year for Alumni
     * 
     * @param id       ALumni ID
     * @param gradYear New Graduation Year for Alumni
     */
    public void setAlumniGradYear(int id, int gradYear) {
        alumniMap.get(id).setGradYear(gradYear);
    }

    /**
     * Set the Job Title for ALumni
     * 
     * @param id  ALumni ID
     * @param job New Job Title for ALumni
     */
    public void setAlumniJob(int id, String job) {
        alumniMap.get(id).setJob(job);
    }

    /**
     * Set the Major for Alumni
     * 
     * @param id    ALumni ID
     * @param major New Major for ALumni
     */
    public void setAlumniMajor(int id, String major) {
        alumniMap.get(id).setMajor(major);
    }

    /**
     * Set Name for Alumni
     * 
     * @param id   Alumni ID
     * @param name New Name for Alumni
     */
    public void setAlumniName(int id, String name) {
        alumniMap.get(id).setName(name);
    }

    /**
     * Set Employing Organization for ALumni
     * 
     * @param id  Alumni ID
     * @param org New Employing Organization for ALumni
     */
    public void setAlumniOrg(int id, String org) {
        alumniMap.get(id).setOrganization(org);
    }

    // ----- EVENTS -----

    /**
     * Set a LocalDateTime for an Event
     * 
     * @param id     Event ID
     * @param year   Year in which Event is happening
     * @param month  Month in which Event is happening
     * @param day    Day on which Event is happening
     * @param hour   Hour that the Event starts
     * @param minute Minute that the Event starts
     */
    public void setEventDateTime(int id, int year, int month, int dayOfMonth, int hour, int minute) {
        eventMap.get(id).setStartDate(year, month, dayOfMonth, hour, minute);
    }

    /**
     * Set Name for Event
     * 
     * @param id   Event ID
     * @param name New Name for Event
     */
    public void setEventName(int id, String name) {
        eventMap.get(id).setName(name);
    }

    /**
     * Set a Room Number for Event
     * 
     * @param id   Event ID
     * @param room New Room Number for Event
     */
    public void setEventRoom(int id, int room) {
        eventMap.get(id).setRoom(room);
    }

    public void setGuestSpeaker(int eventId, int guestSpeakerId) {
        eventMap.get(eventId).setGuestSpeaker(alumniMap.get(guestSpeakerId));
    }

    // ----- HOST -----

    /**
     * Set a new Topic for the Host of a Training Event
     * 
     * @param trainingID ID of Training Event to be Edited
     * @param topic      New topic for Host
     */
    public void setTrainingHostTopic(int trainingID, String topic) {
        trainingMap.get(trainingID).getHost().setTopic(topic);
    }

    /**
     * Set a new Phone Number for the Host of a Training Event
     * 
     * @param trainingID  ID of Training Event to be Edited
     * @param phoneNumber New Phone Number for Host
     */
    public void setTrainingHostPhone(int trainingID, long phoneNumber) {
        trainingMap.get(trainingID).getHost().setPhoneNumber(phoneNumber);
    }

    /**
     * Set a new Email Address for the Host of a Training Event
     * 
     * @param trainingID   ID of Training Event to be Edited
     * @param emailAddress New Email Address for Host
     */
    public void setTrainingHostEmail(int trainingID, String emailAddress) {
        trainingMap.get(trainingID).getHost().setEmailAddress(emailAddress);
    }

    /**
     * Set a new Topic for the Host of a Event
     * 
     * @param eventID ID of Event to be Edited
     * @param topic   New Topic for Host
     */
    public void setEventHostTopic(int eventID, String topic) {
        eventMap.get(eventID).getHost().setTopic(topic);
    }

    /**
     * Set a new Phone Number for the Host of a Event
     * 
     * @param eventID     ID of Event to be Edited
     * @param phoneNumber New Phone Number for Host
     */
    public void setEventHostPhone(int eventID, long phoneNumber) {
        eventMap.get(eventID).getHost().setPhoneNumber(phoneNumber);
    }

    /**
     * Set a new Email Address for the Host of a Event
     * 
     * @param eventID      ID of Event to be Edited
     * @param emailAddress New Email Address for Host
     */
    public void setEventHostEmail(int eventID, String emailAddress) {
        eventMap.get(eventID).getHost().setEmailAddress(emailAddress);
    }

    // ----- TRAINING -----

    /**
     * Set a LocalDateTime for a Training Event
     * 
     * @param id         Training ID
     * @param year       Year in which Training is happening
     * @param month      Month in which Training is happening
     * @param dayOfMonth Day on which Training is happening
     * @param hour       Hour that the Training starts
     * @param minute     Minute that the Training starts
     */
    public void setTrainingDate(int id, int year, int month, int dayOfMonth, int hour, int minute) {
        trainingMap.get(id).setStartDate(year, month, dayOfMonth, hour, minute);
    }

    /**
     * Set Name for Training Event
     * 
     * @param id   Training ID
     * @param name New Name for Training Event
     */
    public void setTrainingName(int id, String name) {
        trainingMap.get(id).setName(name);
    }

    /**
     * Set Room Number for Training Event
     * 
     * @param id   Training ID
     * @param room New Room Number for Training Event
     */
    public void setTrainingRoom(int id, int room) {
        trainingMap.get(id).setRoom(room);
    }

    /**
     * Set Skill for Training Event
     * 
     * @param id       Training ID
     * @param newSkill New Skill for Training Event
     */
    public void setTrainingSkill(int id, String newSkill) {
        trainingMap.get(id).setSkill(newSkill);
    }

    // ----- RAND ----

    /**
     * Set Password for an Alumni
     * 
     * @param id    Alumni ID
     * @param newPw New Password for Alumni
     */
    public void setPassword(int id, String newPw) {
        alumniMap.get(id).setPassword(newPw);
    }

    /**
     * Set Total Number of Spots Available at an Event
     * 
     * @param id    Event ID
     * @param spots New Number of Total Available Spots
     */
    public void setNumOfTotalSpotsEvents(int id, int spots) {
        eventMap.get(id).setTotalSpots(spots);
    }

    /**
     * Set Total Number of Spots Available at an Training Event
     * 
     * @param id    Training ID
     * @param spots New number of Total Available Spots
     */
    public void setNumOfTotalSpotsTraining(int id, int spots) {
        trainingMap.get(id).setTotalSpots(spots);
    }

    // ==================== DISPLAY ====================

    /**
     * Display the Alumni Map
     */
    public void displayAlumni() {
        for (Alumni alumni : alumniMap.values()) {
            System.out.println(" ----------------------------------------------------- ");
            System.out.println(alumni.toString());
        }
    }

    /**
     * Display Attendants for an Event
     * 
     * @param eventID Event ID
     */
    public void displayAttendantsEvent(int eventID) {
        System.out.println("Attendants:");
        for (Event event : eventMap.values()) {
            if (event.isEmpty()) {
                System.out.println("-!-NO ATTENDANTS-!-");
            } else {
                System.out.println(" ----------------------------------------------------- ");
                int id = event.getAttendants();
                System.out.println(alumniMap.get(id).getName());
            }
        }
    }

    /**
     * Display Attendants for a Training Event
     * 
     * @param trainingID Training ID
     */
    public void displayAttendantsTraining(int trainingID) {
        System.out.println("Attendants:");
        for (Training training : trainingMap.values()) {
            if (training.isEmpty()) {
                System.out.println("-!-NO ATTENDANTS-!-");
            } else {
                System.out.println(" ----------------------------------------------------- ");
                int id = training.getAttendants();
                System.out.println(alumniMap.get(id).getName());
            }
        }
    }

    /**
     * Display Events and Training Events that happen within a specified year
     * 
     * @param year Year to display
     */
    public void displayByYear() {
        int year = 0;
        do {
            System.out.println("Enter a Year:");
            year = intInput();
        } while (year < 1000);
        int check = Integer.parseInt(Integer.toString(year).substring(2, 4));
        System.out.println("Events happening in the year " + year);
        for (Event event : eventMap.values()) {
            if (event.getYear() == check) {
                System.out.println(" ----------------------------------------------------- ");
            System.out.println(event.toString());
            }
        }
        System.out.println(" ----------------------------------------------------- ");
        System.out.println("Training happening in the year " + year);
        for (Training training : trainingMap.values()) {
            if (training.getYear() == check) {
                System.out.println(" ----------------------------------------------------- ");
            System.out.println(training.toString());
            }
        }
    }

    /**
     * Displays the Donations made by an Alumni
     * 
     * @param id Alumni ID
     */
    public void displayDonationsAlumni(int id) {
        for (int i = 0; i < donationList.size(); i++) {
            if (id == donationList.get(i).getAlumniId()) {
                System.out.println(" ----------------------------------------------------- ");
                System.out.println(donationList.get(i).toString());
            }
        }
    }

    /**
     * Displays the Donations for an Event
     * 
     * @param id Event ID
     */
    public void displayDonationsEvents(int id) {
        for (int i = 0; i < donationList.size(); i++) {
            if (id == donationList.get(i).getEventId()) {
                System.out.println(" ----------------------------------------------------- ");
                System.out.println("Donation Amount" + donationList.get(i).getAmountDonated());
            }
        }

    }

    /**
     * Display the Events Map and their Host
     */
    public void displayEvents() {
        for (Event events : eventMap.values()) {
            System.out.println(" ----------------------------------------------------- ");
            System.out.println(events.toString());
            System.out.println(events.getHosttoString());
        }
    }

    /**
     * Display Host's for both Events and Training
     */
    public void displayHosts() {
        System.out.println("The Hosts for Events are:");
        for (Event events : eventMap.values()) {
            System.out.println(" ----------------------------------------------------- ");
            System.out.println("Event " + events.getID() + " " + events.getHosttoString());
        }
        System.out.println("The Hosts for Trainings are:");
        for (Training training : trainingMap.values()) {
            System.out.println(" ----------------------------------------------------- ");
            System.out.println("Training " + training.getID() + " " + training.getHosttoString());
        }

    }

    /**
     * Display the Events and Training Events that an Alumni is attending
     * 
     * @param id Alumni ID
     */
    public void displayMyAttendance(int id) {
        System.out.println("My Events:");
        int counter = 0;
        for (Event event : eventMap.values()) {
            if (event.checkForAttendance(id)) {
                System.out.println(" ----------------------------------------------------- ");
                System.out.println("You are attending " + event.getName() + " | Event ID # " + event.getID());
                counter++;
            }
        }
        if (counter == 0)
            System.out.println("NOT CURRENTLY ATTENDING ANY EVENTs");
        counter = 0;
        System.out.println("My Training:");
        for (Training training : trainingMap.values()) {
            if (training.checkForAttendance(id)) {
                System.out.println(" ----------------------------------------------------- ");
                System.out.println(
                        "You are attending " + training.getName() + " | Training Event ID # " + training.getID());
                counter++;
            }
        }
        if (counter == 0)
            System.out.println("NOT CURRENTLY ATTENDING ANY TRAINING");
    }

    /**
     * Display Training Events and their Host
     */
    public void displayTraining() {
        for (Training training : trainingMap.values()) {
            System.out.println(" ----------------------------------------------------- ");
            System.out.println(training.toString());
            System.out.println(training.getHosttoString());
        }
    }

    // ==================== CHECKERS ====================

    /**
     * Check to see if an ALumni's ID exists in the ALumni map
     * 
     * @param id Alumni ID
     * @return Whether the Alumni is in the map or not
     */
    public boolean checkId(int id) {
        for (Alumni alumni : alumniMap.values()) {
            if (id == alumni.getID())
                return true;
        }
        return false;
    }

    /**
     * Check to see if an Alumni is already attending an Event
     * 
     * @param id      Alumni ID
     * @param eventID Event ID
     * @return Whether the ALumni is already attending
     */
    public boolean alreadyAttendingEvent(int id, int eventID) {
        return (eventMap.get(eventID).checkForAttendance(id));
    }

    /**
     * Check to see if an Alumni is already attending a Training Event
     * 
     * @param id         Alumni ID
     * @param trainingID Training ID
     * @return Whether the Alumni is already attending
     */
    public boolean alreadyAttendingTraining(int id, int trainingID) {
        return (trainingMap.get(trainingID).checkForAttendance(id));
    }

    /**
     * Checks to see if the Event being requested is in the map
     * 
     * @param eventID ID of Event to be checked
     * @return true if Event is in map, false if Event is not in map
     */
    public boolean isExistingEvent(int eventID) {
        return (eventMap.containsKey(eventID));
    }

    /**
     * Checks to see if the Training Event being requested is in the map
     * 
     * @param eventID ID of Training Event to be checked
     * @return true if Event is in map, false if Training Event is not in map
     */
    public boolean isExistingTraining(int eventID) {
        return (trainingMap.containsKey(eventID));
    }
    // ==================== CREATE ====================

    /**
     * Create and place an Alumni Object into the AlumniMap
     * 
     * @param name         Alumni's Name
     * @param address      Alumni's Mailing Address
     * @param major        Alumni's Major
     * @param gradYear     Alumni's Graduation Year
     * @param job          Alumni's Job Title
     * @param organization Alumni's Employing Organization
     * @param password     Alumni's Password
     * @return Alumni's ID
     */
    public int createAlumni(String name, String address, String major, int gradYear, String job,
            String organization,
            String password) {
        int id = alumniMap.lastKey();
        id++;
        Alumni a = new Alumni(id, name, address, major, gradYear, job, organization, password);
        alumniMap.put(id, a);
        return id;
    }

    /**
     * Create and place an Event Object into the EventMap
     * 
     * @param name       Event's Name
     * @param room       Event's Room Number
     * @param totalSpots Total spots available at the Event
     * @param startDate  Starting date for the Event
     * @param host       Event's Host
     */
    public void createEvent(String name, int room, int totalSpots, LocalDateTime startDate, Host host) {
        int id = eventMap.lastKey();
        id++;
        Event e = new Event(id, name, room, totalSpots, startDate, host);
        eventMap.put(id, e);
    }

    /**
     * Create and place a Training Event into the TrainingMap
     * 
     * @param name       Training Event's Name
     * @param room       Training Event's Room Number
     * @param totalSpots Total spots available at the Training Event
     * @param startDate  Starting date for the Training Event
     * @param host       Training Event's Host
     * @param skill      Skill being taught at the Training Event
     */
    public void createTrainingEvent(String name, int room, int totalSpots, LocalDateTime startDate, Host host,
            String skill) {
        int id = trainingMap.lastKey();
        id++;
        Training t = new Training(id, name, room, totalSpots, startDate, host, skill);
        trainingMap.put(id, t);
    }

    // ==================== DELETE ====================

    /**
     * Delete Specified Alumni from ALumniMap and any Events / Training they own from the corresponding maps
     * 
     * @param id Alumni ID
     */
    public void deleteAlumni(int id) {
        for (Event event : eventMap.values()) {
            if (id == event.getHostId()) {
                int e = event.getID();
                eventMap.remove(e);
            }
        }
        for (Training training : trainingMap.values()) {
            if (id == training.getHostId()) {
                int t = training.getID();
                trainingMap.remove(t);
            }
        }
        alumniMap.remove(id);
    }

    /**
     * Delete Specified Event from EventMap
     * 
     * @param id Event ID
     */
    public void deleteEvent(int id) {
        eventMap.remove(id);
    }

    /**
     * Delete Specified Training Event from TrainingMap
     * 
     * @param id
     */
    public void deleteTraining(int id) {
        trainingMap.remove(id);
    }

    // ==================== ADDERS ====================

    /**
     * Add a Donation and put it in the Donation List
     * 
     * @param alumniId       ID of the ALumni making the donation
     * @param eventId        ID of the Event that the donation is going towards
     * @param donationAmount Amount of the Donation being made
     */
    public void addDonationToList(int alumniId, int eventId, double donationAmount) {
        donationList.add(new Donation(alumniId, eventId, donationAmount));
        System.out.println(donationList.get(donationList.size() - 1).toString());
    }

    /**
     * Register an ALumni (by Name) for an Event
     * 
     * @param id   Event ID
     * @param name Name of attending ALumni
     */
    public void joinEvent(int eventId, int id) {
        eventMap.get(eventId).addAttendant(id);
    }

    /**
     * Register an ALumni (by Name) for an Training Event
     * 
     * @param id   Training Event ID
     * @param name Name of attending ALumni
     */
    public void joinTraining(int eventId, int id) {
        trainingMap.get(eventId).addAttendant(id);
    }

    // ==================== INPUT====================

    /**
     * Get User Text Input
     * 
     * @return User input : nextLine
     */
    public String stringInput() {
        return in.nextLine();

    }

    /**
     * Get User Integer Input
     * 
     * @return User input : int
     */
    public int intInput() {
        int n;
        do {
            while (!in.hasNextInt()) {
                String s = in.next();
                System.out.println(" ----------------------------------------------------- ");
                System.out.printf("\"%s\" IS NOT A VALID NUMBER%n", s);
                System.out.println(" ----------------------------------------------------- ");
            }
            n = in.nextInt();
            if (n < 0) {
                System.out.println(" ----------------------------------------------------- ");
                System.out.println(n + " IS NOT A VALID NUMBER.");
                System.out.println(" ----------------------------------------------------- ");
            }
            in.nextLine();

        } while (n < 0);
        return n;
    }

    /**
     * Get User Integer Input within a boundary
     * 
     * @return User input : int
     */
    public int intInput(int boundary) {
        int n = 0;
        if (!in.hasNextInt()) {
            String s = in.next();
            System.out.println(" ----------------------------------------------------- ");
            System.out.printf("\"%s\" IS NOT A VALID NUMBER%n", s);
            System.out.println(" ----------------------------------------------------- ");
            return n;
        }
        n = in.nextInt();
        if (n > boundary || n < 1) {
            System.out.println(" ----------------------------------------------------- ");
            System.out.println(n + " IS NOT A VALID NUMBER.");
            System.out.println(" ----------------------------------------------------- ");
        }
        in.nextLine();
        return n;
    }

    /**
     * Get User Long input
     * 
     * @return User input : Long
     */
    public long longInput() {
        long n;
        do {
            while (!in.hasNextLong()) {
                String s = in.next();
                System.out.println(" ----------------------------------------------------- ");
                System.out.printf("\"%s\" IS NOT A VALID NUMBER%n", s);
                System.out.println(" ----------------------------------------------------- ");
            }
            n = in.nextLong();
            if (n < 0) {
                System.out.println(" ----------------------------------------------------- ");
                System.out.println(n + " IS NOT A VALID NUMBER.");
                System.out.println(" ----------------------------------------------------- ");
            }
            in.nextLine();

        } while (n < 0);
        return n;
    }

    /**
     * Get User Double Input
     * 
     * @return User input : double
     */
    public double doubleInput() {
        double n;
        do {
            while (!in.hasNextDouble()) {
                String s = in.next();
                System.out.println(" ----------------------------------------------------- ");
                System.out.printf("\"%s\" IS NOT A VALID NUMBER%n", s);
                System.out.println(" ----------------------------------------------------- ");
            }
            n = in.nextDouble();
            if (n < 0) {
                System.out.println(" ----------------------------------------------------- ");
                System.out.println(n + " IS NOT A VALID NUMBER.");
                System.out.println(" ----------------------------------------------------- ");
            }
            in.nextLine();

        } while (n < 0);
        return n;
    }
}
