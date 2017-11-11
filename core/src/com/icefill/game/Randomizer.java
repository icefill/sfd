package com.icefill.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class Randomizer {
	private static Random rnd= new Random();
	
	public static int nextInt(int upper_bound) {
		if (upper_bound==1) return 0;
		else return rnd.nextInt(upper_bound);
	}
	public static int nextInt(int min_value,int max_value) {
		if (min_value<max_value) {
			return nextInt(max_value+1-min_value)+min_value;
		}
		else if (min_value==max_value) {
			return min_value;
		}
		else {
			return nextInt(min_value+1-max_value)+max_value;
		} 
	}
	
	public static float nextFloat() {
		return rnd.nextFloat();
	}
	public static boolean hitInRatio(float ratio) {
		return rnd.nextFloat()<ratio;
	}
	public static boolean hitInPercent(float percent) {
		return 100*rnd.nextFloat()<percent;
	}
}
