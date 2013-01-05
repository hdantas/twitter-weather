REGISTER bin/masti4.jar;

DEFINE AssignToBlockPig masti4.AssignToBlockPig;
DEFINE getTimeStampPig masti4.getTimeStampPig;

DEFINE AssignGPSBlock (notdone_tweets) RETURNS incomplete_tweets, complete_tweets {

	tweets = FOREACH $notdone_tweets GENERATE count .. tweet, AssignToBlockPig(gps_lat,gps_long,block) AS block;
	
	grouped_tweets = group tweets BY block;
	grids = FOREACH grouped_tweets GENERATE *, COUNT(tweets) AS count_tweets;
	
	incomplete = FILTER grids BY (count_tweets > 100); -- '1' should be replaced by the threshold
	$incomplete_tweets = FOREACH incomplete GENERATE FLATTEN(tweets);
	
	complete = FILTER grids BY (count_tweets <= 100); -- '1' should be replaced by the threshold
	$complete_tweets = FOREACH complete GENERATE FLATTEN(tweets);
};


-- Loads
file = LOAD '../data/complete.txt' USING PigStorage('\t') AS (count:int,userID:int,userName:chararray,messageID:int,date:chararray,gps_lat:double,gps_long:double,source:chararray,tweet:chararray);

clean_file = FILTER file BY (gps_lat != -1000) AND (gps_long != -1000);

-- Processing

firstrun = FOREACH clean_file GENERATE *, '' AS block;
notdone0,done0 = AssignGPSBlock(firstrun);
finished0 = done0;

notdone1,done1 = AssignGPSBlock(notdone0);
finished1 = UNION done1,done0;

notdone2,done2 = AssignGPSBlock(notdone1);
finished2 = UNION done2,finished1;

notdone3,done3 = AssignGPSBlock(notdone2);
finished3 = UNION done3,finished2;

notdone4,done4 = AssignGPSBlock(notdone3);
finished4 = UNION done4,finished3;

-- notdone5,done5 = AssignGPSBlock(notdone4);
-- finished5 = UNION done5,finished4;
-- 
-- notdone6,done6 = AssignGPSBlock(notdone5);
-- finished6 = UNION done6,finished5;
-- 
-- notdone7,done7 = AssignGPSBlock(notdone6);
-- finished7 = UNION done7, finished6;
-- 
-- notdone8,done8 = AssignGPSBlock(notdone7);
-- finished8 = UNION done8, finished7;

result = UNION notdone4, done4;

STORE result INTO '$output_dir'; --write the result on Disk. $output_dir is a command line argument

