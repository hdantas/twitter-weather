all:run

compileUDF:AssignToBlockPig.java getTimeStampPig.java
	javac -cp $$PIG_INSTALL/pig-0.10.0.jar -d classes/ AssignToBlockPig.java getTimeStampPig.java

#Jar it all up
jarUDF:
	jar -cvf bin/masti4.jar -C classes/ .

run:tweets.pig
	pig -x local -logfile ../logs/ -param output_dir=../results/tweets_blocks_`date +'%y.%m.%d_%H.%M.%S'` -f tweets.pig

clean:
	rm -rv classes/* bin/* *.class

