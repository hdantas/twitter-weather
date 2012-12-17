-- dont forget to preprocess the twitter dumps to delete any \n or \t that may give problems later

REGISTER bin/masti4.jar;

-- Loads
file = LOAD '../data/sample.txt' USING PigStorage('\t') AS (count:int,userID:int,userName:chararray,messageID:int,date:chararray,gps_lat:chararray,gps_long:chararray,source:chararray,tweet:chararray);

-- Processing

tweets_blocks = FOREACH file GENERATE count, masti4.AssignToBlockPig(gps_lat,gps_long);

STORE tweets_blocks INTO '../results/tweets_blocks'; --write the result on Disk
