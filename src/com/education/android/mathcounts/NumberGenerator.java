package com.education.android.mathcounts;

import java.util.Random;


public class NumberGenerator {
	public static int[][] getAdditionRandy(int level, int numQuestions) {
		switch(level) {
		case 1:
			return getEasyAddition(numQuestions);
		case 2:
			return getMediumAddition(numQuestions);
		case 3:
			return getHardAddition(numQuestions);
		case 4:
			return getExpertAddition(numQuestions);
		default:
			return getEasyAddition(numQuestions);
		}
	}
	public static int[][] getSubtractionRandy(int level, int numQuestions) {
		switch(level) {
		case 1:
			return getEasySubtraction(numQuestions);
		case 2:
			return getMediumSubtraction(numQuestions);
		case 3:
			return getHardSubtraction(numQuestions);
		case 4:
			return getExpertSubtraction(numQuestions);
		default:
			return getEasySubtraction(numQuestions);
		}
	}
	private static int[][] getEasyAddition(int numQuestions) {
		int[][] questionValues = new int[numQuestions][3];
	
		for(int i = 0; i < numQuestions; i++) {
			do {
				if(getRandom(2) == 0) {
					questionValues[i][0] = getRandom(10) + 1;
					questionValues[i][1] = getRandom(10) + 1;
				} else {
					questionValues[i][0] = getRandom(11);
					questionValues[i][1] = getRandom(11);
				}
				
			} while(checkExists(questionValues, i, numQuestions/10));
			questionValues[i][2] = questionValues[i][0] + questionValues[i][1];
		}
		return questionValues;
	}
	private static int[][] getMediumAddition(int numQuestions ) {
		int[][] questionValues = new int[numQuestions][3];
		
		for(int i = 0; i < numQuestions; i++) {
			do {
				
				if(getRandom(10) > 3) {
					do {
						questionValues[i][0] = getWeightedRandom(15, 6);
					} while(questionValues[i][0] > 15 || questionValues[i][0] < 5);
				} else {
					do {
						questionValues[i][0] = getWeightedRandom(15, 4);
					} while(questionValues[i][0] <= 15 || questionValues[i][0] > 25);
				}
				
				if(getRandom(10) > 3) {
					do {
						questionValues[i][1] = getWeightedRandom(15, 6);
					} while(questionValues[i][1] > 15 || questionValues[i][1] < 5);
				} else {
					do {
						questionValues[i][1] = getWeightedRandom(15, 4);
					} while(questionValues[i][1] <= 15 || questionValues[i][1] > 25);
				}
				
			} while(checkExists(questionValues, i, numQuestions/10));
			questionValues[i][2] = questionValues[i][0] + questionValues[i][1];
		}
		return questionValues;
	}
	private static int[][] getHardAddition(int numQuestions) {
		int[][] questionValues = new int[numQuestions][3];
		
		for(int i = 0; i < numQuestions; i++) {
			do {
				
				if(getRandom(10) > 3) {
					do {
						questionValues[i][0] = getWeightedRandom(40, 40);
					} while(questionValues[i][0] > 40 || questionValues[i][0] < 5);
				} else {
					do {
						questionValues[i][0] = getWeightedRandom(40, 15);
					} while(questionValues[i][0] <= 40);
				}
				
				if(getRandom(10) > 3) {
					do {
						questionValues[i][1] = getWeightedRandom(40, 40);
					} while(questionValues[i][1] > 40 || questionValues[i][1] < 5);
				} else {
					do {
						questionValues[i][1] = getWeightedRandom(40, 15);
					} while(questionValues[i][1] <= 40);
				}
				
			} while(checkExists(questionValues, i, numQuestions/10));
			questionValues[i][2] = questionValues[i][0] + questionValues[i][1];
		}
		return questionValues;
	}
	private static int[][] getExpertAddition(int numQuestions) {
		int mean = 750;
		int[][] questionValues = new int[numQuestions][3];
		
		for(int i = 0; i < numQuestions; i++) {
			do {
				
				if(getRandom(1000) > 1) {
					do {
						questionValues[i][0] = getWeightedRandom(mean, 500);
					} while(questionValues[i][0] > mean || questionValues[i][0] < 50);
				} else {
					do {
						questionValues[i][0] = getWeightedRandom(mean, 40);
					} while(questionValues[i][0] <= mean);
				}
				
				if(getRandom(1000) > 1) {
					do {
						questionValues[i][1] = getWeightedRandom(mean, 500);
					} while(questionValues[i][1] > mean || questionValues[i][1] < 50);
				} else {
					do {
						questionValues[i][1] = getWeightedRandom(mean, 40);
					} while(questionValues[i][1] <= mean);
				}
				
			} while(checkExists(questionValues, i, 0) || (questionValues[i][0] + questionValues[i][1] > 999));
			questionValues[i][2] = questionValues[i][0] + questionValues[i][1];
		}
		return questionValues;
	}
	private static int[][] getEasySubtraction(int numQuestions) {
		int[][] questionValues = new int[numQuestions][3];
		int firstNum;
		int secondNum;
		
		for(int i = 0; i < numQuestions; i++) {
			do {
				if(getRandom(2) == 0) {
					firstNum = getRandom(10) + 1;
					secondNum = getRandom(10) + 1;
				} else {
					firstNum = getRandom(11);
					secondNum = getRandom(11);
				}
				
			} while(checkExists(questionValues, i, firstNum, secondNum, numQuestions/10));
			
			questionValues[i][0] = firstNum + secondNum;
			questionValues[i][1] = firstNum;
			questionValues[i][2] = secondNum;
		}
		return questionValues;
	}
	private static int[][] getMediumSubtraction(int numQuestions ) {
		int[][] questionValues = new int[numQuestions][3];
		int firstNum;
		int secondNum;
		
		for(int i = 0; i < numQuestions; i++) {
			do {
				
				if(getRandom(10) > 2) {
					do {
						firstNum = getWeightedRandom(15, 6);
					} while(firstNum > 15 || firstNum < 5);
				} else {
					do {
						firstNum = getWeightedRandom(15, 4);
					} while(firstNum <= 15);
				}
				
				if(getRandom(10) > 2) {
					do {
						secondNum = getWeightedRandom(15, 6);
					} while(secondNum > 15 || secondNum < 5);
				} else {
					do {
						secondNum = getWeightedRandom(15, 4);
					} while(secondNum <= 15);
				}
				
			} while(checkExists(questionValues, i, firstNum, secondNum, numQuestions/10));
			
			questionValues[i][0] = firstNum + secondNum;
			questionValues[i][1] = firstNum;
			questionValues[i][2] = secondNum;
		}
		return questionValues;
	}
	private static int[][] getHardSubtraction(int numQuestions) {
		int[][] questionValues = new int[numQuestions][3];
		int firstNum;
		int secondNum;
		
		for(int i = 0; i < numQuestions; i++) {
			do {
				
				if(getRandom(10) > 2) {
					do {
						firstNum = getWeightedRandom(50, 35);
					} while(firstNum > 50 || firstNum < 5);
				} else {
					do {
						firstNum = getWeightedRandom(50, 10);
					} while(firstNum <= 50);
				}
				
				if(getRandom(10) > 2) {
					do {
						secondNum = getWeightedRandom(50, 35);
					} while(secondNum > 50 || secondNum < 5);
				} else {
					do {
						secondNum = getWeightedRandom(50, 10);
					} while(secondNum <= 50);
				}
				
			} while(checkExists(questionValues, i, firstNum, secondNum, numQuestions/10));
			
			questionValues[i][0] = firstNum + secondNum;
			questionValues[i][1] = firstNum;
			questionValues[i][2] = secondNum;
		}
		return questionValues;
	}
	private static int[][] getExpertSubtraction(int numQuestions) {
		int mean = 750;
		int[][] questionValues = new int[numQuestions][3];
		
		int firstNum;
		int secondNum;
		
		for(int i = 0; i < numQuestions; i++) {
			do {
				
				if(getRandom(1000) > 1) {
					do {
						firstNum = getWeightedRandom(mean, 500);
					} while(firstNum > mean || firstNum < 50);
				} else {
					do {
						firstNum = getWeightedRandom(mean, 40);
					} while(firstNum <= mean);
				}
				
				if(getRandom(1000) > 1) {
					do {
						secondNum = getWeightedRandom(mean, 500);
					} while(secondNum > mean || secondNum < 50);
				} else {
					do {
						secondNum = getWeightedRandom(mean, 40);
					} while(secondNum <= mean);
				}
				
			} while(checkExists(questionValues, i, firstNum, secondNum, 0) || (firstNum + secondNum > 999));
			
			questionValues[i][0] = firstNum + secondNum;
			questionValues[i][1] = firstNum;
			questionValues[i][2] = secondNum;
		}
		return questionValues;
	}
	
