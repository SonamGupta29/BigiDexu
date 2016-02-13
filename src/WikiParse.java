import java.util.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.TreeMap;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class WikiParse 
{
	public static TreeMap<String, HashMap<String , HashMap<Integer, Integer> > > iIndex =
																			new TreeMap<String, HashMap<String , HashMap<Integer, Integer> > >();
	public static TreeMap<String, String> pIDToTitle = new TreeMap<String, String>();
    public static List<File> intermediateIndexFileList = new ArrayList <>();
    public static List<File> intermediateTitleFileList = new ArrayList <>();
    public static List<File> lastIndexFileList = new ArrayList <>();
    public static List<File> lastTitleFileList = new ArrayList <>();
    
    public static BufferedWriter indexLevel1 =  null;
    public static BufferedWriter indexLevel2 =  null;
    public static BufferedWriter indexLevel3 =  null;
    public static int writeCount = 0;
    
	public static void main(String[] args) {
		
		//Start the timer
		long startTime = System.currentTimeMillis();
		
		// create indexLevel1 file
		try {
			indexLevel1 = new BufferedWriter(new FileWriter(new File("indexLevel1")));
			indexLevel2 = new BufferedWriter(new FileWriter(new File("indexLevel2")));
			indexLevel3 = new BufferedWriter(new FileWriter(new File("indexLevel3")));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String inputFileName = null;
		if(args.length == 1) {
			inputFileName = args[0].trim();
		} else {
			System.out.println("Invalid number of command line arguments...");
			System.exit(0);
		}
		
		try {
			parseXML(inputFileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//End the timer
	    long endTime = System.currentTimeMillis();;
	    System.out.println("\nExecution Time : " + (float)(endTime - startTime)/1000 + " sec");
	}
	
	
	public static void parseXML(String rawInputFile)
	{	
		String fileName = rawInputFile;
		
		try {
			//	Obtain and configure the SAX based parser
			SAXParserFactory spf = SAXParserFactory.newInstance();			
			//	Obtain object for SAX Parser
			SAXParser saxParser;
			saxParser = spf.newSAXParser();			
			UserHandler userHandler = new UserHandler();			
		    saxParser.parse(fileName, userHandler);
		    
		    // 	Check if the pages are remaining then write those to the files
		    UserHandler.createIfRemaining();
		    
		    // 	Merge the sorted index file
		    mergeSortedFiles(lastIndexFileList, new File("Index"), lastIndexFileList.size(), 0);
		    
		    //	Merge the sorted title file
		    mergeSortedFiles(intermediateTitleFileList, new File("pIDToTitleMap"), intermediateTitleFileList.size(), 1);
		    
		    //	Clear the hash that we are using for the storage for the intermediate index
			WikiParse.iIndex.clear();
			
		} catch(Exception e) {			
			e.printStackTrace();
		}
	}
	
	public static void mergeSortedFiles(List<File> files, File outputfile, int size, int type) throws IOException {

		PriorityQueue<BinaryFileBuffer> pq = new PriorityQueue<BinaryFileBuffer>((int)size, new Comparator<BinaryFileBuffer>() {

			public int compare(BinaryFileBuffer i, BinaryFileBuffer j) {
				return i.peek().split(":")[0].compareTo(j.peek().split(":")[0]);
			}
		});

		for (File f : files) {
			pq.add(new BinaryFileBuffer(f));
		}
		
		BufferedWriter fbw = new BufferedWriter(new FileWriter(outputfile));
		int rowcounter = 0, firstLevelCounter = 0 , tillCharOffset1 = 0, tillCharOffset2 = 0, secondLevelCounter =0, tillCharOffset3 = 0 , thirdLevelCounter = 0;
		
		long tillCharOffset = 0;
		
		StringBuilder lastterm = new StringBuilder("nullstart");
		lastterm.setLength(0);

		try {
			while(pq.size() > 0) {
				BinaryFileBuffer bfb = pq.poll();
				String r = bfb.pop();
				if(type == 0) {						
					r = r.replaceAll(" ", "");				
					if(lastterm.length() == 0){
						lastterm.append(r);
					} else if (lastterm.length() != 0) {
						if(r.split(":")[0].compareTo(lastterm.toString().split(":")[0]) == 0) {
							StringBuilder temp = new StringBuilder(lastterm.substring(0,lastterm.lastIndexOf("}")) + "," +
																	r.substring(r.indexOf("{") + 1, r.length()));
							lastterm.setLength(0);
							lastterm.append(temp.toString());
						} else {
							
							List <String> docPostingList = new ArrayList <String>();
							
							// Need to get the idf and lets store it like term:idf:posting list
							
							// eg = beyond:{12={2=1, 5=1}, 13={2=1, 5=1}}
							
							String termNList[] = lastterm.toString().split(":");
							
							//first index is of term
							String term = termNList[0];
							
							//Second index will be of posting list to separate out the list for each document
							String docPostList[] = termNList[1].toString().split("},");
							
							// calculate df (document frequency)
							int dfCount = docPostList.length;
													
							for(int itr = 0; itr < dfCount; itr++) {
								
								//Get the id and list --> split on the basis of '={'
								
								StringBuilder oneList = new StringBuilder();
								
								String idNList[] = docPostList[itr].split("=\\{");
								
								String pageID = null;
								
								if (idNList[0].contains("{"))
									pageID = idNList[0].replace("{", "");
								else
									pageID = idNList[0];
								
								oneList.append(pageID+"=");
								
								//Split on the basis of comma
								
								String sectionList[] = idNList[1].trim().replaceAll("\\{|\\}","").split(",");
								
								int sectionListLength  = sectionList.length;
								
								//System.out.print(sectionListLength + " sectionList : " +  idNList[1].trim().replaceAll("\\{|\\}","") + " ");
								float score = 0;
								
								for(int k = 0; k < sectionListLength; k++) {
									
									//split on the basis of the "="
									
									String secPart[] = sectionList[k].split("=");
									
									switch(secPart[0]){
										case "0":
											score += Integer.parseInt(secPart[1].trim()) * 100;
											//title
											break;
							 			case "1":
							 				score += Integer.parseInt(secPart[1].trim()) * 30;
							 				//"infobox":
							 				break;
							 			case "2":
							 				score += Integer.parseInt(secPart[1].trim()) * 10;
							 				//externalLinks"
							 				break;	
							 			case "3":
							 				score += Integer.parseInt(secPart[1].trim()) * 10;
							 				//"references":
							 				break;	
							 			case "4":
							 				score += Integer.parseInt(secPart[1].trim()) * 7;
							 				//"category":
							 				break;
							 			case "5":
							 				score += Integer.parseInt(secPart[1].trim()) * 5;
							 				//optionIndex = 5;
							 				break; 
										}
										oneList.append(secPart[0] + "-" + secPart[1]);
										if(k + 1 != sectionListLength)
											oneList.append(",");
									}							
									docPostingList.add(oneList.toString() + "#" + (score * Math.log10(UserHandler.totalPageCount/ dfCount)));
								}
							
							Collections.sort(docPostingList, new Comparator<String>(){
	
								@Override
								public int compare(String s1, String s2) {
									
									float value1 = Float.parseFloat(s1.split("#")[1]);
									float value2 = Float.parseFloat(s2.split("#")[1]);
									if(value1 == value2)
										return 0;
									else if(value1 < value2)
										return 1;								
									return -1;								
								}								
							});
							
							/*
							 * 	Index value	
							 */
							
							if(++firstLevelCounter == 10000) {
								String writeOut1 = term + ":" + tillCharOffset1;								
								indexLevel1.write(writeOut1 + "\n");
								if(++secondLevelCounter == 10000){
									String writeOut2 = term + ":" + tillCharOffset2;
									indexLevel2.write(writeOut2 + "\n");									
																		
									if(++thirdLevelCounter == 10000) {
										indexLevel3.write(term + ":" + tillCharOffset3 + "\n");										
										thirdLevelCounter = 0;
									}
									secondLevelCounter = 0;
									byte b1[];
									b1 = String.valueOf(writeOut2).getBytes();
					                tillCharOffset3 = tillCharOffset3 + b1.length + 1;
								}
								firstLevelCounter = 0;
								byte b[];
								b = String.valueOf(writeOut1).getBytes();
				                tillCharOffset2 = tillCharOffset2 + b.length + 1;
							}
			               
			                StringBuilder finalWrite =  new StringBuilder();
							
			                finalWrite.append(term + ":");
			                
							//fbw.write(term + ":");
							int len = docPostingList.size();
							for(int itr = 0; itr < len; itr++) {
								//fbw.write(docPostingList.get(itr).split("#")[0]);
								finalWrite.append(docPostingList.get(itr).split("#")[0]);
								if(itr + 1 != len) {
									//fbw.write(":");
									finalWrite.append(":");
								}
							}
							fbw.write(finalWrite.toString());							
							fbw.newLine();
							
							//Get the offset
							
							byte b[];
			                b = finalWrite.toString().getBytes();
			                tillCharOffset1 = tillCharOffset1 + b.length + 1;				
							
							lastterm.setLength(0);
							lastterm.append(r);
						}
					}
				} else {
					fbw.write(r);
					fbw.newLine();
				}
				
				++rowcounter;				
				if(bfb.empty()) {
					bfb.fbr.close();
					bfb.originalfile.delete();// we don't need you anymore
				} else {
					pq.add(bfb); // add it back
				}
			}
		} catch(Exception e) {
			//System.out.println(e.printStackTrace());
		} finally { 
			fbw.close();
			if(type == 0){
				indexLevel1.close();
				indexLevel2.close();
				indexLevel3.close();
			}			
			for(BinaryFileBuffer bfb : pq ) 
				bfb.close();
		}
	}
	
	public static void mergeRecur(List<File> files, File outputfile, int size, int type) throws IOException {

		PriorityQueue<BinaryFileBuffer> pq = new PriorityQueue<BinaryFileBuffer>(size, new Comparator<BinaryFileBuffer>() {
			
			@Override
			public int compare(BinaryFileBuffer i, BinaryFileBuffer j) {
				return i.peek().split(":")[0].compareTo(j.peek().split(":")[0]);
			}
		});

		for (File f : files) {
			pq.add(new BinaryFileBuffer(f));
		}
		
		BufferedWriter fbw = new BufferedWriter(new FileWriter(outputfile));
				
		StringBuilder lastterm = new StringBuilder("nullstart");
		lastterm.setLength(0);

		try {
			while(pq.size() > 0) {
				BinaryFileBuffer bfb = pq.poll();
				String r = bfb.pop();
				if(type == 0) {						
					r = r.replaceAll(" ", "");				
					if(lastterm.length() == 0){
						lastterm.append(r);
					} else if (lastterm.length() != 0) {
						if(r.split(":")[0].compareTo(lastterm.toString().split(":")[0]) == 0) {
							StringBuilder temp = new StringBuilder(lastterm.substring(0,lastterm.lastIndexOf("}")) + "," +
																	r.substring(r.indexOf("{") + 1, r.length()));
							lastterm.setLength(0);
							lastterm.append(temp.toString());
						} else {
							fbw.write(lastterm.toString());
							lastterm.setLength(0);
							lastterm.append(r);
							fbw.newLine();
						}
					}
				} else {
					fbw.write(r);
					fbw.newLine();
				}
				
				if(bfb.empty()) {
					bfb.fbr.close();
					bfb.originalfile.delete();// we don't need you anymore
				} else {
					pq.add(bfb); // add it back
				}
			}
		} catch(Exception e) {
			//System.out.println(e.printStackTrace());
		} finally { 
			fbw.close();
			for(BinaryFileBuffer bfb : pq ) 
				bfb.close();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}

class UserHandler extends DefaultHandler
{
		static int pageCount = 0, totalPageCount = 0;
		static int pCount = 0;
		static int counter = 0;
		public static String pageTitleText = "";
		boolean ID = false;
		boolean pageTitle = false;
		boolean text = false;
		boolean isTaken = false;
		StringBuilder currentDoc = new StringBuilder();
		String pID = "";
		Tokenize t = new Tokenize();
		
		//This method is called every time when parser gets the opening "<" tag
		//identifies which  tag is open at a time by  assigning  an open flag
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
		{
			switch(qName.toLowerCase())	{
				case "id":
					ID = true;
					break;
				case "title":
					pageTitle = true;
					break;
				case "text":
					text = true;
					break;
				default:
					break;
			}
		}
		
		//This method is called every time when the parser get the closing tag ">"
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException
		{
			//Call the method of the tokenization
			// Switch method is more efficient than if - else if statements coz compiler
			// created the jump table for the instructions
			if(qName.toLowerCase().equals("page")) {
				try {
					t.tokenize(pID, docData());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				ID = false;
				pageTitle = false;
				text = false;
				isTaken = false;
				pageTitleText = "";
				pID = "";
				currentDoc.setLength(0);
				totalPageCount++;
				if(++pageCount == 5000) {
					
					//	call the write procedure and clear the page count 
					// 	this thing we will take care because in case of large data dump,
					// 	our all variables will get exhausted and also it is very expensive to bring
					//	the file in memory every time, So till then just goon building the index
					//  Hence after this write to the file					
					// 	Write to the file
					
					System.out.println("Writing page : " + (pCount += 5000));
					
					try {
						
						writeIntermediateIndex(++counter);
						
						if(counter == 50){
							
							WikiParse.mergeRecur(WikiParse.intermediateIndexFileList, new File("middle_" + String.valueOf(++WikiParse.writeCount)),
																					WikiParse.intermediateIndexFileList.size(), 0);
							
							WikiParse.intermediateIndexFileList.clear();
							
							WikiParse.lastIndexFileList.add(new File("middle_" + String.valueOf(WikiParse.writeCount)));
							
							counter = 0;
						}
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					
					//	Clear the hash that we are using for the storage for the intermediate index
					WikiParse.iIndex.clear();
					WikiParse.pIDToTitle.clear();
					pageCount = 0;
				}				
				
			} else if(qName.toLowerCase().equals("text")) {
				text = false;
			}
		}
		
		private static void writeIntermediateIndex(int fileCount) throws IOException {
			
			//Create an intermediate file
			FileWriter fw = new FileWriter(new File("intermediateIndex_" + String.valueOf(fileCount)));
			
			WikiParse.intermediateIndexFileList.add(new File("intermediateIndex_" + String.valueOf(fileCount)));
			Iterator<Entry<String, HashMap<String, HashMap<Integer, Integer>>>> it = WikiParse.iIndex.entrySet().iterator();
		    while (it.hasNext()) 
		    {
		        Map.Entry pair = (Map.Entry)it.next();
		        fw.write(pair.getKey().toString() + ":" + pair.getValue()+"\n");
		        it.remove(); // avoids a ConcurrentModificationException
		    }
		    fw.close();
		    
		    // Add the pID to title map in the pIDToTitleMap
		    fw = new FileWriter(new File("intermediateMap_" + String.valueOf(fileCount)));
		    WikiParse.intermediateTitleFileList.add(new File("intermediateMap_" + String.valueOf(fileCount)));
		    
		    Iterator<Entry<String, String>> itMap = WikiParse.pIDToTitle.entrySet().iterator();
		    while (itMap.hasNext()) 
		    {
		        Map.Entry pair = (Map.Entry)itMap.next();
		        fw.write(pair.getKey().toString() + ":" + pair.getValue()+"\n");
		        itMap.remove(); // avoids a ConcurrentModificationException
		    }
		    fw.close();
		}
		
		public static void createIfRemaining()
		{
			System.out.println("Final Pages : ");
			System.out.println("Total page count : " + totalPageCount);
			System.out.println("pageCount Yet : " +  pCount);
			if(totalPageCount != pCount) {
				try {
					writeIntermediateIndex(++counter);
					
					WikiParse.mergeRecur(WikiParse.intermediateIndexFileList, new File("middle_" + String.valueOf(++WikiParse.writeCount)),
																				WikiParse.intermediateIndexFileList.size(), 0);
					WikiParse.intermediateIndexFileList.clear();						
					WikiParse.lastIndexFileList.add(new File("middle_" + String.valueOf(WikiParse.writeCount)));					
					WikiParse.iIndex.clear();
					WikiParse.pIDToTitle.clear();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		//print the data between the tags "<" & ">"
		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			
			if(ID) {
				if(!isTaken) {
					pID = new String(ch, start, length);
					isTaken = true;
				}
				/*	What if in case any user searches for the text using the revision, hence add 
				*	extracted data also, Anyway I gonna remove numeric data, coz there is very rare chances of
				* 	sear
				*/
				//	currentDoc.append(" " + new String(ch, start, length));
				ID = false;
			} else if(pageTitle) {
				//currentDoc.append(" " + new String(ch, start, length));
				pageTitleText = new String(ch, start, length);
				pageTitle = false;
			} else if(text) {
				currentDoc.append(" " + new String(ch, start, length));
			}
			/*else
			{
				currentDoc.append(" " + new String(ch, start, length));
			}*/
		}
		
		//This method will give the current page text which we can process later
		public String docData() {
			
			return currentDoc.toString();
		}
}


