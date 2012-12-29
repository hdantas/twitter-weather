all:

compileUDF:convertGridToKML.java convertTweetsToKML.java
	javac -cp $$PIG_INSTALL/pig-0.10.0.jar -d classes/ convertGridToKML.java convertTweetsToKML.java

#Jar it all up
jarUDF:
	jar -cvf bin/masti4.jar -C classes/ .

KML:convertToKML.pig
	pig -x local -logfile ../logs/ -param output_dir=../results/convertToKML_`date +'%y.%m.%d_%H.%M.%S'` -f convertToKML.pig

clean:
	rm -rv classes/* bin/* *.class