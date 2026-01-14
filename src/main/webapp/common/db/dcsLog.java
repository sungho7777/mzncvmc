package com.neostringinfo.springBootService.DCS;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Controller;

@Controller
public class LogAnalyzeController {

	static int pNo = 0;
	static String dwsServer = "dws2";
	static String nowLocalDate = "2025-11-12";
	public static void main(String[] args) {
		String[] logFileArr = {
				"C:\\dev_workspace\\DCS\\springBootService\\file\\"+dwsServer+"_2025-12-02_server.log"
				//, "C:\\dev_workspace\\DCS\\springBootService\\file\\"+dwsServer+"_2025-10-29_server.log"
				//, "C:\\dev_workspace\\DCS\\springBootService\\file\\"+dwsServer+"_2025-10-30_server.log"
				//, "C:\\dev_workspace\\DCS\\springBootService\\file\\"+dwsServer+"_2025-10-31_server.log"
		};
        LocalTime nowLocalTime = LocalTime.now();
        String formattedNowTime = String.format("%02d%02d%02d", nowLocalTime.getHour(), nowLocalTime.getMinute(), nowLocalTime.getSecond());
		String saveFilePath = dwsServer+"_WEBLOG_" + ( nowLocalDate + "_" + formattedNowTime)+".txt";


		int[] processYnArr =    {
				0	// 0 Server Stop	: DWS WEB_APP_SERVER STOP
				, 0	// 1 Server Start	: DWS WEB_APP_SERVER START
				, 0 // DCS System

				, 0 // input R no
				, 0 // output R no
				
				, 0 // error
		};
		
		

		System.out.println("NO|DATE|TIME|RandomNo|UserId|HSB|ScreenId|URL|Action|IP|TYPE");

		BufferedWriter writer = null;
		try{
			writer = new BufferedWriter(new FileWriter(saveFilePath, true));
			
			String logMsg = "";
			
			for (int i = 0; i < logFileArr.length; i++) {
				try(BufferedReader br = new BufferedReader(new FileReader(logFileArr[i]))){
					String line;
					while((line = br.readLine()) != null) {
						
						// Server Stop
						if(processYnArr[0] < 1) {
							logMsg = stopServerLogMsg(line, " DWS WEB_APP_SERVER STOP ");
							if(!logMsg.isEmpty()) writer.write(logMsg + System.lineSeparator());
							
							
						}

						// Server Start
						if(processYnArr[1] < 1) {
							logMsg = startServerLogMsg(line, " DWS WEB_APP_SERVER START ");
							if(!logMsg.isEmpty()) writer.write(logMsg + System.lineSeparator());
						}
						
						// DCS System
						if(processYnArr[2] < 1) {
							logMsg = dcsSystemLogMsg(line, " DCS System");
							if(!logMsg.isEmpty()) writer.write(logMsg + System.lineSeparator());
						}

						
						// input R no
						if(processYnArr[3] < 1) {
							logMsg = dataInputRLogMsg(line, "<input:R", "ScreenProcessController");
							if(!logMsg.isEmpty()) writer.write(logMsg + System.lineSeparator());
						}
						
						// output R no
						if(processYnArr[4] < 1) {
							logMsg = dataOutputRLogMsg(line, "<output:R", "ScreenProcessController");
							if(!logMsg.isEmpty()) writer.write(logMsg + System.lineSeparator());
						}
						
						
						


						// UT005023 : Exception handling request to
						//	- 해당 워크플로를 트리거 하기 위해 rest API 를 싱행 하는 동안 오류가 발생

						// ISPN000136: Error executing command GetKeyValueCommand
						// ISPN000299: Unable to acquire lock after 60 seconds for key SessionCreationMetaDataKey
						//	- EJB 사양에 따르면 동시 액세스르 허용하지 않습니다.
						//	- 호출이 감기고 동시 호출은 시간 초과까지 잠금을 기다립니다.
						//	- 
						if(processYnArr[5] < 1) {
							logMsg = errorExecutingCommandGetKeyValueCommandLogMsg(line, " ERROR ", "UT005023: Exception handling request to", "ISPN000299: Unable to acquire lock after 60 seconds for key SessionCreationMetaDataKey");
							if(!logMsg.isEmpty()) writer.write(logMsg + System.lineSeparator());
						}
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		System.out.println("END");
		return ;
	}
	
	

	public static String stopServerLogMsg(String line, String logMsg) {
		Pattern pattern = Pattern.compile(logMsg);
		Matcher matcher = pattern.matcher(line);

		String result = "";
		String logText = "";
		String[] logTextArr = null;
		if(matcher.find()) {
			logText = (++pNo) + " " + line;
			logText = logText.replaceAll("  ", " ").replaceAll(" ", "|");
			logTextArr = logText.split("\\|");

			String num = logTextArr[0];
			String date = logTextArr[1];
			String time = logTextArr[2];
			
			result = num + "|" + date + "|" + time + "|" + dwsServer + "|DWS||||||WEB_APP_SERVER_STOP";
			System.out.println(result);

			return result;
		}
		
		return result;
	}
	public static String startServerLogMsg(String line, String logMsg) {
		Pattern pattern = Pattern.compile(logMsg);
		Matcher matcher = pattern.matcher(line);

		String result = "";
		String logText = "";
		String[] logTextArr = null;
		if(matcher.find()) {
			logText = (++pNo) + " " + line;
			logText = logText.replaceAll("  ", " ").replaceAll(" ", "|");
			logTextArr = logText.split("\\|");

			String num = logTextArr[0];
			String date = logTextArr[1];
			String time = logTextArr[2];
			
			result = num + "|" + date + "|" + time + "|" + dwsServer + "|DWS||||||WEB_APP_SERVER_START";
			System.out.println(result);

			return result;
		}
		
		return result;
	}
	public static String dcsSystemLogMsg(String line, String logMsg) {
		Pattern pattern = Pattern.compile(logMsg);
		Matcher matcher = pattern.matcher(line);

		String result = "";
		String logText = "";
		String[] logTextArr = null;
		if(matcher.find()) {
			logText = line;
			logTextArr = logText.split("DCS System\\|");
			result = logTextArr.length > 1 ? logTextArr[1] : "";

			logText = (++pNo) + " " + line;
			logText = logText.replaceAll("  ", " ").replaceAll(" ", "|");
			logTextArr = logText.split("\\|");
			String num = logTextArr[0];
			String date = logTextArr[1];
			String time = logTextArr[2];
			
			result = num + "|" + date + "|" + time + "|" + result;
			
			System.out.println(result);
			return result;
		}
		
		return result;
	}

	private static String dataInputRLogMsg(String line, String logMsg1, String logMsg2) {
		Pattern pattern1 = Pattern.compile(logMsg1);
		Pattern pattern2 = Pattern.compile(logMsg2);

		Matcher matcher1 = pattern1.matcher(line);
		Matcher matcher2 = pattern2.matcher(line);

		String result = "";
		String logText = "";
		String[] logTextArr = null;
		if(matcher1.find() && matcher2.find()) {
			logText = line;
			logTextArr = logText.split("\\<input\\:R");
			logTextArr = logTextArr[1].split("\\> base");

			result = logTextArr.length > 1 ? "R"+logTextArr[0] : "";
			
			
			

			logText = (++pNo) + " " + line;
			logText = logText.replaceAll("  ", " ").replaceAll(" ", "|");
			logTextArr = logText.split("\\|");
			String num = logTextArr[0];
			String date = logTextArr[1];
			String time = logTextArr[2];
			
			result = num + "|" + date + "|" + time + "|" + result + "|||||||INPUT_DATA";
			
			System.out.println(result);
			return result;
		}
		
		return result;
	}	
	private static String dataOutputRLogMsg(String line, String logMsg1, String logMsg2) {
		Pattern pattern1 = Pattern.compile(logMsg1);
		Pattern pattern2 = Pattern.compile(logMsg2);

		Matcher matcher1 = pattern1.matcher(line);
		Matcher matcher2 = pattern2.matcher(line);

		String result = "";
		String logText = "";
		String[] logTextArr = null;
		if(matcher1.find() && matcher2.find()) {
			logText = line;
			logTextArr = logText.split("\\<output\\:R");
			logTextArr = logTextArr[1].split("\\> outputHeader");

			result = logTextArr.length > 1 ? "R"+logTextArr[0] : "";
			
			
			

			logText = (++pNo) + " " + line;
			logText = logText.replaceAll("  ", " ").replaceAll(" ", "|");
			logTextArr = logText.split("\\|");
			String num = logTextArr[0];
			String date = logTextArr[1];
			String time = logTextArr[2];
			
			result = num + "|" + date + "|" + time + "|" + result + "|||||||OUTPUT_DATA";
			
			System.out.println(result);
			return result;
		}
		
		return result;
	}	
	

	private static String errorExecutingCommandGetKeyValueCommandLogMsg(String line, String logMsg1, String logMsg2, String logMsg3) {
		Pattern pattern1 = Pattern.compile(logMsg1);
		Pattern pattern2 = Pattern.compile(logMsg2);
		Pattern pattern3 = Pattern.compile(logMsg3);

		Matcher matcher1 = pattern1.matcher(line);
		Matcher matcher2 = pattern2.matcher(line);
		Matcher matcher3 = pattern3.matcher(line);

		String result = "";
		String logText = "";
		String[] logTextArr = null;
		if(matcher1.find() && matcher2.find() && matcher3.find()) {
			logText = line;
			logTextArr = logText.split("SessionCreationMetaDataKey");
			logTextArr = logTextArr[1].replaceAll("\\(", "").split("\\) and requestor GlobalTransaction");

			result = logTextArr.length > 1 ? logTextArr[0] : "";

			logText = (++pNo) + " " + line;
			logText = logText.replaceAll("  ", " ").replaceAll(" ", "|");
			logTextArr = logText.split("\\|");
			String num = logTextArr[0];
			String date = logTextArr[1];
			String time = logTextArr[2];
			
			result = num + "|" + date + "|" + time + "|||" + result + "|||||ERR_LOCK";
			
			System.out.println(result);
			return result;
			
			
			
			
			
			
			
			


		}
		
		return result;
	}
}
