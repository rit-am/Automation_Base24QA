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
			
			while (itr.hasNext())//-Start Reading the File
				{
					++IntLineNumber; //-Keep A note which line we are reading
					StrCurrentLine = itr.next();
					if(StrCurrentLine.length()>6)//-Check if the line is empty, a line with less than 6 letters would be junk characters
					{
						if (StrCurrentLine.substring(0,2).equalsIgnoreCase("R "))//-Check if the line we are reading is a Record Header
							{
							IntRecordHead = IntLineNumber-1;//-Note the Header line number
							BoolRecordHead = true;//-Turn Flag on - Header Found
							}
						if (StrCurrentLine.substring(0,6).equalsIgnoreCase("(10.9)"))//Check if line we are reading is record footer
							{
							IntRecordFoot = IntLineNumber-1;//Note the footer line number
							BoolRecordFoot = true;//-Turn Flag on - Footer Found
							if(BoolRecordHead!=true)BoolRecordFoot=false;//-IF we found a footer without a header, its bad data
							}
						if (BoolRecordHead==true && BoolRecordFoot == true)//Check If we have found a header & a footer
						{
							if(IntRecordFoot-IntRecordHead>8)//If record has less than 8 lines, we probably do not need it
							{
								System.out.println("\n");
								for (Integer i = IntRecordHead;i<=IntRecordFoot;i++)//-Lets read through the record
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
							BoolRecordHead = BoolRecordFoot = false;//-Lets clean the flags, move to next reord
						}
					}
				}
				 
		} 
	} 
