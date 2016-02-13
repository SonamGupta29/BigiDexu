import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

class Tokenize{
	
	 	// Stops words list + other words which are kind of meta tags in the wiki dump
	 	
	 	HashSet<String> stopWordsList = new HashSet<String>(Arrays.asList("a","again","about","above","across","after","against","all","almost","alone",
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
							
							//This are the words which are not used in the text, these words are meta-data
							
							"refbegin","reflist","isbn",";",".","'","|","jpg","png","[","]","br","gt","&","lt","&gt","&lt","htm","en","php","isbn","svg",
							"yes","wikitext","wiki","faq","edu","html","net","org","<",">","ref","refs","cite","pdf","url","web","link","abbreviation",
							"id","caption","page","index","aspx","id","file","thumb","alt","thumbnail","defaultsort","abbr","redirect"
	 			));
	 	
	 	Map<String, Integer> occurrences = new HashMap<String, Integer>();
 		stemmer s = new stemmer();
	 	
	 	public void tokenize(String pID, String data) throws Exception
	 	{
	 		int dataLen = data.length();
			char ch;
			StringBuilder infoboxText = new StringBuilder();
			StringBuilder otherText = new StringBuilder();
			StringBuilder categoryText = new StringBuilder();
			StringBuilder referenceText = new StringBuilder();
			StringBuilder linkText = new StringBuilder();
			
			for(int i = 0; i < dataLen; i++)
			{
				ch = data.charAt(i);
				
				if(ch == '<')
				{
					if(i + 5 < dataLen &&  (data.substring(i + 1, i + 5).equalsIgnoreCase(" ref")))
					{
						i+=50;
						while(i + 1 < dataLen && data.charAt(++i) != '>');
					}
				}
				else if(ch == '{')
				{
	    			if(i + 10 < dataLen && (data.substring(i + 1, i + 9).equalsIgnoreCase("{Infobox") ||
	    									data.substring(i + 1, i + 9).equalsIgnoreCase("{infobox")))
	    			{
	    				int ctr = 2;
	    				i = i + 9;
	    				
	    				String temp = infoboxText.toString().toLowerCase().trim();
						if(!temp.isEmpty() && !stopWordsList.contains(temp) && temp.length() > 1)
						{
							s.add(temp.toCharArray(), temp.length());
							temp = s.stem().toString();
							
							if(temp.length() > 2 && temp.length() < 12)
							{
								addToTree(temp, "infobox", pID);
							}	
						}
						infoboxText.setLength(0);
	    				
	    				while(ctr != 0 && i < dataLen)
	    				{
	    					switch((ch=data.charAt(i)))
	    					{
		    					case '{':
		    						ctr++;
		    						break;
		    					case '}':
		    						ctr--;
		    						if(ctr == 0)
		    						{
		    							temp = infoboxText.toString().toLowerCase().trim();
		    							if(!temp.isEmpty() && !stopWordsList.contains(temp) && temp.length() > 1)
		    							{
		    								s.add(temp.toCharArray(), temp.length());
		    								temp = s.stem().toString();
		    								if(temp.length() > 2 && temp.length() < 12)
		    								{	
		    									addToTree(temp, "infobox", pID);
		    								}
		    							}
		    							infoboxText.setLength(0);
		    						}
		    						break;
		    					default:
		    					{
		    						if((ch >= 65 && ch <= 90) || (ch >= 97 && ch <= 122))
		    						{
		    							infoboxText.append(ch);
		    						}
		    						else
		    						{
		    							temp = infoboxText.toString().toLowerCase().trim();
		    							if(!temp.isEmpty() && !stopWordsList.contains(temp)	&& temp.length() > 1)
		    							{
		    								s.add(temp.toCharArray(), temp.length());
		    								temp = s.stem().toString();
		    								if(temp.length() > 2 && temp.length() < 12)
		    								{
		    									addToTree(temp, "infobox", pID);
		    								}
		    							}
		    							infoboxText.setLength(0);
		    						}
		    						break;
		    					}
		    				}
	    					i++;
	    				}
	    				
	    			}
	    			else if(i + 7 < dataLen && (data.substring(i + 1, i + 6).equalsIgnoreCase("{Cite") ||
							 data.substring(i + 1, i + 6).equalsIgnoreCase("{cite"))){
	    				
	    				int ctr = 2;
	    				i = i + 6;
	    				//Extract the text here only
	    				while(ctr != 0 && i < dataLen){
	    					
	    					switch((ch=data.charAt(i)))
	    					{
		    					case '{':
		    						ctr++;
		    						break;
		    					case '}':
		    						ctr--;
		    						break;
		    					default:
		    						break;
	    					}
	    					i++;
	    				}
	    			}    			
	    		}
				else if(ch == '[')
				{
					//Category
					if(i + 12 < dataLen && (data.substring(i + 1, i + 11).equalsIgnoreCase("[Category:") ||
											data.substring(i + 1, i + 11).equalsIgnoreCase("[category:")))
					{
						//If more than one category then the next word should not get append to the previous one hence put one space
						String temp = categoryText.toString().toLowerCase().trim();
						int len = temp.length();
						if(!temp.isEmpty() && !stopWordsList.contains(temp)	&& len > 1)
						{
							s.add(temp.toCharArray(), len);
							temp = s.stem().toString();
							if(len > 2 && len < 12)
							{
								//otherTokens.add(temp);
								addToTree(temp, "category", pID);						
							}
						}
						categoryText.setLength(0);						
						int ctr = 2;
						i = i + 11;
	    				//Extract the text here only
	    				while(ctr != 0 && i < dataLen)
	    				{
	    					switch((ch=data.charAt(i)))
	    					{	
		    					case ']':
		    						ctr--;
		    						if(ctr == 0)
		    						{
		    							temp = categoryText.toString().toLowerCase().trim();
    									len = temp.length();
    									if(!temp.isEmpty() && !stopWordsList.contains(temp)	&& len > 1)
    									{
    										s.add(temp.toCharArray(), len);
    										temp = s.stem().toString();
    										if(len > 2 && len < 12)
    										{
    											//otherTokens.add(temp);
    											addToTree(temp, "category", pID);						
    										}
    									}
		    							categoryText.setLength(0);
		    						}
		    						break;
		    					default:
		    					{
		    						if((ch >= 65 && ch <= 90) || (ch >= 97 && ch <= 122))
		    						{
		    							categoryText.append(ch);
		    						}
		    						else
		    						{
    									temp = categoryText.toString().toLowerCase().trim();
    									len = temp.length();
    									if(!temp.isEmpty() && !stopWordsList.contains(temp)	&& len > 1)
    									{
    										s.add(temp.toCharArray(), len);
    										temp = s.stem().toString();
    										if(len > 2 && len < 12)
    										{
    											//otherTokens.add(temp);
    											addToTree(temp, "category", pID);						
    										}
    									}
		    							categoryText.setLength(0);
		    						}
		    						break;
		    					}
	    					}
	    					i++;
	    				}	
					}
																																		
					//Links in [------------]
					else if(i + 6 < dataLen && (data.substring(i + 1, i + 5).equalsIgnoreCase("[http")))
					{
						while(i < dataLen && data.charAt(i) != ']')
							i++;
					}
				}
				else if(ch == '=')
				{
					// may be reference or may be links we need to check 
					//References
					
					
					if(i + 17 < dataLen && (data.substring(i+1, i+14).equalsIgnoreCase("=References==") ||
											data.substring(i+1, i+16).equalsIgnoreCase("= References ==")))
					{
						//If more than one reference then the next word should not get append to the previous one hence put one space
						
						String temp = referenceText.toString().toLowerCase().trim();
						int len = temp.length();
						if(!temp.isEmpty() && !stopWordsList.contains(temp)	&& len > 1)
						{
							s.add(temp.toCharArray(), len);
							temp = s.stem().toString();
							if(len > 2 && len < 12)
							{
								//otherTokens.add(temp);
								addToTree(temp, "references", pID);						
							}
						}
						referenceText.setLength(0);
						
						int ctr = 2;
	    				i = i + 14;
	    				
						while(i + 2 < dataLen && !data.substring(i, i + 2).equals("{{"))
						{
							i++;
						}
						i = i + 2;
	    				
	    				while(ctr != 0 && i < dataLen)
	    				{
	    					switch((ch=data.charAt(i)))
	    					{	
		    					case '{':
		    						ctr++;
		    						break;
		    					case '}':
		    						ctr--;
		    						if(ctr == 0)
		    						{
    									temp = referenceText.toString().toLowerCase().trim();
    									len = temp.length();
    									if(!temp.isEmpty() && !stopWordsList.contains(temp)	&& len > 1)
    									{
    										s.add(temp.toCharArray(), len);
    										temp = s.stem().toString();
    										if(len > 2 && len < 12)
    										{
    											//otherTokens.add(temp);
    											addToTree(temp, "references", pID);						
    										}
    									}
		    							referenceText.setLength(0);
		    						}
		    						break;
		    					default:
		    					{
		    						if((ch >= 65 && ch <= 90) || (ch >= 97 && ch <= 122))
		    						{
		    							referenceText.append(ch);
		    						}
		    						else
		    						{
		    							temp = referenceText.toString().toLowerCase().trim();
		    							len = temp.length();
		    							if(!temp.isEmpty() && !stopWordsList.contains(temp)	&& len > 1)
		    							{
		    								s.add(temp.toCharArray(), len);
		    								temp = s.stem().toString();
		    								if(len > 2 && len < 12)
		    								{
		    									//otherTokens.add(temp);
		    									addToTree(temp, "references", pID);						
		    								}
		    							}
		    							referenceText.setLength(0);
		    						}
		    						break;
		    					}
	    					}
	    					i++;
	    				}	
					}
					else if(i + 21 < dataLen && (data.substring(i+1, i+18).equalsIgnoreCase("=External links==") ||
							data.substring(i+1, i+20).equalsIgnoreCase("= External links ==")))
					{
						String temp = linkText.toString().toLowerCase().trim();
						int len = temp.length();
						if(!temp.isEmpty() && !stopWordsList.contains(temp)	&& len > 1)
						{
							s.add(temp.toCharArray(), len);
							temp = s.stem().toString();
							if(len > 2 && len < 12)
							{
								//otherTokens.add(temp);
								addToTree(temp, "externalLinks", pID);						
							}
						}
						linkText.setLength(0);
						i = i + 18;
						char lastchar = ' ';
						
						while(i < dataLen){
							if((ch >= 65 && ch <= 90) || (ch >= 97 && ch <= 122))
							{
								linkText.append(ch);
    						}
							else
    						{
    							temp = linkText.toString().toLowerCase().trim();
    							len = temp.length();
    							if(!temp.isEmpty() && !stopWordsList.contains(temp)	&& len > 1)
    							{
    								s.add(temp.toCharArray(), len);
    								temp = s.stem().toString();
    								if(len > 2 && len < 12)
    								{
    									//linkTokens.add(temp);
    									addToTree(temp, "externalLinks", pID);						
    								}
    							}
    							linkText.setLength(0);
    						}
							if(++i < dataLen){
															
								lastchar = ch;
								ch = data.charAt(i);
								if(lastchar == '\n' && ch != '*'){
									break;
								}
							}
						}
					}
					else
					{
						if((ch >= 65 && ch <= 90) || (ch >= 97 && ch <= 122))
						{
							otherText.append(ch);
						}
						else
						{
							String temp = otherText.toString().toLowerCase().trim();
							int len = temp.length();
							if(!temp.isEmpty() && !stopWordsList.contains(temp)	&& len > 1)
							{
								s.add(temp.toCharArray(), len);
								temp = s.stem().toString();
								if(len > 2 && len < 12)
								{
									//otherTokens.add(temp);
									addToTree(temp, "otherText", pID);						
								}
							}
						}
					}					
				}
				else
				{
					if((ch >= 65 && ch <= 90) || (ch >= 97 && ch <= 122))
					{
						otherText.append(ch);
					}
					else
					{
						String temp = otherText.toString().toLowerCase().trim();
						int len = temp.length();
						if(!temp.isEmpty() && !stopWordsList.contains(temp)	&& len > 1)
						{
							s.add(temp.toCharArray(), len);
							temp = s.stem().toString();
							if(len > 2 && len < 12)
							{
								//otherTokens.add(temp);
								addToTree(temp, "otherText", pID);						
							}
						}
						otherText.setLength(0);
					}
				}
			}
			/*
			 *	Finally add the title to the map 
			 */
			
			String titleText = UserHandler.pageTitleText;
			WikiParse.pIDToTitle.put(pID, titleText);
		
			int titleLen = titleText.length();
			StringBuilder titleToken = new StringBuilder();
			for(int i = 0; i < titleLen; i++)
			{
				ch = titleText.charAt(i);
				if(ch == ' ' || i == titleLen)
				{
					addToTree(titleToken.toString().toLowerCase(), "title", pID);
					titleToken.setLength(0);
				}
				else 
				{
					if((ch >= 65 && ch <= 90) || (ch >= 97 && ch <= 122))
					{
						titleToken.append(ch);
					}
					else
					{
						addToTree(titleToken.toString().toLowerCase(), "title", pID);
						titleToken.setLength(0);
					}
				}
			}
		}
	 	
