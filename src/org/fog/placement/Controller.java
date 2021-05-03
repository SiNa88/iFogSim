package org.fog.placement;

/*import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;*/
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.fog.application.AppEdge;
import org.fog.application.AppLoop;
import org.fog.application.AppModule;
import org.fog.application.Application;
import org.fog.entities.Actuator;
import org.fog.entities.FogDevice;
import org.fog.entities.Sensor;
import org.fog.utils.Config;
import org.fog.utils.FogEvents;
import org.fog.utils.FogUtils;
import org.fog.utils.NetworkUsageMonitor;
import org.fog.utils.TimeKeeper;

public class Controller extends SimEntity{
	
	public static boolean ONLY_CLOUD = false;
		
	private List<FogDevice> fogDevices;
	private List<Sensor> sensors;
	private List<Actuator> actuators;
	
	private Map<String, Application> applications;
	private Map<String, Integer> appLaunchDelays;

	private Map<String, ModulePlacement> appModulePlacementPolicy;
	
	public Controller(String name, List<FogDevice> fogDevices, List<Sensor> sensors, List<Actuator> actuators) {
		super(name);
		this.applications = new HashMap<String, Application>();
		setAppLaunchDelays(new HashMap<String, Integer>());
		setAppModulePlacementPolicy(new HashMap<String, ModulePlacement>());
		for(FogDevice fogDevice : fogDevices)
		{
			fogDevice.setControllerId(getId());
		}
		setFogDevices(fogDevices);
		setActuators(actuators);
		setSensors(sensors);
		connectWithLatencies();
	}

	private FogDevice getFogDeviceById(int id){
		for(FogDevice fogDevice : getFogDevices()){
			if(id==fogDevice.getId())
				return fogDevice;
		}
		return null;
	}
	
	private void connectWithLatencies(){
		for(FogDevice fogDevice : getFogDevices()){
			FogDevice parent = getFogDeviceById(fogDevice.getParentId());///////////////////////////////////////////////////.getSiblingId());
			if(parent == null)
				continue;
			double latency = fogDevice.getUplinkLatency();
			parent.getChildToLatencyMap().put(fogDevice.getId(), latency);
			parent.getChildrenIds().add(fogDevice.getId());
			/*if(fogDevice.getName() == "d-1") {
				fogDevice.setSiblingId(4);
				FogDevice Dadaaash = getFogDeviceById(4);
				Dadaaash.getSibToLatencyMap().put(fogDevice.getId(), latency);
				//Dadaaash.getChildrenIds().add(fogDevice.getId());
			}*/
		}
	}
	
	@Override
	public void startEntity() {
		for(String appId : applications.keySet()){
			if(getAppLaunchDelays().get(appId)==0)
				processAppSubmit(applications.get(appId));
			else
				send(getId(), getAppLaunchDelays().get(appId), FogEvents.APP_SUBMIT, applications.get(appId));
		}

		send(getId(), Config.RESOURCE_MANAGE_INTERVAL, FogEvents.CONTROLLER_RESOURCE_MANAGE);
		
		send(getId(), Config.MAX_SIMULATION_TIME, FogEvents.STOP_SIMULATION);
		
		for(FogDevice dev : getFogDevices())
			sendNow(dev.getId(), FogEvents.RESOURCE_MGMT);

	}

	@Override
	public void processEvent(SimEvent ev) {
		switch(ev.getTag()){
		case FogEvents.APP_SUBMIT:
			processAppSubmit(ev);
			break;
		case FogEvents.TUPLE_FINISHED:
			processTupleFinished(ev);
			break;
		case FogEvents.CONTROLLER_RESOURCE_MANAGE:
			manageResources();
			break;
		case FogEvents.STOP_SIMULATION:
			CloudSim.stopSimulation();
			printTimeDetails();
			printPowerDetails();
			printCostDetails();
			printCostOfDevices();
			printNetworkUsageDetails();
			System.exit(0);
			break;
			
		}
	}
	
