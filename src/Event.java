import java.util.ArrayList;
import java.time.LocalDateTime;

public class Event {
    private int id;
    private String name;
    private int room;
    private int numberOfParticipants;
    // TODO change date format
    private LocalDateTime startDate;
    // TODO implement speaker
    private Host host;
    private Alumni guestSpeaker;
    private ArrayList<String> attendants;

    // constructors
    public Event() {
        // empty
    }

    // existing event
    public Event(int id, String name, int room, int numberOfParticipants, LocalDateTime startDate,
            ArrayList<String> attendant, Host host) {
        this.id = id;
        this.name = name;
        this.room = room;
        this.numberOfParticipants = numberOfParticipants;
        this.startDate = startDate;
        this.attendants = attendant;
        this.host = host;
    }

    // adding event
    public Event(int id, String name, int room, int numberOfParticipants, LocalDateTime startDate, Host host) {
        this.id = id;
        this.name = name;
        this.room = room;
        this.numberOfParticipants = numberOfParticipants;
        this.startDate = startDate;
        this.host = host;
    }

    // getters and setters
    public Alumni getAlumni(){
        return guestSpeaker;
    }
    
    public void setAlumni(Alumni alumni){
        guestSpeaker = alumni;
    }
    public int getId() {
        return this.id;
    }

    public int getHostId() {
        return host.getId();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // testing
    public void setTime(int year, int month, int day, int hour, int minute) {
        this.startDate = LocalDateTime.of(year, month, day, hour, minute);
    }

    public int getRoom() {
        return this.room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public int getNumberOfParticipants() {
        return this.numberOfParticipants;
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public LocalDateTime getStartDate() {
        return this.startDate;
    }

    public void setStartDate(int year, int month, int dayOfMonth, int hour, int minute) {
        startDate = LocalDateTime.of(year, month, dayOfMonth, hour, minute);
    }

    public void addAttendant(String name) {
        attendants.add(name);
    }

    @Override
    public String toString() {
        return "{" + " id='" + getId() + "'" + ", name='" + getName() + "'" + ", time='" + startDate.getHour() + ":" + startDate.getMinute() + "'" + ", room='"
                + getRoom() + "'" + ", numberOfParticipants='" + getNumberOfParticipants() + "'" + ", startDate='"
                + getStartDate() + "'" + "}";
    }

    public String save() {
        return getId() + "," + getName() + "," +  "," + getRoom() + "," + getNumberOfParticipants() + ","
                + getStartDate();
    }

    public String saveHost() {
        return host.save();
    }

    public String saveAttendants() {
        String x = "";
        for (int i = 0; i < attendants.size(); i++) {
            if (i == attendants.size() - 1) {
                x += attendants.get(i);
            } else {
                x += attendants.get(i) + ",";
            }
        }
        return x;
    }

}
