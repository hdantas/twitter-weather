REGISTER bin/masti4.jar;

DEFINE AssignToBlockPig masti4.AssignToBlockPig;
DEFINE getTimeStampPig masti4.getTimeStampPig;

DEFINE AssignGPSBlock (notdone_tweets) RETURNS incomplete_tweets, complete_tweets {

	tweets = FOREACH $notdone_tweets GENERATE count .. tweet, AssignToBlockPig(gps_lat,gps_long,block) AS block;
	
	grouped_tweets = group tweets BY block;
	grids = FOREACH grouped_tweets {
		users = DISTINCT tweets.userID;
		cnt = COUNT(users);
		GENERATE *, cnt AS count_users;
	};

	
	incomplete = FILTER grids BY (count_users > 100); -- '1' should be replaced by the threshold
	$incomplete_tweets = FOREACH incomplete GENERATE FLATTEN(tweets);
	
	complete = FILTER grids BY (count_users <= 100); -- '1' should be replaced by the threshold
	$complete_tweets = FOREACH complete GENERATE FLATTEN(tweets);
};


-- Loads
file = LOAD '../data/complete.txt' USING PigStorage('\t') AS (count:long,userID:long,userName:chararray,messageID:long,date:chararray,gps_lat:double,gps_long:double,source:chararray,tweet:chararray);

clean_file = FILTER file BY (gps_lat != -1000.0) AND (gps_long != -1000.0);

-- Processing

firstrun = FOREACH clean_file GENERATE *, '' AS block;
notdone0,done0 = AssignGPSBlock(firstrun);
STORE done0 INTO '$output_dir/0';

notdone1,done1 = AssignGPSBlock(notdone0);
STORE done1 INTO '$output_dir/1';

notdone2,done2 = AssignGPSBlock(notdone1);
STORE done2 INTO '$output_dir/2';

notdone3,done3 = AssignGPSBlock(notdone2);
STORE done3 INTO '$output_dir/3';

notdone4,done4 = AssignGPSBlock(notdone3);
STORE done4 INTO '$output_dir/4';

notdone5,done5 = AssignGPSBlock(notdone4);
STORE done5 INTO '$output_dir/5';

notdone6,done6 = AssignGPSBlock(notdone5);
STORE done6 INTO '$output_dir/6';

notdone7,done7 = AssignGPSBlock(notdone6);
STORE done7 INTO '$output_dir/7';

notdone8,done8 = AssignGPSBlock(notdone7);
STORE done8 INTO '$output_dir/8';

notdone9,done9 = AssignGPSBlock(notdone8);
STORE done9 INTO '$output_dir/9';

notdone10,done10 = AssignGPSBlock(notdone9);
STORE done10 INTO '$output_dir/10';

notdone11,done11 = AssignGPSBlock(notdone10);
STORE done11 INTO '$output_dir/11';

notdone12,done12 = AssignGPSBlock(notdone11);
STORE done12 INTO '$output_dir/12';

notdone13,done13 = AssignGPSBlock(notdone12);
STORE done13 INTO '$output_dir/13';

notdone14,done14 = AssignGPSBlock(notdone13);
STORE done14 INTO '$output_dir/14';

notdone15,done15 = AssignGPSBlock(notdone14);
STORE done15 INTO '$output_dir/15';

notdone16,done16 = AssignGPSBlock(notdone15);
STORE done16 INTO '$output_dir/16';

notdone17,done17 = AssignGPSBlock(notdone16);
STORE done17 INTO '$output_dir/17';

notdone18,done18 = AssignGPSBlock(notdone17);
STORE done18 INTO '$output_dir/18';

notdone19,done19 = AssignGPSBlock(notdone18);
STORE done19 INTO '$output_dir/19';

notdone20,done20 = AssignGPSBlock(notdone19);
STORE done20 INTO '$output_dir/20';

STORE notdone20 INTO '$output_dir/21';
