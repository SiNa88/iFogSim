package org.fog.test.perfeval;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.PowerHost;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.sdn.overbooking.BwProvisionerOverbooking;
import org.cloudbus.cloudsim.sdn.overbooking.PeProvisionerOverbooking;
import org.fog.application.AppEdge;
import org.fog.application.AppLoop;
import org.fog.application.Application;
import org.fog.application.selectivity.FractionalSelectivity;
import org.fog.entities.Actuator;
import org.fog.entities.FogBroker;
import org.fog.entities.FogDevice;
import org.fog.entities.FogDeviceCharacteristics;
import org.fog.entities.Sensor;
import org.fog.entities.Tuple;
import org.fog.placement.Controller;
import org.fog.placement.ModuleMapping;
import org.fog.placement.ModulePlacementEdgewards;
import org.fog.placement.ModulePlacementMapping;
import org.fog.policy.AppModuleAllocationPolicy;
import org.fog.scheduler.StreamOperatorScheduler;
import org.fog.utils.FogLinearPowerModel;
import org.fog.utils.FogUtils;
import org.fog.utils.TimeKeeper;
import org.fog.utils.distribution.DeterministicDistribution;


public class VRGameFogMatching2 {
	static List<FogDevice> fogDevices = new ArrayList<FogDevice>();
	static List<Sensor> sensors = new ArrayList<Sensor>();
	static List<Actuator> actuators = new ArrayList<Actuator>();
	//static List<Sensor> sensors1 = new ArrayList<Sensor>();
	//static List<Actuator> actuators1 = new ArrayList<Actuator>();	
	static List<FogDevice> edges = new ArrayList<FogDevice>();

	  static int ar=-83616,  ra=-109646, qr=-9378;
//	  static int ar=173507,  ra=207230, qr=-113108;
//	  static int ar=207230,  ra=109646, qr=-362064;
//	  static int ar=-50194,  ra=-9378,  qr=83616;
//	  static int ar=-50194,  ra=-72308, qr=-52010;
//	  static int ar=-50194,  ra=-50194, qr=-50194;
//	  static int ar=173507,  ra=173507, qr=207230;
//	  static int ar=52010,   ra=-50194, qr=-72308;
//	  static int ar=-113108, ra=-109646, qr=-9378;
//	  static int ar=-362064, ra=207230,  qr=109646;
	 
	static boolean CLOUD = true;

