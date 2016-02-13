import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class Tokenize_old{
	
	 	//Pattern categoryTextPattern = Pattern.compile("\\[\\[Category:(.*?)\\]\\]", Pattern.MULTILINE);
	 	//Pattern referencesTextpattern = Pattern.compile("==.?[Rr]eferences.?==");
	 		 	
	 	// Stops words list + other words which are kind of meta tags in the wiki dump
	 	
	 /*	HashSet stopWordsList = new HashSet<String>(Arrays.asList("a","again","about","above","across","after","against","all","almost","alone",
	 							"along","already","also","although","always","am","among","an","and","another","any","anybody","anyone","anything",
 							"anywhere","are","area","areas","aren't","around","as","ask","asked","asking","asks","at","away","b","back","backed",
							"backing","backs","be","became","because","become","becomes","been","before","began","behind","being","beings","below",
							"best","better","between","big","both","but","by","c","came","can","can't","cannot","case","cases","certain","certainly",
							"clear","clearly","com","come","coord","could","couldn't","d","did","didn't","differ","different","differently","do",
							"does","doesn't","doing","don't","done","down","downed","downing","downs","during","e","each","early","either","end",
							"ended","ending","ends","enough","even","evenly","ever","every","everybody","everyone","everything","everywhere","f",
							"face","faces","fact","facts","far","felt","few","find","finds","first","for","four","from","full","fully","further",
							"furthered","furthering","furthers","g","gave","general","generally","get","gets","give","given","gives","go","going",
							"good","goods","got","gr","great","greater","greatest","group","grouped","grouping","groups","h","had","hadn't","has",
							"hasn't","have","haven't","having","he","he'd","he'll","he's","her","here","here's","hers","herself","high","higher",
							"highest","him","himself","his","how","how's","however","http","https","i","i'd","i'll","i'm","i've","if","important",
							"in","interest","interested","interesting","interests","into","is","isn't","it","it's","its","itself","j","just","k",
							"keep","keeps","kind","knew","know","known","knows","l","large","largely","last","later","latest","least","less","let",
							"let's","lets","like","likely","long","longer","longest","m","made","make","making","man","many","may","me","member",
							"members","men","might","more","most","mostly","mr","mrs","much","must","mustn't","my","myself","n","nbsp","necessary",
							"need","needed","needing","needs","never","new","newer","newest","next","no","nobody","non","noone","nor","not",
							"nothing","now","nowhere","number","numbers","o","of","off","often","old","older","oldest","on","once","one","only",
							"open","opened","opening","opens","or","order","ordered","ordering","orders","other","others","ought","our","ours",
							"ourselves","out","over","own","p","part","parted","parting","parts","per","perhaps","place","places","point","pointed",
							"pointing","points","possible","present","presented","presenting","presents","problem","problems","put","puts","q",
							"quite","r","rather","really","right","room","rooms","s","said","same","saw","say","says","second","seconds","see",
							"seem","seemed","seeming","seems","sees","several","shall","shan't","she","she'd","she'll","she's","should","shouldn't",
							"show","showed","showing","shows","side","sides","since","small","smaller","smallest","so","some","somebody","someone",
							"something","somewhere","state","states","still","such","sure","t","take","taken","td","than","that","that's","the",
							"their","theirs","them","themselves","then","there","there's","therefore","these","they","they'd","they'll","they're",
							"they've","thing","things","think","thinks","this","those","though","thought","thoughts","three","through","thus","to",
							"today","together","too","took","toward","tr","turn","turned","turning","turns","two","u","under","until","up","upon",
							"us","use","used","uses","v","very","w","want","wanted","wanting","wants","was","wasn't","way","ways","we","we'd",
							"we'll","we're","we've","well","wells","went","were","weren't","what","what's","when","when's","where","where's",
							"whether","which","while","who","who's","whole","whom","whose","why","why's","will","with","within","without","won't",
							"work","worked","working","works","would","wouldn't","www","x","y","year","years","yet","you","you'd","you'll","you're",
							"you've","young","younger","youngest","your","yours","yourself","yourselves","z",
							
							//This are the words which are not used in the text, these words are metadata
							
							"Refbegin","Reflist","isbn"
	 			
	 			));
	 	
	 	Map<String, Integer> occurrences = new HashMap<String, Integer>();*/
	 	
		public void tokenize(String pID, String data) throws IOException{
			long startTime = System.nanoTime();		
			
			/*	
			 * What if I parse the text linearly and check for each occurrence like
			 * 	check if the text is {{Infobox or not, like External Links or not!!!,
			 * 	category, references, title
			 * 
			 * 	OK Lets give it a try
			 * 	but not worth as it is like getting all the substrings from the text
			 *  of some specific length
			 */
			
			//Category Text
			/*StringBuilder extractedCategoryText = new StringBuilder(102400);
			
			Matcher catergoryTextMatcher = categoryTextPattern.matcher(data);
			
			while(catergoryTextMatcher.find()){
				extractedCategoryText.append(catergoryTextMatcher.group(1) + " ");
			}*/
			
			//Now replace the category text form the original data by null to avoid the duplication
			// Not able to remove the category text but removed the [ and ] from the text
			/*data = data.replaceAll("\\[\\]", "");
			
			//Infobox text
			String infoboxTextPattern = "{{Infobox";
			StringBuilder extractedinfoboxText = new StringBuilder(102400);

			int infoboxTextPatternLength = infoboxTextPattern.length(), startLoc = 0,
										   i = 0, ctr = 0, textLength = data.length(), substringStartLoc;
									
			while((substringStartLoc = data.indexOf(infoboxTextPattern, startLoc)) != -1){
				ctr = 2;
				for(i = data.indexOf(infoboxTextPattern, startLoc) + 9 ; ctr != 0 && i < textLength - infoboxTextPatternLength; i++){
					if(data.charAt(i) == '{'){
						ctr++;
					}else if(data.charAt(i) == '}'){
						ctr--;
					}
				}
				extractedinfoboxText.append(data.substring(substringStartLoc + 9, i));
				startLoc = i - 1;
			}*/
			
			
			/*//References 
			Matcher referenceTextMatcher = referencesTextpattern.matcher(data);
			
			StringBuilder extractedreferenceText = new StringBuilder(102400);
			
			try{
				
				if(referenceTextMatcher.find())
				{
					int startIndex = referenceTextMatcher.end();
					
					if(startIndex + 2 < textLength){
						while(!data.substring(startIndex, startIndex + 2).equalsIgnoreCase("{{")) 
							startIndex++;
						
						ctr = 2;
						for(i = startIndex + 2 ; ctr != 0 && i < textLength - 16 ; i++){
							if(data.charAt(i) == '{'){
								ctr++;
							}else if(data.charAt(i) == '}'){
								ctr--;
							}
						}
						if(i - 3 > textLength)
							i = textLength - 3;
						extractedreferenceText.append(data.substring(startIndex + 2, i - 2));
					}
				}
			}
			catch(Exception e){
				
			}*/
		/*						
			data = data.replaceAll("&gt;", ">");
			data = data.replaceAll("&lt;", "<");
			data = data.replaceAll("<.?ref.?>(.*)<.?\\/.?ref.?>", " ");
	           data = data.replaceAll("</?.*?>", " ");
	           data = data.replaceAll("\\'+|%|\\*|-|\\+|[0-9]|;", " ");
	           data=data.replaceAll(":"," ");
	           data=data.replaceAll("\\?"," ");
	           data=data.replaceAll("\\["," ");
	           data=data.replaceAll("\\]"," ");
	   			data=data.replaceAll("\\("," ");
	   			data=data.replaceAll("\\)"," ");
	   			data=data.replaceAll("\\{"," ");
	   			data=data.replaceAll("\\}"," ");
	   			data=data.replaceAll(","," ");
	   			data=data.replaceAll("="," ");
	   			data=data.replaceAll("/"," ");
	   			data=data.replaceAll("\\|"," ");
	   			data=data.replaceAll("-"," ");
	   			data=data.replaceAll("\\."," ");
	   			data=data.replaceAll("\""," ");
	   			data=data.replaceAll("\'"," ");
	   			data=data.replaceAll("( )+"," ");
	   			data=data.replaceAll("\n","");	
		    //Split the data
		    String words[]= data.split(" ");*/
		    
		    
		/*    
		   for(int i = 0; i < words.length; i++)
		    {
		    	if(!stopWordsList.contains(words[i].toLowerCase().trim()) && !words[i].isEmpty() )
		    	{
		    		Integer oldCount = occurrences.get(words[i].trim());
		    		if ( oldCount == null ) {
		    			oldCount = 0;
		    		}
		    		occurrences.put(words[i].trim(), oldCount + 1);
		    	}
		    }
		   */
		  /* for (Map.Entry<String, Integer> entry : occurrences.entrySet()) {
		        String key = entry.getKey().toString();;
		     
		        Integer value = entry.getValue();
		        System.out.println(pID + ":" + key + "-->" + value + "\n");
		    }
			*/
			
			
			
			
			/*
			
			
			int dataLength = data.length();
			
			String categoryTextPattern = "[[Category:";
			int categoryTextPatternLength = categoryTextPattern.length();
			String extractedCategoryText = "";
			
			String infoboxTextPattern = "{{Infobox";
			int infoboxTextPatternLength = infoboxTextPattern.length();
			String extractedinfoboxText = "";
			
			String linksTextPattern = "== External links ==";
			int linksTextPatternLength = linksTextPattern.length();
			String extractedlinksText = "";
			
			String referenceTextPattern = "References";
			int referenceTextPatternLength = referenceTextPattern.length();
			String extractedreferenceText = "";
			
			String extractedRemainingText = "";
			
									
			for(int i = 0; i < dataLength - linksTextPatternLength ; i++){
				
				// For category Text compare it with the categorypattern
				if((data.substring(i, i + categoryTextPatternLength)).equals(categoryTextPattern)){
					
					System.out.println("Extracting Category text...");
					
					//Get the data till the ]]
					extractedCategoryText += (data.substring(i + categoryTextPatternLength, 
															(i = data.indexOf("]]", i + categoryTextPatternLength + 1))))
										  + " \n";
					
				}
				
				//	For infoBox text compare it with the infobox text
				else if((data.substring(i, i + infoboxTextPatternLength)).equals(infoboxTextPattern)){
					
					System.out.println("Extracting infoBox text...");
					//Get the data till the }}
					int ctr = 2, j;
					
					for(j = i + infoboxTextPatternLength ; ctr != 0 && j < dataLength; j++){
						
						if(data.charAt(j) == '{')
							ctr++;
						else if(data.charAt(j) == '}')
							ctr--;
						//else
							//extractedinfoboxText += data.charAt(j);
					}
					
					//Just added
					extractedinfoboxText += data.substring(i + infoboxTextPatternLength, j);
					
					i = j - 1; 
				}
				
				//For external links compare it with the "== External links =="
				
				else if((data.substring(i, i + linksTextPatternLength)).equals(linksTextPattern)){
					
					System.out.println("Extracting links text...");
										
					int j = i + linksTextPatternLength ;
					
					// We need a pattern like new line and then * in the start of the new line, but 
					// we need to take care about the * that will occur in the line, what if the * occurs 
					// hence we will use new line and * pattern to get the line till then we will skip everything
			
					// loop till start of the link (in wiki dump denoted by "*"
					while(!(data.substring(j, j + 2)).equals("\n*")){
						
						j++;
					}
					
					//loop till we dont find "*" at the start of the line
					while((data.substring(j, j + 2)).equals("\n*")){
						
						j++;
						
						while(data.charAt(j) != '\n'){
							
							extractedlinksText += data.charAt(j);
							j++;
						}						
					}
					
					i = j - 1;
				}				
				
				// For reference text
				else if((data.substring(i, i + referenceTextPatternLength)).equals(referenceTextPattern)){
					
					System.out.println("Extracting reference text...");
					
					int ctr = 2, j = i + referenceTextPatternLength;
					
					while(!(data.substring(j, j + 2)).equals("{{")){
						
						j++;
					}
					
					for( j = j + 3 ; ctr != 0 && j < dataLength; j++){
						
						if(data.charAt(j) == '{')
							ctr++;
						else if(data.charAt(j) == '}')
							ctr--;
						//else
							//extractedreferenceText += data.charAt(j);
					}
					
					//added now
					extractedinfoboxText += data.substring(i + referenceTextPatternLength, j);

					i = j - 1;
				}
				else
					extractedRemainingText += data.charAt(i);
			}
			
			//remove the junk from the remaining text
			extractedRemainingText = extractedRemainingText.replaceAll("&gt;", ">");
			extractedRemainingText = extractedRemainingText.replaceAll("&lt;", "<");
			extractedRemainingText = extractedRemainingText.replaceAll("<ref>.*?</ref>", " ");
			extractedRemainingText = extractedRemainingText.replaceAll("</?.*?>", " ");
			extractedRemainingText = extractedRemainingText.replaceAll("\\{\\{.*?\\}\\}", " ");
			extractedRemainingText = extractedRemainingText.replaceAll("\\[\\[.*?:.*?\\]\\]", " ");
			extractedRemainingText = extractedRemainingText.replaceAll("\\[\\[(.*?)\\]\\]", "$1");
			extractedRemainingText = extractedRemainingText.replaceAll("\\s(.*?)\\|(\\w+\\s)", " $2");
			extractedRemainingText = extractedRemainingText.replaceAll("\\[.*?\\]", " ");
			extractedRemainingText = extractedRemainingText.replaceAll("\\'+", "");               
			
			
			
			
			
			FileWriter f = null;
			try {
				f = new FileWriter("care.txt", true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				f.write("\n\nCategory\n\n " + extractedCategoryText + "\n\nInfobox\n\n" + 
							extractedinfoboxText + "\n\nExternal Links\n\n" + extractedlinksText+
							"\n\nReference Text\n\n" + ext
		    		ractedreferenceText + 
							"\n\nOther Text\n\n" + extractedRemainingText);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//InfoBox Text
			// TODO just retrived the text from the Infobox, tokenization is pending
			
			String infoBoxText = "";
			int startLoc = 0, i, ctr = 0, textLength = data.length();
									
			while(data.indexOf("{{Infobox", startLoc) != -1){
				
				ctr = 2;
				
				for(i = data.indexOf("{{Infobox", startLoc) + 9 ; ctr != 0 && i < textLength; i++){
					
					if(data.charAt(i) == '{')
						ctr++;
					else if(data.charAt(i) == '}')
						ctr--;
					
					infoBoxText += data.charAt(i);
					
				}
				startLoc = i - 1;
			}
			//System.out.println("InfoBoxText : " + infoBoxText + " " + data.length());
			
			
			// TODO just retrived the text from the category, tokenization is pending
			
			/*	Category Text
			
				pattern : [[Category:1970 births]]
			
			
			
			String categoryPattern = "[[Category:";
			String categoryText = "";
			int currentIndex = 0;
			startLoc = -1;
			
			while((currentIndex = data.indexOf(categoryPattern, startLoc + 1)) != -1){
				
				categoryText+=(data.substring(currentIndex + 11,
									(startLoc = data.indexOf("]]", currentIndex + 10)))) + "\n";				
			}
			
			//System.out.println(categoryText);
			
			
			
			
			/*String pattern = "is|are|of|i|was";
			
			String text =  "is are of i was";
			
			Pattern r = Pattern.compile(pattern);
			Matcher mm = r.matcher(text.trim());
			
			System.out.println("#####################################");
			
			if(mm.find())
			{
				System.out.println("Found");
			}	
			System.out.println("#####################################");
			
			
			
			
			
			
			 * Patterns  -> * , (space) ( ) [ ] | { } " ' 
			 * 
			 
			Satyam:|is|mad
			
			String[]tokens = data.split(" |[(]|[)]|[{]|[}]|\"|[|]|[.]|\'|[;]|[,]|[-]|[=]");
					
			FileWriter fw = null;
			
			try {
				
				fw = new FileWriter("out.txt", true);
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
			for(int i = 0; i < tokens.length; i++){
				
				try {
					
					Matcher m = r.matcher(tokens[i].trim());
					
					if(!tokens[i].isEmpty() && !m.find())
					{
						//System.out.println("stop word : " + m.find());
						fw.write("ID : " + pID + " " + tokens[i].toLowerCase() + "\n");
					}
					
				} catch (IOException e) {
					
					e.printStackTrace();
				}
				//System.out.println(tokens[i]);
			}
			
			System.out.println("Total words : " + tokens.length);
*/			long endTime = System.nanoTime();		
			
			//System.out.println("Tokenization completed... "  + (endTime - startTime));
			
		}
}


