REGISTER bin/masti4.jar;

DEFINE ConvertGPStoFlows masti4.ConvertGPStoFlows;

-- Loads
file = LOAD '../data/sample_grid.txt' USING PigStorage('\t') AS (count:long,userID:long,userName:chararray,messageID:long,date:chararray,gps_lat:double,gps_long:double,source:chararray,tweet:chararray,gpsblock:chararray);

ordered_file = ORDER file BY count; -- order chronologically

-- Processing
grouped_gpsblocks = GROUP ordered_file BY userID;

flows = FOREACH grouped_gpsblocks GENERATE ConvertGPStoFlows(*);

flattened_flows = FOREACH flows GENERATE FLATTEN(bag_of_flowTuples);

grouped_flows = GROUP flattened_flows BY (sourceGPSBlock, destinationGPSBlock);
count_grouped_flows = FOREACH grouped_flows GENERATE COUNT(flattened_flows) AS count, *;
ordered_flows = ORDER count_grouped_flows BY count DESC;
popular_flows = LIMIT ordered_flows 30;
result = popular_flows;

STORE flattened_flows INTO '$output_dir'; --write the result on Disk. $output_dir is a command line argument
-- STORE result INTO '$output_dir'; --write the result on Disk. $output_dir is a command line argument

