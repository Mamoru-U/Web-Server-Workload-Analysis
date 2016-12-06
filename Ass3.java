//import java.io.*;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.*;

public class Ass3{
     
     public static void main(String[] args){
          try{
               FileReader fileIn = new FileReader(new File ("UofS_access_log"));
               BufferedReader sc = new BufferedReader(fileIn);
               
               File fileOut = new File ("results.txt");
               PrintWriter pW = new PrintWriter (fileOut);
               
               //PrintWriter pWW = new PrintWriter (new File ("resultss.txt"));
               
               String currentDate = "";
               int numRequest = 0;
               int numDays = 0;
               double localSuccessBytes = 0;
               double remoteSuccessBytes = 0;
               double notSuccessBytes = 0;
               
               int successful = 0;
               int notModified = 0;
               int found = 0;
               int unsuccessful = 0;
               
               int numLocal = 0;
               int numRemote = 0;
               
//different File being organized
               String[] html = {"html", "htm", "shtml", "map"};
               int numHtml = 0;
               double numHtmlBytes = 0;
               String[] images = {"gif", "jpeg", "jpg", "xbm", "bmp", "rgb", "xpm"};
               int numImages = 0;
               double numImagesBytes = 0;
               String[] sound = {"au", "snd", "wav", "mid", "midi", "lha", "aif", "aiff"};
               int numSound = 0;
               double numSoundBytes = 0;
               String[] video = {"mov", "movie", "avi", "qt", "mpeg", "mpg"};
               int numVideo = 0;
               double numVideoBytes = 0;
               String[] formatted = {"ps", "eps", "doc", "dvi", "txt"};
               int numFormatted = 0;
               double numFormattedBytes = 0;
               String[] dynamic = {"cgi", "pl", "cgi-bin"};
               int numDynamic = 0;
               double numDynamicBytes = 0;
               int numOthers = 0;
               double numOthersBytes = 0;
               
               ArrayList<String> objectUnique = new ArrayList<String>();
               ArrayList<Integer> objectUniqueSize = new ArrayList<Integer>();
               ArrayList<String> objectNotUnique = new ArrayList<String>();
               ArrayList<Integer> objectNotUniqueSize = new ArrayList<Integer>();
               
               double totalObjectUniqueSize = 0;
               double totalObjectNotUniqueSize = 0;
               String log = "";
               while ((log = sc.readLine()) != null){
                    String[] splitLog = log.split("\\s+");
                    
//check if that Line has more than 9 split String.
//If false then skip the Line.
                    if (splitLog.length > 9){
                         int datePosTest = 3;
                         boolean test1 = true;
                         while (test1){
//check if the Dates and time are in the right position.
                              if (splitLog[datePosTest].charAt(0) == '['){
                                   if (!(splitLog[datePosTest + 1].charAt(splitLog[datePosTest + 1].length() - 1) == ']')){
                                        System.out.println(log);
                                   }
                                   test1 = false;
                              }else{
                                   datePosTest++;
                              }
                         }
                    }
                    
                    numRequest++;
                    int datePos = 3;
                    String today = "";
                    boolean test2 = true;
//Get the position of the Date;
                    while (test2){
                         if(splitLog[datePos].charAt(0) == '['){
                              today = splitLog[datePos].substring(1, 12);
                              test2 = false;
                         }else{
                              datePos++;
                         }
                    }
                    
//get the total amount of days the System has been checking
                    if (!(today.equals(currentDate))){
                         currentDate = today;
                         //System.out.println(currentDate);
                         numDays++;
                    }
                    
                    int respCode = Integer.parseInt(splitLog[splitLog.length - 2]);
                    
                    int tranSize = 0;
                    if (!(splitLog[splitLog.length - 1].equals("-"))){
                         tranSize = Integer.parseInt(splitLog[splitLog.length - 1]);
                    }
                    
//Checking which responce the server made
//Unsuccessful responce
                    if (respCode >= 400){
                         unsuccessful++;
                         notSuccessBytes += tranSize;
                    }else{
//Successful responce
                         if (respCode == 200){
                              successful++;
                              
//Set What category of files it belongs to
                              if (splitLog[0].contains("usask.ca") || splitLog[0].contains("128.233.")){
                                   numLocal++;
                                   localSuccessBytes += tranSize;
                              }else{
                                   numRemote++;
                                   remoteSuccessBytes += tranSize;
                              }
                              
                              String preSubLog = splitLog[datePos + 3].substring((splitLog[datePos + 3].lastIndexOf("/")));
                              if (splitLog[datePos + 3].endsWith("\"")){
                                   preSubLog = preSubLog.substring(0, preSubLog.length() - 1);
                              }
                              
                              if (preSubLog.endsWith("/") || preSubLog.endsWith(".") || preSubLog.endsWith("..")){
                                   numHtml++;
                                   numHtmlBytes += tranSize;
                              }else if (preSubLog.contains("?")){
                                   numDynamic++;
                                   numDynamicBytes += tranSize;
                              }else if (preSubLog.contains(".")){
                                   String subLog = preSubLog.substring((preSubLog.lastIndexOf(".") + 1));
                                   subLog = subLog.toLowerCase();
                                   
                                   if (Arrays.asList(html).contains(subLog)){
                                        numHtml++;
                                        numHtmlBytes += tranSize;
                                   }else if (Arrays.asList(images).contains(subLog)){
                                        numImages++;
                                        numImagesBytes += tranSize;
                                   }else if (Arrays.asList(sound).contains(subLog)){
                                        numSound++;
                                        numSoundBytes += tranSize;
                                   }else if (Arrays.asList(video).contains(subLog)){
                                        numVideo++;
                                        numVideoBytes += tranSize;
                                   }else if (Arrays.asList(formatted).contains(subLog)){
                                        numFormatted++;
                                        numFormattedBytes += tranSize;
                                   }else if (Arrays.asList(dynamic).contains(subLog)){
                                        numDynamic++;
                                        numDynamicBytes += tranSize;
                                   }else{
                                        numOthers++;
                                        numOthersBytes += tranSize;
                                   }
                              }else{
                                   numOthers++;
                                   numOthersBytes += tranSize;
                                   //pWW.println(splitLog[datePos + 3]);
                              }
                              
//Check if the File/Object is Unique(only called once) or not.
                              if (!(objectNotUnique.contains(splitLog[datePos + 3].toLowerCase()))){
                                   if (objectUnique.contains(splitLog[datePos + 3].toLowerCase())){
                                        objectNotUnique.add(splitLog[datePos + 3].toLowerCase());
                                        objectNotUniqueSize.add(tranSize);
                                        int position = objectUnique.indexOf(splitLog[datePos + 3].toLowerCase());
                                        
                                        objectUnique.remove(position);
                                        objectUniqueSize.remove(position);
                                   }else{
                                        objectUnique.add(splitLog[datePos + 3].toLowerCase());
                                        objectUniqueSize.add(tranSize);
                                   }
                              }
                              
//Not modified responce
                         }else if (respCode == 304){
                              notModified++;
                              notSuccessBytes += tranSize;
//Found responce
                         }else if (respCode == 302){
                              found++;
                              notSuccessBytes += tranSize;
                         }
                    }
               }
               
//Calculating the total Size of Unique Objects
               for (int i = 0; i < objectUniqueSize.size(); i++){
                    totalObjectUniqueSize += objectUniqueSize.get(i);
               }
               
//Calculating the total Size of Not Unique Objects
               for (int i = 0; i < objectNotUniqueSize.size(); i++){
                    totalObjectNotUniqueSize += objectNotUniqueSize.get(i);
               }
               
               double successBytes = notSuccessBytes + localSuccessBytes + remoteSuccessBytes;
               
               
//Initializing the Print Method to show result.
               pW.println("1. Requests Per Day: " + String.format("%.2f", (numRequest*1.0)/numDays));
               
               pW.println("\n2. Total Bytes Transferred(in MB): " + String.format("%.2f", successBytes/1000000) + " MB");
               
               pW.println("\n3. Average Bytes Per day(in MB): " + String.format("%.2f", (successBytes/1000000)/numDays) + " MB");
               
               pW.println("\n4. Various Responses Breakdown :");
               pW.println("Found  : " + String.format("%.2f", (found*100.0)/numRequest) + " %");
               pW.println("Not Modified  : " + String.format("%.2f", (notModified*100.0)/numRequest) + " %");
               pW.println("Successful  : " + String.format("%.2f", (successful*100.0)/numRequest) + " %");
               pW.println("Unsuccessful : " + String.format("%.2f", (unsuccessful*100.0)/numRequest) + " %");
               
               pW.println("\n\nHost Wise Distribution of Requests and Bytes Transferred:");
               
               pW.println("\n5. Number of Requests:");
               pW.println("local : " + String.format("%.2f", (numLocal*100.0)/(numLocal + numRemote)) + " %");
               pW.println("remote : " + String.format("%.2f", (numRemote*100.0)/(numLocal + numRemote)) + " %");
               
               pW.println("\n6. Bytes Transferred:");
               pW.println("local : " + String.format("%.2f", (localSuccessBytes*100)/(localSuccessBytes + remoteSuccessBytes)) + " %");
               pW.println("remote : " + String.format("%.2f", (remoteSuccessBytes*100)/(localSuccessBytes + remoteSuccessBytes)) + " %");
               
               pW.println("\n\nFile Category Wise Distribution :");
               
               int totalNumRequest = numHtml + numImages + numSound + numVideo + numFormatted + numDynamic + numOthers;
               
               pW.println("\n7. Number of Request Distribution :");
               pW.println("Sound : " + String.format("%.2f", (numSound*100.0)/totalNumRequest) + " %");
               pW.println("Formatted : " + String.format("%.2f", (numFormatted*100.0)/totalNumRequest) + " %");
               pW.println("Others : " + String.format("%.2f", (numOthers*100.0)/totalNumRequest) + " %");
               pW.println("Images : " + String.format("%.2f", (numImages*100.0)/totalNumRequest) + " %");
               pW.println("HTML : " + String.format("%.2f", (numHtml*100.0)/totalNumRequest) + " %");
               pW.println("Dynamic : " + String.format("%.2f", (numDynamic*100.0)/totalNumRequest) + " %");
               pW.println("Video : " + String.format("%.2f", (numVideo*100.0)/totalNumRequest) + " %");
               
               double totalNumBytesRequest = numHtmlBytes + numImagesBytes + numSoundBytes + numVideoBytes + numFormattedBytes + numDynamicBytes + numOthersBytes;
               
               pW.println("\n8. Bytes Transferred Distribution :");
               pW.println("Sound : " + String.format("%.2f", (numSoundBytes*100)/totalNumBytesRequest) + " %");
               pW.println("Formatted : " + String.format("%.2f", (numFormattedBytes*100)/totalNumBytesRequest) + " %");
               pW.println("Others : " + String.format("%.2f", (numOthersBytes*100)/totalNumBytesRequest) + " %");
               pW.println("Images : " + String.format("%.2f", (numImagesBytes*100)/totalNumBytesRequest) + " %");
               pW.println("HTML : " + String.format("%.2f", (numHtmlBytes*100)/totalNumBytesRequest) + " %");
               pW.println("Dynamic : " + String.format("%.2f", (numDynamicBytes*100)/totalNumBytesRequest) + " %");
               pW.println("Video : " + String.format("%.2f", (numVideoBytes*100)/totalNumBytesRequest) + " %");
               
               pW.println("\n9. Mean Average Size for Different Categories:");
               pW.println("Sound : " + String.format("%.2f", (numSoundBytes)/numSound) + " bytes");
               pW.println("Formatted : " + String.format("%.2f", (numFormattedBytes)/numFormatted) + " bytes");
               pW.println("Others : " + String.format("%.2f", (numOthersBytes)/numOthers) + " bytes");
               pW.println("Images : " + String.format("%.2f", (numImagesBytes)/numImages) + " bytes");
               pW.println("HTML : " + String.format("%.2f", (numHtmlBytes)/numHtml) + " bytes");
               pW.println("Dynamic : " + String.format("%.2f", (numDynamicBytes)/numDynamic) + " bytes");
               pW.println("Video : " + String.format("%.2f", (numVideoBytes)/numVideo) + " bytes");
               
               pW.println("\n10. Unique Files accessed once: " + String.format("%.2f", (objectUnique.size()*100.0) / (objectUnique.size() + objectNotUnique.size())) + " %");
               pW.println("Unique Bytes accessed once: " + String.format("%.2f", (totalObjectUniqueSize*100) / (totalObjectNotUniqueSize + totalObjectUniqueSize)) + " %");
               
               sc.close ();
               pW.close ();
               //pWW.close ();
          }catch(Exception e){
               System.out.println(e);
          }
     }
}