	public static int[][] getMultiplicationRandy(int level, int numQuestions) {
		int[][] questionValues = new int[numQuestions][3];
		
		int firstNum;
		int secondNum;
		int repeats = numQuestions / 10;
		
		if(level == 13) {
		
			for(int i = 0; i < numQuestions; i++) {
				do {
					firstNum = getRandom(11) + 2;
					secondNum = getRandom(11) + 2;
				} while(checkExists(questionValues, i, firstNum, secondNum, repeats));
				
				questionValues[i][0] = firstNum;
				questionValues[i][1] = secondNum;
				questionValues[i][2] = firstNum * secondNum;
			}
		} else {
		
			for(int i = 0; i < numQuestions; i++) {
				do {
						
					if(getRandom(2) == 0) {
						firstNum = level;
						
						if(getRandom(10) > 1)
							secondNum = getRandom(11) + 2;
						else
							secondNum = getRandom(13);
					} else {
						
						if(getRandom(10) > 1)
							firstNum = getRandom(11) + 2;
						else
							firstNum = getRandom(13);
						
						secondNum = level;
					}
					
				} while(checkExists(questionValues, i, firstNum, secondNum, repeats));
				
				questionValues[i][0] = firstNum;
				questionValues[i][1] = secondNum;
				questionValues[i][2] = firstNum * secondNum;
			}
		}
		
		return questionValues;
	}
	
