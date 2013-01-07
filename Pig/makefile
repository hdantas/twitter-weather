all:

compileUDF:AssignToBlockPig.java ConvertGPStoFlows.java
	javac -cp $$PIG_INSTALL/pig-0.10.0.jar -d classes/ AssignToBlockPig.java ConvertGPStoFlows.java

#Jar it all up
jarUDF:
	jar -cvf bin/masti4.jar -C classes/ .

grid1:grid_first_run.pig
	pig -x local -logfile ../logs/ -param output_dir=../results/grid1_`date +'%y.%m.%d_%H.%M.%S'` -f grid_first_run.pig

grid2:grid_second_run.pig
	pig -x local -logfile ../logs/ -param output_dir=../results/grid2_`date +'%y.%m.%d_%H.%M.%S'` -f grid_second_run.pig

stats:grid_stats.pig
	pig -x local -logfile ../logs/ -param output_dir=../results/stats_`date +'%y.%m.%d_%H.%M.%S'` -param input_file=../data/complete_grid.txt -f grid_stats.pig

flows:flows.pig
	pig -x local -logfile ../logs/ -param output_dir=../results/flows_`date +'%y.%m.%d_%H.%M.%S'` -f flows.pig

clean:
	rm -rv classes/* bin/* *.class