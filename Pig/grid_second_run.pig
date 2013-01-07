REGISTER bin/masti4.jar;

DEFINE AssignToBlockPig masti4.AssignToBlockPig;
DEFINE getTimeStampPig masti4.getTimeStampPig;

DEFINE AssignGPSBlock (notdone_tweets) RETURNS incomplete_tweets, complete_tweets {

	tweets = FOREACH $notdone_tweets GENERATE count .. tweet, AssignToBlockPig(gps_lat,gps_long,block) AS block;
	
	grouped_tweets = GROUP tweets BY block;
	grids = FOREACH grouped_tweets GENERATE *, COUNT(tweets) AS count_tweets;
	
	incomplete = FILTER grids BY (count_tweets > 100); -- '1' should be replaced by the threshold
	$incomplete_tweets = FOREACH incomplete GENERATE FLATTEN(tweets);
	
	complete = FILTER grids BY (count_tweets <= 100); -- '1' should be replaced by the threshold
	$complete_tweets = FOREACH complete GENERATE FLATTEN(tweets);
};


-- Loads
file = LOAD '../data/grid_first_run.txt' USING PigStorage('\t') AS (count:long,userID:long,userName:chararray,messageID:long,date:chararray,gps_lat:double,gps_long:double,source:chararray,tweet:chararray,block:chararray);

tweets = FILTER file BY (block != 'X');

-- Processing
grouped_tweets = GROUP tweets BY block;
grids = FOREACH grouped_tweets GENERATE *, COUNT(tweets) AS count_tweets;

incomplete = FILTER grids BY (count_tweets > 1); -- '1' should be replaced by the threshold
notdone0 = FOREACH incomplete GENERATE FLATTEN(tweets);

complete = FILTER grids BY (count_tweets <= 1); -- '1' should be replaced by the threshold
done0 = FOREACH complete GENERATE FLATTEN(tweets);

STORE done0 INTO '$output_dir/0';

notdone1,done1 = AssignGPSBlock(notdone0);
STORE done1 INTO '$output_dir/1';

notdone2,done2 = AssignGPSBlock(notdone1);
STORE done2 INTO '$output_dir/2';

notdone3,done3 = AssignGPSBlock(notdone2);
STORE done3 INTO '$output_dir/3';

notdone4,done4 = AssignGPSBlock(notdone3);
STORE done4 INTO '$output_dir/4';

STORE notdone4 INTO '$output_dir/5';

