package com.education.android.mathcounts;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.education.android.mathcounts.TestDatabaseHelper.CardCursor;

public class DropboxUploader {
	//Temporary api keys for development purposes
	private static final String APP_KEY = "ugz7chkom1kuenb";
    private static final String APP_SECRET = "hrkqjp3yj3wh2hg";
    
    //static to ensure only one instance of the uploader exists at a time
	private static DropboxUploader mUploader;
	
	private DbxAccountManager mAccountManager;
	private Context mContext;
	
	//private constructor which cannot be called directly; must use newInstance()
	private DropboxUploader(final Context c) {
		mContext = c;
		mAccountManager = DbxAccountManager.getInstance(c, APP_KEY, APP_SECRET);
	}
	
	//static for reasons mentioned above. called once in onCreate() of HomeActivity, which is the launcher activity
	public static DropboxUploader newInstance(Context c) {
		if(mUploader == null) {
			mUploader = new DropboxUploader(c);
		}
		return mUploader;
	}
	
	//get already created uploader
	public static DropboxUploader getInstance() {
		return mUploader;
	}
	
	//same as Dropbox implementation of method with same name
	public boolean hasLinkedAccount() {
		return mAccountManager.hasLinkedAccount();
	}
	
	//same as Dropbox implementation of method with same name
	public void startLink(Activity activity, int requestCode) {
		mAccountManager.startLink(activity, requestCode);
	}
	
	//upload results of saved test to Dropbox as .xls file
	public void uploadResults(long id, Context c) {
		if(!hasLinkedAccount()) return;
		
		//query SQLite database for test with given id
		Test test = TestManager.getTest(id, c);
		
		DbxFile testFile = null;
		DbxFile todayFile = null;
		//get filesystem, create path, set testFile to open file if exists, create file if not
		try {
			//filename will be username followed by date and time test was saved
			Date date = new Date();
			String filename = new SimpleDateFormat("h:mm:ss a").format(date);
			
			String month = new SimpleDateFormat("MMMM").format(date);
			String year = new SimpleDateFormat("yyyy").format(date);
			String day = new SimpleDateFormat("cccc").format(date);
			String number = ordinal(Integer.parseInt(new SimpleDateFormat("d").format(date)));
			
			DbxFileSystem dbxFs = DbxFileSystem.forAccount(mAccountManager.getLinkedAccount());
			DbxPath path = new DbxPath(month + " " + year + "/" + day + ", " + month + " " + number + "/" + (test.getUser().equals("") ? "No name" : test.getUser()) + "/" + test.getOperation() + " " + filename + ".xls");
			DbxPath todayPath = new DbxPath(month + " " + year + "/" + day + ", " + month + " " + number + "/" + "Today's Results" + ".xls");
			
			if(dbxFs.exists(path)) 
				testFile = dbxFs.open(path);
			else
				testFile = dbxFs.create(path);
			
			if(dbxFs.exists(todayPath))
				todayFile = dbxFs.open(todayPath);
			else
				todayFile = dbxFs.create(todayPath);
				
			if(todayFile == null || testFile == null) return;
			
			generateFile(todayFile, testFile, test, c);
		} catch(IOException e){}
		  finally {
			//close dbx file and upload to app folder on Dropbox

			if(todayFile == null || testFile == null) return;
			  
			testFile.close();
			todayFile.close();
			
			Toast.makeText(mContext, "Uploading to Dropbox", Toast.LENGTH_SHORT).show();
		}
	}
	
	//create temporary .xls file locally then make testFile from the temporary file
	private void generateFile(DbxFile dbxTodayFile, DbxFile dbxFile, Test test, Context c) {
		String operation = "";
		if(test.getOperation().equals(TestManager.ADDITION))
			operation = "+";
		else if(test.getOperation().equals(TestManager.SUBTRACTION))
			operation = "-";
		else if(test.getOperation().equals(TestManager.MULTIPLICATION))
			operation = "x";
		else if(test.getOperation().equals(TestManager.DIVISION))
			operation = "÷";
		

		ArrayList<Card> cardList = new ArrayList<Card>();
		
		//query SQLite database for questions corresponding to test with given id
		CardCursor cursor = TestManager.queryCardsForTest(test.getTestId(), c);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()) {
			cardList.add(cursor.getCard());
			cursor.moveToNext();
		}
		
