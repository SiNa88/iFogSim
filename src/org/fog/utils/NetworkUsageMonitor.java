package org.fog.utils;

public class NetworkUsageMonitor {

	private static double networkUsage = 0.0;
	
	public static void sendingTuple(double Latency, double tupleNwSize){
		//Latency = TransmissionDelay + linkLatency(PropagationDelay)
		//networkUsage += (Latency*tupleNwSize);
		networkUsage += (tupleNwSize);
		//System.out.println(networkUsage);
	}
	
	public static double getNetworkUsage(){
		return networkUsage;
	}
}
