all:

compileUDF:AssignToBlockPig.java ConvertGPStoFlows.java
	javac -cp $$PIG_INSTALL/pig-0.10.0.jar -d classes/ AssignToBlockPig.java ConvertGPStoFlows.java

#Jar it all up
jarUDF:
	jar -cvf bin/masti4.jar -C classes/ .

grid:grid.pig
	pig -x local -logfile ../logs/ -param output_dir=../results/grid_`date +'%y.%m.%d_%H.%M.%S'` -f grid.pig

flows:flows.pig
	pig -x local -logfile ../logs/ -param output_dir=../results/flows_`date +'%y.%m.%d_%H.%M.%S'` -f flows.pig

clean:
	rm -rv classes/* bin/* *.class