	static int numOfDepts = 1;
	static int numOfedgesPerDept = 10;
	//static int numOfCamerasPerArea = 4;
	static double TRANSMISSION_TIME = 5.1 ; // 0.9 // 2.1//3.15
    //static Random randomno = new Random();
	public static void main(String[] args) {
		/**
		 * 			Seeds based on Normal Distribution
		 * [ 593.58531984 -241.84330379   31.45307944 -594.87910665  107.09611314
 		 *	-504.28323029  619.18495666   74.64754262  998.1101853   457.71620458]
 		 *
 		 * [-362064, -50194, 173507, -72308, 52010
 		 *  -83616, -113108, -9378, -109646, 207230]
 		 *  
		 */
		//random(173507);
		 // create random object
	      //randomno.nextGaussian();
	      // setting seed
	      //randomno.setSeed(50194);

//		File file = new File("mydata.csv");
//        
//        if(file.delete())
//        {
//            System.out.println("File deleted successfully");
//        }
//        else
//        {
//            System.out.println("Failed to delete the file");
//        }
		Log.printLine("Starting Applications...");

		try {
			Log.disable();
			int num_user = 1; // number of cloud users
			Calendar calendar = Calendar.getInstance();
			boolean trace_flag = false; // mean trace events

			CloudSim.init(num_user, calendar, trace_flag);


			String appId_vs = "vs";
			
			FogBroker broker0 = new FogBroker("broker_0");
			//FogBroker broker1 = new FogBroker("broker_1");			
			

			//Application application_ar = createApplicationVideoStreamAnalysis(appId_ar, broker0.getId());
			//Application application_ra = createApplicationRobotAssistant(appId_ra, broker0.getId());
			//Application application_qr = createApplicationQueryRetrieval(appId_qr, broker0.getId());
			Application application_vs = createApplicationVideoStreamAnalysis   (appId_vs, broker0.getId());
			
			
			//application_ar.setUserId		(broker0.getId());
			//application_ra.setUserId		(broker0.getId());
			//application_qr.setUserId		(broker0.getId());
			application_vs.setUserId		(broker0.getId());
			
			createFogDevices(broker0.getId(), appId_vs, appId_vs, appId_vs);
			
			ModuleMapping moduleMapping_0 = ModuleMapping.createModuleMapping(); // initializing a module mapping
			
			
			if(CLOUD)
			{
				

				/**
				 * Matching
				 */
				/*
				 * moduleMapping_0.addModuleToDevice("transcoding", "tier-server2");
				 * moduleMapping_0.addModuleToDevice("permanent_storing", "tier-server1");
				 * moduleMapping_0.addModuleToDevice("detecting_classifying", "tier-server2");
				 * moduleMapping_0.addModuleToDevice("high_quali_encod", "e-0-7");
				 * moduleMapping_0.addModuleToDevice("lightCNN", "tier-server2");
				 * moduleMapping_0.addModuleToDevice("packaging", "tier-server1");
				 */

				/**
				 * RTR-RP
				 */				
				
				  moduleMapping_0.addModuleToDevice("transcoding", "cloud");
				  moduleMapping_0.addModuleToDevice("permanent_storing", "cloud");
				  moduleMapping_0.addModuleToDevice("detecting_classifying", "tier-server2");
				  moduleMapping_0.addModuleToDevice("high_quali_encod", "e-0-7");
				  moduleMapping_0.addModuleToDevice("lightCNN", "cloud");
				  moduleMapping_0.addModuleToDevice("packaging", "cloud");
				
			}
			
			
			//2020.09.1///////////for(int index = 0 ; index < fogDevices.size();index++)
			//2020.09.1///////////	System.out.println(index+"   "+fogDevices.get(index).getName()+"   "+fogDevices.get(index).getId());
			
			Controller controller = new Controller("master-controller", fogDevices, sensors, actuators);
			
						
			//controller.submitApplication(application_vs ,// 1000,
				//	(CLOUD)?(new ModulePlacementMapping(fogDevices, application_vs, moduleMapping_0))
							//(new ModulePlacementMapo(fogDevices, sensors, actuators,  application_ar, moduleMapping_0))
					//		:(new ModulePlacementEdgewards(fogDevices, sensors, actuators, application_vs, moduleMapping_0)));
			controller.submitApplication(application_vs ,(new ModulePlacementEdgewards(fogDevices,  sensors, actuators, application_vs, moduleMapping_0)));

			
			/*for(int index = 0 ; index < fogDevices.size();index++)
			{
				if (index != 0 && index != 1 && index != 2 && index != 9 && index != 16 && index != 23 && index+1 != fogDevices.size())
				{
					fogDevices.get(index).setParentId(fogDevices.get(index+1).getId());
					fogDevices.get(index+1).setParentId(fogDevices.get(index).getId());
					//System.out.println(index);
				}
			}
			fogDevices.get(2).setParentId(fogDevices.get(9).getId());
			fogDevices.get(9).setParentId(fogDevices.get(16).getId());
			fogDevices.get(16).setParentId(fogDevices.get(23).getId());
			fogDevices.get(23).setParentId(fogDevices.get(2).getId());*/
			
			TimeKeeper.getInstance().setSimulationStartTime(Calendar.getInstance().getTimeInMillis());
			System.out.println("\nSimulation Start Time = "+Calendar.getInstance().getTimeInMillis());

			CloudSim.startSimulation();

			CloudSim.stopSimulation();
			
			Log.printLine("Applications are finished!");
		} catch (Exception e) {
			e.printStackTrace();
			Log.printLine("Unwanted errors happen");
		}
	}
	
