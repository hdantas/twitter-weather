class user {

	public int statuses_count;
	public int followers_count;
	public String id_str;
	public String created_at;
	public String screen_name;
	public String lang;
	public String following;
	public String description;
	public int favourites_count;
	public String location;
	public String name;
	public int id;
	public int utc_offset;
	public int friends_count;
}

class multtweets {
	public tweet[] tw;
}

class geo{
	public String type;
	public double[]coordinates;
	
	
}

class tweet {

	public user user;
	public String retweet_count;
	public String id_str;
	public String created_at;
	public String text;
	public geo geo;
	public String source;
	public geo coordinates;
}
