-- pw.println(count+ ","+ t.user.screen_name + "," + t.text + "," +t.user.location + "," + t.geo + "," +t.coordinates);

-- Loads
file = LOAD 'sample.txt' USING PigStorage('\t') AS (count:int,userID:chararray,tweet:chararray,place:chararray,gps1:chararray,gps2:chararray);

-- Processing
tweets = FOREACH file GENERATE count, userID;
grouped_tweets = GROUP tweets BY userID;
flatten_tweets = FOREACH grouped_tweets GENERATE group, COUNT(tweets.count);

sorted_tweets = ORDER flatten_tweets BY $1 DESC; --sort 
STORE sorted_tweets INTO 'big_tweets'; --write the result on Disk

