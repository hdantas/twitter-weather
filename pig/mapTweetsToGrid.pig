-- dont forget to preprocess the twitter dumps to delete any \n or \t that may give problems later
REGISTER bin/myUDF.jar;

-- Loads
file = LOAD '../sample.txt' USING PigStorage('\t') AS (count:int,userID:int,userName:chararray,messageID:int,date:chararray,gps:chararray,source:cahararray,tweet:chararray);

-- Processing
tweets = FOREACH file GENERATE file, myUDF.mapToGrid(gps);

sorted_tweets = ORDER tweets BY count ASC; --sort 
STORE sorted_tweets INTO ../results/mapped_tweets; --write the result on Disk