	public static int[][] getChallengeMultiplication(int numQuestions) {
		int[][] questionValues = new int[numQuestions][3];
		int firstNum = 0;
		int secondNum = 0;
		int answer = 0;
		
		for(int i = 0; i < numQuestions; i++) {
			while(answer > 200 || answer < 50) {
				firstNum = getRandom(23) + 3;
				secondNum = getRandom(23) + 3;
				
				answer = firstNum * secondNum;
			}
			
			questionValues[i][0] = firstNum;
			questionValues[i][1] = secondNum;
			questionValues[i][2] = answer;
		}
		
		return questionValues;	
	} 
	
	public static int[][] getDivisionRandy(int level, int numQuestions) {
		int[][] questionValues = new int[numQuestions][3];
		
		int firstNum;
		int secondNum;
		int repeats = numQuestions / 12;
		
		if(level == 13) {
			for(int i = 0; i < numQuestions; i++) {
				do {
					firstNum = getRandom(11) + 2;
					secondNum = getRandom(11) + 2;
				} while(checkExists(questionValues, i, firstNum * secondNum, secondNum, repeats));
				
				questionValues[i][0] = firstNum * secondNum;
				questionValues[i][1] = secondNum;
				questionValues[i][2] = firstNum;
			}
			
		} else {
			for(int i = 0; i < numQuestions; i++) {
				do {
					secondNum = level;
					
					if(getRandom(10) > 1)
						firstNum = getRandom(11) + 2;
					else
						firstNum = getRandom(12) + 1;
	
				} while(checkExists(questionValues, i, firstNum * secondNum, secondNum, repeats));
				
				questionValues[i][0] = firstNum * secondNum;
				questionValues[i][1] = secondNum;
				questionValues[i][2] = firstNum;
			}
		}
		
		return questionValues;
	}
	
