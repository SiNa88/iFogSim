package org.fog.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.cloudbus.cloudsim.Cloudlet.Resource;
import org.cloudbus.cloudsim.core.CloudSim;
import org.fog.entities.Tuple;

public class TimeKeeper {

	private static TimeKeeper instance;
	
	private long simulationStartTime;
	private int count;
	private Map<Integer, Double>  emitTimes;
	private Map<Integer, Double>  endTimes;
	private Map<Integer, List<Integer>> loopIdToTupleIds;
	private Map<Integer, Double>  tupleIdToCpuStartTime;
	private Map<String , Double>  tupleTypeToAverageCpuTime;
	private Map<String , Integer> tupleTypeToExecutedTupleCount;
	

	private Map<Integer, Double>  tupleIdToSendStartTime;
	private Map<String , Double>  tupleTypeToAverageSendTime;
	private Map<String , Integer> tupleTypeToSentTupleCount;
	
	private Map<Integer, Double> loopIdToCurrentAverage;
	private Map<Integer, Integer> loopIdToCurrentNum;
	
	public static TimeKeeper getInstance(){
		if(instance == null)
			instance = new TimeKeeper();
		return instance;
	}
	
	public int getUniqueId(){
		return count++;
	}
	
	public void tupleStartedExecution(Tuple tuple){
		tupleIdToCpuStartTime.put(tuple.getCloudletId(), CloudSim.clock());
	}
	
	public void tupleEndedExecution(Tuple tuple)
	{
		if(!tupleIdToCpuStartTime.containsKey(tuple.getCloudletId()))
			return;
		double executionTime = CloudSim.clock() - tupleIdToCpuStartTime.get(tuple.getCloudletId());
		//System.out.println(tuple.getCloudletId()+" "+tuple.getSrcModuleName());//" "+(tuple.getActualCPUTime(tuple.getResourceId()))+
		if(!tupleTypeToAverageCpuTime.containsKey(tuple.getTupleType())){
			tupleTypeToAverageCpuTime.put(tuple.getTupleType(), executionTime);
			tupleTypeToExecutedTupleCount.put(tuple.getTupleType(), 1);
		} 
		else
		{
			double currentAverage = tupleTypeToAverageCpuTime.get(tuple.getTupleType());
			int currentCount = tupleTypeToExecutedTupleCount.get(tuple.getTupleType());
			tupleTypeToAverageCpuTime.put(tuple.getTupleType(), (currentAverage*currentCount+executionTime)/(currentCount+1));
			DeviceExeTime(tuple);
		}
		////////System.out.println("NwLength"+tuple.getCloudletFileSize());
		////////System.out.println("CpuLength"+tuple.getCloudletTotalLength());
		//System.out.println(tuple.getDestModuleName()+" "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
		
	}
	