	private static double random(double d){
	    Random randNum = new Random();
	    randNum.setSeed((long) d);
	    Float flt = randNum.nextFloat();
	    //System.out.println(flt);
	    //System.out.println(randNum.nextInt(5));
	    return flt;
	}
	/**
		 *
		 * [-362064, -50194, 173507, -72308, 52010
 		 *  -83616, -113108, -9378, -109646, 207230]
		 */

	private static void create_ds_sensor(FogDevice edge, int userId0, String app_ds, double delay) {
		//Random rnd = new Random(40);
        //rnd.nextInt(100);//System.out.println();
		/**
		 *
		 * [-362064, -50194, 173507, -72308, 52010
 		 *  -83616, -113108, -9378, -109646, 207230]
		 */
		//String id = edge.getName();
		Sensor caSensor = new Sensor("s-"+app_ds, "CAMERA", userId0, app_ds, 
				new DeterministicDistribution(100+(random(-113108))));//1000
		// inter-transmission time of sensor follows a deterministic distribution
		sensors.add(caSensor);
		caSensor.setGatewayDeviceId(edge.getId());
		caSensor.setLatency(delay);  // latency of connection between CAMERA sensors and the parent Smartphone is 1 ms
	}
	private static void create_ds_actuator(FogDevice edge, int userId0, String app_ds, double delay) {
		/**
		 *
		 * [-362064, -50194, 173507, -72308, 52010
 		 *  -83616, -113108, -9378, -109646, 207230]
		 */
		//String id = edge.getName();
		Actuator display = new Actuator("a-"+app_ds, userId0, app_ds, "USER");
		actuators.add(display);
		display.setGatewayDeviceId(edge.getId());
		display.setLatency(delay);   // latency of connection between panorama_visualizer and the parent Smartphone is 1 ms
		
	}
	private static void createFogDevices( int userId0, String appId0, String appId1, String appId2)//, String appId2, String appId3, String appId4) 
	{
		//CPU_r = [100000* 10 ^ 6,79000* 10 ^ 6,76000* 10 ^ 6, 29000* 10 ^ 6,28000* 10 ^ 6,27000* 10 ^ 6,26000* 10 ^ 6,25000* 10 ^ 6,24000* 10 ^ 6,23000* 10 ^ 6,29000* 10 ^ 6,21000* 10 ^ 6,20000* 10 ^ 6,20000* 10 ^ 6]#MIPS
		//m5a.24xlarge (- ECUs, 96 vCPUs, 2.5 GHz, AMD EPYC 7571, 384 GiB memory, EBS only)
		//m5d.8xlarge (131 ECUs, 32 vCPUs, 3.1 GHz, Intel Xeon Platinum 8175, 128 GiB memory, 2 x 600 GiB Storage Capacity, BW 10 Gigabit)
		FogDevice cloud = createFogDevice("cloud", 100000, 128000, 512000, 100, 5288, 0, 0.01, 16*103, 16*83.25); // creates the fog device Cloud at the apex of the hierarchy with level=0
		//System.out.println("cloud"+"  mips: "+(256000)+"  ram: "+(32000)+"  storage: "+512000+"  upBw: "+(100)+"  downBw: "+(12288) +"  busyPower: "+(16*103)+"  idlePower: "+(16*83.25));////////
		//  Megabits/s
//////////////////////////////////////////////////////////////////cloud.setSiblingId(-1);
		cloud.setParentId(-1);
		fogDevices.add(cloud);
		//m4.4xlarge (53.5 ECUs, 16 vCPUs, 2.4 GHz, Intel Xeon E5-2676v3, 64 GiB memory, BW High)
		FogDevice tier2 = createFogDevice("tier-server2", 79000, 64000, 256000, +10000, 9048, 4, 0.0, 5*107.339, 5*83.4333); // creates the fog device tier Server (level=1)
		//System.out.println("tier-server2"+"  mips: "+(64000)+"  ram: "+(16000)+"  storage: "+256000+"  upBw: "+(2048)+"  downBw: "+(2048) +"  busyPower: "+(5*107.339)+"  idlePower: "+(5*83.4333));////////
		fogDevices.add(tier2);
		
		//////////////////////////////////////////
		FogDevice tier1 = createFogDevice("tier-server1", 76000, 64000, 256000, +10000, 9048, 2, 0.0, 5*107.339, 5*83.4333); // creates the fog device tier Server (level=1)
		//System.out.println("tier-server"+"  mips: "+(64000)+"  ram: "+(16000)+"  storage: "+256000+"  upBw: "+(2048)+"  downBw: "+(2048) +"  busyPower: "+(5*107.339)+"  idlePower: "+(5*83.4333));////////
		fogDevices.add(tier1);
		///////////////////////////////////
		
		tier1.setParentId(tier2.getId()); 
		tier1.setUplinkLatency(60);
		 
		tier2.setParentId(cloud.getId()); 
		tier2.setUplinkLatency(100); 
		//tier2.setDownlinkBandwidth(downlinkBandwidth);

		FogDevice edge7 = addedge("e-0-7", userId0, appId0, appId1, appId2,  tier1.getId() ,  0+7); // adding edges to the physical topology. Smartphones have been modeled as fog devices as well.
		edge7.setUplinkLatency(3);  
		fogDevices.add(edge7);	   
		
	}

