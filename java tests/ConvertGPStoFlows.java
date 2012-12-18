public class ConvertGPStoFlows {
	public static void main (String[] args) {
		
		int userID = 1;
	
		int sourceCount = 2;
		int destinationCount = 3;
		
		double sourceLatitude = 4.0;
		double sourceLongitude = 5.0;
		
		double destinationLatitude = 6.0;
		double destinationLongitude = 7.0;
		
		String sourceGPSBlock = "eight";
		String destinationGPSBlock = "nine";


		String outputString = String.format("userID:%d, sourceGPSBlock:%s, destinationGPSBlock:%s, sourceCount:%d, destinationCount:%d%n",
			userID,sourceGPSBlock,destinationGPSBlock,sourceCount,destinationCount);
		System.out.println(outputString);
		
	}
}