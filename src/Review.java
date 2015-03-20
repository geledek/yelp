public class Review {
	
	private String businessId;
    private String userId;
    private String stars;
    private String text;
    private String date;
    private String vote;
    private String voteFunny;
    private String voteUseful;
    private String voteCool;

    private String businessName;
    private String longitude;
    private String latitude;
    private String businessStars;
    private String fullAddress;
    private String city;
    private String state;
    private String neighborhoods;
    private String category;
    private String monday;
    private String tuesday;
    private String wednesday;
    private String thursday;
    private String friday;
    private String saturday;
    private String sunday;


    public Review() {
    }

    /** Creates a new instance of Accommodation */
    public Review(String businessId,
                  String userId,
                  String stars,
                  String text,
                  String date,
                  String voteFunny,
                  String voteUseful,
                  String voteCool,
                  String name,
                  String longitude,
                  String latitude,
                  String businessStars,
                  String fullAddress,
                  String city,
                  String state,
                  String neighborhoods,
                  String category,
                  String monday,
                  String tuesday,
                  String wednesday,
                  String thursday,
                  String friday,
                  String saturday,
                  String sunday) {
        this.businessId = businessId;
        this.userId = userId;
        this.stars = stars;   
        this.text = text;
        this.date = date;
        this.voteFunny = voteFunny;
        this.voteUseful = voteUseful;
        this.voteCool = voteCool;

        this.businessName = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.businessStars = businessStars;
        this.fullAddress = fullAddress;
        this.city = city;
        this.state = state;
        this.neighborhoods = neighborhoods;
        this.category = category;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }

    public String getFullAddress () {
        return fullAddress;
    }

    public String getCity () {
        return city;
    }

    public String getState () {
        return state;
    }

    public String getNeighborhoods () {
        return neighborhoods;
    }

    public String getCategory () {
        return category;
    }

    public String getBusinessId() {
        return businessId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String name){
        this.businessName = name;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getUserId() {
        return userId;
    }
    
    public String getStars() {
        return stars;
    }
    
    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public String getVote() {
        return this.vote;
    }

    public String getBusinessStars() {
        return businessStars;
    }

    public void setBusinessStars(String businessStars) {
        this.businessStars = businessStars;
    }

    public String getVoteFunny() {
        return voteFunny;
    }

    public void setVoteFunny(String voteFunny) {
        this.voteFunny = voteFunny;
    }

    public String getVoteUseful() {
        return voteUseful;
    }

    public void setVoteUseful(String voteUseful) {
        this.voteUseful = voteUseful;
    }

    public String getVoteCool() {
        return voteCool;
    }

    public void setVoteCool(String voteCool) {
        this.voteCool = voteCool;
    }

    public String getMonday () {
        return monday;
    }

    public String getTuesday () {
        return tuesday;
    }

    public String getWednesday () {
        return wednesday;
    }

    public String getThursday () {
        return thursday;
    }

    public String getFriday () {
        return friday;
    }

    public String getSaturday () {
        return saturday;
    }

    public String getSunday () {
        return sunday;
    }


    public String toString() {
        return "Review "
                + getBusinessId() + "\t"
                + getUserId() + "\t"
                + getStars() + "\t"
                + getText() + "\t"
                + getDate() + "\t"
                + getVoteFunny() + "\t"
                + getVoteUseful() + "\t"
                + getVoteCool() + "\t"
                + getBusinessName() + "\t"
                + getLongitude() + "\t"
                + getLatitude() + "\t"
                + getBusinessStars() + "\t"
                + getFullAddress() + "\t"
                + getCity() + "\t"
                + getState() + "\t"
                + getNeighborhoods() + "\t"
                + getCategory() + "\t"
                + getMonday() + "\t"
                + getTuesday() + "\t"
                + getWednesday() + "\t"
                + getThursday() + "\t"
                + getFriday() + "\t"
                + getSaturday() + "\t"
                + getSunday() + "\n";
               
    }

}