	private void printNetworkUsageDetails() {
		//System.out.println(Config.MAX_SIMULATION_TIME);
		//System.out.println("Total network traffic (KB) = "+round2(NetworkUsageMonitor.getNetworkUsage()/Config.MAX_SIMULATION_TIME/1000,10));
		
		//System.out.println("Total network traffic (MB) = "+round2((NetworkUsageMonitor.getNetworkUsage()/(Calendar.getInstance().getTimeInMillis() - TimeKeeper.getInstance().getSimulationStartTime()))/(1024*1024),10));
		System.out.println("Total network traffic (MB) = "+round2((NetworkUsageMonitor.getNetworkUsage()),10));///(1024*1024),10));

		for(Integer loopId : TimeKeeper.getInstance().getLoopIdToTupleIds().keySet()){
			
			//System.out.println(getStringForLoopId(loopId) + " ---> \n"+);
			//System.out.println("Total network traffic (MB) = "+round2((NetworkUsageMonitor.getNetworkUsage()/(round2(TimeKeeper.getInstance().getLoopIdToCurrentAverage().get(loopId),4)))/(1024*1024),10));
		}
	}

	private void printCostDetails(){
		//System.out.println("Cost of execution in cloud = "+getCloud().getTotalCost());
	}
	private void printCostOfDevices(){
		double sumofCost = 0;
		for(FogDevice dev : getFogDevices()) {
			//if(!dev.getName().equals("cloud"))
			sumofCost+=round2(dev.getTotalCost(),6);
			System.out.println("Cost of Fog device "+ dev.getName() + " = " +(round2(dev.getTotalCost(),6)));		
			
		}
		System.out.println("Cost of Fog devices =          "+ round2(sumofCost,10));
	}
	
	private void printPowerDetails() {
		long sumofEnergy = 0;
		for(FogDevice fogDevice : getFogDevices())
		{
			//if ((!fogDevice.getName().equals("cloud")) || (!fogDevice.getName().equals("proxy-server")))
			{
				System.out.println(fogDevice.getName() + " : Energy Consumed = "+round2(fogDevice.getEnergyConsumption()/1000, 3));
				sumofEnergy += fogDevice.getEnergyConsumption();
			}
		}
		System.out.println("Sum of Energy is:  "+sumofEnergy/1000);
	}

	private static float round2(double number, int decimalPlace) {
		BigDecimal bd = new BigDecimal(number);
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
		return bd.floatValue();
	}