		//create temporary file
		File file = new File(Environment.getExternalStorageDirectory() + 
				File.separator + "hi.xls");
		File todayFileOld = new File(Environment.getExternalStorageDirectory() + 
				File.separator + "hii.xls");
		File todayFileNew = new File(Environment.getExternalStorageDirectory() + 
				File.separator + "hiii.xls");
		
		try {
			todayFileOld.createNewFile();
			todayFileNew.createNewFile();
			file.createNewFile();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		InputStream inputStream;
		OutputStream outputStream;
		try {
			inputStream = dbxTodayFile.getReadStream();
			outputStream = new FileOutputStream(todayFileOld);
			
			int read = 0;
			byte[] bytes = new byte[1024];
	 
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}

			inputStream.close();
			outputStream.close();
		} catch (DbxException e3) {
			e3.printStackTrace();
		} catch (IOException e3) {
			e3.printStackTrace();
		}

		//use java excel api to create new excel workbook
		Workbook todayWorkbookOld = null;
		WritableWorkbook todayWorkbookNew = null;
		try {
			if(Workbook.getWorkbook(todayFileOld) != null) {
				todayWorkbookOld = Workbook.getWorkbook(todayFileOld);
			}
		} catch (BiffException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		try {
			todayWorkbookNew = Workbook.createWorkbook(todayFileNew);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		if(todayWorkbookNew == null) return;
		
		WritableSheet todaySheetNew = todayWorkbookNew.createSheet("First Sheet", 0);

		ArrayList<String[]> cellList = new ArrayList<String[]>();
		if(todayWorkbookOld != null) {
			Sheet todaySheetOld = todayWorkbookOld.getSheet(0);
			for(int j = 1; j < todaySheetOld.getRows(); j++){
				String[] cellArray = new String[7];
		         for(int i = 0; i < todaySheetOld.getColumns(); i++)  {            
			         if(todaySheetOld.getCell(i, j).getType() == CellType.LABEL) { 
			        	 cellArray[i] = todaySheetOld.getCell(i, j).getContents();
			         }               
		         }  
		         cellList.add(cellArray);
			} 
		}

		WritableWorkbook workbook = null;
		try {
			workbook = Workbook.createWorkbook(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(workbook == null) return;
		
		WritableSheet sheet = workbook.createSheet("First Sheet", 0);
		try {
		    int numCorrect = 0;
		    for(int j = 0; j < cardList.size(); j++) {
	    		if(String.valueOf(cardList.get(j).getAnswer()).equals(cardList.get(j).getStudentAnswer()))
	    			numCorrect++;   	
		    }

			WritableFont headerFont = new WritableFont(WritableFont.TIMES, 
					8, WritableFont.BOLD, false); 
			WritableCellFormat headerFormat = new WritableCellFormat(headerFont);
			headerFormat.setAlignment(Alignment.CENTRE);
			headerFormat.setWrap(true);
			
			//get attributes from test and put them in spreadsheet
			sheet.addCell(new Label(0, 0, "Name", headerFormat));
		    sheet.addCell(new Label(1, 0, "Score", headerFormat));
		    sheet.addCell(new Label(2, 0, "Difficulty Level", headerFormat));
		    sheet.addCell(new Label(3, 0, "Operation", headerFormat));
		    sheet.addCell(new Label(4, 0, "Time Allowed", headerFormat));
		    sheet.addCell(new Label(5, 0, "Time Remaining", headerFormat));
		    sheet.addCell(new Label(6, 0, "Quit?", headerFormat));
		    
			sheet.addCell(new Label(0, 3, "Question #", headerFormat));
			sheet.addCell(new Label(1, 3, "Question", headerFormat));
			sheet.addCell(new Label(2, 3, "Correct Answer", headerFormat));
			sheet.addCell(new Label(3, 3, "Student Answer", headerFormat));
			
			WritableFont font = new WritableFont(WritableFont.TIMES, 
					8, WritableFont.NO_BOLD, false); 
			WritableCellFormat format = new WritableCellFormat(font);
			format.setAlignment(Alignment.CENTRE);
			format.setWrap(true);

		    DecimalFormat df = new DecimalFormat("0.00");
   		    sheet.addCell(new Label(0, 1, test.getUser().equals("") ? "No name" : test.getUser(), format));
		    sheet.addCell(new Label(1, 1, numCorrect + "/" + cardList.size() + " (" +
		    		(df.format(( numCorrect / ((double) cardList.size())*100))) + "%)", format));
		    String difficulty;
		    if((test.getOperation().equals(TestManager.ADDITION) || test.getOperation().equals(TestManager.SUBTRACTION)) && 
		    		test.getDifficulty() == 1)
		    	difficulty = "Easy";
		    else if((test.getOperation().equals(TestManager.ADDITION) || test.getOperation().equals(TestManager.SUBTRACTION)) &&
		    		test.getDifficulty() == 2)
		    	difficulty = "Medium";
		    else if((test.getOperation().equals(TestManager.ADDITION) || test.getOperation().equals(TestManager.SUBTRACTION)) &&
		    		test.getDifficulty() == 3)
		    	difficulty = "Hard";
		    else if((test.getOperation().equals(TestManager.ADDITION) || test.getOperation().equals(TestManager.SUBTRACTION)) &&
		    		test.getDifficulty() == 4)
		    	difficulty = "Expert";
		    else if((test.getOperation().equals(TestManager.MULTIPLICATION) || test.getOperation().equals(TestManager.DIVISION)) &&
		    		test.getDifficulty() == 13)
		    	difficulty = "All";
		    else
		    	difficulty = "" + test.getDifficulty();
		    sheet.addCell(new Label(2, 1, difficulty, format));
		    sheet.addCell(new Label(3, 1, test.getOperation(), format));
		    sheet.addCell(new Label(4, 1, test.getTimeAllotted(), format));
		    sheet.addCell(new Label(5, 1, test.getTimeRemaining(), format));
		    sheet.addCell(new Label(6, 1, test.didQuitTest() ? "Yes" : "No", format));

			
			WritableFont dataFont = new WritableFont(WritableFont.TIMES, 
					8, WritableFont.NO_BOLD, false); 
			WritableCellFormat dataFormat = new WritableCellFormat(dataFont);
			dataFormat.setAlignment(Alignment.CENTRE);
			dataFormat.setWrap(true);
			
			WritableFont dataFontRed = new WritableFont(WritableFont.TIMES, 
					8, WritableFont.NO_BOLD, false); 
			dataFontRed.setColour(Colour.RED);
			
			WritableCellFormat dataFormatRed = new WritableCellFormat(dataFontRed);
			dataFormatRed.setAlignment(Alignment.CENTRE);
			dataFormatRed.setWrap(true);
		   
			//get attributes from individual test questions and put them in spreadsheet
			for(int i = 0; i < cardList.size(); i++) {
				Card card = cardList.get(i);
				
				String studentAns = card.getStudentAnswer().equals("") ? "—" : 
					card.getStudentAnswer();
				
				WritableCellFormat cellFormat = null;
				if(studentAns.equals("—") || Integer.parseInt(studentAns) != card.getAnswer()) {
					cellFormat = dataFormatRed;
				} else {
					cellFormat = dataFormat;
				}
				
				sheet.addCell(new Label(0, i + 4, "" + card.getQuestionNum(), cellFormat));
				sheet.addCell(new Label(1, i + 4, 
						card.getFirstNum() + " " + operation + " " + card.getSecondNum(),
						cellFormat));
				sheet.addCell(new Label(2, i + 4, "" + card.getAnswer(), cellFormat));
				sheet.addCell(new Label(3, i + 4, studentAns, cellFormat));
			}
			
		} catch (RowsExceededException e1) {
			e1.printStackTrace();
		} catch (WriteException e1) {
			e1.printStackTrace();
		} 
		
	    try {
	    	//write temporary file to local storage
		    workbook.write();
			workbook.close();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    try {
		    int numCorrect = 0;
		    for(int j = 0; j < cardList.size(); j++) {
	    		if(String.valueOf(cardList.get(j).getAnswer()).equals(cardList.get(j).getStudentAnswer()))
	    			numCorrect++;   	
		    }

			WritableFont headerFont = new WritableFont(WritableFont.TIMES, 
					8, WritableFont.BOLD, false); 
			WritableCellFormat headerFormat = new WritableCellFormat(headerFont);
			headerFormat.setAlignment(Alignment.CENTRE);
			headerFormat.setWrap(true);
			
			todaySheetNew.addCell(new Label(0, 0, "Name", headerFormat));
		    todaySheetNew.addCell(new Label(1, 0, "Score", headerFormat));
		    todaySheetNew.addCell(new Label(2, 0, "Difficulty Level", headerFormat));
		    todaySheetNew.addCell(new Label(3, 0, "Operation", headerFormat));
		    todaySheetNew.addCell(new Label(4, 0, "Time Allowed", headerFormat));
		    todaySheetNew.addCell(new Label(5, 0, "Time Remaining", headerFormat));
		    todaySheetNew.addCell(new Label(6, 0, "Quit?", headerFormat));
			
			WritableFont font = new WritableFont(WritableFont.TIMES, 
					8, WritableFont.NO_BOLD, false); 
			WritableCellFormat format = new WritableCellFormat(font);
			format.setAlignment(Alignment.CENTRE);
			format.setWrap(true);

		    DecimalFormat df = new DecimalFormat("0.00");
		    
			WritableFont dataFont = new WritableFont(WritableFont.TIMES, 
					8, WritableFont.NO_BOLD, false); 
			WritableCellFormat dataFormat = new WritableCellFormat(dataFont);
			dataFormat.setAlignment(Alignment.CENTRE);
			dataFormat.setWrap(true);
		    
			String[] cellArray = new String[7];
		    cellArray[0] = test.getUser().equals("") ? "No name" : test.getUser();
		    cellArray[1] = numCorrect + "/" + cardList.size() + " (" + (df.format(( numCorrect / ((double) cardList.size())*100))) + "%)";
		    if((test.getOperation().equals(TestManager.ADDITION) || test.getOperation().equals(TestManager.SUBTRACTION)) && 
		    		test.getDifficulty() == 1)
		    	cellArray[2] = "Easy";
		    else if((test.getOperation().equals(TestManager.ADDITION) || test.getOperation().equals(TestManager.SUBTRACTION)) &&
		    		test.getDifficulty() == 2)
		    	cellArray[2] = "Medium";
		    else if((test.getOperation().equals(TestManager.ADDITION) || test.getOperation().equals(TestManager.SUBTRACTION)) &&
		    		test.getDifficulty() == 3)
		    	cellArray[2] = "Hard";
		    else if((test.getOperation().equals(TestManager.ADDITION) || test.getOperation().equals(TestManager.SUBTRACTION)) &&
		    		test.getDifficulty() == 4)
		    	cellArray[2] = "Expert";
		    else if((test.getOperation().equals(TestManager.MULTIPLICATION) || test.getOperation().equals(TestManager.DIVISION)) &&
		    		test.getDifficulty() == 13)
		    	cellArray[2] = "All";
		    else
		    	cellArray[2] = "" + test.getDifficulty();
		    cellArray[3] = test.getOperation();
		    cellArray[4] = test.getTimeAllotted();
		    cellArray[5] = test.getTimeRemaining();
		    cellArray[6] = test.didQuitTest() ? "Yes" : "No";
			cellList.add(cellArray);
			
			Collections.sort(cellList, new Comparator<String[]>() {
				@Override
				public int compare(String[] cell1, String[] cell2) {
					return cell1[0].compareToIgnoreCase(cell2[0]);
				}
		    });
			
			for(int i = 0; i < cellList.size(); i++) {
				String[] stringArray = cellList.get(i);
				
				todaySheetNew.addCell(new Label(0, i + 1, stringArray[0], dataFormat));
			    todaySheetNew.addCell(new Label(1, i + 1, stringArray[1], dataFormat));
			    todaySheetNew.addCell(new Label(2, i + 1, stringArray[2], dataFormat));
			    todaySheetNew.addCell(new Label(3, i + 1, stringArray[3], dataFormat));
			    todaySheetNew.addCell(new Label(4, i + 1, stringArray[4], dataFormat));
			    todaySheetNew.addCell(new Label(5, i + 1, stringArray[5], dataFormat));
			    todaySheetNew.addCell(new Label(6, i + 1, stringArray[6], dataFormat));
			}
		} catch (RowsExceededException e1) {
			e1.printStackTrace();
		} catch (WriteException e1) {
			e1.printStackTrace();
		} 
	    
	    try {
	    	//write temporary file to local storage
		    todayWorkbookNew.write();
			todayWorkbookNew.close();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    //write dbxFile
	    try {
			dbxFile.writeFromExistingFile(file, false);
			dbxTodayFile.writeFromExistingFile(todayFileNew, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//unlink accounts if user turns Dropbox storage off
	public void unlinkAccounts() {
		mAccountManager.unlink();
	}
	
	public static String ordinal(int i) {
	    String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
	    switch (i % 100) {
	    case 11:
	    case 12:
	    case 13:
	        return i + "th";
	    default:
	        return i + sufixes[i % 10];

	    }
	}
}
