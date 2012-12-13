-- dont forget to preprocess the twitter dumps to delete any \n or \t that may give problems later

-- Loads
file = LOAD 'sample.txt' USING PigStorage('\t') AS (count:int,userID:int,userName:chararray,messageID:int,date:chararray,gps:chararray,source:cahararray,tweet:chararray);

-- Processing
tweets = FOREACH file GENERATE count, userID;
grouped_tweets = GROUP tweets BY userID;
flatten_tweets = FOREACH grouped_tweets GENERATE group, COUNT(tweets.count);

sorted_tweets = ORDER flatten_tweets BY $1 DESC; --sort 
STORE sorted_tweets INTO 'big_tweets'; --write the result on Disk

