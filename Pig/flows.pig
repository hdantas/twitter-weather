REGISTER bin/masti4.jar;

DEFINE ConvertGPStoFlows masti4.ConvertGPStoFlows;


-- Loads
file = LOAD '../data/data_with_blocks.txt' USING PigStorage('\t') AS (count:int,userID:int, gps_lat:double,gps_long:double,gpsblock:chararray);

-- Processing
grouped_gpsblocks = GROUP file BY userID;

flows = FOREACH grouped_gpsblocks GENERATE ConvertGPStoFlows(*);
-- grouped_flows = GROUP flows BY (source, destination);
-- ordered_flows = ORDER grouped_flows BY COUNT(*) DESC;
-- popular_flows = LIMIT ordered_flows 30;

-- result = popular_flows;

STORE result INTO '$output_dir'; --write the result on Disk. $output_dir is a command line argument