	private String getStringForLoopId(int loopId){
		for(String appId : getApplications().keySet()){
			Application app = getApplications().get(appId);
//			for(AppModule module : app.getModules()){
//				//System.out.println(module.getId());
//				Double startTime = TimeKeeper.getInstance().getEmitTimes().get(module.getId());
//				Double endTime = TimeKeeper.getInstance().getEndTimes().get(module.getId());
//				if(startTime == null || endTime == null) {
//					//System.out.println(":(");
//					break;
//				}
//				System.out.println("--------------------------------------");
//				System.out.println(endTime-startTime);
//				System.out.println("--------------------------------------");
//			}
			for(AppLoop loop : app.getLoops()){
				if(loop.getLoopId() == loopId)
					return loop.getModules().toString();
			}
		}
		return null;
	}
	private void printTimeDetails() {
		/*try(FileWriter fw = new FileWriter("mydata.csv", true); 
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw))
			{
			    out.println();
			} catch (IOException e) {
			    return;//exception handling left as an exercise for the reader
			}*/
		System.out.println("==========================================");
		System.out.println("================= RESULTS ================");
		System.out.println("==========================================");
		//System.out.println();
		//getCloudletFileSize()/FogDevice.getUplinkBandwidth();
		/*tuple.getCloudletFileSize()/getUplinkBandwidth()
		 * +getUplinkLatency()
		 */
		System.out.println("==========================================");
		System.out.println("Simulation Start Time =  "+TimeKeeper.getInstance().getSimulationStartTime());
		System.out.println("Simulation Stop  Time =  "+Calendar.getInstance().getTimeInMillis());//TimeKeeper.getInstance().getCount()+
		System.out.println("EXECUTION TIME (ms) : "+ (Calendar.getInstance().getTimeInMillis() - TimeKeeper.getInstance().getSimulationStartTime())+"\n");//round2(,4)
		System.out.println("==========================================");
		System.out.println("APPLICATION LOOP DELAY (Service time or Response time) :");
		System.out.println("==========================================");
		for(Integer loopId : TimeKeeper.getInstance().getLoopIdToTupleIds().keySet()){
			/*double average = 0, count = 0;
			for(int tupleId : TimeKeeper.getInstance().getLoopIdToTupleIds().get(loopId)){
				Double startTime = 	TimeKeeper.getInstance().getEmitTimes().get(tupleId);
				Double endTime = 	TimeKeeper.getInstance().getEndTimes().get(tupleId);
				if(startTime == null || endTime == null)
					break;
				average += endTime-startTime;
				count += 1;
			}
			System.out.println(getStringForLoopId(loopId) + " ---> "+(average/count));*/
			/** Writing to a file
			 * try(FileWriter fw = new FileWriter("myfile.csv", true); 
				    BufferedWriter bw = new BufferedWriter(fw);
				    PrintWriter out = new PrintWriter(bw))
				{
				    out.println(TimeKeeper.getInstance().getLoopIdToTupleIds());
				} catch (IOException e) {
				    //exception handling left as an exercise for the reader
				}
			 **/
			//System.out.println(TimeKeeper.getInstance().getLoopIdToTupleIds());
			//System.out.println(getStringForLoopId(loopId));
			//System.out.println(TimeKeeper.getInstance().getLoopIdToCurrentAverage().get(loopId));
			System.out.println(getStringForLoopId(loopId) + " ---> \n"+round2(TimeKeeper.getInstance().getLoopIdToCurrentAverage().get(loopId),4));
			//System.out.println(getStringForLoopId(loopId) + " ---> "+(TimeKeeper.getInstance().getLoopIdToCurrentAverage().get(loopId))+"\n");//round2,4
			//System.out.println(loopId+" "+round2(TimeKeeper.getInstance().getLoopIdToCurrentAverage().get(loopId),4)+"\n");
		}
		//for(Integer loopId : TimeKeeper.getInstance().getLoopIdToTupleIds().keySet())
		{
			////////////////////////////System.out.println(TimeKeeper.getInstance().getLoopIdToCurrentNum().get(loopId)+ "\t\t"+ (round2(TimeKeeper.getInstance().getLoopIdToCurrentNum().get(loopId)/TimeKeeper.getInstance().getLoopIdToCurrentAverage().get(loopId),3)*1000));
			//////System.out.println(TimeKeeper.getInstance().getCount()+"\n");
			////System.out.println("==========================================");
			////System.out.println("Current Loop Number is: ");
			////System.out.println(getStringForLoopId(loopId) + " ---> "+TimeKeeper.getInstance().getLoopIdToCurrentNum().get(loopId));
//			for (Integer tupleId : TimeKeeper.getInstance().getLoopIdToTupleIds().get(loopId))
//			{
//				Double startTime = 	TimeKeeper.getInstance().getEmitTimes().get(tupleId);
//				Double endTime = 	TimeKeeper.getInstance().getEndTimes().get(tupleId);
//				if(startTime == null || endTime == null) {
//					System.out.println(":( ");
//					break;
//				}
//				System.out.println("--------------------------------------");
//				System.out.println(endTime-startTime);
//				System.out.println("--------------------------------------");
//			}
		}
		System.out.println("===========================================");
		System.out.println("Number of satisfied requests");
		System.out.println("===========================================");
		for(Integer loopId : TimeKeeper.getInstance().getLoopIdToTupleIds().keySet())
		{
			//System.out.println(getStringForLoopId(loopId) + " ---> "+(round2((TimeKeeper.getInstance().getLoopIdToCurrentNum().get(loopId))/TimeKeeper.getInstance().getLoopIdToCurrentAverage().get(loopId),4)*1000)+"\n");//
		}
		System.out.println("===========================================");
		System.out.println("===========================================");
		System.out.println("Component CPU EXECUTION DELAY");
		System.out.println("===========================================");
		
		for(String tupleType : TimeKeeper.getInstance().getTupleTypeToAverageCpuTime().keySet())
		{
			System.out.println(tupleType + " ---> "+round2(TimeKeeper.getInstance().getTupleTypeToAverageCpuTime().get(tupleType),4));
		}
		System.out.println("===========================================");
		System.out.println("===========================================");
		System.out.println("Component Sending DELAY");
		System.out.println("===========================================");
		
		for(String tupleType : TimeKeeper.getInstance().getTupleTypeToAverageSendTime().keySet())
		{
			System.out.println(tupleType + " ---> "+round2(TimeKeeper.getInstance().getTupleTypeToAverageSendTime().get(tupleType),4));
		}
		System.out.println("===========================================");
		System.out.println("===========================================");
		
	}

	protected void manageResources(){
		send(getId(), Config.RESOURCE_MANAGE_INTERVAL, FogEvents.CONTROLLER_RESOURCE_MANAGE);
	}
	
