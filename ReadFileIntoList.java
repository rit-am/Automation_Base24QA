package xpnet;
import java.util.*; import java.nio.charset.StandardCharsets; import java.nio.file.*; import java.io.*; 
public class ReadFileIntoList 
	{
	private static String StrFileLocation_1 ="C:\\Users\\";
	private static String StrFileLocation_2="\\Downloads\\Logs\\";
	private static String StrFileName="aud_220219.txt";
	public static List<String> readFileInList(String fileName) 
		{ 
			List<String> lines = Collections.emptyList(); 
			try{lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);}
			catch (IOException e){e.printStackTrace();}
			return lines; 
		}
	public static void run() 
		{ 
			Integer IntLineNumber= 0,IntRecordHead=0,IntRecordFoot=0;
			String StrCurrentLine ="",
					StrFileLocation=
					StrFileLocation_1+
					System.getenv().get("USERNAME")+
					StrFileLocation_2+
					StrFileName;
			boolean  BoolRecordHead = false, BoolRecordFoot = false;
			List<String> l = readFileInList(StrFileLocation); Iterator<String> itr = l.iterator(); 
			while (itr.hasNext())
				{
					++IntLineNumber;StrCurrentLine = itr.next();
					if(StrCurrentLine.length()>6)
					{
						String StrCheckHeader = StrCurrentLine.substring(0,2);
						if (StrCheckHeader.equalsIgnoreCase("R ")) 
							{
							IntRecordHead = IntLineNumber-1; BoolRecordHead = true;
							//System.out.println(":: Record Head found @ :: Line# "+IntRecordHead + " :: " +StrCurrentLine);
							}
						String StrCheckFooter = StrCurrentLine.substring(0,6);
						if (StrCheckFooter.equalsIgnoreCase("(10.9)")) 
							{
							IntRecordFoot = IntLineNumber-1; BoolRecordFoot = true;
							//System.out.println(":: Record Foot found @ :: Line# "+IntRecordFoot +  " :: " +StrCurrentLine);
							if(BoolRecordHead!=true)BoolRecordFoot=false;
							}
						//Check if Record is found
						if (BoolRecordHead==true && BoolRecordFoot == true)
						{
							//System.out.println(":: Complete Record found From :: Line# "+IntRecordHead + " -> Line# "+IntRecordFoot);
							if(IntRecordFoot-IntRecordHead>8)
							{
								System.out.println("\n");
								for (Integer i = IntRecordHead;i<=IntRecordFoot;i++)
								{
									//Instead of writing to console, write to file, based on TID/MID
									//Iterate through the list, check if the test condition (KEY)is available
									//Test condition (KEY) can be RRN/STAN/Card Number + Amount/etc
									//If available Re-Start Iteration by re-initilizing value of i to IntRecordHead
									//Stream Output to a file
									System.out.println(" :: Line# "+i+ " ::" + l.get(i).toString());
								}
								System.out.println("\n");
							}
							else
							{
								//System.out.println(":: \t\tIgnoring record < 6 lines");
							}
							BoolRecordHead = BoolRecordFoot = false;
						}
					}
				}
				 
		} 
	} 
