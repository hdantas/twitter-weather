grid = LOAD '../data/grid.txt' USING PigStorage('\t') AS (code:chararray,minLat:double,maxLat:double,minLong:double,maxLong:double);
tweets = LOAD '../data/sample_geo_cleaned.txt' USING PigStorage('\t') AS (count:int,userID:int,userName:chararray,messageID:int,date:chararray,gps_lat:double,gps_long:double,source:chararray,tweet:chararray);


REGISTER bin/masti4.jar

-- withsize = FOREACH grid GENERATE SIZE(code) AS size, *;
-- ordered = ORDER withsize BY size, code;
-- result = FOREACH ordered GENERATE masti4.convertGridToKML(code,minLat,maxLat,minLong,maxLong);

shorttweets = FOREACH tweets GENERATE count;
orderedtweets = ORDER shorttweets BY count DESC;
totaltweets = LIMIT orderedtweets 1;

result = FOREACH tweets GENERATE masti4.convertTweetsToKML(count,gps_lat,gps_long,totaltweets.count);

STORE result INTO '$output_dir'; --write the result on Disk. $output_dir is a command line argument
