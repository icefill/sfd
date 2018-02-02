package com.icefill.game.utils;

import java.util.Collections;
import java.util.LinkedList;


public class NonRepeatRandomizer {
		private LinkedList<LookupElt> look_up=new LinkedList<LookupElt>();
		private int upper_bound;
		private int n;
		private int current_value;
		private int upper_bound_y;
		
		public NonRepeatRandomizer(int upper_bound) {
			this.upper_bound_y=upper_bound;
			n=this.upper_bound=upper_bound;
			for (int i=0;i<upper_bound;i++) {
				look_up.add(new LookupElt(i));
			}
		}
		public NonRepeatRandomizer(int upper_bound_x, int upper_bound_y) {
			n=this.upper_bound=upper_bound_x*upper_bound_y;
			this.upper_bound_y=upper_bound_y;
			for (int x=0;x<upper_bound_x;x++) {
				for (int y=0;y<upper_bound_y;y++) {
					look_up.add(new LookupElt(x*upper_bound_y+y));
				}	
			}
		}
		public NonRepeatRandomizer(int min_x, int upper_bound_x, int min_y, int upper_bound_y) {
			n=this.upper_bound=(upper_bound_x-min_x)*(upper_bound_y-min_y);
			this.upper_bound_y=upper_bound_y;
			for (int x=min_x;x<upper_bound_x;x++) {
				for (int y=min_y;y<upper_bound_y;y++) {
					look_up.add(new LookupElt(x*upper_bound_y+y));
				}	
			}
		}
		
		public int next() {
			if (n==1)
			{
				n=upper_bound;
				current_value= look_up.get(0).value;
				return look_up.get(0).value;
			}
			else {
				int rn = Randomizer.nextInt(n);
				current_value = look_up.get(rn).value;
				Collections.swap(look_up, rn, n - 1);
				n--;
				return current_value;
			}
		}
		public int getX() {
			return current_value/upper_bound_y;
		}
		public int getY() {
			return current_value%upper_bound_y;
		}
		public void reset() {
			n=upper_bound;
		}
		
	public static class LookupElt{
		public int value;
		public LookupElt(int value) {
			this.value=value;
		}
		
	}

}