	private static FogDevice addedge(String id, int userId0, String appId0, String appId1, String appId2,  int parentId, int i)
	{
		int Num = 29000, Num2 = 10000, Num3 = 0;// Num2 in Mbit/sec
		FogDevice edge = null;
		
		if (i == 9)
		{
			edge = createFogDevice(id, Num, Num, 64000, Num2, Num2, 1, 0, 8.53+Num3, ((8.44+Num3)/2));
			
		}
		else if (i == 7)
		{
			edge = createFogDevice(id, Num, Num, 64000, Num2, Num2, 5, 0, 8.53+Num3, ((8.44+Num3)/2));
			
		}
		else if (i == 8)
		{
			edge = createFogDevice(id, Num, Num, 64000, Num2, Num2, 3, 0, 8.53+Num3, ((8.44+Num3)/2));
		}
		
		
		edge.setParentId(parentId);	
		
		int counter = 0;
		while (counter < 50) { //20//6  
			if(id.contains("-0-7"))
			{
				create_ds_sensor (edge, userId0, appId0, 1. );
				create_ds_actuator (edge, userId0, appId0, 1. );
			}
			counter++;
		}
				
		edges.add(edge);
		return edge;
	}
	private static FogDevice createFogDevice(String nodeName, long mips, /*int numberOfPe,*/
			int ram, long storage , int upBw, int downBw, int level, double ratePerMips, double busyPower, double idlePower) {
		
		List<Pe> peList = new ArrayList<Pe>();

		// 3. Create PEs and add these into a list.
		
		//long mipss = mips+1000;
		peList.add(new Pe(0, new PeProvisionerOverbooking(mips))); // need to store Pe id and MIPS Rating
		
		//System.out.println("MIPS: "+ mips);
		int hostId = FogUtils.generateEntityId();
		//long storage = 128000; // host storage (Megabytes)
		////////////////System.out.println(upBw+" "+downBw+" ");
		int bw = downBw;//(upBw - downBw);
		
//		if (bw < 10000 && bw >= 0)	
//		{
//			bw = 10000; //  Megabits/s
//			//upBw = downBw + bw;
//		}
//		else if ( bw < 0)	bw = downBw;
		
		PowerHost host = new PowerHost(
				hostId,
				new RamProvisionerSimple(ram), /*org.cloudbus.cloudsim.Host.getRamProvisioner().getRam()*/  //ram+10000 
				new BwProvisionerOverbooking(bw), /*org.cloudbus.cloudsim.Host.getBwProvisioner().getBw()*/ //bw+30000
				storage,
				peList,
				new StreamOperatorScheduler(peList),
				new FogLinearPowerModel(busyPower, idlePower)
			);
		//+30000
		//ram+10000
		System.out.println(nodeName+"    mips: "+(mips)+"  ram: "+ (ram) +"  storage: "+storage+"  Bw: "+(bw) +"  busyPower: "+busyPower+"  idlePower: "+(idlePower));
		List<Host> hostList = new ArrayList<Host>();
		hostList.add(host);
		
		String arch = "x86"; // system architecture
		String os = "Linux"; // operating system
		String vmm = "Xen";
		
		double costPerSec = 0.0; // the cost of using processing in this resource
		double costPerMem = 0.0; // the cost of using memory in this resource
		double costPerStorage = 0.00000000; // the cost of using storage in this
										// resource
		double costPerBw = 0.0; // the cost of using bw in this resource
		
		double time_zone = 1.0; // time zone this resource located
		
		LinkedList<Storage> storageList = new LinkedList<Storage>(); // we are not adding SAN
																	// devices by now

		FogDeviceCharacteristics characteristics = new FogDeviceCharacteristics(
				arch, os, vmm, host, time_zone, costPerSec, costPerMem,
				costPerStorage, costPerBw);

		FogDevice fogdevice = null;
		try {
			fogdevice = new FogDevice(nodeName, characteristics, //+10000
					new AppModuleAllocationPolicy(hostList), storageList, 10, upBw, downBw, 0, ratePerMips, costPerSec, costPerMem,
					costPerStorage, costPerBw);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		fogdevice.setLevel(level);
		return fogdevice;
	}

	@SuppressWarnings("serial")
	private static Application createApplicationVideoStreamAnalysis(String appId, int userId)
	{
		Application application = Application.createApplication(appId, userId);
		
		/*
		 * Adding modules (vertices) to the application model (directed graph)
		 */

		//["high_quali_encod","lightCNN","transcoding","packaging"]
		//["high_quali_encod","lightCNN","detecting_classifying","permanent_storing","packaging"],
		//["high_quali_encod","lightCNN","transcoding","permanent_storing","packaging"]]

		application.addAppModule("high_quali_encod",  10000, 500, 5000);//100, 100, 512/*in MB*/);
		application.addAppModule("lightCNN",     		10000, 500, 5000);// 200, 200, 512);
		application.addAppModule("detecting_classifying", 10000, 500, 5000);//300, 300, 512);
		application.addAppModule("transcoding",       10000, 500, 5000);//100, 100, 512);
		application.addAppModule("permanent_storing", 10000, 500, 5000);//400, 200, 512);
		application.addAppModule("packaging",         10000, 500, 5000);//300, 300, 512);
	
		/*
		 * Connecting the application modules (vertices) in the application model (directed graph) with edges
		 */
		
		application.addAppEdge("CAMERA", "high_quali_encod", 10000, 10, 10125000 /*in Bytes*/,        "CAMERA", Tuple.UP, AppEdge.SENSOR); // adding edge from  to    module carrying tuples of type
		application.addAppEdge("high_quali_encod", "lightCNN", 10000, 40, 10125000,  "A11", Tuple.UP, AppEdge.MODULE); // adding edge from   to   module carrying tuples of type 
		application.addAppEdge("lightCNN", "detecting_classifying", 10000, 30, 10125000, "A21", Tuple.UP, AppEdge.MODULE); // adding edge from   to   module carrying tuples of type 
		application.addAppEdge("lightCNN", "transcoding", 10000, 10, 10125000,  "A12", Tuple.UP, AppEdge.MODULE); // adding edge from   to   module carrying tuples of type
		application.addAppEdge("transcoding", "permanent_storing", 10000, 10, 10125000,      "A31", Tuple.UP, AppEdge.MODULE); // adding edge from   to   module carrying tuples of type
		application.addAppEdge("transcoding", "packaging", 10000, 10, 10125000,      "A13", Tuple.UP, AppEdge.MODULE); // adding edge from   to   module carrying tuples of type
		application.addAppEdge("detecting_classifying", "permanent_storing", 10000, 30, 10125000, "A22", Tuple.UP, AppEdge.MODULE); // adding edge from   to   module carrying tuples of type 
		application.addAppEdge("permanent_storing", "packaging", 10000, 10, 10125000,      "A33", Tuple.UP, AppEdge.MODULE); // adding edge from   to   module carrying tuples of type 
		application.addAppEdge("packaging", "USER", 10000, 10, 10125000,      "USER", Tuple.DOWN, AppEdge.ACTUATOR); // adding edge from   to   module carrying tuples of type 
		

		//Data sizes: 102500 ,1025000, 3075000, 5125000 ,10125000
		
		/*
		 * Defining the input-output relationships (represented by selectivity) of the application modules. 
		 */

		application.addTupleMapping("high_quali_encod",   "CAMERA",   "A11", new FractionalSelectivity(1.0)); // 1.0 tuples of type  are emitted by   module per incoming tuple of type 
		application.addTupleMapping("lightCNN", "A11",  "A12", new FractionalSelectivity(1.0)); // 1.0 tuples of type  are emitted by   module per incoming tuple of type 		 
		application.addTupleMapping("transcoding",  "A12",  "A31", new FractionalSelectivity(1)); // 1.0 tuples of type  are emitted by   module per incoming tuple of type
		application.addTupleMapping("permanent_storing",  "A31",  "A33", new FractionalSelectivity(1)); // 1.0 tuples of type  are emitted by   module per incoming tuple of type

		application.addTupleMapping("transcoding",  "A12",  "A13", new FractionalSelectivity(1)); // 1.0 tuples of type  are emitted by   module per incoming tuple of type 		 
		application.addTupleMapping("packaging",  "A13", "USER", new FractionalSelectivity(1)); // 1.0 tuples of type  are emitted by   module per incoming tuple of type
		
		application.addTupleMapping("lightCNN", "A11",  "A21", new FractionalSelectivity(1.0)); // 1.0 tuples of type  are emitted by   module per incoming tuple of type 
		application.addTupleMapping("detecting_classifying", "A21",  "A22", new FractionalSelectivity(1.0)); // 1.0 tuples of type  are emitted by   module per incoming tuple of type 
		application.addTupleMapping("permanent_storing",  "A22",  "A33", new FractionalSelectivity(1)); // 1.0 tuples of type  are emitted by   module per incoming tuple of type
		application.addTupleMapping("packaging",  "A33", "USER", new FractionalSelectivity(1)); // 1.0 tuples of type  are emitted by   module per incoming tuple of type
		
		/*
		 * Defining application loops (maybe incomplete loops) to monitor the latency of. 
		 * Here, we add two loops for monitoring :  ->   ->   and   ->  
		 */

		AppLoop loop1 = new AppLoop(new ArrayList<String>(){{add("CAMERA");add("high_quali_encod");add("lightCNN");add("transcoding");add("packaging");add("USER");}});
		AppLoop loop2 = new AppLoop(new ArrayList<String>(){{add("CAMERA");add("high_quali_encod");add("lightCNN");add("detecting_classifying");add("permanent_storing");add("packaging");add("USER");}});
		AppLoop loop3 = new AppLoop(new ArrayList<String>(){{add("CAMERA");add("high_quali_encod");add("lightCNN");add("transcoding");add("permanent_storing");add("packaging");add("USER");}});
		List<AppLoop> loops = new ArrayList<AppLoop>(){{add(loop3);}};
		application.setLoops(loops);
		
		return application;
	}
}