	public static int[][] getChallengeDivision(int numQuestions) {
		int[][] questionValues = new int[numQuestions][3];
		int[][] question = getChallengeMultiplication(numQuestions);
		
		for(int i = 0; i < numQuestions; i++) {
			questionValues[i][0] = question[i][2];
			questionValues[i][1] = question[i][1];
			questionValues[i][2] = question[i][0];
		}
		
		return questionValues;
	}
	
	public static int[][] getOperationsChallengeMode() {
		int[][] questionValues = new int[4][3];
		
		for(int i = 0; i< questionValues.length; i++) {
			if(i == 0) {
				int[][] question = getExpertAddition(1);
				questionValues[i][0] = question[0][0];
				questionValues[i][1] = question[0][1];
				questionValues[i][2] = question[0][2];
			}
			if(i == 1) {
				int[][] question = getExpertSubtraction(1);
				questionValues[i][0] = question[0][0];
				questionValues[i][1] = question[0][1];
				questionValues[i][2] = question[0][2];
			}
			if(i == 2) {
				int[][] question = getChallengeMultiplication(1);
				questionValues[i][0] = question[0][0];
				questionValues[i][1] = question[0][1];
				questionValues[i][2] = question[0][2];
			}
			if(i == 3) {
				int[][] question = getChallengeDivision(1);
				questionValues[i][0] = question[0][0];
				questionValues[i][1] = question[0][1];
				questionValues[i][2] = question[0][2];
			}
		}
		return questionValues;
	}
	
	public static String[][] getOrderChallengeMode() {
		String[][] order = new String[6][2];
		
		for(int i = 0; i < order.length; i++) {
			Random rand = new Random();
			int answer = 0;
			int first = 0;
			int second = 0;
			int third = 0;
			int fourth = 0;
			
			if(i == 0) {
				int questionType = rand.nextInt(3);
				
				if(questionType == 0) {
					third = rand.nextInt(26) + 10;
					
					while(answer <  50 || answer > 150) {
						first = rand.nextInt(18) + 3;
						second = rand.nextInt(18) + 3;
						
						answer = first * second;
					}
					
					answer += third;
					
					order[i][0] = first + " x " + second + " + " + third;
					order[i][1] = Integer.toString(answer);
				} else if(questionType == 1) {
					
					third = rand.nextInt(26) + 10;
					
					while(answer <  50 || answer > 200) {
						first = rand.nextInt(18) + 3;
						second = rand.nextInt(18) + 3;
						
						answer = first * second;
					}
					
					answer -= third;
					
					order[i][0] = first + " x " + second + " - " + third;
					order[i][1] = Integer.toString(answer);
				} else {
					third = rand.nextInt(26) + 10;
					
					while(answer <  50 || answer > 200) {
						first = rand.nextInt(18) + 3;
						second = rand.nextInt(18) + 3;
						
						answer = first * second;
					}
					
					answer += third;
					
					order[i][0] = third + " + " + second + " x " + first;
					order[i][1] = Integer.toString(answer);
					
				} 
				
			} else if(i == 1) {
				int questionType = rand.nextInt(2); 
				
				if(questionType == 0) {
					
					do {
						first = rand.nextInt(31) + 20;
						second = rand.nextInt(31) + 20;
					} while((first + second) < 50); 
					
					third = rand.nextInt(6) + 3;
					
					while((first + second) % third != 0) {
						second--;
					}
					
					answer = (first + second) / third;
					
					order[i][0] = "(" + first + " + " + second + ") / " + third;
					order[i][1] = Integer.toString(answer);
				} else {
					do {
						first = rand.nextInt(26) + 25;
						int temp = rand.nextInt(11) + 5;
						
						second = first - temp;
				
						third = rand.nextInt(11) + 5;
						answer = (first - second) * third;
					} while(answer < 30);
					
					order[i][0] = "(" + first + " - " + second + ") x " + third;
					order[i][1] = Integer.toString(answer);
				}
			} else if(i == 2) {
				int questionType = rand.nextInt(2);
				
				do {
					first = rand.nextInt(11) + 5;
					second = rand.nextInt(11) + 5;
				} while((first * second) < 25 || (first * second) > 100);
				
				do {
					fourth = rand.nextInt(7) + 3;
					third = fourth * (rand.nextInt(11) + 5);
				} while(third < 30);
				
				answer = first * second + third / fourth;
				
				if(questionType == 0) {
					order[i][0] = first + " x " + second + " + " + third + " / " + fourth;
					order[i][1] = Integer.toString(answer);
				} else {
					order[i][0] = third + " / " + fourth + " + " + first + " x " + second;
					order[i][1] = Integer.toString(answer);
				}
				
			} else if(i == 3) {
				do {
					int temp = rand.nextInt(16) + 5;
					
					first = rand.nextInt(26) + 25;
					second = first - temp;
				
					third = rand.nextInt(11) + 5;
					fourth = rand.nextInt(11) + 5;
					
					answer = (first - second) * (third + fourth);
				} while(third + fourth > 30 || answer > 200);
				
				order[i][0] = "(" + first + " - " + second + ") x (" + third + " + " + fourth + ")";
				order[i][1] = Integer.toString(answer);
			} else if(i == 4) {
				int numerator = 0;
				int denomonator = 0;
				do {
					int temp = rand.nextInt(10) + 3;
					
					first = rand.nextInt(51) + 25;
					second = first - temp;
					denomonator = first - second;
				
					third = rand.nextInt(51) + 20;
					fourth = rand.nextInt(51) + 20;
					numerator = third + fourth;
					
					answer = (third + fourth) / (first - second);
				} while(numerator > 144 || denomonator > 12 || numerator % denomonator != 0);
				
				order[i][0] = "(" + third + " + " + fourth + ") / (" + first + " - " + second + ")";
				order[i][1] = Integer.toString(answer);
			} else {
				answer = (int) rand.nextInt(7) + 13;
				
				order[i][0] = "" + (answer * answer);
				order[i][1] = "" + answer;
			}
		}
		
		return order;
	}
	
