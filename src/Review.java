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
                  String businessStars) {
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
                + getBusinessStars() + "\n";
               
    }

}


