-- Loads
file = LOAD '$input_file' USING PigStorage('\t') AS (count:long,userID:long,userName:chararray,messageID:long,date:chararray,gps_lat:double,gps_long:double,source:chararray,tweet:chararray,block:chararray);

--file = LOAD '../data/complete_grid.txt' USING PigStorage('\t') AS (count:long,userID:long,userName:chararray,messageID:long,date:chararray,gps_lat:double,gps_long:double,source:chararray,tweet:chararray,block:chararray);


-- Processing

grouped = GROUP file BY block;

out_line = FOREACH grouped {
	users = DISTINCT file.userID;
	cnt_users = COUNT(users);
	cnt_tweets = COUNT (file);
	GENERATE group, SIZE(group) AS blocklength, cnt_users AS cnt_users, cnt_tweets AS cnt_tweets;	
};

result = ORDER out_line BY cnt_tweets DESC, group;

STORE result INTO '$output_dir'; --write the result on Disk. $output_dir is a command line argument

