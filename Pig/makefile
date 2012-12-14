all:run

compileUDF:AssignToBlockPig.java getTimeStamp.java
	javac -cp $$PIG_INSTALL/pig-0.10.0.jar -d classes/ AssignToBlockPig.java getTimeStamp.java

#Jar it all up
jarUDF:
	jar -cvf bin/masti4.jar -C classes/ .

run:tweets.pig
	pig -x local -f tweets.pig

clean:
	rm -rv classes/*
	rm -rv bin/*
		
cleanPIG:
	rm -r ../results/tweets_blocks_gps
