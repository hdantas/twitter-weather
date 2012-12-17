-- dont forget to preprocess the twitter dumps to delete any \n or \t that may give problems later

REGISTER bin/masti4.jar;

DEFINE AssignToBlockPig masti4.AssignToBlockPig;
DEFINE getTimeStampPig masti4.getTimeStampPig;

DEFINE AssignGPSBlock (notdone_tweets) RETURNS incomplete_tweets, complete_tweets {

	tweets = FOREACH $notdone_tweets GENERATE count, userID, gps_lat, gps_long, AssignToBlockPig(gps_lat,gps_long,block) AS block;
	
	grouped_tweets = group tweets BY block;
	grids = FOREACH grouped_tweets GENERATE *, COUNT(tweets) AS count_tweets;
	
	incomplete = FILTER grids BY (count_tweets > 1); -- '1' should be replaced by the threshold
	$incomplete_tweets = FOREACH incomplete GENERATE FLATTEN(tweets);
	
	complete = FILTER grids BY (count_tweets <= 1); -- '1' should be replaced by the threshold
	$complete_tweets = FOREACH complete GENERATE FLATTEN(tweets);
};


-- Loads
file = LOAD '../data/tweets_small_geotag.txt' USING PigStorage('\t') AS (count:int,userID:int,userName:chararray,messageID:int,date:chararray,gps_lat:double,gps_long:double,source:chararray,tweet:chararray);

clean_file = FILTER file BY (gps_lat != -1000) AND (gps_long != -1000);

-- Processing
firstrun = FOREACH clean_file GENERATE count, userID, gps_lat, gps_long, '' AS block;
notdone0,done0 = AssignGPSBlock(firstrun);
finished0 = done0;

notdone1,done1 = AssignGPSBlock(notdone0);
finished1 = UNION done1,finished0;

notdone2,done2 = AssignGPSBlock(notdone1);
finished2 = UNION done2,finished1;

notdone3,done3 = AssignGPSBlock(notdone2);
finished3 = UNION done3, finished2;

notdone4,done4 = AssignGPSBlock(notdone3);
finished4 = UNION done4, finished3;

geotaged_tweets = UNION notdone4, finished4;
result = geotaged_tweets;

STORE result INTO '$output_dir'; --write the result on Disk. $output_dir is a command line argument