	public static String[] getMultiplicationMadness() {
		String[] question = new String[2];
		Random rand = new Random();
		
		int first = 0; 
		int second = 0;
		int third = 0;
		int answer = 0;
		
		int questionType = rand.nextInt(4);
		
		if(questionType == 0) {
			do {
				first = rand.nextInt(16) + 5;
				second = rand.nextInt(16) + 5;
				third = rand.nextInt(13) + 3;
				answer = (first + second) * third;
			} while(answer < 50 || answer > 200);
			
			question[0] = "(" + first + " + " + second + ") x " + third + " =";
			question[1] = Integer.toString(answer);
		} else if(questionType == 1) {
			int temp = 0;
			do {
				first = rand.nextInt(21) + 10;
				temp = rand.nextInt(13) + 3;
				second = first - temp;
				third = rand.nextInt(13) + 3;
				answer = (first - second) * third;
			} while(answer < 50 || answer > 200 || second < 1);
			
			question[0] = "(" + first + " - " + second + ") x " + third + " =";
			question[1] = Integer.toString(answer);
		} else if(questionType == 2) {
			do {
				first = rand.nextInt(16) + 5;
				second = rand.nextInt(16) + 5;
				third = rand.nextInt(13) + 3;
				answer = third * (first + second);
			} while(answer < 50 || answer > 200);
			
			question[0] = third + " x (" + first + " + " + second + ") =";
			question[1] = Integer.toString(answer);
		} else {
			int temp = 0;
			do {
				first = rand.nextInt(21) + 10;
				temp = rand.nextInt(13) + 3;
				second = first - temp;
				third = rand.nextInt(13) + 3;
				answer = third * (first - second);
			} while(answer < 50 || answer > 200 || second < 1);
			
			question[0] = third + " x (" + first + " - " + second + ") =";
			question[1] = Integer.toString(answer);
		}
		
		return question;
	}

	public static int[] getSquareRoot() {
		Random rand = new Random();
		
		int answer = rand.nextInt(21);
		int question = (int) Math.pow(answer, 2);
		
		return new int[] {question, answer};
	}
	