	private void DeviceExeTime(Tuple tuple) 
	{
		int resourceId = tuple.getResourceId();
		
		if (resourceId == 3) {	
			try(FileWriter fw = new FileWriter("mytime-Cloud-"+tuple.getDestModuleName()+ ".csv", true); 
				    BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw))
			{
				//out.println(name+", "+tuple.getDestModuleName()+", "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
				out.println(tupleTypeToAverageCpuTime.get(tuple.getTupleType())); 
			} catch (IOException e) {
				return;//exception handling left as an exercise for the reader
			}
		}
		else if (resourceId == 4) 
		{	try(FileWriter fw = new FileWriter("mytime-Proxy-"+tuple.getDestModuleName()+ ".csv", true); 
				    BufferedWriter bw = new BufferedWriter(fw);
				    PrintWriter out = new PrintWriter(bw))
			{
				//out.println(name+", "+tuple.getDestModuleName()+", "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
				out.println(tupleTypeToAverageCpuTime.get(tuple.getTupleType())); 
			} catch (IOException e) {
				return;//exception handling left as an exercise for the reader
			}
		}
		else if (resourceId == 5) 
		{	
			try(FileWriter fw = new FileWriter("mytime-d-0-"+tuple.getDestModuleName()+ ".csv", true); 
				    BufferedWriter bw = new BufferedWriter(fw);
				    PrintWriter out = new PrintWriter(bw))
			{
				//out.println(name+", "+tuple.getDestModuleName()+", "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
				out.println(tupleTypeToAverageCpuTime.get(tuple.getTupleType())); 
			} catch (IOException e) {
				return;//exception handling left as an exercise for the reader
			}
		}
		else if (resourceId == 6) 
		{	
			try(FileWriter fw = new FileWriter("mytime-m-0-0-"+tuple.getDestModuleName()+ ".csv", true); 
				    BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw))
			{
				//out.println(name+", "+tuple.getDestModuleName()+", "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
				out.println(tupleTypeToAverageCpuTime.get(tuple.getTupleType())); 
			} catch (IOException e) {
				return;//exception handling left as an exercise for the reader
			}
		}
		else if (resourceId == 9) 
		{	try(FileWriter fw = new FileWriter("mytime-m-0-1-"+tuple.getDestModuleName()+ ".csv", true); 
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw))
			{
				//out.println(name+", "+tuple.getDestModuleName()+", "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
				out.println(tupleTypeToAverageCpuTime.get(tuple.getTupleType())); 
			} catch (IOException e) {
			    return;//exception handling left as an exercise for the reader
			}
		}
		else if (resourceId == 12) 
		{	try(FileWriter fw = new FileWriter("mytime-m-0-2-"+tuple.getDestModuleName()+ ".csv", true); 
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw))
			{
				//out.println(name+", "+tuple.getDestModuleName()+", "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
				out.println(tupleTypeToAverageCpuTime.get(tuple.getTupleType())); 
			} catch (IOException e) {
			    return;//exception handling left as an exercise for the reader
			}
		}
		else if (resourceId == 15) 
		{	
			try(FileWriter fw = new FileWriter("mytime-m-0-3-"+tuple.getDestModuleName()+ ".csv", true); 
				    BufferedWriter bw = new BufferedWriter(fw);
				    PrintWriter out = new PrintWriter(bw))
			{
				//out.println(name+", "+tuple.getDestModuleName()+", "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
				out.println(tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
			} catch (IOException e) {
				return;//exception handling left as an exercise for the reader
			}
		}
		else if (resourceId == 18) 
		{	
			try(FileWriter fw = new FileWriter("mytime-m-0-4-"+tuple.getDestModuleName()+ ".csv", true); 
				    BufferedWriter bw = new BufferedWriter(fw);
				    PrintWriter out = new PrintWriter(bw))
			{
				//out.println(name+", "+tuple.getDestModuleName()+", "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
				out.println(tupleTypeToAverageCpuTime.get(tuple.getTupleType())); 
			} catch (IOException e) {
				return;//exception handling left as an exercise for the reader
			}
		}
		else if (resourceId == 21) 
		{	
			try(FileWriter fw = new FileWriter("mytime-m-0-5-"+tuple.getDestModuleName()+ ".csv", true); 
				    BufferedWriter bw = new BufferedWriter(fw);
				    PrintWriter out = new PrintWriter(bw))
			{
				//out.println(name+", "+tuple.getDestModuleName()+", "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
				out.println(tupleTypeToAverageCpuTime.get(tuple.getTupleType())); 
			} catch (IOException e) {
				return;//exception handling left as an exercise for the reader
			}
		}
		else if (resourceId == 24) 
		{	
			try(FileWriter fw = new FileWriter("mytime-d-1-"+tuple.getDestModuleName()+ ".csv", true); 
				    BufferedWriter bw = new BufferedWriter(fw);
				    PrintWriter out = new PrintWriter(bw))
			{
				//out.println(name+", "+tuple.getDestModuleName()+", "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
				out.println(tupleTypeToAverageCpuTime.get(tuple.getTupleType())); 
			} catch (IOException e) {
				return;//exception handling left as an exercise for the reader
			}
		}
		else if (resourceId == 25) 
		{	
			try(FileWriter fw = new FileWriter("mytime-m-1-0-"+tuple.getDestModuleName()+ ".csv", true); 
				    BufferedWriter bw = new BufferedWriter(fw);
				    PrintWriter out = new PrintWriter(bw))
			{
				//out.println(name+", "+tuple.getDestModuleName()+", "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
				out.println(tupleTypeToAverageCpuTime.get(tuple.getTupleType())); 
			} catch (IOException e) {
				return;//exception handling left as an exercise for the reader
			}
		}
		else if (resourceId == 28) 
		{	
			try(FileWriter fw = new FileWriter("mytime-m-1-1-"+tuple.getDestModuleName()+ ".csv", true); 
				    BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw))
			{
				//out.println(name+", "+tuple.getDestModuleName()+", "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
				out.println(tupleTypeToAverageCpuTime.get(tuple.getTupleType())); 
			} catch (IOException e) {
				return;//exception handling left as an exercise for the reader
			}
		}
		else if (resourceId == 31) 
		{	
			try(FileWriter fw = new FileWriter("mytime-m-1-2-"+tuple.getDestModuleName()+ ".csv", true); 
				    BufferedWriter bw = new BufferedWriter(fw);
				    PrintWriter out = new PrintWriter(bw))
			{
				//out.println(name+", "+tuple.getDestModuleName()+", "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
				out.println(tupleTypeToAverageCpuTime.get(tuple.getTupleType())); 
			} catch (IOException e) {
				return;//exception handling left as an exercise for the reader
			}
		}
		else if (resourceId == 34) 
		{	
			try(FileWriter fw = new FileWriter("mytime-m-1-3-"+tuple.getDestModuleName()+ ".csv", true); 
				    BufferedWriter bw = new BufferedWriter(fw);
				    PrintWriter out = new PrintWriter(bw))
			{
				//out.println(name+", "+tuple.getDestModuleName()+", "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
				out.println(tupleTypeToAverageCpuTime.get(tuple.getTupleType())); 
			} catch (IOException e) {
				return;//exception handling left as an exercise for the reader
			}
		}
		else if (resourceId == 37) 
		{	
			try(FileWriter fw = new FileWriter("mytime-m-1-4-"+tuple.getDestModuleName()+ ".csv", true); 
				    BufferedWriter bw = new BufferedWriter(fw);
				    PrintWriter out = new PrintWriter(bw))
			{
				//out.println(name+", "+tuple.getDestModuleName()+", "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
				out.println(tupleTypeToAverageCpuTime.get(tuple.getTupleType())); 
			} catch (IOException e) {
				return;//exception handling left as an exercise for the reader
			}
		}
		else if (resourceId == 40) 
		{	
			try(FileWriter fw = new FileWriter("mytime-m-1-5-"+tuple.getDestModuleName()+ ".csv", true); 
				    BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw))
			{
				//out.println(name+", "+tuple.getDestModuleName()+", "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
				out.println(tupleTypeToAverageCpuTime.get(tuple.getTupleType())); 
			} catch (IOException e) {
				return;//exception handling left as an exercise for the reader
			}
		}
		else if (resourceId == 43) 
		{	
			try(FileWriter fw = new FileWriter("mytime-d-2-"+tuple.getDestModuleName()+ ".csv", true); 
					BufferedWriter bw = new BufferedWriter(fw);
				    PrintWriter out = new PrintWriter(bw))
			{
				//out.println(name+", "+tuple.getDestModuleName()+", "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
				out.println(tupleTypeToAverageCpuTime.get(tuple.getTupleType())); 
			} catch (IOException e) {
				return;//exception handling left as an exercise for the reader
			}
		}
		else if (resourceId == 44) 
		{	
			try(FileWriter fw = new FileWriter("mytime-m-2-0-"+tuple.getDestModuleName()+ ".csv", true); 
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw))
			{
				//out.println(name+", "+tuple.getDestModuleName()+", "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
				out.println(tupleTypeToAverageCpuTime.get(tuple.getTupleType())); 
			} catch (IOException e) {
				return;//exception handling left as an exercise for the reader
			}
		}
		else if (resourceId == 47) 
		{	
			try(FileWriter fw = new FileWriter("mytime-m-2-1-"+tuple.getDestModuleName()+ ".csv", true); 
				    BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw))
			{
				//out.println(name+", "+tuple.getDestModuleName()+", "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
				out.println(tupleTypeToAverageCpuTime.get(tuple.getTupleType())); 
			} catch (IOException e) {
				return;//exception handling left as an exercise for the reader
			}
			
		}
		else if (resourceId == 50) 
		{	
			try(FileWriter fw = new FileWriter("mytime-m-2-2-"+tuple.getDestModuleName()+ ".csv", true); 
				    BufferedWriter bw = new BufferedWriter(fw);
				    PrintWriter out = new PrintWriter(bw))
			{
				//out.println(name+", "+tuple.getDestModuleName()+", "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
				out.println(tupleTypeToAverageCpuTime.get(tuple.getTupleType())); 
			} catch (IOException e) {
				return;//exception handling left as an exercise for the reader
			}			
		}
		else if (resourceId == 53) 
		{	
			try(FileWriter fw = new FileWriter("mytime-m-2-3-"+tuple.getDestModuleName()+ ".csv", true); 
				    BufferedWriter bw = new BufferedWriter(fw);
				    PrintWriter out = new PrintWriter(bw))
			{
				//out.println(name+", "+tuple.getDestModuleName()+", "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
				out.println(tupleTypeToAverageCpuTime.get(tuple.getTupleType())); 
			} catch (IOException e) {
					return;//exception handling left as an exercise for the reader
			}
			
		}
		else if (resourceId == 56) 
		{	
			try(FileWriter fw = new FileWriter("mytime-m-2-4-"+tuple.getDestModuleName()+ ".csv", true); 
					BufferedWriter bw = new BufferedWriter(fw);
				    PrintWriter out = new PrintWriter(bw))
			{
				//out.println(name+", "+tuple.getDestModuleName()+", "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
				out.println(tupleTypeToAverageCpuTime.get(tuple.getTupleType())); 
			} catch (IOException e) {
				return;//exception handling left as an exercise for the reader
			}			
		}
		else if (resourceId == 59) 
		{	
			try(FileWriter fw = new FileWriter("mytime-m-2-5-"+tuple.getDestModuleName()+ ".csv", true); 
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw))
			{
				//out.println(name+", "+tuple.getDestModuleName()+", "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
				out.println(tupleTypeToAverageCpuTime.get(tuple.getTupleType())); 
			} catch (IOException e) {
				return;//exception handling left as an exercise for the reader
			}
		}
		else if (resourceId == 62) 
		{	
			try(FileWriter fw = new FileWriter("mytime-d-3-"+tuple.getDestModuleName()+ ".csv", true); 
				    BufferedWriter bw = new BufferedWriter(fw);
				    PrintWriter out = new PrintWriter(bw))
			{
				//out.println(name+", "+tuple.getDestModuleName()+", "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
				out.println(tupleTypeToAverageCpuTime.get(tuple.getTupleType())); 
			} catch (IOException e) {
				return;//exception handling left as an exercise for the reader
			}
		}
		else if (resourceId == 63) 
		{	
			try(FileWriter fw = new FileWriter("mytime-m-3-0-"+tuple.getDestModuleName()+".csv", true); 
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw))
			{
				//out.println(name+", "+tuple.getDestModuleName()+", "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
				out.println(tupleTypeToAverageCpuTime.get(tuple.getTupleType())); 
			} catch (IOException e) {
				return;//exception handling left as an exercise for the reader
			}			
		}
		else if (resourceId == 66) 
		{	
			try(FileWriter fw = new FileWriter("mytime-m-3-1-"+tuple.getDestModuleName()+".csv", true); 
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw))
			{
				//out.println(name+", "+tuple.getDestModuleName()+", "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
				out.println(tupleTypeToAverageCpuTime.get(tuple.getTupleType())); 
			} catch (IOException e) {
				return;//exception handling left as an exercise for the reader
			}
		}
		else if (resourceId == 69) 
		{	
			try(FileWriter fw = new FileWriter("mytime-m-3-2-"+tuple.getDestModuleName()+".csv", true); 
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw))
			{
				//out.println(name+", "+tuple.getDestModuleName()+", "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
				out.println(tupleTypeToAverageCpuTime.get(tuple.getTupleType())); 
			} catch (IOException e) {
				return;//exception handling left as an exercise for the reader
			}
		}
		else if (resourceId == 72) 
		{	
			try(FileWriter fw = new FileWriter("mytime-m-3-3-"+tuple.getDestModuleName()+".csv", true); 
				    BufferedWriter bw = new BufferedWriter(fw);
				    PrintWriter out = new PrintWriter(bw))
			{
				//out.println(name+", "+tuple.getDestModuleName()+", "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
				out.println(tupleTypeToAverageCpuTime.get(tuple.getTupleType())); 
			} catch (IOException e) {
				return;//exception handling left as an exercise for the reader
			}
		}
		else if (resourceId == 75) 
		{	
			try(FileWriter fw = new FileWriter("mytime-m-3-4-"+tuple.getDestModuleName()+".csv", true); 
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw))
			{
				//out.println(name+", "+tuple.getDestModuleName()+", "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
				out.println(tupleTypeToAverageCpuTime.get(tuple.getTupleType())); 
			} catch (IOException e) {
				return;//exception handling left as an exercise for the reader
			}
			
		}
		else if (resourceId == 78) 
		{	
			try(FileWriter fw = new FileWriter("mytime-m-3-5-"+tuple.getDestModuleName()+".csv", true); 
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw))
			{
				//out.println(name+", "+tuple.getDestModuleName()+", "+tupleTypeToAverageCpuTime.get(tuple.getTupleType()));
				out.println(tupleTypeToAverageCpuTime.get(tuple.getTupleType())); 
			} catch (IOException e) {
				return;//exception handling left as an exercise for the reader
			}
		}
		
	}

	public void tupleStartedSending(Tuple tuple){
		//if (tuple.get() == "EEG")
			//System.out.println(tuple.getDestModuleName());
		//if (tuple.getDestModuleName())
		//tuple.getDirection(), tupleIdToSendStartTime.put()
		tupleIdToSendStartTime.put(tuple.getCloudletId(), CloudSim.clock());
	}
	
	public void tupleEndedSending(Tuple tuple){
		if(!tupleIdToSendStartTime.containsKey(tuple.getCloudletId()))
			return;
		double SendingTime = CloudSim.clock() - tupleIdToSendStartTime.get(tuple.getCloudletId());
		/** Writing to a file **/
		/*try(FileWriter fw = new FileWriter("count.csv", true); 
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw))
			{
			    out.println("1");
			} catch (IOException e) {
			    return;//exception handling left as an exercise for the reader
			}*/
		//System.out.println(tuple.getCloudletId()+" "+" "+tuple.getSrcModuleName()+" "+tuple.getDestModuleName()+" "+SendingTime);//" "+(tuple.getActualCPUTime(tuple.getResourceId()))+
		if(!tupleTypeToAverageSendTime.containsKey(tuple.getTupleType()))
		{
			tupleTypeToAverageSendTime.put(tuple.getTupleType(), SendingTime);
			tupleTypeToSentTupleCount.put(tuple.getTupleType(), 1);
			///////////////System.out.println(tupleTypeToAverageSendTime.containsKey(tuple.getTupleType()));
		}
		else
		{
			//////////////System.out.println(tupleTypeToAverageSendTime.containsKey(tuple.getTupleType()));
			double currentAverage = tupleTypeToAverageSendTime.get(tuple.getTupleType());
			int currentCount      = tupleTypeToSentTupleCount.get(tuple.getTupleType());
			/////////////System.out.println(currentAverage);
			tupleTypeToAverageSendTime.put(tuple.getTupleType(), (currentAverage*currentCount+SendingTime)/(currentCount+1));
			/////////////System.out.println(currentCount);
		}
	}
	
	public Map<Integer, List<Integer>> loopIdToTupleIds(){
		return getInstance().getLoopIdToTupleIds();
	}
	
	private TimeKeeper(){
		count = 1;
		setEmitTimes(new HashMap<Integer, Double>());
		setEndTimes(new HashMap<Integer, Double>());
		setLoopIdToTupleIds(new HashMap<Integer, List<Integer>>());
		setTupleTypeToAverageCpuTime(new HashMap<String, Double>());
		setTupleTypeToExecutedTupleCount(new HashMap<String, Integer>());
		setTupleIdToCpuStartTime(new HashMap<Integer, Double>());
		setTupleTypeToAverageSendTime(new HashMap<String, Double>());
		setTupleTypeToSentTupleCount(new HashMap<String, Integer>());
		setTupleIdToSendStartTime(new HashMap<Integer, Double>());
		setLoopIdToCurrentAverage(new HashMap<Integer, Double>());
		setLoopIdToCurrentNum(new HashMap<Integer, Integer>());
	}
	
	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Map<Integer, Double> getEmitTimes() {
		return emitTimes;
	}

	public void setEmitTimes(Map<Integer, Double> emitTimes) {
		this.emitTimes = emitTimes;
	}

	public Map<Integer, Double> getEndTimes() {
		return endTimes;
	}

	public void setEndTimes(Map<Integer, Double> endTimes) {
		this.endTimes = endTimes;
	}

	public Map<Integer, List<Integer>> getLoopIdToTupleIds() {
		return loopIdToTupleIds;
	}

	public void setLoopIdToTupleIds(Map<Integer, List<Integer>> loopIdToTupleIds) {
		this.loopIdToTupleIds = loopIdToTupleIds;
	}

	public Map<String, Double> getTupleTypeToAverageSendTime() {
		return tupleTypeToAverageSendTime;
	}

	public void setTupleTypeToAverageSendTime(
			Map<String, Double> tupleTypeToAverageSendTime) {
		this.tupleTypeToAverageSendTime = tupleTypeToAverageSendTime;
	}

	public Map<String, Integer> getTupleTypeToSentTupleCount() {
		return tupleTypeToSentTupleCount;
	}

	public void setTupleTypeToSentTupleCount(
			Map<String, Integer> tupleTypeToSentTupleCount) {
		this.tupleTypeToSentTupleCount = tupleTypeToSentTupleCount;
	}

	public Map<Integer, Double> getTupleIdToSendStartTime() {
		return tupleIdToSendStartTime;
	}

	public void setTupleIdToSendStartTime(Map<Integer, Double> tupleIdToSendStartTime) {
		this.tupleIdToSendStartTime = tupleIdToSendStartTime;
	}

	public Map<String, Double> getTupleTypeToAverageCpuTime() {
		return tupleTypeToAverageCpuTime;
	}

	public void setTupleTypeToAverageCpuTime(
			Map<String, Double> tupleTypeToAverageCpuTime) {
		this.tupleTypeToAverageCpuTime = tupleTypeToAverageCpuTime;
		System.out.println(tupleTypeToAverageCpuTime);
	}

	public Map<String, Integer> getTupleTypeToExecutedTupleCount() {
		return tupleTypeToExecutedTupleCount;
	}

	public void setTupleTypeToExecutedTupleCount(
			Map<String, Integer> tupleTypeToExecutedTupleCount) {
		this.tupleTypeToExecutedTupleCount = tupleTypeToExecutedTupleCount;
	}

	public Map<Integer, Double> getTupleIdToCpuStartTime() {
		return tupleIdToCpuStartTime;
	}

	public void setTupleIdToCpuStartTime(Map<Integer, Double> tupleIdToCpuStartTime) {
		this.tupleIdToCpuStartTime = tupleIdToCpuStartTime;
	}
	
	public long getSimulationStartTime() {
		return simulationStartTime;
	}

	public void setSimulationStartTime(long simulationStartTime) {
		this.simulationStartTime = simulationStartTime;
	}

	public Map<Integer, Double> getLoopIdToCurrentAverage() {
		return loopIdToCurrentAverage;
	}

	public void setLoopIdToCurrentAverage(Map<Integer, Double> loopIdToCurrentAverage) {
		this.loopIdToCurrentAverage = loopIdToCurrentAverage;
	}

	public Map<Integer, Integer> getLoopIdToCurrentNum() {
		return loopIdToCurrentNum;
	}

	public void setLoopIdToCurrentNum(Map<Integer, Integer> loopIdToCurrentNum) {
		/** Writing to a file **/
		/*try(FileWriter fw = new FileWriter("count.csv", true); 
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw))
			{
			    out.println(loopIdToCurrentNum.keySet());
			} catch (IOException e) {
			    return;
			}*/
		this.loopIdToCurrentNum = loopIdToCurrentNum;
	}
}