 	public static void addToTree(String word, String extractType, String pID)
 	{
 		int optionIndex = -1;
 		if(word.trim().length()<=2)
 			return;
 		
 		//word = word.toLowerCase();
 		
 		switch(extractType)
 		{
 			case "title":
				optionIndex = 0;
				break;
 			case "infobox":
 				optionIndex = 1;
 				break;
 			case "externalLinks":
 				optionIndex = 2;
 				break;	
 			case "references":
 				optionIndex = 3;
 				break;	
 			case "category":
 				optionIndex = 4;
 				break;
 			case "otherText":
 				optionIndex = 5;
 				break;
 		}
 		
 		//Check for the word
		if(WikiParse.iIndex.containsKey(word))
		{
			//Check for the pID
			if(WikiParse.iIndex.get(word).containsKey(pID))
			{
				//Check for the category like "infobox", etc
				if(WikiParse.iIndex.get(word).get(pID).containsKey(optionIndex))
				{
					WikiParse.iIndex.get(word).get(pID).put(optionIndex, WikiParse.iIndex.get(word).get(pID).get(optionIndex) + 1);
				}
				else
				{
					// First create Inner hashmap as the key is not present 
					WikiParse.iIndex.get(word).get(pID).put(optionIndex, 1);
				}
			}
			//Page no does not exists
			else
			{
				// First create Inner hashmap as the key is not present 
				HashMap<Integer, Integer> intCount = new HashMap<Integer, Integer>();
				
				//Add the word in the index
				intCount.put(optionIndex, 1);
				
				//Add to the hashmap
				WikiParse.iIndex.get(word).put(pID, intCount);		
			}
		}
		else
		{
			// First create Inner hashmap as the key is not present 
			HashMap<Integer, Integer> intCount = new HashMap<Integer, Integer>();
			
			//Add the word in the index
			intCount.put(optionIndex, 1);
			
			//	Then create a outer map which will store the document ID 
			HashMap<String, HashMap<Integer, Integer>> intIdMap = new HashMap<String, HashMap<Integer, Integer>>();
			intIdMap.put(pID, intCount);
			
			//Now outermost hashmap 
			WikiParse.iIndex.put(word, intIdMap);		    									
		}
 	}
 }