all:

compileUDF:AssignToBlockPig.java ConvertGPStoFlows.java DetermineBlockCoordinates.java
	javac -cp $$PIG_INSTALL/pig-0.10.0.jar -d classes/ AssignToBlockPig.java ConvertGPStoFlows.java DetermineBlockCoordinates.java

#Jar it all up
jarUDF:
	jar -cvf bin/masti4.jar -C classes/ .

grid:grid.pig
	pig -x local -logfile ../logs/ -param output_dir=../results/grid_`date +'%y.%m.%d_%H.%M.%S'` -f grid.pig

stats:grid_stats.pig
	pig -x local -logfile ../logs/ -param output_dir=../results/stats_`date +'%y.%m.%d_%H.%M.%S'` -param input_file=../data/complete_grid.txt -f grid_stats.pig


flows:flows.pig
	# pig -x local -logfile ../logs/ -param input_file=../data/ByDay/Dec17_grid -param output_dir=../results/Dec17_flows_`date +'%y.%m.%d_%H.%M.%S'` -f flows.pig
	# pig -x local -logfile ../logs/ -param input_file=../data/ByDay/Dec18_grid -param output_dir=../results/Dec18_flows_`date +'%y.%m.%d_%H.%M.%S'` -f flows.pig
	# pig -x local -logfile ../logs/ -param input_file=../data/ByDay/Dec19_grid -param output_dir=../results/Dec19_flows_`date +'%y.%m.%d_%H.%M.%S'` -f flows.pig
	# pig -x local -logfile ../logs/ -param input_file=../data/ByDay/Dec20_grid -param output_dir=../results/Dec20_flows_`date +'%y.%m.%d_%H.%M.%S'` -f flows.pig
	# pig -x local -logfile ../logs/ -param input_file=../data/ByDay/Dec21_grid -param output_dir=../results/Dec21_flows_`date +'%y.%m.%d_%H.%M.%S'` -f flows.pig
	# pig -x local -logfile ../logs/ -param input_file=../data/ByDay/Dec24_grid -param output_dir=../results/Dec24_flows_`date +'%y.%m.%d_%H.%M.%S'` -f flows.pig
	# pig -x local -logfile ../logs/ -param input_file=../data/ByDay/Dec25_grid -param output_dir=../results/Dec25_flows_`date +'%y.%m.%d_%H.%M.%S'` -f flows.pig
	# pig -x local -logfile ../logs/ -param input_file=../data/ByDay/Dec26_grid -param output_dir=../results/Dec26_flows_`date +'%y.%m.%d_%H.%M.%S'` -f flows.pig
	# pig -x local -logfile ../logs/ -param input_file=../data/ByDay/Dec27_grid -param output_dir=../results/Dec27_flows_`date +'%y.%m.%d_%H.%M.%S'` -f flows.pig
	# pig -x local -logfile ../logs/ -param input_file=../data/ByDay/Dec28_grid -param output_dir=../results/Dec28_flows_`date +'%y.%m.%d_%H.%M.%S'` -f flows.pig
	# pig -x local -logfile ../logs/ -param input_file=../data/ByDay/Dec31_grid -param output_dir=../results/Dec31_flows_`date +'%y.%m.%d_%H.%M.%S'` -f flows.pig
	# pig -x local -logfile ../logs/ -param input_file=../data/ByDay/Jan01_grid -param output_dir=../results/Jan01_flows_`date +'%y.%m.%d_%H.%M.%S'` -f flows.pig
	# pig -x local -logfile ../logs/ -param input_file=../data/ByDay/Jan02_grid -param output_dir=../results/Jan02_flows_`date +'%y.%m.%d_%H.%M.%S'` -f flows.pig
	# pig -x local -logfile ../logs/ -param input_file=../data/ByDay/Jan03_grid -param output_dir=../results/Jan03_flows_`date +'%y.%m.%d_%H.%M.%S'` -f flows.pig
	# pig -x local -logfile ../logs/ -param input_file=../data/ByDay/Jan04_grid -param output_dir=../results/Jan04_flows_`date +'%y.%m.%d_%H.%M.%S'` -f flows.pig

clean:
	rm -rv classes/* bin/* *.class

test:
	ls -lt
	cd ..
	ls -lt