	public static int[] getSquare() {
		Random rand = new Random();
		
		int question = rand.nextInt(21);
		int answer = (int) Math.pow(question, 2);
		
		return new int[] {question, answer};
	}
	
	/**
	 * A percentage question is generated.
	 * The amount will be 1-10 or 15-100 by 5.
	 * The percentage will be 5-95 by 5. If the amount has a 5 in the ones place (e.g. 65), the percentage will be 10-90 by 10 to prevent a difficult calculation.
	 * @return a array of ints with length 3 containing the amount, percentage, and answer
	 */
	public static int[] getPercentage() {
		Random rand = new Random();
		
		
		int amount; 			//the amount the percentage will be taken from					
		int percentage;		//the percentage multiplied by the amount	
		double answer;			//answer to the generated question
		
		//there are 28 possible questions: 1-10 or 15-100 by 5
		//this will evenly distribute the likelihood for each possible amount
		if(rand.nextInt(28) < 10) {	 					
			//generates an amount 1-10
			amount = rand.nextInt(10) + 1;	
		} else {
			//generates an amount 15-100 by 5
			amount = (rand.nextInt(18) + 3) * 5;		
		}
		
		//if the amount does not have a 5 in the ones place
		//this is to prevent making a difficult calculation e.g. 35% of 75
		if((amount / 5) % 2 == 0) {					
			//the percentage will be 5-95 by 5
			percentage = (rand.nextInt(18) + 1) * 5;
		} else {
			//the percentage will be 10-90 by 10
			percentage = (rand.nextInt(9) + 1) * 10;
		}
		
		answer = amount * percentage / 100.0;
		
		if(answer % 1 != 0)
			return getPercentage();
		
		return new int[] {amount, percentage, (int) answer};
	}
	
	public static int[] getTwoMinuteDrill(int position) {
		int operation = position % 4;
		switch(operation) {
		case 0: 
			int[][] additionQuestion = getMediumAddition(1);
			return new int[] {additionQuestion[0][0], additionQuestion[0][1], additionQuestion[0][2]};
		case 1:
			int[][] subtractionQuestion = getMediumSubtraction(1);
			return new int[] {subtractionQuestion[0][0], subtractionQuestion[0][1], subtractionQuestion[0][2]};
		case 2:
			int[][] multiplicationQuestion = getMultiplicationRandy(13, 1);
			return new int[] {multiplicationQuestion[0][0], multiplicationQuestion[0][1], multiplicationQuestion[0][2]};
		case 3:
			int[][] divisionQuestion = getDivisionRandy(13, 1);
			return new int[] {divisionQuestion[0][0], divisionQuestion[0][1], divisionQuestion[0][2]};
		default:
				return new int[] {0, 0, 0};
		}
	}
	
	private static boolean checkExists(int[][] questionValues, int index, int repeats) {
		int counter = 0;
		
		int firstNum = questionValues[index][0];
		int secondNum = questionValues[index][1];
		
		for(int i = 0; i < index; i++) {
			if(questionValues[i][0] == firstNum || questionValues[i][1] == secondNum) {
				counter++;
			}
		}
		if(counter > repeats) 
			return true;
		return false;
	}
	
	private static boolean checkExists(int[][] questionValues, int index, int firstNum, int secondNum, int repeats) {
		int counter = 0;
		
		for(int i = 0; i < index; i++) {
			if(questionValues[i][0] == firstNum && questionValues[i][1] == secondNum) {
				if(i == (index - 1)) {
					return true;
				}
				counter++;
			}
			if(counter > repeats) {
				return true;
			}
		}
		return false;
	}
	private static int getRandom(int seed) {
		Random rand = new Random();
		return rand.nextInt(seed);
	}
	private static int getWeightedRandom(int mean, int stdDeviation) {
		return (int) Math.round(getNormalDist()*stdDeviation + mean);
	}
	private static double getNormalDist() {
		Random rand = new Random();
		return (rand.nextDouble()*2 - 1) + (rand.nextDouble()*2 - 1) + (rand.nextDouble()*2 - 1);
	}
}