	private void processTupleFinished(SimEvent ev) {
		//Tuple tuple = (Tuple)ev.getData();
		System.out.println("==========================================");
		System.out.println("==========================================");
		System.out.println("==========================================");
		System.out.println("==========================================");
		System.out.println(CloudSim.clock());
		/*for(String tupleType : TimeKeeper.getInstance().getTupleTypeToAverageSendTime().keySet())
		{
			System.out.println(tupleType + " ---> "+TimeKeeper.getInstance().getTupleTypeToAverageSendTime().get(tupleType));
		}*/
		System.out.println("==========================================");
		System.out.println("==========================================");
		System.out.println("==========================================");
		System.out.println("==========================================");
	}
	
	@Override
	public void shutdownEntity() {	
	}
	
	public void submitApplication(Application application, int delay, ModulePlacement modulePlacement){
		FogUtils.appIdToGeoCoverageMap.put(application.getAppId(), application.getGeoCoverage());
		getApplications().put(application.getAppId(), application);
		getAppLaunchDelays().put(application.getAppId(), delay);
		getAppModulePlacementPolicy().put(application.getAppId(), modulePlacement);
		
		for(Sensor sensor : sensors){
			sensor.setApp(getApplications().get(sensor.getAppId()));
		}
		for(Actuator ac : actuators){
			ac.setApp(getApplications().get(ac.getAppId()));
		}
		;
		//for(AppLoop loop : application.getLoops())
		{
			for(AppEdge edge : application.getEdges())
			{
				//if(edge.getEdgeType() == AppEdge.ACTUATOR)
				{
					String moduleName = edge.getSource();
					//loop.getModules();
					for(Actuator actuator : getActuators()){
						if(actuator.getActuatorType().equalsIgnoreCase(edge.getDestination()))
							application.getModuleByName(moduleName).subscribeActuator(actuator.getId(), edge.getTupleType());
					}
				}
			}	
		}
	}
	
	public void submitApplication(Application application, ModulePlacement modulePlacement){
		submitApplication(application, 0, modulePlacement);
	}
	
	
	private void processAppSubmit(SimEvent ev){
		Application app = (Application) ev.getData();
		processAppSubmit(app);
	}
	
	private void processAppSubmit(Application application){
		///////////System.out.println("At time: "+CloudSim.clock()+", application "+ application.getAppId()+" is submitted.");
		FogUtils.appIdToGeoCoverageMap.put(application.getAppId(), application.getGeoCoverage());
		getApplications().put(application.getAppId(), application);
		
		ModulePlacement modulePlacement = getAppModulePlacementPolicy().get(application.getAppId());
		for(FogDevice fogDevice : fogDevices){
			sendNow(fogDevice.getId(), FogEvents.ACTIVE_APP_UPDATE, application);
		}
		
		Map<Integer, List<AppModule>> deviceToModuleMap = modulePlacement.getDeviceToModuleMap();
		for(Integer deviceId : deviceToModuleMap.keySet()){
			for(AppModule module : deviceToModuleMap.get(deviceId)){
				sendNow(deviceId, FogEvents.APP_SUBMIT, application);
				sendNow(deviceId, FogEvents.LAUNCH_MODULE, module);
			}
		}
	}

	public List<FogDevice> getFogDevices() {
		return fogDevices;
	}

	public void setFogDevices(List<FogDevice> fogDevices) {
		this.fogDevices = fogDevices;
	}

	public Map<String, Integer> getAppLaunchDelays() {
		return appLaunchDelays;
	}

	public void setAppLaunchDelays(Map<String, Integer> appLaunchDelays) {
		this.appLaunchDelays = appLaunchDelays;
	}

	public Map<String, Application> getApplications() {
		return applications;
	}

	public void setApplications(Map<String, Application> applications) {
		this.applications = applications;
	}

	public List<Sensor> getSensors() {
		return sensors;
	}

	public void setSensors(List<Sensor> sensors) {
		for(Sensor sensor : sensors)
			sensor.setControllerId(getId());
		this.sensors = sensors;
	}

	public List<Actuator> getActuators() {
		return actuators;
	}

	public void setActuators(List<Actuator> actuators) {
		this.actuators = actuators;
	}

	public Map<String, ModulePlacement> getAppModulePlacementPolicy() {
		return appModulePlacementPolicy;
	}

	public void setAppModulePlacementPolicy(Map<String, ModulePlacement> appModulePlacementPolicy) {
		this.appModulePlacementPolicy = appModulePlacementPolicy;
	}
}