
public class Alumni {
    private String name;
    private String address;
    private String major;
    private String gradYear;
    private String job;
    private String organization;

    public Alumni() {

    }
    /**
     * passing in values for alumni
     * @param name name of alumni
     * @param address mailing address
     * @param major School Major for alumni's degree
     * @param gradYear Year of graduation 
     * @param job employment of alumni
     * @param organization employer of almuni
     */
    public Alumni(String name, String address, String major, String gradYear, String job, String organization) {
        this.name = name;
        this.address = address;
        this.major = major;
        this.gradYear = gradYear;
        this.job = job;
        this.organization = organization;
    }

    /**
     * to get the name of alumni
     * @return returns the name of alumni 
     */ 
    public String getName() {
        return this.name;
    }

    /**
     * sets name of alumni
     * @param name passing name of alumni
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMajor() {
        return this.major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getGradYear() {
        return this.gradYear;
    }

    public void setGradYear(String gradYear) {
        this.gradYear = gradYear;
    }

    public String getJob() {
        return this.job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getOrganization() {
        return this.organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }
    

}