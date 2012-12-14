-- dont forget to preprocess the twitter dumps to delete any \n or \t that may give problems later

REGISTER bin/masti4.jar;
--REGISTER /home/nuno/Downloads/pig-0.10.0/contrib/piggybank/java/piggybank.jar;

DEFINE AssignToBlockPig masti4.AssignToBlockPig;
DEFINE getTimeStamp masti4.getTimeStamp;

-- Loads
file = LOAD '../data/sample_geo_cleaned.txt' USING PigStorage('\t') AS (count:int,userID:int,userName:chararray,messageID:int,date:chararray,gps_lat:chararray,gps_long:chararray,source:chararray,tweet:chararray);

-- Processing
tweets_blocks = FOREACH file GENERATE count, AssignToBlockPig(gps_lat,gps_long), AssignToBlockPig(gps_lat,gps_long,'1','C');

-- output_dir = CONCAT('../results/',(CONCAT(getTimeStamp(),'tweets_blocks'));

STORE tweets_blocks INTO '../results/tweets_blocks_gps'; --write the result on Disk
